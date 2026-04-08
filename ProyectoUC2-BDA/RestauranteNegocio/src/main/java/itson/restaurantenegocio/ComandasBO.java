/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.restaurantenegocio;

import itson.restaurantedominio.Comanda;
import itson.restaurantedominio.Mesa;
import itson.restaurantedtos.ComandaDTO;
import itson.restaurantedtos.DetalleComandaDTO;
import itson.restaurantedtos.EstadoComanda;
import itson.restaurantepersistencia.IComandasDAO;
import itson.restaurantepersistencia.PersistenciaException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

/**
 * Clase que implementa la lógica de negocio para la gestión de comandas.
 *
 * Esta clase valida las reglas de negocio antes de interactuar con la capa de
 * persistencia.
 *
 * @author Andrea Lara, Nahomi Figueroa, Zaira Barajas
 */
public class ComandasBO implements IComandasBO {

    private IComandasDAO comandasDAO;
    private static final Logger LOGGER = Logger.getLogger(IngredientesBO.class.getName());

    public ComandasBO(IComandasDAO comandasDAO) {
        this.comandasDAO = comandasDAO;
    }

    /**
     * Metodo que genera un folio para una comanda.
     *
     * El formato del folio es: OB-YYYYMMDD-XXX, XXX es un número consecutivo.
     *
     * @return folio generado.
     * @throws PersistenciaException si ocurre un error al consultar el
     * consecutivo.
     */
    private String generarFolio() throws PersistenciaException {

        LocalDate hoy = LocalDate.now();

        int consecutivo = comandasDAO.contarComandasPorFecha(hoy) + 1;

        String fecha = hoy.toString().replace("-", "");

        String folio = "OB-" + fecha + "-" + String.format("%03d", consecutivo);

        return folio;
    }

    @Override
    public double calcularTotal(List<DetalleComandaDTO> detalles) throws NegocioException {
        try {

            if (detalles == null || detalles.isEmpty()) {
                throw new NegocioException("No se puede calcular el total sin productos.");
            }

            for (DetalleComandaDTO detalle : detalles) {
                if (detalle.getCantidad() <= 0) {
                    throw new NegocioException("Cantidad inválida en un producto.");
                }

                if (detalle.getIdProducto() == null) {
                    throw new NegocioException("Producto inválido en detalle.");
                }
            }

            return comandasDAO.calcularTotal(detalles);

        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al calcular el total.", ex);
        }
    }

    /**
     * Método que permite registrar una nueva comanda validando las reglas de
     * negocio.
     *
     * @param comandaDTO DTO con la información de la comanda.
     * @return Comanda registrada.
     * @throws NegocioException si no se cumplen las reglas de negocio.
     */
    @Override
    public Comanda guardar(ComandaDTO comandaDTO) throws NegocioException {
        try {

            if (comandaDTO.getDetalles() == null || comandaDTO.getDetalles().isEmpty()) {
                throw new NegocioException("La comanda debe tener al menos un producto.");
            }

            if (comandasDAO.existeComandaActivaPorMesa(comandaDTO.getIdMesa())) {
                throw new NegocioException("La mesa ya tiene una comanda activa.");
            }

            //validar productos uno por uno
            for (DetalleComandaDTO detalle : comandaDTO.getDetalles()) {

                if (detalle.getCantidad() <= 0) {
                    throw new NegocioException("La cantidad de un producto debe ser mayor a 0.");
                }

                // evitar comantarios muy largo o inutiles
                if (detalle.getComentario() != null && detalle.getComentario().length() > 100) {
                    throw new NegocioException("El comentario es demasiado largo.");
                }
            }

            LocalDateTime hoy = LocalDateTime.now();

            String folio = generarFolio();

            ComandaDTO nuevaComanda = new ComandaDTO(
                    folio,
                    comandaDTO.getIdMesa(),
                    comandaDTO.getIdCliente(),
                    comandaDTO.getDetalles(),
                    hoy,
                    EstadoComanda.ABIERTA,
                    calcularTotal(comandaDTO.getDetalles())
                    
            );

            return comandasDAO.guardar(nuevaComanda);

        } catch (PersistenciaException ex) {
            LOGGER.severe(ex.getMessage());

            throw new NegocioException("Error al guardar la comanda.", ex);
        }
    }

    /**
     * Método que permite buscar una comanda por su ID.
     *
     * @param idComanda ID de la comanda.
     * @return Comanda encontrada o null.
     * @throws NegocioException si ocurre un error.
     */
    @Override
    public Comanda buscarPorId(Long idComanda) throws NegocioException {
        try {
            return comandasDAO.buscarPorId(idComanda);
        } catch (PersistenciaException ex) {
            LOGGER.severe(ex.getMessage());

            throw new NegocioException("Error al buscar la comanda.", ex);
        }
    }

    /**
     * Método que permite obtener las mesas disponibles.
     *
     * @return Lista de mesas disponibles.
     * @throws NegocioException si ocurre un error.
     */
    @Override
    public List<Mesa> obtenerMesasDisponibles() throws NegocioException {
        try {
            return comandasDAO.obtenerMesasDisponibles();
        } catch (PersistenciaException ex) {
            LOGGER.severe(ex.getMessage());

            throw new NegocioException("Error al obtener mesas disponibles.", ex);
        }
    }

    /**
     * Método que permite consultar comandas por rango de fechas.
     *
     * @param inicio Fecha inicial.
     * @param fin Fecha final.
     * @return Lista de comandas.
     * @throws NegocioException si ocurre un error.
     */
    @Override
    public List<Comanda> buscarPorRangoFechas(LocalDate inicio, LocalDate fin) throws NegocioException {
        try {
            if (inicio.isAfter(fin)) {
                throw new NegocioException("El rango de fechas es inválido.");
            }
            return comandasDAO.buscarPorRangoFechas(inicio, fin);
        } catch (PersistenciaException ex) {
            LOGGER.severe(ex.getMessage());

            throw new NegocioException("Error al consultar comandas.", ex);
        }
    }

    /**
     * Método que permite obtener el total de ventas en un rango de fechas.
     *
     * Solo considera comandas entregadas.
     *
     * @param inicio Fecha inicial.
     * @param fin Fecha final.
     * @return total de ventas.
     * @throws NegocioException si ocurre un error.
     */
    @Override
    public Double obtenerTotalVentasPorRango(LocalDate inicio, LocalDate fin) throws NegocioException {
        try {
            if (inicio.isAfter(fin)) {
                throw new NegocioException("El rango de fechas es inválido.");
            }
            return comandasDAO.obtenerTotalVentasPorRango(inicio, fin);
        } catch (PersistenciaException ex) {
            LOGGER.severe(ex.getMessage());

            throw new NegocioException("Error al calcular ventas.", ex);
        }
    }
}
