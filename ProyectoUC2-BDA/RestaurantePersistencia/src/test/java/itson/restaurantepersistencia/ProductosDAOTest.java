/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package itson.restaurantepersistencia;

import itson.restaurantedominio.Ingrediente;
import itson.restaurantedominio.Producto;
import itson.restaurantedominio.UnidadMedida;
import itson.restaurantedtos.DetallesRecetaDTO;
import itson.restaurantedtos.EstadoProducto;
import itson.restaurantedtos.NuevoProductoDTO;
import itson.restaurantedtos.ProductoDTO;
import itson.restaurantedtos.TipoProducto;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

/**
 *
 * @author Nahomi Figueroa
 */
public class ProductosDAOTest {

    private IProductosDAO productosDAO;
    private Long idIngredientePrueba;

    public ProductosDAOTest() {
    }

    @BeforeEach
    void setUp() {
        productosDAO = new ProductosDAO();

        // Setup
        EntityManager em = ManejadorConexiones.crearEntityManager();
        em.getTransaction().begin();
        Ingrediente ingrediente = new Ingrediente();
        ingrediente.setNombre("Ingrediente Prueba " + System.currentTimeMillis());
        ingrediente.setUnidadMedida(UnidadMedida.PIEZAS);
        ingrediente.setStock(50.0);
        em.persist(ingrediente);
        em.getTransaction().commit();

        idIngredientePrueba = ingrediente.getIdIngrediente();
        em.close();
    }

    @Test
    public void guardarTest() {
        List<DetallesRecetaDTO> recetaValida = new ArrayList<>();
        recetaValida.add(new DetallesRecetaDTO(idIngredientePrueba, 2.0));

        NuevoProductoDTO productoNuevo = new NuevoProductoDTO();
        productoNuevo.setNombre("Hamburguesa de Prueba " + System.currentTimeMillis());
        productoNuevo.setDescripcion("Deliciosa hamburguesa");
        productoNuevo.setPrecio(150.0);
        productoNuevo.setTipo(TipoProducto.PLATILLO);
        productoNuevo.setDetallesReceta(recetaValida);

        assertDoesNotThrow(() -> {
            Producto resultado = productosDAO.guardar(productoNuevo);
            assertNotNull(resultado);
            assertNotNull(resultado.getIdProducto());
            assertTrue(resultado.getNombre().startsWith("Hamburguesa de Prueba"));
            assertFalse(resultado.getIngredientesRequeridos().isEmpty());
        });

        // Falla: Duplicado
        assertThrows(PersistenciaException.class, () -> {
            productosDAO.guardar(productoNuevo);
        });

        // Falla: Nulo
        assertThrows(PersistenciaException.class, () -> {
            productosDAO.guardar(null);
        });
    }

    @Test
    public void actualizarTest() {
        assertDoesNotThrow(() -> {
            List<DetallesRecetaDTO> receta = new ArrayList<>();
            receta.add(new DetallesRecetaDTO(idIngredientePrueba, 1.0));

            NuevoProductoDTO nuevo = new NuevoProductoDTO();
            nuevo.setNombre("Taco de Prueba " + System.currentTimeMillis());
            nuevo.setPrecio(30.0);
            nuevo.setTipo(TipoProducto.PLATILLO);
            nuevo.setDetallesReceta(receta);

            Producto guardado = productosDAO.guardar(nuevo);

            ProductoDTO aActualizar = new ProductoDTO();
            aActualizar.setId(guardado.getIdProducto());
            aActualizar.setNombre("Taco de Prueba Modificado " + System.currentTimeMillis());
            aActualizar.setPrecio(40.0);

            Producto resultado = productosDAO.actualizar(aActualizar);

            assertNotNull(resultado);
            assertTrue(resultado.getNombre().contains("Modificado"));
            assertEquals(40.0, resultado.getPrecio());
        });
    }

    @Test
    public void buscarPorIdTest() {
        assertDoesNotThrow(() -> {
            List<DetallesRecetaDTO> receta = new ArrayList<>();
            receta.add(new DetallesRecetaDTO(idIngredientePrueba, 1.0));
            NuevoProductoDTO nuevo = new NuevoProductoDTO();
            nuevo.setNombre("Refresco Prueba " + System.currentTimeMillis());
            nuevo.setPrecio(25.0);
            nuevo.setTipo(TipoProducto.BEBIDA);
            nuevo.setDetallesReceta(receta);

            Producto guardado = productosDAO.guardar(nuevo);

            Producto encontrado = productosDAO.buscarPorId(guardado.getIdProducto());
            assertNotNull(encontrado);
            assertEquals(guardado.getNombre(), encontrado.getNombre());
        });

        assertThrows(PersistenciaException.class, () -> {
            productosDAO.buscarPorId(99999L);
        });
    }

