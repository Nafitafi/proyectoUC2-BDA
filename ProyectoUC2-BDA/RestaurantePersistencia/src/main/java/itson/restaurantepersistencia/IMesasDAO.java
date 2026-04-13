/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package itson.restaurantepersistencia;

import itson.restaurantedominio.Mesa;
import java.util.List;

/**
 * Interfaz que define la clase MesasDAO.
 *
 * Este DAO se encarga de gestionar la persistencia de las mesas
 *
 * @author Andrea Lara, Nahomi Figueroa, Zaira Barajas
 */
public interface IMesasDAO {

    /**
     * Cuenta el número total de mesas registradas en la base de datos.
     *
     * @return número total de mesas
     * @throws PersistenciaException si ocurre un error al acceder a la base de
     * datos
     */
    Long contarMesas() throws PersistenciaException;

    /**
     * Guarda una nueva mesa en la base de datos.
     *
     * @param mesa objeto Mesa a persistir
     * @throws PersistenciaException si ocurre un error al guardar la mesa
     */
    void guardar(Mesa mesa) throws PersistenciaException;

    
}


