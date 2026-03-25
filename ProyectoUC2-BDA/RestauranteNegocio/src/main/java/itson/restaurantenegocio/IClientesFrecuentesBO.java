/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package itson.restaurantenegocio;

import itson.restaurantedominio.ClienteFrecuente;
import itson.restaurantedtos.ClienteFrecuenteActualizadoDTO;
import itson.restaurantedtos.ClienteFrecuenteDTO;
import itson.restaurantedtos.ClienteFrecuenteNuevoDTO;
import java.util.List;

/**
 *
 * @author Andrea Lara, Nahomi Figueroa, Zaira Barajas
 */
public interface IClientesFrecuentesBO {
    
    /**
     * Método que se asegura de validar que la entrada de la busqueda es correcta.
     * 
     * @param busqueda filtro aplicado
     * @return Lista de objetos tipo ClienteFrecuenteDTO
     * @throws NegocioException en caso de que algo falle en el 
     */
    public abstract List<ClienteFrecuenteDTO> buscarClientes(String busqueda) throws NegocioException;
    
    /**
     * Método intermedio entre la presentación y persistencia que verifica los datos recibidos del cliente a registrar 
     * para determinar si estos son válidos o no.
     * 
     * @param clienteNuevo DTO con los datos del cliente nuevo a registrar.
     * @return Objeto tipo ClienteFrecuente con los datos que fueron almacenados 
     * en la base de datos.
     * @throws NegocioException si algún dato es inválido o si surge un error en la capa de persistencia.
     */
    public abstract ClienteFrecuente crearCliente(ClienteFrecuenteNuevoDTO clienteNuevo) throws NegocioException;
    
    /**
     * Método intermedio entre la presentación y persistencia que verifica los datos recibidos del cliente a actualizar 
     * para determinar si estos son válidos o no.
     * 
     * @param clienteActualizado DTO con la información a actualizar del
     * cliente.
     * @return Objeto tipo ClienteFrecuente con los datos que fueron actualizados 
     * en la base de datos.
     * @throws NegocioException si algún dato es inválido o si surge un error en la capa de persistencia.
     */
    public abstract ClienteFrecuente actualizarCliente(ClienteFrecuenteActualizadoDTO clienteActualizado) throws NegocioException;
    
    /**
     * Método con validaciones que evita errores imprevistos al consultar datos del cliente.
     * 
     * @param IdClienteFrecuente Id correspondiente al cliente a consultar .
     * @return numero de visitas asociadas al cliente.
     * @throws NegocioException en caso de que algun dato no sea optimo.
     */
    public abstract Long obtenerVisitas(Long IdClienteFrecuente) throws NegocioException;
    
    /**
     * Método con validaciones que evita errores imprevistos al consultar datos del cliente.
     * 
     * @param IdClienteFrecuente Id correspondiente al cliente a consultar .
     * @return total gastado por el cliente.
     * @throws NegocioException en caso de que algun dato no sea optimo.
     */
    public abstract Double obtenerTotalGastado(Long IdClienteFrecuente) throws NegocioException;
    
    
    /**
     * Método que calcula los puntos de un cliente en especifico (en base a su total gastado).
     * 
     * @param IdClienteFrecuente Id correspondiente al cliente a calcular sus puntos .
     * @return numero de puntos del cliente.
     * @throws NegocioException en caso de que algun dato no sea optimo.
     */
    public abstract int calcularPuntos(Long IdClienteFrecuente) throws NegocioException;
}
