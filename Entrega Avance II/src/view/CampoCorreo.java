package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/** Campo de correo con dimensiones estables para las pantallas de autenticación. */
public class CampoCorreo extends JPanel {
    private final CampoCorreoInterno campoCorreo = new CampoCorreoInterno();

    public CampoCorreo() {
        super(new BorderLayout());
        setOpaque(true);
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(222, 215, 205), 1),
                new EmptyBorder(0, 0, 0, 3)));
        campoCorreo.setBorder(new EmptyBorder(0, 13, 0, 12));
        campoCorreo.setOpaque(false);
        campoCorreo.setToolTipText("nombre@correo.com");
        add(campoCorreo, BorderLayout.CENTER);
        setPreferredSize(new Dimension(430, 42));
        setMinimumSize(new Dimension(430, 42));
        setMaximumSize(new Dimension(430, 42));
    }

    public String getText() {
        return campoCorreo.getText();
    }

    public void setText(String texto) {
        campoCorreo.setText(texto);
    }

    public void configurar(Font fuente, Color colorTexto, Color colorFondo) {
        campoCorreo.setFont(fuente);
        campoCorreo.setForeground(colorTexto);
        campoCorreo.setCaretColor(colorTexto);
        campoCorreo.setBackground(colorFondo);
        setBackground(colorFondo);
    }

    public void establecerIndicador(String indicador) {
        campoCorreo.establecerIndicador(indicador);
        campoCorreo.setToolTipText(indicador);
    }

    private static final class CampoCorreoInterno extends JTextField {
        private String indicador = "nombre@correo.com";

        void establecerIndicador(String texto) {
            indicador = texto == null ? "" : texto;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics graficos) {
            super.paintComponent(graficos);
            if (getText().isEmpty() && !hasFocus() && !indicador.isEmpty()) {
                graficos.setColor(new Color(165, 174, 181));
                graficos.drawString(indicador, getInsets().left,
                        (getHeight() - getFontMetrics(getFont()).getHeight()) / 2
                                + getFontMetrics(getFont()).getAscent());
            }
        }
    }
}
