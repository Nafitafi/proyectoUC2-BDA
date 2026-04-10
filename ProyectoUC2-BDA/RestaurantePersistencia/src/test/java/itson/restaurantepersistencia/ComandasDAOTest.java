///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
// */
//package itson.restaurantepersistencia;
//
//import itson.restaurantedominio.Comanda;
//import itson.restaurantedtos.ComandaDTO;
//import itson.restaurantedtos.DetalleComandaDTO;
//import itson.restaurantedtos.EstadoComanda;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.List;
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;
//import org.junit.jupiter.api.BeforeEach;
//
///**
// *
// * @author Carmen Andrea Lara Osuna
// */
//public class ComandasDAOTest {
//
//    private IComandasDAO comandasDAO;
//
//    @BeforeEach
//    public void setUp() {
//        comandasDAO = new ComandasDAO();
//    }
//
//    /**
//     * Prueba para verificar que se puede guardar una comanda correctamente.
//     */
//    @Test
//    public void testGuardarComanda() throws PersistenciaException {
//
//        List<DetalleComandaDTO> detalles = Arrays.asList(
//                new DetalleComandaDTO(201L, 3, "Sin cebolla"),
//                new DetalleComandaDTO(305L, 2, "Con hielo"));
//
//        ComandaDTO comanda = new ComandaDTO(
//                "OB-20260407-001",
//                5L,
//                101L,
//                detalles,
//                LocalDateTime.of(2026, 4, 7, 20, 15),
//                EstadoComanda.ABIERTA,
//                130.0
//        );
//
//        assertDoesNotThrow(() -> {
//            Comanda comandaGuardada = comandasDAO.guardar(comanda);
//            assertNotNull(comandaGuardada);
//            assertEquals("OB-20260407-001", comandaGuardada.getFolio());
//            assertEquals(150.0, comandaGuardada.getTotal());
//            assertEquals(1, comandaGuardada.getMesa().getNumero());
//        });
//
//    }
//
//    /**
//     * Prueba para verificar que se puede buscar una comanda por id.
//     */
//    @Test
//    public void testBuscarPorId() throws PersistenciaException {
//
//         List<DetalleComandaDTO> detalles = Arrays.asList(
//                new DetalleComandaDTO(201L, 3, "Sin cebolla"),
//                new DetalleComandaDTO(305L, 2, "Con hielo"));
//
//        ComandaDTO comanda = new ComandaDTO(
//                "OB-20260407-001",
//                5L,
//                101L,
//                detalles,
//                LocalDateTime.of(2026, 4, 7, 20, 15),
//                EstadoComanda.ABIERTA,
//                130.0
//        );
//
//        assertDoesNotThrow(() -> {
//            Comanda guardada = comandasDAO.guardar(comanda);
//
//            Comanda encontrada = comandasDAO.buscarPorId(guardada.getId());
//
//            assertNotNull(encontrada);
//            assertEquals(guardada.getId(), encontrada.getId());
//        });
//
//    }
//
//    /**
//     * Prueba para verificar si una mesa tiene una comanda activa.
//     */
//    @Test
//    public void testExisteComandaActivaPorMesa() throws PersistenciaException {
//
//         List<DetalleComandaDTO> detalles = Arrays.asList(
//                new DetalleComandaDTO(201L, 3, "Sin cebolla"),
//                new DetalleComandaDTO(305L, 2, "Con hielo"));
//
//        ComandaDTO comanda = new ComandaDTO(
//                "OB-20260407-001",
//                5L,
//                101L,
//                detalles,
//                LocalDateTime.of(2026, 4, 7, 20, 15),
//                EstadoComanda.ABIERTA,
//                130.0
//        );
//        assertDoesNotThrow(() -> {
//            comandasDAO.guardar(comanda);
//
//            boolean existe = comandasDAO.existeComandaActivaPorMesa(5L);
//
//            assertTrue(existe);
//        });
//
//    }
//
//    /**
//     * Prueba para verificar el conteo de comandas por fecha.
//     */
//    @Test
//    public void testContarComandasPorFecha() throws PersistenciaException {
//
//        LocalDate hoy = LocalDate.now();
//
//        int conteoAntes = comandasDAO.contarComandasPorFecha(hoy);
//
//         List<DetalleComandaDTO> detalles = Arrays.asList(
//                new DetalleComandaDTO(201L, 3, "Sin cebolla"),
//                new DetalleComandaDTO(305L, 2, "Con hielo"));
//
//        ComandaDTO comanda = new ComandaDTO(
//                "OB-20260407-001",
//                5L,
//                101L,
//                detalles,
//                LocalDateTime.now(),
//                EstadoComanda.ABIERTA,
//                130.0
//        );
//        assertDoesNotThrow(() -> {
//            comandasDAO.guardar(comanda);
//
//            int conteoDespues = comandasDAO.contarComandasPorFecha(hoy);
//
//            assertTrue(conteoDespues > conteoAntes);
//        });
//    }
//
//    /**
//     * Prueba para verificar el total de ventas en un rango de fechas.
//     */
//    @Test
//    public void testObtenerTotalVentasPorRango() throws PersistenciaException {
//
//        LocalDate inicio = LocalDate.now().minusDays(1);
//        LocalDate fin = LocalDate.now().plusDays(1);
//
//        assertDoesNotThrow(() -> {
//            Double total = comandasDAO.obtenerTotalVentasPorRango(inicio, fin);
//
//            assertNotNull(total);
//            assertTrue(total >= 0);
//        });
//
//    }
//}
