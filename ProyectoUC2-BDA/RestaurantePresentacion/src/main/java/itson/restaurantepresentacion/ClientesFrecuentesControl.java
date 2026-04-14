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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Clase de control para las formas de clientes frecuentes; consulta, registro y
 * actualización.
 *
 * @author Andrea Lara, Nahomi Figueroa, Zaira Barajas
 */
public class ClientesFrecuentesControl {

    private final ClientesFrecuentesFORM clientesForm;
    private final IClientesFrecuentesBO clientesBO;

    public ClientesFrecuentesControl(ClientesFrecuentesFORM clientesForm) {
        this.clientesBO = new ClientesFrecuentesBO();
        this.clientesForm = clientesForm;
        this.clientesForm.setControl(this);
        clientesForm.getBtnAñadirCliente().addActionListener(e -> abrirRegistroCliente());
        clientesForm.getTablaClientes().getColumnModel().getColumn(5)
                .setCellEditor(new ClientesFrecuentesFORM.BotonModificar(
                        new JCheckBox(),
                        clientesForm.getTablaClientes(), this
                ));
        cargarTabla("");
        clientesForm.getBtnInventario().addActionListener(e -> abrirIngredientes());
        clientesForm.getBtnPlatillos().addActionListener(e -> abrirProductos());
        clientesForm.getBtnReporteClientes().addActionListener(e -> abrirReportesClientes());
        clientesForm.getBtnReporteComandas().addActionListener(e -> abrirReportesComandas());
        clientesForm.getBtnMenu().addActionListener(e -> regresar());
    }

    /**
     * Método encargado de llenar la tabla de clientes tanto con un filtro como
     * sin él.
     *
     * @param filtro búsqueda deseada en la lista de clientes.
     */
    public void cargarTabla(String filtro) {
        try {
            List<ClienteFrecuenteDTO> listaClientes = clientesBO.buscarClientes(filtro);
            DefaultTableModel modelo = (DefaultTableModel) clientesForm.getTablaClientes().getModel();
            modelo.setRowCount(0);

            for (ClienteFrecuenteDTO cliente : listaClientes) {
                String apellidoM = (cliente.getApellidoM() != null) ? " " + cliente.getApellidoM() : "";
                String nombreCompleto = cliente.getNombre() + " " + cliente.getApellidoP() + apellidoM;

                Object[] fila = {
                    cliente.getId(),
                    nombreCompleto,
                    cliente.getNumeroTelefono(),
                    cliente.getPuntos(),
                    cliente.getTotalGastado(),
                    "Modificar"
                };
                modelo.addRow(fila);
            }
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(clientesForm, "Error al cargar clientes", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método que llama a la ventana para actualizar un cliente.
     *
     * @param row el número de fila del cliente seleccionado
     */
    public void actualizar(int row) {
        abrirActualizarCliente(row);
    }

    /**
     * Método privado encargado de recibir de la tabla de clientes la
     * información del cliente seleccionado para permitir que el usuario la
     * actualice en una ventana nueva.
     *
     * @param row el número de fila del cliente seleccionado
     */
    private void abrirActualizarCliente(int row) {
        try {
            JTable tabla = clientesForm.getTablaClientes();
            Long idSeleccionado = Long.parseLong(tabla.getValueAt(row, 0).toString());
            ClienteFrecuenteDTO clienteDTO = clientesBO.obtenerClientePorId(idSeleccionado);
            if (clienteDTO != null) {
                ActualizarClientesFORM actualizar = new ActualizarClientesFORM(clienteDTO);
                accionAlCerrar(actualizar, clientesForm);
                refrescarTablaAlCerrar(actualizar);
                actualizar.setVisible(true);

                ClienteFrecuente clienteActualizado = actualizar.getClienteActualizado();
                if (clienteActualizado != null) {
                    actualizarFilaEditada(clienteActualizado);
                }
            }
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(clientesForm, "Error al consultar el cliente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método utilizado por abrirActualizarCliente() que se encarga de
     * actualizar la fila en la que se encuentra el cliente que fue actualizado.
     *
     * @param cliente objeto ClienteFrecuente con la información actualizada.
     */
    private void actualizarFilaEditada(ClienteFrecuente cliente) {
        DefaultTableModel modelo = (DefaultTableModel) clientesForm.getTablaClientes().getModel();

        for (int i = 0; i < modelo.getRowCount(); i++) {
            Long idEnTabla = Long.valueOf(modelo.getValueAt(i, 0).toString());

            if (idEnTabla.equals(cliente.getIdCliente())) {
                String apellidoM = (cliente.getApellidoM() != null) ? " " + cliente.getApellidoM() : "";
                String nombreCompleto = cliente.getNombre() + " " + cliente.getApellidoP() + apellidoM;

                modelo.setValueAt(nombreCompleto, i, 1);
                modelo.setValueAt(cliente.getNumeroTelefono(), i, 2);

                modelo.fireTableRowsUpdated(i, i);
                break;
            }
        }
    }

    /**
     * Método privado para abrir la ventana de registro de clientes
     */
    private void abrirRegistroCliente() {
        RegistroClientesFORM registroForm = new RegistroClientesFORM();
        accionAlCerrar(registroForm, clientesForm);
        refrescarTablaAlCerrar(registroForm);
        registroForm.setVisible(true);
        clientesForm.dispose();
    }

    /**
     * Método estático para abrir el formulario ClientesFrecuentesFORM desde
     * cualquier otra ventana.
     *
     * * @param ventanaAnterior (Opcional) El JFrame desde donde se está
     * llamando. Se usa para cerrarlo. Se pasa null si no se desea mantener la
     * otra ventana abierta.
     */
    public static void abrirClientesFrecuentes(javax.swing.JFrame ventanaAnterior) {
        ClientesFrecuentesFORM clientesForm = new ClientesFrecuentesFORM();
        ClientesFrecuentesControl control = new ClientesFrecuentesControl(clientesForm);
        clientesForm.setLocationRelativeTo(null);
        clientesForm.setVisible(true);
        if (ventanaAnterior != null) {
            ventanaAnterior.dispose();
        }
    }

    public static void accionAlCerrar(JFrame ventanaActual, JFrame ventanaPrincipal) {
        ventanaActual.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ventanaPrincipal.setVisible(true);
            }
        });
    }

    /**
     * Método para refrescar la tabla al cerrar un formulario secundario.
     *
     * @param ventanaSecundaria JFrame del formulario que se abre
     */
    private void refrescarTablaAlCerrar(JFrame ventanaSecundaria) {
        ventanaSecundaria.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                // Recarga toda la tabla
                cargarTabla("");
            }
        });
    }
    
