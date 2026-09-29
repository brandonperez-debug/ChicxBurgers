package ChicxBurgerDB;

public class Producto {

    // Atributos privados: representan las columnas que nos interesan de PRODUCTO
    private int idProducto;
    private String nombreProducto;
    private double precio;

    // Constructor: molde para crear un objeto Producto con sus 3 datos
    public Producto(int idProducto, String nombreProducto, double precio) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.precio = precio;
    }

    // Getters: puertas para leer los datos desde otras clases
    public int getIdProducto() {
        return idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public double getPrecio() {
        return precio;
    }
}