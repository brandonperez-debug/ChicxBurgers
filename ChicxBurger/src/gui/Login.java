package gui;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import main.Conexion.conexion;

public class Login extends JFrame {

    // ==========================================
    // COLORES
    // ==========================================

    private final Color ROJO = Color.decode("#f53418");
    private final Color NARANJA = Color.decode("#f6781c");
    private final Color MOSTAZA = Color.decode("#a28813");
    private final Color DORADO = Color.decode("#db8f1b");
    private final Color BEIGE = Color.decode("#e0cfc8");
    private final Color CAFE = Color.decode("#661d05");

    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JCheckBox chkMostrar;

    // Controla si el formulario esta en modo Login o modo Registro
    private boolean modoRegistro = false;

    public Login() {

        setTitle("Chiksx Burger - Login");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Pantalla completa
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        crearInterfaz();
    }

    // ==========================================
    // FUENTE GRAFFITI
    // ==========================================

    private Font fuenteGraffiti(int tamanio) {

        String[] fuentes = {
            "Ravie",
            "Bauhaus 93",
            "Showcard Gothic",
            "Arial Black"
        };

        String[] disponibles =
                GraphicsEnvironment
                        .getLocalGraphicsEnvironment()
                        .getAvailableFontFamilyNames();

        for (String fuente : fuentes) {

            for (String disponible : disponibles) {

                if (disponible.equalsIgnoreCase(fuente)) {

                    return new Font(
                            disponible,
                            Font.BOLD,
                            tamanio
                    );
                }
            }
        }

        return new Font(
                "Arial Black",
                Font.BOLD,
                tamanio
        );
    }

    // ==========================================
    // INTERFAZ
    // ==========================================

