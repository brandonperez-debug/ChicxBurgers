package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;
import ChicxBurgerDB.UsuarioDAO;

public class GestionUsuarios extends JFrame {

    Color ROJO = Color.decode("#f53418");
    Color NARANJA = Color.decode("#f6781c");
    Color MOSTAZA = Color.decode("#a28813");
    Color DORADO = Color.decode("#db8f1b");
    Color BEIGE = Color.decode("#e0cfc8");
    Color CAFE = Color.decode("#661d05");

    JTextField txtNombreCompleto;
    JTextField txtUsuarioLogin;
    JPasswordField txtContrasena;
    JTextField txtBuscar;
    JComboBox<String> cbRol;
    JComboBox<String> cbEstado;

    JTable tabla;
    DefaultTableModel modelo;

    UsuarioDAO usuarioDAO = new UsuarioDAO();
    int idSeleccionado = -1; // -1 = ningun usuario seleccionado (modo agregar)

    public GestionUsuarios() {

        setTitle("Chiksx Burger - Gestion de Usuarios");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel principal = new JPanel(new BorderLayout());
        principal.setBackground(BEIGE);

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(Color.WHITE);
        encabezado.setBorder(new EmptyBorder(15, 30, 15, 30));

        JLabel titulo = new JLabel("Gestion de Usuarios");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 25));
        titulo.setForeground(CAFE);

        JLabel subtitulo = new JLabel("Administra los usuarios de Chiksx Burger");
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

        JLabel tituloDatos = new JLabel("Datos del usuario");
        tituloDatos.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tituloDatos.setForeground(CAFE);
        tituloDatos.setAlignmentX(Component.LEFT_ALIGNMENT);

        datos.add(tituloDatos);
        datos.add(Box.createVerticalStrut(15));

        txtNombreCompleto = crearCampo();
        txtUsuarioLogin = crearCampo();
        txtContrasena = new JPasswordField();
        txtContrasena.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtContrasena.setPreferredSize(new Dimension(230, 35));

        cbRol = new JComboBox<>();
        cbRol.addItem("Administrador");
        cbRol.addItem("Cajero");
        cbRol.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        cbEstado = new JComboBox<>();
        cbEstado.addItem("Activo");
        cbEstado.addItem("Inactivo");
        cbEstado.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        datos.add(crearFila("Nombre completo:", txtNombreCompleto));
        datos.add(crearFila("Usuario (login):", txtUsuarioLogin));
        datos.add(crearFila("Contrasena:", txtContrasena));
        datos.add(crearFila("Rol:", cbRol));
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

        JLabel buscarLabel = new JLabel("Buscar usuario:");
        buscarLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        buscarLabel.setForeground(CAFE);

        txtBuscar = new JTextField();
        JButton btnBuscar = crearBoton("BUSCAR", DORADO);

        buscarPanel.add(buscarLabel, BorderLayout.WEST);
        buscarPanel.add(txtBuscar, BorderLayout.CENTER);
        buscarPanel.add(btnBuscar, BorderLayout.EAST);

        panelTabla.add(buscarPanel, BorderLayout.NORTH);

        String[] columnas = {"ID", "Nombre completo", "Usuario", "Rol", "Estado"};
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

        cargarUsuarios();

        btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (txtNombreCompleto.getText().isEmpty()
                        || txtUsuarioLogin.getText().isEmpty()
                        || txtContrasena.getPassword().length == 0) {
                    JOptionPane.showMessageDialog(GestionUsuarios.this,
                            "Completa nombre, usuario y contrasena.", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    boolean ok = usuarioDAO.crearUsuario(
                            txtNombreCompleto.getText(),
                            txtUsuarioLogin.getText(),
                            new String(txtContrasena.getPassword()),
                            (String) cbRol.getSelectedItem()
                    );
                    if (ok) {
                        JOptionPane.showMessageDialog(GestionUsuarios.this, "Usuario agregado correctamente.");
                        limpiar();
                        cargarUsuarios();
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(GestionUsuarios.this,
                            "Error al agregar usuario: " + ex.getMessage());
                }
            }
        });

        btnEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (idSeleccionado == -1) {
                    JOptionPane.showMessageDialog(GestionUsuarios.this, "Selecciona un usuario de la tabla.");
                    return;
                }

                try {
                    int estado = cbEstado.getSelectedItem().equals("Activo") ? 1 : 0;
                    boolean ok = usuarioDAO.actualizarUsuario(
                            idSeleccionado,
                            txtNombreCompleto.getText(),
                            txtUsuarioLogin.getText(),
                            new String(txtContrasena.getPassword()),
                            (String) cbRol.getSelectedItem(),
                            estado
                    );
                    if (ok) {
                        JOptionPane.showMessageDialog(GestionUsuarios.this, "Usuario actualizado correctamente.");
                        limpiar();
                        cargarUsuarios();
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(GestionUsuarios.this,
                            "Error al actualizar usuario: " + ex.getMessage());
                }
            }
        });

        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (idSeleccionado == -1) {
                    JOptionPane.showMessageDialog(GestionUsuarios.this, "Selecciona un usuario.");
                    return;
                }

                int respuesta = JOptionPane.showConfirmDialog(GestionUsuarios.this,
                        "Deseas eliminar este usuario?", "Confirmar", JOptionPane.YES_NO_OPTION);

                if (respuesta == JOptionPane.YES_OPTION) {
                    try {
                        usuarioDAO.eliminarUsuario(idSeleccionado);
                        limpiar();
                        cargarUsuarios();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(GestionUsuarios.this,
                                "No se pudo eliminar (puede tener ventas o turnos asociados): " + ex.getMessage());
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
                txtNombreCompleto.setText(modelo.getValueAt(fila, 1).toString());
                txtUsuarioLogin.setText(modelo.getValueAt(fila, 2).toString());
                txtContrasena.setText("");
                cbRol.setSelectedItem(modelo.getValueAt(fila, 3).toString());
                cbEstado.setSelectedItem(modelo.getValueAt(fila, 4).toString());
            }
        });

        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String buscar = txtBuscar.getText().toLowerCase();
                for (int i = 0; i < tabla.getRowCount(); i++) {
                    String nombre = tabla.getValueAt(i, 1).toString().toLowerCase();
                    String login = tabla.getValueAt(i, 2).toString().toLowerCase();
                    if (nombre.contains(buscar) || login.contains(buscar)) {
                        tabla.setRowSelectionInterval(i, i);
                        return;
                    }
                }
                JOptionPane.showMessageDialog(GestionUsuarios.this, "Usuario no encontrado.");
            }
        });

        setVisible(true);
    }

    private void cargarUsuarios() {
        modelo.setRowCount(0);
        try {
            List<Object[]> lista = usuarioDAO.listarUsuarios();
            for (Object[] fila : lista) {
                modelo.addRow(fila);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar usuarios: " + e.getMessage());
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
        txtNombreCompleto.setText("");
        txtUsuarioLogin.setText("");
        txtContrasena.setText("");
        cbRol.setSelectedIndex(0);
        cbEstado.setSelectedIndex(0);
        tabla.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GestionUsuarios();
            }
        });
    }
}