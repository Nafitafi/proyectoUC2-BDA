/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package itson.restaurantepersistencia.adapters;

import itson.restaurantedominio.EstadoComanda;

/**
 *Adaptador del Enum EstadoComanda de DTOs a EstadoComanda de dominio
 * 
 * @author Andrea Lara, Nahomi Figueroa, Zaira Barajas
 */
public class EstadoComandaDTOAEstadoComandaAdapter {

    public static EstadoComanda adaptar(itson.restaurantedtos.EstadoComanda estadoDTO) {

        EstadoComanda estado = EstadoComanda.ABIERTA; 

        switch (estadoDTO) {
            case ABIERTA:
                estado = EstadoComanda.ABIERTA;
                break;
            case ENTREGADA:
                estado = EstadoComanda.ENTREGADA;
                break;
            case CANCELADA:
                estado = EstadoComanda.CANCELADA;
                break;
            default:
                break;
        }

        return estado;
    }
}
   
    

