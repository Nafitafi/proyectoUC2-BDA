/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.restaurantenegocio;

import itson.restaurantedominio.Ingrediente;
import itson.restaurantedtos.IngredienteActualizadoDTO;
import itson.restaurantedtos.IngredienteNuevoDTO;
import itson.restaurantepersistencia.IIngredientesDAO;
import itson.restaurantepersistencia.IngredientesDAO;
import itson.restaurantepersistencia.PersistenciaException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase de negocio que valida la información de la clase Ingrediente.
 * @author Zaira Paola Barajas Díaz
 */
public class IngredientesBO implements IIngredientesBO {
    private IIngredientesDAO ingredientesDAO = new IngredientesDAO();
    private static final Logger LOGGER = Logger.getLogger(IngredientesBO.class.getName());

    /**
    * Método que recibe y valida un DTO de ingrediente para después intentar registrarlo
    * como un nuevo ingrediente a la base de datos.
    * @param ingredienteNuevo DTO con la información del ingrediente a validar y agregar.
    * @return un objeto tipo Ingrediente con los datos del ingrediente que se 
    * agregó a la base de datos.
    * @throws NegocioException si el ingrediente no cumple con las validaciones requeridas
    * o si existe un problema al conectar con la base de datos.
    */
    @Override
    public Ingrediente agregar(IngredienteNuevoDTO ingredienteNuevo) throws NegocioException {
        if (ingredienteNuevo.getNombre() == null) {
            throw new NegocioException("El nombre no puede estar vacío.");
        } else if (ingredienteNuevo.getNombre().length()>100) {
            throw new NegocioException("El nombre no puede tener más de 100 caracteres.");
        } 
        
        if (ingredienteNuevo.getUnidadMedida() == null){
            throw new NegocioException("La unidad de medida no puede estar vacía.");
        }
        
        if (ingredienteNuevo.getStock() == null){
            throw new NegocioException("El stock no puede estar vacío.");
        } else if (ingredienteNuevo.getStock() < 0) {
            throw new NegocioException("El stock no puede ser negativo.");
        }
       
        
        try {
            Ingrediente ingrediente = ingredientesDAO.agregar(ingredienteNuevo);
            return ingrediente;
            
        } catch (PersistenciaException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NegocioException("No fue posible registrar el ingrediente.");
        }
    }

    /**
     * Método que busca un ingrediente existente que el usuario desea modificar 
     * y valida para después actualizarlo en la base de datos.
     * @param ingredienteActualizar DTO con la información a actualizar del ingrediente.
     * @return un objeto tipo Ingrediente que contiene los cambios reflejados 
     * en la base de datos.
     * @throws NegocioException si el ingrediente no cumple con las validaciones requeridas
    * o si existe un problema al conectar con la base de datos.
     */
    @Override
    public Ingrediente modificar(IngredienteActualizadoDTO ingredienteActualizar) throws NegocioException {
        if (ingredienteActualizar.getNombre() != null) {
            if (ingredienteActualizar.getNombre().length()>100) {
                throw new NegocioException("El nombre no puede tener más de 100 caracteres.");
            }
        } 
        
        if (ingredienteActualizar.getUnidadMedida() == null){
            throw new NegocioException("La unidad de medida no puede estar vacía.");
        }
        
        if (ingredienteActualizar.getStock() != null){
            if (ingredienteActualizar.getStock() < 0) {
                throw new NegocioException("El stock no puede ser negativo.");
            }
        }
        
        try {
            Ingrediente ingrediente = ingredientesDAO.modificar(ingredienteActualizar);
            return ingrediente;
            
        } catch (PersistenciaException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NegocioException("No fue posible modificar el ingrediente.");
        }
    }

    /**
     * Método que busca un ingrediente existente que el usuario desea eliminar
     * y lo actualiza en la base de datos.
     * @param id id del ingrediente a eliminar.
     * @return un objeto tipo Ingrediente con los datos del ingrediente eliminado
     * de la base de datos.
     * @throws NegocioException si el ingrediente no existe o si existe un problema 
    * al conectar con la base de datos.
     */
    @Override
    public Ingrediente eliminar(Long id) throws NegocioException {
        try {
            if (existsEnProducto(id)){
                throw new NegocioException("No es posible eliminar un ingrediente que es parte de una receta.");
            }
            return ingredientesDAO.eliminar(id);
            
        } catch (PersistenciaException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NegocioException("No fue posible consultar los ingredientes.");
        }
    }

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
    @Override
    public Ingrediente gestionarInventario(Long id, Double cantidad, boolean operacion) throws NegocioException {
        if (cantidad<0) {
                throw new NegocioException("No es posible gestionar el stock con una cantidad negativa.");
            } else if (cantidad == 0) {
                throw new NegocioException("No es posible estionar el stock con una cantidad de 0.");
            }
        
        try {
            return ingredientesDAO.gestionarInventario(id, cantidad, operacion);
            
        } catch (PersistenciaException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NegocioException("No fue posible desinventariar los ingredientes.");
        }
    }

    /**
     * Método que consulta y regresa una lista con todos los ingredientes registrados
     * en la base de datos.
     * @return una lista con todos los ingredientes de la base de datos.
     * @throws NegocioException si existe un problema al conectar con la base
     * de datos.
     */
    @Override
    public List<Ingrediente> recuperarIngredientes() throws NegocioException {
        try {
            return ingredientesDAO.recuperarIngredientes();
        } catch (PersistenciaException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NegocioException("No fue posible consultar los ingredientes.");
        }
    }

    /**
     * Método que busca un ingrediente por su id.
     * @param id id del ingrediente a buscar.
     * @return el ingrediente con el id del parámetro.
     * @throws NegocioException si el id no existe o si existe un problema al conectar con la base
     * de datos.
     */
    @Override
    public Ingrediente buscar(Long id) throws NegocioException {
        try {
            return ingredientesDAO.buscar(id);
        } catch (PersistenciaException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NegocioException("No fue posible consultar los ingredientes.");
        }
    }

    /**
     * Método que toma un objeto ingrediente y lo compara con los ingredientes registrados en
     * la base de datos para determinar si este ya existe.
     * @param ingrediente objeto tipo Ingrediente
     * @return true si el ingrediente ya existe en la base de datos, false en caso contrario.
     * @throws NegocioException si existe un problema al conectar con la base
     * de datos.
     */
    @Override
    public boolean exists(Ingrediente ingrediente) throws NegocioException {
        if (ingrediente == null) {
            throw new NegocioException("Ingrediente nulo");
        }
        
        try {
            return ingredientesDAO.exists(ingrediente);
        } catch (PersistenciaException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NegocioException("No fue posible consultar los ingredientes.");
        }
    }

    /**
     * Método que revisa si un ingrediente está siendo utilizado en algun producto.
     * @param id el id del ingrediente a verificar
     * @return true si el ingrediente existe en un producto, false en caso contrario.
     * @throws NegocioException si existe un problema al conectar con la base
     * de datos.
     */
    @Override
    public boolean existsEnProducto(Long id) throws NegocioException {
        try {
            return ingredientesDAO.existsEnProducto(id);
        } catch (PersistenciaException ex) {
            throw new NegocioException("No fue posible consultar los ingredientes.");
        }
    }
    
}
