/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.restaurantedtos;

/**
 *
 * @author Andrea Lara, Nahomi Figueroa, Zaira Barajas
 */
 public class DetalleComandaDTO {

    private Long idProducto;
    private String nombreProducto;
    private int cantidad;
    private String comentario;
    private double precio;
    private double subtotal;

    public DetalleComandaDTO(Long idProducto, String nombreProducto, int cantidad, String comentario, double precio, double subtotal) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.comentario = comentario;
        this.precio = precio;
        this.subtotal = subtotal;
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public String getComentario() {
        return comentario;
    }

    public double getPrecio() {
        return precio;
    }

    public double getSubtotal() {
        return subtotal;
    }

}
