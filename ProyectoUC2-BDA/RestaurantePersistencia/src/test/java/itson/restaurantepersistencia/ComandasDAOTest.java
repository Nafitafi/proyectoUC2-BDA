/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package itson.restaurantepersistencia;

import itson.restaurantedominio.Comanda;
import itson.restaurantedtos.ComandaDTO;
import itson.restaurantedtos.DetalleComandaDTO;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

/**
 *
 * @author Carmen Andrea Lara Osuna
 */
public class ComandasDAOTest {

    private IComandasDAO comandasDAO;

    @BeforeEach
    public void setUp() {
        comandasDAO = new ComandasDAO();
    }

    /**
     * Prueba para verificar que se puede guardar una comanda correctamente.
     */
    @Test
    public void testGuardarComanda() throws PersistenciaException {

        List<DetalleComandaDTO> detalles = new LinkedList<>();
        detalles.add(new DetalleComandaDTO(
                1L,
                "Hamburguesa",
                1,
                "Sin cebolla",
                150.0,
                150.0
        ));
        ComandaDTO comanda = new ComandaDTO(
                "OB-20260405-001",
                LocalDateTime.now(),
                itson.restaurantedtos.EstadoComanda.ABIERTA,
                150.0,
                1,
                null,
                detalles
        );

        assertDoesNotThrow(() -> {
            Comanda comandaGuardada = comandasDAO.guardar(comanda);
            assertNotNull(comandaGuardada);
            assertEquals("OB-20260405-001", comandaGuardada.getFolio());
            assertEquals(150.0, comandaGuardada.getTotalAcumulado());
            assertEquals(1, comandaGuardada.getMesa().getNumero());
        });

    }

    /**
     * Prueba para verificar que se puede buscar una comanda por id.
     */
    @Test
    public void testBuscarPorId() throws PersistenciaException {

        // Primero guardamos una comanda
        List<DetalleComandaDTO> detalles = new LinkedList<>();
        detalles.add(new DetalleComandaDTO(
                1L,
                "Taco de asada",
                1,
                "Sin tomate",
                25.0,
                25.0
        ));

        ComandaDTO comanda = new ComandaDTO(
                "OB-20260405-002",
                LocalDateTime.now(),
                itson.restaurantedtos.EstadoComanda.ABIERTA,
                25.0,
                1,
                null,
                detalles
        );

        assertDoesNotThrow(() -> {
            Comanda guardada = comandasDAO.guardar(comanda);

            Comanda encontrada = comandasDAO.buscarPorId(guardada.getId());

            assertNotNull(encontrada);
            assertEquals(guardada.getId(), encontrada.getId());
        });

    }

    /**
     * Prueba para verificar si una mesa tiene una comanda activa.
     */
    @Test
    public void testExisteComandaActivaPorMesa() throws PersistenciaException {

        List<DetalleComandaDTO> detalles = new LinkedList<>();
        detalles.add(new DetalleComandaDTO(
                1L,
                "Taco de asada",
                2,
                "",
                25.0,
                50.0
        ));
        ComandaDTO dto = new ComandaDTO(
                "OB-20260405-003",
                LocalDateTime.now(),
                itson.restaurantedtos.EstadoComanda.ABIERTA,
                50.0,
                2,
                null,
                detalles
        );

        assertDoesNotThrow(() -> {
            comandasDAO.guardar(dto);

            boolean existe = comandasDAO.existeComandaActivaPorMesa(2);

            assertTrue(existe);
        });

    }

    /**
     * Prueba para verificar el conteo de comandas por fecha.
     */
    @Test
    public void testContarComandasPorFecha() throws PersistenciaException {

        LocalDate hoy = LocalDate.now();

        int conteoAntes = comandasDAO.contarComandasPorFecha(hoy);

        List<DetalleComandaDTO> detalles = new LinkedList<>();
        detalles.add(new DetalleComandaDTO(
                1L,
                "Taco de asada",
                1,
                "Sin tomate",
                25.0,
                25.0
        ));
        ComandaDTO comanda1 = new ComandaDTO(
                "OB-20260405-004",
                LocalDateTime.now(),
                itson.restaurantedtos.EstadoComanda.ABIERTA,
                25.0,
                3,
                null,
                detalles
        );

        assertDoesNotThrow(() -> {
            comandasDAO.guardar(comanda1);

            int conteoDespues = comandasDAO.contarComandasPorFecha(hoy);

            assertTrue(conteoDespues > conteoAntes);
        });
    }

    /**
     * Prueba para verificar el total de ventas en un rango de fechas.
     */
    @Test
    public void testObtenerTotalVentasPorRango() throws PersistenciaException {

        LocalDate inicio = LocalDate.now().minusDays(1);
        LocalDate fin = LocalDate.now().plusDays(1);

        assertDoesNotThrow(() -> {
            Double total = comandasDAO.obtenerTotalVentasPorRango(inicio, fin);

            assertNotNull(total);
            assertTrue(total >= 0);
        });

    }
}
