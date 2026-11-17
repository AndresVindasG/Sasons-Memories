package view;

// Importa los componentes necesarios para construir interfaces gráficas.
import javax.swing.*;

// Importa herramientas para manejar dimensiones, posiciones y gráficos.
import java.awt.*;

/**
 * Matriz 6×6 que conserva cartas cuadradas de 128 px cuando hay espacio.
 *
 * Esta clase representa el tablero principal del juego.
 * Se encarga de crear las 36 cartas, organizarlas en una matriz
 * de 6 filas y 6 columnas y ajustar su tamaño según el espacio disponible.
 *
 * Hereda de JPanel porque representa un panel de la interfaz gráfica.
 */
public final class TableroCartas extends JPanel {

    // Cantidad total de cartas que tendrá el tablero.
    // Al ser static, pertenece a la clase y no a cada objeto.
    // Al ser final, su valor no puede modificarse.
    public static final int CANTIDAD_CARTAS = 36;

    // Tamaño preferido de cada carta en píxeles.
    // Las cartas tendrán este tamaño siempre que exista espacio suficiente.
    public static final int TAMANO_CARTA_PREFERIDO = 128;

    // Separación en píxeles entre una carta y otra.
    // Es un atributo privado porque solamente se utiliza dentro de esta clase.
    private static final int SEPARACION = 8;

    // Margen interno que se deja alrededor de la matriz de cartas.
    private static final int MARGEN = 16;

    // Arreglo que almacena los 36 objetos de tipo FichaJuego.
    // Cada posición representa una carta del tablero.
    private final FichaJuego[] cartas = new FichaJuego[CANTIDAD_CARTAS];


    // ---------------------------------------------------------
    // CONSTRUCTOR DEL TABLERO
    // ---------------------------------------------------------

    // Constructor encargado de crear las cartas y agregarlas al tablero.
    public TableroCartas() {

        // Hace que el fondo del panel sea transparente.
        setOpaque(false);

        // Establece un administrador de distribución nulo.
        // Esto significa que las posiciones y dimensiones de las cartas
        // se establecerán manualmente mediante setBounds().
        setLayout(null);


        // Recorre todas las posiciones disponibles del arreglo de cartas.
        // El ciclo se ejecuta 36 veces, desde el índice 0 hasta el 35.
        for (int indice = 0; indice < cartas.length; indice++) {

            // Crea una nueva carta y la almacena en la posición actual.
            cartas[indice] = new FichaJuego();

            // Establece el tamaño preferido de la carta.
            // Su ancho y alto iniciales son de 128 píxeles.
            // De esta manera se busca conservar su forma cuadrada.
            cartas[indice].setPreferredSize(
                    new Dimension(
                            TAMANO_CARTA_PREFERIDO,
                            TAMANO_CARTA_PREFERIDO
                    )
            );

            // Establece el tamaño mínimo permitido para una carta.
            // Evita que se reduzca por debajo de 44 x 44 píxeles.
            cartas[indice].setMinimumSize(new Dimension(44, 44));

            // Establece un nombre accesible para cada carta.
            // Esto ayuda a las tecnologías de asistencia a identificar
            // cada componente de manera individual.
            //
            // indice + 1 permite que las cartas se identifiquen
            // desde la carta 1 hasta la carta 36.
            cartas[indice].getAccessibleContext().setAccessibleName(
                    "Carta " + (indice + 1)
            );

            // Agrega la carta actual al panel del tablero.
            add(cartas[indice]);
        }
    }


    // ---------------------------------------------------------
    // MÉTODO PARA OBTENER UNA CARTA
    // ---------------------------------------------------------

    // Recibe el índice de una carta y devuelve el objeto correspondiente.
    // Permite que otras clases puedan acceder a una carta específica.
    public FichaJuego obtenerCarta(int indice) {

        // Devuelve la carta almacenada en la posición indicada del arreglo.
        return cartas[indice];
    }


    // ---------------------------------------------------------
    // TAMAÑO PREFERIDO DEL TABLERO
    // ---------------------------------------------------------

    // Sobrescribe el método getPreferredSize de JPanel.
    // Permite establecer el tamaño ideal que debería tener el tablero.
    @Override
    public Dimension getPreferredSize() {

        // Calcula el tamaño total de la matriz.
        //
        // TAMANO_CARTA_PREFERIDO * 6:
        // Representa el espacio ocupado por las seis cartas de cada fila.
        //
        // SEPARACION * 5:
        // Representa los cinco espacios que existen entre seis cartas.
        //
        // MARGEN * 2:
        // Representa los márgenes izquierdo y derecho,
        // o superior e inferior.
        int dimensionMatriz =
                TAMANO_CARTA_PREFERIDO * 6
                        + SEPARACION * 5
                        + MARGEN * 2;

        // Devuelve las dimensiones preferidas del tablero.
        // Como es una matriz cuadrada, el ancho y el alto son iguales.
        return new Dimension(dimensionMatriz, dimensionMatriz);
    }


    // ---------------------------------------------------------
    // DISTRIBUCIÓN Y POSICIONAMIENTO DE LAS CARTAS
    // ---------------------------------------------------------

