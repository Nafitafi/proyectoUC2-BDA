/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package itson.restaurantepersistencia;

import itson.restaurantedominio.ClienteFrecuente;
import itson.restaurantedominio.Comanda;
import itson.restaurantedominio.DetalleComanda;
import itson.restaurantedominio.Mesa;
import itson.restaurantedtos.ComandaDTO;
import itson.restaurantedtos.DetalleComandaDTO;
import itson.restaurantepersistencia.adapters.EstadoComandaDTOAEstadoComandaAdapter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

/**
 * Clase que implementa la interfaz IComandasDAO.
 *
 * Esta clase se encarga de gestionar la persistencia de las comandas en la base
 * de datos, permitiendo su registro y consulta según los requerimientos del sistema.
 *
 * @author Andrea Lara, Nahomi Figueroa, Zaira Barajas
 */
public class ComandasDAO implements IComandasDAO {

    private static final Logger LOGGER = Logger.getLogger(ComandasDAO.class.getName());

    /**
     * Método que permite almacenar en la base de datos la información de una
     * comanda, incluyendo mesa, cliente (si aplica), fecha, estado, total y
     * detalles de los productos solicitados.
     *
     * @param comandaDTO DTO con la información de la comanda a guardar.
     * @return Objeto tipo Comanda con los datos que fueron almacenados en la
     * base de datos.
     * @throws PersistenciaException si hay un problema con la conexión a la
     * base de datos.
     */
    @Override
    public Comanda guardar(ComandaDTO comandaDTO) throws PersistenciaException {
        try {
            EntityManager entityManager = ManejadorConexiones.crearEntityManager();
            entityManager.getTransaction().begin();

            Comanda comanda = new Comanda();
            comanda.setFechaHora(LocalDateTime.now());
            comanda.setEstado(EstadoComandaDTOAEstadoComandaAdapter.adaptar(comandaDTO.getEstado()));
            comanda.setFolio(comandaDTO.getFolio());
            comanda.setTotalAcumulado(comandaDTO.getTotal());

            Mesa mesa = entityManager.createQuery(
                    "SELECT m FROM Mesa m WHERE m.numero = :numero", Mesa.class)
                    .setParameter("numero", comandaDTO.getNumeroMesa())
                    .getSingleResult();

            comanda.setMesa(mesa);

            if (comandaDTO.getIdCliente() != null) {
                ClienteFrecuente cliente = entityManager.find(ClienteFrecuente.class, comandaDTO.getIdCliente());
                comanda.setCliente(cliente);
            }

            List<DetalleComanda> detalles = new LinkedList<>();

            for (DetalleComandaDTO d : comandaDTO.getDetalles()) {

                DetalleComanda detalle = new DetalleComanda();

                Producto producto = entityManager.find(Producto.class, d.getIdProducto());

                detalle.setProducto(producto);
                detalle.setCantidad(d.getCantidad());
                detalle.setComentario(d.getComentario());
                detalle.setComanda(comanda);

                detalles.add(detalle);
            }

            comanda.setDetalles(detalles);

            entityManager.persist(comanda);

            entityManager.getTransaction().commit();
            entityManager.close();

            return comanda;

        } catch (PersistenceException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("No fue posible guardar la comanda.");
        }
    }

    /**
     * Método que permite obtener una comanda a partir de su identificador único.
     *
     * @param idComanda ID correspondiente a la comanda que se desea buscar.
     * @return Objeto Comanda encontrado en la base de datos, o null si no existe.
     * @throws PersistenciaException si hay un problema al consultar los datos
     * de la base de datos.
     */
    @Override
    public Comanda buscarPorId(Long idComanda) throws PersistenciaException {
        try {
            EntityManager em = ManejadorConexiones.crearEntityManager();
            return em.find(Comanda.class, idComanda);
        } catch (PersistenceException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("No se pudo buscar la comanda.");
        }
    }

    /**
     * Método que permite verificar si existe una comanda activa (estado ABIERTA)
     * asociada a una mesa específica.
     *
     * @param numeroMesa Número de la mesa que se desea verificar.
     * @return true si la mesa tiene una comanda activa; false en caso contrario.
     * @throws PersistenciaException si hay un problema al consultar los datos
     * de la base de datos.
     */
    @Override
    public boolean existeComandaActivaPorMesa(int numeroMesa) throws PersistenciaException {
        try {
            EntityManager em = ManejadorConexiones.crearEntityManager();

            String consultaJPQL = """
                SELECT COUNT(c) FROM Comanda c
                WHERE c.mesa.numero = :numeroMesa
                AND c.estado = 'ABIERTA'
            """;

            Long count = em.createQuery(consultaJPQL, Long.class)
                    .setParameter("numeroMesa", numeroMesa)
                    .getSingleResult();

            return count > 0;

        } catch (PersistenceException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("Error al verificar la mesa.");
        }
    }

