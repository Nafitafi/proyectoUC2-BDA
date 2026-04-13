/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.restaurantepersistencia;

import itson.restaurantedominio.Mesa;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

/**
 * Implementacion de IMesasDAO
 *
 * @author Carmen Andrea Lara Osuna
 */
public class MesasDAO implements IMesasDAO {

    private static final Logger LOGGER = Logger.getLogger(MesasDAO.class.getName());

     /**
     * Cuenta el número total de mesas registradas en la base de datos.
     *
     * @return número total de mesas
     * @throws PersistenciaException si ocurre un error al acceder a la base de
     * datos
     */
    @Override
    public Long contarMesas() throws PersistenciaException {
        EntityManager em = ManejadorConexiones.crearEntityManager();
        try {
            return em.createQuery("SELECT COUNT(m) FROM Mesa m", Long.class)
                    .getSingleResult();
        } catch (PersistenceException ex) {
            throw new PersistenciaException("Error al contar mesas", ex);
        } finally {
            em.close();
        }
    }

    /**
     * Guarda una nueva mesa en la base de datos.
     *
     * @param mesa objeto Mesa a persistir
     * @throws PersistenciaException si ocurre un error al guardar la mesa
     */
    @Override
    public void guardar(Mesa mesa) throws PersistenciaException {
        EntityManager em = ManejadorConexiones.crearEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(mesa);
            em.getTransaction().commit();
        } catch (PersistenceException ex) {
            throw new PersistenciaException("Error al guardar mesa", ex);
        } finally {
            em.close();
        }
    }

}
