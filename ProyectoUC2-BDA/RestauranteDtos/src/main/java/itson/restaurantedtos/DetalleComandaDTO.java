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
    private int cantidad;
    private String comentario;

    public DetalleComandaDTO() {
    }

    
    public DetalleComandaDTO(Long idProducto, int cantidad, String comentario) {
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.comentario = comentario;
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public String getComentario() {
        return comentario;
    }

}
