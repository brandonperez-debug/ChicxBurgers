package ChicxBurgerDB;

import main.Conexion.conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Acceso a datos de la tabla USUARIO (empleados: Administrador / Cajero).
 * Usa la clase main.Conexion.conexion ya existente en el proyecto.
 * Esa clase devuelve null (y muestra su propio dialogo de error) si
 * la conexion falla, por eso aqui se revisa "con == null" antes de usarla.
 */
public class UsuarioDAO {

    /** Devuelve true si ya existe un usuario con ese login (usuario_login es UNIQUE). */
    public boolean existeUsuarioLogin(String usuarioLogin) throws SQLException {
        String sql = "SELECT id_usuario FROM USUARIO WHERE usuario_login = ?";
        Connection con = conexion.getConnection();
        if (con == null) {
            // conexion.getConnection() ya mostro el JOptionPane con el error
            throw new SQLException("No hay conexion con la base de datos.");
        }
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuarioLogin);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } finally {
            con.close();
        }
    }

    /** Inserta un nuevo empleado. rol debe ser "Administrador" o "Cajero". */
    public boolean crearUsuario(String nombreCompleto, String usuarioLogin,
                                 String contrasena, String rol) throws SQLException {
        String sql = "INSERT INTO USUARIO (nombre_completo, usuario_login, contrasena, rol, estado) " +
                     "VALUES (?, ?, ?, ?, 1)";
        Connection con = conexion.getConnection();
        if (con == null) {
            throw new SQLException("No hay conexion con la base de datos.");
        }
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
}