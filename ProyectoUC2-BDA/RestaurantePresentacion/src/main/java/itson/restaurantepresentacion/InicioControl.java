 ///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
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

    public InicioControl(InicioFORM inicioForm) {
        this.inicioForm = inicioForm;

        inicioForm.getBtnMesero().addActionListener(e -> abrirMenuMesero());
        inicioForm.getBtnAdministrador().addActionListener(e -> abrirMenuAdministrador());
    }

    private void abrirMenuMesero() {
        MenuMeseroFORM menuMesero = new MenuMeseroFORM();
        new MenuMeseroControl(menuMesero);
        menuMesero.setLocationRelativeTo(null);
        menuMesero.setVisible(true);
        inicioForm.dispose();
    }

    private void abrirMenuAdministrador() {
        MenuAdminFORM menuAdmin = new MenuAdminFORM();
        new MenuAdministradorControl(menuAdmin);
        menuAdmin.setLocationRelativeTo(null);
        menuAdmin.setVisible(true);
        inicioForm.dispose();
    }
}
