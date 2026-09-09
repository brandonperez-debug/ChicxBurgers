package gui;

import ChicxBurgerDB.UsuarioDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;

/**
 * Formulario para crear usuarios (empleados: Administrador / Cajero).
 * Al crear el usuario correctamente, cierra este formulario y abre
 * el menu principal de Chiksx Burger.
 */
public class CrearUsuarioForm extends JFrame {

    private final JTextField txtNombre = new JTextField();
    private final JTextField txtUsuario = new JTextField();
    private final JPasswordField txtContrasena = new JPasswordField();
    private final JPasswordField txtConfirmar = new JPasswordField();
    private final JComboBox<String> cmbRol = new JComboBox<>(new String[]{"Administrador", "Cajero"});

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public CrearUsuarioForm() {
        setTitle("Chiksx Burger - Crear usuario");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(460, 560);
        setMinimumSize(new Dimension(420, 520));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Chiksxburgermenu.CREAM);
        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(buildForm(), BorderLayout.CENTER);
        setContentPane(root);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(Chiksxburgermenu.MAROON);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 4, 0, Chiksxburgermenu.GOLD),
                new EmptyBorder(26, 28, 22, 28)));

        JLabel title = new JLabel("Crear usuario");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(Chiksxburgermenu.CREAM);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Registra un nuevo empleado (Administrador o Cajero)");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitle.setForeground(Chiksxburgermenu.GOLD);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitle.setBorder(new EmptyBorder(4, 0, 0, 0));

        header.add(title);
        header.add(subtitle);
        return header;
    }

    private JPanel buildForm() {
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setBackground(Chiksxburgermenu.CREAM);
        wrap.setBorder(new EmptyBorder(28, 28, 28, 28));

        Chiksxburgermenu.RoundedPanel card = new Chiksxburgermenu.RoundedPanel(
                18, Color.WHITE, Chiksxburgermenu.CREAM_2);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(26, 26, 26, 26));

        card.add(campo("Nombre completo", txtNombre));
        card.add(Box.createVerticalStrut(16));
        card.add(campo("Usuario (login)", txtUsuario));
        card.add(Box.createVerticalStrut(16));
        card.add(campo("Contrasena", txtContrasena));
        card.add(Box.createVerticalStrut(16));
        card.add(campo("Confirmar contrasena", txtConfirmar));
        card.add(Box.createVerticalStrut(16));
        card.add(campo("Rol", cmbRol));
        card.add(Box.createVerticalStrut(24));
        card.add(buildBotones());

        wrap.add(card, BorderLayout.CENTER);
        return wrap;
    }

    private JPanel campo(String etiqueta, JComponent input) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        lbl.setForeground(Chiksxburgermenu.INK_SOFT);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl.setBorder(new EmptyBorder(0, 0, 5, 0));

        input.setAlignmentX(Component.LEFT_ALIGNMENT);
        input.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        input.setFont(new Font("SansSerif", Font.PLAIN, 14));
        if (input instanceof JTextField || input instanceof JPasswordField) {
            ((JComponent) input).setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Chiksxburgermenu.CREAM_2, 1, true),
                    new EmptyBorder(6, 10, 6, 10)));
        }

        p.add(lbl);
        p.add(input);
        return p;
    }

    private JPanel buildBotones() {
        JPanel p = new JPanel(new GridLayout(1, 2, 12, 0));
        p.setOpaque(false);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        JButton btnCancelar = new JButton("Cancelar");
        estilizarBoton(btnCancelar, Chiksxburgermenu.CREAM_2, Chiksxburgermenu.BROWN_950);
        btnCancelar.addActionListener(e -> dispose());

        JButton btnCrear = new JButton("Crear usuario");
        estilizarBoton(btnCrear, Chiksxburgermenu.RED, Color.WHITE);
        btnCrear.addActionListener(e -> crearUsuario());

        p.add(btnCancelar);
        p.add(btnCrear);
        return p;
    }

    private void estilizarBoton(JButton btn, Color fondo, Color texto) {
        btn.setBackground(fondo);
        btn.setForeground(texto);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(10, 14, 10, 14));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    /** Valida los campos, inserta el usuario y si todo sale bien abre el menu. */
    private void crearUsuario() {
        String nombre = txtNombre.getText().trim();
        String usuario = txtUsuario.getText().trim();
        String contrasena = new String(txtContrasena.getPassword());
        String confirmar = new String(txtConfirmar.getPassword());
        String rol = (String) cmbRol.getSelectedItem();

        if (nombre.isEmpty() || usuario.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completa todos los campos.",
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (contrasena.length() < 6) {
            JOptionPane.showMessageDialog(this, "La contrasena debe tener al menos 6 caracteres.",
                    "Contrasena invalida", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!contrasena.equals(confirmar)) {
            JOptionPane.showMessageDialog(this, "Las contrasenas no coinciden.",
                    "Contrasena invalida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (usuarioDAO.existeUsuarioLogin(usuario)) {
                JOptionPane.showMessageDialog(this, "Ese usuario ya existe, elige otro.",
                        "Usuario duplicado", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean creado = usuarioDAO.crearUsuario(nombre, usuario, contrasena, rol);
            if (creado) {
                JOptionPane.showMessageDialog(this, "Usuario creado correctamente.",
                        "Listo", JOptionPane.INFORMATION_MESSAGE);
                abrirMenuYCerrar();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo crear el usuario.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error de base de datos:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Abre el menu principal de Chiksx Burger y cierra este formulario. */
    private void abrirMenuYCerrar() {
        SwingUtilities.invokeLater(() -> new Chiksxburgermenu().setVisible(true));
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) { }
            new CrearUsuarioForm().setVisible(true);
        });
    }
}