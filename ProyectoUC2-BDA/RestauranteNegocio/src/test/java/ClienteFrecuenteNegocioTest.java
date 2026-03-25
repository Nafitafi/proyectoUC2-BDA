/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import itson.restaurantedominio.ClienteFrecuente;
import itson.restaurantedtos.ClienteFrecuenteActualizadoDTO;
import itson.restaurantenegocio.ClientesFrecuentesBO;
import itson.restaurantenegocio.IClientesFrecuentesBO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Zaira
 */
public class ClienteFrecuenteNegocioTest {
    
    public ClienteFrecuenteNegocioTest() {
    }

    @Test
    public void actualizarBOFuncionaOk(){
        IClientesFrecuentesBO bo = new ClientesFrecuentesBO();
        ClienteFrecuenteActualizadoDTO nuevo = new ClienteFrecuenteActualizadoDTO
        (1L, "Monica", null, null, null);
        assertDoesNotThrow(()-> {
        ClienteFrecuente resultado = bo.actualizarCliente(nuevo);
        assertNotNull(resultado.getIdCliente());
        assertEquals(nuevo.getNombre(), resultado.getNombre());
        assertNotNull(resultado.getApellidoP());
        assertNotNull(resultado.getApellidoM());
        assertNotNull(resultado.getCorreo());
    });
    }
}
