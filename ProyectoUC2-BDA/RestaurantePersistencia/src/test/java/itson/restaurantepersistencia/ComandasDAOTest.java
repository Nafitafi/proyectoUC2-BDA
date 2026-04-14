 ///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
// */
package itson.restaurantepersistencia;

import itson.restaurantedominio.ClienteFrecuente;
import itson.restaurantedominio.Comanda;
import itson.restaurantedominio.EstadoProducto;
import itson.restaurantedtos.EstadoComanda;
import itson.restaurantedominio.Mesa;
import itson.restaurantedominio.Producto;
import itson.restaurantedominio.TipoProducto;
import itson.restaurantedtos.ComandaDTO;
import itson.restaurantedtos.DetalleComandaDTO;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class ComandasDAOTest {

    private ComandasDAO comandasDAO;

    @BeforeEach
    void setUp() {
        comandasDAO = new ComandasDAO();
    }

    @Test
    void testGuardarComandaFunciona() throws PersistenciaException {
        EntityManager em = ManejadorConexiones.crearEntityManager();
        em.getTransaction().begin();

        Mesa mesa = new Mesa();
        mesa.setNumero(1);
        em.persist(mesa);

        ClienteFrecuente cliente = new ClienteFrecuente();
        cliente.setNombre("Andrea");
        cliente.setApellidoP("Lara");
        cliente.setApellidoM("Osuna");
        cliente.setNumeroTelefono("1122334455");
        cliente.setCorreo("c@mail.com");
        cliente.setFechaRegistro(LocalDate.now());
        cliente.setPuntos(0);
        em.persist(cliente);

        Producto p1 = new Producto();
        p1.setNombre("Pizza");
        p1.setPrecio(50.0);
        p1.setDescripcion("Pizza grande de queso");
        p1.setEstado(EstadoProducto.ACTIVO);
        p1.setTipo(TipoProducto.PLATILLO);
        p1.setImagen("pizza.jpg".getBytes());
        em.persist(p1);

        Producto p2 = new Producto();
        p2.setNombre("Refresco");
        p2.setPrecio(30.0);
        p2.setDescripcion("Refresco de cola");
        p2.setEstado(EstadoProducto.ACTIVO);
        p2.setTipo(TipoProducto.BEBIDA);
        p2.setImagen("refresco.jpg".getBytes());
        em.persist(p2);

        em.getTransaction().commit();

        List<DetalleComandaDTO> detalles = Arrays.asList(
                new DetalleComandaDTO(p1.getId(), 2, "Sin cebolla"),
                new DetalleComandaDTO(p2.getId(), 1, "Con hielo")
        );

        ComandaDTO comanda = new ComandaDTO(
                "OB-20260407-001",
                mesa.getId(),
                cliente.getIdCliente(),
                detalles,
                LocalDateTime.now(),
                EstadoComanda.ABIERTA,
                0.0
        );

        assertDoesNotThrow(() -> {
            Comanda resultado = comandasDAO.guardar(comanda);

            assertNotNull(resultado.getId());
            assertEquals("OB-20260407-001", resultado.getFolio());
            assertEquals(130.0, resultado.getTotal());
        });

    }

    @Test
    void testBuscarPorId() throws PersistenciaException {
        ComandaDTO comanda = new ComandaDTO(
                "OB-20260407-002",
                1L,
                1L,
                List.of(),
                LocalDateTime.now(),
                EstadoComanda.ABIERTA,
                0.0
        );

        assertDoesNotThrow(() -> {
            Comanda guardada = comandasDAO.guardar(comanda);
            Comanda encontrada = comandasDAO.buscarPorId(guardada.getId());

            assertNotNull(encontrada);
            assertEquals(guardada.getId(), encontrada.getId());
        });

    }

    @Test
    void testExisteComandaActivaPorMesa() throws PersistenciaException {
        ComandaDTO comanda = new ComandaDTO(
                "OB-20260407-003",
                1L,
                1L,
                List.of(),
                LocalDateTime.now(),
                EstadoComanda.ABIERTA,
                0.0
        );

        assertDoesNotThrow(() -> {
            comandasDAO.guardar(comanda);
            assertTrue(comandasDAO.existeComandaActivaPorMesa(1L));

        });
    }

    @Test
    void testContarComandasPorFecha() throws PersistenciaException {
        LocalDate hoy = LocalDate.now();
        int conteoAntes = comandasDAO.contarComandasPorFecha(hoy);

        ComandaDTO comanda = new ComandaDTO(
                "OB-20260407-004",
                1L,
                1L,
                List.of(),
                LocalDateTime.now(),
                EstadoComanda.ABIERTA,
                0.0
        );
        assertDoesNotThrow(() -> {
            comandasDAO.guardar(comanda);
        });

        int conteoDespues = comandasDAO.contarComandasPorFecha(hoy);
        assertTrue(conteoDespues > conteoAntes);
    }

    @Test
    void testObtenerTotalVentasPorRango() throws PersistenciaException {
        LocalDate inicio = LocalDate.now().minusDays(1);
        LocalDate fin = LocalDate.now().plusDays(1);

        assertDoesNotThrow(() -> {
            Double total = comandasDAO.obtenerTotalVentasPorRango(inicio, fin);
            assertNotNull(total);
            assertTrue(total >= 0);
        });

    }
}
