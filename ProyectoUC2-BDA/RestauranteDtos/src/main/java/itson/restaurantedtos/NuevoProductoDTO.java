/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.restaurantedtos;

import java.util.List;

/**
 *
 * @author nafbr
 */
public class NuevoProductoDTO {
    private String nombre;
    private String descripcion;
    private Double precio;
    private TipoProducto tipo;
    private List<DetallesRecetaDTO> detallesReceta;

    public NuevoProductoDTO() {
    }

    public NuevoProductoDTO(String nombre, String descripcion, Double precio, TipoProducto tipo, List<DetallesRecetaDTO> detallesReceta) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.tipo = tipo;
        this.detallesReceta = detallesReceta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public TipoProducto getTipo() {
        return tipo;
    }

    public void setTipo(TipoProducto tipo) {
        this.tipo = tipo;
    }

    public List<DetallesRecetaDTO> getDetallesReceta() {
        return detallesReceta;
    }

    public void setDetallesReceta(List<DetallesRecetaDTO> detallesReceta) {
        this.detallesReceta = detallesReceta;
    }
}
