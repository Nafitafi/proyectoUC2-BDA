/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package itson.restaurantepersistencia;

import itson.restaurantedominio.ClienteFrecuente;
import itson.restaurantedtos.ClienteFrecuenteActualizadoDTO;
import itson.restaurantedtos.ClienteFrecuenteNuevoDTO;
import java.util.List;

/**
 * Interfaz que define la clase ClientesFrecuentesDAO.
 * @author Andrea Lara, Nahomi Figueroa, Zaira Barajas
 */
public interface IClientesFrecuentesDAO {
    
    /**
     * Método que permite almacenar en la base de datos la información de un cliente frecuente;
     * nombre, apellidos, número de teléfono y correo.
     * @param clienteNuevo DTO con la información del cliente a guardar.
     * @return Objeto tipo ClienteFrecuente con los datos que fueron almacenados en la base de datos.
     * @throws PersistenciaException si hay un problema con la conexión a la base de datos.
     */
    public abstract ClienteFrecuente guardar(ClienteFrecuenteNuevoDTO clienteNuevo) throws PersistenciaException;
    
    /**
     * Método que permite actualizar en la base de datos la información de un cliente frecuente registrado;
     * nombre, apellidos, número de teléfono y correo.
     * @param clienteActualizado DTO con la información a actualizar del cliente.
     * @return Objeto tipo ClienteFrecuente con los datos que fueron actualizados en la base de datos.
     * @throws PersistenciaException si hay un problema con la conexión a la base de datos.
     */
    public abstract ClienteFrecuente actualizar(ClienteFrecuenteActualizadoDTO clienteActualizado) throws PersistenciaException;
    
    /**
     * Método que permite realizar la consulta en la base de datos sobre la información de todos los clientes registrados.
     * @return Lista de objetos tipo ClienteFrecuente con los datos que fueron almacenados en la base de datos.
     * @throws PersistenciaException si hay un problema con la conexión a la base de datos.
     */
    public List<ClienteFrecuente> bucarTodosClientes() throws PersistenciaException;
    
    /**
     * Método que permita la consulta en la base de datos sobre la información de clientes registrados que coincidan con
     * el filtro (ya sea nombre, correo electronico o número) proporcionado en el parámetro.
     * @param filtro Filtro que se solicita aplicar para la consulta.
     * @return 
     * @throws PersistenciaException si hay un problema con la conexión a la base de datos.
     */
    public List<ClienteFrecuente> buscarPorFiltro(String filtro) throws PersistenciaException;
}
