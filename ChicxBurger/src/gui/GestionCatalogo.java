package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import ChicxBurgerDB.ProductoDAO;

public class GestionCatalogo extends JFrame {

    Color ROJO = Chiksxburgermenu.RED;
    Color CAFE = Chiksxburgermenu.BROWN_950;
    Color BEIGE = Chiksxburgermenu.CREAM;
    Color DORADO = Chiksxburgermenu.AMBAR_CLARO;

    JTable tabla;
    DefaultTableModel modelo;
    ProductoDAO productoDAO = new ProductoDAO();

    // barra lateral que sale del logo, empieza escondida
    private JPanel sidebar;
    private boolean sidebarVisible = false;

    public GestionCatalogo() {

        setTitle("ChicxBurger - Catalogo y Menu Dinamico");
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

        JLabel titulo = new JLabel("Catalogo y Menu Dinamico");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(Chiksxburgermenu.GOLD);
        encabezado.add(titulo, BorderLayout.EAST);

        principal.add(encabezado, BorderLayout.NORTH);

        JPanel contenido = new JPanel(new BorderLayout(20, 20));
        contenido.setBackground(BEIGE);
        contenido.setBorder(new EmptyBorder(25, 30, 25, 30));

        JPanel panelTabla = new JPanel(new BorderLayout(10, 10));
        panelTabla.setBackground(Color.WHITE);
        panelTabla.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CAFE, 1),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JButton btnActualizar = crearBoton("ACTUALIZAR", DORADO);
        JPanel topTabla = new JPanel(new BorderLayout());
        topTabla.setBackground(Color.WHITE);
        JLabel tituloTabla = new JLabel("Productos activos (todas las categorias y horarios)");
        tituloTabla.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tituloTabla.setForeground(CAFE);
        topTabla.add(tituloTabla, BorderLayout.WEST);
        topTabla.add(btnActualizar, BorderLayout.EAST);
        panelTabla.add(topTabla, BorderLayout.NORTH);

        String[] columnas = {"ID", "Nombre", "Descripcion", "Precio", "Categoria", "Horario", "Estado"};
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

        contenido.add(panelTabla, BorderLayout.CENTER);
        principal.add(contenido, BorderLayout.CENTER);
        add(principal);

        cargarCatalogo();

        btnActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarCatalogo();
            }
        });

        setVisible(true);
    }

    private void cargarCatalogo() {
        modelo.setRowCount(0);
        try {
            for (Object[] fila : productoDAO.listarTodos()) {
                // solo mostramos los activos, que es lo que aparece en el menu real
                if (fila[6].equals("Activo")) {
                    modelo.addRow(fila);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar el catalogo: " + e.getMessage());
        }
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GestionCatalogo();
            }
        });
    }
}