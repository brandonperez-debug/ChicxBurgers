package gui;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import ChicxBurgerDB.ProductoDAO;
import main.Conexion.conexion;
import java.sql.*;
import java.util.HashMap;

// pantalla del menu, ya casi la termino jaja
// intente que se pareciera al de mcdonalds pero con los colores de nosotros
public class Chiksxburgermenu extends JFrame {

    // colores de la marca, no muevan esto porfa
    static final Color BROWN_950 = new Color(0x3A, 0x18, 0x10);
    static final Color MAROON    = new Color(0x5C, 0x27, 0x18);
    static final Color RED       = new Color(0xC1, 0x27, 0x2D);
    static final Color GOLD      = new Color(0xE8, 0xA3, 0x3D);
    static final Color GOLD_DEEP = new Color(0xD9, 0x8A, 0x1F);
    static final Color CREAM     = new Color(0xFB, 0xF1, 0xE2);
    static final Color CREAM_2   = new Color(0xF4, 0xE6, 0xCF);
    static final Color INK       = new Color(0x2B, 0x18, 0x10);
    static final Color INK_SOFT  = new Color(0x6B, 0x4A, 0x3A);

    // colores nuevos pa la entrada del menu, tipo sitio de excavacion
    static final Color BASALTO     = new Color(0x24, 0x19, 0x14);
    static final Color BASALTO_2   = new Color(0x33, 0x24, 0x1C);
    static final Color AMBAR       = new Color(0xC9, 0x7A, 0x2B);
    static final Color AMBAR_CLARO = new Color(0xE2, 0xA3, 0x5A);

    static final Font FONT_LOGO   = new Font("SansSerif", Font.BOLD, 22);
    static final Font FONT_NAV    = new Font("SansSerif", Font.BOLD, 14);
    static final Font FONT_H1     = new Font("SansSerif", Font.BOLD, 38);
    static final Font FONT_SUB    = new Font("SansSerif", Font.PLAIN, 15);
    static final Font FONT_CARD   = new Font("SansSerif", Font.BOLD, 16);
    static final Font FONT_TAG    = new Font("SansSerif", Font.PLAIN, 12);
    static final Font FONT_BREADCRUMB = new Font("SansSerif", Font.BOLD, 13);

    // esto es para cambiar de pantalla sin abrir otra ventana
    private final JPanel categoriaContenedor = new JPanel();
    private JPanel menuPrincipal;
    private JScrollPane scroll;
    private JLabel navMenuLink;
    private JLabel navCarritoLink;
    private JComponent gridCategoriasPanel;

    // aqui guardo lo que va llevando el cliente
    private final List<ItemPedido> carrito = new ArrayList<>();
    private final int idUsuarioActual;
    private final ProductoDAO productoDAO = new ProductoDAO();
    private final HashMap<String, Integer> metodosPagoMap = new HashMap<>();

    // TODO: igual que en GestionPedidos, esto deberia venir de un TURNO
    // abierto real. Debe existir una fila en TURNO con este id.
    private static final int ID_TURNO_ACTUAL = 1;
    private static final double EXTRA_COMBO = 18.0;
    private static final String[] OPCIONES_PERSONALIZACION = {
        "Sin lechuga", "Sin tomate", "Sin cebolla", "Sin queso", "Sin salsa especial", "Sin carne/pollo"
    };

    public Chiksxburgermenu() {
        this(1);
}

    public Chiksxburgermenu(int idUsuarioActual) {
        this.idUsuarioActual = idUsuarioActual;
        cargarMetodosPago();
        setTitle("ChicxBurger - Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // lo de Toolkit no me agarraba bien toda la pantalla, con esto le
        // pido directo al sistema el area maxima que puede ocupar una
        // ventana (la pantalla completa, descontando la barra de tareas)
        Rectangle limites = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        setBounds(limites);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(780, 560));

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(CREAM);

        root.add(buildHeader(), BorderLayout.NORTH);

        menuPrincipal = new JPanel();
        menuPrincipal.setLayout(new BoxLayout(menuPrincipal, BoxLayout.Y_AXIS));
        menuPrincipal.setBackground(CREAM);
        menuPrincipal.add(buildHero());
        menuPrincipal.add(buildGridCategorias());

        categoriaContenedor.setLayout(new BoxLayout(categoriaContenedor, BoxLayout.Y_AXIS));
        categoriaContenedor.setBackground(CREAM);

        // ojo no le cambien esto a cardlayout xq se ve todo chueco al regresar
        // (ya me paso, se quedaba un espacio en blanco gigante abajo sin razon)
        scroll = new JScrollPane(menuPrincipal);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBackground(CREAM);
        scroll.getViewport().setBackground(CREAM);

        root.add(scroll, BorderLayout.CENTER);
        root.add(buildFooter(), BorderLayout.SOUTH);

        setContentPane(root);
    }

    private void irAMenuPrincipal() {
        scroll.setViewportView(menuPrincipal);
        scroll.revalidate();
        scroll.repaint();
        SwingUtilities.invokeLater(() -> scroll.getVerticalScrollBar().setValue(0));
    }

