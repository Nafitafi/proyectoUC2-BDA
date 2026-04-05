/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.restaurantepersistencia.adapters;

import itson.restaurantedominio.Ingrediente;
import itson.restaurantedominio.UnidadMedida;
import itson.restaurantedtos.IngredienteNuevoDTO;

/**
 * Adaptador de IngredienteNuevoDTO a Ingrediente
 * @author Zaira Paola Barajas Díaz
 */
public class IngredienteNuevoDTOAIngredienteAdapter {
    
    /**
     * Método estático que convierte un DTO de ingrediente a un objeto Ingrediente.
     * Esto para convertir el enum de unidad de medida correctamente.
     * @param ingredienteNuevo DTO del ingrediente nuevo
     * @return objeto tipo ingrediente integrado con todos los datos a registrar en
     * la base de datos.
     */
    public static Ingrediente adaptar(IngredienteNuevoDTO ingredienteNuevo){
        UnidadMedida unidadMedida;
        switch (ingredienteNuevo.getUnidadMedida()){
            case itson.restaurantedtos.UnidadMedida.GRAMOS:
                unidadMedida = UnidadMedida.GRAMOS;
                break;
            case itson.restaurantedtos.UnidadMedida.KILOGRAMOS:
                unidadMedida = UnidadMedida.KILOGRAMOS;
                break;
            case itson.restaurantedtos.UnidadMedida.LITROS:
                unidadMedida = UnidadMedida.LITROS;
                break;
            case itson.restaurantedtos.UnidadMedida.MILILITROS:
                unidadMedida = UnidadMedida.MILILITROS;
                break;
            default:
                unidadMedida = UnidadMedida.PIEZAS;
        }
        
        Ingrediente ingrediente = new Ingrediente(
                ingredienteNuevo.getNombre(),
                unidadMedida,
                ingredienteNuevo.getStock(),
                ingredienteNuevo.getImagen()
        );
        
        return ingrediente;
    }
}
