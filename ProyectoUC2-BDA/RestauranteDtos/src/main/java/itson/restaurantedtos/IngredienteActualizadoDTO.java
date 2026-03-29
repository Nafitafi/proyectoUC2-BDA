/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.restaurantedtos;

/**
 * DTO para un ingrediente ya existente actualizado.
 * @author Zaira Paola Barajas Díaz
 */
public class IngredienteActualizadoDTO {
    private Long idIngrediente;
    private String nombre;
    private UnidadMedida unidadMedida;
    private Double stock;
    private String imagen;

    public IngredienteActualizadoDTO() {
    }

    public IngredienteActualizadoDTO(Long idIngrediente, String nombre, UnidadMedida unidadMedida, Double stock, String imagen) {
        this.idIngrediente = idIngrediente;
        this.nombre = nombre;
        this.unidadMedida = unidadMedida;
        this.stock = stock;
        this.imagen = imagen;
    }

    public IngredienteActualizadoDTO(Long idIngrediente, String nombre, UnidadMedida unidadMedida, Double stock) {
        this.idIngrediente = idIngrediente;
        this.nombre = nombre;
        this.unidadMedida = unidadMedida;
        this.stock = stock;
    }

    public Long getIdIngrediente() {
        return idIngrediente;
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
