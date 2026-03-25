/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.restaurantepresentacion;

import itson.restaurantedominio.ClienteFrecuente;
import itson.restaurantedtos.ClienteFrecuenteDTO;
import itson.restaurantenegocio.ClientesFrecuentesBO;
import itson.restaurantenegocio.IClientesFrecuentesBO;
import itson.restaurantenegocio.NegocioException;
import itson.restaurantepersistencia.ClientesFrecuentesDAO;
import itson.restaurantepersistencia.IClientesFrecuentesDAO;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Carmen Andrea Lara Osuna
 */
public class ClientesFrecuentesControl {

    private final ClientesFrecuentesFORM clientesForm;
    private final IClientesFrecuentesBO clientesBO;

    public ClientesFrecuentesControl(ClientesFrecuentesFORM clientesForm) {
        IClientesFrecuentesDAO clientesDAO = new ClientesFrecuentesDAO();
        this.clientesBO = new ClientesFrecuentesBO();
        this.clientesForm = clientesForm;

        // Listener del botón Añadir Cliente
        clientesForm.getBtnAñadirCliente().addActionListener(e -> abrirRegistroCliente());

        // Listener del buscador
        clientesForm.getBuscadorClientesPanelFORM1().addBuscadorKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cargarTabla(clientesForm.getBuscadorClientesPanelFORM1().getTextoBusqueda());
            }
        });
        
        // Configurar el editor de la tabla con callback
        clientesForm.getTablaClientes().getColumnModel().getColumn(10)
                .setCellEditor(new ClientesFrecuentesFORM.BotonModificar(
                        new JCheckBox(),
                        clientesForm.getTablaClientes(), this
                ));
        cargarTabla("");
    }

    private void cargarTabla(String filtro) {
        try {
            List<ClienteFrecuenteDTO> listaClientes = clientesBO.buscarClientes(filtro);
            DefaultTableModel modelo = (DefaultTableModel) clientesForm.getTablaClientes().getModel();
            modelo.setRowCount(0);

            for (ClienteFrecuenteDTO cliente : listaClientes) {
                Object[] fila = {
                    cliente.getId(),
                    cliente.getNombre(),
                    cliente.getApellidoP(),
                    cliente.getApellidoM(),
                    cliente.getNumeroTelefono(),
                    cliente.getCorreo(),
                    cliente.getFechaRegistro(),
                    cliente.getPuntos(),
                    "Acciones"
                };
                modelo.addRow(fila);
            }
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(clientesForm, "Error al cargar clientes", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void actualizar(int row){
        abrirActualizarCliente(row);
    }

    private void abrirActualizarCliente(int row) {
        JTable tabla = clientesForm.getTablaClientes();

        Long idClic = Long.parseLong(tabla.getValueAt(row, 0).toString());
        String nombreClic = tabla.getValueAt(row, 1).toString();
        String apellidoPClic = tabla.getValueAt(row, 2).toString();
        String apellidoMClic = tabla.getValueAt(row, 3).toString();
        String telefonoClic = tabla.getValueAt(row, 4).toString();
        String correoClic = tabla.getValueAt(row, 5).toString();

        ClienteFrecuenteDTO clienteDTO = new ClienteFrecuenteDTO(idClic, nombreClic, apellidoPClic, apellidoMClic, telefonoClic, correoClic);

        ActualizarClientesFORM actualizar = new ActualizarClientesFORM(clienteDTO);
        actualizar.setVisible(true);

        ClienteFrecuente clienteActualizado = actualizar.getClienteActualizado();
        if (clienteActualizado != null) {
            actualizarFilaEditada(clienteActualizado);
        }
    }

    private void actualizarFilaEditada(ClienteFrecuente cliente) {
        DefaultTableModel modelo = (DefaultTableModel) clientesForm.getTablaClientes().getModel();
        for (int i = 0; i < modelo.getRowCount(); i++) {
            Long idEnTabla = Long.valueOf(modelo.getValueAt(i, 0).toString());
            if (idEnTabla.equals(cliente.getIdCliente())) {
                modelo.setValueAt(cliente.getNombre(), i, 1);
                modelo.setValueAt(cliente.getApellidoP(), i, 2);
                modelo.setValueAt(cliente.getApellidoM(), i, 3);
                modelo.setValueAt(cliente.getNumeroTelefono(), i, 4);
                modelo.setValueAt(cliente.getCorreo(), i, 5);
                modelo.fireTableRowsUpdated(i, i);
                break;
            }
        }
    }

    private void abrirRegistroCliente() {
        RegistroClientesFORM registroForm = new RegistroClientesFORM();
        registroForm.setVisible(true);
    }
}
