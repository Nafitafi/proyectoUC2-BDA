/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package itson.restaurantepersistencia;

import itson.restaurantedominio.Ingrediente;
import itson.restaurantedtos.IngredienteActualizadoDTO;
import itson.restaurantedtos.IngredienteNuevoDTO;
import java.util.List;

/**
 * Interfaz para la DAO de Ingredientes
 * @author Zaira Paola Barajas Díaz
 */
public interface IIngredientesDAO {
    
    /**
    * Método que recibe un DTO de ingrediente para registrar un nuevo ingrediente a 
    * la base de datos.
    * @param ingredienteNuevo DTO con la información del ingrediente a agregar.
    * @return un objeto tipo Ingrediente con los datos del ingrediente que se 
    * agregó a la base de datos.
    * @throws PersistenciaException si el ingrediente ya existe o si existe un problema 
    * al conectar con la base de datos.
    */
    public abstract Ingrediente agregar(IngredienteNuevoDTO ingredienteNuevo) throws PersistenciaException;
    
    /**
     * Método que busca un ingrediente existente que el usuario desea modificar 
     * y lo actualiza en la base de datos.
     * @param ingredienteActualizar DTO con la información a actualizar del ingrediente.
     * @return un objeto tipo Ingrediente que contiene los cambios reflejados 
     * en la base de datos.
     * @throws PersistenciaException si el ingrediente ya existe o si existe un problema 
    * al conectar con la base de datos.
     */
    public abstract Ingrediente modificar(IngredienteActualizadoDTO ingredienteActualizar) throws PersistenciaException;
    
    /**
     * Método que busca un ingrediente existente que el usuario desea eliminar
     * y lo actualiza en la base de datos.
     * @param id id del ingrediente a eliminar.
     * @return un objeto tipo Ingrediente con los datos del ingrediente eliminado
     * de la base de datos.
     * @throws PersistenciaException si el ingrediente no existe o si existe un problema 
    * al conectar con la base de datos.
     */
    public abstract Ingrediente eliminar(Long id) throws PersistenciaException;
    
    /**
     * Método que actualiza el stock de un ingrediente, busca su id y luego le resta
     * una cantidad determinada.
     * @param id id del ingrediente a actualizar.
     * @param cantidad cantidad que desea restarsele o sumarle al stock actual.
     * @param operacion true para sumar la cantidad, false para restarla
     * @return un objeto tipo Ingrediente con el stock actualizado tal como se
     * reflejó en la base de datos.
     * @throws PersistenciaException si existe un problema al conectar con la base
     * de datos.
     */
    public abstract Ingrediente gestionarInventario(Long id, Double cantidad, boolean operacion) throws PersistenciaException;
    
    /**
     * Método que consulta y regresa una lista con todos los ingredientes registrados
     * en la base de datos.
     * @return una lista con todos los ingredientes de la base de datos.
     * @throws PersistenciaException si existe un problema al conectar con la base
     * de datos.
     */
    public abstract List<Ingrediente> recuperarIngredientes() throws PersistenciaException;
    
    /**
     * Método que busca un ingrediente por su id.
     * @param id id del ingrediente a buscar.
     * @return el ingrediente con el id del parámetro.
     * @throws PersistenciaException si el id no existe o si existe un problema al conectar con la base
     * de datos.
     */
    public abstract Ingrediente buscar(Long id) throws PersistenciaException;
    
    /**
     * Método que toma un objeto ingrediente y lo compara con los ingredientes registrados en
     * la base de datos para determinar si este ya existe.
     * @param ingrediente objeto tipo Ingrediente
     * @return true si el ingrediente ya existe en la base de datos, false en caso contrario.
     * @throws PersistenciaException si existe un problema al conectar con la base
     * de datos.
     */
    public abstract boolean exists(Ingrediente ingrediente) throws PersistenciaException;
    
    
    /**
     * Método que revisa si un ingrediente está siendo utilizado en algun producto.
     * @param id el id del ingrediente a verificar
     * @return true si el ingrediente existe en un producto, false en caso contrario.
     * @throws PersistenciaException si existe un problema al conectar con la base
     * de datos.
     */
    public abstract boolean existsEnProducto(Long id) throws PersistenciaException;
}
