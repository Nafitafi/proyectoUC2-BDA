/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.restaurantenegocio.adapters;

import itson.restaurantedominio.ClienteFrecuente;
import itson.restaurantedtos.ClienteFrecuenteDTO;
import itson.restaurantedtos.ClienteFrecuenteNuevoDTO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nafbr
 */
public class ClientesFrecuentesAClientesFrecuentesDTOAdapter {
    
    public static List<ClienteFrecuenteDTO> convertirADTO(List<ClienteFrecuente> entidades) {
    List<ClienteFrecuenteDTO> dtos = new ArrayList<>();
    for (ClienteFrecuente c : entidades) {
        dtos.add(new ClienteFrecuenteDTO(c.getIdCliente(), c.getNombre(), c.getApellidoP(), c.getApellidoM(), c.getNumeroTelefono(), c.getCorreo(), c.getPuntos(), c.getFechaRegistro()));
    }
    return dtos;
}
}
