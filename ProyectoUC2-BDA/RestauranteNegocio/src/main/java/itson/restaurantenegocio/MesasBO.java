/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.restaurantenegocio;

import itson.restaurantedominio.Mesa;
import itson.restaurantepersistencia.IMesasDAO;
import itson.restaurantepersistencia.MesasDAO;
import itson.restaurantepersistencia.PersistenciaException;

/**
 *
 * @author Carmen Andrea Lara Osuna
 */
public class MesasBO implements IMesasBO {

    private final MesasDAO mesasDAO;

    public MesasBO() {
        this.mesasDAO = new MesasDAO();
    }

    /**
     * Inicializa las mesas en el sistema. Si no existen registros, se crean
     * automáticamente 20 mesas disponibles.
     *
     * @throws PersistenciaException si ocurre un error al acceder a la base de
     * datos
     */
    @Override
    public void inicializarMesas() throws PersistenciaException {
        Long totalMesas = mesasDAO.contarMesas();

        if (totalMesas == 0) {
            for (int i = 1; i <= 20; i++) {
                Mesa mesa = new Mesa();
                mesa.setNumero(i);
                mesasDAO.guardar(mesa);
            }
        }
    }

}
