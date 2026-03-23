/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package itson.restaurantepersistencia;

import itson.restaurantedominio.ClienteFrecuente;
import itson.restaurantedtos.ClienteFrecuenteActualizadoDTO;
import itson.restaurantedtos.ClienteFrecuenteDTO;

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
    public abstract ClienteFrecuente guardar(ClienteFrecuenteDTO clienteNuevo) throws PersistenciaException;
    
    /**
     * Método que permite actualizar en la base de datos la información de un cliente frecuente registrado;
     * nombre, apellidos, número de teléfono y correo.
     * @param clienteActualizado DTO con la información a actualizar del cliente.
     * @return Objeto tipo ClienteFrecuente con los datos que fueron actualizados en la base de datos.
     * @throws PersistenciaException si hay un problema con la conexión a la base de datos.
     */
    public abstract ClienteFrecuente actualizar(ClienteFrecuenteActualizadoDTO clienteActualizado) throws PersistenciaException;
}
