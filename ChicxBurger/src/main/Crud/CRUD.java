package main.Crud;

import java.sql.*;
import javax.swing.JOptionPane;
import Funcionalidades.PasswordUtil;
import main.Conexion.conexion;

public class CRUD {
    // CREAR
    public boolean nuevoUsuario(String nombreCompleto, String usuarioLogin,
            String contrasena, String rol) {

        // Checar que el rol sea válido según el ENUM de la base de datos
        if (!"Administrador".equals(rol) && !"Cajero".equals(rol)) {
            JOptionPane.showMessageDialog(null,
                    "El rol debe ser 'Administrador' o 'Cajero'.",
                    "Rol inválido",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validar contraseña mínima (la BD pide al menos 6 caracteres)
        if (contrasena == null || contrasena.length() < 6) {
            JOptionPane.showMessageDialog(null,
                    "La contraseña debe tener al menos 6 caracteres.",
                    "Contraseña inválida",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String sql = "INSERT INTO USUARIO (nombre_completo, usuario_login, contrasena, rol, estado) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = conexion.getConnection()) {
            if (con == null) {
                JOptionPane.showMessageDialog(null,
                        "No se pudo establecer conexión con la base de datos.",
                        "Error de conexión",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, nombreCompleto);
                ps.setString(2, usuarioLogin);
                ps.setString(3, PasswordUtil.hash(contrasena));
                ps.setString(4, rol);
                ps.setBoolean(5, true); // Por defecto entra activo

                ps.executeUpdate();
                System.out.println("El usuario '" + usuarioLogin + "' fue creado con éxito en ChicxBurger.");
                return true;
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            // Salta si el usuario_login ya está registrado (es UNIQUE)
            JOptionPane.showMessageDialog(null,
                    "Ya existe un usuario con ese nombre de usuario (login).",
                    "Usuario duplicado",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al crear el usuario: " + e.getMessage(),
                    "Error de base de datos",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }
    // EDITAR
    public boolean editarUsuario(int idUsuario, String nombreCompleto, String usuarioLogin,
            String rol, boolean estado) {

        String sql = "UPDATE USUARIO SET nombre_completo = ?, usuario_login = ?, rol = ?, estado = ? "
                + "WHERE id_usuario = ?";

        try (Connection con = conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombreCompleto);
            ps.setString(2, usuarioLogin);
            ps.setString(3, rol);
            ps.setBoolean(4, estado);
            ps.setInt(5, idUsuario);

            int filas = ps.executeUpdate();
            if (filas > 0) {
                System.out.println("El usuario con ID " + idUsuario + " fue actualizado con éxito en ChicxBurger.");
                return true;
            } else {
                System.out.println("No se encontró ningún usuario con ID " + idUsuario + " para actualizar.");
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Error de SQL al editar el usuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Actualiza únicamente la contraseña (va por separado por el tema del hash)
    public boolean editarContrasenaUsuario(int idUsuario, String nuevaContrasena) {
        if (nuevaContrasena == null || nuevaContrasena.length() < 6) {
            JOptionPane.showMessageDialog(null,
                    "La contraseña debe tener al menos 6 caracteres.",
                    "Contraseña inválida",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String sql = "UPDATE USUARIO SET contrasena = ? WHERE id_usuario = ?";

        try (Connection con = conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, PasswordUtil.hash(nuevaContrasena));
            ps.setInt(2, idUsuario);

            int filas = ps.executeUpdate();
            System.out.println("Contraseña actualizada para el usuario con ID " + idUsuario + ".");
            return filas > 0;

        } catch (SQLException e) {
            System.out.println("Error de SQL al editar la contraseña: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // ELIMINAR (Borrado físico)
    public boolean eliminarUsuario(int idUsuario) {
        String sql = "DELETE FROM USUARIO WHERE id_usuario = ?";

        try (Connection con = conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            int filas = ps.executeUpdate();

            if (filas > 0) {
                System.out.println("El usuario con ID " + idUsuario + " fue eliminado con éxito de ChicxBurger.");
                return true;
            } else {
                System.out.println("No se encontró ningún usuario con ID " + idUsuario + " para eliminar.");
                return false;
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            // Pasa si el usuario tiene turnos o ventas amarradas (por el ON DELETE RESTRICT)
            JOptionPane.showMessageDialog(null,
                    "No se puede eliminar el usuario: tiene turnos o ventas asociadas.\n"
                    + "Puedes desactivarlo en su lugar.",
                    "No se pudo eliminar",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        } catch (SQLException e) {
            System.out.println("Error de SQL al eliminar el usuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Desactivar cuenta (baja lógica para no perder el historial de ventas o turnos)
    public boolean desactivarUsuario(int idUsuario) {
        String sql = "UPDATE USUARIO SET estado = 0 WHERE id_usuario = ?";

        try (Connection con = conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            int filas = ps.executeUpdate();
            System.out.println("La cuenta del usuario con ID " + idUsuario + " ahora está inactiva en ChicxBurger.");
            return filas > 0;

        } catch (SQLException e) {
            System.out.println("Error de SQL al desactivar el usuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean activarUsuario(int idUsuario) {
        String sql = "UPDATE USUARIO SET estado = 1 WHERE id_usuario = ?";

        try (Connection con = conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            int filas = ps.executeUpdate();
            System.out.println("La cuenta del usuario con ID " + idUsuario + " ahora está activa en ChicxBurger.");
            return filas > 0;

        } catch (SQLException e) {
            System.out.println("Error de SQL al activar el usuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // BUSCAR

    // Revisa si el ID existe y lo regresa, si no, devuelve null
    public Integer buscarUsuario(int idUsuario) {
        Integer idEncontrado = null;
        String sql = "SELECT id_usuario FROM USUARIO WHERE id_usuario = ?";

        try (Connection con = conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    idEncontrado = rs.getInt("id_usuario");
                    System.out.println("El usuario con ID " + idEncontrado + " fue encontrado con éxito en ChicxBurger.");
                } else {
                    System.out.println("No se encontró ningún usuario con ID " + idUsuario);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error de SQL al buscar el usuario por ID: " + e.getMessage());
            e.printStackTrace();
        }
        return idEncontrado;
    }

    // Busca por el nombre de usuario (suele usarse en el login)
    public String buscarUsuarioPorLogin(String usuarioLogin) {
        String loginEncontrado = null;
        String sql = "SELECT usuario_login FROM USUARIO WHERE usuario_login = ?";

        try (Connection con = conexion.getConnection()) {
            if (con == null) {
                JOptionPane.showMessageDialog(null,
                        "No se pudo establecer conexión con la base de datos.",
                        "Error de conexión",
                        JOptionPane.ERROR_MESSAGE);
                return null;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, usuarioLogin);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        loginEncontrado = rs.getString("usuario_login");
                        System.out.println("El usuario '" + loginEncontrado + "' fue encontrado con éxito en ChicxBurger.");
                    } else {
                        System.out.println("No se encontró ningún usuario con login: " + usuarioLogin);
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Error de SQL al buscar el usuario por login: " + e.getMessage());
            e.printStackTrace();
        }
        return loginEncontrado;
    }

    // Trae toda la info del usuario (bueno para rellenar campos al editar)
    public ResultSetUsuario buscarUsuarioCompleto(int idUsuario) {
        String sql = "SELECT id_usuario, nombre_completo, usuario_login, rol, estado "
                + "FROM USUARIO WHERE id_usuario = ?";

        try (Connection con = conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new ResultSetUsuario(
                            rs.getInt("id_usuario"),
                            rs.getString("nombre_completo"),
                            rs.getString("usuario_login"),
                            rs.getString("rol"),
                            rs.getBoolean("estado")
                    );
                } else {
                    System.out.println("No se encontró ningún usuario con ID " + idUsuario);
                    return null;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error de SQL al buscar el usuario: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Comprueba las credenciales haciendo match con el hash guardado (solo si está activo)
    public boolean validarCredenciales(String usuarioLogin, String contrasena) {
        String sql = "SELECT contrasena FROM USUARIO WHERE usuario_login = ? AND estado = 1";

        try (Connection con = conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuarioLogin);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hashGuardado = rs.getString("contrasena");
                    return PasswordUtil.hash(contrasena).equals(hashGuardado);
                }
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Error de SQL al validar credenciales: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Clase auxiliar para mover los datos del usuario cómodamente
    public static class ResultSetUsuario {
        public final int idUsuario;
        public final String nombreCompleto;
        public final String usuarioLogin;
        public final String rol;
        public final boolean estado;

        public ResultSetUsuario(int idUsuario, String nombreCompleto, String usuarioLogin,
                String rol, boolean estado) {
            this.idUsuario = idUsuario;
            this.nombreCompleto = nombreCompleto;
            this.usuarioLogin = usuarioLogin;
            this.rol = rol;
            this.estado = estado;
        }
    }

}

