package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import main.Conexion.conexion;
import java.sql.*;

// panel principal del admin. ya quedo con los mismos colores que el resto
// (lo cafe/ambar de Chiksxburgermenu) y ya no se duplican las ventanas al
// cambiar de seccion, nomas se queda abierta una a la vez

public class PaneldeAdmin extends JFrame {

    Font titulo = new Font("Segoe UI", Font.BOLD, 25);
    Font subtitulo = new Font("Segoe UI", Font.BOLD, 17);
    Font normal = new Font("Segoe UI", Font.PLAIN, 14);
    Font boton = new Font("Segoe UI", Font.BOLD, 14);

    JPanel tablaContenedor; // panel donde alternamos entre tabla y mensaje vacio

    // barra lateral que sale del logo, empieza escondida (igual que en el menu)
    private JPanel sidebar;
    private boolean sidebarVisible = false;

    public PaneldeAdmin() {

        setTitle("ChicxBurger - Administracion");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        sidebar = EstiloAdmin.construirBarraLateral();

        add(buildHeader(), BorderLayout.NORTH);
        add(buildContenido(), BorderLayout.CENTER);
    }

    // abre o cierra la barra lateral segun como este en ese momento
    private void toggleSidebar() {
        Container raiz = getContentPane();
        if (sidebarVisible) {
            raiz.remove(sidebar);
            sidebarVisible = false;
        } else {
            raiz.add(sidebar, BorderLayout.WEST);
            sidebarVisible = true;
        }
        raiz.revalidate();
        raiz.repaint();
    }

