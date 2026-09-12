package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import main.Conexion.conexion;
import java.sql.*;

public class PaneldeAdmin extends JFrame {

    Color ROJO = Color.decode("#f53418");
    Color NARANJA = Color.decode("#f6781c");
    Color MOSTAZA = Color.decode("#a28813");
    Color DORADO = Color.decode("#db8f1b");
    Color BEIGE = Color.decode("#e0cfc8");
    Color CAFE = Color.decode("#661d05");

    Font titulo = new Font("Segoe UI", Font.BOLD, 25);
    Font subtitulo = new Font("Segoe UI", Font.BOLD, 17);
    Font normal = new Font("Segoe UI", Font.PLAIN, 14);
    Font boton = new Font("Segoe UI", Font.BOLD, 14);

    JPanel tablaContenedor; // panel donde alternamos entre tabla y mensaje vacio

    public PaneldeAdmin() {

        setTitle("Chiksx Burger - Administracion");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        JPanel menu = new JPanel();
        menu.setBackground(CAFE);
        menu.setPreferredSize(new Dimension(230, 0));
        menu.setLayout(new BorderLayout());

        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(CAFE);
        logoPanel.setBorder(new EmptyBorder(30, 15, 25, 15));
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));

        JLabel logo = new JLabel("CHIKSX");
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 30));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel burger = new JLabel("BURGER");
        burger.setForeground(DORADO);
        burger.setFont(new Font("Segoe UI", Font.BOLD, 20));
        burger.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSeparator linea = new JSeparator();
        linea.setForeground(DORADO);
        linea.setMaximumSize(new Dimension(180, 2));

        logoPanel.add(logo);
        logoPanel.add(burger);
        logoPanel.add(Box.createVerticalStrut(15));
        logoPanel.add(linea);

        menu.add(logoPanel, BorderLayout.NORTH);

        JPanel opciones = new JPanel();
        opciones.setBackground(CAFE);
        opciones.setLayout(new BoxLayout(opciones, BoxLayout.Y_AXIS));

        JButton inicio = crearBotonMenu("Inicio");
        JButton usuarios = crearBotonMenu("Gestion de usuarios");
        JButton pedidos = crearBotonMenu("Gestion de pedidos");
        JButton producto = crearBotonMenu("Gestion de producto");
        JButton stock = crearBotonMenu("Gestion de stock");
        JButton promociones = crearBotonMenu("Gestion de promociones");
        JButton catalogo = crearBotonMenu("Gestion de catalogo y menu dinamico");
        JButton salir = crearBotonMenu("Cerrar sesion");

        opciones.add(inicio);
        opciones.add(usuarios);
        opciones.add(pedidos);
        opciones.add(producto);
        opciones.add(stock);
        opciones.add(promociones);
        opciones.add(catalogo);

        opciones.add(Box.createVerticalGlue());

        opciones.add(salir);

        menu.add(opciones, BorderLayout.CENTER);

        usuarios.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GestionUsuarios();
            }
        });

        pedidos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GestionPedidos();
            }
        });

        producto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GestionProducto();
            }
        });

        stock.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GestionStock();
            }
        });

        promociones.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GestionPromociones();
            }
        });

        catalogo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GestionCatalogo();
            }
        });

        salir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int opcion = JOptionPane.showConfirmDialog(
                        PaneldeAdmin.this,
                        "¿Deseas cerrar sesion?",
                        "Cerrar sesion",
                        JOptionPane.YES_NO_OPTION
                );
                if (opcion == JOptionPane.YES_OPTION) {
                    dispose();
                }
            }
        });

        JPanel principal = new JPanel(new BorderLayout());
        principal.setBackground(BEIGE);

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(Color.WHITE);
        encabezado.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0, 0, 0, 25)),
                new EmptyBorder(15, 30, 15, 30)
        ));

        JLabel nombre = new JLabel("Chiksx Burger");
        nombre.setFont(titulo);
        nombre.setForeground(CAFE);

        JLabel administrador = new JLabel("Panel administrativo");
        administrador.setFont(normal);
        administrador.setForeground(Color.GRAY);

        JPanel textos = new JPanel();
        textos.setBackground(Color.WHITE);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.add(nombre);
        textos.add(administrador);

        encabezado.add(textos, BorderLayout.WEST);

        JLabel usuarioAdmin = new JLabel("Administrador");
        usuarioAdmin.setFont(subtitulo);
        usuarioAdmin.setForeground(CAFE);
        encabezado.add(usuarioAdmin, BorderLayout.EAST);

        principal.add(encabezado, BorderLayout.NORTH);

        JPanel contenido = new JPanel();
        contenido.setBackground(BEIGE);
        contenido.setBorder(new EmptyBorder(25, 35, 25, 35));
        contenido.setLayout(new BorderLayout(20, 20));

        JPanel tituloPanel = new JPanel(new BorderLayout());
        tituloPanel.setBackground(BEIGE);

        JLabel bienvenida = new JLabel("Panel de control");
        bienvenida.setFont(titulo);
        bienvenida.setForeground(CAFE);

        JLabel descripcion = new JLabel(
                "Administra los usuarios, pedidos, productos, stock, promociones y catalogo de Chiksx Burger"
        );
        descripcion.setFont(normal);
        descripcion.setForeground(Color.DARK_GRAY);

        JPanel textoTitulo = new JPanel();
        textoTitulo.setBackground(BEIGE);
        textoTitulo.setLayout(new BoxLayout(textoTitulo, BoxLayout.Y_AXIS));
        textoTitulo.add(bienvenida);
        textoTitulo.add(Box.createVerticalStrut(5));
        textoTitulo.add(descripcion);

        tituloPanel.add(textoTitulo, BorderLayout.WEST);
        contenido.add(tituloPanel, BorderLayout.NORTH);

        JPanel centro = new JPanel();
        centro.setBackground(BEIGE);
        centro.setLayout(new BorderLayout(20, 20));

        // ===== TARJETAS CON ICONO, CONECTADAS A LA BASE DE DATOS =====
        JPanel tarjetas = new JPanel(new GridLayout(1, 4, 18, 0));
        tarjetas.setBackground(BEIGE);

        tarjetas.add(crearTarjeta("🍔", "HAMBURGUESAS", String.valueOf(contarProductosPorCategoria("Hamburguesas")), ROJO));
        tarjetas.add(crearTarjeta("🥤", "BEBIDAS", String.valueOf(contarProductosPorCategoria("Bebidas")), NARANJA));
        tarjetas.add(crearTarjeta("👤", "USUARIOS", String.valueOf(contarUsuariosActivos()), MOSTAZA));
        tarjetas.add(crearTarjeta("🏷️", "PROMOCIONES", String.valueOf(contarPromocionesActivas()), DORADO));

        centro.add(tarjetas, BorderLayout.NORTH);

        JPanel inferior = new JPanel(new BorderLayout(20, 0));
        inferior.setBackground(BEIGE);

        JPanel gestion = new JPanel();
        gestion.setBackground(Color.WHITE);
        gestion.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 0, 0, 25), 1),
                new EmptyBorder(20, 20, 20, 20)
        ));
        gestion.setLayout(new BoxLayout(gestion, BoxLayout.Y_AXIS));

        JLabel tituloGestion = new JLabel("Accesos rapidos");
        tituloGestion.setFont(subtitulo);
        tituloGestion.setForeground(CAFE);
        tituloGestion.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel textoGestion = new JLabel("Selecciona una opcion");
        textoGestion.setFont(normal);
        textoGestion.setForeground(Color.GRAY);
        textoGestion.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnUsuarios = crearBotonGestion("GESTION USUARIOS", ROJO);
        JButton btnPedidos = crearBotonGestion("GESTION PEDIDOS", CAFE);

        gestion.add(tituloGestion);
        gestion.add(Box.createVerticalStrut(5));
        gestion.add(textoGestion);
        gestion.add(Box.createVerticalStrut(25));
        gestion.add(btnUsuarios);
        gestion.add(Box.createVerticalStrut(15));
        gestion.add(btnPedidos);

        btnUsuarios.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GestionUsuarios();
            }
        });

        btnPedidos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GestionPedidos();
            }
        });

        JPanel tablaPanel = new JPanel(new BorderLayout(10, 10));
        tablaPanel.setBackground(Color.WHITE);
        tablaPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 0, 0, 25), 1),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel tituloPedidos = new JLabel("Pedidos recientes");
        tituloPedidos.setFont(subtitulo);
        tituloPedidos.setForeground(CAFE);
        tablaPanel.add(tituloPedidos, BorderLayout.NORTH);

        tablaContenedor = new JPanel(new BorderLayout());
        tablaContenedor.setBackground(Color.WHITE);
        tablaPanel.add(tablaContenedor, BorderLayout.CENTER);

        cargarPanelPedidosRecientes();

        inferior.add(gestion, BorderLayout.WEST);
        inferior.add(tablaPanel, BorderLayout.CENTER);

        centro.add(inferior, BorderLayout.CENTER);
        contenido.add(centro, BorderLayout.CENTER);
        principal.add(contenido, BorderLayout.CENTER);

        add(menu, BorderLayout.WEST);
        add(principal, BorderLayout.CENTER);

        setVisible(true);
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

            JLabel icono = new JLabel("🧾");
            icono.setFont(new Font("Segoe UI", Font.PLAIN, 40));
            icono.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel mensaje = new JLabel("Todavia no hay pedidos registrados");
            mensaje.setFont(new Font("Segoe UI", Font.BOLD, 15));
            mensaje.setForeground(CAFE);
            mensaje.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel submensaje = new JLabel("Registra tu primer pedido desde Gestion de pedidos");
            submensaje.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            submensaje.setForeground(Color.GRAY);
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
            tabla.getTableHeader().setBackground(CAFE);
            tabla.getTableHeader().setForeground(Color.WHITE);
            tabla.setSelectionBackground(BEIGE);
            tabla.setSelectionForeground(CAFE);
            tabla.setShowGrid(false);
            tabla.setIntercellSpacing(new Dimension(0, 0));

            // filas alternadas para que se lea mejor
            tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value,
                        boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    if (!isSelected) {
                        c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(250, 245, 242));
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

    private JButton crearBotonMenu(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(normal);
        boton.setForeground(Color.WHITE);
        boton.setBackground(CAFE);
        boton.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 10));
        boton.setHorizontalAlignment(SwingConstants.LEFT);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                boton.setBackground(ROJO);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                boton.setBackground(CAFE);
            }
        });

        return boton;
    }

    private JPanel crearTarjeta(String icono, String titulo, String numero, Color color) {
        JPanel tarjeta = new JPanel();
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 2, true),
                new EmptyBorder(15, 15, 15, 15)
        ));
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));

        JLabel iconoLabel = new JLabel(icono);
        iconoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 26));
        iconoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel tituloLabel = new JLabel(titulo);
        tituloLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        tituloLabel.setForeground(Color.GRAY);
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

    private JButton crearBotonGestion(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setForeground(Color.WHITE);
        boton.setBackground(color);
        boton.setPreferredSize(new Dimension(240, 55));
        boton.setMaximumSize(new Dimension(240, 55));
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder());
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        Color colorOriginal = color;
        Color colorHover = color.darker();

        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                boton.setBackground(colorHover);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                boton.setBackground(colorOriginal);
            }
        });

        return boton;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PaneldeAdmin();
            }
        });
    }
}