    private void crearInterfaz() {

        JPanel principal =
                new JPanel(new BorderLayout());

        principal.setBackground(CAFE);

        // ==========================================
        // FRANJA ROJA
        // ==========================================

        JPanel franjaRoja =
                new JPanel(new BorderLayout());

        franjaRoja.setBackground(ROJO);

        // Franja delgada
        franjaRoja.setPreferredSize(
                new Dimension(32, 0)
        );

        JLabel iconos =
                new JLabel(
                        "<html><center>"
                        + "★<br><br>"
                        + "☰<br><br>"
                        + "♨"
                        + "</center></html>"
                );

        iconos.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        19
                )
        );

        iconos.setForeground(Color.WHITE);

        iconos.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        franjaRoja.add(
                iconos,
                BorderLayout.CENTER
        );

        // ==========================================
        // PANEL CAFE
        // ==========================================

        JPanel panelCafe =
                new JPanel(null) {

            @Override
            protected void paintComponent(Graphics g) {

                super.paintComponent(g);

                Graphics2D g2 =
                        (Graphics2D) g;

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                g2.setColor(CAFE);

                g2.fillRect(
                        0,
                        0,
                        getWidth(),
                        getHeight()
                );

                // Manchas decorativas
                g2.setColor(
                        new Color(90, 35, 10)
                );

                for (int i = 0; i < 12; i++) {

                    int x =
                            (i * 145)
                            % Math.max(
                                    1,
                                    getWidth()
                            );

                    int y =
                            (i * 100)
                            % Math.max(
                                    1,
                                    getHeight()
                            );

                    g2.fillOval(
                            x,
                            y,
                            110,
                            40
                    );
                }

                // Hamburguesa
                dibujarHamburguesa(
                        g2,
                        getWidth() / 2 - 140,
                        getHeight() - 220
                );

                // Bebida
                dibujarBebida(
                        g2,
                        getWidth() / 2 + 100,
                        getHeight() - 220
                );
            }
        };

        // ==========================================
        // TITULO CHIKSX
        // ==========================================

        JLabel lblChiksx =
                new JLabel("CHICX");

        lblChiksx.setFont(
                fuenteGraffiti(58)
        );

        lblChiksx.setForeground(
                Color.WHITE
        );

        lblChiksx.setBounds(
                55,
                55,
                400,
                80
        );

        panelCafe.add(lblChiksx);

        // ==========================================
        // BURGER
        // ==========================================

        JLabel lblBurger =
                new JLabel("BURGER");

        lblBurger.setFont(
                fuenteGraffiti(52)
        );

        lblBurger.setForeground(
                NARANJA
        );

        lblBurger.setBounds(
                80,
                125,
                400,
                75
        );

        panelCafe.add(lblBurger);

        // ==========================================
        // FRASE
        // ==========================================

        JLabel lblFrase =
                new JLabel(
                        "<html>"
                        + "<center>"
                        + "SABOR QUE TE HACE<br>"
                        + "<font color='#db8f1b'>"
                        + "VOLVER"
                        + "</font>"
                        + "</center>"
                        + "</html>"
                );

        lblFrase.setFont(
                fuenteGraffiti(20)
        );

        lblFrase.setForeground(
                Color.WHITE
        );

        lblFrase.setBounds(
                390,
                105,
                250,
                100
        );

        panelCafe.add(lblFrase);

        // ==========================================
        // CATEGORIAS
        // ==========================================

        JLabel lblCategorias =
                new JLabel(
                        "HAMBURGUESAS  •  BEBIDAS"
                );

        lblCategorias.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        16
                )
        );

        lblCategorias.setForeground(
                DORADO
        );

        lblCategorias.setBounds(
                60,
                255,
                350,
                30
        );

        panelCafe.add(
                lblCategorias
        );

        // ==========================================
        // PANEL DERECHO BEIGE
        // ==========================================

        JPanel panelDerecho =
                new JPanel();

        panelDerecho.setBackground(
                BEIGE
        );

        /*
         * Aumentamos el ancho beige.
         * Esto hace que el panel cafe quede
         * un poco mas delgado.
         */
        panelDerecho.setPreferredSize(
                new Dimension(
                        630,
                        0
                )
        );

        panelDerecho.setBorder(
                new EmptyBorder(
                        50,
                        70,
                        40,
                        70
                )
        );

        panelDerecho.setLayout(
                new BoxLayout(
                        panelDerecho,
                        BoxLayout.Y_AXIS
                )
        );

        // ==========================================
        // BIENVENIDO
        // ==========================================

        JLabel lblBienvenido =
                new JLabel(
                        "¡BIENVENIDO!"
                );

        lblBienvenido.setFont(
                fuenteGraffiti(38)
        );

        lblBienvenido.setForeground(
                CAFE
        );

        lblBienvenido.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        panelDerecho.add(
                lblBienvenido
        );

        // ==========================================
        // SUBTITULO
        // ==========================================

        JLabel lblSubtitulo =
                new JLabel(
                        "Inicia sesion para continuar"
                );

        lblSubtitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        16
                )
        );

        lblSubtitulo.setForeground(
                MOSTAZA
        );

        lblSubtitulo.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        panelDerecho.add(
                Box.createVerticalStrut(7)
        );

        panelDerecho.add(
                lblSubtitulo
        );

        panelDerecho.add(
                Box.createVerticalStrut(45)
        );

        // ==========================================
        // ANCHO DEL FORMULARIO
        // ==========================================

        int anchoFormulario = 390;

        // ==========================================
        // USUARIO
        // ==========================================

        JLabel lblUsuario =
                new JLabel("USUARIO");

        lblUsuario.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        16
                )
        );

        lblUsuario.setForeground(
                CAFE
        );

        /*
         * El grupo completo esta centrado,
         * pero el texto queda al ras izquierdo
         * de la caja.
         */
        lblUsuario.setHorizontalAlignment(
                SwingConstants.LEFT
        );
 lblUsuario.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        lblUsuario.setPreferredSize(
                new Dimension(
                        anchoFormulario,
                        25
                )
        );

        lblUsuario.setMaximumSize(
                new Dimension(
                        anchoFormulario,
                        25
                )
        );

        panelDerecho.add(
                lblUsuario
        );

        panelDerecho.add(
                Box.createVerticalStrut(6)
        );

        // ==========================================
        // CAMPO USUARIO
        // ==========================================

        txtUsuario =
                new JTextField();

        txtUsuario.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        16
                )
        );

        txtUsuario.setBackground(
                Color.WHITE
        );

        txtUsuario.setForeground(
                CAFE
        );

        txtUsuario.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                NARANJA,
                                2
                        ),
                        new EmptyBorder(
                                8,
                                14,
                                8,
                                14
                        )
                )
        );

        txtUsuario.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        txtUsuario.setPreferredSize(
                new Dimension(
                        anchoFormulario,
                        50
                )
        );

        txtUsuario.setMaximumSize(
                new Dimension(
                        anchoFormulario,
                        50
                )
        );

        panelDerecho.add(
                txtUsuario
        );

        // ==========================================
        // CONTRASEÑA
        // ==========================================

        panelDerecho.add(
                Box.createVerticalStrut(22)
        );

        JLabel lblPassword =
                new JLabel(
                        "CONTRASEÑA"
                );

        lblPassword.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        16
                )
        );

        lblPassword.setForeground(
                CAFE
        );

        // Al ras izquierdo de la caja
        lblPassword.setHorizontalAlignment(
                SwingConstants.LEFT
        );

        lblPassword.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        lblPassword.setPreferredSize(
                new Dimension(
                        anchoFormulario,
                        25
                )
        );

        lblPassword.setMaximumSize(
                new Dimension(
                        anchoFormulario,
                        25
                )
        );

        panelDerecho.add(
                lblPassword
        );

        panelDerecho.add(
                Box.createVerticalStrut(6)
        );

        // ==========================================
        // CAMPO CONTRASEÑA
        // ==========================================

        txtPassword =
                new JPasswordField();

        txtPassword.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        16
                )
        );

        txtPassword.setBackground(
                Color.WHITE
        );

        txtPassword.setForeground(
                CAFE
        );

        txtPassword.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                NARANJA,
                                2
                        ),
                        new EmptyBorder(
                                8,
                                14,
                                8,
                                14
                        )
                )
        );

        txtPassword.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        txtPassword.setPreferredSize(
                new Dimension(
                        anchoFormulario,
                        50
                )
        );

        txtPassword.setMaximumSize(
                new Dimension(
                        anchoFormulario,
                        50
                )
        );

        panelDerecho.add(
                txtPassword
        );

        // ==========================================
        // MOSTRAR CONTRASEÑA
        // ==========================================

        chkMostrar =
                new JCheckBox(
                        "Mostrar contraseña"
                );

        chkMostrar.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        chkMostrar.setForeground(
                CAFE
        );

        chkMostrar.setBackground(
                BEIGE
        );

        chkMostrar.setFocusPainted(
                false
        );

        /*
         * El checkbox queda alineado
         * con el inicio de las cajas.
         */
        chkMostrar.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        chkMostrar.setPreferredSize(
                new Dimension(
                        anchoFormulario,
                        25
                )
        );

        chkMostrar.setMaximumSize(
                new Dimension(
                        anchoFormulario,
                        25
                )
        );

        chkMostrar.addActionListener(
                e -> {

                    if (chkMostrar.isSelected()) {

                        txtPassword.setEchoChar(
                                (char) 0
                        );

                    } else {

                        txtPassword.setEchoChar(
                                '•'
                        );
                    }
                }
        );

        panelDerecho.add(
                Box.createVerticalStrut(6)
        );

        panelDerecho.add(
                chkMostrar
        );

        // ==========================================
        // BOTON
        // ==========================================

        JButton btnIngresar =
                new JButton(
                        "INGRESAR"
                );

        btnIngresar.setFont(
                fuenteGraffiti(21)
        );

        btnIngresar.setForeground(
                Color.WHITE
        );

        btnIngresar.setBackground(
                ROJO
        );

        btnIngresar.setFocusPainted(
                false
        );

        btnIngresar.setBorderPainted(
                false
        );

        btnIngresar.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        btnIngresar.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        btnIngresar.setPreferredSize(
                new Dimension(
                        anchoFormulario,
                        58
                )
        );

        btnIngresar.setMaximumSize(
                new Dimension(
                        anchoFormulario,
                        58
                )
        );

        btnIngresar.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e) {

                        btnIngresar.setBackground(
                                NARANJA
                        );
                    }

                    @Override
                    public void mouseExited(
                            MouseEvent e) {

                        btnIngresar.setBackground(
                                ROJO
                        );
                    }
                }
        );

        btnIngresar.addActionListener(
                e -> {
                    if (modoRegistro) {
                        crearCuenta();
                    } else {
                        iniciarSesion();
                    }
                }
        );

        panelDerecho.add(
                Box.createVerticalStrut(22)
        );

        panelDerecho.add(
                btnIngresar
        );

        // ==========================================
        // REGISTRO
        // ==========================================

        panelDerecho.add(
                Box.createVerticalStrut(27)
        );

        JLabel lblCuenta =
                new JLabel(
                        "¿No tienes una cuenta?"
                );

        lblCuenta.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        lblCuenta.setForeground(
                CAFE
        );

        lblCuenta.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        panelDerecho.add(
                lblCuenta
        );

        JLabel lblRegistro =
                new JLabel(
                        "Registrate aqui"
                );

        lblRegistro.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        19
                )
        );

        lblRegistro.setForeground(
                DORADO
        );

        lblRegistro.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        panelDerecho.add(
                Box.createVerticalStrut(4)
        );

        panelDerecho.add(
                lblRegistro
        );

        // ==========================================
        // CLIC EN "REGISTRATE AQUI" / "INICIA SESION AQUI"
        // ==========================================

        lblRegistro.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        lblRegistro.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseClicked(MouseEvent e) {

                        modoRegistro = !modoRegistro;

                        if (modoRegistro) {

                            lblBienvenido.setText("¡CREA TU CUENTA!");
                            lblSubtitulo.setText("Registrate para continuar");
                            lblUsuario.setText("CORREO");
                            lblPassword.setText("CONTRASEÑA");
                            btnIngresar.setText("CREAR CUENTA");
                            lblCuenta.setText("¿Ya tienes una cuenta?");
                            lblRegistro.setText("Inicia sesion aqui");

                        } else {

                            lblBienvenido.setText("¡BIENVENIDO!");
                            lblSubtitulo.setText("Inicia sesion para continuar");
                            lblUsuario.setText("USUARIO");
                            lblPassword.setText("CONTRASEÑA");
                            btnIngresar.setText("INGRESAR");
                            lblCuenta.setText("¿No tienes una cuenta?");
                            lblRegistro.setText("Registrate aqui");
                        }

                        // Limpiamos los campos al cambiar de modo
                        txtUsuario.setText("");
                        txtPassword.setText("");
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        lblRegistro.setForeground(NARANJA);
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        lblRegistro.setForeground(DORADO);
                    }
                }
        );

        // ==========================================
        // AGREGAR PANELES
        // ==========================================

        principal.add(
                franjaRoja,
                BorderLayout.WEST
        );

        principal.add(
                panelCafe,
                BorderLayout.CENTER
        );

        principal.add(
                panelDerecho,
                BorderLayout.EAST
        );

        add(principal);
 }

    // ==========================================
    // HAMBURGUESA
    // ==========================================

    private void dibujarHamburguesa(
            Graphics2D g2,
            int x,
            int y) {

        // Pan
        g2.setColor(DORADO);

        g2.fillRoundRect(
                x,
                y,
                250,
                65,
                25,
                25
        );

        // Ajonjoli
        g2.setColor(BEIGE);

        for (int i = 0; i < 15; i++) {

            int px =
                    x + 20
                    + (i * 35) % 210;

            int py =
                    y + 15
                    + (i * 11) % 25;

            g2.fillOval(
                    px,
                    py,
                    5,
                    3
            );
        }

        // Lechuga
        g2.setColor(
                new Color(70, 120, 25)
        );

        g2.fillRect(
                x + 5,
                y + 55,
                240,
                18
        );

        // Tomate
        g2.setColor(ROJO);

        g2.fillRect(
                x + 8,
                y + 72,
                235,
                15
        );

        // Queso
        g2.setColor(DORADO);

        g2.fillRect(
                x + 5,
                y + 87,
                240,
                18
        );

        // Carne
        g2.setColor(
                new Color(60, 25, 8)
        );

        g2.fillRoundRect(
                x + 5,
                y + 103,
                240,
                48,
                15,
                15
        );

        // Pan inferior
        g2.setColor(
                new Color(160, 80, 10)
        );

        g2.fillRoundRect(
                x,
                y + 150,
                250,
                35,
                12,
                12
        );
    }

    // ==========================================
    // BEBIDA
    // ==========================================

    private void dibujarBebida(
            Graphics2D g2,
            int x,
            int y) {

        // Vaso
        g2.setColor(
                new Color(60, 25, 8)
        );

        g2.fillRoundRect(
                x,
                y,
                75,
                150,
                12,
                12
        );

        // Refresco
        g2.setColor(
                new Color(40, 18, 5)
        );

        g2.fillRoundRect(
                x + 7,
                y + 18,
                61,
                125,
                10,
                10
        );

        // Hielo
        g2.setColor(
                new Color(230, 220, 205)
        );

        for (int i = 0; i < 6; i++) {

            g2.fillRoundRect(
                    x + 10
                    + (i * 13) % 45,

                    y + 30
                    + (i * 15) % 60,

                    13,
                    11,
                    4,
                    4
            );
        }

        // Pajilla
        g2.setColor(ROJO);

        g2.setStroke(
                new BasicStroke(
                        5,
                        BasicStroke.CAP_ROUND,
                        BasicStroke.JOIN_ROUND
                )
        );

        g2.drawLine(
                x + 35,
                y + 20,
                x + 58,
                y - 45
        );
    }

    // ==========================================
    // LOGIN
    // ==========================================

