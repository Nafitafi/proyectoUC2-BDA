/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package itson.restaurantepresentacion;

/**
 *
 * @author Carmen Andrea Lara Osuna
 */
public class MenuAdministradorControl {
    private final MenuAdminFORM menuAdmin;

    public MenuAdministradorControl(MenuAdminFORM menuAdmin) {
        this.menuAdmin = menuAdmin;

        menuAdmin.getBtnProductos().addActionListener(e -> abrirProductos());
        menuAdmin.getBtnIngredientes().addActionListener(e -> abrirIngredientes());
        menuAdmin.getBtnClientes().addActionListener(e -> abrirClientes());
        menuAdmin.getBtnReportesClientes().addActionListener(e -> abrirReportesClientes());
        menuAdmin.getBtnReportesComandas().addActionListener(e -> abrirReportesComandas());
        menuAdmin.getBtnRegresar().addActionListener(e -> regresarInicio());
    }

    private void abrirProductos() {
        ProductosFORM productosForm = new ProductosFORM();
        new ProductosControl(productosForm);
        productosForm.setLocationRelativeTo(null);
        productosForm.setVisible(true);
        menuAdmin.dispose();
    }

    private void abrirIngredientes() {
        InventarioIngredientesFORM ingredientesForm = new InventarioIngredientesFORM();
        new IngredientesControl(ingredientesForm);
        ingredientesForm.setLocationRelativeTo(null);
        ingredientesForm.setVisible(true);
        menuAdmin.dispose();
    }

    private void abrirClientes() {
        ClientesFrecuentesFORM clientesForm = new ClientesFrecuentesFORM();
        new ClientesFrecuentesControl(clientesForm);
        clientesForm.setLocationRelativeTo(null);
        clientesForm.setVisible(true);
        menuAdmin.dispose();
    }

    private void abrirReportesClientes() {
        ReporteClientesFORM reportesForm = new ReporteClientesFORM();
        new ReportesControl(reportesForm);
        reportesForm.setLocationRelativeTo(null);
        reportesForm.setVisible(true);
        menuAdmin.dispose();
    }

    private void abrirReportesComandas() {
        ReporteComandasFORM reportesForm = new ReporteComandasFORM();
        new ReportesControl(reportesForm);
        reportesForm.setLocationRelativeTo(null);
        reportesForm.setVisible(true);
        menuAdmin.dispose();
    }

    private void regresarInicio() {
        InicioFORM inicioForm = new InicioFORM();
        new InicioControl(inicioForm);
        inicioForm.setLocationRelativeTo(null);
        inicioForm.setVisible(true);
        menuAdmin.dispose();
    }
}


