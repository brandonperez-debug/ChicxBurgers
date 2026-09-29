package ChicxBurgerDB;

import main.Conexion.conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IngredienteDAO {

    // fila: id, nombre, unidad, stock_actual, stock_minimo, proveedor
    public List<Object[]> listarTodos() throws SQLException {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT i.id_ingrediente, i.nombre_ingrediente, i.unidad_medida, "
                + "i.stock_actual, i.stock_minimo, p.nombre_proveedor "
                + "FROM INGREDIENTE i JOIN PROVEEDOR p ON i.id_proveedor = p.id_proveedor "
                + "ORDER BY i.id_ingrediente";
        Connection con = conexion.getConnection();
        if (con == null) throw new SQLException("No hay conexion con la base de datos.");
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getInt("id_ingrediente"),
                    rs.getString("nombre_ingrediente"),
                    rs.getString("unidad_medida"),
                    rs.getDouble("stock_actual"),
                    rs.getDouble("stock_minimo"),
                    rs.getString("nombre_proveedor")
                });
            }
        } finally {
            con.close();
        }
        return lista;
    }

    public boolean insertarIngrediente(String nombre, String unidad, double stockActual,
                                        double stockMinimo, int idProveedor) throws SQLException {
        String sql = "INSERT INTO INGREDIENTE (nombre_ingrediente, unidad_medida, stock_actual, stock_minimo, id_proveedor) "
                + "VALUES (?, ?, ?, ?, ?)";
        Connection con = conexion.getConnection();
        if (con == null) throw new SQLException("No hay conexion con la base de datos.");
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, unidad);
            ps.setDouble(3, stockActual);
            ps.setDouble(4, stockMinimo);
            ps.setInt(5, idProveedor);
            return ps.executeUpdate() == 1;
        } finally {
            con.close();
        }
    }

    public boolean actualizarIngrediente(int id, String nombre, String unidad, double stockActual,
                                          double stockMinimo, int idProveedor) throws SQLException {
        String sql = "UPDATE INGREDIENTE SET nombre_ingrediente=?, unidad_medida=?, stock_actual=?, "
                + "stock_minimo=?, id_proveedor=? WHERE id_ingrediente=?";
        Connection con = conexion.getConnection();
        if (con == null) throw new SQLException("No hay conexion con la base de datos.");
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, unidad);
            ps.setDouble(3, stockActual);
            ps.setDouble(4, stockMinimo);
            ps.setInt(5, idProveedor);
            ps.setInt(6, id);
            return ps.executeUpdate() == 1;
        } finally {
            con.close();
        }
    }

    public boolean eliminarIngrediente(int id) throws SQLException {
        String sql = "DELETE FROM INGREDIENTE WHERE id_ingrediente=?";
        Connection con = conexion.getConnection();
        if (con == null) throw new SQLException("No hay conexion con la base de datos.");
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        } finally {
            con.close();
        }
    }

    public List<Object[]> listarProveedores() throws SQLException {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT id_proveedor, nombre_proveedor FROM PROVEEDOR";
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
}