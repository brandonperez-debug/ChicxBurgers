package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class GestionCuentas extends JFrame {

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
    JTextField txtUsuario;
    JPasswordField txtContrasena;

    JComboBox<String> cbRol;
    JComboBox<String> cbEstado;

    JTextField txtBuscar;

    JTable tabla;
    DefaultTableModel modelo;

    // ==========================================
    // CONSTRUCTOR
    // ==========================================
    public GestionCuentas() {

        setTitle("Chiksx Burger - Gestion de Cuentas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // ==========================================
        // PANEL PRINCIPAL
        // ==========================================
        JPanel principal = new JPanel(
                new BorderLayout()
        );

        principal.setBackground(BEIGE);

        // ==========================================
        // ENCABEZADO
        // ==========================================
        JPanel encabezado = new JPanel(
                new BorderLayout()
        );

        encabezado.setBackground(Color.WHITE);

        encabezado.setBorder(
                new EmptyBorder(15, 30, 15, 30)
        );

        JLabel titulo = new JLabel(
                "Gestion de Cuentas"
        );

        titulo.setFont(
                new Font("Segoe UI", Font.BOLD, 25)
        );

        titulo.setForeground(CAFE);

        JLabel subtitulo = new JLabel(
                "Administra las cuentas de acceso al sistema"
        );

        subtitulo.setFont(
                new Font("Segoe UI", Font.PLAIN, 14)
        );

        subtitulo.setForeground(Color.GRAY);

        JPanel textos = new JPanel();

        textos.setBackground(Color.WHITE);

        textos.setLayout(
                new BoxLayout(
                        textos,
                        BoxLayout.Y_AXIS
                )
        );

        textos.add(titulo);
        textos.add(subtitulo);

        encabezado.add(
                textos,
                BorderLayout.WEST
        );

        JLabel logo = new JLabel(
                "CHIKSX BURGER"
        );

        logo.setFont(
                new Font("Segoe UI", Font.BOLD, 18)
        );

        logo.setForeground(ROJO);

        encabezado.add(
                logo,
                BorderLayout.EAST
        );

        principal.add(
                encabezado,
                BorderLayout.NORTH
        );

        // ==========================================
        // CONTENIDO
        // ==========================================
        JPanel contenido = new JPanel(
                new BorderLayout(20, 20)
        );

        contenido.setBackground(BEIGE);

        contenido.setBorder(
                new EmptyBorder(25, 30, 25, 30)
        );

        // ==========================================
        // PANEL DATOS
        // ==========================================
        JPanel datos = new JPanel();

        datos.setBackground(Color.WHITE);

        datos.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(CAFE, 1),
                        new EmptyBorder(
                                20, 20, 20, 20
                        )
                )
        );

        datos.setLayout(
                new BoxLayout(
                        datos,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel tituloDatos = new JLabel(
                "Datos de la cuenta"
        );

        tituloDatos.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        18
                )
        );

        tituloDatos.setForeground(CAFE);

        datos.add(tituloDatos);

        datos.add(
                Box.createVerticalStrut(15)
        );

        // ==========================================
        // CAMPOS
        // ==========================================
        txtId = crearCampo();

        txtUsuario = crearCampo();

        txtContrasena = new JPasswordField();

        txtContrasena.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        cbRol = new JComboBox<>();

        cbRol.addItem("Administrador");
        cbRol.addItem("Empleado");

        cbEstado = new JComboBox<>();

        cbEstado.addItem("Activo");
        cbEstado.addItem("Inactivo");

        datos.add(
                crearFila(
                        "ID:",
                        txtId
                )
        );

        datos.add(
                crearFila(
                        "Usuario:",
                        txtUsuario
                )
        );

        datos.add(
                crearFila(
                        "Contrasena:",
                        txtContrasena
                )
        );

        datos.add(
                crearFila(
                        "Rol:",
                        cbRol
                )
        );

        datos.add(
                crearFila(
                        "Estado:",
                        cbEstado
                )
        );

        datos.add(
                Box.createVerticalStrut(15)
        );

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

        datos.add(
                Box.createVerticalStrut(8)
        );

        datos.add(btnEditar);

        datos.add(
                Box.createVerticalStrut(8)
        );

        datos.add(btnEliminar);

        datos.add(
                Box.createVerticalStrut(8)
        );

        datos.add(btnLimpiar);

        // ==========================================
        // PANEL TABLA
        // ==========================================
        JPanel panelTabla = new JPanel(
                new BorderLayout(10, 10)
        );

        panelTabla.setBackground(Color.WHITE);

        panelTabla.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                CAFE,
                                1
                        ),
                        new EmptyBorder(
                                15, 15, 15, 15
                        )
                )
        );

        // ==========================================
        // BUSCAR
        // ==========================================
        JPanel buscarPanel = new JPanel(
                new BorderLayout(10, 0)
        );

        buscarPanel.setBackground(Color.WHITE);

        JLabel buscarLabel = new JLabel(
                "Buscar cuenta:"
        );

        buscarLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
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
            "Usuario",
            "Rol",
            "Estado"
        };

        Object[][] datosIniciales = {

            {
                "001",
                "admin",
                "Administrador",
                "Activo"
            },

            {
                "002",
                "carlos",
                "Empleado",
                "Activo"
            },

            {
                "003",
                "maria",
                "Empleado",
                "Activo"
            },

            {
                "004",
                "jose",
                "Empleado",
                "Inactivo"
            }
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
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        tabla.setRowHeight(35);

        tabla.getTableHeader().setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        tabla.getTableHeader().setBackground(CAFE);

        tabla.getTableHeader().setForeground(
                Color.WHITE
        );

        tabla.setSelectionBackground(BEIGE);

        tabla.setSelectionForeground(CAFE);

        JScrollPane scroll = new JScrollPane(
                tabla
        );

        scroll.setBorder(
                BorderFactory.createEmptyBorder()
        );

        panelTabla.add(
                scroll,
                BorderLayout.CENTER
        );

        // ==========================================
        // AGREGAR A CONTENIDO
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
        // BOTON AGREGAR
        // ==========================================
        btnAgregar.addActionListener(
                new ActionListener() {

            @Override
            public void actionPerformed(
                    ActionEvent e) {

                if (txtId.getText().equals("")
                        || txtUsuario.getText().equals("")
                        || txtContrasena.getPassword().length == 0) {

                    JOptionPane.showMessageDialog(
                            GestionCuentas.this,
                            "Completa los campos principales.",
                            "Aviso",
                            JOptionPane.WARNING_MESSAGE
                    );

                } else {

                    modelo.addRow(
                            new Object[]{

                                txtId.getText(),

                                txtUsuario.getText(),

                                cbRol.getSelectedItem(),

                                cbEstado.getSelectedItem()
                            }
                    );

                    JOptionPane.showMessageDialog(
                            GestionCuentas.this,
                            "Cuenta creada correctamente."
                    );

                    limpiar();
                }
            }
        });

        // ==========================================
        // BOTON EDITAR
        // ==========================================
        btnEditar.addActionListener(
                new ActionListener() {

            @Override
            public void actionPerformed(
                    ActionEvent e) {

                int fila =
                        tabla.getSelectedRow();

                if (fila == -1) {

                    JOptionPane.showMessageDialog(
                            GestionCuentas.this,
                            "Selecciona una cuenta."
                    );

                } else {

                    modelo.setValueAt(
                            txtId.getText(),
                            fila,
                            0
                    );

                    modelo.setValueAt(
                            txtUsuario.getText(),
                            fila,
                            1
                    );

                    modelo.setValueAt(
                            cbRol.getSelectedItem(),
                            fila,
                            2
                    );

                    modelo.setValueAt(
                            cbEstado.getSelectedItem(),
                            fila,
                            3
                    );

                    JOptionPane.showMessageDialog(
                            GestionCuentas.this,
                            "Cuenta actualizada correctamente."
                    );

                    limpiar();
                }
            }
        });

        // ==========================================
        // BOTON ELIMINAR
        // ==========================================
        btnEliminar.addActionListener(
                new ActionListener() {

            @Override
            public void actionPerformed(
                    ActionEvent e) {

                int fila =
                        tabla.getSelectedRow();

                if (fila == -1) {

                    JOptionPane.showMessageDialog(
                            GestionCuentas.this,
                            "Selecciona una cuenta."
                    );

                } else {

                    int respuesta =
                            JOptionPane.showConfirmDialog(
                                    GestionCuentas.this,
                                    "¿Deseas eliminar esta cuenta?",
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
        // BOTON LIMPIAR
        // ==========================================
        btnLimpiar.addActionListener(
                new ActionListener() {

            @Override
            public void actionPerformed(
                    ActionEvent e) {

                limpiar();
            }
        });

        // ==========================================
        // SELECCIONAR CUENTA
        // ==========================================
        tabla.addMouseListener(
                new MouseAdapter() {

            @Override
            public void mouseClicked(
                    MouseEvent e) {

                int fila =
                        tabla.getSelectedRow();

                txtId.setText(
                        modelo.getValueAt(
                                fila, 0
                        ).toString()
                );

                txtUsuario.setText(
                        modelo.getValueAt(
                                fila, 1
                        ).toString()
                );

                cbRol.setSelectedItem(
                        modelo.getValueAt(
                                fila, 2
                        ).toString()
                );

                cbEstado.setSelectedItem(
                        modelo.getValueAt(
                                fila, 3
                        ).toString()
                );
            }
        });

        // ==========================================
        // BUSCAR
        // ==========================================
        btnBuscar.addActionListener(
                new ActionListener() {

            @Override
            public void actionPerformed(
                    ActionEvent e) {

                String buscar =
                        txtBuscar.getText()
                                .toLowerCase();

                for (int i = 0;
                        i < tabla.getRowCount();
                        i++) {

                    String usuario =
                            tabla.getValueAt(
                                    i, 1
                            )
                            .toString()
                            .toLowerCase();

                    if (usuario.contains(buscar)) {

                        tabla.setRowSelectionInterval(
                                i,
                                i
                        );

                        return;
                    }
                }

                JOptionPane.showMessageDialog(
                        GestionCuentas.this,
                        "Cuenta no encontrada."
                );
            }
        });

        setVisible(true);
    }

    // ==========================================
    // CREAR CAMPO
    // ==========================================
    private JTextField crearCampo() {

        JTextField campo =
                new JTextField();

        campo.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
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

        JPanel panel =
                new JPanel(
                        new BorderLayout(10, 5)
                );

        panel.setBackground(Color.WHITE);

        panel.setMaximumSize(
                new Dimension(270, 55)
        );

        JLabel etiqueta =
                new JLabel(nombre);

        etiqueta.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
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

        JButton boton =
                new JButton(texto);

        boton.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
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
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        return boton;
    }

    // ==========================================
    // LIMPIAR
    // ==========================================
    private void limpiar() {

        txtId.setText("");

        txtUsuario.setText("");

        txtContrasena.setText("");

        cbRol.setSelectedIndex(0);

        cbEstado.setSelectedIndex(0);

        tabla.clearSelection();
    }

    // ==========================================
    // MAIN
    // ==========================================
    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                new Runnable() {

            @Override
            public void run() {

                new GestionCuentas();
            }
        });
    }
}