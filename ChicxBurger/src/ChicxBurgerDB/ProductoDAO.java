package ChicxBurgerDB;

import main.Conexion.conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    public List<Producto> listarProductosActivos() throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT id_producto, nombre_producto, precio FROM PRODUCTO WHERE estado = 1";
        Connection con = conexion.getConnection();
        if (con == null) throw new SQLException("No hay conexion con la base de datos.");
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                productos.add(new Producto(rs.getInt("id_producto"), rs.getString("nombre_producto"), rs.getDouble("precio")));
            }
        } finally {
            con.close();
        }
        return productos;
    }

    // fila lista para tabla de gestion: id, nombre, descripcion, precio, categoria, tiempo, estado
    public List<Object[]> listarTodos() throws SQLException {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT p.id_producto, p.nombre_producto, p.descripcion, p.precio, "
                + "c.nombre_categoria, t.nombre_horario, p.estado "
                + "FROM PRODUCTO p "
                + "JOIN CATEGORIA c ON p.id_categoria = c.id_categoria "
                + "JOIN TIEMPO_COMIDA t ON p.id_tiempo_comida = t.id_tiempo_comida "
                + "ORDER BY p.id_producto";
        Connection con = conexion.getConnection();
        if (con == null) throw new SQLException("No hay conexion con la base de datos.");
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getInt("id_producto"),
                    rs.getString("nombre_producto"),
                    rs.getString("descripcion"),
                    rs.getDouble("precio"),
                    rs.getString("nombre_categoria"),
                    rs.getString("nombre_horario"),
                    rs.getInt("estado") == 1 ? "Activo" : "Inactivo"
                });
            }
        } finally {
            con.close();
        }
        return lista;
    }

    public boolean insertarProducto(String nombre, String descripcion, double precio,
                                     int idCategoria, int idTiempoComida) throws SQLException {
        String sql = "INSERT INTO PRODUCTO (nombre_producto, descripcion, precio, id_categoria, id_tiempo_comida, estado) "
                + "VALUES (?, ?, ?, ?, ?, 1)";
        Connection con = conexion.getConnection();
        if (con == null) throw new SQLException("No hay conexion con la base de datos.");
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, descripcion);
            ps.setDouble(3, precio);
            ps.setInt(4, idCategoria);
            ps.setInt(5, idTiempoComida);
            return ps.executeUpdate() == 1;
        } finally {
            con.close();
        }
    }

    public boolean actualizarProducto(int id, String nombre, String descripcion, double precio,
                                       int idCategoria, int idTiempoComida, int estado) throws SQLException {
        String sql = "UPDATE PRODUCTO SET nombre_producto=?, descripcion=?, precio=?, id_categoria=?, "
                + "id_tiempo_comida=?, estado=? WHERE id_producto=?";
        Connection con = conexion.getConnection();
        if (con == null) throw new SQLException("No hay conexion con la base de datos.");
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, descripcion);
            ps.setDouble(3, precio);
            ps.setInt(4, idCategoria);
            ps.setInt(5, idTiempoComida);
            ps.setInt(6, estado);
            ps.setInt(7, id);
            return ps.executeUpdate() == 1;
        } finally {
            con.close();
        }
    }

    // soft delete: PRODUCTO tiene relaciones RESTRICT (PRODUCTO_INGREDIENTE, DETALLE_VENTA)
    // asi que en vez de borrar la fila, la desactivamos
    public boolean desactivarProducto(int id) throws SQLException {
        String sql = "UPDATE PRODUCTO SET estado = 0 WHERE id_producto = ?";
        Connection con = conexion.getConnection();
        if (con == null) throw new SQLException("No hay conexion con la base de datos.");
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        } finally {
            con.close();
        }
    }

    public List<Object[]> listarCategorias() throws SQLException {
        return listarGenerico("SELECT id_categoria, nombre_categoria FROM CATEGORIA");
    }

    public List<Object[]> listarTiemposComida() throws SQLException {
        return listarGenerico("SELECT id_tiempo_comida, nombre_horario FROM TIEMPO_COMIDA");
    }

    private List<Object[]> listarGenerico(String sql) throws SQLException {
        List<Object[]> lista = new ArrayList<>();
        Connection con = conexion.getConnection();
        if (con == null) throw new SQLException("No hay conexion con la base de datos.");
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Object[]{rs.getInt(1), rs.getString(2)});
            }
        } finally {
            con.close();
        }
        return lista;
    }

    // Busca un producto por nombre exacto; si no existe, lo crea con una
