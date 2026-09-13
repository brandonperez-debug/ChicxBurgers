package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import ChicxBurgerDB.ProductoDAO;

public class GestionCatalogo extends JFrame {

    Color ROJO = Color.decode("#f53418");
    Color CAFE = Color.decode("#661d05");
    Color BEIGE = Color.decode("#e0cfc8");
    Color DORADO = Color.decode("#db8f1b");

    JTable tabla;
    DefaultTableModel modelo;
    ProductoDAO productoDAO = new ProductoDAO();

    public GestionCatalogo() {

        setTitle("Chiksx Burger - Catalogo y Menu Dinamico");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel principal = new JPanel(new BorderLayout());
        principal.setBackground(BEIGE);

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(Color.WHITE);
        encabezado.setBorder(new EmptyBorder(15, 30, 15, 30));

        JLabel titulo = new JLabel("Catalogo y Menu Dinamico");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 25));
        titulo.setForeground(CAFE);

        JLabel subtitulo = new JLabel("Productos activos disponibles para la venta");
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GestionCatalogo();
            }
        });
    }
}