    /**
     * Método para abrir la pantalla de productos.
     */
    private void abrirProductos() {
        ProductosFORM productosForm = new ProductosFORM();
        productosForm.setLocationRelativeTo(null);
        productosForm.setVisible(true);
        clientesForm.dispose();
    }

    /**
     * Método para abrir el inventario de ingredientes
     */
    private void abrirIngredientes() {
        InventarioIngredientesFORM ingredientesForm = new InventarioIngredientesFORM();
        new IngredientesControl(ingredientesForm);
        ingredientesForm.setLocationRelativeTo(null);
        ingredientesForm.setVisible(true);
        clientesForm.dispose();
    }

    /**
     * Método para abrir la forma de clientes
     */
    private void abrirClientes() {
        ClientesFrecuentesFORM clientesForm = new ClientesFrecuentesFORM();
        new ClientesFrecuentesControl(clientesForm);
        clientesForm.setLocationRelativeTo(null);
        clientesForm.setVisible(true);
        clientesForm.dispose();
    }

    /**
     * Método para abrir el reporte de clientes
     */
    private void abrirReportesClientes() {
        ReporteClientesFORM reportesForm = new ReporteClientesFORM();
        new ReportesControl(reportesForm);
        reportesForm.setLocationRelativeTo(null);
        reportesForm.setVisible(true);
        clientesForm.dispose();
    }

    /**
     * Método para abrir el reporte de comandas
     */
    private void abrirReportesComandas() {
        ReporteComandasFORM reportesForm = new ReporteComandasFORM();
        new ReportesControl(reportesForm);
        reportesForm.setLocationRelativeTo(null);
        reportesForm.setVisible(true);
        clientesForm.dispose();
    }

    /**
     * Método para regresar al inicio
     */
    private void regresar() {
        MenuAdminFORM menu = new MenuAdminFORM();
        MenuAdministradorControl controlAdmin = new MenuAdministradorControl(menu);
        menu.setLocationRelativeTo(null);
        menu.setVisible(true);
        clientesForm.dispose();
    }
}
