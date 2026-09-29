package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

// esto lo saque aparte pa no repetirlo en los 7 archivos del panel de admin.
// el problema real que resuelve: JButton normal con setBackground(color) no
// pinta nada en windows si el Look and Feel del sistema esta activo (el que
// pone Login.java al arrancar), se queda con su gris de siempre y el texto
// blanco se ve invisible. este boton se pinta el mismo su fondo, asi que
// no le importa que Look and Feel este activo
public class EstiloAdmin {

    // ==========================================
    // BARRA LATERAL COMPARTIDA (misma en las 7 pantallas del admin)
    // ==========================================
    public static JPanel construirBarraLateral() {
        JPanel menu = new JPanel(new BorderLayout());
        menu.setBackground(Chiksxburgermenu.MAROON);
        menu.setPreferredSize(new Dimension(260, 10));
        menu.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 4, Chiksxburgermenu.GOLD));

        JPanel opciones = new JPanel();
        opciones.setOpaque(false);
        opciones.setLayout(new BoxLayout(opciones, BoxLayout.Y_AXIS));
        opciones.setBorder(new EmptyBorder(20, 0, 20, 0));

        opciones.add(itemBarra("Inicio", () -> NavegadorAdmin.mostrar(new PaneldeAdmin())));
        opciones.add(itemBarra("Gestion de usuarios", () -> NavegadorAdmin.mostrar(new GestionUsuarios())));
        opciones.add(itemBarra("Gestion de pedidos", () -> NavegadorAdmin.mostrar(new GestionPedidos())));
        opciones.add(itemBarra("Gestion de producto", () -> NavegadorAdmin.mostrar(new GestionProducto())));
        opciones.add(itemBarra("Gestion de stock", () -> NavegadorAdmin.mostrar(new GestionStock())));
        opciones.add(itemBarra("Gestion de promociones", () -> NavegadorAdmin.mostrar(new GestionPromociones())));
        opciones.add(itemBarra("Gestion de catalogo y menu dinamico", () -> NavegadorAdmin.mostrar(new GestionCatalogo())));

        JLabel salir = itemBarra("Cerrar sesion", () -> {
            int opcion = JOptionPane.showConfirmDialog(
                    null, "Deseas cerrar sesion?", "Cerrar sesion", JOptionPane.YES_NO_OPTION);
            if (opcion == JOptionPane.YES_OPTION) {
                NavegadorAdmin.mostrar(new Login());
            }
        });

        JPanel salirWrap = new JPanel(new BorderLayout());
        salirWrap.setOpaque(false);
        salirWrap.add(salir, BorderLayout.NORTH);

        menu.add(opciones, BorderLayout.NORTH);
        menu.add(salirWrap, BorderLayout.SOUTH);
        return menu;
    }

    private static JLabel itemBarra(String texto, Runnable accion) {
        JLabel item = new JLabel(texto);
        item.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        item.setOpaque(true);
        item.setBackground(Chiksxburgermenu.MAROON);
        item.setForeground(Chiksxburgermenu.CREAM_2);
        item.setBorder(new EmptyBorder(16, 20, 16, 20));
        item.setAlignmentX(Component.LEFT_ALIGNMENT);
        // esto es justo lo que le faltaba antes: sin esto, el fondo amarillo
        // del hover no llenaba todo el ancho de la barra
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, item.getMaximumSize().height));
        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        item.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) { accion.run(); }
            @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                item.setBackground(Chiksxburgermenu.GOLD);
                item.setForeground(Chiksxburgermenu.BROWN_950);
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e) {
                item.setBackground(Chiksxburgermenu.MAROON);
                item.setForeground(Chiksxburgermenu.CREAM_2);
            }
        });
        return item;
    }

    // boton solido, de un solo color fijo (para agregar/editar/eliminar/etc)
    public static class BotonSolido extends JButton {
        private Color colorFondo;
        private final Color colorTexto;

        public BotonSolido(String texto, Color fondo, Color textoColor) {
            super(texto);
            this.colorFondo = fondo;
            this.colorTexto = textoColor;
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(false);
            setForeground(textoColor);
            setFont(new Font("Segoe UI", Font.BOLD, 13));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            // se oscurece un poco al pasar el mouse, pa que se sienta clicable
            Color hover = fondo.darker();
            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                    colorFondo = hover;
                    repaint();
                }
                @Override public void mouseExited(java.awt.event.MouseEvent e) {
                    colorFondo = fondo;
                    repaint();
                }
            });
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(colorFondo);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // el mismo icono cuadrado con 3 rayitas que usamos en el menu y el login,
    // nomas que aqui esta accesible para cualquier pantalla del panel
    public static Icon crearIconoLogo(int size, Color fondo, Color rayitas) {
        return new Icon() {
            public int getIconWidth() { return size; }
            public int getIconHeight() { return size; }
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(fondo);
                g2.fillRoundRect(x, y, size, size, size / 4, size / 4);
                g2.setColor(rayitas);
                g2.setStroke(new BasicStroke(size / 12f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                int pad = size / 5;
                for (int i = 0; i < 3; i++) {
                    int ly = y + pad + i * (size - 2 * pad) / 2;
                    g2.drawLine(x + pad, ly, x + size - pad, ly);
                }
                g2.dispose();
            }
        };
    }

    // iconos sencillos dibujados a mano para las tarjetas del panel de inicio,
    // en vez de emojis (esos a veces salen como cuadrito vacio, sin la fuente
    // correcta instalada)
    public static Icon iconoHamburguesa(int size, Color color) {
        return new Icon() {
            public int getIconWidth() { return size; }
            public int getIconHeight() { return size; }
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                int panY = y + size / 6;
                int panH = size / 3;
                g2.fillRoundRect(x, panY, size, panH, panH, panH);
                g2.fillRect(x + 2, y + size / 2, size - 4, size / 8);
                int baseY = y + size - size / 3;
                g2.fillRoundRect(x, baseY, size, size / 4, size / 8, size / 8);
                g2.dispose();
            }
        };
    }

    public static Icon iconoBebida(int size, Color color) {
        return new Icon() {
            public int getIconWidth() { return size; }
            public int getIconHeight() { return size; }
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                Polygon vaso = new Polygon();
                vaso.addPoint(x + size / 5, y + size / 6);
                vaso.addPoint(x + size - size / 5, y + size / 6);
                vaso.addPoint(x + size - size / 4, y + size);
                vaso.addPoint(x + size / 4, y + size);
                g2.fillPolygon(vaso);
                g2.dispose();
            }
        };
    }

    public static Icon iconoUsuario(int size, Color color) {
        return new Icon() {
            public int getIconWidth() { return size; }
            public int getIconHeight() { return size; }
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                int cabezaD = size / 2;
                g2.fillOval(x + (size - cabezaD) / 2, y, cabezaD, cabezaD);
                g2.fillArc(x, y + size / 2, size, size, 0, 180);
                g2.dispose();
            }
        };
    }

    public static Icon iconoEtiqueta(int size, Color color) {
        return new Icon() {
            public int getIconWidth() { return size; }
            public int getIconHeight() { return size; }
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                Polygon etiqueta = new Polygon();
                etiqueta.addPoint(x, y + size / 2);
                etiqueta.addPoint(x + size / 2, y);
                etiqueta.addPoint(x + size, y);
                etiqueta.addPoint(x + size, y + size / 2);
                etiqueta.addPoint(x + size / 2, y + size);
                g2.fillPolygon(etiqueta);
                g2.setColor(Color.WHITE);
                g2.fillOval(x + size / 3, y + size / 4, size / 6, size / 6);
                g2.dispose();
            }
        };
    }

    public static Icon iconoRecibo(int size, Color color) {
        return new Icon() {
            public int getIconWidth() { return size; }
            public int getIconHeight() { return size; }
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fillRoundRect(x + size / 5, y, size - (2 * size / 5), size, size / 10, size / 10);
                g2.setColor(Color.WHITE);
                for (int i = 0; i < 3; i++) {
                    int ly = y + size / 3 + i * (size / 6);
                    g2.fillRect(x + size / 3, ly, size / 3, 2);
                }
                g2.dispose();
            }
        };
    }
}