    /**
     * Método que permite obtener una lista de mesas disponibles, es decir,
     * aquellas que no tienen una comanda activa asociada.
     *
     * @return Lista de objetos Mesa con las mesas disponibles para abrir una
     * comanda.
     * @throws PersistenciaException si hay un problema al consultar los datos
     * de la base de datos.
     */
    @Override
    public List<Mesa> obtenerMesasDisponibles() throws PersistenciaException {
        try {
            EntityManager em = ManejadorConexiones.crearEntityManager();

            String consultaJPQL = """
                SELECT m FROM Mesa m
                WHERE m.numero NOT IN (
                    SELECT c.mesa.numero FROM Comanda c
                    WHERE c.estado = 'ABIERTA'
                )
            """;

            return em.createQuery(consultaJPQL, Mesa.class).getResultList();

        } catch (PersistenceException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("No se pudieron obtener las mesas disponibles.");
        }
    }

    /**
     * Método que permite contar la cantidad de comandas registradas en una fecha
     * específica. Este método es utilizado para generar el consecutivo del folio.
     *
     * @param fecha Fecha de la cual se desea obtener el conteo de comandas.
     * @return número entero de comandas registradas en la fecha indicada.
     * @throws PersistenciaException si hay un problema al consultar los datos
     * de la base de datos.
     */
    @Override
    public int contarComandasPorFecha(LocalDate fecha) throws PersistenciaException {
        try {
            EntityManager em = ManejadorConexiones.crearEntityManager();

            LocalDateTime inicio = fecha.atStartOfDay();
            LocalDateTime fin = fecha.plusDays(1).atStartOfDay();

            String consultaJPQL = """
                SELECT COUNT(c) FROM Comanda c
                WHERE c.fechaHora >= :inicio
                AND c.fechaHora < :fin
            """;

            Long count = em.createQuery(consultaJPQL, Long.class)
                    .setParameter("inicio", inicio)
                    .setParameter("fin", fin)
                    .getSingleResult();

            return count.intValue();

        } catch (PersistenceException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("No se pudo contar las comandas.");
        }
    }

    /**
     * Método que permite consultar las comandas registradas dentro de un rango
     * de fechas determinado.
     *
     * @param inicio Fecha y hora de inicio del rango.
     * @param fin Fecha y hora de fin del rango.
     * @return Lista de objetos tipo Comanda dentro del rango especificado.
     * @throws PersistenciaException si hay un problema al consultar los datos
     * de la base de datos.
     */
    @Override
    public List<Comanda> buscarPorRangoFechas(LocalDate inicio, LocalDate fin) throws PersistenciaException {
        try {
            EntityManager em = ManejadorConexiones.crearEntityManager();

            String consultaJPQL = """
                SELECT c FROM Comanda c
                WHERE c.fechaHora BETWEEN :inicio AND :fin
            """;

            return em.createQuery(consultaJPQL, Comanda.class)
                    .setParameter("inicio", inicio)
                    .setParameter("fin", fin)
                    .getResultList();

        } catch (PersistenceException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("No se pudieron obtener las comandas.");
        }
    }

    /**
     * Método que permite obtener el total acumulado de ventas en un rango de
     * fechas específico, considerando únicamente las comandas entregadas.
     *
     * @param inicio Fecha y hora de inicio del rango.
     * @param fin Fecha y hora de fin del rango.
     * @return total de ventas en el periodo indicado.
     * @throws PersistenciaException si hay un problema al consultar los datos
     * de la base de datos.
     */
    @Override
    public Double obtenerTotalVentasPorRango(LocalDateTime inicio, LocalDateTime fin) throws PersistenciaException {
        try {
            EntityManager em = ManejadorConexiones.crearEntityManager();

            String consultaJPQL = """
                SELECT SUM(c.total) FROM Comanda c
                WHERE c.fechaHora BETWEEN :inicio AND :fin
                AND c.estado = 'ENTREGADA'
            """;

            Double total = em.createQuery(consultaJPQL, Double.class)
                    .setParameter("inicio", inicio)
                    .setParameter("fin", fin)
                    .getSingleResult();

            return total != null ? total : 0.0;

        } catch (PersistenceException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("No se pudo calcular el total de ventas.");
        }
    }

     /**
     * Método que permite obtener las comandas necesarias para la generación de
     * reportes, incluyendo información de mesa y cliente.
     *
     * @param inicio Fecha y hora de inicio del rango.
     * @param fin Fecha y hora de fin del rango.
     * @return Lista de objetos tipo Comanda con la información necesaria para
     * reportes.
     * @throws PersistenciaException si hay un problema al consultar los datos
     * de la base de datos.
     */
    @Override
    public List<Comanda> obtenerComandasParaReporte(LocalDate inicio, LocalDate fin) throws PersistenciaException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}