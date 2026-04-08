/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.restaurantedtos;

import java.util.List;

/**
 *
 * @author Nahomi Figueroa
 */
public class ProductoDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Double precio;
    private TipoProducto tipo;
    private EstadoProducto estado;
    private byte[] imagen;
    private List<DetallesRecetaDTO> detallesReceta;

    public ProductoDTO() {
    }

    public Long getId() {
        return id;
    }

    public EstadoProducto getEstado() {
        return estado;
    }

    public void setEstado(EstadoProducto estado) {
        this.estado = estado;
    }

    public void setId(Long id) {
        this.id = id;
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

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public void setDetallesReceta(List<DetallesRecetaDTO> detallesReceta) {
        this.detallesReceta = detallesReceta;
    }
}
