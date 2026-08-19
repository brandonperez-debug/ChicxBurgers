package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class GestionUsuarios extends JFrame {

    // ==========================================
    // COLORES CHIKSX BURGER
    // ==========================================
    Color ROJO = Color.decode("#f53418");
    Color NARANJA = Color.decode("#f6781c");
    Color MOSTAZA = Color.decode("#a28813");
    Color DORADO = Color.decode("#db8f1b");
    Color BEIGE = Color.decode("#e0cfc8");
    Color CAFE = Color.decode("#661d05");

    // ==========================================
    // COMPONENTES
    // ==========================================
    JTextField txtId;
    JTextField txtNombre;
    JTextField txtApellido;
    JTextField txtTelefono;
    JTextField txtCorreo;
    JTextField txtBuscar;

    JComboBox<String> cbEstado;

    JTable tabla;
    DefaultTableModel modelo;

    // ==========================================
    // CONSTRUCTOR
    // ==========================================
    public GestionUsuarios() {

        setTitle("Chiksx Burger - Gestion de Usuarios");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // ==========================================
        // PANEL PRINCIPAL
        // ==========================================
        JPanel principal = new JPanel(new BorderLayout());
        principal.setBackground(BEIGE);

        // ==========================================
        // ENCABEZADO
        // ==========================================
        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(Color.WHITE);
        encabezado.setBorder(
                new EmptyBorder(15, 30, 15, 30)
        );

        JLabel titulo = new JLabel("Gestion de Usuarios");
        titulo.setFont(
                new Font("Segoe UI", Font.BOLD, 25)
        );
        titulo.setForeground(CAFE);

        JLabel subtitulo = new JLabel(
                "Administra los usuarios de Chiksx Burger"
        );
        subtitulo.setFont(
                new Font("Segoe UI", Font.PLAIN, 14)
        );
        subtitulo.setForeground(Color.GRAY);

        JPanel textos = new JPanel();
        textos.setBackground(Color.WHITE);
        textos.setLayout(
                new BoxLayout(textos, BoxLayout.Y_AXIS)
        );

        textos.add(titulo);
        textos.add(subtitulo);

        encabezado.add(textos, BorderLayout.WEST);

        JLabel logo = new JLabel("CHIKSX BURGER");
        logo.setFont(
                new Font("Segoe UI", Font.BOLD, 18)
        );
        logo.setForeground(ROJO);

        encabezado.add(logo, BorderLayout.EAST);

        principal.add(encabezado, BorderLayout.NORTH);

        // ==========================================
        // CONTENIDO
        // ==========================================
        JPanel contenido = new JPanel(new BorderLayout(20, 20));
        contenido.setBackground(BEIGE);
        contenido.setBorder(
                new EmptyBorder(25, 30, 25, 30)
        );

        // ==========================================
        // PANEL DE DATOS
        // ==========================================
        JPanel datos = new JPanel();
        datos.setBackground(Color.WHITE);
        datos.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(CAFE, 1),
                        new EmptyBorder(20, 20, 20, 20)
                )
        );

        datos.setLayout(
                new BoxLayout(datos, BoxLayout.Y_AXIS)
        );

        JLabel tituloDatos = new JLabel("Datos del usuario");
        tituloDatos.setFont(
                new Font("Segoe UI", Font.BOLD, 18)
        );
        tituloDatos.setForeground(CAFE);
        tituloDatos.setAlignmentX(Component.LEFT_ALIGNMENT);

        datos.add(tituloDatos);
        datos.add(Box.createVerticalStrut(15));

        // ==========================================
        // CAMPOS
        // ==========================================
        txtId = crearCampo("ID");
        txtNombre = crearCampo("Nombre");
        txtApellido = crearCampo("Apellido");
        txtTelefono = crearCampo("Telefono");
        txtCorreo = crearCampo("Correo");

        cbEstado = new JComboBox<>();
        cbEstado.addItem("Activo");
        cbEstado.addItem("Inactivo");
        cbEstado.setFont(
                new Font("Segoe UI", Font.PLAIN, 14)
        );

        datos.add(crearFila("ID:", txtId));
        datos.add(crearFila("Nombre:", txtNombre));
        datos.add(crearFila("Apellido:", txtApellido));
        datos.add(crearFila("Telefono:", txtTelefono));
        datos.add(crearFila("Correo:", txtCorreo));
        datos.add(crearFila("Estado:", cbEstado));

        datos.add(Box.createVerticalStrut(15));

        // ==========================================
        // BOTONES
        // ==========================================
        JButton btnAgregar = crearBoton(
                "AGREGAR",
                ROJO
        );

        JButton btnEditar = crearBoton(
                "EDITAR",
                NARANJA
        );

        JButton btnEliminar = crearBoton(
                "ELIMINAR",
                CAFE
        );

        JButton btnLimpiar = crearBoton(
                "LIMPIAR",
                MOSTAZA
        );

        datos.add(btnAgregar);
        datos.add(Box.createVerticalStrut(8));
        datos.add(btnEditar);
        datos.add(Box.createVerticalStrut(8));
        datos.add(btnEliminar);
        datos.add(Box.createVerticalStrut(8));
        datos.add(btnLimpiar);

        // ==========================================
        // PANEL TABLA
        // ==========================================
        JPanel panelTabla = new JPanel(new BorderLayout(10, 10));
        panelTabla.setBackground(Color.WHITE);
        panelTabla.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(CAFE, 1),
                        new EmptyBorder(15, 15, 15, 15)
                )
        );

        // ==========================================
        // BUSCADOR
        // ==========================================
        JPanel buscarPanel = new JPanel(new BorderLayout(10, 0));
        buscarPanel.setBackground(Color.WHITE);

        JLabel buscarLabel = new JLabel("Buscar usuario:");
        buscarLabel.setFont(
                new Font("Segoe UI", Font.BOLD, 14)
        );
        buscarLabel.setForeground(CAFE);

        txtBuscar = new JTextField();

        JButton btnBuscar = crearBoton(
                "BUSCAR",
                DORADO
        );

        buscarPanel.add(
                buscarLabel,
                BorderLayout.WEST
        );

        buscarPanel.add(
                txtBuscar,
                BorderLayout.CENTER
        );

        buscarPanel.add(
                btnBuscar,
                BorderLayout.EAST
        );

        panelTabla.add(
                buscarPanel,
                BorderLayout.NORTH
        );

        // ==========================================
        // TABLA
        // ==========================================
        String[] columnas = {
            "ID",
            "Nombre",
            "Apellido",
            "Telefono",
            "Correo",
            "Estado"
        };

        Object[][] datosIniciales = {
            {"001", "Carlos", "Lopez", "5555-1234",
                "carlos@gmail.com", "Activo"},

            {"002", "Maria", "Garcia", "5555-2345",
                "maria@gmail.com", "Activo"},

            {"003", "Jose", "Hernandez", "5555-3456",
                "jose@gmail.com", "Inactivo"},

            {"004", "Ana", "Martinez", "5555-4567",
                "ana@gmail.com", "Activo"}
        };

        modelo = new DefaultTableModel(
                datosIniciales,
                columnas
        ) {

            @Override
            public boolean isCellEditable(
                    int fila,
                    int columna) {

                return false;
            }
        };

        tabla = new JTable(modelo);

        tabla.setFont(
                new Font("Segoe UI", Font.PLAIN, 13)
        );

        tabla.setRowHeight(35);

        tabla.getTableHeader().setFont(
                new Font("Segoe UI", Font.BOLD, 13)
        );

        tabla.getTableHeader().setBackground(CAFE);
        tabla.getTableHeader().setForeground(Color.WHITE);

        tabla.setSelectionBackground(BEIGE);
        tabla.setSelectionForeground(CAFE);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(
                BorderFactory.createEmptyBorder()
        );

        panelTabla.add(
                scroll,
                BorderLayout.CENTER
        );

        // ==========================================
        // AGREGAR PANELES
        // ==========================================
        contenido.add(
                datos,
                BorderLayout.WEST
        );

        contenido.add(
                panelTabla,
                BorderLayout.CENTER
        );

        principal.add(
                contenido,
                BorderLayout.CENTER
        );

        add(principal);

        // ==========================================
        // EVENTO AGREGAR
        // ==========================================
        btnAgregar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                if (txtId.getText().equals("")
                        || txtNombre.getText().equals("")
                        || txtApellido.getText().equals("")) {

                    JOptionPane.showMessageDialog(
                            GestionUsuarios.this,
                            "Completa los campos principales.",
                            "Aviso",
                            JOptionPane.WARNING_MESSAGE
                    );

                } else {

                    modelo.addRow(new Object[]{
                        txtId.getText(),
                        txtNombre.getText(),
                        txtApellido.getText(),
                        txtTelefono.getText(),
                        txtCorreo.getText(),
                        cbEstado.getSelectedItem()
                    });

                    JOptionPane.showMessageDialog(
                            GestionUsuarios.this,
                            "Usuario agregado correctamente."
                    );

                    limpiar();
                }
            }
        });

        // ==========================================
        // EVENTO EDITAR
        // ==========================================
        btnEditar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                int fila = tabla.getSelectedRow();

                if (fila == -1) {

                    JOptionPane.showMessageDialog(
                            GestionUsuarios.this,
                            "Selecciona un usuario de la tabla."
                    );

                } else {

                    modelo.setValueAt(
                            txtId.getText(),
                            fila,
                            0
                    );

                    modelo.setValueAt(
                            txtNombre.getText(),
                            fila,
                            1
                    );

                    modelo.setValueAt(
                            txtApellido.getText(),
                            fila,
                            2
                    );

                    modelo.setValueAt(
                            txtTelefono.getText(),
                            fila,
                            3
                    );

                    modelo.setValueAt(
                            txtCorreo.getText(),
                            fila,
                            4
                    );

                    modelo.setValueAt(
                            cbEstado.getSelectedItem(),
                            fila,
                            5
                    );

                    JOptionPane.showMessageDialog(
                            GestionUsuarios.this,
                            "Usuario actualizado correctamente."
                    );

                    limpiar();
                }
            }
        });

        // ==========================================
        // EVENTO ELIMINAR
        // ==========================================
        btnEliminar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                int fila = tabla.getSelectedRow();

                if (fila == -1) {

                    JOptionPane.showMessageDialog(
                            GestionUsuarios.this,
                            "Selecciona un usuario."
                    );

                } else {

                    int respuesta =
                            JOptionPane.showConfirmDialog(
                                    GestionUsuarios.this,
                                    "¿Deseas eliminar este usuario?",
                                    "Confirmar",
                                    JOptionPane.YES_NO_OPTION
                            );

                    if (respuesta ==
                            JOptionPane.YES_OPTION) {

                        modelo.removeRow(fila);

                        limpiar();
                    }
                }
            }
        });

        // ==========================================
        // EVENTO LIMPIAR
        // ==========================================
        btnLimpiar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                limpiar();
            }
        });

        // ==========================================
        // EVENTO SELECCIONAR TABLA
        // ==========================================
        tabla.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                int fila = tabla.getSelectedRow();

                txtId.setText(
                        modelo.getValueAt(fila, 0).toString()
                );

                txtNombre.setText(
                        modelo.getValueAt(fila, 1).toString()
                );

                txtApellido.setText(
                        modelo.getValueAt(fila, 2).toString()
                );

                txtTelefono.setText(
                        modelo.getValueAt(fila, 3).toString()
                );

                txtCorreo.setText(
                        modelo.getValueAt(fila, 4).toString()
                );

                cbEstado.setSelectedItem(
                        modelo.getValueAt(fila, 5).toString()
                );
            }
        });

        // ==========================================
        // EVENTO BUSCAR
        // ==========================================
        btnBuscar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                String buscar =
                        txtBuscar.getText().toLowerCase();

                for (int i = 0; i < tabla.getRowCount(); i++) {

                    String nombre =
                            tabla.getValueAt(i, 1)
                                    .toString()
                                    .toLowerCase();

                    String apellido =
                            tabla.getValueAt(i, 2)
                                    .toString()
                                    .toLowerCase();

                    if (nombre.contains(buscar)
                            || apellido.contains(buscar)) {

                        tabla.setRowSelectionInterval(i, i);

                        return;
                    }
                }

                JOptionPane.showMessageDialog(
                        GestionUsuarios.this,
                        "Usuario no encontrado."
                );
            }
        });

        setVisible(true);
    }

    // ==========================================
    // CREAR CAMPO
    // ==========================================
    private JTextField crearCampo(String texto) {

        JTextField campo = new JTextField();

        campo.setFont(
                new Font("Segoe UI", Font.PLAIN, 14)
        );

        campo.setPreferredSize(
                new Dimension(230, 35)
        );

        return campo;
    }

    // ==========================================
    // CREAR FILA
    // ==========================================
    private JPanel crearFila(
            String nombre,
            JComponent campo) {

        JPanel panel = new JPanel(
                new BorderLayout(10, 5)
        );

        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(
                new Dimension(270, 45)
        );

        JLabel etiqueta = new JLabel(nombre);

        etiqueta.setFont(
                new Font("Segoe UI", Font.BOLD, 13)
        );

        etiqueta.setForeground(CAFE);

        panel.add(
                etiqueta,
                BorderLayout.NORTH
        );

        panel.add(
                campo,
                BorderLayout.CENTER
        );

        return panel;
    }

    // ==========================================
    // CREAR BOTON
    // ==========================================
    private JButton crearBoton(
            String texto,
            Color color) {

        JButton boton = new JButton(texto);

        boton.setFont(
                new Font("Segoe UI", Font.BOLD, 12)
        );

        boton.setForeground(Color.WHITE);
        boton.setBackground(color);

        boton.setFocusPainted(false);
        boton.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 15, 10, 15
                )
        );

        boton.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        return boton;
    }

    // ==========================================
    // LIMPIAR
    // ==========================================
    private void limpiar() {

        txtId.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");

        cbEstado.setSelectedIndex(0);

        tabla.clearSelection();
    }

    // ==========================================
    // MAIN
    // ==========================================
    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new GestionUsuarios();
            }
        });
    }
}
