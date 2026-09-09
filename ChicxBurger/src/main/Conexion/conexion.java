package main.Conexion;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import javax.swing.JOptionPane;

public class conexion {
    private static final String url_default = "jdbc:mysql://localhost:3306/ChicxBurger";
    private static final String user_default = "root";
    private static final String password_default = "123456789";

    private static final String ARCHIVO_CONFIG = "db.properties";

    private static String url;
    private static String user;
    private static String password;
    private static boolean configCargada = false;

    private static synchronized void cargarConfig(){
       if (configCargada) {
            return;
        }
        Properties props = new Properties();
        try (InputStream in = new FileInputStream(ARCHIVO_CONFIG)) {
            props.load(in);
        } catch (IOException e) {
            // No existe db.properties: se usan los valores por defecto de desarrollo local.
        }
        url = props.getProperty("db.url", url_default);
        user = props.getProperty("db.user", user_default);
        password = props.getProperty("db.password", password_default);
        configCargada = true;
    }

    public static Connection getConnection(){
        cargarConfig();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                    "No se encontró el driver MySQL JDBC.\n" +
                    "Agrega mysql-connector-java.jar al classpath del proyecto.",
                    "Error de conexión",
                    JOptionPane.ERROR_MESSAGE);
            System.out.println("Error al conectar a la base de datos: com.mysql.cj.jdbc.Driver no encontrado");
            e.printStackTrace();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al conectar a la base de datos:\n" + e.getMessage(),
                    "Error de conexión",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error inesperado al conectar a la base de datos:\n" + e.getMessage(),
                    "Error de conexión",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return null;
        }
    }