private void iniciarSesion() {

    String usuario = txtUsuario.getText().trim();
    String password = new String(txtPassword.getPassword());

    if (usuario.isEmpty() || password.isEmpty()) {
        JOptionPane.showMessageDialog(
                this,
                "Debes ingresar usuario y contraseña.",
                "Campos vacios",
                JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    String sql = "SELECT * FROM USUARIO WHERE usuario_login = ? AND contrasena = ? AND estado = 1";

    try (Connection con = conexion.getConnection()) {

        if (con == null) {
            return;
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuario);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    String rol = rs.getString("rol");
                    String nombreCompleto = rs.getString("nombre_completo");

                    JOptionPane.showMessageDialog(
                            this,
                            "¡Bienvenido a Chiksx Burger, " + nombreCompleto + "!",
                            "Inicio de sesion",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    dispose(); // cierra la ventana de login

                    int idUsuario = rs.getInt("id_usuario");

                        if (rol.equals("Administrador")) {
                        new PaneldeAdmin();
                        } else {
                        new Chiksxburgermenu(idUsuario).setVisible(true);
                        }

                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Usuario o contraseña incorrectos.",
                            "Error de acceso",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(
                this,
                "Error al consultar la base de datos:\n" + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
  }

    // ==========================================
    // REGISTRO (CREAR CUENTA)
    // ==========================================

    /**
     * Expresion regular simple para validar un correo electronico
     * (algo@algo.algo).
     */
    private static final String PATRON_CORREO =
            "^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$";

    private void crearCuenta() {

        String correo = txtUsuario.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (correo.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debes ingresar correo y contraseña.",
                    "Campos vacios",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (!correo.matches(PATRON_CORREO)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Ingresa un correo electronico valido.\nEjemplo: nombre@dominio.com",
                    "Correo invalido",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (password.length() < 6) {
            JOptionPane.showMessageDialog(
                    this,
                    "La contraseña debe tener al menos 6 caracteres.",
                    "Contraseña invalida",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String sqlExiste = "SELECT id_usuario FROM USUARIO WHERE usuario_login = ?";
        String sqlInsertar = "INSERT INTO USUARIO (nombre_completo, usuario_login, contrasena, rol, estado) " +
                              "VALUES (?, ?, ?, ?, 1)";

        try (Connection con = conexion.getConnection()) {

            if (con == null) {
                return;
            }

            // 1) Verificar que el correo no este registrado ya
            try (PreparedStatement psExiste = con.prepareStatement(sqlExiste)) {

                psExiste.setString(1, correo);

                try (ResultSet rs = psExiste.executeQuery()) {
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(
                                this,
                                "Ya existe una cuenta con ese correo.",
                                "Correo en uso",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }
                }
            }

            // 2) Insertar el nuevo usuario
            try (PreparedStatement psInsertar = con.prepareStatement(sqlInsertar)) {

                psInsertar.setString(1, correo);   // nombre_completo (usamos el correo)
                psInsertar.setString(2, correo);   // usuario_login
                psInsertar.setString(3, password); // contrasena
                psInsertar.setString(4, "Cajero"); // rol por defecto

                int filas = psInsertar.executeUpdate();

                if (filas == 1) {

                    JOptionPane.showMessageDialog(
                            this,
                            "¡Cuenta creada correctamente!",
                            "Registro exitoso",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    dispose(); // cierra la ventana de login/registro

                    new Chiksxburgermenu().setVisible(true);

                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "No se pudo crear la cuenta.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "Error al crear la cuenta:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}