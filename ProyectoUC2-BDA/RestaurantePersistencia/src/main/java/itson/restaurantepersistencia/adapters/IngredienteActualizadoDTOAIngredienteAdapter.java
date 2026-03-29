/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.restaurantepersistencia.adapters;

import itson.restaurantedominio.Ingrediente;
import itson.restaurantedominio.UnidadMedida;
import itson.restaurantedtos.IngredienteActualizadoDTO;

/**
 *
 * @author Zaira
 */
public class IngredienteActualizadoDTOAIngredienteAdapter {
    /**
     * Método estático que convierte un DTO de ingrediente a un objeto Ingrediente.
     * Esto para convertir el enum de unidad de medida correctamente.
     * @param ingredienteActualizado DTO del ingrediente actualizado
     * @return objeto tipo ingrediente integrado con todos los datos actualizados
     */
    public static Ingrediente adaptar(IngredienteActualizadoDTO ingredienteActualizado){
        UnidadMedida unidadMedida;
        switch (ingredienteActualizado.getUnidadMedida()){
            case itson.restaurantedtos.UnidadMedida.GRAMOS:
                unidadMedida = UnidadMedida.GRAMOS;
            case itson.restaurantedtos.UnidadMedida.KILOGRAMOS:
                unidadMedida = UnidadMedida.KILOGRAMOS;
            case itson.restaurantedtos.UnidadMedida.LITROS:
                unidadMedida = UnidadMedida.LITROS;
            case itson.restaurantedtos.UnidadMedida.MILILITROS:
                unidadMedida = UnidadMedida.MILILITROS;
            default:
                unidadMedida = UnidadMedida.PIEZAS;
        }
        
        Ingrediente ingrediente = new Ingrediente(
                ingredienteActualizado.getIdIngrediente(),
                ingredienteActualizado.getNombre(),
                unidadMedida,
                ingredienteActualizado.getStock(),
                ingredienteActualizado.getImagen()
        );
        
        return ingrediente;
    }
}
