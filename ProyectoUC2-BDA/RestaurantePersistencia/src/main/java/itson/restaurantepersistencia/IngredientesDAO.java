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
        Ingrediente ingrediente = IngredienteNuevoDTOAIngredienteAdapter.adaptar(ingredienteNuevo);
        if (exists(ingrediente)){
            throw new PersistenciaException("El ingrediente ya existe en la base de datos.");
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
                ingrediente.setNombre(ingredienteActualizado.getNombre());
            }
            
            if (ingredienteActualizar.getUnidadMedida() != null) {
                ingrediente.setUnidadMedida(ingredienteActualizado.getUnidadMedida());
            }
            
            if (ingredienteActualizar.getStock() != null){
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
    public Ingrediente desinventariar(Long id, Double cantidad) throws PersistenciaException {
        EntityManager entityManager = ManejadorConexiones.crearEntityManager();
        try {
            if (cantidad == null || cantidad <= 0){
                throw new PersistenciaException("No fue posible desinventariar; cantidad nula o negativa");
            }
            
            Ingrediente ingrediente = entityManager.find(Ingrediente.class, id);
            
            if (ingrediente == null){
                throw new PersistenciaException("No se encontró el ingrediente solicitado.");
            }
            
            if (ingrediente.getStock() < cantidad){
                throw new PersistenciaException("No fue posible desinventariar; "
                        + "el stock actual es menor al requerido.");
            } 
            ingrediente.setStock(ingrediente.getStock()-cantidad);
            
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
     * Método que recibe un DTO de ingrediente y busca de acuerdo a los datos que 
     * no son nulos.
     * @param ingrediente DTO con información completa o parcial que el usuario desea
     * consultar.
     * @return una lista con las coincidencias de ingredientes de la búsqueda del usuario.
     * @throws PersistenciaException si existe un problema al conectar con la base
     * de datos.
     */
    @Override
    public List<Ingrediente> buscar(IngredienteNuevoDTO ingrediente) throws PersistenciaException {
        EntityManager entityManager = ManejadorConexiones.crearEntityManager();
        try {
            
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Ingrediente> consulta = builder.createQuery(Ingrediente.class);
            Root<Ingrediente> ingredientes = consulta.from(Ingrediente.class);
            
            consulta.where(
                builder.or(
                    builder.equal(ingredientes.get("nombre"), ingrediente.getNombre()),
                    builder.equal(ingredientes.get("unidad_medida"), ingrediente.getUnidadMedida()),
                    builder.equal(ingredientes.get("stock_actual"), ingrediente.getStock()),
                    builder.equal(ingredientes.get("imagen"), ingrediente.getImagen())
                )
            );
            
            return entityManager.createQuery(consulta).getResultList();
            
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
            Long coincidencias = entityManager.createQuery(jpql, Long.class)
                       .setParameter("nom", ingrediente.getNombre())
                       .setParameter("unid", ingrediente.getUnidadMedida())
                       .getSingleResult();

            return coincidencias > 0;
            
        } catch (PersistenceException ex){
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("No fue posible consultar los ingredientes.");
        } finally {
            entityManager.close();
        }
    }
}
