/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package itson.restaurantepresentacion;

import itson.restaurantenegocio.NegocioException;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Carmen Andrea Lara Osuna
 */
public class MenuAdministradorControl {
    private final MenuAdminFORM menuAdmin;
    private static final Logger LOGGER = Logger.getLogger(MenuAdministradorControl.class.getName());

    public MenuAdministradorControl(MenuAdminFORM menuAdmin) {
        this.menuAdmin = menuAdmin;

        menuAdmin.getBtnProductos().addActionListener(e -> abrirProductos());
        menuAdmin.getBtnIngredientes().addActionListener(e -> abrirIngredientes());
        menuAdmin.getBtnClientes().addActionListener(e -> abrirClientes());
        menuAdmin.getBtnReportesClientes().addActionListener(e -> abrirReportesClientes());
        menuAdmin.getBtnReportesComandas().addActionListener(e -> abrirReportesComandas());
        menuAdmin.getBtnRegresar().addActionListener(e -> regresarInicio());
    }

    /**
     * Método para abrir la pantalla de productos.
     */
    private void abrirProductos() {
        ProductosFORM productosForm = new ProductosFORM();
        productosForm.setLocationRelativeTo(null);
        productosForm.setVisible(true);
        menuAdmin.dispose();
    }

    /**
     * Método para abrir el inventario de ingredientes
     */
    private void abrirIngredientes() {
        InventarioIngredientesFORM ingredientesForm = new InventarioIngredientesFORM();
        new IngredientesControl(ingredientesForm);
        ingredientesForm.setLocationRelativeTo(null);
        ingredientesForm.setVisible(true);
        menuAdmin.dispose();
    }

    /**
     * Método para abrir la forma de clientes
     */
    private void abrirClientes() {
        ClientesFrecuentesFORM clientesForm = new ClientesFrecuentesFORM();
        new ClientesFrecuentesControl(clientesForm);
        clientesForm.setLocationRelativeTo(null);
        clientesForm.setVisible(true);
        menuAdmin.dispose();
    }

    /**
     * Método para abrir el reporte de clientes
     */
    private void abrirReportesClientes() {
        ReporteClientesFORM reportesForm = new ReporteClientesFORM();
        new ReportesControl(reportesForm);
        reportesForm.setLocationRelativeTo(null);
        reportesForm.setVisible(true);
        menuAdmin.dispose();
    }

    /**
     * Método para abrir el reporte de comandas
     */
    private void abrirReportesComandas() {
        ReporteComandasFORM reportesForm = new ReporteComandasFORM();
        new ReportesControl(reportesForm);
        reportesForm.setLocationRelativeTo(null);
        reportesForm.setVisible(true);
        menuAdmin.dispose();
    }

    /**
     * Método para regresar al inicio
     */
    private void regresarInicio() {
        InicioFORM inicioForm = new InicioFORM();
        new InicioControl(inicioForm);
        inicioForm.setLocationRelativeTo(null);
        inicioForm.setVisible(true);
        menuAdmin.dispose();
    }
}


