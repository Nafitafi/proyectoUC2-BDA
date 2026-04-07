/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package itson.restaurantepersistencia;

import itson.restaurantedominio.Ingrediente;
import itson.restaurantedominio.Producto;
import itson.restaurantedominio.UnidadMedida;
import itson.restaurantedtos.DetallesRecetaDTO;
import itson.restaurantedtos.NuevoProductoDTO;
import itson.restaurantedtos.ProductoActualizadoDTO;
import itson.restaurantedtos.TipoProducto;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

/**
 *
 * @author nafbr
 */
public class ProductosDAOTest {
    
    private IProductosDAO productosDAO;
    private Long idIngredientePrueba;
    
    public ProductosDAOTest() {
    }

    @Test
    public void testSomeMethod() {
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
        //Preparar una receta válida
        List<DetallesRecetaDTO> recetaValida = new ArrayList<>();
        recetaValida.add(new DetallesRecetaDTO(idIngredientePrueba, 2.0));

        // Preparar el DTO válido
        NuevoProductoDTO productoNuevo = new NuevoProductoDTO();
        productoNuevo.setNombre("Hamburguesa de Prueba");
        productoNuevo.setDescripcion("Deliciosa hamburguesa");
        productoNuevo.setPrecio(150.0);
        productoNuevo.setTipo(TipoProducto.PLATILLO);
        productoNuevo.setDetallesReceta(recetaValida);

        // Prueba de éxito
        assertDoesNotThrow(() -> {
            Producto resultado = productosDAO.guardar(productoNuevo);
            assertNotNull(resultado);
            assertNotNull(resultado.getIdProducto());
            assertEquals("Hamburguesa de Prueba", resultado.getNombre());
            assertFalse(resultado.getIngredientesRequeridos().isEmpty());
        });

        // Pruebas de falla 
        assertThrows(PersistenciaException.class, () -> {
            // Intentar guardar el mismo producto (nombre duplicado)
            productosDAO.guardar(productoNuevo);
        });

        assertThrows(PersistenciaException.class, () -> {
            // Producto nulo
            productosDAO.guardar(null);
        });

        assertThrows(PersistenciaException.class, () -> {
            // Sin nombre
            NuevoProductoDTO sinNombre = new NuevoProductoDTO();
            sinNombre.setNombre("");
            productosDAO.guardar(sinNombre);
        });

        assertThrows(PersistenciaException.class, () -> {
            // Sin receta
            NuevoProductoDTO sinReceta = new NuevoProductoDTO();
            sinReceta.setNombre("Hot Dog Prueba");
            sinReceta.setPrecio(50.0);
            sinReceta.setTipo(TipoProducto.PLATILLO);
            sinReceta.setDetallesReceta(new ArrayList<>()); // Receta vacía
            productosDAO.guardar(sinReceta);
        });
    }

    @Test
    public void actualizarTest() {
        assertDoesNotThrow(() -> {
            // Guardar un producto primero
            List<DetallesRecetaDTO> receta = new ArrayList<>();
            receta.add(new DetallesRecetaDTO(idIngredientePrueba, 1.0));
            
            NuevoProductoDTO nuevo = new NuevoProductoDTO();
            nuevo.setNombre("Taco de Prueba");
            nuevo.setPrecio(30.0);
            nuevo.setTipo(TipoProducto.PLATILLO);
            nuevo.setDetallesReceta(receta);
            
            Producto guardado = productosDAO.guardar(nuevo);

            //  Intentar actualizarlo
            ProductoActualizadoDTO aActualizar = new ProductoActualizadoDTO();
            aActualizar.setId(guardado.getIdProducto());
            aActualizar.setNombre("Taco de Prueba Modificado");
            aActualizar.setPrecio(40.0);

            Producto resultado = productosDAO.actualizar(aActualizar);
            
            assertNotNull(resultado);
            assertEquals("Taco de Prueba Modificado", resultado.getNombre());
            assertEquals(40.0, resultado.getPrecio());
        });

        // Pruebas de falla
        assertThrows(PersistenciaException.class, () -> {
            // Actualizar con ID nulo
            productosDAO.actualizar(new ProductoActualizadoDTO());
        });
        
        assertThrows(PersistenciaException.class, () -> {
            // Actualizar un ID que no existe
            ProductoActualizadoDTO noExiste = new ProductoActualizadoDTO();
            noExiste.setId(99999L);
            productosDAO.actualizar(noExiste);
        });
    }

