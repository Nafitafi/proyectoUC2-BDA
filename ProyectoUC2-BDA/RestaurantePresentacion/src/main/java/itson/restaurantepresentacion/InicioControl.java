/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.restaurantepresentacion;

/**
 *
 * @author Carmen Andrea Lara Osuna
 */
import itson.restaurantenegocio.NegocioException;
import java.util.logging.Logger;

/**
 * Controlador para la pantalla de inicio y menús. Conecta los botones de rol
 * con los menús correspondientes y gestiona las acciones dentro de cada menú.
 */
public class InicioControl {

    private final InicioFORM inicioForm;
    private static final Logger LOGGER = Logger.getLogger(InicioControl.class.getName());

    public InicioControl(InicioFORM inicioForm) {
        this.inicioForm = inicioForm;

        // Botones de inicio
        inicioForm.getBtnMesero().addActionListener(e -> abrirMenuMesero());
        inicioForm.getBtnAdministrador().addActionListener(e -> abrirMenuAdministrador());
    }

    /**
     * Abre el menú de mesero y configura sus botones.
     */
    private void abrirMenuMesero() {
        try {
            MenuMeseroFORM menuMesero = new MenuMeseroFORM();

            menuMesero.getBtnAbrirComanda().addActionListener(e -> abrirComanda());

            menuMesero.setLocationRelativeTo(null);
            menuMesero.setVisible(true);
            inicioForm.dispose();
        } catch (Exception ex) {
            LOGGER.severe("Error al abrir menú mesero: " + ex.getMessage());
        }
    }

    /**
     * Abre el menú de administrador y configura sus botones.
     */
    private void abrirMenuAdministrador() {
        try {
            MenuAdminFORM menuAdmin = new MenuAdminFORM();

            // Control de botones del menú administrador
            menuAdmin.getBtnProductos().addActionListener(e -> abrirProductos());
            menuAdmin.getBtnIngredientes().addActionListener(e -> abrirIngredientes());
            menuAdmin.getBtnClientes().addActionListener(e -> abrirClientes());
            menuAdmin.getBtnReportes().addActionListener(e -> abrirReportes());

            menuAdmin.setLocationRelativeTo(null);
            menuAdmin.setVisible(true);
            inicioForm.dispose();
        } catch (Exception ex) {
            LOGGER.severe("Error al abrir menú administrador: " + ex.getMessage());
        }
    }

    // Métodos auxiliares para abrir formularios específicos
    private void abrirComanda() {
        ComandasFORM comandaForm = new ComandasFORM();
        try {
            new ComandaControl(comandaForm);
        } catch (NegocioException ex) {
            LOGGER.severe("Error al abrir menú administrador: " + ex.getMessage());
        }
        comandaForm.setLocationRelativeTo(null);
        comandaForm.setVisible(true);
    }

    private void abrirClientes() {
        ClientesFrecuentesFORM clientesForm = new ClientesFrecuentesFORM();
        new ClientesFrecuentesControl(clientesForm);
        clientesForm.setLocationRelativeTo(null);
        clientesForm.setVisible(true);
    }

    private void abrirProductos() {
        ProductosFORM productosForm = new ProductosFORM();
        new ProductosControl(productosForm);
        productosForm.setLocationRelativeTo(null);
        productosForm.setVisible(true);
    }

    private void abrirIngredientes() {
        InventarioIngredientes ingredientesForm = new InventarioIngredientes();
        ingredientesForm.setLocationRelativeTo(null);
        ingredientesForm.setVisible(true);
    }

    private void abrirReportes() {
        ReportesFORM reportesForm = new ReportesFORM();
        new ReportesControl(reportesForm);
        reportesForm.setLocationRelativeTo(null);
        reportesForm.setVisible(true);
    }
}
