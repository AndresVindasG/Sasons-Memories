package view;

// Importa los componentes necesarios para crear interfaces gráficas.
import javax.swing.*;

// Permite utilizar bordes y márgenes personalizados.
import javax.swing.border.EmptyBorder;

// Importa las herramientas para organizar y dibujar elementos gráficos.
import java.awt.*;

/**
 * Selector funcional de los modos Patrones, Memoria y premios.
 *
 * Esta clase representa la pantalla donde el usuario puede
 * seleccionar el modo de juego que desea utilizar o consultar los premios.
 *
 * Hereda de JPanel porque representa un panel de la interfaz gráfica.
 */
public class PanelSeleccionModo extends JPanel {

    // Botón que permite ingresar al modo de juego Patrones.
    // Se configura utilizando el método boton de EstiloInterfaz.
    private final JButton botonPatrones = EstiloInterfaz.boton(
            "Patrones", EstiloInterfaz.AZUL, 270, 70, 20);

    // Botón que permite ingresar al modo de juego Memoria.
    private final JButton botonMemoria = EstiloInterfaz.boton(
            "Memoria", EstiloInterfaz.AZUL, 270, 70, 20);

    // Botón que permite acceder a la sección de premios.
    private final JButton botonPremios = EstiloInterfaz.boton(
            "Ver premios", EstiloInterfaz.GRIS, 270, 64, 18);


