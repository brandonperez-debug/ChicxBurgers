package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import ChicxBurgerDB.IngredienteDAO;

public class GestionStock extends JFrame {

    Color ROJO = Color.decode("#f53418");
    Color NARANJA = Color.decode("#f6781c");
    Color MOSTAZA = Color.decode("#a28813");
    Color DORADO = Color.decode("#db8f1b");
    Color BEIGE = Color.decode("#e0cfc8");
    Color CAFE = Color.decode("#661d05");

    JTextField txtNombre, txtUnidad, txtStockActual, txtStockMinimo, txtBuscar;
    JComboBox<String> cbProveedor;

    JTable tabla;
    DefaultTableModel modelo;

    IngredienteDAO ingredienteDAO = new IngredienteDAO();
    HashMap<String, Integer> proveedoresMap = new HashMap<>();
    int idSeleccionado = -1;

    public GestionStock() {

        setTitle("Chiksx Burger - Gestion de Stock");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel principal = new JPanel(new BorderLayout());
        principal.setBackground(BEIGE);

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(Color.WHITE);
        encabezado.setBorder(new EmptyBorder(15, 30, 15, 30));

        JLabel titulo = new JLabel("Gestion de Stock");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 25));
        titulo.setForeground(CAFE);

        JLabel subtitulo = new JLabel("Administra los ingredientes e inventario");
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

        JLabel tituloDatos = new JLabel("Datos del ingrediente");
        tituloDatos.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tituloDatos.setForeground(CAFE);
        tituloDatos.setAlignmentX(Component.LEFT_ALIGNMENT);
        datos.add(tituloDatos);
        datos.add(Box.createVerticalStrut(15));

        txtNombre = crearCampo();
        txtUnidad = crearCampo();
        txtStockActual = crearCampo();
        txtStockMinimo = crearCampo();

        cbProveedor = new JComboBox<>();
        cbProveedor.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        datos.add(crearFila("Nombre:", txtNombre));
        datos.add(crearFila("Unidad de medida:", txtUnidad));
        datos.add(crearFila("Stock actual:", txtStockActual));
        datos.add(crearFila("Stock minimo:", txtStockMinimo));
        datos.add(crearFila("Proveedor:", cbProveedor));

        datos.add(Box.createVerticalStrut(15));

        JButton btnAgregar = crearBoton("AGREGAR", ROJO);
        JButton btnEditar = crearBoton("EDITAR", NARANJA);
        JButton btnEliminar = crearBoton("ELIMINAR", CAFE);
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
        JLabel buscarLabel = new JLabel("Buscar ingrediente:");
        buscarLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        buscarLabel.setForeground(CAFE);
        txtBuscar = new JTextField();
        JButton btnBuscar = crearBoton("BUSCAR", DORADO);
        buscarPanel.add(buscarLabel, BorderLayout.WEST);
        buscarPanel.add(txtBuscar, BorderLayout.CENTER);
        buscarPanel.add(btnBuscar, BorderLayout.EAST);
        panelTabla.add(buscarPanel, BorderLayout.NORTH);

        String[] columnas = {"ID", "Nombre", "Unidad", "Stock actual", "Stock minimo", "Proveedor"};
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

        cargarProveedores();
        cargarIngredientes();

        btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (txtNombre.getText().isEmpty() || txtUnidad.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(GestionStock.this, "Completa nombre y unidad.");
                    return;
                }
                try {
                    double stockActual = Double.parseDouble(txtStockActual.getText());
                    double stockMinimo = Double.parseDouble(txtStockMinimo.getText());
                    int idProveedor = proveedoresMap.get((String) cbProveedor.getSelectedItem());

                    ingredienteDAO.insertarIngrediente(txtNombre.getText(), txtUnidad.getText(),
                            stockActual, stockMinimo, idProveedor);
                    JOptionPane.showMessageDialog(GestionStock.this, "Ingrediente agregado correctamente.");
                    limpiar();
                    cargarIngredientes();

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(GestionStock.this, "Stock actual y minimo deben ser numeros.");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(GestionStock.this, "Error al agregar: " + ex.getMessage());
                }
            }
        });

        btnEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (idSeleccionado == -1) {
                    JOptionPane.showMessageDialog(GestionStock.this, "Selecciona un ingrediente de la tabla.");
                    return;
                }
                try {
                    double stockActual = Double.parseDouble(txtStockActual.getText());
                    double stockMinimo = Double.parseDouble(txtStockMinimo.getText());
                    int idProveedor = proveedoresMap.get((String) cbProveedor.getSelectedItem());

                    ingredienteDAO.actualizarIngrediente(idSeleccionado, txtNombre.getText(), txtUnidad.getText(),
                            stockActual, stockMinimo, idProveedor);
                    JOptionPane.showMessageDialog(GestionStock.this, "Ingrediente actualizado correctamente.");
                    limpiar();
                    cargarIngredientes();

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(GestionStock.this, "Stock actual y minimo deben ser numeros.");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(GestionStock.this, "Error al actualizar: " + ex.getMessage());
                }
            }
        });

        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (idSeleccionado == -1) {
                    JOptionPane.showMessageDialog(GestionStock.this, "Selecciona un ingrediente.");
                    return;
                }
                int respuesta = JOptionPane.showConfirmDialog(GestionStock.this,
                        "Deseas eliminar este ingrediente?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (respuesta == JOptionPane.YES_OPTION) {
                    try {
                        ingredienteDAO.eliminarIngrediente(idSeleccionado);
                        limpiar();
                        cargarIngredientes();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(GestionStock.this,
                                "No se pudo eliminar (puede estar usado en algun producto): " + ex.getMessage());
                    }
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
                txtUnidad.setText(modelo.getValueAt(fila, 2).toString());
                txtStockActual.setText(modelo.getValueAt(fila, 3).toString());
                txtStockMinimo.setText(modelo.getValueAt(fila, 4).toString());
                cbProveedor.setSelectedItem(modelo.getValueAt(fila, 5).toString());
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
                JOptionPane.showMessageDialog(GestionStock.this, "Ingrediente no encontrado.");
            }
        });

        setVisible(true);
    }

    private void cargarProveedores() {
        try {
            for (Object[] p : ingredienteDAO.listarProveedores()) {
                proveedoresMap.put((String) p[1], (Integer) p[0]);
                cbProveedor.addItem((String) p[1]);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar proveedores: " + e.getMessage());
        }
    }

    private void cargarIngredientes() {
        modelo.setRowCount(0);
        try {
            for (Object[] fila : ingredienteDAO.listarTodos()) {
                modelo.addRow(fila);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar ingredientes: " + e.getMessage());
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
        txtUnidad.setText("");
        txtStockActual.setText("");
        txtStockMinimo.setText("");
        if (cbProveedor.getItemCount() > 0) cbProveedor.setSelectedIndex(0);
        tabla.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GestionStock();
            }
        });
    }
}