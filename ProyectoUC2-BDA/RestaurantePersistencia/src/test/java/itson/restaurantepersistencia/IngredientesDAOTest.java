/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package itson.restaurantepersistencia;

import itson.restaurantedominio.Ingrediente;
import itson.restaurantedtos.IngredienteActualizadoDTO;
import itson.restaurantedtos.IngredienteNuevoDTO;
import itson.restaurantedtos.UnidadMedida;
import itson.restaurantepersistencia.adapters.IngredienteNuevoDTOAIngredienteAdapter;
import java.util.List;
import javax.persistence.PersistenceException;
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
public class IngredientesDAOTest {
    private IIngredientesDAO ingredientesDAO;
    public IngredientesDAOTest() {
    }
    
    @BeforeEach
    void setUp() {
        ingredientesDAO = new IngredientesDAO();
    }
    
    /**
     * Test que verifica el funcionamiento del método agregar(IngredienteNuevoDTO ingredienteNuevo) de la clase IngredientesDAO.
     */
    @Test
    public void agregarTest(){
        Exception e;
        
        IngredienteNuevoDTO ingrediente1 = new IngredienteNuevoDTO(
                "Lechuga",
                UnidadMedida.PIEZAS,
                12.0,
                "https://superlavioleta.com/cdn/shop/products/lechuga.jpg?v=1775052689"
            );
        
        IngredienteNuevoDTO ingrediente2 = new IngredienteNuevoDTO(
                "Lechuga",
                UnidadMedida.KILOGRAMOS,
                10.0,
                "https://superlavioleta.com/cdn/shop/products/lechuga.jpg?v=1775052689"
            );
        
        //No debería lanzar error si se intenta agregar este producto
        assertDoesNotThrow(() -> {
            Ingrediente resultado = ingredientesDAO.agregar(ingrediente1);
            assertNotNull(resultado);
            assertEquals(resultado.getNombre(), ingrediente1.getNombre());
            assertEquals(resultado.getUnidadMedida(), ingrediente1.getUnidadMedida());
        });
        
        assertDoesNotThrow(() -> {
        //Tampoco debería lanzar error con este producto: mismo nombre, diferente unidad de medida
            Ingrediente resultado2 = ingredientesDAO.agregar(ingrediente2);
            assertNotNull(resultado2);
            assertEquals(resultado2.getNombre(), ingrediente2.getNombre());
            assertEquals(resultado2.getUnidadMedida(), ingrediente2.getUnidadMedida());
        });
        
        //Lanza error si se agrega:
        e = assertThrows(PersistenciaException.class, () -> {
            //un ingrediente ya existente (mismo nombre, misma unidad de medida)
            ingredientesDAO.agregar(new IngredienteNuevoDTO(
                "Lechuga",
                UnidadMedida.PIEZAS,
                12.0,
                "https://superlavioleta.com/cdn/shop/products/lechuga.jpg?v=1775052689"
            ));
            
            //un ingrediente nulo
            ingredientesDAO.agregar(new IngredienteNuevoDTO());
            
            //un ingrediente sin nombre
            ingredientesDAO.agregar(new IngredienteNuevoDTO(
                    null,
                    UnidadMedida.PIEZAS,
                    12.0
            ));
            
            //un ingrediente con un nombre de más de 100 caracteres
            ingredientesDAO.agregar(new IngredienteNuevoDTO(
                    "Alejandro Maximiliano de la Santísima Trinidad Rodríguez-Villanueva y Benavides de los Monteros Tercero",
                    UnidadMedida.PIEZAS,
                    12.0
            ));
            
            //un ingrediente sin unidad de medida
            ingredientesDAO.agregar(new IngredienteNuevoDTO(
                    "Lechuga",
                    null,
                    12.0
            ));
            
            //un ingrediente sin stock
            ingredientesDAO.agregar(new IngredienteNuevoDTO(
                    "Lechuga",
                    UnidadMedida.PIEZAS,
                    null
            ));
            
            //una imagen de más de 255 caracteres
            ingredientesDAO.agregar(new IngredienteNuevoDTO(
                    "Lechuga",
                    UnidadMedida.PIEZAS,
                    12.0,
                    "https://www.vinculoconmasde255caracteres-vamosanombrarlosprimeros-100pokemonaversisellena:"
                            + "BulbasaurIvysaurVenusaurCharmanderCharmeleonCharizardSquirtleWartortleBlastoise"
                            + "CaterpieMetapodButterfreeWeedleKakunaBeedrillPidgeyPidgeottoPidgeotRattataRaticateSpearow"
            ));
        });
    }
    
