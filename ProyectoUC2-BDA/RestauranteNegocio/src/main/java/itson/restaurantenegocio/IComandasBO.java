/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package itson.restaurantenegocio;

import itson.restaurantedominio.Comanda;
import itson.restaurantedominio.Mesa;
import itson.restaurantedtos.ComandaDTO;
import itson.restaurantedtos.DetalleComandaDTO;
import java.time.LocalDate;
import java.util.List;

/**
 * Interfaz que define la lógica de negocio para la gestión de comandas.
 *
 * @author Andrea Lara, Nahomi Figueroa, Zaira Barajas
 */
public interface IComandasBO {

    
       
    /**
     * Método que permite registrar una nueva comanda en el sistema.
     *
     *
     * @param comandaDTO DTO con la información de la comanda a registrar.
     * @return Objeto Comanda con los datos almacenados en la base de datos.
     * @throws NegocioException si no se cumplen las reglas de negocio.
     */
    public abstract Comanda guardar(ComandaDTO comandaDTO) throws NegocioException;

     /**
     * Método que verifica el calculo del total de una comanda.
     *
     *
     * @param detalles lista de detalles de una comanda.
     * @return total calculado de la comanda
     * @throws NegocioException si no se cumplen las reglas de negocio.
     */
    public double calcularTotal(List<DetalleComandaDTO> detalles) throws NegocioException ;
    
    
    /**
     * Método que permite obtener una comanda a partir de su identificador.
     *
     * @param idComanda ID de la comanda a buscar.
     * @return Objeto Comanda encontrado, o null si no existe.
     * @throws NegocioException si ocurre un error en la lógica de negocio.
     */
    public abstract Comanda buscarPorId(Long idComanda) throws NegocioException;

    /**
     * Método que permite obtener las mesas disponibles para abrir una comanda.
     *
     * @return Lista de mesas disponibles.
     * @throws NegocioException si ocurre un error en la lógica de negocio.
     */
    public abstract List<Mesa> obtenerMesasDisponibles() throws NegocioException;

    /**
     * Método que permite obtener las comandas registradas dentro de un rango de
     * fechas.
     *
     * @param inicio Fecha y hora de inicio del rango.
     * @param fin Fecha y hora de fin del rango.
     * @return Lista de comandas dentro del rango especificado.
     * @throws NegocioException si ocurre un error en la lógica de negocio.
     */
    public abstract List<Comanda> buscarPorRangoFechas(LocalDate inicio, LocalDate fin) throws NegocioException;

    /**
     * Método que permite obtener el total de ventas en un rango de fechas.
     *
     * @param inicio Fecha y hora de inicio del rango.
     * @param fin Fecha y hora de fin del rango.
     * @return total acumulado de ventas.
     * @throws NegocioException si ocurre un error en la lógica de negocio.
     */
    public abstract Double obtenerTotalVentasPorRango(LocalDate inicio, LocalDate fin) throws NegocioException;
}