    // Constructor de la clase.
    // Se encarga de construir y organizar los elementos de la pantalla.
    public PanelSeleccionModo() {

        // Llama al constructor de JPanel y establece BorderLayout
        // como administrador de distribución.
        // Los valores 0, 0 representan las separaciones horizontal y vertical.
        super(new BorderLayout(0, 0));

        // Establece el color de fondo de toda la pantalla.
        setBackground(EstiloInterfaz.FONDO);


        // ---------------------------------------------------------
        // PANEL CENTRAL DE SELECCIÓN DE MODOS
        // ---------------------------------------------------------

        // Crea un panel que contendrá el título y los botones de los modos.
        JPanel seleccion = new JPanel();

        // Hace que el panel sea transparente para mostrar el fondo principal.
        seleccion.setOpaque(false);

        // Organiza los componentes verticalmente.
        // Los elementos se colocarán uno debajo del otro.
        seleccion.setLayout(new BoxLayout(seleccion, BoxLayout.Y_AXIS));

        // Establece los márgenes internos del panel.
        // Los valores representan: arriba, izquierda, abajo y derecha.
        seleccion.setBorder(new EmptyBorder(48, 20, 30, 20));


        // Crea el título principal de la pantalla.
        JLabel titulo = EstiloInterfaz.titulo("Elige el modo de juego", 38);

        // Centra horizontalmente el título.
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Agrega un espacio flexible en la parte superior.
        // Permite distribuir el contenido dentro del panel.
        seleccion.add(Box.createVerticalGlue());

        // Agrega el título al panel de selección.
        seleccion.add(titulo);

        // Agrega un espacio vertical de 36 píxeles aproximadamente
        // entre el título y el primer botón.
        seleccion.add(Box.createVerticalStrut(36));


        // ---------------------------------------------------------
        // BOTÓN DEL MODO PATRONES
        // ---------------------------------------------------------

        // Centra horizontalmente el botón Patrones.
        botonPatrones.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Agrega el botón al panel de selección.
        seleccion.add(botonPatrones);

        // Agrega una separación entre los botones.
        seleccion.add(Box.createVerticalStrut(22));


        // ---------------------------------------------------------
        // BOTÓN DEL MODO MEMORIA
        // ---------------------------------------------------------

        // Centra horizontalmente el botón Memoria.
        botonMemoria.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Agrega el botón al panel de selección.
        seleccion.add(botonMemoria);

        // Agrega un espacio flexible debajo de los botones.
        // Ayuda a mantener el contenido distribuido verticalmente.
        seleccion.add(Box.createVerticalGlue());


        // Incorpora el panel de selección en la región central
        // del BorderLayout principal.
        add(seleccion, BorderLayout.CENTER);


        // ---------------------------------------------------------
        // PANEL INFERIOR DE PREMIOS
        // ---------------------------------------------------------

        // Crea un panel personalizado utilizando GridBagLayout.
        // Se utiliza una clase anónima para modificar la forma
        // en que se dibuja el fondo del panel.
        JPanel franjaPremios = new JPanel(new GridBagLayout()) {

            // Sobrescribe paintComponent para personalizar el dibujo
            // del fondo de este panel.
            @Override
            protected void paintComponent(Graphics graficos) {

                // Crea una copia del objeto Graphics original.
                // Esto permite realizar modificaciones gráficas
                // sin alterar directamente el objeto recibido.
                Graphics2D lienzo = (Graphics2D) graficos.create();

                // Activa el suavizado de bordes.
                // Permite que las figuras se dibujen con bordes más definidos.
                lienzo.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                // Establece el color azul para dibujar la franja inferior.
                lienzo.setColor(EstiloInterfaz.AZUL);

                // Dibuja un rectángulo con esquinas redondeadas.
                //
                // 12: posición horizontal inicial.
                // 0: posición vertical inicial.
                // getWidth() - 24: ancho del rectángulo.
                // getHeight() + 30: alto del rectángulo.
                // 46, 46: curvatura de las esquinas.
                lienzo.fillRoundRect(
                        12, 0,
                        getWidth() - 24,
                        getHeight() + 30,
                        46, 46
                );

                // Libera los recursos gráficos utilizados por la copia.
                lienzo.dispose();

                // Llama al método original para que Swing
                // continúe dibujando correctamente los componentes.
                super.paintComponent(graficos);
            }
        };


        // Hace que el fondo del panel sea transparente.
        // Esto permite visualizar el fondo personalizado que se dibuja.
        franjaPremios.setOpaque(false);

        // Establece el tamaño preferido de la franja de premios.
        // Su ancho es de 800 píxeles y su alto de 250 píxeles.
        franjaPremios.setPreferredSize(new Dimension(800, 250));


        // ---------------------------------------------------------
        // CONTENIDO DE LA FRANJA DE PREMIOS
        // ---------------------------------------------------------

        // Crea un panel interno que contendrá el título y el botón de premios.
        JPanel contenidoPremios = new JPanel();

        // Hace que el panel interno sea transparente.
        contenidoPremios.setOpaque(false);

        // Organiza sus componentes verticalmente.
        contenidoPremios.setLayout(
                new BoxLayout(contenidoPremios, BoxLayout.Y_AXIS)
        );


        // Crea el título de la sección de premios.
        JLabel tituloPremios = EstiloInterfaz.titulo(
                "¿Quiénes son los Top Nice de Sason’s Memories?", 25
        );

        // Establece el color blanco para el título.
        tituloPremios.setForeground(Color.WHITE);

        // Centra horizontalmente el título.
        tituloPremios.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Agrega el título al panel de contenido.
        contenidoPremios.add(tituloPremios);

        // Agrega una separación vertical de 24 píxeles aproximadamente.
        // Permite separar el título del botón.
        contenidoPremios.add(Box.createVerticalStrut(24));


        // ---------------------------------------------------------
        // BOTÓN PARA CONSULTAR LOS PREMIOS
        // ---------------------------------------------------------

        // Centra horizontalmente el botón.
        botonPremios.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Agrega el botón al panel de contenido.
        contenidoPremios.add(botonPremios);


        // Incorpora el contenido dentro de la franja de premios.
        // GridBagLayout permite colocar el contenido en el centro.
        franjaPremios.add(contenidoPremios);

        // Agrega la franja de premios en la parte inferior
        // de la pantalla principal.
        add(franjaPremios, BorderLayout.SOUTH);
    }


    // ---------------------------------------------------------
    // MÉTODOS PARA ASIGNAR ACCIONES A LOS BOTONES
    // ---------------------------------------------------------

    // Recibe una acción que se ejecutará cuando el usuario
    // presione el botón del modo Patrones.
    public void accionPatrones(Runnable accion) {

        // Agrega un ActionListener al botón.
        // Cuando se produce el evento de clic, ejecuta la acción recibida.
        botonPatrones.addActionListener(evento -> accion.run());
    }

    // Recibe una acción que se ejecutará cuando el usuario
    // presione el botón del modo Memoria.
    public void accionMemoria(Runnable accion) {

        // Detecta el clic sobre el botón y ejecuta la acción correspondiente.
        botonMemoria.addActionListener(evento -> accion.run());
    }

    // Recibe una acción que se ejecutará cuando el usuario
    // presione el botón para consultar los premios.
    public void accionPremios(Runnable accion) {

        // Detecta el clic sobre el botón y ejecuta la acción recibida.
        botonPremios.addActionListener(evento -> accion.run());
    }
}