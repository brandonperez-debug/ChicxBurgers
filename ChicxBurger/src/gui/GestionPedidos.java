package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import ChicxBurgerDB.Producto;
import ChicxBurgerDB.ProductoDAO;
import main.Conexion.conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GestionPedidos extends JFrame {

    Color ROJO = Color.decode("#f53418");
    Color Naranja = Color.decode("#f57c18");
    Color MOSTAZA = Color.decode("#A28813");
    Color DORADO = Color.decode("#db8f1b");
    Color BEIGE = Color.decode("#e0cfc8");
    Color CAFE = Color.decode("#661d05");

    Font titulo = new Font("Segoe UI", Font.BOLD, 25);
    Font subtitulo = new Font("Segoe UI", Font.PLAIN, 14);
    Font normal = new Font("Segoe UI", Font.PLAIN, 14);

    JComboBox<String> cbProducto;
    JTextField txtCantidad;
    JComboBox<String> cbMetodoPago;
    JLabel lblTotal;

    JTable tablaCarrito;
    DefaultTableModel modeloCarrito;

    JTable tablaHistorial;
    DefaultTableModel modeloHistorial;

    // ===== datos auxiliares (antes estaban mal ubicados como variables locales) =====
    List<Producto> productosDisponibles = new ArrayList<>();
    HashMap<String, Integer> metodosPagoMap = new HashMap<>();

    // listas paralelas a las filas de la tabla carrito, para saber que id_producto
    // y cantidad corresponde a cada fila (la JTable solo guarda texto)
    List<Integer> idsProductoCarrito = new ArrayList<>();
    List<Integer> cantidadesCarrito = new ArrayList<>();

    // TODO (cuando ya no sea urgencia): esto debe venir de la sesion real de login,
    // por ahora se deja fijo en el usuario admin1 (id_usuario = 1) para que compile y funcione.
    int idUsuarioActual = 1;

    // TODO: idem, debe venir de un TURNO abierto real. Debe existir una fila en TURNO
    // con este id antes de registrar una venta, o fallara por la llave foranea.
    int idTurnoActual = 1;

    public GestionPedidos() {

        setTitle("Chiksx Burger - Gestion de Pedidos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel principal = new JPanel(new BorderLayout());
        principal.setBackground(BEIGE);

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(Color.WHITE);
        encabezado.setBorder(new EmptyBorder(15, 30, 15, 30));

        JLabel lblTitulo = new JLabel("Gestion de Pedidos");
        lblTitulo.setFont(titulo);
        lblTitulo.setForeground(CAFE);

        JLabel lblSubtitulo = new JLabel("Crea pedidos nuevos y consulta el historial de ventas");
        lblSubtitulo.setFont(subtitulo);
        lblSubtitulo.setForeground(Color.GRAY);

        JPanel textos = new JPanel();
        textos.setBackground(Color.WHITE);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.add(lblTitulo);
        textos.add(lblSubtitulo);

        encabezado.add(textos, BorderLayout.WEST);

        JLabel logo = new JLabel("CHIKSX BURGER");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logo.setForeground(ROJO);
        encabezado.add(logo, BorderLayout.EAST);

        principal.add(encabezado, BorderLayout.NORTH);

        JPanel contenido = new JPanel(new BorderLayout(20, 20));
        contenido.setBackground(BEIGE);
        contenido.setBorder(new EmptyBorder(25, 30, 25, 30));

        JPanel nuevoPedido = new JPanel();
        nuevoPedido.setBackground(Color.WHITE);
        nuevoPedido.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CAFE, 1),
                new EmptyBorder(20, 20, 20, 20)
        ));
        nuevoPedido.setLayout(new BoxLayout(nuevoPedido, BoxLayout.Y_AXIS));

        JLabel tituloNuevo = new JLabel("Nuevo pedido");
        tituloNuevo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tituloNuevo.setForeground(CAFE);
        tituloNuevo.setAlignmentX(Component.LEFT_ALIGNMENT);

        nuevoPedido.add(tituloNuevo);
        nuevoPedido.add(Box.createVerticalStrut(15));

        JLabel lblProducto = new JLabel("Producto:");
        lblProducto.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblProducto.setForeground(CAFE);
        lblProducto.setAlignmentX(Component.LEFT_ALIGNMENT);

        cbProducto = new JComboBox<>();
        cbProducto.setMaximumSize(new Dimension(230, 35));
        cbProducto.setAlignmentX(Component.LEFT_ALIGNMENT);

        nuevoPedido.add(lblProducto);
        nuevoPedido.add(cbProducto);
        nuevoPedido.add(Box.createVerticalStrut(12));

        JLabel lblCantidad = new JLabel("Cantidad:");
        lblCantidad.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblCantidad.setForeground(CAFE);
        lblCantidad.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtCantidad = new JTextField();
        txtCantidad.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtCantidad.setMaximumSize(new Dimension(230, 35));
        txtCantidad.setAlignmentX(Component.LEFT_ALIGNMENT);

        nuevoPedido.add(lblCantidad);
        nuevoPedido.add(txtCantidad);
        nuevoPedido.add(Box.createVerticalStrut(12));

        JLabel lblMetodo = new JLabel("Metodo de pago:");
        lblMetodo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblMetodo.setForeground(CAFE);
        lblMetodo.setAlignmentX(Component.LEFT_ALIGNMENT);

        cbMetodoPago = new JComboBox<>();
        cbMetodoPago.setMaximumSize(new Dimension(230, 35));
        cbMetodoPago.setAlignmentX(Component.LEFT_ALIGNMENT);

        nuevoPedido.add(lblMetodo);
        nuevoPedido.add(cbMetodoPago);
        nuevoPedido.add(Box.createVerticalStrut(20));

        JButton btnAgregar = crearBoton("AGREGAR AL PEDIDO", ROJO);
        JButton btnQuitar = crearBoton("QUITAR PRODUCTO", Naranja);
        JButton btnRegistrar = crearBoton("REGISTRAR PEDIDO", CAFE);
        JButton btnCancelar = crearBoton("CANCELAR PEDIDO", MOSTAZA);

        nuevoPedido.add(btnAgregar);
        nuevoPedido.add(Box.createVerticalStrut(8));
        nuevoPedido.add(btnQuitar);
        nuevoPedido.add(Box.createVerticalStrut(8));
        nuevoPedido.add(btnRegistrar);
        nuevoPedido.add(Box.createVerticalStrut(8));
        nuevoPedido.add(btnCancelar);
        nuevoPedido.add(Box.createVerticalStrut(20));

        lblTotal = new JLabel("Total: Q0.00");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTotal.setForeground(ROJO);
        lblTotal.setAlignmentX(Component.LEFT_ALIGNMENT);

        nuevoPedido.add(lblTotal);

        JPanel derecho = new JPanel(new BorderLayout(0, 20));
        derecho.setBackground(BEIGE);

        JPanel panelCarrito = new JPanel(new BorderLayout(10, 10));
        panelCarrito.setBackground(Color.WHITE);
        panelCarrito.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CAFE, 1),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel tituloCarrito = new JLabel("Productos del pedido actual");
        tituloCarrito.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tituloCarrito.setForeground(CAFE);
        panelCarrito.add(tituloCarrito, BorderLayout.NORTH);

        String[] columnasCarrito = {"Producto", "Cantidad", "Precio unitario", "Subtotal"};
        modeloCarrito = new DefaultTableModel(columnasCarrito, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        tablaCarrito = new JTable(modeloCarrito);
        tablaCarrito.setFont(normal);
        tablaCarrito.setRowHeight(30);
        tablaCarrito.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaCarrito.getTableHeader().setBackground(CAFE);
        tablaCarrito.getTableHeader().setForeground(Color.WHITE);
        tablaCarrito.setSelectionBackground(BEIGE);
        tablaCarrito.setSelectionForeground(CAFE);

        JScrollPane scrollCarrito = new JScrollPane(tablaCarrito);
        scrollCarrito.setBorder(BorderFactory.createEmptyBorder());
        scrollCarrito.setPreferredSize(new Dimension(0, 220));
        panelCarrito.add(scrollCarrito, BorderLayout.CENTER);

        JPanel panelHistorial = new JPanel(new BorderLayout(10, 10));
        panelHistorial.setBackground(Color.WHITE);
        panelHistorial.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CAFE, 1),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel tituloHistorial = new JLabel("Historial de ventas");
        tituloHistorial.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tituloHistorial.setForeground(CAFE);
        panelHistorial.add(tituloHistorial, BorderLayout.NORTH);

        String[] columnasHistorial = {"No. Venta", "Fecha", "Usuario", "Metodo de pago", "Total"};
        modeloHistorial = new DefaultTableModel(columnasHistorial, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        tablaHistorial = new JTable(modeloHistorial);
        tablaHistorial.setFont(normal);
        tablaHistorial.setRowHeight(30);
        tablaHistorial.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaHistorial.getTableHeader().setBackground(CAFE);
        tablaHistorial.getTableHeader().setForeground(Color.WHITE);
        tablaHistorial.setSelectionBackground(BEIGE);
        tablaHistorial.setSelectionForeground(CAFE);

        JScrollPane scrollHistorial = new JScrollPane(tablaHistorial);
        scrollHistorial.setBorder(BorderFactory.createEmptyBorder());
        panelHistorial.add(scrollHistorial, BorderLayout.CENTER);

        derecho.add(panelCarrito, BorderLayout.NORTH);
        derecho.add(panelHistorial, BorderLayout.CENTER);

        contenido.add(nuevoPedido, BorderLayout.WEST);
        contenido.add(derecho, BorderLayout.CENTER);

        principal.add(contenido, BorderLayout.CENTER);
        add(principal);

        // ===== cargar datos iniciales =====
        cargarProductos();
        cargarMetodosPago();
        cargarHistorial();

        // ===== eventos =====

        btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarProductoAlCarrito();
            }
        });

        btnQuitar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int fila = tablaCarrito.getSelectedRow();
                if (fila == -1) {
                    JOptionPane.showMessageDialog(GestionPedidos.this,
                            "Selecciona un producto del pedido actual.");
                    return;
                }
                modeloCarrito.removeRow(fila);
                idsProductoCarrito.remove(fila);
                cantidadesCarrito.remove(fila);
                actualizarTotal();
            }
        });

        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vaciarCarrito();
            }
        });

        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarPedido();
            }
        });

        setVisible(true);
    }

    // Cargar datos

    private void cargarProductos() {
        ProductoDAO productoDAO = new ProductoDAO();
        try {
            productosDisponibles = productoDAO.listarProductosActivos();
            for (Producto p : productosDisponibles) {
                cbProducto.addItem(p.getNombreProducto());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar productos: " + e.getMessage());
        }
    }

    private void cargarMetodosPago() {
        String sql = "SELECT id_metodo_pago, nombre_metodo FROM METODO_PAGO";
        try (Connection con = conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id_metodo_pago");
                String nombre = rs.getString("nombre_metodo");
                metodosPagoMap.put(nombre, id);
                cbMetodoPago.addItem(nombre);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar metodos de pago: " + e.getMessage());
        }
    }

    private void cargarHistorial() {
        String sql = "SELECT v.id_venta, v.fecha_hora, u.nombre_completo, "
                + "m.nombre_metodo, v.total "
                + "FROM VENTA v "
                + "JOIN USUARIO u ON v.id_usuario = u.id_usuario "
                + "JOIN METODO_PAGO m ON v.id_metodo_pago = m.id_metodo_pago "
                + "ORDER BY v.fecha_hora DESC";

        try (Connection con = conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                modeloHistorial.addRow(new Object[]{
                    rs.getInt("id_venta"),
                    rs.getTimestamp("fecha_hora"),
                    rs.getString("nombre_completo"),
                    rs.getString("nombre_metodo"),
                    String.format("Q%.2f", rs.getDouble("total"))
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar historial: " + e.getMessage());
        }
    }

    //logica de carrito
    private void agregarProductoAlCarrito() {

        String nombreSeleccionado = (String) cbProducto.getSelectedItem();
        if (nombreSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "No hay productos disponibles.");
            return;
        }

        int cantidad;
        try {
            cantidad = Integer.parseInt(txtCantidad.getText().trim());
            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a 0.");
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Escribe una cantidad valida (numero entero).");
            return;
        }

        Producto productoElegido = null;
        for (Producto p : productosDisponibles) {
            if (p.getNombreProducto().equals(nombreSeleccionado)) {
                productoElegido = p;
                break;
            }
        }

        if (productoElegido == null) {
            JOptionPane.showMessageDialog(this, "No se encontro el producto seleccionado.");
            return;
        }

        double subtotal = productoElegido.getPrecio() * cantidad;

        modeloCarrito.addRow(new Object[]{
            productoElegido.getNombreProducto(),
            cantidad,
            String.format("Q%.2f", productoElegido.getPrecio()),
            String.format("Q%.2f", subtotal)
        });

        idsProductoCarrito.add(productoElegido.getIdProducto());
        cantidadesCarrito.add(cantidad);

        txtCantidad.setText("");
        actualizarTotal();
    }

    private void actualizarTotal() {
        double total = 0;
        for (int i = 0; i < modeloCarrito.getRowCount(); i++) {
            String nombre = (String) modeloCarrito.getValueAt(i, 0);
            int cantidad = cantidadesCarrito.get(i);
            for (Producto p : productosDisponibles) {
                if (p.getNombreProducto().equals(nombre)) {
                    total += p.getPrecio() * cantidad;
                    break;
                }
            }
        }
        lblTotal.setText(String.format("Total: Q%.2f", total));
    }

    private void vaciarCarrito() {
        modeloCarrito.setRowCount(0);
        idsProductoCarrito.clear();
        cantidadesCarrito.clear();
        lblTotal.setText("Total: Q0.00");
    }

    // ================= REGISTRAR VENTA =================

    private void registrarPedido() {

        if (modeloCarrito.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Agrega al menos un producto antes de registrar.");
            return;
        }

        String metodoSeleccionado = (String) cbMetodoPago.getSelectedItem();
        if (metodoSeleccionado == null || !metodosPagoMap.containsKey(metodoSeleccionado)) {
            JOptionPane.showMessageDialog(this, "Selecciona un metodo de pago valido.");
            return;
        }
        int idMetodoPago = metodosPagoMap.get(metodoSeleccionado);

        double total = 0;
        for (int i = 0; i < modeloCarrito.getRowCount(); i++) {
            String nombre = (String) modeloCarrito.getValueAt(i, 0);
            int cantidad = cantidadesCarrito.get(i);
            for (Producto p : productosDisponibles) {
                if (p.getNombreProducto().equals(nombre)) {
                    total += p.getPrecio() * cantidad;
                    break;
                }
            }
        }

        Connection con = null;

        try {
            con = conexion.getConnection();
            con.setAutoCommit(false); // agrupamos VENTA + DETALLE_VENTA en una sola transaccion

            String sqlVenta = "INSERT INTO VENTA (total, descuento_total, id_usuario, id_metodo_pago, id_turno) "
                    + "VALUES (?, 0, ?, ?, ?)";

            int idVentaGenerado;

            try (PreparedStatement psVenta = con.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS)) {
                psVenta.setDouble(1, total);
                psVenta.setInt(2, idUsuarioActual);
                psVenta.setInt(3, idMetodoPago);
                psVenta.setInt(4, idTurnoActual);
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
                    + "VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement psDetalle = con.prepareStatement(sqlDetalle)) {
                for (int i = 0; i < modeloCarrito.getRowCount(); i++) {
                    int idProducto = idsProductoCarrito.get(i);
                    int cantidad = cantidadesCarrito.get(i);

                    double precioUnitario = 0;
                    for (Producto p : productosDisponibles) {
                        if (p.getIdProducto() == idProducto) {
                            precioUnitario = p.getPrecio();
                            break;
                        }
                    }
                    double subtotal = precioUnitario * cantidad;

                    psDetalle.setInt(1, idVentaGenerado);
                    psDetalle.setInt(2, idProducto);
                    psDetalle.setInt(3, cantidad);
                    psDetalle.setDouble(4, precioUnitario);
                    psDetalle.setDouble(5, subtotal);
                    psDetalle.addBatch();
                }
                psDetalle.executeBatch();
            }

            con.commit();

            JOptionPane.showMessageDialog(this, "Pedido registrado correctamente. No. Venta: " + idVentaGenerado);

            vaciarCarrito();
            modeloHistorial.setRowCount(0);
            cargarHistorial();

        } catch (SQLException e) {

            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    // si ni siquiera se puede revertir, solo lo mostramos
                }
            }

            JOptionPane.showMessageDialog(this, "Error al registrar el pedido: " + e.getMessage());

        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException ex) {
                    // conexion ya cerrada o invalida, no hacemos nada mas
                }
            }
        }
    }

    // Botones
    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        boton.setForeground(Color.WHITE);
        boton.setBackground(color);
        boton.setFocusPainted(false);
        boton.setAlignmentX(Component.LEFT_ALIGNMENT);
        boton.setMaximumSize(new Dimension(230, 40));
        boton.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }

   // main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GestionPedidos();
            }
        });
    }
}