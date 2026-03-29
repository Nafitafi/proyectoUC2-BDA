/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.restaurantedtos;

/**
 * DTO para el ingrediente recién creado.
 * @author Zaira Paola Barajas Díaz
 */
public class IngredienteNuevoDTO {
    private String nombre;
    private UnidadMedida unidadMedida;
    private Double stock;
    private String imagen;

    public IngredienteNuevoDTO() {
    }

    public IngredienteNuevoDTO(String nombre, UnidadMedida unidadMedida, Double stock, String imagen) {
        this.nombre = nombre;
        this.unidadMedida = unidadMedida;
        this.stock = stock;
        this.imagen = imagen;
    }

    public IngredienteNuevoDTO(String nombre, UnidadMedida unidadMedida, Double stock) {
        this.nombre = nombre;
        this.unidadMedida = unidadMedida;
        this.stock = stock;
    }

    public String getNombre() {
        return nombre;
    }

    public UnidadMedida getUnidadMedida() {
        return unidadMedida;
    }

    public Double getStock() {
        return stock;
    }

    public String getImagen() {
        return imagen;
    }
    
    
}
