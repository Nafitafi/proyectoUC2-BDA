/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import itson.restaurantedominio.ClienteFrecuente;
import itson.restaurantedominio.Comanda;
import itson.restaurantedominio.EstadoComanda;
import itson.restaurantedtos.ClienteFrecuenteNuevoDTO;
import itson.restaurantepersistencia.ClientesFrecuentesDAO;
import itson.restaurantepersistencia.ManejadorConexiones;
import itson.restaurantepersistencia.PersistenciaException;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

/**
 * Clase de pruebas para la capa de persistencia de los clientes frecuentes
 * @author Andrea Lara, Nahomi Figueroa, Zaira Barajas
 */
public class ClientesFrecuentesDAOTest {
    
    public ClientesFrecuentesDAOTest() {
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

    @Test
    void obtenerVisitasClienteFrecuenteFuncionaOk() throws PersistenciaException {
        ClienteFrecuenteNuevoDTO cliente = new ClienteFrecuenteNuevoDTO("Andrea", "Lara", "Osuna", "1122334455", "c@mail.com");
        ClienteFrecuente clientePrueba = clientesDAO.guardar(cliente);

        EntityManager em = ManejadorConexiones.crearEntityManager();
        em.getTransaction().begin();

        Comanda c1 = new Comanda();
        c1.setFolio("FOLIO-001");
        c1.setCliente(clientePrueba);
        c1.setEstado(EstadoComanda.ENTREGADA);
        c1.setTotalAcumulado(200.0);
        c1.setFechaHora(LocalDate.now());

        Comanda c2 = new Comanda();
        c2.setFolio("FOLIO-002");
        c2.setCliente(clientePrueba);
        c2.setEstado(EstadoComanda.ENTREGADA);
        c2.setTotalAcumulado(100.0);
        c2.setFechaHora(LocalDate.now());

        Comanda c3 = new Comanda();
        c3.setFolio("FOLIO-003");
        c3.setCliente(clientePrueba);
        c3.setEstado(EstadoComanda.CANCELADA);
        c3.setTotalAcumulado(0.0);
        c3.setFechaHora(LocalDate.now());
        
        em.persist(c1);
        em.persist(c2);
        em.persist(c3);

        em.getTransaction().commit();

        assertDoesNotThrow(() -> {
            Long visitas = clientesDAO.obtenerVisitasClienteFrecuente(clientePrueba.getIdCliente());
            assertEquals(2L, visitas);
        });

    }
    
    @Test
    void obtenerTotalGastadoClienteFrecuenteFuncionaOk() throws PersistenciaException{
        ClienteFrecuenteNuevoDTO cliente = new ClienteFrecuenteNuevoDTO("Pedro", "Gonzales", "Perez", "89047352", "pepe@mail.com");
        ClienteFrecuente clientePrueba = clientesDAO.guardar(cliente);

        EntityManager em = ManejadorConexiones.crearEntityManager();
        em.getTransaction().begin();

        Comanda c1 = new Comanda();
        c1.setFolio("FOLIO-004");
        c1.setCliente(clientePrueba);
        c1.setEstado(EstadoComanda.ENTREGADA);
        c1.setTotalAcumulado(300.0);
        c1.setFechaHora(LocalDate.now());

        Comanda c2 = new Comanda();
        c2.setFolio("FOLIO-005");
        c2.setCliente(clientePrueba);
        c2.setEstado(EstadoComanda.ENTREGADA);
        c2.setTotalAcumulado(100.0);
        c2.setFechaHora(LocalDate.now());

        Comanda c3 = new Comanda();
        c3.setFolio("FOLIO-006");
        c3.setCliente(clientePrueba);
        c3.setEstado(EstadoComanda.CANCELADA);
        c3.setTotalAcumulado(0.0);
        c3.setFechaHora(LocalDate.now());
        
        em.persist(c1);
        em.persist(c2);
        em.persist(c3);

        em.getTransaction().commit();

        assertDoesNotThrow(() -> {
            Double totalGastado = clientesDAO.obtenerTotalGastadoClienteFrecuente(clientePrueba.getIdCliente());
            assertEquals(400.0, totalGastado);
        });
    }
    
}
