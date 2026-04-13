/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package itson.restaurantenegocio;

import itson.restaurantepersistencia.PersistenciaException;

/**
 * Interfaz que define la clase MesasBO.
 *
 * Se encarga de manejar los objetos de negocio mesas
 *
 * @author Andrea Lara, Nahomi Figueroa, Zaira Barajas
 */
public interface IMesasBO {
    
    /**
     * Inicializa las mesas en el sistema.
     * Si no existen registros, se crean automáticamente 20 mesas .
     *
     * @throws PersistenciaException si ocurre un error al guardar en la BD.
     */
    void inicializarMesas() throws PersistenciaException;
}
