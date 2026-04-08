/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.restaurantepersistencia;

import itson.restaurantedominio.Ingrediente;
import itson.restaurantedtos.IngredienteActualizadoDTO;
import itson.restaurantedtos.IngredienteNuevoDTO;
import itson.restaurantepersistencia.adapters.IngredienteActualizadoDTOAIngredienteAdapter;
import itson.restaurantepersistencia.adapters.IngredienteNuevoDTOAIngredienteAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Clase DAO para los ingredientes.
 * @author Zaira Paola Barajas Díaz
 */
public class IngredientesDAO implements IIngredientesDAO{

    private static final Logger LOGGER = Logger.getLogger(IngredientesDAO.class.getName());
    
    /**
    * Método que recibe un DTO de ingrediente para registrar un nuevo ingrediente a 
    * la base de datos.
    * @param ingredienteNuevo DTO con la información del ingrediente a agregar.
    * @return un objeto tipo Ingrediente con los datos del ingrediente que se 
    * agregó a la base de datos.
    * @throws PersistenciaException si el ingrediente ya existe o si existe un problema 
    * al conectar con la base de datos.
    */
    @Override
    public Ingrediente agregar(IngredienteNuevoDTO ingredienteNuevo) throws PersistenciaException {
        if (ingredienteNuevo == null){
            throw new PersistenciaException("Ingrediente nulo.");
        }
        
        if (ingredienteNuevo.getUnidadMedida() == null){
            throw new PersistenciaException("La unidad de medida no puede estar vacía.");
        }
        
        Ingrediente ingrediente = IngredienteNuevoDTOAIngredienteAdapter.adaptar(ingredienteNuevo);
        
        if (exists(ingrediente)){
            throw new PersistenciaException("El ingrediente ya existe en la base de datos.");
        }
        
        if (ingredienteNuevo.getNombre() == null) {
            throw new PersistenciaException("El nombre no puede estar vacío.");
        } else if (ingredienteNuevo.getNombre().length()>100) {
            throw new PersistenciaException("El nombre no puede tener más de 100 caracteres.");
        } 
       
        if (ingredienteNuevo.getStock() == null){
            throw new PersistenciaException("El stock no puede estar vacío.");
        } else if (ingredienteNuevo.getStock() < 0) {
            throw new PersistenciaException("El stock no puede ser negativo.");
        }
        
        if (ingredienteNuevo.getImagen() != null){
            if (ingredienteNuevo.getImagen().length() > 255) {
                throw new PersistenciaException("El URL de imagen excede los 255 caracteres.");
            }
        }
        
        EntityManager entityManager = ManejadorConexiones.crearEntityManager();

        try {
            
            entityManager.getTransaction().begin();
            entityManager.persist(ingrediente);
            entityManager.getTransaction().commit();
            
            return ingrediente;
            
        } catch (PersistenceException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("No fue posible registrar el ingrediente.");
        } finally {
            entityManager.close();
        }
    }

