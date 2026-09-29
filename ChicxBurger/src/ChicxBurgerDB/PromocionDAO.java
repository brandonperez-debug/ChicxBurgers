package ChicxBurgerDB;

import main.Conexion.conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PromocionDAO {

    // fila: id, nombre, descripcion, tipo, valor, fecha_inicio, fecha_fin, estado
    public List<Object[]> listarTodas() throws SQLException {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT id_promocion, nombre_promocion, descripcion, tipo_descuento, valor_descuento, "
                + "fecha_inicio, fecha_fin, estado FROM PROMOCION ORDER BY id_promocion";
        Connection con = conexion.getConnection();
        if (con == null) throw new SQLException("No hay conexion con la base de datos.");
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getInt("id_promocion"),
                    rs.getString("nombre_promocion"),
                    rs.getString("descripcion"),
                    rs.getString("tipo_descuento"),
                    rs.getDouble("valor_descuento"),
                    rs.getDate("fecha_inicio"),
                    rs.getDate("fecha_fin"),
                    rs.getInt("estado") == 1 ? "Activo" : "Inactivo"
                });
            }
        } finally {
            con.close();
        }
        return lista;
    }

    public boolean insertarPromocion(String nombre, String descripcion, String tipoDescuento,
                                      double valor, Date fechaInicio, Date fechaFin) throws SQLException {
        String sql = "INSERT INTO PROMOCION (nombre_promocion, descripcion, tipo_descuento, valor_descuento, "
                + "fecha_inicio, fecha_fin, estado) VALUES (?, ?, ?, ?, ?, ?, 1)";
        Connection con = conexion.getConnection();
        if (con == null) throw new SQLException("No hay conexion con la base de datos.");
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, descripcion);
            ps.setString(3, tipoDescuento);
            ps.setDouble(4, valor);
            ps.setDate(5, fechaInicio);
            ps.setDate(6, fechaFin);
            return ps.executeUpdate() == 1;
        } finally {
            con.close();
        }
    }

    public boolean actualizarPromocion(int id, String nombre, String descripcion, String tipoDescuento,
                                        double valor, Date fechaInicio, Date fechaFin, int estado) throws SQLException {
        String sql = "UPDATE PROMOCION SET nombre_promocion=?, descripcion=?, tipo_descuento=?, valor_descuento=?, "
                + "fecha_inicio=?, fecha_fin=?, estado=? WHERE id_promocion=?";
        Connection con = conexion.getConnection();
        if (con == null) throw new SQLException("No hay conexion con la base de datos.");
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, descripcion);
            ps.setString(3, tipoDescuento);
            ps.setDouble(4, valor);
            ps.setDate(5, fechaInicio);
            ps.setDate(6, fechaFin);
            ps.setInt(7, estado);
            ps.setInt(8, id);
            return ps.executeUpdate() == 1;
        } finally {
            con.close();
        }
    }

    // VENTA.id_promocion tiene ON DELETE SET NULL, asi que el borrado real es seguro
    public boolean eliminarPromocion(int id) throws SQLException {
        String sql = "DELETE FROM PROMOCION WHERE id_promocion=?";
        Connection con = conexion.getConnection();
        if (con == null) throw new SQLException("No hay conexion con la base de datos.");
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        } finally {
            con.close();
        }
    }
}