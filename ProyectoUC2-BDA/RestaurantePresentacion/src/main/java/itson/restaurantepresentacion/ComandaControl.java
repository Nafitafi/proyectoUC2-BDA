/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.restaurantepresentacion;

/**
 *
 * @author Carmen Andrea Lara Osuna
 */
import itson.restaurantedominio.Comanda;
import itson.restaurantedominio.DetalleComanda;
import itson.restaurantedominio.Mesa;
import itson.restaurantedominio.Producto;
import itson.restaurantedtos.ClienteFrecuenteDTO;
import itson.restaurantedtos.ComandaDTO;
import itson.restaurantedtos.DetalleComandaDTO;
import itson.restaurantenegocio.ClientesFrecuentesBO;
import itson.restaurantenegocio.ComandasBO;
import itson.restaurantenegocio.IClientesFrecuentesBO;
import itson.restaurantenegocio.IComandasBO;
import itson.restaurantenegocio.IProductosBO;
import itson.restaurantenegocio.NegocioException;
import itson.restaurantenegocio.ProductosBO;
import itson.restaurantepersistencia.ClientesFrecuentesDAO;
import itson.restaurantepersistencia.ComandasDAO;
import itson.restaurantepersistencia.IClientesFrecuentesDAO;
import itson.restaurantepersistencia.IComandasDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.logging.Logger;

public class ComandaControl {

    private final ComandasFORM comandaForm;
    private final IClientesFrecuentesBO clientesBO;
    private final IComandasBO comandasBO;
    private final IProductosBO productosBO;
    
    private static final Logger LOGGER = Logger.getLogger(ComandaControl.class.getName());

    /**
     * Inicializa el controlador, configura listeners y carga combos/tablas.
     *
     * @param comandaForm formulario principal de comandas
     * @throws NegocioException si falla la carga inicial
     */
    public ComandaControl(ComandasFORM comandaForm) throws NegocioException {
        IClientesFrecuentesDAO clientesDAO = new ClientesFrecuentesDAO();
        IComandasDAO comandasDAO = new ComandasDAO();

        this.clientesBO = new ClientesFrecuentesBO();
        this.comandasBO = new ComandasBO(comandasDAO);
        this.productosBO = new ProductosBO();

        this.comandaForm = comandaForm;

        comandaForm.getBtnAgregarProducto().addActionListener(e -> agregarProducto());
        comandaForm.getBtnEnviarComanda().addActionListener(e -> enviarComanda());
        comandaForm.getBtnCancelar().addActionListener(e -> cancelarComanda());
        comandaForm.getBtnLimpiar().addActionListener(e -> limpiarFormulario());
        comandaForm.getBtnRegresar().addActionListener(e -> regresarMenu());

        cargarClientes();
        cargarMesasDisponibles();
        inicializarTabla();
    }

    /**
     * Carga clientes frecuentes en el combo, incluyendo "Cliente General" como
     * default.
     *
     * @throws NegocioException si falla la consulta
     */
    private void cargarClientes() throws NegocioException {
        List<ClienteFrecuenteDTO> clientes = clientesBO.buscarClientes(null);
        DefaultComboBoxModel<ClienteFrecuenteDTO> modelo = new DefaultComboBoxModel<>();
        modelo.addElement(new ClienteFrecuenteDTO(0L, "Cliente General", "", "", "", ""));
        for (ClienteFrecuenteDTO c : clientes) {
            modelo.addElement(c);
        }
        comandaForm.getCboClientes().setModel(modelo);
    }

    /**
     * Carga mesas disponibles en el combo.
     *
     * @throws NegocioException si falla la consulta
     */
    private void cargarMesasDisponibles() throws NegocioException {
        List<Mesa> mesas = comandasBO.obtenerMesasDisponibles();
        DefaultComboBoxModel<Mesa> modelo = new DefaultComboBoxModel<>();
        modelo.addElement(new Mesa(0L, 0));
        for (Mesa m : mesas) {
            modelo.addElement(m);
        }
        comandaForm.getCboMesas().setModel(modelo);
    }

    /**
     * Inicializa la tabla de detalles vacía.
     */
    private void inicializarTabla() {
        DefaultTableModel modelo = new DefaultTableModel(new Object[]{"Producto", "Comentario", "Cantidad"}, 0);
        comandaForm.getTblDetalles().setModel(modelo);
    }

    /**
     * Agrega producto seleccionado con comentario y cantidad a la tabla.
     */
    private void agregarProducto() {
        Producto producto = (Producto) comandaForm.getCboProductos().getSelectedItem();
        String comentario = comandaForm.getTxtComentarios().getText();
        int cantidad = (int) comandaForm.getSpnCantidad().getValue(); // ahora viene del spinner

        DefaultTableModel modelo = (DefaultTableModel) comandaForm.getTblDetalles().getModel();
        modelo.addRow(new Object[]{producto, comentario, cantidad});
    }

