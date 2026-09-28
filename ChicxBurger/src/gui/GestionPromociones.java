package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.sql.SQLException;
import ChicxBurgerDB.PromocionDAO;

public class GestionPromociones extends JFrame {

    Color ROJO = Chiksxburgermenu.RED;
    Color NARANJA = Chiksxburgermenu.AMBAR;
    Color MOSTAZA = Chiksxburgermenu.GOLD_DEEP;
    Color DORADO = Chiksxburgermenu.AMBAR_CLARO;
    Color BEIGE = Chiksxburgermenu.CREAM;
    Color CAFE = Chiksxburgermenu.BROWN_950;

    JTextField txtNombre, txtDescripcion, txtValor, txtFechaInicio, txtFechaFin, txtBuscar;
    JComboBox<String> cbTipo, cbEstado;

    JTable tabla;
    DefaultTableModel modelo;

    PromocionDAO promocionDAO = new PromocionDAO();
    int idSeleccionado = -1;

    // barra lateral que sale del logo, empieza escondida
    private JPanel sidebar;
    private boolean sidebarVisible = false;

    public GestionPromociones() {

        setTitle("ChicxBurger - Gestion de Promociones");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        sidebar = EstiloAdmin.construirBarraLateral();

        JPanel principal = new JPanel(new BorderLayout());
        principal.setBackground(BEIGE);

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(Chiksxburgermenu.MAROON);
        encabezado.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 4, 0, Chiksxburgermenu.GOLD),
                new EmptyBorder(15, 30, 15, 30)
        ));

        JPanel logoFila = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        logoFila.setOpaque(false);
        JLabel mark = new JLabel(EstiloAdmin.crearIconoLogo(34, Chiksxburgermenu.GOLD, Chiksxburgermenu.BROWN_950));
        mark.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        mark.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { toggleSidebar(); }
        });
        logoFila.add(mark);
        JLabel logo = new JLabel("CHICXBURGER");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logo.setForeground(Chiksxburgermenu.CREAM);
        logoFila.add(logo);
        encabezado.add(logoFila, BorderLayout.WEST);

        JLabel titulo = new JLabel("Gestion de Promociones");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(Chiksxburgermenu.GOLD);
        encabezado.add(titulo, BorderLayout.EAST);

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

        JLabel tituloDatos = new JLabel("Datos de la promocion");
        tituloDatos.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tituloDatos.setForeground(CAFE);
        tituloDatos.setAlignmentX(Component.LEFT_ALIGNMENT);
        datos.add(tituloDatos);
        datos.add(Box.createVerticalStrut(15));

        txtNombre = crearCampo();
        txtDescripcion = crearCampo();
        txtValor = crearCampo();

        cbTipo = new JComboBox<>();
        cbTipo.addItem("Porcentaje");
        cbTipo.addItem("Monto Fijo");
        cbTipo.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        txtFechaInicio = crearCampo();
        txtFechaInicio.setToolTipText("Formato: YYYY-MM-DD");
        txtFechaFin = crearCampo();
        txtFechaFin.setToolTipText("Formato: YYYY-MM-DD");

        cbEstado = new JComboBox<>();
        cbEstado.addItem("Activo");
        cbEstado.addItem("Inactivo");
        cbEstado.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        datos.add(crearFila("Nombre:", txtNombre));
        datos.add(crearFila("Descripcion:", txtDescripcion));
        datos.add(crearFila("Tipo de descuento:", cbTipo));
        datos.add(crearFila("Valor:", txtValor));
        datos.add(crearFila("Fecha inicio (YYYY-MM-DD):", txtFechaInicio));
        datos.add(crearFila("Fecha fin (YYYY-MM-DD):", txtFechaFin));
        datos.add(crearFila("Estado:", cbEstado));

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
        JLabel buscarLabel = new JLabel("Buscar promocion:");
        buscarLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        buscarLabel.setForeground(CAFE);
        txtBuscar = new JTextField();
        JButton btnBuscar = crearBoton("BUSCAR", DORADO);
        buscarPanel.add(buscarLabel, BorderLayout.WEST);
        buscarPanel.add(txtBuscar, BorderLayout.CENTER);
        buscarPanel.add(btnBuscar, BorderLayout.EAST);
        panelTabla.add(buscarPanel, BorderLayout.NORTH);

        String[] columnas = {"ID", "Nombre", "Descripcion", "Tipo", "Valor", "Inicio", "Fin", "Estado"};
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

        cargarPromociones();

        btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double valor = Double.parseDouble(txtValor.getText());
                    Date inicio = Date.valueOf(txtFechaInicio.getText().trim());
                    Date fin = Date.valueOf(txtFechaFin.getText().trim());

                    promocionDAO.insertarPromocion(txtNombre.getText(), txtDescripcion.getText(),
                            (String) cbTipo.getSelectedItem(), valor, inicio, fin);
                    JOptionPane.showMessageDialog(GestionPromociones.this, "Promocion agregada correctamente.");
                    limpiar();
                    cargarPromociones();

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(GestionPromociones.this, "El valor debe ser un numero.");
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(GestionPromociones.this,
                            "Las fechas deben tener el formato YYYY-MM-DD.");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(GestionPromociones.this, "Error al agregar: " + ex.getMessage());
                }
            }
        });

        btnEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (idSeleccionado == -1) {
                    JOptionPane.showMessageDialog(GestionPromociones.this, "Selecciona una promocion de la tabla.");
                    return;
                }
                try {
                    double valor = Double.parseDouble(txtValor.getText());
                    Date inicio = Date.valueOf(txtFechaInicio.getText().trim());
                    Date fin = Date.valueOf(txtFechaFin.getText().trim());
                    int estado = cbEstado.getSelectedItem().equals("Activo") ? 1 : 0;

                    promocionDAO.actualizarPromocion(idSeleccionado, txtNombre.getText(), txtDescripcion.getText(),
                            (String) cbTipo.getSelectedItem(), valor, inicio, fin, estado);
                    JOptionPane.showMessageDialog(GestionPromociones.this, "Promocion actualizada correctamente.");
                    limpiar();
                    cargarPromociones();

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(GestionPromociones.this, "El valor debe ser un numero.");
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(GestionPromociones.this,
                            "Las fechas deben tener el formato YYYY-MM-DD.");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(GestionPromociones.this, "Error al actualizar: " + ex.getMessage());
                }
            }
        });

        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (idSeleccionado == -1) {
                    JOptionPane.showMessageDialog(GestionPromociones.this, "Selecciona una promocion.");
                    return;
                }
                int respuesta = JOptionPane.showConfirmDialog(GestionPromociones.this,
                        "Deseas eliminar esta promocion?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (respuesta == JOptionPane.YES_OPTION) {
                    try {
                        promocionDAO.eliminarPromocion(idSeleccionado);
                        limpiar();
                        cargarPromociones();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(GestionPromociones.this, "Error al eliminar: " + ex.getMessage());
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
                txtDescripcion.setText(modelo.getValueAt(fila, 2).toString());
                cbTipo.setSelectedItem(modelo.getValueAt(fila, 3).toString());
                txtValor.setText(modelo.getValueAt(fila, 4).toString());
                txtFechaInicio.setText(modelo.getValueAt(fila, 5).toString());
                txtFechaFin.setText(modelo.getValueAt(fila, 6).toString());
                cbEstado.setSelectedItem(modelo.getValueAt(fila, 7).toString());
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
                JOptionPane.showMessageDialog(GestionPromociones.this, "Promocion no encontrada.");
            }
        });

        setVisible(true);
    }

    private void cargarPromociones() {
        modelo.setRowCount(0);
        try {
            for (Object[] fila : promocionDAO.listarTodas()) {
                modelo.addRow(fila);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar promociones: " + e.getMessage());
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

    private JButton crearBoton(String texto, Color color) {
        EstiloAdmin.BotonSolido boton = new EstiloAdmin.BotonSolido(texto, color, Color.WHITE);
        boton.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        return boton;
    }

    private void limpiar() {
        idSeleccionado = -1;
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtValor.setText("");
        txtFechaInicio.setText("");
        txtFechaFin.setText("");
        cbTipo.setSelectedIndex(0);
        cbEstado.setSelectedIndex(0);
        tabla.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GestionPromociones();
            }
        });
    }
}