/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package itson.restaurantepresentacion;

/**
 *
 * @author Nahomi Amelia Figueroa Briones
 */
public class SistemaRestaurante {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        ClientesFrecuentesFORM ventana = new ClientesFrecuentesFORM();
//        ClientesFrecuentesControl control = new ClientesFrecuentesControl(ventana);
//        ventana.setLocationRelativeTo(null);
//        ventana.setVisible(true);
//
    InventarioIngredientesFORM v = new InventarioIngredientesFORM();
    IngredientesControl control = new IngredientesControl(v);
            v.setVisible(true);
    
//        ProductosFORM ventana = new ProductosFORM();
//        ProductosControl control = new ProductosControl();
//        ventana.setLocationRelativeTo(null);
//        ventana.setVisible(true);

    }
}