    @Test
    public void buscarPorIdTest() {
        assertDoesNotThrow(() -> {
            // Guardar producto
            List<DetallesRecetaDTO> receta = new ArrayList<>();
            receta.add(new DetallesRecetaDTO(idIngredientePrueba, 1.0));
            NuevoProductoDTO nuevo = new NuevoProductoDTO();
            nuevo.setNombre("Refresco Prueba");
            nuevo.setPrecio(25.0);
            nuevo.setTipo(TipoProducto.BEBIDA);
            nuevo.setDetallesReceta(receta);
            
            Producto guardado = productosDAO.guardar(nuevo);

            // Buscarlo por ID
            Producto encontrado = productosDAO.buscarPorId(guardado.getIdProducto());
            assertNotNull(encontrado);
            assertEquals("Refresco Prueba", encontrado.getNombre());
        });

        // Falla: Buscar un ID que no existe
        assertThrows(PersistenciaException.class, () -> {
            productosDAO.buscarPorId(99999L);
        });
    }

    @Test
    public void verificarDisponibilidadTest() throws PersistenciaException {
        // Crear producto
        List<DetallesRecetaDTO> receta = new ArrayList<>();
        receta.add(new DetallesRecetaDTO(idIngredientePrueba, 5.0)); // Requiere 5
        NuevoProductoDTO nuevo = new NuevoProductoDTO();
        nuevo.setNombre("Pastel Prueba");
        nuevo.setPrecio(200.0);
        nuevo.setTipo(TipoProducto.POSTRE);
        nuevo.setDetallesReceta(receta);
        
        Producto guardado = productosDAO.guardar(nuevo);

        assertDoesNotThrow(() -> {
            // El ingrediente se creó con stock 50.0, requiere 5.0 -> Debe estar disponible
            boolean disponible = productosDAO.verificarDisponibilidad(guardado.getIdProducto());
            assertTrue(disponible);
        });
        
        // Falla silenciosa: Verificar un ID que no existe retorna false 
        assertDoesNotThrow(() -> {
            boolean disponible = productosDAO.verificarDisponibilidad(99999L);
            assertFalse(disponible);
        });
    }

    @Test
    public void buscarProductosActivosFiltroTest() throws PersistenciaException {
        // Insertar datos
        List<DetallesRecetaDTO> receta = new ArrayList<>();
        receta.add(new DetallesRecetaDTO(idIngredientePrueba, 1.0));
        
        NuevoProductoDTO p1 = new NuevoProductoDTO();
        p1.setNombre("Sopa Especial");
        p1.setPrecio(80.0);
        p1.setTipo(TipoProducto.PLATILLO);
        p1.setDetallesReceta(receta);
        productosDAO.guardar(p1);

        NuevoProductoDTO p2 = new NuevoProductoDTO();
        p2.setNombre("Sopa de Tortilla");
        p2.setPrecio(90.0);
        p2.setTipo(TipoProducto.PLATILLO);
        p2.setDetallesReceta(receta);
        productosDAO.guardar(p2);

        assertDoesNotThrow(() -> {
            // Buscar por coincidencia parcial
            List<Producto> encontradas = productosDAO.buscarProductosActivosFiltro("Sopa", null);
            assertFalse(encontradas.isEmpty());
            assertTrue(encontradas.size() >= 2);
            
            // Buscar por tipo específico
            List<Producto> platillos = productosDAO.buscarProductosActivosFiltro(null, TipoProducto.PLATILLO);
            assertFalse(platillos.isEmpty());
        });
    }

    @Test
    public void existeTest() throws PersistenciaException {
        //  Guardar producto
        List<DetallesRecetaDTO> receta = new ArrayList<>();
        receta.add(new DetallesRecetaDTO(idIngredientePrueba, 1.0));
        
        NuevoProductoDTO p1 = new NuevoProductoDTO();
        p1.setNombre("Enchiladas Prueba");
        p1.setPrecio(100.0);
        p1.setTipo(TipoProducto.PLATILLO);
        p1.setDetallesReceta(receta);
        Producto guardado = productosDAO.guardar(p1);

        assertDoesNotThrow(() -> {
            // Crear un objeto temporal con el mismo nombre pero SIN ID
            Producto productoPrueba = new Producto();
            productoPrueba.setNombre("Enchiladas Prueba");
            
            // Debe regresar true porque ya existe "Enchiladas Prueba" en la BD
            assertTrue(productosDAO.existe(productoPrueba));
            
            // Crear un objeto temporal con nombre diferente
            Producto productoNoExiste = new Producto();
            productoNoExiste.setNombre("Un Nombre Que Definitivamente No Existe");
            
            // Debe regresar false
            assertFalse(productosDAO.existe(productoNoExiste));
            
            // Probar con el mismo objeto guardado (tiene su propio ID, debería ignorarse a sí mismo)
            assertFalse(productosDAO.existe(guardado));
        });
    }
}
