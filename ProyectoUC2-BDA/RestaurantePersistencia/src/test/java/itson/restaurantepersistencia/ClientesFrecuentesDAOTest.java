/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package itson.restaurantepersistencia;

import itson.restaurantedominio.ClienteFrecuente;
import itson.restaurantedtos.ClienteFrecuenteActualizadoDTO;
import itson.restaurantedtos.ClienteFrecuenteNuevoDTO;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

/**
 *
 * @author Andrea Lara, Nahomi Figueroa, Zaira Barajas
 */
public class ClientesFrecuentesDAOTest {
    
    public ClientesFrecuentesDAOTest() {
    }

    @Test
    public void testSomeMethod() {
    }
    
    private ClientesFrecuentesDAO clientesDAO;

    @BeforeEach
    void setUp() {
        clientesDAO = new ClientesFrecuentesDAO();
    }

    @Test
    void guardarClienteTestFunciona() throws PersistenciaException {
        ClienteFrecuenteNuevoDTO nuevo = new ClienteFrecuenteNuevoDTO();
        nuevo.setNombre("Sal");
        nuevo.setApellidoP("Fisher");
        nuevo.setApellidoM("Johnson");
        nuevo.setNumeroTelefono("6441234567");
        nuevo.setCorreo("sallyface@email.com");
        ClienteFrecuente resultado = clientesDAO.guardar(nuevo);
        assertNotNull(resultado.getIdCliente());
        assertEquals("Sal", resultado.getNombre());
    }

    
    @Test
    void buscarPorFiltroTest() throws PersistenciaException {
        ClienteFrecuenteNuevoDTO c1 = new ClienteFrecuenteNuevoDTO("Carlos", "Slim", "X", "555123", "c@slim.com");
        clientesDAO.guardar(c1);

        List<ClienteFrecuente> listaPorNombre = clientesDAO.buscarPorFiltro("carlos");
        List<ClienteFrecuente> listaPorTelefono = clientesDAO.buscarPorFiltro("555");
        assertFalse(listaPorNombre.isEmpty());
        assertTrue(listaPorNombre.get(0).getNombre().equalsIgnoreCase("Carlos"));
        assertFalse(listaPorTelefono.isEmpty());
    }

}
