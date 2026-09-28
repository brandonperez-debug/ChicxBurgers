package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import main.Conexion.conexion;

// pantalla de login, ya la cambie para que se parezca al menu (lo de
// los fosiles y el ambar). ya no tiene lo de registrate aqui, ahora hay
// un boton normal que abre el formulario de crear cuenta

public class Login extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JCheckBox chkMostrar;

    public Login() {

        setTitle(
                "ChicxBurger - Login"
        );

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setExtendedState(
                JFrame.MAXIMIZED_BOTH
        );

        crearInterfaz();
    }

    // arma toda la ventana, el panel de la izquierda y el del formulario
    private void crearInterfaz() {

        JPanel raiz =
                new JPanel(
                        new BorderLayout()
                );

        JPanel panelMarca =
                buildPanelMarca();

        // antes esto era un numero fijo (560), ahora lo saco como un
        // porcentaje de la pantalla real para que el lado cafe se vea
        // mas grande y no se quede chiquito en pantallas grandes
        int anchoPantalla =
                GraphicsEnvironment.getLocalGraphicsEnvironment()
                        .getMaximumWindowBounds()
                        .width;

        int anchoMarca =
                (int) (anchoPantalla * 0.58);

        panelMarca.setPreferredSize(
                new Dimension(
                        anchoMarca,
                        10
                )
        );

        raiz.add(
                panelMarca,
                BorderLayout.WEST
        );

        raiz.add(
                buildPanelForm(),
                BorderLayout.CENTER
        );

        setContentPane(raiz);
    }

    // ==========================================
    // lado izquierdo, el oscuro con el logo y el texto de bienvenida
    // ==========================================

    private JPanel buildPanelMarca() {

        PanelMarca marca =
                new PanelMarca();

        marca.setLayout(
                new BorderLayout()
        );

        marca.setBorder(
                new EmptyBorder(
                        56,
                        48,
                        70,
                        62
                )
        );

        JPanel contenido =
                new JPanel();

        contenido.setLayout(
                new BoxLayout(
                        contenido,
                        BoxLayout.Y_AXIS
                )
        );

        contenido.setOpaque(false);

        // fila del logo, arriba de todo
        JPanel logoFila =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                10,
                                0
                        )
                );

        logoFila.setOpaque(false);
        logoFila.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel logoIcono =
                new JLabel(
                        iconoLogo(32)
                );

        JLabel logoTexto =
                new JLabel(
                        "CHICXBURGER"
                );

        logoTexto.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        20
                )
        );

        logoTexto.setForeground(
                Chiksxburgermenu.CREAM
        );

        logoFila.add(logoIcono);
        logoFila.add(logoTexto);

        // el titulo grande de bienvenida
        JLabel h1 =
                new JLabel(
                        "<html><div style='width:380px'>Excava tu proxima "
                        + "<font color='#e2a35a'>comida favorita</font></div></html>"
                );

        h1.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        34
                )
        );

        h1.setForeground(
                Chiksxburgermenu.CREAM
        );

        h1.setBorder(
                new EmptyBorder(
                        34,
                        0,
                        14,
                        0
                )
        );

        h1.setAlignmentX(Component.LEFT_ALIGNMENT);

        // parrafito chiquito debajo del titulo
        JLabel p =
                new JLabel(
                        "<html><div style='width:340px'>Inicia sesion para ver tu historial de pedidos, tus combos guardados y las promociones de esta era.</div></html>"
                );

        p.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );

        p.setForeground(
                Chiksxburgermenu.CREAM_2
        );

        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        // las huellitas de adorno, bien facil, nomas 3 iconos con menos opacidad cada uno
        JPanel huellas =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                24,
                                0
                        )
                );

        huellas.setOpaque(false);

        huellas.setBorder(
                new EmptyBorder(
                        34,
                        0,
                        0,
                        0
                )
        );

        huellas.setAlignmentX(Component.LEFT_ALIGNMENT);

        float[] alfas = {0.55f, 0.38f, 0.24f};
        int[] corrimientos = {0, 10, 20};

        for (int i = 0; i < 3; i++) {

            JLabel pie =
                    new JLabel(
                            iconoHuella(
                                    20,
                                    26,
                                    alfas[i]
                            )
                    );

            pie.setBorder(
                    new EmptyBorder(
                            corrimientos[i],
                            0,
                            0,
                            0
                    )
            );

            huellas.add(pie);
        }

        contenido.add(logoFila);
        contenido.add(h1);
        contenido.add(p);
        contenido.add(huellas);

        marca.add(
                contenido,
                BorderLayout.WEST
        );

        return marca;
    }

    // el panel con el fondo degradado, el brillo del ambar y el corte
    // rasgado del lado derecho (mismo truco que ya usamos en el menu,
    // nomas que aca va parado en vez de acostado)
    static class PanelMarca extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 =
                    (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int w = getWidth();
            int h = getHeight();

            GradientPaint fondo =
                    new GradientPaint(
                            0,
                            0,
                            Chiksxburgermenu.BASALTO,
                            w,
                            h,
                            Chiksxburgermenu.BASALTO_2
                    );

            g2.setPaint(fondo);

            g2.fillRect(
                    0,
                    0,
                    w,
                    h
            );

            RadialGradientPaint brillo =
                    new RadialGradientPaint(
                            new java.awt.geom.Point2D.Float(
                                    w * 0.25f,
                                    h * 0.2f
                            ),
                            Math.max(w, h) * 0.55f,
                            new float[]{0f, 1f},
                            new Color[]{
                                    new Color(
                                            Chiksxburgermenu.AMBAR.getRed(),
                                            Chiksxburgermenu.AMBAR.getGreen(),
                                            Chiksxburgermenu.AMBAR.getBlue(),
                                            85
                                    ),
                                    new Color(
                                            Chiksxburgermenu.AMBAR.getRed(),
                                            Chiksxburgermenu.AMBAR.getGreen(),
                                            Chiksxburgermenu.AMBAR.getBlue(),
                                            0
                                    )
                            }
                    );

            g2.setPaint(brillo);

            g2.fillRect(
                    0,
                    0,
                    w,
                    h
            );

            // aca va el cortecito irregular del lado derecho
            int franjaAncho = 40;

            Random rnd =
                    new Random(9);

            Path2D.Double borde =
                    new Path2D.Double();

            borde.moveTo(w, 0);
            borde.lineTo(w - franjaAncho, 0);

            int pasos = 20;

            for (int i = 0; i <= pasos; i++) {

                double y =
                        h * (i / (double) pasos);

                double x =
                        w - franjaAncho
                        + rnd.nextDouble() * (franjaAncho * 0.75);

                borde.lineTo(x, y);
            }

            borde.lineTo(w, h);
            borde.closePath();

            g2.setColor(
                    Chiksxburgermenu.CREAM
            );

            g2.fill(borde);

            g2.dispose();
        }
    }

    // el iconito del logo, un cuadrito con 3 rayitas adentro
    private Icon iconoLogo(int size) {

        return new Icon() {

            public int getIconWidth() {
                return size;
            }

            public int getIconHeight() {
                return size;
            }

            public void paintIcon(Component c, Graphics g, int x, int y) {

                Graphics2D g2 =
                        (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                g2.setColor(
                        Chiksxburgermenu.AMBAR
                );

                g2.fillRoundRect(
                        x,
                        y,
                        size,
                        size,
                        8,
                        8
                );

                g2.setColor(
                        Chiksxburgermenu.BASALTO
                );

                g2.setStroke(
                        new BasicStroke(
                                2.6f,
                                BasicStroke.CAP_ROUND,
                                BasicStroke.JOIN_ROUND
                        )
                );

                int pad = size / 5;

                for (int i = 0; i < 3; i++) {

                    int ly =
                            y + pad
                            + i * (size - 2 * pad) / 2;

                    g2.drawLine(
                            x + pad,
                            ly,
                            x + size - pad,
                            ly
                    );
                }

                g2.dispose();
            }
        };
    }

    // huellita chiquita nomas de adorno, la misma idea del menu
    private Icon iconoHuella(int w, int h, float alfa) {

        return new Icon() {

            public int getIconWidth() {
                return w;
            }

            public int getIconHeight() {
                return h;
            }

            public void paintIcon(Component c, Graphics g, int x, int y) {

                Graphics2D g2 =
                        (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                Color base =
                        Chiksxburgermenu.AMBAR_CLARO;

                Color col =
                        new Color(
                                base.getRed(),
                                base.getGreen(),
                                base.getBlue(),
                                (int) (255 * alfa)
                        );

                g2.setColor(col);

                g2.fillOval(
                        x,
                        y + h / 3,
                        w,
                        h * 2 / 3
                );

                int toeW = w / 3;

                g2.fillOval(x, y, toeW, h / 2);
                g2.fillOval(x + toeW, y - 2, toeW, h / 2 + 4);
                g2.fillOval(x + 2 * toeW, y, toeW, h / 2);

                g2.dispose();
            }
        };
    }

    // ==========================================
    // lado derecho, el formulario para entrar
    // ==========================================

    private JPanel buildPanelForm() {

        JPanel form =
                new JPanel(
                        new GridBagLayout()
                );

        form.setBackground(
                Chiksxburgermenu.CREAM
        );

        JPanel tarjeta =
                new JPanel();

        tarjeta.setLayout(
                new BoxLayout(
                        tarjeta,
                        BoxLayout.Y_AXIS
                )
        );

        tarjeta.setOpaque(false);

        tarjeta.setMaximumSize(
                new Dimension(
                        380,
                        560
                )
        );

        JLabel huevo =
                new JLabel(
                        iconoHuevo(46)
                );

        huevo.setAlignmentX(Component.LEFT_ALIGNMENT);

        huevo.setBorder(
                new EmptyBorder(
                        0,
                        0,
                        16,
                        0
                )
        );

        JLabel titulo =
                new JLabel(
                        "Bienvenido de vuelta"
                );

        titulo.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        26
                )
        );

        titulo.setForeground(
                Chiksxburgermenu.BROWN_950
        );

        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitulo =
                new JLabel(
                        "Inicia sesion para continuar"
                );

        subtitulo.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );

        subtitulo.setForeground(
                Chiksxburgermenu.INK_SOFT
        );

        subtitulo.setBorder(
                new EmptyBorder(
                        2,
                        0,
                        26,
                        0
                )
        );

        subtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtUsuario = new JTextField();
        txtPassword = new JPasswordField();

        JPanel campoUsuario =
                campoConEtiqueta(
                        "USUARIO",
                        txtUsuario
                );

        JPanel campoPassword =
                campoConEtiqueta(
                        "CONTRASEÑA",
                        txtPassword
                );

        chkMostrar =
                new JCheckBox(
                        "Mostrar contraseña"
                );

        chkMostrar.setOpaque(false);

        chkMostrar.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        chkMostrar.setForeground(
                Chiksxburgermenu.INK_SOFT
        );

        chkMostrar.setAlignmentX(Component.LEFT_ALIGNMENT);

        chkMostrar.setBorder(
                new EmptyBorder(
                        0,
                        0,
                        24,
                        0
                )
        );

        // cuando le den click al checkbox, se ve o se oculta la contraseña
        chkMostrar.addActionListener(
                e -> txtPassword.setEchoChar(
                        chkMostrar.isSelected() ? (char) 0 : '\u2022'
                )
        );

        JButton btnIngresar =
                new Chiksxburgermenu.BotonAmbar(
                        "Iniciar sesion"
                );

        btnIngresar.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnIngresar.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        48
                )
        );

        btnIngresar.addActionListener(
                e -> iniciarSesion()
        );

        JButton btnCrearCuenta =
                new JButton(
                        "Crear una cuenta nueva"
                );

        btnCrearCuenta.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnCrearCuenta.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        46
                )
        );

        btnCrearCuenta.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        14
                )
        );

        btnCrearCuenta.setForeground(
                Chiksxburgermenu.BROWN_950
        );

        btnCrearCuenta.setBackground(
                Chiksxburgermenu.CREAM_2
        );

        btnCrearCuenta.setFocusPainted(false);

        btnCrearCuenta.setBorder(
                BorderFactory.createLineBorder(
                        Chiksxburgermenu.CREAM_2,
                        2,
                        true
                )
        );

        btnCrearCuenta.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        // este boton nomas cierra el login y abre el formulario de crear cuenta
        btnCrearCuenta.addActionListener(
                e -> {
                    dispose();
                    new CrearUsuarioForm().setVisible(true);
                }
        );

        tarjeta.add(huevo);
        tarjeta.add(titulo);
        tarjeta.add(subtitulo);
        tarjeta.add(campoUsuario);
        tarjeta.add(Box.createVerticalStrut(16));
        tarjeta.add(campoPassword);
        tarjeta.add(Box.createVerticalStrut(6));
        tarjeta.add(chkMostrar);
        tarjeta.add(btnIngresar);
        tarjeta.add(Box.createVerticalStrut(12));
        tarjeta.add(btnCrearCuenta);

        form.add(tarjeta);

        return form;
    }

    // arma un campo con su etiqueta arriba, para no repetir el mismo codigo
    // para usuario y para contraseña
    private JPanel campoConEtiqueta(String texto, JTextField input) {

        JPanel p =
                new JPanel();

        p.setOpaque(false);

        p.setLayout(
                new BoxLayout(
                        p,
                        BoxLayout.Y_AXIS
                )
        );

        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl =
                new JLabel(texto);

        lbl.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        lbl.setForeground(
                Chiksxburgermenu.INK_SOFT
        );

        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        lbl.setBorder(
                new EmptyBorder(
                        0,
                        0,
                        5,
                        0
                )
        );

        input.setAlignmentX(Component.LEFT_ALIGNMENT);

        input.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        36
                )
        );

        input.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );

        input.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                Chiksxburgermenu.CREAM_2,
                                2,
                                true
                        ),
                        new EmptyBorder(
                                6,
                                10,
                                6,
                                10
                        )
                )
        );

        p.add(lbl);
        p.add(input);

        return p;
    }

    // el huevito con la grieta, nomas de adorno arriba del formulario
    private Icon iconoHuevo(int size) {

        return new Icon() {

            public int getIconWidth() {
                return size;
            }

            public int getIconHeight() {
                return size;
            }

            public void paintIcon(Component c, Graphics g, int x, int y) {

                Graphics2D g2 =
                        (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                g2.setColor(
                        Chiksxburgermenu.AMBAR_CLARO
                );

                g2.fillOval(
                        x + size / 6,
                        y,
                        size - size / 3,
                        size
                );

                g2.setColor(
                        Chiksxburgermenu.BASALTO
                );

                g2.setStroke(
                        new BasicStroke(
                                2.2f,
                                BasicStroke.CAP_ROUND,
                                BasicStroke.JOIN_ROUND
                        )
                );

                Path2D.Double grieta =
                        new Path2D.Double();

                grieta.moveTo(x + size * 0.38, y + size * 0.3);
                grieta.lineTo(x + size * 0.55, y + size * 0.45);
                grieta.lineTo(x + size * 0.35, y + size * 0.58);
                grieta.lineTo(x + size * 0.58, y + size * 0.72);

                g2.draw(grieta);

                g2.dispose();
            }
        };
    }

    // ==========================================
    // aca abajo va la logica de siempre, revisa la base de datos
    // ==========================================

    private void iniciarSesion() {

        String usuario =
                txtUsuario.getText().trim();

        String password =
                new String(
                        txtPassword.getPassword()
                );

        if (usuario.isEmpty() || password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debes ingresar usuario y contraseña.",
                    "Campos vacios",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String sql =
                "SELECT * FROM USUARIO WHERE usuario_login = ? AND contrasena = ? AND estado = 1";

        try (Connection con = conexion.getConnection()) {

            if (con == null) {
                return;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, usuario);
                ps.setString(2, password);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {

                        String rol =
                                rs.getString("rol");

                        String nombreCompleto =
                                rs.getString("nombre_completo");

                        JOptionPane.showMessageDialog(
                                this,
                                "¡Bienvenido a ChicxBurger, " + nombreCompleto + "!",
                                "Inicio de sesion",
                                JOptionPane.INFORMATION_MESSAGE
                        );

                        dispose();

                        // si es admin va al panel, si no, al menu de siempre
                        if (rol.equals("Administrador")) {

                            NavegadorAdmin.mostrar(new PaneldeAdmin());

                        } else {

                            new Chiksxburgermenu().setVisible(true);
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
}