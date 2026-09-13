package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import ChicxBurgerDB.ProductoDAO;

public class GestionProducto extends JFrame {

    Color ROJO = Color.decode("#f53418");
    Color NARANJA = Color.decode("#f6781c");
    Color MOSTAZA = Color.decode("#a28813");
    Color DORADO = Color.decode("#db8f1b");
    Color BEIGE = Color.decode("#e0cfc8");
    Color CAFE = Color.decode("#661d05");

    JTextField txtNombre, txtDescripcion, txtPrecio, txtBuscar;
    JComboBox<String> cbCategoria, cbTiempoComida, cbEstado;

    JTable tabla;
    DefaultTableModel modelo;

    ProductoDAO productoDAO = new ProductoDAO();
    HashMap<String, Integer> categoriasMap = new HashMap<>();
    HashMap<String, Integer> tiemposMap = new HashMap<>();
    int idSeleccionado = -1;

    public GestionProducto() {

        setTitle("Chiksx Burger - Gestion de Producto");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel principal = new JPanel(new BorderLayout());
        principal.setBackground(BEIGE);

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(Color.WHITE);
        encabezado.setBorder(new EmptyBorder(15, 30, 15, 30));

        JLabel titulo = new JLabel("Gestion de Producto");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 25));
        titulo.setForeground(CAFE);

        JLabel subtitulo = new JLabel("Administra el catalogo de productos");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitulo.setForeground(Color.GRAY);

        JPanel textos = new JPanel();
        textos.setBackground(Color.WHITE);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.add(titulo);
        textos.add(subtitulo);
        encabezado.add(textos, BorderLayout.WEST);

        JLabel logo = new JLabel("CHIKSX BURGER");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logo.setForeground(ROJO);
        encabezado.add(logo, BorderLayout.EAST);

        principal.add(encabezado, BorderLayout.NORTH);

        JPanel contenido = new JPanel(new BorderLayout(20, 20));
        contenido.setBackground(BEIGE);
        contenido.setBorder(new EmptyBorder(25, 30, 25, 30));

        JPanel datos = new JPanel();
        datos.setBackground(Color.WHITE);
        datos.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CAFE, 1),
                new EmptyBorder(20, 20, 20, 20)
        ));
        datos.setLayout(new BoxLayout(datos, BoxLayout.Y_AXIS));

        JLabel tituloDatos = new JLabel("Datos del producto");
        tituloDatos.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tituloDatos.setForeground(CAFE);
        tituloDatos.setAlignmentX(Component.LEFT_ALIGNMENT);
        datos.add(tituloDatos);
        datos.add(Box.createVerticalStrut(15));

        txtNombre = crearCampo();
        txtDescripcion = crearCampo();
        txtPrecio = crearCampo();

        cbCategoria = new JComboBox<>();
        cbCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        cbTiempoComida = new JComboBox<>();
        cbTiempoComida.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        cbEstado = new JComboBox<>();
        cbEstado.addItem("Activo");
        cbEstado.addItem("Inactivo");
        cbEstado.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        datos.add(crearFila("Nombre:", txtNombre));
        datos.add(crearFila("Descripcion:", txtDescripcion));
        datos.add(crearFila("Precio:", txtPrecio));
        datos.add(crearFila("Categoria:", cbCategoria));
        datos.add(crearFila("Tiempo de comida:", cbTiempoComida));
        datos.add(crearFila("Estado:", cbEstado));

        datos.add(Box.createVerticalStrut(15));

        JButton btnAgregar = crearBoton("AGREGAR", ROJO);
        JButton btnEditar = crearBoton("EDITAR", NARANJA);
        JButton btnEliminar = crearBoton("DESACTIVAR", CAFE);
        JButton btnLimpiar = crearBoton("LIMPIAR", MOSTAZA);

        datos.add(btnAgregar);
        datos.add(Box.createVerticalStrut(8));
        datos.add(btnEditar);
        datos.add(Box.createVerticalStrut(8));
        datos.add(btnEliminar);
        datos.add(Box.createVerticalStrut(8));
        datos.add(btnLimpiar);

        JPanel panelTabla = new JPanel(new BorderLayout(10, 10));
        panelTabla.setBackground(Color.WHITE);
        panelTabla.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CAFE, 1),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JPanel buscarPanel = new JPanel(new BorderLayout(10, 0));
        buscarPanel.setBackground(Color.WHITE);
        JLabel buscarLabel = new JLabel("Buscar producto:");
        buscarLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        buscarLabel.setForeground(CAFE);
        txtBuscar = new JTextField();
        JButton btnBuscar = crearBoton("BUSCAR", DORADO);
        buscarPanel.add(buscarLabel, BorderLayout.WEST);
        buscarPanel.add(txtBuscar, BorderLayout.CENTER);
        buscarPanel.add(btnBuscar, BorderLayout.EAST);
        panelTabla.add(buscarPanel, BorderLayout.NORTH);

        String[] columnas = {"ID", "Nombre", "Descripcion", "Precio", "Categoria", "Tiempo", "Estado"};
        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        tabla = new JTable(modelo);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.setRowHeight(35);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabla.getTableHeader().setBackground(CAFE);
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.setSelectionBackground(BEIGE);
        tabla.setSelectionForeground(CAFE);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        panelTabla.add(scroll, BorderLayout.CENTER);

        contenido.add(datos, BorderLayout.WEST);
        contenido.add(panelTabla, BorderLayout.CENTER);
        principal.add(contenido, BorderLayout.CENTER);
        add(principal);

        cargarCombos();
        cargarProductos();

        btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (txtNombre.getText().isEmpty() || txtPrecio.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(GestionProducto.this, "Completa nombre y precio.");
                    return;
                }
                try {
                    double precio = Double.parseDouble(txtPrecio.getText());
                    int idCat = categoriasMap.get((String) cbCategoria.getSelectedItem());
                    int idTiempo = tiemposMap.get((String) cbTiempoComida.getSelectedItem());

                    productoDAO.insertarProducto(txtNombre.getText(), txtDescripcion.getText(), precio, idCat, idTiempo);
                    JOptionPane.showMessageDialog(GestionProducto.this, "Producto agregado correctamente.");
                    limpiar();
                    cargarProductos();

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(GestionProducto.this, "El precio debe ser un numero valido.");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(GestionProducto.this, "Error al agregar: " + ex.getMessage());
                }
            }
        });

        btnEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (idSeleccionado == -1) {
                    JOptionPane.showMessageDialog(GestionProducto.this, "Selecciona un producto de la tabla.");
                    return;
                }
                try {
                    double precio = Double.parseDouble(txtPrecio.getText());
                    int idCat = categoriasMap.get((String) cbCategoria.getSelectedItem());
                    int idTiempo = tiemposMap.get((String) cbTiempoComida.getSelectedItem());
                    int estado = cbEstado.getSelectedItem().equals("Activo") ? 1 : 0;

                    productoDAO.actualizarProducto(idSeleccionado, txtNombre.getText(), txtDescripcion.getText(),
                            precio, idCat, idTiempo, estado);
                    JOptionPane.showMessageDialog(GestionProducto.this, "Producto actualizado correctamente.");
                    limpiar();
                    cargarProductos();

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(GestionProducto.this, "El precio debe ser un numero valido.");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(GestionProducto.this, "Error al actualizar: " + ex.getMessage());
                }
            }
        });

        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (idSeleccionado == -1) {
                    JOptionPane.showMessageDialog(GestionProducto.this, "Selecciona un producto.");
                    return;
                }
                try {
                    productoDAO.desactivarProducto(idSeleccionado);
                    limpiar();
                    cargarProductos();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(GestionProducto.this, "Error al desactivar: " + ex.getMessage());
                }
            }
        });

        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiar();
            }
        });

        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tabla.getSelectedRow();
                if (fila == -1) return;

                idSeleccionado = (int) modelo.getValueAt(fila, 0);
                txtNombre.setText(modelo.getValueAt(fila, 1).toString());
                txtDescripcion.setText(modelo.getValueAt(fila, 2).toString());
                txtPrecio.setText(modelo.getValueAt(fila, 3).toString());
                cbCategoria.setSelectedItem(modelo.getValueAt(fila, 4).toString());
                cbTiempoComida.setSelectedItem(modelo.getValueAt(fila, 5).toString());
                cbEstado.setSelectedItem(modelo.getValueAt(fila, 6).toString());
            }
        });

        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String buscar = txtBuscar.getText().toLowerCase();
                for (int i = 0; i < tabla.getRowCount(); i++) {
                    String nombre = tabla.getValueAt(i, 1).toString().toLowerCase();
                    if (nombre.contains(buscar)) {
                        tabla.setRowSelectionInterval(i, i);
                        return;
                    }
                }
                JOptionPane.showMessageDialog(GestionProducto.this, "Producto no encontrado.");
            }
        });

        setVisible(true);
    }

    private void cargarCombos() {
        try {
            List<Object[]> categorias = productoDAO.listarCategorias();
            for (Object[] c : categorias) {
                categoriasMap.put((String) c[1], (Integer) c[0]);
                cbCategoria.addItem((String) c[1]);
            }
            List<Object[]> tiempos = productoDAO.listarTiemposComida();
            for (Object[] t : tiempos) {
                tiemposMap.put((String) t[1], (Integer) t[0]);
                cbTiempoComida.addItem((String) t[1]);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar categorias/tiempos: " + e.getMessage());
        }
    }

    private void cargarProductos() {
        modelo.setRowCount(0);
        try {
            for (Object[] fila : productoDAO.listarTodos()) {
                modelo.addRow(fila);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar productos: " + e.getMessage());
        }
    }

    private JTextField crearCampo() {
        JTextField campo = new JTextField();
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setPreferredSize(new Dimension(230, 35));
        return campo;
    }

    private JPanel crearFila(String nombre, JComponent campo) {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(270, 45));
        JLabel etiqueta = new JLabel(nombre);
        etiqueta.setFont(new Font("Segoe UI", Font.BOLD, 13));
        etiqueta.setForeground(CAFE);
        panel.add(etiqueta, BorderLayout.NORTH);
        panel.add(campo, BorderLayout.CENTER);
        return panel;
    }

    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        boton.setForeground(Color.WHITE);
        boton.setBackground(color);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }

    private void limpiar() {
        idSeleccionado = -1;
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtPrecio.setText("");
        if (cbCategoria.getItemCount() > 0) cbCategoria.setSelectedIndex(0);
        if (cbTiempoComida.getItemCount() > 0) cbTiempoComida.setSelectedIndex(0);
        cbEstado.setSelectedIndex(0);
        tabla.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GestionProducto();
            }
        });
    }
}