package gui;

import javax.swing.JFrame;

// esto es nomas para que nunca haya 2 ventanas del panel de admin abiertas
// al mismo tiempo. guarda cual es la ventana actual, y cada vez que le
// pedimos que muestre una nueva, cierra la de antes primero
public class NavegadorAdmin {

    private static JFrame ventanaActual;

    public static void mostrar(JFrame nueva) {
        if (ventanaActual != null) {
            ventanaActual.dispose();
        }
        ventanaActual = nueva;
        nueva.setVisible(true);
    }
}