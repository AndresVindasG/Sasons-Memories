package model;

import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.RectVector;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;

import javax.imageio.ImageIO;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGR2GRAY;
import static org.bytedeco.opencv.global.opencv_imgproc.cvtColor;
import static org.bytedeco.opencv.global.opencv_imgproc.equalizeHist;
import static org.bytedeco.opencv.global.opencv_videoio.CAP_DSHOW;
import static org.bytedeco.opencv.global.opencv_videoio.CAP_PROP_FRAME_HEIGHT;
import static org.bytedeco.opencv.global.opencv_videoio.CAP_PROP_FRAME_WIDTH;

/** Lee fotogramas de la cámara y detecta un rostro en tiempo real con el clasificador Haar de OpenCV. */
public class CapturadorCamara implements AutoCloseable {
    private final VideoCapture camara;
    private final CascadeClassifier detectorRostros;
    private final Path archivoClasificador;
    private volatile FotogramaActual fotogramaActual;
    private volatile String errorCamara;
    private volatile boolean activo = true;
    private final Thread hiloCamara;

    public CapturadorCamara() throws IOException {
        archivoClasificador = copiarClasificador();
        detectorRostros = new CascadeClassifier(archivoClasificador.toString().replace('\\', '/'));
        if (detectorRostros.empty()) throw new IOException("No se pudo cargar el detector facial de OpenCV.");

        camara = new VideoCapture();
        boolean abierta = camara.open(0, CAP_DSHOW);
        if (!abierta) abierta = camara.open(0);
        if (!abierta) {
            camara.release();
            throw new IOException("No se pudo abrir la cámara. Revisa que esté conectada y habilitada para aplicaciones de escritorio.");
        }
        camara.set(CAP_PROP_FRAME_WIDTH, 640);
        camara.set(CAP_PROP_FRAME_HEIGHT, 480);
        hiloCamara = new Thread(this::leerFotogramas, "captura-camara-facial");
        hiloCamara.setDaemon(true);
        hiloCamara.start();
    }

    public FotogramaActual obtenerFotogramaActual() { return fotogramaActual; }
    public String obtenerErrorCamara() { return errorCamara; }

