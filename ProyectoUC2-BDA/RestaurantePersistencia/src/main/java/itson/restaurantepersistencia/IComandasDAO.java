/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package itson.restaurantepersistencia;

import itson.restaurantedominio.Comanda;
import itson.restaurantedominio.Mesa;
import itson.restaurantedtos.ComandaDTO;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Interfaz que define la clase ComandasDAO.
 *
 * Este DAO se encarga de gestionar la persistencia de las comandas
 *
 * @author Andrea Lara, Nahomi Figueroa, Zaira Barajas
 */
public interface IComandasDAO {

    /**
     * Método que permite almacenar en la base de datos la información de una
     * comanda, incluyendo mesa, cliente (si es de un cliente frecuente), fecha,
     * estado, total y detalles de los productos.
     *
     * @param comandaDTO DTO con la información de la comanda a guardar.
     * @return Objeto tipo Comanda con los datos que fueron almacenados en la
     * base de datos.
     * @throws PersistenciaException si hay un problema con la conexión a la
     * base de datos.
     */
    public abstract Comanda guardar(ComandaDTO comandaDTO) throws PersistenciaException;

    /**
     * Método que permite obtener una comanda a partir de id.
     *
     * @param idComanda Id de la comanda que se desea buscar.
     * @return Objeto Comanda encontrado en la base de datos, null si no existe.
     * @throws PersistenciaException si hay un problema al consultar los datos
     * de la base de datos.
     */
    public abstract Comanda buscarPorId(Long idComanda) throws PersistenciaException;

    /**
     * Método que permite verificar si existe una comanda activa (estado
     * ABIERTA) asociada a una mesa específica.
     *
     * @param numeroMesa Número de la mesa que se desea verificar.
     * @return true si la mesa tiene una comanda activa, false si no.
     * @throws PersistenciaException si hay un problema al consultar los datos
     * de la base de datos.
     */
    public abstract boolean existeComandaActivaPorMesa(int numeroMesa) throws PersistenciaException;

    /**
     * Método que permite obtener una lista con las mesas disponibles.
     *
     * @return Lista de objetos Mesa con las mesas disponibles para abrir una
     * comanda
     * @throws PersistenciaException si hay un problema al consultar los datos
     * de la base de datos.
     */
    public abstract List<Mesa> obtenerMesasDisponibles() throws PersistenciaException;

    /**
     * Método que permite contar la cantidad de comandas registradas en una
     * fecha específica. Metodo para el numero del folio.
     *
     * @param fecha Fecha de la cual se desea obtener el conteo de comandas.
     * @return número entero de comandas registradas en la fecha especifica.
     * @throws PersistenciaException si hay un problema al consultar los datos
     * de la base de datos.
     */
    public abstract int contarComandasPorFecha(LocalDate fecha) throws PersistenciaException;

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
    public abstract List<Comanda> buscarPorRangoFechas(LocalDate inicio, LocalDate fin) throws PersistenciaException;

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
    public abstract List<Comanda> obtenerComandasParaReporte(LocalDateTime inicio, LocalDateTime fin) throws PersistenciaException;

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
    public abstract Double obtenerTotalVentasPorRango(LocalDate inicio, LocalDate fin) throws PersistenciaException;
}
