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
public class MenuMeseroControl {
    private final MenuMeseroFORM menuMesero;

    public MenuMeseroControl(MenuMeseroFORM menuMesero) {
        this.menuMesero = menuMesero;

        menuMesero.getBtnAbrirComanda().addActionListener(e -> abrirComanda());
        menuMesero.getBtnRegresar().addActionListener(e -> regresarInicio());
    }

    private void abrirComanda() {
    ComandasFORM comandaForm = new ComandasFORM();
    try {
        new ComandaControl(comandaForm);
        comandaForm.setLocationRelativeTo(null);
        comandaForm.setVisible(true);
        menuMesero.dispose();
    } catch (NegocioException ex) {
        Logger.getLogger(MenuMeseroControl.class.getName())
              .severe("Error al abrir comanda: " + ex.getMessage());
        JOptionPane.showMessageDialog(menuMesero,
              "No se pudo abrir la comanda.\n" + ex.getMessage(),
              "Error", JOptionPane.ERROR_MESSAGE);
    }
}


    private void regresarInicio() {
        InicioFORM inicioForm = new InicioFORM();
        new InicioControl(inicioForm);
        inicioForm.setLocationRelativeTo(null);
        inicioForm.setVisible(true);
        menuMesero.dispose();
    }
}