    public BufferedImage capturarRostroActual() {
        FotogramaActual fotograma = fotogramaActual;
        if (fotograma == null || fotograma.rostro == null) return null;
        Rectangle area = fotograma.rostro;
        BufferedImage rostro = new BufferedImage(area.width, area.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graficos = rostro.createGraphics();
        graficos.drawImage(fotograma.imagenOriginal, 0, 0, area.width, area.height,
                area.x, area.y, area.x + area.width, area.y + area.height, null);
        graficos.dispose();
        return rostro;
    }

    private void leerFotogramas() {
        Mat fotograma = new Mat();
        Mat escalaGrises = new Mat();
        RectVector detecciones = new RectVector();
        while (activo) {
            try {
                if (!camara.read(fotograma) || fotograma.empty()) {
                    Thread.sleep(25);
                    continue;
                }
                cvtColor(fotograma, escalaGrises, COLOR_BGR2GRAY);
                equalizeHist(escalaGrises, escalaGrises);
                detectorRostros.detectMultiScale(escalaGrises, detecciones);

                BufferedImage imagenOriginal = convertirImagen(fotograma);
                Rectangle rostroDetectado = rostroMasGrande(detecciones, imagenOriginal.getWidth(), imagenOriginal.getHeight());
                BufferedImage imagenPrevia = copiarImagen(imagenOriginal);
                if (rostroDetectado != null) {
                    Graphics2D graficos = imagenPrevia.createGraphics();
                    graficos.setColor(new Color(118, 166, 190));
                    graficos.setStroke(new BasicStroke(3));
                    graficos.drawRect(rostroDetectado.x, rostroDetectado.y, rostroDetectado.width, rostroDetectado.height);
                    graficos.dispose();
                }
                fotogramaActual = new FotogramaActual(imagenPrevia, imagenOriginal, rostroDetectado);
                errorCamara = null;
            } catch (Exception excepcion) {
                errorCamara = "La cámara dejó de responder: " + excepcion.getMessage();
                try { Thread.sleep(100); } catch (InterruptedException interrupcion) { Thread.currentThread().interrupt(); break; }
            }
        }
        fotograma.release();
        escalaGrises.release();
        detecciones.close();
    }

    private Rectangle rostroMasGrande(RectVector detecciones, int ancho, int alto) {
        Rectangle mayor = null;
        long areaMayor = 0;
        for (long indice = 0; indice < detecciones.size(); indice++) {
            Rect rostro = detecciones.get(indice);
            Rectangle area = new Rectangle(rostro.x(), rostro.y(), rostro.width(), rostro.height());
            area = area.intersection(new Rectangle(0, 0, ancho, alto));
            long areaActual = (long) area.width * area.height;
            if (areaActual > areaMayor) { mayor = area; areaMayor = areaActual; }
        }
        detecciones.clear();
        return mayor;
    }

    private BufferedImage convertirImagen(Mat fotograma) {
        Mat imagenBgr = new Mat();
        fotograma.copyTo(imagenBgr);
        if (imagenBgr.channels() == 4) {
            Mat temporal = new Mat();
            org.bytedeco.opencv.global.opencv_imgproc.cvtColor(imagenBgr, temporal,
                    org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGRA2BGR);
            imagenBgr.release();
            imagenBgr = temporal;
        }
        int ancho = imagenBgr.cols();
        int alto = imagenBgr.rows();
        int canales = imagenBgr.channels();
        byte[] datos = new byte[ancho * alto * canales];
        imagenBgr.data().get(datos);
        int[] pixelesRgb = new int[ancho * alto];
        for (int indicePixel = 0; indicePixel < pixelesRgb.length; indicePixel++) {
            int indiceCanal = indicePixel * canales;
            int azul = datos[indiceCanal] & 0xff;
            int verde = canales > 1 ? datos[indiceCanal + 1] & 0xff : azul;
            int rojo = canales > 2 ? datos[indiceCanal + 2] & 0xff : azul;
            pixelesRgb[indicePixel] = (rojo << 16) | (verde << 8) | azul;
        }
        BufferedImage imagen = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
        imagen.setRGB(0, 0, ancho, alto, pixelesRgb, 0, ancho);
        imagenBgr.release();
        return imagen;
    }
    private BufferedImage copiarImagen(BufferedImage original) {
        BufferedImage copia = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graficos = copia.createGraphics();
        graficos.drawImage(original, 0, 0, null);
        graficos.dispose();
        return copia;
    }

    private Path copiarClasificador() throws IOException {
        String rutaRecurso = "/recursos/haarcascade_frontalface_default.xml";
        try (InputStream recurso = CapturadorCamara.class.getResourceAsStream(rutaRecurso)) {
            if (recurso != null) {
                Path archivoTemporal = Files.createTempFile("memory-game-haarcascade-", ".xml");
                Files.copy(recurso, archivoTemporal, StandardCopyOption.REPLACE_EXISTING);
                archivoTemporal.toFile().deleteOnExit();
                return archivoTemporal;
            }
        }

        Path carpetaActual = Path.of("").toAbsolutePath();
        while (carpetaActual != null) {
            Path[] rutasAlternativas = {
                    carpetaActual.resolve("resources/recursos/haarcascade_frontalface_default.xml"),
                    carpetaActual.resolve("src/recursos/haarcascade_frontalface_default.xml")
            };
            for (Path rutaAlternativa : rutasAlternativas) {
                if (Files.isRegularFile(rutaAlternativa)) return rutaAlternativa;
            }
            carpetaActual = carpetaActual.getParent();
        }
        throw new IOException("No se encontró el clasificador facial. Verifica que resources/recursos/haarcascade_frontalface_default.xml esté incluido en el proyecto.");
    }
    @Override
    public void close() {
        activo = false;
        camara.release();
        try { hiloCamara.join(1200); } catch (InterruptedException excepcion) { Thread.currentThread().interrupt(); }
        detectorRostros.close();
    }

    public static class FotogramaActual {
        private final BufferedImage imagenPrevia;
        private final BufferedImage imagenOriginal;
        private final Rectangle rostro;
        public FotogramaActual(BufferedImage imagenPrevia, BufferedImage imagenOriginal, Rectangle rostro) {
            this.imagenPrevia = imagenPrevia;
            this.imagenOriginal = imagenOriginal;
            this.rostro = rostro;
        }
        public BufferedImage obtenerImagenPrevia() { return imagenPrevia; }
        public boolean tieneRostro() { return rostro != null; }
    }
}