    /**
     * Envía la comanda capturada, delega a la BO y abre confirmación.
     */
    private void enviarComanda() {
        try {
            Long idMesa = ((Mesa) comandaForm.getCboMesas().getSelectedItem()).getId();
            Long idCliente = ((ClienteFrecuenteDTO) comandaForm.getCboClientes().getSelectedItem()).getId();
            List<DetalleComandaDTO> detalles = convertirTablaADetalles(
                    (DefaultTableModel) comandaForm.getTblDetalles().getModel());

            ComandaDTO comandaDTO = new ComandaDTO(idMesa, idCliente, detalles);
            Comanda comandaGuardada = comandasBO.guardar(comandaDTO);

            ComandaEnviadaFORM confirmacionForm = new ComandaEnviadaFORM();
            cargarDatosConfirmacion(confirmacionForm, comandaGuardada);
            confirmacionForm.setLocationRelativeTo(null);
            confirmacionForm.setVisible(true);

            comandaForm.dispose();

        } catch (NegocioException ex) {
            LOGGER.severe(ex.getMessage());
            JOptionPane.showMessageDialog(comandaForm, "Error al enviar comanda: " + ex.getMessage());
        }
    }

    /**
     * Llena el formulario de confirmación con folio, mesa, cliente, total y
     * productos.
     *
     * @param confirmacionForm formulario de confirmación
     * @param comanda comanda registrada
     */
    private void cargarDatosConfirmacion(ComandaEnviadaFORM confirmacionForm, Comanda comanda) throws NegocioException {
        confirmacionForm.getLblFolio().setText(comanda.getFolio());
        confirmacionForm.getLblMesa().setText(String.valueOf(comanda.getMesa().getNumero()));

        try {
            ClienteFrecuenteDTO clienteDTO = clientesBO.obtenerClientePorId(comanda.getCliente().getIdCliente());
            if (clienteDTO != null && comanda.getCliente().getIdCliente() != 0) {
                confirmacionForm.getLblCliente().setText(clienteDTO.getNombre() + " " + clienteDTO.getApellidoP());
            } else {
                confirmacionForm.getLblCliente().setText("Cliente General");
            }
        } catch (NegocioException ex) {
            confirmacionForm.getLblCliente().setText("Error al cargar cliente");
        }

        confirmacionForm.getLblTotal().setText(String.format("$%,.2f", comanda.getTotal()));

        DefaultTableModel modelo = new DefaultTableModel(new Object[]{"Producto", "Comentario", "Cantidad"}, 0);
        for (DetalleComanda d : comanda.getDetalles()) {
            Producto producto = productosBO.obtenerProductoPorId(d.getProducto().getId());
            String nombreProducto = (producto != null) ? producto.getNombre() : "Producto desconocido";
            modelo.addRow(new Object[]{nombreProducto, d.getComentario(), d.getCantidad()});
        }
        confirmacionForm.getTblComandaEnviada().setModel(modelo);

        confirmacionForm.getBtnRegresar().addActionListener(e -> regresarMenu());
    }

    /**
     * Convierte filas de la tabla en objetos DetalleComandaDTO.
     *
     * @param modelo modelo de la tabla
     * @return lista de detalles
     */
    private List<DetalleComandaDTO> convertirTablaADetalles(DefaultTableModel modelo) {
        List<DetalleComandaDTO> detalles = new java.util.ArrayList<>();
        for (int i = 0; i < modelo.getRowCount(); i++) {
            Producto producto = (Producto) modelo.getValueAt(i, 0);
            String comentario = (String) modelo.getValueAt(i, 1);
            int cantidad = (int) modelo.getValueAt(i, 2);
            detalles.add(new DetalleComandaDTO(producto.getIdProducto(), cantidad, comentario));
        }
        return detalles;
    }

    /**
     * Cancela la comanda y cierra el formulario.
     */
    private void cancelarComanda() {
        comandaForm.dispose();
    }

    /**
     * Limpia la tabla y comentarios del formulario.
     */
    private void limpiarFormulario() {
        inicializarTabla();
        comandaForm.getTxtComentarios().setText("");
    }

    /**
     * Regresa al menú principal y cierra el formulario actual.
     */
    private void regresarMenu() {
        MenuPrincipalForm menu = new MenuPrincipalForm();
        menu.setLocationRelativeTo(null);
        menu.setVisible(true);
        comandaForm.dispose();
    }
}
