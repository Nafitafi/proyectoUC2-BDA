/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package itson.restaurantenegocio;

import itson.restaurantedominio.Ingrediente;
import itson.restaurantedtos.IngredienteActualizadoDTO;
import itson.restaurantedtos.IngredienteNuevoDTO;
import java.util.List;

/**
 * Interfaz de Negocio para los Ingredientes.
 * @author Zaira Paola Barajas Díaz
 */
public interface IIngredientesBO {
    
    /**
    * Método que recibe y valida un DTO de ingrediente para después intentar registrarlo
    * como un nuevo ingrediente a la base de datos.
    * @param ingredienteNuevo DTO con la información del ingrediente a validar y agregar.
    * @return un objeto tipo Ingrediente con los datos del ingrediente que se 
    * agregó a la base de datos.
    * @throws NegocioException si el ingrediente no cumple con las validaciones requeridas
    * o si existe un problema al conectar con la base de datos.
    */
    public abstract Ingrediente agregar(IngredienteNuevoDTO ingredienteNuevo) throws NegocioException;
    
    /**
     * Método que busca un ingrediente existente que el usuario desea modificar 
     * y valida para después actualizarlo en la base de datos.
     * @param ingredienteActualizar DTO con la información a actualizar del ingrediente.
     * @return un objeto tipo Ingrediente que contiene los cambios reflejados 
     * en la base de datos.
     * @throws NegocioException si el ingrediente no cumple con las validaciones requeridas
    * o si existe un problema al conectar con la base de datos.
     */
    public abstract Ingrediente modificar(IngredienteActualizadoDTO ingredienteActualizar) throws NegocioException;
    
    /**
     * Método que busca un ingrediente existente que el usuario desea eliminar
     * y lo actualiza en la base de datos.
     * @param id id del ingrediente a eliminar.
     * @return un objeto tipo Ingrediente con los datos del ingrediente eliminado
     * de la base de datos.
     * @throws NegocioException si el ingrediente no existe o si existe un problema 
    * al conectar con la base de datos.
     */
    public abstract Ingrediente eliminar(Long id) throws NegocioException;
    
    /**
     * Método que valida y actualiza el stock de un ingrediente, busca su id y luego le resta
     * una cantidad determinada.
     * @param id id del ingrediente a actualizar.
     * @param cantidad cantidad que desea restarsele al stock actual.
     * @return un objeto tipo Ingrediente con el stock actualizado tal como se
     * reflejó en la base de datos.
     * @throws NegocioException si el stock es insuficiente o si existe un problema 
     * al conectar con la base de datos.
     */
    public abstract Ingrediente gestionarInventario(Long id, Double cantidad, boolean operacion) throws NegocioException;
    
    /**
     * Método que consulta y regresa una lista con todos los ingredientes registrados
     * en la base de datos.
     * @return una lista con todos los ingredientes de la base de datos.
     * @throws NegocioException si existe un problema al conectar con la base
     * de datos.
     */
    public abstract List<Ingrediente> recuperarIngredientes() throws NegocioException;
    
    /**
     * Método que busca un ingrediente por su id.
     * @param id id del ingrediente a buscar.
     * @return el ingrediente con el id del parámetro.
     * @throws NegocioException si el id no existe o si existe un problema al conectar con la base
     * de datos.
     */
    public abstract Ingrediente buscar(Long id) throws NegocioException;
    
    /**
     * Método que toma un objeto ingrediente y lo compara con los ingredientes registrados en
     * la base de datos para determinar si este ya existe.
     * @param ingrediente objeto tipo Ingrediente
     * @return true si el ingrediente ya existe en la base de datos, false en caso contrario.
     * @throws NegocioException si existe un problema al conectar con la base
     * de datos.
     */
    public abstract boolean exists(Ingrediente ingrediente) throws NegocioException;
}
