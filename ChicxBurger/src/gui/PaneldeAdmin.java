package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PaneldeAdmin extends JFrame {

    // ==========================================
    // COLORES DE CHIKSX BURGER
    // ==========================================
    Color ROJO = Color.decode("#f53418");
    Color NARANJA = Color.decode("#f6781c");
    Color MOSTAZA = Color.decode("#a28813");
    Color DORADO = Color.decode("#db8f1b");
    Color BEIGE = Color.decode("#e0cfc8");
    Color CAFE = Color.decode("#661d05");

    // ==========================================
    // FUENTES
    // ==========================================
    Font titulo = new Font("Segoe UI", Font.BOLD, 25);
    Font subtitulo = new Font("Segoe UI", Font.BOLD, 17);
    Font normal = new Font("Segoe UI", Font.PLAIN, 14);
    Font boton = new Font("Segoe UI", Font.BOLD, 14);

    // ==========================================
    // CONSTRUCTOR
    // ==========================================
    public PaneldeAdmin() {
        
        
        
        setTitle("Chiksx Burger - Administracion");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        // ==========================================
        // BARRA LATERAL
        // ==========================================
        JPanel menu = new JPanel();
        menu.setBackground(CAFE);
        menu.setPreferredSize(new Dimension(230, 0));
        menu.setLayout(new BorderLayout());

        // ==========================================
        // LOGO
        // ==========================================
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(CAFE);
        logoPanel.setBorder(new EmptyBorder(30, 15, 25, 15));
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));

        JLabel logo = new JLabel("CHIKSX");
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 30));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel burger = new JLabel("BURGER");
        burger.setForeground(DORADO);
        burger.setFont(new Font("Segoe UI", Font.BOLD, 20));
        burger.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSeparator linea = new JSeparator();
        linea.setForeground(DORADO);
        linea.setMaximumSize(new Dimension(180, 2));

        logoPanel.add(logo);
        logoPanel.add(burger);
        logoPanel.add(Box.createVerticalStrut(15));
        logoPanel.add(linea);

        menu.add(logoPanel, BorderLayout.NORTH);

        // ==========================================
        // OPCIONES DEL MENU
        // ==========================================
        JPanel opciones = new JPanel();
        opciones.setBackground(CAFE);
        opciones.setLayout(new BoxLayout(opciones, BoxLayout.Y_AXIS));

        JButton inicio = crearBotonMenu("Inicio");
        JButton usuarios = crearBotonMenu("Gestion usuarios");
        JButton cuentas = crearBotonMenu("Gestion cuentas");
        JButton pedidos = crearBotonMenu("Pedidos");
        JButton productos = crearBotonMenu("Productos");
        JButton salir = crearBotonMenu("Cerrar sesion");

        opciones.add(inicio);
        opciones.add(usuarios);
        opciones.add(cuentas);
        opciones.add(pedidos);
        opciones.add(productos);

        opciones.add(Box.createVerticalGlue());

        opciones.add(salir);

        menu.add(opciones, BorderLayout.CENTER);

        // ==========================================
        // EVENTOS DE LOS BOTONES
        // ==========================================

        usuarios.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GestionUsuarios();
            }
        });

        cuentas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GestionCuentas();
            }
        });

        salir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int opcion = JOptionPane.showConfirmDialog(
                        PaneldeAdmin.this,
                        "¿Deseas cerrar sesion?",
                        "Cerrar sesion",
                        JOptionPane.YES_NO_OPTION
                );

                if (opcion == JOptionPane.YES_OPTION) {
                    dispose();
                }
            }
        });

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
        encabezado.setBorder(new EmptyBorder(15, 30, 15, 30));

        JLabel nombre = new JLabel("Chiksx Burger");
        nombre.setFont(titulo);
        nombre.setForeground(CAFE);

        JLabel administrador = new JLabel("Panel administrativo");
        administrador.setFont(normal);
        administrador.setForeground(Color.GRAY);

        JPanel textos = new JPanel();
        textos.setBackground(Color.WHITE);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));

        textos.add(nombre);
        textos.add(administrador);

        encabezado.add(textos, BorderLayout.WEST);

        JLabel usuarioAdmin = new JLabel("Administrador");
        usuarioAdmin.setFont(subtitulo);
        usuarioAdmin.setForeground(CAFE);

        encabezado.add(usuarioAdmin, BorderLayout.EAST);

        principal.add(encabezado, BorderLayout.NORTH);

        // ==========================================
        // CONTENIDO
        // ==========================================
        JPanel contenido = new JPanel();
        contenido.setBackground(BEIGE);
        contenido.setBorder(new EmptyBorder(25, 35, 25, 35));
        contenido.setLayout(new BorderLayout(20, 20));

        // ==========================================
        // TITULO
        // ==========================================
        JPanel tituloPanel = new JPanel(new BorderLayout());
        tituloPanel.setBackground(BEIGE);

        JLabel bienvenida = new JLabel("Panel de control");
        bienvenida.setFont(titulo);
        bienvenida.setForeground(CAFE);

        JLabel descripcion = new JLabel(
                "Administra los usuarios, cuentas y pedidos de Chiksx Burger"
        );
        descripcion.setFont(normal);
        descripcion.setForeground(Color.DARK_GRAY);

        JPanel textoTitulo = new JPanel();
        textoTitulo.setBackground(BEIGE);
        textoTitulo.setLayout(new BoxLayout(textoTitulo, BoxLayout.Y_AXIS));

        textoTitulo.add(bienvenida);
        textoTitulo.add(Box.createVerticalStrut(5));
        textoTitulo.add(descripcion);

        tituloPanel.add(textoTitulo, BorderLayout.WEST);

        contenido.add(tituloPanel, BorderLayout.NORTH);

        // ==========================================
        // CENTRO
        // ==========================================
        JPanel centro = new JPanel();
        centro.setBackground(BEIGE);
        centro.setLayout(new BorderLayout(20, 20));

        // ==========================================
        // TARJETAS
        // ==========================================
        JPanel tarjetas = new JPanel(new GridLayout(1, 4, 15, 0));
        tarjetas.setBackground(BEIGE);

        tarjetas.add(crearTarjeta(
                "HAMBURGUESAS",
                "24",
                ROJO
        ));

        tarjetas.add(crearTarjeta(
                "BEBIDAS",
                "18",
                NARANJA
        ));

        tarjetas.add(crearTarjeta(
                "USUARIOS",
                "35",
                MOSTAZA
        ));

        tarjetas.add(crearTarjeta(
                "CUENTAS",
                "12",
                DORADO
        ));

        centro.add(tarjetas, BorderLayout.NORTH);

        // ==========================================
        // PARTE INFERIOR
        // ==========================================
        JPanel inferior = new JPanel(new BorderLayout(20, 0));
        inferior.setBackground(BEIGE);

        // ==========================================
        // BOTONES DE GESTION
        // ==========================================
        JPanel gestion = new JPanel();
        gestion.setBackground(Color.WHITE);
        gestion.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CAFE, 1),
                new EmptyBorder(20, 20, 20, 20)
        ));

        gestion.setLayout(new BoxLayout(gestion, BoxLayout.Y_AXIS));

        JLabel tituloGestion = new JLabel("Gestion administrativa");
        tituloGestion.setFont(subtitulo);
        tituloGestion.setForeground(CAFE);
        tituloGestion.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel textoGestion = new JLabel(
                "Selecciona una opcion"
        );
        textoGestion.setFont(normal);
        textoGestion.setForeground(Color.GRAY);
        textoGestion.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnUsuarios = crearBotonGestion(
                "GESTION USUARIOS",
                ROJO
        );

        JButton btnCuentas = crearBotonGestion(
                "GESTION CUENTAS",
                CAFE
        );

        gestion.add(tituloGestion);
        gestion.add(Box.createVerticalStrut(5));
        gestion.add(textoGestion);
        gestion.add(Box.createVerticalStrut(25));
        gestion.add(btnUsuarios);
        gestion.add(Box.createVerticalStrut(15));
        gestion.add(btnCuentas);

        btnUsuarios.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GestionUsuarios();
            }
        });

        btnCuentas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GestionCuentas();
            }
        });

        // ==========================================
        // TABLA DE PEDIDOS
        // ==========================================
        JPanel tablaPanel = new JPanel(new BorderLayout());
        tablaPanel.setBackground(Color.WHITE);
        tablaPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CAFE, 1),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel tituloPedidos = new JLabel("Pedidos recientes");
        tituloPedidos.setFont(subtitulo);
        tituloPedidos.setForeground(CAFE);

        tablaPanel.add(tituloPedidos, BorderLayout.NORTH);

        String[] columnas = {
            "No.",
            "Cliente",
            "Pedido",
            "Tipo",
            "Total",
            "Estado"
        };

        Object[][] datos = {
            {"01", "Carlos", "Hamburguesa Clasica", "Hamburguesa", "Q35.00", "Entregado"},
            {"02", "Maria", "Combo Chiksx", "Hamburguesa", "Q48.00", "Preparando"},
            {"03", "Jose", "Coca Cola", "Bebida", "Q12.00", "Entregado"},
            {"04", "Ana", "Hamburguesa BBQ", "Hamburguesa", "Q42.00", "Pendiente"},
            {"05", "Luis", "Combo Familiar", "Hamburguesa", "Q85.00", "Preparando"}
        };

        DefaultTableModel modelo = new DefaultTableModel(datos, columnas) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tabla = new JTable(modelo);

        tabla.setFont(normal);
        tabla.setRowHeight(35);
        tabla.getTableHeader().setFont(
                new Font("Segoe UI", Font.BOLD, 13)
        );
        tabla.getTableHeader().setBackground(CAFE);
        tabla.getTableHeader().setForeground(Color.WHITE);

        tabla.setSelectionBackground(BEIGE);
        tabla.setSelectionForeground(CAFE);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        tablaPanel.add(scroll, BorderLayout.CENTER);

        inferior.add(gestion, BorderLayout.WEST);
        inferior.add(tablaPanel, BorderLayout.CENTER);

        centro.add(inferior, BorderLayout.CENTER);

        contenido.add(centro, BorderLayout.CENTER);

        principal.add(contenido, BorderLayout.CENTER);

        // ==========================================
        //  AGREGAR A LA VENTANA
        // ==========================================
        add(menu, BorderLayout.WEST);
        add(principal, BorderLayout.CENTER);

        setVisible(true);
    }

    // ==========================================
    // CREAR BOTON DEL MENU
    // ==========================================
    private JButton crearBotonMenu(String texto) {

        JButton boton = new JButton(texto);

        boton.setFont(normal);
        boton.setForeground(Color.WHITE);
        boton.setBackground(CAFE);

        boton.setBorder(BorderFactory.createEmptyBorder(
                15, 20, 15, 10
        ));

        boton.setHorizontalAlignment(SwingConstants.LEFT);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        boton.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                boton.setBackground(ROJO);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                boton.setBackground(CAFE);
            }
        });

        return boton;
    }

    // ==========================================
    // CREAR TARJETA
    // ==========================================
    private JPanel crearTarjeta(
            String titulo,
            String numero,
            Color color
    ) {

        JPanel tarjeta = new JPanel();
        tarjeta.setBackground(Color.WHITE);

        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 2),
                new EmptyBorder(15, 15, 15, 15)
        ));

        tarjeta.setLayout(new BoxLayout(
                tarjeta,
                BoxLayout.Y_AXIS
        ));

        JLabel tituloLabel = new JLabel(titulo);
        tituloLabel.setFont(
                new Font("Segoe UI", Font.BOLD, 12)
        );
        tituloLabel.setForeground(Color.GRAY);
        tituloLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel numeroLabel = new JLabel(numero);
        numeroLabel.setFont(
                new Font("Segoe UI", Font.BOLD, 30)
        );
        numeroLabel.setForeground(color);
        numeroLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        tarjeta.add(tituloLabel);
        tarjeta.add(Box.createVerticalStrut(8));
        tarjeta.add(numeroLabel);

        return tarjeta;
    }

    // ==========================================
    // CREAR BOTON DE GESTION
    // ==========================================
    private JButton crearBotonGestion(
        String texto,
        Color color
) {

    JButton boton = new JButton(texto);

    boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
    boton.setForeground(Color.WHITE);
    boton.setBackground(color);

    boton.setPreferredSize(new Dimension(240, 55));
    boton.setMaximumSize(new Dimension(240, 55));
    boton.setAlignmentX(Component.CENTER_ALIGNMENT);

    boton.setFocusPainted(false);
    boton.setBorder(BorderFactory.createEmptyBorder());
    boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

    return boton;
}
    // ==========================================
    // MAIN
    // ==========================================
    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new PaneldeAdmin();
            }
        });
    }
    
    
}