    /**
     * Método que busca un ingrediente existente que el usuario desea modificar 
     * y lo actualiza en la base de datos.
     * @param ingredienteActualizar DTO con la información a actualizar del ingrediente.
     * @return un objeto tipo Ingrediente que contiene los cambios reflejados 
     * en la base de datos.
     * @throws PersistenciaException  si el ingrediente ya existe o si existe un problema 
     * al conectar con la base de datos.
     */
    @Override
    public Ingrediente modificar(IngredienteActualizadoDTO ingredienteActualizar) throws PersistenciaException {
        EntityManager entityManager = ManejadorConexiones.crearEntityManager();
        try {
            
            Ingrediente ingredienteActualizado = IngredienteActualizadoDTOAIngredienteAdapter.adaptar(ingredienteActualizar);
            Ingrediente ingrediente = entityManager.find(Ingrediente.class, ingredienteActualizado.getIdIngrediente());
            
            if (exists(ingredienteActualizado)){
                throw new PersistenciaException("El ingrediente ya existe en la base de datos.");
            }
            
            if (ingredienteActualizar.getNombre() != null) {
                if (ingredienteActualizar.getNombre().length()>100) {
                    throw new PersistenciaException("El nombre no puede tener más de 100 caracteres.");
                }
                ingrediente.setNombre(ingredienteActualizado.getNombre());
            }
            
            if (ingredienteActualizar.getUnidadMedida() != null) {
                ingrediente.setUnidadMedida(ingredienteActualizado.getUnidadMedida());
            } else {
                throw new PersistenciaException("La unidad de medida no puede estar vacía.");
            }
            
            if (ingredienteActualizar.getStock() != null){
                if (ingredienteActualizar.getStock() < 0) {
                    throw new PersistenciaException("El stock no puede ser negativo.");
                }
                ingrediente.setStock(ingredienteActualizado.getStock());
            }
            
            if (ingredienteActualizar.getImagen() != null) {
                ingrediente.setImagen(ingredienteActualizado.getImagen());
            }
            
            entityManager.getTransaction().begin();
            entityManager.persist(ingrediente);
            entityManager.getTransaction().commit();

            return ingrediente;
            
        } catch (PersistenceException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("No fue posible modificar el ingrediente.");
        } finally {
            entityManager.close();
        }
    }

    /**
     * Método que busca un ingrediente existente que el usuario desea eliminar
     * y lo actualiza en la base de datos.
     * @param id id del ingrediente a eliminar.
     * @return un objeto tipo Ingrediente con los datos del ingrediente eliminado
     * de la base de datos.
     * @throws PersistenciaException si el ingrediente no existe o si existe un problema 
    * al conectar con la base de datos.
     */
    @Override
    public Ingrediente eliminar(Long id) throws PersistenciaException {
        EntityManager entityManager = ManejadorConexiones.crearEntityManager();
        try {
            Ingrediente ingrediente = entityManager.find(Ingrediente.class, id);
            
            if(existsEnProducto(id)){
                throw new PersistenciaException("No es posible eliminar un ingrediente que es parte de una receta.");
            }
            
            if (ingrediente == null){
                throw new PersistenciaException("No se encontró el ingrediente solicitado.");
            }
            
            if (!exists(ingrediente)){
                throw new PersistenciaException("El ingrediente no existe en la base de datos.");
            }
            
            entityManager.getTransaction().begin();
            entityManager.remove(ingrediente);
            entityManager.getTransaction().commit();
            
            return ingrediente;
        } catch (PersistenceException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("No fue posible eliminar el ingrediente.");
        } finally {
            entityManager.close();
        }
    }

    /**
     * Método que actualiza el stock de un ingrediente, busca su id y luego le resta
     * una cantidad determinada.
     * @param id id del ingrediente a actualizar.
     * @param cantidad cantidad que desea restarsele al stock actual.
     * @return un objeto tipo Ingrediente con el stock actualizado tal como se
     * reflejó en la base de datos.
     * @throws PersistenciaException si existe un problema al conectar con la base
     * de datos.
     */
    @Override
    public Ingrediente gestionarInventario(Long id, Double cantidad, boolean operacion) throws PersistenciaException {
        EntityManager entityManager = ManejadorConexiones.crearEntityManager();
        try {
            if (cantidad == null || cantidad <= 0){
                    throw new PersistenciaException("No es posible gestionar el stock con una cantidad nula o negativa");
            }
            
            Ingrediente ingrediente = entityManager.find(Ingrediente.class, id);
            
            if (ingrediente == null){
                    throw new PersistenciaException("No se encontró el ingrediente solicitado.");
                }
            
            if (operacion){
                ingrediente.setStock(ingrediente.getStock()+cantidad);
                
            } else {

                if (ingrediente.getStock() < cantidad){
                    throw new PersistenciaException("No fue posible desinventariar; "
                            + "el stock actual es menor al requerido.");
                } 
                ingrediente.setStock(ingrediente.getStock()-cantidad);
            }
            
            entityManager.getTransaction().begin();
            entityManager.persist(ingrediente);
            entityManager.getTransaction().commit();
            
            return ingrediente;
            
        } catch (PersistenceException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("No fue posible actualizar el stock del ingrediente.");
        } finally {
            entityManager.close();
        }
    }

