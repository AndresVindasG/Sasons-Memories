package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.Path2D;

/** Campo de contraseña con control para mostrar u ocultar el texto a solicitud. */
public class CampoContrasena extends JPanel {
    private final CampoPasswordInterno campoContrasena = new CampoPasswordInterno();
    private final JButton botonVisibilidad = new JButton();
    private final char caracterOculto;
    private boolean textoVisible;

    public CampoContrasena() {
        super(new BorderLayout());
        setOpaque(true);
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(222, 215, 205), 1),
                new EmptyBorder(0, 0, 0, 3)));
        caracterOculto = campoContrasena.getEchoChar();
        campoContrasena.setBorder(new EmptyBorder(0, 13, 0, 7));
        campoContrasena.setOpaque(false);
        campoContrasena.establecerIndicador("••••••••");

        botonVisibilidad.setIcon(new IconoOjo(false));
        botonVisibilidad.setToolTipText("Mostrar contraseña");
        botonVisibilidad.getAccessibleContext().setAccessibleName("Mostrar contraseña");
        botonVisibilidad.setBorderPainted(false);
        botonVisibilidad.setContentAreaFilled(false);
        botonVisibilidad.setFocusPainted(false);
        botonVisibilidad.setOpaque(false);
        botonVisibilidad.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        botonVisibilidad.setPreferredSize(new Dimension(42, 38));
        botonVisibilidad.addActionListener(evento -> alternarVisibilidad());

        add(campoContrasena, BorderLayout.CENTER);
        add(botonVisibilidad, BorderLayout.EAST);
        setPreferredSize(new Dimension(430, 42));
        setMinimumSize(new Dimension(430, 42));
        setMaximumSize(new Dimension(430, 42));
    }

    public char[] getPassword() {
        return campoContrasena.getPassword();
    }

    public void setText(String texto) {
        campoContrasena.setText(texto);
    }

    public void establecerIndicador(String indicador) {
        campoContrasena.establecerIndicador(indicador);
    }

    public void configurar(Font fuente, Color colorTexto, Color colorFondo) {
        campoContrasena.setFont(fuente);
        campoContrasena.setForeground(colorTexto);
        campoContrasena.setCaretColor(colorTexto);
        campoContrasena.setBackground(colorFondo);
        setBackground(colorFondo);
        botonVisibilidad.setBackground(colorFondo);
    }

    private void alternarVisibilidad() {
        int posicionCursor = campoContrasena.getCaretPosition();
        textoVisible = !textoVisible;
        campoContrasena.setEchoChar(textoVisible ? (char) 0 : caracterOculto);
        botonVisibilidad.setIcon(new IconoOjo(textoVisible));
        String descripcion = textoVisible ? "Ocultar contraseña" : "Mostrar contraseña";
        botonVisibilidad.setToolTipText(descripcion);
        botonVisibilidad.getAccessibleContext().setAccessibleName(descripcion);
        campoContrasena.setCaretPosition(Math.min(posicionCursor, campoContrasena.getDocument().getLength()));
        campoContrasena.requestFocusInWindow();
    }

    private static final class CampoPasswordInterno extends JPasswordField {
        private String indicador = "";

        void establecerIndicador(String texto) {
            indicador = texto == null ? "" : texto;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics graficos) {
            super.paintComponent(graficos);
            if (getPassword().length == 0 && !hasFocus() && !indicador.isEmpty()) {
                graficos.setColor(new Color(165, 174, 181));
                graficos.drawString(indicador, getInsets().left,
                        (getHeight() - getFontMetrics(getFont()).getHeight()) / 2
                                + getFontMetrics(getFont()).getAscent());
            }
        }
    }

    private static final class IconoOjo implements Icon {
        private final boolean tachado;

        IconoOjo(boolean tachado) {
            this.tachado = tachado;
        }

        @Override public int getIconWidth() { return 20; }
        @Override public int getIconHeight() { return 20; }

        @Override
        public void paintIcon(Component componente, Graphics graficos, int x, int y) {
            Graphics2D lienzo = (Graphics2D) graficos.create();
            lienzo.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            lienzo.setColor(new Color(75, 86, 93));
            lienzo.setStroke(new BasicStroke(1.7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            Path2D ojo = new Path2D.Double();
            ojo.moveTo(x + 1, y + 10);
            ojo.curveTo(x + 5, y + 4, x + 15, y + 4, x + 19, y + 10);
            ojo.curveTo(x + 15, y + 16, x + 5, y + 16, x + 1, y + 10);
            lienzo.draw(ojo);
            lienzo.drawOval(x + 7, y + 7, 6, 6);
            if (tachado) lienzo.drawLine(x + 3, y + 17, x + 17, y + 3);
            lienzo.dispose();
        }
    }
}
