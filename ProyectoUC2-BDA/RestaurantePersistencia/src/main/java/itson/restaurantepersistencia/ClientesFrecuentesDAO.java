/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.restaurantepersistencia;

import itson.restaurantedominio.ClienteFrecuente;
import itson.restaurantedtos.ClienteFrecuenteActualizadoDTO;
import itson.restaurantedtos.ClienteFrecuenteDTO;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

/**
 *
 * @author Andrea Lara, Nahomi Figueroa, Zaira Barajas
 */
public class ClientesFrecuentesDAO implements IClientesFrecuentesDAO {

    private static final Logger LOGGER = Logger.getLogger(ClientesFrecuentesDAO.class.getName());

    /**
     * Método que permite almacenar en la base de datos la información de un cliente frecuente;
     * nombre, apellidos, número de teléfono y correo.
     * @param clienteNuevo DTO con la información del cliente a guardar.
     * @return Objeto tipo ClienteFrecuente con los datos que fueron almacenados en la base de datos.
     * @throws PersistenciaException si hay un problema con la conexión a la base de datos.
     */
    @Override
    public ClienteFrecuente guardar(ClienteFrecuenteDTO clienteNuevo) throws PersistenciaException {
        ClienteFrecuente cliente = new ClienteFrecuente
       (clienteNuevo.getNombre(), clienteNuevo.getApellidoP(),clienteNuevo.getApellidoM(), clienteNuevo.getNumeroTelefono());
        
        if (clienteNuevo.getCorreo() != null) cliente.setCorreo(clienteNuevo.getCorreo());
        
        try {
            EntityManager entityManager = ManejadorConexiones.crearEntityManager();
            entityManager.getTransaction().begin();
            entityManager.persist(cliente);
            entityManager.getTransaction().commit();
            
            return cliente;
            
        } catch (PersistenceException ex){
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("No fue posible guardar al cliente.");
        }
    }

    /**
     *Método que permite actualizar en la base de datos la información de un cliente frecuente registrado;
     * nombre, apellidos, número de teléfono y correo.
     * @param clienteActualizado DTO con la información a actualizar del cliente.
     * @return Objeto tipo ClienteFrecuente con los datos que fueron actualizados en la base de datos.
     * @throws PersistenciaException si hay un problema con la conexión a la base de datos.
     */
    @Override
    public ClienteFrecuente actualizar(ClienteFrecuenteActualizadoDTO clienteActualizado) throws PersistenciaException {
        try {
            EntityManager entityManager = ManejadorConexiones.crearEntityManager();
            entityManager.getTransaction().begin();
            
            ClienteFrecuente clienteRegistrado = entityManager.find(ClienteFrecuente.class, clienteActualizado.getId());
            clienteRegistrado.setNombre(clienteActualizado.getNombre());
            clienteRegistrado.setApellidoP(clienteActualizado.getApellidoP());
            clienteRegistrado.setApellidoM(clienteActualizado.getApellidoM());
            clienteRegistrado.setNumeroTelefono(clienteActualizado.getNumeroTelefono());
            clienteRegistrado.setCorreo(clienteActualizado.getCorreo());
            
            entityManager.persist(clienteRegistrado);
            entityManager.getTransaction().commit();
            
            return clienteRegistrado;
            
        } catch (PersistenceException ex){
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("No fue posible actualizar al cliente.");
        }
    }


}
