package ChicxBurgerDB;

import main.Conexion.conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public boolean existeUsuarioLogin(String usuarioLogin) throws SQLException {
        String sql = "SELECT id_usuario FROM USUARIO WHERE usuario_login = ?";
        Connection con = conexion.getConnection();
        if (con == null) throw new SQLException("No hay conexion con la base de datos.");
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuarioLogin);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } finally {
            con.close();
        }
    }

    public boolean crearUsuario(String nombreCompleto, String usuarioLogin,
                                String contrasena, String rol) throws SQLException {
        String sql = "INSERT INTO USUARIO (nombre_completo, usuario_login, contrasena, rol, estado) "
                + "VALUES (?, ?, ?, ?, 1)";
        Connection con = conexion.getConnection();
        if (con == null) throw new SQLException("No hay conexion con la base de datos.");
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombreCompleto);
            ps.setString(2, usuarioLogin);
            ps.setString(3, contrasena);
            ps.setString(4, rol);
            return ps.executeUpdate() == 1;
        } finally {
            con.close();
        }
    }

    // fila lista para tabla: id, nombre, login, rol, estado(texto)
    public List<Object[]> listarUsuarios() throws SQLException {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT id_usuario, nombre_completo, usuario_login, rol, estado FROM USUARIO ORDER BY id_usuario";
        Connection con = conexion.getConnection();
        if (con == null) throw new SQLException("No hay conexion con la base de datos.");
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getInt("id_usuario"),
                    rs.getString("nombre_completo"),
                    rs.getString("usuario_login"),
                    rs.getString("rol"),
                    rs.getInt("estado") == 1 ? "Activo" : "Inactivo"
                });
            }
        } finally {
            con.close();
        }
        return lista;
    }

    public boolean actualizarUsuario(int id, String nombreCompleto, String usuarioLogin,
                                      String contrasena, String rol, int estado) throws SQLException {
        String sql = "UPDATE USUARIO SET nombre_completo=?, usuario_login=?, contrasena=?, rol=?, estado=? "
                + "WHERE id_usuario=?";
        Connection con = conexion.getConnection();
        if (con == null) throw new SQLException("No hay conexion con la base de datos.");
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombreCompleto);
            ps.setString(2, usuarioLogin);
            ps.setString(3, contrasena);
            ps.setString(4, rol);
            ps.setInt(5, estado);
            ps.setInt(6, id);
            return ps.executeUpdate() == 1;
        } finally {
            con.close();
        }
    }

    public boolean eliminarUsuario(int id) throws SQLException {
        String sql = "DELETE FROM USUARIO WHERE id_usuario=?";
        Connection con = conexion.getConnection();
        if (con == null) throw new SQLException("No hay conexion con la base de datos.");
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        } finally {
            con.close();
        }
    }

    public int obtenerIdPorLogin(String usuarioLogin) throws SQLException {
    String sql = "SELECT id_usuario FROM USUARIO WHERE usuario_login = ?";
    Connection con = conexion.getConnection();
    if (con == null) throw new SQLException("No hay conexion con la base de datos.");
    try (PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, usuarioLogin);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
            throw new SQLException("Usuario no encontrado: " + usuarioLogin);
        }
    } finally {
        con.close();
    }
}
    
}