    @Test
    public void verificarDisponibilidadTest() throws PersistenciaException {
        List<DetallesRecetaDTO> receta = new ArrayList<>();
        receta.add(new DetallesRecetaDTO(idIngredientePrueba, 5.0));
        NuevoProductoDTO nuevo = new NuevoProductoDTO();
        nuevo.setNombre("Pastel Prueba " + System.currentTimeMillis());
        nuevo.setPrecio(200.0);
        nuevo.setTipo(TipoProducto.POSTRE);
        nuevo.setDetallesReceta(receta);

        Producto guardado = productosDAO.guardar(nuevo);

        assertDoesNotThrow(() -> {
            boolean disponible = productosDAO.verificarDisponibilidad(guardado.getIdProducto());
            assertTrue(disponible);
        });

        assertDoesNotThrow(() -> {
            boolean disponible = productosDAO.verificarDisponibilidad(99999L);
            assertFalse(disponible);
        });
    }

    @Test
    public void buscarPorNombreActivosTest() throws PersistenciaException {
        List<DetallesRecetaDTO> receta = new ArrayList<>();
        receta.add(new DetallesRecetaDTO(idIngredientePrueba, 1.0));

        NuevoProductoDTO p1 = new NuevoProductoDTO();
        p1.setNombre("Sopa Especial " + System.currentTimeMillis());
        p1.setPrecio(80.0);
        p1.setTipo(TipoProducto.PLATILLO);
        p1.setDetallesReceta(receta);
        productosDAO.guardar(p1);

        assertDoesNotThrow(() -> {
            List<Producto> encontradas = productosDAO.buscarPorNombreActivos("Sopa Especial");
            assertFalse(encontradas.isEmpty());
        });
    }

    @Test
    public void buscarPorTipoTest() throws PersistenciaException {
        List<DetallesRecetaDTO> receta = new ArrayList<>();
        receta.add(new DetallesRecetaDTO(idIngredientePrueba, 1.0));

        NuevoProductoDTO p2 = new NuevoProductoDTO();
        p2.setNombre("Agua de Jamaica " + System.currentTimeMillis());
        p2.setPrecio(20.0);
        p2.setTipo(TipoProducto.BEBIDA);
        p2.setDetallesReceta(receta);
        productosDAO.guardar(p2);

        assertDoesNotThrow(() -> {
            List<Producto> bebidas = productosDAO.buscarPorTipo(TipoProducto.BEBIDA);
            assertFalse(bebidas.isEmpty());

            // Probar trayendo todos (tipo null)
            List<Producto> todos = productosDAO.buscarPorTipo(null);
            assertFalse(todos.isEmpty());
        });
    }

    @Test
    public void consultarTodosProductosTest() throws PersistenciaException {
        assertDoesNotThrow(() -> {
            List<Producto> todos = productosDAO.consultarTodosProductos();
            assertNotNull(todos);
        });
    }

    @Test
    public void actualizarEstadoTest() throws PersistenciaException {
        List<DetallesRecetaDTO> receta = new ArrayList<>();
        receta.add(new DetallesRecetaDTO(idIngredientePrueba, 1.0));

        NuevoProductoDTO p1 = new NuevoProductoDTO();
        p1.setNombre("Papas Fritas " + System.currentTimeMillis());
        p1.setPrecio(50.0);
        p1.setTipo(TipoProducto.EXTRA);
        p1.setDetallesReceta(receta);
        Producto guardado = productosDAO.guardar(p1);

        assertDoesNotThrow(() -> {
            productosDAO.actualizarEstado(guardado.getIdProducto(), EstadoProducto.INACTIVO);
            Producto actualizado = productosDAO.buscarPorId(guardado.getIdProducto());
            assertEquals(itson.restaurantedominio.EstadoProducto.INACTIVO, actualizado.getEstado());
        });
    }

    @Test
    public void existeTest() throws PersistenciaException {
        List<DetallesRecetaDTO> receta = new ArrayList<>();
        receta.add(new DetallesRecetaDTO(idIngredientePrueba, 1.0));

        String nombreUnico = "Enchiladas Prueba " + System.currentTimeMillis();
        NuevoProductoDTO p1 = new NuevoProductoDTO();
        p1.setNombre(nombreUnico);
        p1.setPrecio(100.0);
        p1.setTipo(TipoProducto.PLATILLO);
        p1.setDetallesReceta(receta);
        Producto guardado = productosDAO.guardar(p1);

        assertDoesNotThrow(() -> {
            Producto productoPrueba = new Producto();
            productoPrueba.setNombre(nombreUnico);
            assertTrue(productosDAO.existe(productoPrueba));

            Producto productoNoExiste = new Producto();
            productoNoExiste.setNombre("Un Nombre Que Definitivamente No Existe");
            assertFalse(productosDAO.existe(productoNoExiste));

            assertFalse(productosDAO.existe(guardado));
        });
    }
}