    /**
     * Método que consulta y regresa una lista con todos los ingredientes registrados
     * en la base de datos.
     * @return una lista con todos los ingredientes de la base de datos.
     * @throws PersistenciaException si existe un problema al conectar con la base
     * de datos.
     */
    @Override
    public List<Ingrediente> recuperarIngredientes() throws PersistenciaException {
        List<Ingrediente> lista = new ArrayList();
        EntityManager entityManager = ManejadorConexiones.crearEntityManager();
        try {
            lista = entityManager.createQuery
                    ("SELECT i FROM Ingrediente i", Ingrediente.class)
                    .getResultList();
            
            return lista;
        } catch (PersistenceException ex){
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("No fue posible consultar los ingredientes.");
        } finally {
            entityManager.close();
        }
            
    }

    /**
     * Método que busca un ingrediente por su id.
     * @param id id del ingrediente a buscar.
     * @return el ingrediente con el id del parámetro.
     * @throws PersistenciaException si el id no existe o si existe un problema al conectar con la base
     * de datos.
     */
    @Override
    public Ingrediente buscar(Long id) throws PersistenciaException {
        EntityManager entityManager = ManejadorConexiones.crearEntityManager();
        try {
            
            Ingrediente ingrediente = entityManager.find(Ingrediente.class, id);

            if (ingrediente != null){
                return ingrediente;
            } else {
                throw new PersistenciaException("No se encontró un ingrediente con el id proporcionado.");
            }
        } catch (PersistenceException ex){
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("No fue posible consultar los ingredientes.");
        } finally {
            entityManager.close();
        }
    }

    /**
     * Método que toma un objeto ingrediente y lo compara con los ingredientes registrados en
     * la base de datos para determinar si este ya existe.
     * @param ingrediente objeto tipo Ingrediente
     * @return true si el ingrediente ya existe en la base de datos, false en caso contrario.
     * @throws PersistenciaException si existe un problema al conectar con la base
     * de datos.
     */
    @Override
    public boolean exists(Ingrediente ingrediente) throws PersistenciaException {
        EntityManager entityManager = ManejadorConexiones.crearEntityManager();
        try {
            
            String jpql = "SELECT COUNT(i) FROM Ingrediente i WHERE LOWER(i.nombre) = LOWER(:nom) AND i.unidadMedida = :unid";
            
            if (ingrediente.getIdIngrediente() != null) {
                jpql += " AND i.idIngrediente != :id";
            }
            
            TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class)
                .setParameter("nom", ingrediente.getNombre())
                .setParameter("unid", ingrediente.getUnidadMedida());

            if (ingrediente.getIdIngrediente() != null) {
                query.setParameter("id", ingrediente.getIdIngrediente());
            }
            
            return query.getSingleResult() > 0;
        } catch (PersistenceException ex){
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("No fue posible consultar los ingredientes.");
        } finally {
            entityManager.close();
        }
    }

    /**
     * Método que revisa si un ingrediente está siendo utilizado en algun producto.
     * @param id el id del ingrediente a verificar
     * @return true si el ingrediente existe en un producto, false en caso contrario.
     * @throws PersistenciaException si existe un problema al conectar con la base
     * de datos.
     */
    @Override
    public boolean existsEnProducto(Long id) throws PersistenciaException {
        EntityManager entityManager = ManejadorConexiones.crearEntityManager();
        try {
            
            String jpql = "SELECT COUNT(d) FROM DetallesReceta d WHERE d.ingrediente.idIngrediente = :idIngrediente";
            
            TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class)
                .setParameter("idIngrediente", id);

            
            return query.getSingleResult() > 0;
            
        } catch (PersistenceException ex){
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("No fue posible consultar los ingredientes.");
        } finally {
            entityManager.close();
        }
    }
}