    private void cargarMetodosPago() {
    String sql = "SELECT id_metodo_pago, nombre_metodo FROM METODO_PAGO";
    try (Connection con = conexion.getConnection();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
            metodosPagoMap.put(rs.getString("nombre_metodo"), rs.getInt("id_metodo_pago"));
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error al cargar metodos de pago: " + e.getMessage());
    }
}
   
private void registrarVenta(String metodoPagoSeleccionado) {

    if (carrito.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Tu carrito esta vacio.");
        return;
    }

    if (metodoPagoSeleccionado == null || !metodosPagoMap.containsKey(metodoPagoSeleccionado)) {
        JOptionPane.showMessageDialog(this, "Selecciona un metodo de pago valido.");
        return;
    }
    int idMetodoPago = metodosPagoMap.get(metodoPagoSeleccionado);

    double total = 0;
    for (ItemPedido item : carrito) {
        total += item.precio;
    }

    Connection con = null;

    try {
        con = conexion.getConnection();
        con.setAutoCommit(false);

        String sqlVenta = "INSERT INTO VENTA (total, descuento_total, id_usuario, id_metodo_pago, id_turno) "
                + "VALUES (?, 0, ?, ?, ?)";

        int idVentaGenerado;

        try (PreparedStatement psVenta = con.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS)) {
            psVenta.setDouble(1, total);
            psVenta.setInt(2, idUsuarioActual);
            psVenta.setInt(3, idMetodoPago);
            psVenta.setInt(4, ID_TURNO_ACTUAL);
            psVenta.executeUpdate();

            try (ResultSet keys = psVenta.getGeneratedKeys()) {
                if (keys.next()) {
                    idVentaGenerado = keys.getInt(1);
                } else {
                    throw new SQLException("No se pudo obtener el id de la venta generada.");
                }
            }
        }

        String sqlDetalle = "INSERT INTO DETALLE_VENTA (id_venta, id_producto, cantidad, precio_unitario, subtotal) "
                + "VALUES (?, ?, 1, ?, ?)";

        try (PreparedStatement psDetalle = con.prepareStatement(sqlDetalle)) {
            for (ItemPedido item : carrito) {
                // obtiene o crea el producto real detras del nombre tematico
                int idProducto = productoDAO.obtenerOCrearProductoPorNombre(item.nombreProducto, item.precio);

                psDetalle.setInt(1, idVentaGenerado);
                psDetalle.setInt(2, idProducto);
                psDetalle.setDouble(3, item.precio);
                psDetalle.setDouble(4, item.precio);
                psDetalle.addBatch();
            }
            psDetalle.executeBatch();
        }

        con.commit();

        JOptionPane.showMessageDialog(this, "Pedido confirmado. No. de orden: " + idVentaGenerado);

        carrito.clear();
        actualizarContadorCarrito();
        irACarrito();

    } catch (SQLException e) {
        if (con != null) {
            try {
                con.rollback();
            } catch (SQLException ex) {
                // si ni siquiera se puede revertir, solo lo mostramos abajo
            }
        }
        JOptionPane.showMessageDialog(this, "Error al confirmar el pedido: " + e.getMessage());

    } finally {
        if (con != null) {
            try {
                con.setAutoCommit(true);
                con.close();
            } catch (SQLException ex) {
                // conexion ya cerrada o invalida
            }
        }
    }
}

    private void mostrarCategoriaEnScroll() {
        scroll.setViewportView(categoriaContenedor);
        scroll.revalidate();
        scroll.repaint();
        SwingUtilities.invokeLater(() -> scroll.getVerticalScrollBar().setValue(0));
    }

    private void irACategoria(String nombreCategoria, List<Producto> productos) {
        categoriaContenedor.removeAll();

        categoriaContenedor.add(buildBreadcrumb(nombreCategoria));
        categoriaContenedor.add(buildTituloCategoria(nombreCategoria));
        categoriaContenedor.add(buildGridProductos(productos));
        categoriaContenedor.add(Box.createVerticalStrut(40));

        categoriaContenedor.revalidate();
        categoriaContenedor.repaint();
        mostrarCategoriaEnScroll();
    }

    private void irACajitaFeliz() {
        categoriaContenedor.removeAll();

        categoriaContenedor.add(buildBreadcrumb("Cajita Feliz"));
        categoriaContenedor.add(buildTituloCategoria("Cajita Feliz"));

        categoriaContenedor.add(buildSubtitulo("Con Hamburguesa"));
        categoriaContenedor.add(buildGridProductos(cajitaConHamburguesa()));

        categoriaContenedor.add(buildSubtitulo("Con Nuggets"));
        categoriaContenedor.add(buildGridProductos(cajitaConNuggets()));

        categoriaContenedor.add(Box.createVerticalStrut(40));

        categoriaContenedor.revalidate();
        categoriaContenedor.repaint();
        mostrarCategoriaEnScroll();
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(MAROON);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 4, 0, GOLD));
        header.setPreferredSize(new Dimension(10, 76));

        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        logoPanel.setOpaque(false);
        logoPanel.setBorder(new EmptyBorder(0, 28, 0, 0));

        JLabel mark = new JLabel(buildLogoIcon(40));
        logoPanel.add(mark);

        JPanel logoText = new JPanel();
        logoText.setOpaque(false);
        logoText.setLayout(new BoxLayout(logoText, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("CHICXBURGER");
        title.setFont(FONT_LOGO);
        title.setForeground(CREAM);
        JLabel subtitle = new JLabel("SABOR QUE ALIMENTA");
        subtitle.setFont(new Font("SansSerif", Font.BOLD, 10));
        subtitle.setForeground(GOLD);
        logoText.add(title);
        logoText.add(subtitle);
        logoPanel.add(logoText);

        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        navPanel.setOpaque(false);
        navPanel.setBorder(new EmptyBorder(0, 0, 0, 28));

        navMenuLink = navLink("Menu", true, this::irAMenuPrincipal);
        navPanel.add(navMenuLink);
        navPanel.add(navLink("Promociones & Apps", false, null));
        navPanel.add(navLink("Cajita Feliz", false, this::irACajitaFeliz));
        navCarritoLink = navLink("Carrito", false, this::irACarrito);
        navPanel.add(navCarritoLink);

        header.add(logoPanel, BorderLayout.WEST);
        header.add(navPanel, BorderLayout.EAST);
        return header;
    }

    private JLabel navLink(String text, boolean active, Runnable onClick) {
        JLabel link = new JLabel(text);
        link.setFont(FONT_NAV);
        link.setOpaque(true);
        link.setBorder(new EmptyBorder(10, 18, 10, 18));
        if (active) {
            link.setBackground(GOLD);
            link.setForeground(BROWN_950);
        } else {
            link.setBackground(MAROON);
            link.setForeground(CREAM_2);
        }
        if (onClick != null) {
            link.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            link.addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) { onClick.run(); }
            });
        }
        return link;
    }

    private Icon buildLogoIcon(int size) {
        return new Icon() {
            public int getIconWidth() { return size; }
            public int getIconHeight() { return size; }
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(GOLD);
                g2.fillRoundRect(x, y, size, size, 12, 12);
                g2.setColor(BROWN_950);
                g2.setStroke(new BasicStroke(3.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                int pad = size / 5;
                for (int i = 0; i < 3; i++) {
                    int ly = y + pad + i * (size - 2 * pad) / 2;
                    g2.drawLine(x + pad, ly, x + size - pad, ly);
                }
                g2.dispose();
            }
        };
    }

    private JPanel buildHero() {
        HeroPanel hero = new HeroPanel();
        hero.setLayout(new BorderLayout());
        hero.setBorder(new EmptyBorder(56, 32, 74, 32));
        hero.setAlignmentX(Component.LEFT_ALIGNMENT);

        // el texto lo meti en un panel aparte pegado a la izquierda pq si no
        // se me centraba solo y no hallaba por que asi que mejor asi quedo
        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setOpaque(false);

        JLabel eyebrow = new JLabel("  Recien desenterrado esta semana");
        eyebrow.setFont(new Font("SansSerif", Font.BOLD, 12));
        eyebrow.setForeground(AMBAR_CLARO);
        eyebrow.setOpaque(true);
        eyebrow.setBackground(BASALTO_2);
        eyebrow.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(AMBAR.getRed(), AMBAR.getGreen(), AMBAR.getBlue(), 150), 1, true),
                new EmptyBorder(6, 14, 6, 14)));
        eyebrow.setAlignmentX(Component.LEFT_ALIGNMENT);
        eyebrow.setMaximumSize(eyebrow.getPreferredSize());

        // el titulo con lo del span de color lo hago con font color en html
        // pq asi si me lo pinta bien de dos colores la etiqueta
        JLabel h1 = new JLabel("<html><div style='width:520px'>66 millones de años de "
                + "<font color='#e2a35a'>sabor</font>, listo en minutos.</div></html>");
        h1.setFont(FONT_H1);
        h1.setForeground(CREAM);
        h1.setBorder(new EmptyBorder(16, 0, 10, 0));
        h1.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel p = new JLabel("<html><div style='width:480px'>Cada platillo lleva el nombre de una especie distinta. Elige tu era, arma tu combo, y dejanos el resto.</div></html>");
        p.setFont(FONT_SUB);
        p.setForeground(CREAM_2);
        p.setBorder(new EmptyBorder(0, 0, 24, 0));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        BotonAmbar btnVerMenu = new BotonAmbar("Ver el menu completo");
        btnVerMenu.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnVerMenu.addActionListener(e -> {
            if (gridCategoriasPanel != null) {
                gridCategoriasPanel.scrollRectToVisible(new Rectangle(0, 0, 10, 10));
            }
        });

        // las huellitas que van bajando hacia las categorias, puro adorno
        JPanel huellas = new JPanel(new FlowLayout(FlowLayout.LEFT, 28, 0));
        huellas.setOpaque(false);
        huellas.setBorder(new EmptyBorder(30, 0, 0, 0));
        huellas.setAlignmentX(Component.LEFT_ALIGNMENT);
        float[] alfas = {0.6f, 0.4f, 0.25f};
        int[] corrimientos = {0, 12, 22};
        for (int i = 0; i < 3; i++) {
            JLabel pie = new JLabel(crearIconoHuella(22, 28, alfas[i]));
            pie.setBorder(new EmptyBorder(corrimientos[i], 0, 0, 0));
            huellas.add(pie);
        }

        contenido.add(eyebrow);
        contenido.add(h1);
        contenido.add(p);
        contenido.add(btnVerMenu);
        contenido.add(huellas);

        hero.add(contenido, BorderLayout.WEST);
        return hero;
    }

    // el fondo del hero, con degradado tipo roca, brillo de ambar y el
    // borde rasgado abajo pa que no quede una linea recta y aburrida
    static class HeroPanel extends JPanel {
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();

            GradientPaint fondo = new GradientPaint(0, 0, BASALTO, w, h, BASALTO_2);
            g2.setPaint(fondo);
            g2.fillRect(0, 0, w, h);

            RadialGradientPaint brillo = new RadialGradientPaint(
                    new Point2D.Float(w * 0.82f, h * 0.2f), Math.max(w, h) * 0.55f,
                    new float[]{0f, 1f},
                    new Color[]{
                            new Color(AMBAR.getRed(), AMBAR.getGreen(), AMBAR.getBlue(), 90),
                            new Color(AMBAR.getRed(), AMBAR.getGreen(), AMBAR.getBlue(), 0)
                    });
            g2.setPaint(brillo);
            g2.fillRect(0, 0, w, h);

            // franja rasgada abajo, como si fuera roca partida, del color
            // que sigue despues (CREAM) para que se vea la transicion
            int franjaAlto = 46;
            Random rnd = new Random(7);
            Path2D.Double borde = new Path2D.Double();
            borde.moveTo(0, h);
            borde.lineTo(0, h - franjaAlto);
            int pasos = 22;
            for (int i = 0; i <= pasos; i++) {
                double x = w * (i / (double) pasos);
                double y = h - franjaAlto + rnd.nextDouble() * (franjaAlto * 0.75);
                borde.lineTo(x, y);
            }
            borde.lineTo(w, h);
            borde.closePath();
            g2.setColor(CREAM);
            g2.fill(borde);

            g2.dispose();
        }
    }

    // dibujita de huellita de dinosaurio, para el hero nomas
    private Icon crearIconoHuella(int w, int h, float alfa) {
        return new Icon() {
            public int getIconWidth() { return w; }
            public int getIconHeight() { return h; }
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color col = new Color(AMBAR_CLARO.getRed(), AMBAR_CLARO.getGreen(), AMBAR_CLARO.getBlue(), (int) (255 * alfa));
                g2.setColor(col);
                g2.fillOval(x, y + h / 3, w, h * 2 / 3);
                int toeW = w / 3;
                g2.fillOval(x, y, toeW, h / 2);
                g2.fillOval(x + toeW, y - 2, toeW, h / 2 + 4);
                g2.fillOval(x + 2 * toeW, y, toeW, h / 2);
                g2.dispose();
            }
        };
    }

    private JPanel buildGridCategorias() {
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setBackground(CREAM);
        wrap.setBorder(new EmptyBorder(10, 32, 60, 32));
        // esto es lo que le faltaba: si no le pongo el mismo alignmentX que
        // el hero, el BoxLayout hace un ajuste raro entre los dos paneles y
        // el hero queda angosto y pegado a la derecha en vez de ocupar todo
        wrap.setAlignmentX(Component.LEFT_ALIGNMENT);
        gridCategoriasPanel = wrap;

        JPanel grid = new JPanel(new GridLayout(0, 3, 20, 20));
        grid.setBackground(CREAM);

        grid.add(buildCardCategoria("Desayunos", "20 platillos para empezar el dia",
                () -> irACategoria("Desayunos", productosDesayunos())));
        grid.add(buildCardCategoria("Almuerzos y cenas", "Hamburguesas y pollo",
                () -> irACategoria("Almuerzos y Cenas", productosAlmuerzos())));
        grid.add(buildCardCategoria("Postres", "Dulce final",
                () -> irACategoria("Postres", productosPostres())));
        grid.add(buildCardCategoria("Bebidas", "Frias y calientes",
                () -> irACategoria("Bebidas", productosBebidas())));
        grid.add(buildCardCategoria("Antojos", "Papas y snacks",
                () -> irACategoria("Antojos", productosAntojos())));
        grid.add(buildCardCategoria("Cajita Feliz", "Hamburguesa o Nuggets",
                this::irACajitaFeliz));

        wrap.add(grid, BorderLayout.CENTER);
        return wrap;
    }

    private JPanel buildCardCategoria(String label, String tag, Runnable onClick) {
        JPanel card = new TarjetaCategoria(new BorderLayout(22, 0));
        card.setBackground(Color.WHITE);

        // el borde normal y el que sale cuando pasas el mouse, para el resaltado
        Border bordeNormal = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CREAM_2, 2, true),
                new EmptyBorder(22, 22, 22, 22));
        Border bordeHover = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AMBAR, 2, true),
                new EmptyBorder(22, 22, 22, 22));
        card.setBorder(bordeNormal);
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        card.add(placeholderImagen(120, 120), BorderLayout.WEST);

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        JLabel title = new JLabel(label);
        title.setFont(FONT_CARD);
        title.setForeground(BROWN_950);
        JLabel tagLbl = new JLabel(tag);
        tagLbl.setFont(FONT_TAG);
        tagLbl.setForeground(INK_SOFT);
        textPanel.add(title);
        textPanel.add(Box.createVerticalStrut(3));
        textPanel.add(tagLbl);
        card.add(textPanel, BorderLayout.CENTER);

        card.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { onClick.run(); }
            @Override public void mouseEntered(MouseEvent e) { card.setBorder(bordeHover); card.setBackground(CREAM_2); }
            @Override public void mouseExited(MouseEvent e) { card.setBorder(bordeNormal); card.setBackground(Color.WHITE); }
        });

        return card;
    }

    private JPanel buildBreadcrumb(String nombreCategoria) {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        bar.setOpaque(false);
        bar.setBorder(new EmptyBorder(26, 32, 0, 32));
        bar.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel link = new JLabel("Menu");
        link.setFont(FONT_BREADCRUMB);
        link.setForeground(GOLD_DEEP);
        link.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        link.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { irAMenuPrincipal(); }
        });

        JLabel sep = new JLabel(" / ");
        sep.setFont(FONT_BREADCRUMB);
        sep.setForeground(INK_SOFT);

        JLabel actual = new JLabel(nombreCategoria);
        actual.setFont(FONT_BREADCRUMB);
        actual.setForeground(INK);

        bar.add(link);
        bar.add(sep);
        bar.add(actual);
        return bar;
    }

    private JLabel buildTituloCategoria(String nombreCategoria) {
        JLabel titulo = new JLabel(nombreCategoria);
        titulo.setFont(FONT_H1);
        titulo.setForeground(BROWN_950);
        titulo.setBorder(new EmptyBorder(6, 32, 24, 32));
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        return titulo;
    }

    private JLabel buildSubtitulo(String texto) {
        JLabel sub = new JLabel(texto);
        sub.setFont(new Font("SansSerif", Font.BOLD, 20));
        sub.setForeground(RED);
        sub.setBorder(new EmptyBorder(10, 32, 16, 32));
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);
        return sub;
    }

    private JPanel buildGridProductos(List<Producto> productos) {
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.setBorder(new EmptyBorder(0, 32, 10, 32));
        wrap.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel grid = new JPanel(new GridLayout(0, 4, 18, 22));
        grid.setOpaque(false);
        for (Producto p : productos) {
            grid.add(buildCardProducto(p));
        }
        wrap.add(grid, BorderLayout.CENTER);
        return wrap;
    }

    private JPanel buildCardProducto(Producto p) {
        JPanel card = new TarjetaProducto();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);

        Border bordeNormal = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CREAM_2, 2, true),
                new EmptyBorder(14, 14, 14, 14));
        Border bordeHover = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AMBAR, 2, true),
                new EmptyBorder(14, 14, 14, 14));
        card.setBorder(bordeNormal);
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { abrirDetalleProducto(p); }
            @Override public void mouseEntered(MouseEvent e) { card.setBorder(bordeHover); card.setBackground(CREAM_2); }
            @Override public void mouseExited(MouseEvent e) { card.setBorder(bordeNormal); card.setBackground(Color.WHITE); }
        });

        JComponent imagen = placeholderImagenConTexto(200, 120, "Imagen aqui");
        imagen.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(imagen);
        card.add(Box.createVerticalStrut(10));

        JLabel nombre = new JLabel("<html><div style='width:190px'>" + p.nombre + "</div></html>");
        nombre.setFont(FONT_CARD);
        nombre.setForeground(BROWN_950);
        nombre.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(nombre);
        card.add(Box.createVerticalStrut(4));

        JLabel desc = new JLabel("<html><div style='width:190px'>" + p.descripcion + "</div></html>");
        desc.setFont(FONT_TAG);
        desc.setForeground(INK_SOFT);
        desc.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(desc);
        card.add(Box.createVerticalStrut(6));

        JLabel precio = new JLabel(formatoPrecio(p.precio));
        precio.setFont(new Font("SansSerif", Font.BOLD, 14));
        precio.setForeground(RED);
        precio.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(precio);

        return card;
    }

    private JComponent placeholderImagen(int w, int h) {
        return new ImagePlaceholder(w, h);
    }

    private JComponent placeholderImagenConTexto(int w, int h, String texto) {
        JPanel wrap = new JPanel();
        wrap.setLayout(new BoxLayout(wrap, BoxLayout.Y_AXIS));
        wrap.setOpaque(false);

        ImagePlaceholder img = new ImagePlaceholder(w, h);
        img.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel caption = new JLabel(texto);
        caption.setFont(new Font("SansSerif", Font.PLAIN, 11));
        caption.setForeground(INK_SOFT);
        caption.setAlignmentX(Component.LEFT_ALIGNMENT);
        caption.setBorder(new EmptyBorder(4, 2, 0, 0));

        wrap.add(img);
        wrap.add(caption);
        return wrap;
    }

    // el recuadro vacio pa las fotos, mi compa las va a poner despues
    static class ImagePlaceholder extends JComponent {
        private final int w, h;
        ImagePlaceholder(int w, int h) {
            this.w = w;
            this.h = h;
            setPreferredSize(new Dimension(w, h));
            setMaximumSize(new Dimension(w, h));
            setMinimumSize(new Dimension(w, h));
            setOpaque(false);
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(CREAM_2);
            g2.fillRoundRect(0, 0, w, h, 14, 14);

            float[] dash = {6f, 5f};
            g2.setColor(GOLD_DEEP);
            g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10f, dash, 0f));
            g2.drawRoundRect(2, 2, w - 5, h - 5, 14, 14);

            int iw = Math.max(24, Math.min(w, h) / 2);
            int fx = (w - iw) / 2;
            int fy = (h - iw / 2) / 2;
            int fw = iw;
            int fh = iw / 2 + 6;

            g2.setColor(new Color(0xC9, 0xB2, 0x8F));
            g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawRoundRect(fx, fy, fw, fh, 6, 6);
            g2.fillOval(fx + 6, fy + 6, 8, 8);

            Polygon mountain = new Polygon();
            mountain.addPoint(fx + 5, fy + fh - 5);
            mountain.addPoint(fx + fw / 2, fy + 10);
            mountain.addPoint(fx + fw - 5, fy + fh - 5);
            g2.fillPolygon(mountain);

            g2.dispose();
        }
    }

    // panel con las esquinas redondas, lo usan otras pantallas tambien no borrar
    static class RoundedPanel extends JPanel {
        private final int radius;
        private final Color fill;
        private final Color edge;
        RoundedPanel(int radius, Color fill, Color edge) {
            this.radius = radius;
            this.fill = fill;
            this.edge = edge;
            setOpaque(false);
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(fill);
            g2.fill(new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, radius, radius));
            g2.setColor(edge);
            g2.setStroke(new BasicStroke(2f));
            g2.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 3, getHeight() - 3, radius, radius));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // efecto de derretido, como si fuera queso o miel goteando desde
    // arriba del panel. mismo cuento del timer que usabamos pa la garra
    // pero mas lento pa que se vea como que va cayendo poco a poco
    static class DerretidoHover extends MouseAdapter {
        private final Component objetivo;
        private double progreso = 0.0;
        private Timer timer;

        DerretidoHover(Component objetivo) {
            this.objetivo = objetivo;
        }

        double getProgreso() {
            return progreso;
        }

        @Override public void mouseEntered(MouseEvent e) { animarHacia(1.0); }
        @Override public void mouseExited(MouseEvent e) { animarHacia(0.0); }

        private void animarHacia(double destino) {
            if (timer != null && timer.isRunning()) {
                timer.stop();
            }
            timer = new Timer(16, null);
            timer.addActionListener(ev -> {
                double paso = 0.06;
                if (progreso < destino) {
                    progreso = Math.min(destino, progreso + paso);
                } else if (progreso > destino) {
                    progreso = Math.max(destino, progreso - paso);
                }
                objetivo.repaint();
                if (progreso == destino) {
                    timer.stop();
                }
            });
            timer.start();
        }
    }

    // dibuja la franjita arriba y las gotas colgando, tipo queso derretido
    private static void pintarDerretido(Graphics2D g2base, int width, int height, double progreso) {
        if (progreso <= 0.001) {
            return;
        }
        Graphics2D g2 = (Graphics2D) g2base.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setClip(0, 0, width, height);

        int franjaAlto = (int) (10 * progreso);

        // semilla fija segun el tamaño del panel pa que el goteo no ande
        // cambiando de forma cada vez que se repinta
        long semilla = width * 31L + height;
        Random rnd = new Random(semilla);

        int espacio = 20;
        int cantidad = Math.max(3, width / espacio);

        GradientPaint pintura = new GradientPaint(0, 0, AMBAR_CLARO, 0, height * 0.5f, AMBAR);
        g2.setPaint(pintura);
        g2.fillRect(0, 0, width, franjaAlto);

        for (int i = 0; i < cantidad; i++) {
            double cx = espacio * 0.5 + i * espacio + (rnd.nextDouble() - 0.5) * 6;
            double largoBase = 14 + rnd.nextDouble() * 22;
            double largo = largoBase * progreso;
            double anchoGota = 10 + rnd.nextDouble() * 6;

            double izq = cx - anchoGota / 2;
            double der = cx + anchoGota / 2;
            Path2D.Double gota = new Path2D.Double();
            gota.moveTo(izq, franjaAlto);
            gota.quadTo(izq, franjaAlto + largo * 0.6, cx, franjaAlto + largo);
            gota.quadTo(der, franjaAlto + largo * 0.6, der, franjaAlto);
            gota.closePath();
            g2.fill(gota);
        }
        g2.dispose();
    }

    // tarjeta de categoria con el goteo encima al pasar el mouse
    static class TarjetaCategoria extends JPanel {
        private final DerretidoHover hover;
        TarjetaCategoria(LayoutManager layout) {
            super(layout);
            hover = new DerretidoHover(this);
            addMouseListener(hover);
        }
        @Override public void paint(Graphics g) {
            super.paint(g);
            pintarDerretido((Graphics2D) g, getWidth(), getHeight(), hover.getProgreso());
        }
    }

    // tarjeta de producto, mismo goteo
    static class TarjetaProducto extends JPanel {
        private final DerretidoHover hover;
        TarjetaProducto() {
            hover = new DerretidoHover(this);
            addMouseListener(hover);
        }
        @Override public void paint(Graphics g) {
            super.paint(g);
            pintarDerretido((Graphics2D) g, getWidth(), getHeight(), hover.getProgreso());
        }
    }

    // este es el boton nuevo, con degradado ambar como si fuera resina
    // fosilizada en vez de un color plano, para agregar y para seguir comprando
    static class BotonAmbar extends JButton {
        BotonAmbar(String texto) {
            super(texto);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setForeground(BROWN_950);
            setFont(new Font("SansSerif", Font.BOLD, 15));
            setBorder(new EmptyBorder(14, 24, 14, 24));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();

            GradientPaint resina = new GradientPaint(0, 0, AMBAR_CLARO, w, h, AMBAR);
            g2.setPaint(resina);
            g2.fillRoundRect(0, 0, w, h, 9, 9);

            // una franjita mas clara arriba como brillo del ambar
            g2.setColor(new Color(255, 255, 255, 55));
            g2.fillRoundRect(3, 3, w - 6, h / 2 - 2, 7, 7);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    private JPanel buildFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(BROWN_950);
        footer.setBorder(new EmptyBorder(16, 32, 16, 32));

        JLabel brand = new JLabel("Orgullosamente parte de ChicxBurger Mesoamerica", buildLogoIcon(26), SwingConstants.LEFT);
        brand.setFont(new Font("SansSerif", Font.PLAIN, 13));
        brand.setForeground(GOLD);
        brand.setIconTextGap(10);

        JPanel links = new JPanel(new FlowLayout(FlowLayout.RIGHT, 18, 0));
        links.setOpaque(false);
        for (String l : new String[]{"Proveedores", "Contacto", "Trabaja con nosotros", "Linea de Etica"}) {
            JLabel a = new JLabel(l);
            a.setFont(new Font("SansSerif", Font.BOLD, 12));
            a.setForeground(CREAM_2);
            links.add(a);
        }

        footer.add(brand, BorderLayout.WEST);
        footer.add(links, BorderLayout.EAST);
        return footer;
    }

    private void abrirDetalleProducto(Producto p) {
        JDialog dialog = new JDialog(this, "Detalle del producto", true);
        dialog.setSize(460, 700);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(CREAM);

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(MAROON);
        top.setBorder(new EmptyBorder(14, 18, 14, 18));
        JButton btnCerrar = new JButton("<  Regresar");
        btnCerrar.setForeground(CREAM);
        btnCerrar.setBackground(MAROON);
        btnCerrar.setBorderPainted(false);
        btnCerrar.setFocusPainted(false);
        btnCerrar.setFont(FONT_NAV);
        btnCerrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCerrar.addActionListener(e -> dialog.dispose());
        top.add(btnCerrar, BorderLayout.WEST);
        root.add(top, BorderLayout.NORTH);

        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setBackground(CREAM);
        contenido.setBorder(new EmptyBorder(22, 24, 22, 24));

        JComponent imagen = placeholderImagenConTexto(390, 220, "Imagen aqui");
        imagen.setAlignmentX(Component.LEFT_ALIGNMENT);
        contenido.add(imagen);
        contenido.add(Box.createVerticalStrut(16));

        JLabel nombre = new JLabel(p.nombre);
        nombre.setFont(new Font("SansSerif", Font.BOLD, 24));
        nombre.setForeground(BROWN_950);
        nombre.setAlignmentX(Component.LEFT_ALIGNMENT);
        contenido.add(nombre);
        contenido.add(Box.createVerticalStrut(8));

        JLabel desc = new JLabel("<html><div style='width:380px'>" + p.descripcion + "</div></html>");
        desc.setFont(FONT_SUB);
        desc.setForeground(INK_SOFT);
        desc.setAlignmentX(Component.LEFT_ALIGNMENT);
        contenido.add(desc);
        contenido.add(Box.createVerticalStrut(14));

        JLabel lblPrecio = new JLabel(formatoPrecio(p.precio));
        lblPrecio.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblPrecio.setForeground(RED);
        lblPrecio.setAlignmentX(Component.LEFT_ALIGNMENT);
        contenido.add(lblPrecio);
        contenido.add(Box.createVerticalStrut(18));

        JRadioButton rbSolo = new JRadioButton("Sola", true);
        JRadioButton rbCombo = new JRadioButton("En combo (papas y bebida)  +" + formatoPrecio(EXTRA_COMBO));

        if (p.permiteCombo) {
            JLabel tituloCombo = new JLabel("Como la quieres?");
            tituloCombo.setFont(new Font("SansSerif", Font.BOLD, 14));
            tituloCombo.setForeground(BROWN_950);
            tituloCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
            contenido.add(tituloCombo);
            contenido.add(Box.createVerticalStrut(6));

            ButtonGroup grupo = new ButtonGroup();
            grupo.add(rbSolo);
            grupo.add(rbCombo);
            rbSolo.setOpaque(false);
            rbCombo.setOpaque(false);
            rbSolo.setFont(FONT_SUB);
            rbCombo.setFont(FONT_SUB);
            rbSolo.setAlignmentX(Component.LEFT_ALIGNMENT);
            rbCombo.setAlignmentX(Component.LEFT_ALIGNMENT);

            ActionListener actualizarPrecio = e -> {
                double total = p.precio + (rbCombo.isSelected() ? EXTRA_COMBO : 0);
                lblPrecio.setText(formatoPrecio(total));
            };
            rbSolo.addActionListener(actualizarPrecio);
            rbCombo.addActionListener(actualizarPrecio);

            contenido.add(rbSolo);
            contenido.add(rbCombo);
            contenido.add(Box.createVerticalStrut(18));
        }

        JLabel tituloPersonaliza = new JLabel("Personaliza tu pedido");
        tituloPersonaliza.setFont(new Font("SansSerif", Font.BOLD, 14));
        tituloPersonaliza.setForeground(BROWN_950);
        tituloPersonaliza.setAlignmentX(Component.LEFT_ALIGNMENT);
        contenido.add(tituloPersonaliza);
        contenido.add(Box.createVerticalStrut(6));

        List<JCheckBox> checks = new ArrayList<>();
        for (String opcion : OPCIONES_PERSONALIZACION) {
            JCheckBox cb = new JCheckBox(opcion);
            cb.setOpaque(false);
            cb.setFont(FONT_SUB);
            cb.setForeground(INK);
            cb.setAlignmentX(Component.LEFT_ALIGNMENT);
            checks.add(cb);
            contenido.add(cb);
        }
        contenido.add(Box.createVerticalStrut(10));

        JScrollPane scrollDetalle = new JScrollPane(contenido);
        scrollDetalle.setBorder(null);
        scrollDetalle.getVerticalScrollBar().setUnitIncrement(14);
        root.add(scrollDetalle, BorderLayout.CENTER);

        JButton btnAgregar = new BotonAmbar("Agregar al pedido");
        btnAgregar.addActionListener(e -> {
            List<String> exclusiones = new ArrayList<>();
            for (JCheckBox cb : checks) {
                if (cb.isSelected()) exclusiones.add(cb.getText());
            }
            String tipo = p.permiteCombo ? (rbCombo.isSelected() ? "En combo" : "Sola") : "";
            double precioFinal = p.precio + (p.permiteCombo && rbCombo.isSelected() ? EXTRA_COMBO : 0);

            carrito.add(new ItemPedido(p.nombre, tipo, exclusiones, precioFinal));
            actualizarContadorCarrito();
            dialog.dispose();
        });
        root.add(btnAgregar, BorderLayout.SOUTH);

        dialog.setContentPane(root);
        dialog.setVisible(true);
    }

    private String formatoPrecio(double valor) {
        return String.format("Q%.2f", valor);
    }

    private void actualizarContadorCarrito() {
        navCarritoLink.setText(carrito.isEmpty() ? "Carrito" : "Carrito (" + carrito.size() + ")");
    }

    // pantalla del carrito, aca se ve todo lo que a agregado el cliente
   private void irACarrito() {
    categoriaContenedor.removeAll();

    categoriaContenedor.add(buildBreadcrumb("Carrito"));
    categoriaContenedor.add(buildTituloCategoria("Tu Carrito"));

    if (carrito.isEmpty()) {
        JLabel vacio = new JLabel("Aun no has agregado productos a tu carrito.");
        vacio.setFont(FONT_SUB);
        vacio.setForeground(INK_SOFT);
        vacio.setBorder(new EmptyBorder(0, 32, 20, 32));
        vacio.setAlignmentX(Component.LEFT_ALIGNMENT);
        categoriaContenedor.add(vacio);
    } else {
        JPanel lista = new JPanel();
        lista.setLayout(new BoxLayout(lista, BoxLayout.Y_AXIS));
        lista.setOpaque(false);
        lista.setBorder(new EmptyBorder(0, 32, 10, 32));
        lista.setAlignmentX(Component.LEFT_ALIGNMENT);

        double total = 0;
        for (ItemPedido item : new ArrayList<>(carrito)) {
            JPanel fila = buildFilaCarrito(item);
            fila.setAlignmentX(Component.LEFT_ALIGNMENT);
            lista.add(fila);
            lista.add(Box.createVerticalStrut(12));
            total += item.precio;
        }
        categoriaContenedor.add(lista);

        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        totalPanel.setOpaque(false);
        totalPanel.setBorder(new EmptyBorder(6, 32, 20, 32));
        totalPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lblTotal = new JLabel("Total: " + formatoPrecio(total));
        lblTotal.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTotal.setForeground(BROWN_950);
        totalPanel.add(lblTotal);
        categoriaContenedor.add(totalPanel);

        // ===== metodo de pago + boton de confirmar =====
        JPanel pagoPanel = new JPanel();
        pagoPanel.setOpaque(false);
        pagoPanel.setLayout(new BoxLayout(pagoPanel, BoxLayout.Y_AXIS));
        pagoPanel.setBorder(new EmptyBorder(0, 32, 20, 32));
        pagoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblMetodo = new JLabel("Metodo de pago:");
        lblMetodo.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblMetodo.setForeground(BROWN_950);
        lblMetodo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JComboBox<String> cbMetodoPago = new JComboBox<>();
        for (String nombre : metodosPagoMap.keySet()) {
            cbMetodoPago.addItem(nombre);
        }
        cbMetodoPago.setFont(FONT_SUB);
        cbMetodoPago.setAlignmentX(Component.LEFT_ALIGNMENT);
        cbMetodoPago.setMaximumSize(new Dimension(240, 36));

        pagoPanel.add(lblMetodo);
        pagoPanel.add(Box.createVerticalStrut(6));
        pagoPanel.add(cbMetodoPago);
        categoriaContenedor.add(pagoPanel);

        JButton btnConfirmar = new BotonAmbar("Confirmar pedido");
        btnConfirmar.addActionListener(e -> registrarVenta((String) cbMetodoPago.getSelectedItem()));

        JPanel confirmarWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        confirmarWrap.setOpaque(false);
        confirmarWrap.setBorder(new EmptyBorder(0, 32, 20, 32));
        confirmarWrap.setAlignmentX(Component.LEFT_ALIGNMENT);
        confirmarWrap.add(btnConfirmar);
        categoriaContenedor.add(confirmarWrap);
    }

    JButton btnSeguir = new BotonAmbar("Seguir comprando");
    btnSeguir.addActionListener(e -> irAMenuPrincipal());

    JPanel botonWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    botonWrap.setOpaque(false);
    botonWrap.setBorder(new EmptyBorder(0, 32, 40, 32));
    botonWrap.setAlignmentX(Component.LEFT_ALIGNMENT);
    botonWrap.add(btnSeguir);
    categoriaContenedor.add(botonWrap);

    categoriaContenedor.revalidate();
    categoriaContenedor.repaint();
    mostrarCategoriaEnScroll();
}

    private JPanel buildFilaCarrito(ItemPedido item) {
        JPanel fila = new JPanel(new BorderLayout(14, 0));
        fila.setBackground(Color.WHITE);
        fila.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CREAM_2, 2, true),
                new EmptyBorder(14, 16, 14, 16)));
        fila.setMaximumSize(new Dimension(720, 130));

        StringBuilder detalle = new StringBuilder();
        if (!item.tipo.isEmpty()) {
            detalle.append(item.tipo);
        }
        if (!item.exclusiones.isEmpty()) {
            if (detalle.length() > 0) detalle.append("  -  ");
            detalle.append(String.join(", ", item.exclusiones));
        }

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        JLabel nombre = new JLabel(item.nombreProducto);
        nombre.setFont(FONT_CARD);
        nombre.setForeground(BROWN_950);
        info.add(nombre);
        if (detalle.length() > 0) {
            JLabel det = new JLabel("<html><div style='width:380px'>" + detalle + "</div></html>");
            det.setFont(FONT_TAG);
            det.setForeground(INK_SOFT);
            info.add(Box.createVerticalStrut(3));
            info.add(det);
        }
        fila.add(info, BorderLayout.CENTER);

        JPanel derecha = new JPanel();
        derecha.setOpaque(false);
        derecha.setLayout(new BoxLayout(derecha, BoxLayout.Y_AXIS));
        JLabel precio = new JLabel(formatoPrecio(item.precio));
        precio.setFont(new Font("SansSerif", Font.BOLD, 15));
        precio.setForeground(RED);
        precio.setAlignmentX(Component.RIGHT_ALIGNMENT);
        JLabel quitar = new JLabel("Quitar");
        quitar.setFont(new Font("SansSerif", Font.PLAIN, 12));
        quitar.setForeground(GOLD_DEEP);
        quitar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        quitar.setAlignmentX(Component.RIGHT_ALIGNMENT);
        quitar.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                carrito.remove(item);
                actualizarContadorCarrito();
                irACarrito();
            }
        });
        derecha.add(precio);
        derecha.add(Box.createVerticalStrut(6));
        derecha.add(quitar);
        fila.add(derecha, BorderLayout.EAST);

        return fila;
    }

    static class Producto {
        final String nombre;
        final String descripcion;
        final double precio;
        final boolean permiteCombo;

        Producto(String nombre, String descripcion, double precio) {
            this(nombre, descripcion, precio, false);
        }

        Producto(String nombre, String descripcion, double precio, boolean permiteCombo) {
            this.nombre = nombre;
            this.descripcion = descripcion;
            this.precio = precio;
            this.permiteCombo = permiteCombo;
        }
    }

    static class ItemPedido {
        final String nombreProducto;
        final String tipo;
        final List<String> exclusiones;
        final double precio;

        ItemPedido(String nombreProducto, String tipo, List<String> exclusiones, double precio) {
            this.nombreProducto = nombreProducto;
            this.tipo = tipo;
            this.exclusiones = exclusiones;
            this.precio = precio;
        }
    }

    // aqui abajo estan todos los presios y nombres de la comida, son un chorro
    private static List<Producto> productosDesayunos() {
        return Arrays.asList(
            new Producto("Huevos Jurasicos", "Dos huevos revueltos estilo rancho con jamon y queso derretido.", 32),
            new Producto("McMuffin T-Rex", "Pan muffin tostado con salchicha, huevo y queso cheddar.", 28),
            new Producto("Croissant Pterodactilo", "Croissant relleno de jamon, huevo y queso suizo.", 34),
            new Producto("Hotcakes Triceratops", "Tres hotcakes esponjosos con miel y mantequilla.", 30),
            new Producto("Burrito Raptor", "Burrito de huevo, tocino, papas y pico de gallo.", 33),
            new Producto("Bagel Estegosaurio", "Bagel tostado con queso crema, salmon y eneldo.", 36),
            new Producto("Wrap Velociraptor", "Tortilla rellena de huevo, aguacate y tocino.", 31),
            new Producto("Sandwich Braquiosaurio", "Pan artesanal con huevo, jamon de pavo y queso.", 35),
            new Producto("Waffle Jurasico", "Waffle crocante con fresas y crema batida.", 32),
            new Producto("Chilaquiles Cretacicos", "Chilaquiles rojos con pollo deshebrado y crema.", 38),
            new Producto("Omelette Diplodocus", "Omelette de tres quesos con champinones y espinaca.", 34),
            new Producto("Papas Rex con Huevo", "Papas doradas con huevo estrellado y salsa.", 29),
            new Producto("Panque Fosil de Platano", "Rebanada de panque casero con miel de maple.", 22),
            new Producto("Yogurt Prehistorico", "Yogurt natural con granola y frutos rojos.", 24),
            new Producto("Combo Dino Huevo y Tocino", "Dos huevos al gusto con tocino crujiente y pan tostado.", 36)
        );
    }

    private static List<Producto> productosAlmuerzos() {
        return Arrays.asList(
            new Producto("T-Rex Doble", "Dos carnes de res, doble queso cheddar, tocino y salsa especial.", 55, true),
            new Producto("Raptor Clasica", "Carne de res, lechuga, tomate, cebolla y queso americano.", 38, true),
            new Producto("Triceratops BBQ", "Carne de res con salsa BBQ, aros de cebolla y queso ahumado.", 48, true),
            new Producto("Diplodocus Gigante", "Carne triple con queso, tocino y salsa secreta Chiksx.", 62, true),
            new Producto("Velociraptor Picante", "Carne de res con jalapenos, queso pepper jack y mayo chipotle.", 46, true),
            new Producto("Estegosaurio Clasica", "Carne de res con queso suizo y champinones salteados.", 44, true),
            new Producto("Pterodactilo Crispy", "Filete de pollo empanizado con lechuga y mayo de ajo.", 42, true),
            new Producto("Alosaurio Buffalo", "Filete de pollo banado en salsa buffalo con queso azul.", 45, true),
            new Producto("Braquiosaurio Doble Pollo", "Doble filete de pollo con tocino y queso cheddar.", 50, true),
            new Producto("Compsognathus Junior", "Hamburguesa sencilla ideal para los mas pequenos.", 28, true),
            new Producto("Ankylosaurus Fuego", "Carne de res con salsa picante habanero y queso pepper jack.", 47, true),
            new Producto("Spinosaurus Especial", "Carne de res, doble tocino, huevo frito y queso cheddar.", 58, true),
            new Producto("Iguanodon Clasica de Pollo", "Filete de pollo a la parrilla con vegetales frescos.", 40, true),
            new Producto("Parasaurolophus BBQ Pollo", "Filete de pollo con salsa BBQ y aros de cebolla.", 44, true),
            new Producto("Megalosaurio Suprema", "Carne de res, queso doble, tocino y aderezo Chiksx.", 56, true),
            new Producto("Utahraptor Ranch", "Filete de pollo empanizado con salsa ranch y tocino.", 43, true),
            new Producto("Carnotaurus Extreme", "Carne de res picante con jalapenos y queso pepper jack.", 49, true),
            new Producto("Therizinosaurus Verde", "Carne de res con guacamole, lechuga y pico de gallo.", 41, true),
            new Producto("Dino Nuggets Combo", "Nuggets de pollo crujientes con papas y salsa a elegir.", 39),
            new Producto("Tiranosaurio Familiar", "Combo doble de hamburguesas T-Rex para compartir.", 95)
        );
    }

    private static List<Producto> productosPostres() {
        return Arrays.asList(
            new Producto("Cono Volcan de Chocolate", "Cono suave banado en chocolate caliente.", 18),
            new Producto("Malteada Jurasica", "Malteada cremosa de vainilla con topping de galleta.", 24),
            new Producto("Pie Fosil de Manzana", "Pay de manzana caliente con canela.", 20),
            new Producto("Sundae Dino Rex", "Helado de vainilla con jarabe de chocolate y nuez.", 19),
            new Producto("Brownie Cretacico", "Brownie tibio con nuez y trozo de chocolate.", 22),
            new Producto("Cheesecake Triasico", "Rebanada de cheesecake con salsa de fresa.", 28),
            new Producto("Cono Clasico de Vainilla", "Cono suave sabor vainilla.", 12),
            new Producto("Cono Clasico de Chocolate", "Cono suave sabor chocolate.", 12),
            new Producto("Torbellino Oreo Dino", "Helado suave mezclado con trozos de galleta de chocolate.", 25),
            new Producto("Paleta Prehistorica", "Paleta helada de fresa con chispas de colores.", 14),
            new Producto("Dona Jurasica", "Dona glaseada con chispas de colores.", 16),
            new Producto("Galleta Gigante Rex", "Galleta de chocolate recien horneada.", 15),
            new Producto("Rollo Fosil de Canela", "Rollo de canela con glaseado dulce.", 18),
            new Producto("Copa Cretacica de Frutas", "Mezcla de frutas frescas de temporada.", 20),
            new Producto("Flan Casero Dino", "Flan napolitano con caramelo.", 19)
        );
    }

    private static List<Producto> productosBebidas() {
        return Arrays.asList(
            new Producto("Refresco Jurasico", "Refresco de cola bien frio.", 14),
            new Producto("Limonada Raptor", "Limonada natural con hierbabuena.", 16),
            new Producto("Te Helado Dino", "Te negro helado con limon.", 15),
            new Producto("Cafe Fosil Americano", "Cafe negro recien preparado.", 14),
            new Producto("Capuchino Cretacico", "Cafe espresso con leche espumada.", 20),
            new Producto("Chocolate Caliente Rex", "Chocolate caliente cremoso.", 18),
            new Producto("Agua Mineral Jurasica", "Agua mineral con gas.", 10),
            new Producto("Jugo Natural de Naranja", "Jugo de naranja recien exprimido.", 16),
            new Producto("Smoothie Dino de Fresa", "Smoothie cremoso de fresa natural.", 22),
            new Producto("Malteada Triasica de Oreo", "Malteada cremosa con galleta de chocolate.", 24)
        );
    }

    private static List<Producto> productosAntojos() {
        return Arrays.asList(
            new Producto("Papas Fritas Rex", "Papas a la francesa crujientes.", 20),
            new Producto("Papas Gajo Jurasicas", "Papas gajo sazonadas con especias.", 24),
            new Producto("Aros de Cebolla Dino", "Aros de cebolla empanizados y crujientes.", 22),
            new Producto("Nuggets de Pollo Raptor", "Nuggets de pollo crujientes, 6 piezas.", 28),
            new Producto("Alitas BBQ Triceratops", "Alitas banadas en salsa BBQ.", 38),
            new Producto("Alitas Picantes Velociraptor", "Alitas banadas en salsa picante.", 38),
            new Producto("Quesadilla Fosil", "Quesadilla de queso derretido con tortilla de harina.", 26),
            new Producto("Bastones de Queso Dino", "Bastones de queso mozzarella empanizados.", 24),
            new Producto("Palomitas de Pollo Rex", "Trocitos de pollo empanizados estilo palomitas.", 27),
            new Producto("Totopos Cretacicos con Queso", "Totopos banados en queso derretido.", 25)
        );
    }

    private static List<Producto> cajitaConHamburguesa() {
        return Arrays.asList(
            new Producto("Cajita Mini Rex", "Hamburguesa sencilla, papas chicas y bebida a elegir.", 32),
            new Producto("Cajita Diplodocus", "Hamburguesa con queso, papas chicas y bebida a elegir.", 38),
            new Producto("Cajita Doble Rex", "Dos hamburguesas sencillas, papas chicas y bebida a elegir.", 45)
        );
    }

    private static List<Producto> cajitaConNuggets() {
        return Arrays.asList(
            new Producto("Cajita Raptor 4", "4 piezas de nuggets, papas chicas y bebida a elegir.", 30),
            new Producto("Cajita Raptor 6", "6 piezas de nuggets, papas chicas y bebida a elegir.", 36),
            new Producto("Cajita Fiesta Dino", "Nuggets y papas grandes para compartir, con dos bebidas.", 58)
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) { }
            new Chiksxburgermenu().setVisible(true);
        });
    }
}