    // Sobrescribe doLayout(), método encargado de organizar
    // las posiciones y dimensiones de los componentes del panel.
    @Override
    public void doLayout() {

        // Calcula el espacio horizontal disponible para las seis cartas.
        //
        // getWidth(): obtiene el ancho actual del tablero.
        // MARGEN * 2: descuenta los márgenes laterales.
        // SEPARACION * 5: descuenta los cinco espacios entre cartas.
        //
        // Math.max(1, ...) garantiza que el resultado sea al menos 1.
        int espacioAncho = Math.max(
                1,
                getWidth() - MARGEN * 2 - SEPARACION * 5
        );

        // Calcula el espacio vertical disponible para las seis filas.
        // Se aplica el mismo procedimiento que para el ancho.
        int espacioAlto = Math.max(
                1,
                getHeight() - MARGEN * 2 - SEPARACION * 5
        );


        // Determina el tamaño final de cada carta.
        //
        // espacioAncho / 6:
        // Calcula cuánto espacio horizontal corresponde a cada carta.
        //
        // espacioAlto / 6:
        // Calcula cuánto espacio vertical corresponde a cada carta.
        //
        // Math.min:
        // Selecciona el menor valor para evitar que las cartas
        // sobrepasen el espacio disponible.
        //
        // TAMANO_CARTA_PREFERIDO:
        // Evita que las cartas superen los 128 píxeles.
        //
        // Math.max(1, ...):
        // Garantiza que el tamaño calculado sea como mínimo 1 píxel.
        int lado = Math.max(
                1,
                Math.min(
                        TAMANO_CARTA_PREFERIDO,
                        Math.min(espacioAncho / 6, espacioAlto / 6)
                )
        );


        // Calcula el ancho total que ocuparán las seis columnas.
        // Incluye el tamaño de las cartas y las separaciones.
        int anchoMatriz = lado * 6 + SEPARACION * 5;

        // Como el tablero es cuadrado, el alto de la matriz
        // será igual a su ancho.
        int altoMatriz = anchoMatriz;


        // Calcula la posición horizontal donde comenzará la matriz.
        // Se utiliza para centrar el tablero dentro del panel.
        int origenX = (getWidth() - anchoMatriz) / 2;

        // Calcula la posición vertical donde comenzará la matriz.
        // También permite centrar el tablero verticalmente.
        int origenY = (getHeight() - altoMatriz) / 2;


        // ---------------------------------------------------------
        // UBICACIÓN INDIVIDUAL DE LAS 36 CARTAS
        // ---------------------------------------------------------

        // Recorre todas las cartas almacenadas en el arreglo.
        for (int indice = 0; indice < cartas.length; indice++) {

            // Calcula la fila en la que se ubicará la carta.
            //
            // La división entera entre 6 permite obtener la fila.
            // Los índices 0-5 pertenecen a la primera fila,
            // los índices 6-11 a la segunda y así sucesivamente.
            int fila = indice / 6;

            // Calcula la columna en la que se ubicará la carta.
            //
            // El operador % obtiene el residuo de la división.
            // De esta manera se obtienen las columnas de 0 a 5.
            int columna = indice % 6;


            // Establece la posición y el tamaño de cada carta.
            //
            // origenX + columna * (lado + SEPARACION):
            // Calcula la posición horizontal de la carta.
            //
            // origenY + fila * (lado + SEPARACION):
            // Calcula la posición vertical de la carta.
            //
            // lado, lado:
            // Establece el ancho y alto de la carta.
            // Ambos valores son iguales para conservar su forma cuadrada.
            cartas[indice].setBounds(
                    origenX + columna * (lado + SEPARACION),
                    origenY + fila * (lado + SEPARACION),
                    lado,
                    lado
            );
        }
    }


    // ---------------------------------------------------------
    // DIBUJO DEL FONDO DEL TABLERO
    // ---------------------------------------------------------

    // Sobrescribe paintComponent para personalizar la apariencia del tablero.
    @Override
    protected void paintComponent(Graphics graficos) {

        // Crea una copia del objeto Graphics y la convierte en Graphics2D.
        // Esto permite utilizar herramientas gráficas más avanzadas.
        Graphics2D lienzo = (Graphics2D) graficos.create();

        // Activa el suavizado de bordes para mejorar la apariencia
        // de las figuras dibujadas.
        lienzo.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        // Establece el color azul definido en EstiloInterfaz.
        lienzo.setColor(EstiloInterfaz.AZUL);

        // Dibuja un rectángulo azul con esquinas redondeadas.
        //
        // 0, 0: posición inicial del rectángulo.
        // getWidth(): ancho del panel.
        // getHeight(): alto del panel.
        // 36, 36: nivel de redondeo de las esquinas.
        lienzo.fillRoundRect(
                0, 0,
                getWidth(),
                getHeight(),
                36, 36
        );

        // Libera los recursos gráficos de la copia creada.
        lienzo.dispose();

        // Llama al método original para que Swing continúe
        // con el proceso normal de dibujo del componente.
        super.paintComponent(graficos);
    }
}