    // ==========================================
    // BARRA DE ARRIBA, igual que la del menu (marron con borde dorado)
    // ==========================================
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Chiksxburgermenu.MAROON);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 4, 0, Chiksxburgermenu.GOLD));
        header.setPreferredSize(new Dimension(10, 76));

        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        logoPanel.setOpaque(false);
        logoPanel.setBorder(new EmptyBorder(0, 28, 0, 0));

        // este icono ahora es el botoncito que abre y cierra la barra lateral
        JLabel mark = new JLabel(EstiloAdmin.crearIconoLogo(40, Chiksxburgermenu.GOLD, Chiksxburgermenu.BROWN_950));
        mark.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        mark.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { toggleSidebar(); }
        });
        logoPanel.add(mark);

        JPanel logoText = new JPanel();
        logoText.setOpaque(false);
        logoText.setLayout(new BoxLayout(logoText, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("CHICXBURGER");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Chiksxburgermenu.CREAM);
        JLabel subtitle = new JLabel("PANEL ADMINISTRATIVO");
        subtitle.setFont(new Font("Segoe UI", Font.BOLD, 10));
        subtitle.setForeground(Chiksxburgermenu.GOLD);
        logoText.add(title);
        logoText.add(subtitle);
        logoPanel.add(logoText);

        JLabel usuarioAdmin = new JLabel("Administrador  ");
        usuarioAdmin.setFont(subtitulo);
        usuarioAdmin.setForeground(Chiksxburgermenu.GOLD);

        header.add(logoPanel, BorderLayout.WEST);
        header.add(usuarioAdmin, BorderLayout.EAST);
        return header;
    }

    // ==========================================
    // CONTENIDO CENTRAL (tarjetas + accesos rapidos + pedidos recientes)
    // ==========================================
    private JPanel buildContenido() {
        JPanel contenido = new JPanel();
        contenido.setBackground(Chiksxburgermenu.CREAM);
        contenido.setBorder(new EmptyBorder(25, 35, 25, 35));
        contenido.setLayout(new BorderLayout(20, 20));

        JPanel tituloPanel = new JPanel(new BorderLayout());
        tituloPanel.setBackground(Chiksxburgermenu.CREAM);

        JLabel bienvenida = new JLabel("Panel de control");
        bienvenida.setFont(titulo);
        bienvenida.setForeground(Chiksxburgermenu.BROWN_950);

        JLabel descripcion = new JLabel(
                "Administra los usuarios, pedidos, productos, stock, promociones y catalogo de ChicxBurger"
        );
        descripcion.setFont(normal);
        descripcion.setForeground(Chiksxburgermenu.INK_SOFT);

        JPanel textoTitulo = new JPanel();
        textoTitulo.setBackground(Chiksxburgermenu.CREAM);
        textoTitulo.setLayout(new BoxLayout(textoTitulo, BoxLayout.Y_AXIS));
        textoTitulo.add(bienvenida);
        textoTitulo.add(Box.createVerticalStrut(5));
        textoTitulo.add(descripcion);

        tituloPanel.add(textoTitulo, BorderLayout.WEST);
        contenido.add(tituloPanel, BorderLayout.NORTH);

        JPanel centro = new JPanel();
        centro.setBackground(Chiksxburgermenu.CREAM);
        centro.setLayout(new BorderLayout(20, 20));

        // ===== TARJETAS CON ICONO DIBUJADO (ya no son emoji, no salen cuadros rotos) =====
        JPanel tarjetas = new JPanel(new GridLayout(1, 4, 18, 0));
        tarjetas.setBackground(Chiksxburgermenu.CREAM);

        tarjetas.add(crearTarjeta(EstiloAdmin.iconoHamburguesa(30, Chiksxburgermenu.RED),
                "HAMBURGUESAS", String.valueOf(contarProductosPorCategoria("Hamburguesas")), Chiksxburgermenu.RED));
        tarjetas.add(crearTarjeta(EstiloAdmin.iconoBebida(30, Chiksxburgermenu.AMBAR),
                "BEBIDAS", String.valueOf(contarProductosPorCategoria("Bebidas")), Chiksxburgermenu.AMBAR));
        tarjetas.add(crearTarjeta(EstiloAdmin.iconoUsuario(30, Chiksxburgermenu.GOLD_DEEP),
                "USUARIOS", String.valueOf(contarUsuariosActivos()), Chiksxburgermenu.GOLD_DEEP));
        tarjetas.add(crearTarjeta(EstiloAdmin.iconoEtiqueta(30, Chiksxburgermenu.AMBAR_CLARO),
                "PROMOCIONES", String.valueOf(contarPromocionesActivas()), Chiksxburgermenu.AMBAR_CLARO));

        centro.add(tarjetas, BorderLayout.NORTH);

        JPanel inferior = new JPanel(new BorderLayout(20, 0));
        inferior.setBackground(Chiksxburgermenu.CREAM);

        JPanel gestion = new JPanel();
        gestion.setBackground(Color.WHITE);
        gestion.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Chiksxburgermenu.CREAM_2, 2, true),
                new EmptyBorder(20, 20, 20, 20)
        ));
        gestion.setLayout(new BoxLayout(gestion, BoxLayout.Y_AXIS));

        JLabel tituloGestion = new JLabel("Accesos rapidos");
        tituloGestion.setFont(subtitulo);
        tituloGestion.setForeground(Chiksxburgermenu.BROWN_950);
        tituloGestion.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel textoGestion = new JLabel("Selecciona una opcion");
        textoGestion.setFont(normal);
        textoGestion.setForeground(Chiksxburgermenu.INK_SOFT);
        textoGestion.setAlignmentX(Component.CENTER_ALIGNMENT);

        EstiloAdmin.BotonSolido btnUsuarios = new EstiloAdmin.BotonSolido(
                "GESTION USUARIOS", Chiksxburgermenu.RED, Color.WHITE);
        btnUsuarios.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnUsuarios.setMaximumSize(new Dimension(240, 55));
        btnUsuarios.setPreferredSize(new Dimension(240, 55));

        EstiloAdmin.BotonSolido btnPedidos = new EstiloAdmin.BotonSolido(
                "GESTION PEDIDOS", Chiksxburgermenu.BROWN_950, Color.WHITE);
        btnPedidos.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnPedidos.setMaximumSize(new Dimension(240, 55));
        btnPedidos.setPreferredSize(new Dimension(240, 55));

        gestion.add(tituloGestion);
        gestion.add(Box.createVerticalStrut(5));
        gestion.add(textoGestion);
        gestion.add(Box.createVerticalStrut(25));
        gestion.add(btnUsuarios);
        gestion.add(Box.createVerticalStrut(15));
        gestion.add(btnPedidos);

        btnUsuarios.addActionListener(e -> NavegadorAdmin.mostrar(new GestionUsuarios()));
        btnPedidos.addActionListener(e -> NavegadorAdmin.mostrar(new GestionPedidos()));

        JPanel tablaPanel = new JPanel(new BorderLayout(10, 10));
        tablaPanel.setBackground(Color.WHITE);
        tablaPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Chiksxburgermenu.CREAM_2, 2, true),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel tituloPedidos = new JLabel("Pedidos recientes");
        tituloPedidos.setFont(subtitulo);
        tituloPedidos.setForeground(Chiksxburgermenu.BROWN_950);
        tablaPanel.add(tituloPedidos, BorderLayout.NORTH);

        tablaContenedor = new JPanel(new BorderLayout());
        tablaContenedor.setBackground(Color.WHITE);
        tablaPanel.add(tablaContenedor, BorderLayout.CENTER);

        cargarPanelPedidosRecientes();

        inferior.add(gestion, BorderLayout.WEST);
        inferior.add(tablaPanel, BorderLayout.CENTER);

        centro.add(inferior, BorderLayout.CENTER);
        contenido.add(centro, BorderLayout.CENTER);

        return contenido;
    }

    // ================= CONSULTAS A LA BASE DE DATOS =================

    private int contarProductosPorCategoria(String nombreCategoria) {
        String sql = "SELECT COUNT(*) FROM PRODUCTO p JOIN CATEGORIA c ON p.id_categoria = c.id_categoria "
                + "WHERE c.nombre_categoria = ? AND p.estado = 1";
        try (Connection con = conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombreCategoria);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al contar productos: " + e.getMessage());
        }
        return 0;
    }

    private int contarUsuariosActivos() {
        String sql = "SELECT COUNT(*) FROM USUARIO WHERE estado = 1";
        try (Connection con = conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al contar usuarios: " + e.getMessage());
        }
        return 0;
    }

    private int contarPromocionesActivas() {
        String sql = "SELECT COUNT(*) FROM PROMOCION WHERE estado = 1";
        try (Connection con = conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al contar promociones: " + e.getMessage());
        }
        return 0;
    }

    // arma la tabla de pedidos recientes, o un mensaje si no hay ninguno todavia
    private void cargarPanelPedidosRecientes() {

        tablaContenedor.removeAll();

        String[] columnas = {"No. Venta", "Usuario", "Metodo de pago", "Total", "Fecha"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String sql = "SELECT v.id_venta, u.nombre_completo, m.nombre_metodo, v.total, v.fecha_hora "
                + "FROM VENTA v "
                + "JOIN USUARIO u ON v.id_usuario = u.id_usuario "
                + "JOIN METODO_PAGO m ON v.id_metodo_pago = m.id_metodo_pago "
                + "ORDER BY v.fecha_hora DESC LIMIT 5";

        try (Connection con = conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                modeloTabla.addRow(new Object[]{
                    rs.getInt("id_venta"),
                    rs.getString("nombre_completo"),
                    rs.getString("nombre_metodo"),
                    String.format("Q%.2f", rs.getDouble("total")),
                    rs.getTimestamp("fecha_hora")
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar pedidos recientes: " + e.getMessage());
        }

        if (modeloTabla.getRowCount() == 0) {

            JPanel vacio = new JPanel(new GridBagLayout());
            vacio.setBackground(Color.WHITE);

            JPanel contenidoVacio = new JPanel();
            contenidoVacio.setBackground(Color.WHITE);
            contenidoVacio.setLayout(new BoxLayout(contenidoVacio, BoxLayout.Y_AXIS));

            JLabel icono = new JLabel(EstiloAdmin.iconoRecibo(40, Chiksxburgermenu.CREAM_2));
            icono.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel mensaje = new JLabel("Todavia no hay pedidos registrados");
            mensaje.setFont(new Font("Segoe UI", Font.BOLD, 15));
            mensaje.setForeground(Chiksxburgermenu.BROWN_950);
            mensaje.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel submensaje = new JLabel("Registra tu primer pedido desde Gestion de pedidos");
            submensaje.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            submensaje.setForeground(Chiksxburgermenu.INK_SOFT);
            submensaje.setAlignmentX(Component.CENTER_ALIGNMENT);

            contenidoVacio.add(icono);
            contenidoVacio.add(Box.createVerticalStrut(8));
            contenidoVacio.add(mensaje);
            contenidoVacio.add(Box.createVerticalStrut(4));
            contenidoVacio.add(submensaje);

            vacio.add(contenidoVacio);
            tablaContenedor.add(vacio, BorderLayout.CENTER);

        } else {

            JTable tabla = new JTable(modeloTabla);
            tabla.setFont(normal);
            tabla.setRowHeight(35);
            tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
            tabla.getTableHeader().setBackground(Chiksxburgermenu.BROWN_950);
            tabla.getTableHeader().setForeground(Color.WHITE);
            tabla.setSelectionBackground(Chiksxburgermenu.CREAM_2);
            tabla.setSelectionForeground(Chiksxburgermenu.BROWN_950);
            tabla.setShowGrid(false);
            tabla.setIntercellSpacing(new Dimension(0, 0));

            // filas alternadas para que se lea mejor
            tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value,
                        boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    if (!isSelected) {
                        c.setBackground(row % 2 == 0 ? Color.WHITE : Chiksxburgermenu.CREAM);
                    }
                    return c;
                }
            });

            JScrollPane scroll = new JScrollPane(tabla);
            scroll.setBorder(BorderFactory.createEmptyBorder());
            tablaContenedor.add(scroll, BorderLayout.CENTER);
        }

        tablaContenedor.revalidate();
        tablaContenedor.repaint();
    }

    // ================= UI HELPERS =================

    private JPanel crearTarjeta(Icon icono, String titulo, String numero, Color color) {
        JPanel tarjeta = new JPanel();
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 2, true),
                new EmptyBorder(15, 15, 15, 15)
        ));
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));

        JLabel iconoLabel = new JLabel(icono);
        iconoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel tituloLabel = new JLabel(titulo);
        tituloLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        tituloLabel.setForeground(Chiksxburgermenu.INK_SOFT);
        tituloLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel numeroLabel = new JLabel(numero);
        numeroLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        numeroLabel.setForeground(color);
        numeroLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        tarjeta.add(iconoLabel);
        tarjeta.add(Box.createVerticalStrut(6));
        tarjeta.add(tituloLabel);
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(numeroLabel);

        return tarjeta;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                NavegadorAdmin.mostrar(new PaneldeAdmin());
            }
        });
    }
}