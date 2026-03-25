/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.restaurantepersistencia;

import itson.restaurantedominio.ClienteFrecuente;
import itson.restaurantedominio.EstadoComanda;
import itson.restaurantedtos.ClienteFrecuenteActualizadoDTO;
import itson.restaurantedtos.ClienteFrecuenteNuevoDTO;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

/**
 *
 * @author Andrea Lara, Nahomi Figueroa, Zaira Barajas
 */
public class ClientesFrecuentesDAO implements IClientesFrecuentesDAO {

    private static final Logger LOGGER = Logger.getLogger(ClientesFrecuentesDAO.class.getName());

    /**
     * Método que permite almacenar en la base de datos la información de un
     * cliente frecuente; nombre, apellidos, número de teléfono y correo.
     *
     * @param clienteNuevo DTO con la información del cliente a guardar.
     * @return Objeto tipo ClienteFrecuente con los datos que fueron almacenados
     * en la base de datos.
     * @throws PersistenciaException si hay un problema con la conexión a la
     * base de datos.
     */
    @Override
    public ClienteFrecuente guardar(ClienteFrecuenteNuevoDTO clienteNuevo) throws PersistenciaException {
        ClienteFrecuente cliente = new ClienteFrecuente(clienteNuevo.getNombre(), clienteNuevo.getApellidoP(), clienteNuevo.getApellidoM(), clienteNuevo.getNumeroTelefono());

        if (clienteNuevo.getCorreo() != null) {
            cliente.setCorreo(clienteNuevo.getCorreo());
        }

        try {
            EntityManager entityManager = ManejadorConexiones.crearEntityManager();
            entityManager.getTransaction().begin();
            entityManager.persist(cliente);
            entityManager.getTransaction().commit();

            return cliente;

        } catch (PersistenceException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("No fue posible guardar al cliente.");
        }
    }

    /**
     * Método que permite actualizar en la base de datos la información de un
     * cliente frecuente registrado; nombre, apellidos, número de teléfono y
     * correo.
     *
     * @param clienteActualizado DTO con la información a actualizar del
     * cliente.
     * @return Objeto tipo ClienteFrecuente con los datos que fueron
     * actualizados en la base de datos.
     * @throws PersistenciaException si hay un problema con la conexión a la
     * base de datos.
     */
    @Override
    public ClienteFrecuente actualizar(ClienteFrecuenteActualizadoDTO clienteActualizado) throws PersistenciaException {
        try {
            EntityManager entityManager = ManejadorConexiones.crearEntityManager();
            entityManager.getTransaction().begin();

            ClienteFrecuente clienteRegistrado = entityManager.find(ClienteFrecuente.class, clienteActualizado.getId());
            if (clienteActualizado.getNombre() != null) {
                clienteRegistrado.setNombre(clienteActualizado.getNombre());
            }

            if (clienteActualizado.getApellidoP() != null) {
                clienteRegistrado.setApellidoP(clienteActualizado.getApellidoP());
            }

            if (clienteActualizado.getApellidoM() != null) {
                clienteRegistrado.setApellidoM(clienteActualizado.getApellidoM());
            }

            if (clienteActualizado.getNumeroTelefono() != null) {
                clienteRegistrado.setNumeroTelefono(clienteActualizado.getNumeroTelefono());
            }

            if (clienteActualizado.getCorreo() != null) {
                clienteRegistrado.setCorreo(clienteActualizado.getCorreo());
            }

            entityManager.persist(clienteRegistrado);
            entityManager.getTransaction().commit();

            return clienteRegistrado;

        } catch (PersistenceException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("No fue posible actualizar al cliente.");
        }
    }

    /**
     * Método que permite realizar la consulta en la base de datos sobre la
     * información de todos los clientes registrados.
     *
     * @return Lista de objetos tipo ClienteFrecuente con los datos que fueron
     * almacenados en la base de datos.
     * @throws PersistenciaException si hay un problema con la conexión a la
     * base de datos.
     */
    @Override
    public List<ClienteFrecuente> bucarTodosClientes() throws PersistenciaException {
        EntityManager entityManager = ManejadorConexiones.crearEntityManager();
        try {
            return entityManager.createQuery("SELECT c FROM ClienteFrecuente c", ClienteFrecuente.class).getResultList();
        } catch (PersistenceException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("No fue posible consultar a los clientes.");
        }
    }

    /**
     * Método que permita la consulta en la base de datos sobre la información
     * de clientes registrados que coincidan con el filtro (ya sea nombre,
     * correo electronico o número) proporcionado en el parámetro.
     *
     * @param filtro Filtro que se solicita aplicar para la consulta.
     * @return
     * @throws PersistenciaException si hay un problema con la conexión a la
     * base de datos.
     */
    @Override
    public List<ClienteFrecuente> buscarPorFiltro(String filtro) throws PersistenciaException {
        EntityManager entityManager = ManejadorConexiones.crearEntityManager();
        try {
            String consulta = "SELECT c FROM ClienteFrecuente c "
                    + "WHERE LOWER(c.nombre) LIKE :filtro "
                    + "OR LOWER(c.apellidoP) LIKE :filtro "
                    + "OR LOWER(c.apellidoM) LIKE :filtro "
                    + "OR c.numeroTelefono LIKE :filtro "
                    + "OR LOWER(c.correo) LIKE :filtro";
            TypedQuery<ClienteFrecuente> query = entityManager.createQuery(consulta, ClienteFrecuente.class);
            query.setParameter("filtro", "%" + filtro.toLowerCase() + "%");
            return query.getResultList();
        } catch (PersistenceException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("No fue posible consultar a los clientes.");
        }

    }

    /**
     * Método que permite obtener el número de visitas asociadas a un cliente
     * especifico.
     *
     * @param idClienteFrecuente ID correspondiente al cliente del cual queremos
     * obtener las visitas.
     * @return numero entero de las visitas asociadas al cliente.
     * @throws PersistenciaException si hay un problema al consultar los datos
     * de la base de datos.
     */
    @Override
    public Long obtenerVisitasClienteFrecuente(Long idClienteFrecuente) throws PersistenciaException {
        try {
            EntityManager entityManager = ManejadorConexiones.crearEntityManager();
            
            String consultaJPQL = """
                                    SELECT COUNT(c) FROM Comanda c
                                    WHERE c.cliente.idCliente = :idCliente
                                    AND c.estado = :estado
                                  """; 
            TypedQuery<Long> query = entityManager.createQuery(consultaJPQL, Long.class);
            query.setParameter("idCliente", idClienteFrecuente);
            query.setParameter("estado", EstadoComanda.ENTREGADA);
            return query.getSingleResult();
            
        } catch (PersistenceException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("No se pudo obtener el numero de visitas del cliente : " + idClienteFrecuente);
        }
    }

    /**
     * Método que permite obtener el total que ha gastado en el restaurante un
     * cliente en especifico.
     *
     * @param idClienteFrecuente ID correspondiente al cliente del cual queremos
     * obtener el total gastado.
     * @return total de dinero gastado del cliente.
     * @throws PersistenciaException si hay un problema al consultar los datos
     * de la base de datos.
     */
    @Override
    public Double obtenerTotalGastadoClienteFrecuente(Long idClienteFrecuente) throws PersistenciaException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