    /**
     * Test que verifica el funcionamiento del método modificar(IngredienteActualizadoDTO ingredienteActualizar) de la clase IngredientesDAO.
     */
    @Test
    public void modificarTest(){
        Exception e;
        
        //Usando un id existente en la base de datos
        //no va a dejar modificar si:
        e = assertThrows(PersistenciaException.class, () -> {
            //el nombre y unidad de medida ya existen en la base de datos
            ingredientesDAO.modificar(new IngredienteActualizadoDTO(
                7L,
                "Lechuga",
                UnidadMedida.PIEZAS,
                null
            ));
        });
        
    }
    
    /**
     * Test que verifica el funcionamiento del método eliminar(Long id) de la clase IngredientesDAO.
     */
    @Test
    public void eliminarTest(){
        Exception e;
        
        //no va a dejar eliminar si:
        e = assertThrows(PersistenciaException.class, () -> {
            //el id no existe
            ingredientesDAO.eliminar(10L);
        });
    }
    
    /**
     * Test que verifica el funcionamiento del método desinventariar(Long id, Double cantidad) de la clase IngredientesDAO.
     */
    @Test
    public void desinventariarTest(){
        Exception e;
        
        //no va a dejar desinventariar si:
        e = assertThrows(PersistenciaException.class, () -> {
            //el id no existe
            ingredientesDAO.desinventariar(10L, 100.0);
            
            //cantidad nula
            ingredientesDAO.desinventariar(7L, 0.0);
            
            //cantidad negativa
            ingredientesDAO.desinventariar(7L, -10.0);
            
            //cantidad mayor al stock
            ingredientesDAO.desinventariar(7L, 200.00);
            
        });
    }
    
    /**
     * Test que verifica el funcionamiento del método recuperarIngredientes() de la clase IngredientesDAO.
     */
    @Test
    public void recuperarIngredientesTest(){
        
        assertDoesNotThrow(() -> {
            //No debería lanzar errores
            List<Ingrediente> lista = ingredientesDAO.recuperarIngredientes();
            //aunque no haya ingredientes, la lista estaría vacía no nula
            assertNotNull(lista);
        });
    }
    
    /**
     * Test que verifica el funcionamiento del método buscar(IngredienteNuevoDTO ingrediente) de la clase IngredientesDAO.
     */
    @Test
    public void buscarTest(){
        //todo
    }
    
    /**
     * Test que verifica el funcionamiento del método buscar(Ingrediente ingrediente) de la clase IngredientesDAO.
     */
    @Test
    public void existsTest(){
        
        //Con un ingrediente no existente en la base de datos
        IngredienteNuevoDTO ingrediente = new IngredienteNuevoDTO(
                "Pepino",
                UnidadMedida.KILOGRAMOS,
                16.0
        );
        assertDoesNotThrow(() -> {
        //como no existe el ingrediente, exists devuelve false
        boolean resultado = ingredientesDAO.exists(IngredienteNuevoDTOAIngredienteAdapter.adaptar(ingrediente));
        assertFalse(resultado);
        
        //Se agrega el ingrediente a la base de datos
        Ingrediente i = ingredientesDAO.agregar(ingrediente);
            
        //como ya existe, volvemos a llamar al metodo y debería regresar true
        resultado = ingredientesDAO.exists(i);
        assertTrue(resultado);
        
        });
        
    }
}