// categoria y horario genericos. Asi los productos "tematicos" del menu
// de clientes se convierten en filas reales de PRODUCTO la primera vez
// que alguien los pide, y DETALLE_VENTA puede referenciarlos sin violar
// la llave foranea.
public int obtenerOCrearProductoPorNombre(String nombre, double precio) throws SQLException {

    String sqlBuscar = "SELECT id_producto FROM PRODUCTO WHERE nombre_producto = ?";

    Connection con = conexion.getConnection();
    if (con == null) throw new SQLException("No hay conexion con la base de datos.");

    try {
        try (PreparedStatement ps = con.prepareStatement(sqlBuscar)) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        int idCategoria = obtenerOCrearCategoria(con, "Menu Cliente");
        int idTiempo = obtenerOCrearTiempoComida(con, "Todo el dia");

        String sqlInsertar = "INSERT INTO PRODUCTO (nombre_producto, descripcion, precio, id_categoria, id_tiempo_comida, estado) "
                + "VALUES (?, ?, ?, ?, ?, 1)";

        try (PreparedStatement ps = con.prepareStatement(sqlInsertar, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nombre);
            ps.setString(2, "Producto generado automaticamente desde el menu de clientes");
            ps.setDouble(3, precio);
            ps.setInt(4, idCategoria);
            ps.setInt(5, idTiempo);
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
                throw new SQLException("No se pudo crear el producto: " + nombre);
            }
        }

    } finally {
        con.close();
    }
}

private int obtenerOCrearCategoria(Connection con, String nombre) throws SQLException {
    String sqlBuscar = "SELECT id_categoria FROM CATEGORIA WHERE nombre_categoria = ?";
    try (PreparedStatement ps = con.prepareStatement(sqlBuscar)) {
        ps.setString(1, nombre);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        }
    }
    String sqlInsertar = "INSERT INTO CATEGORIA (nombre_categoria, descripcion) VALUES (?, ?)";
    try (PreparedStatement ps = con.prepareStatement(sqlInsertar, Statement.RETURN_GENERATED_KEYS)) {
        ps.setString(1, nombre);
        ps.setString(2, "Categoria generada automaticamente");
        ps.executeUpdate();
        try (ResultSet keys = ps.getGeneratedKeys()) {
            if (keys.next()) return keys.getInt(1);
            throw new SQLException("No se pudo crear la categoria: " + nombre);
        }
    }
}

private int obtenerOCrearTiempoComida(Connection con, String nombre) throws SQLException {
    String sqlBuscar = "SELECT id_tiempo_comida FROM TIEMPO_COMIDA WHERE nombre_horario = ?";
    try (PreparedStatement ps = con.prepareStatement(sqlBuscar)) {
        ps.setString(1, nombre);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        }
    }
    String sqlInsertar = "INSERT INTO TIEMPO_COMIDA (nombre_horario, hora_inicio, hora_fin) VALUES (?, '00:00:00', '23:59:59')";
    try (PreparedStatement ps = con.prepareStatement(sqlInsertar, Statement.RETURN_GENERATED_KEYS)) {
        ps.setString(1, nombre);
        ps.executeUpdate();
        try (ResultSet keys = ps.getGeneratedKeys()) {
            if (keys.next()) return keys.getInt(1);
            throw new SQLException("No se pudo crear el tiempo de comida: " + nombre);
        }
    }
}
}