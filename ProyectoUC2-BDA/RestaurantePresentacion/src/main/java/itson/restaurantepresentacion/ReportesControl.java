/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.restaurantepresentacion;

import itson.restaurantedominio.Comanda;
import itson.restaurantedtos.ClienteFrecuenteDTO;
import itson.restaurantedtos.ComandaDTO;
import itson.restaurantenegocio.ClientesFrecuentesBO;
import itson.restaurantenegocio.ComandasBO;
import itson.restaurantenegocio.IClientesFrecuentesBO;
import itson.restaurantenegocio.IComandasBO;
import itson.restaurantenegocio.NegocioException;
import itson.restaurantepersistencia.ComandasDAO;
import itson.restaurantepersistencia.IComandasDAO;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Zaira
 */
public class ReportesControl {
    private TableRowSorter buscadorClientes;
    private ReporteClientesFORM reporteClientes;
    private ReporteComandasFORM reporteComandas;
    private final IClientesFrecuentesBO clientesBO;
    private final IComandasBO comandasBO;
    private static final Logger LOGGER = Logger.getLogger(ReportesControl.class.getName());
    

    /**
     * Constructor para el control de la forma de reporte de clientes
     * @param reporteClientes la forma de reporte de clientes
     */
    public ReportesControl(ReporteClientesFORM reporteClientes) {
        this.reporteClientes = reporteClientes;
        IComandasDAO comandasDAO = new ComandasDAO();
        this.clientesBO = new ClientesFrecuentesBO();
        this.comandasBO = new ComandasBO(comandasDAO);
        crearTablaClientes();
        numeroDeVisitas();
        
        reporteClientes.getBtnRegresar().addActionListener(e -> regresar());
        reporteClientes.getBtnReporteComandas().addActionListener(e -> abrirReporteComandas());
        reporteClientes.getCmbMinimoVisitas().addActionListener(e -> {
            String seleccionado = (String)  reporteClientes.getCmbMinimoVisitas().getSelectedItem();
            if (seleccionado == null || seleccionado.equals("Todas")) {
                buscadorClientes.setRowFilter(null);
            } else {
                buscadorClientes.setRowFilter(RowFilter.regexFilter("^" + seleccionado + "$", 2));
            }
        });
        
        reporteClientes.getBtnLimpiar().addActionListener(e ->  limpiarClientes());
                
    }

    /**
     * Constructor para el control de la forma de reporte de comandas
     * @param reporteComandas la forma de reporte de comandas
     */
    public ReportesControl(ReporteComandasFORM reporteComandas) {
        this.reporteComandas = reporteComandas;
        IComandasDAO comandasDAO = new ComandasDAO();
        this.clientesBO = new ClientesFrecuentesBO();
        this.comandasBO = new ComandasBO(comandasDAO);
        crearTablaComandas();
        
        reporteComandas.getBtnRegresar().addActionListener(e -> regresar());
        reporteComandas.getBtnReporteClientes().addActionListener(e -> abrirReporteClientes());
        reporteComandas.getBtnLimpiar().addActionListener(e -> limpiarComandas());
    }
    
    /**
     * Método para generar la tabla de Clientes
     */
    public void crearTablaClientes(){
        String[] columnas = { "Id", "Nombre", "Visitas", "Total Gastado"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0){
          @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }  
        };
        
        try {
            List<ClienteFrecuenteDTO> clientes = clientesBO.buscarClientes(null);
            for (ClienteFrecuenteDTO c : clientes) {
                String nombre = c.getNombre() + " " + c.getApellidoP() + " " + c.getApellidoM();
                Object[] fila = {
                    c.getId(),
                    nombre,
                    c.getVisitas(),
                    c.getTotalGastado()
                };
                modelo.addRow(fila);
            }
        } catch (NegocioException ex){
            //todo
        }
        
        reporteClientes.getTablaClientes().setModel(modelo);
        reporteClientes.getTablaClientes().getColumnModel().getColumn(0).setMinWidth(0);
        reporteClientes.getTablaClientes().getColumnModel().getColumn(0).setPreferredWidth(0);
        reporteClientes.getTablaClientes().getColumnModel().getColumn(0).setMaxWidth(0);
        reporteClientes.getTablaClientes().getColumnModel().getColumn(0).setResizable(false);
        
        buscadorClientes = new TableRowSorter<>(modelo);
        reporteClientes.getTablaClientes().setRowSorter(buscadorClientes);
        
        
    }
      
    /**
     * Método que obtiene y separa los números de visitas para llenar
     * el combobox de máximo de visitas.
     */
    public void numeroDeVisitas(){
        Set<Integer> numeronDeVisitas = new TreeSet<>();
        DefaultTableModel modelo = (DefaultTableModel) reporteClientes.getTablaClientes().getModel();
        int columnaVisitas = 2; 

        for (int i = 0; i < modelo.getRowCount(); i++) {
            Object valor = modelo.getValueAt(i, columnaVisitas);
            if (valor != null) {
                try {
                    numeronDeVisitas.add(Integer.parseInt(valor.toString()));
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(
                            reporteClientes, 
                            "El número de visitas debe ser numérica.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        }
        reporteClientes.getCmbMinimoVisitas().removeAllItems();
        reporteClientes.getCmbMinimoVisitas().addItem("Todas");

        for (Integer num : numeronDeVisitas) {
            reporteClientes.getCmbMinimoVisitas().addItem(num.toString());
        }
    
    }
    
    
     /**
     * Método que abre el reporte de comandas.
     */
    public void abrirReporteComandas(){
        ReporteComandasFORM comandas = new ReporteComandasFORM();
        ReportesControl control = new ReportesControl(comandas);
        comandas.setLocationRelativeTo(null);
        comandas.setVisible(true);
        reporteClientes.dispose();
    }
    
    /**
     * Método para limpiar los campos del reporte de Clientes
     */
    public void limpiarClientes(){
        reporteClientes.getTxtNombre().setText("");
        reporteClientes.getCmbMinimoVisitas().setSelectedIndex(0);
    }

    
   /**
    * AQUI EN ADELANTE SON MÉTODOS PARA LA FORMA DE COMANDAS:
    */
    
        
    /**
     * Método para generar la tabla de Comandas
     */
    public void crearTablaComandas(){
        String[] columnas = { "Id", "Fecha y Hora", "Mesa", "Cliente", "Estado", "Total"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0){
          @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }  
        };
        
        try{
            List<Comanda> comandas = comandasBO.obtenerComandasParaReporte(null, null);
            for (Comanda c : comandas) {
                String cliente = c.getId() + "." + c.getCliente().getNombre() + " " + c.getCliente().getApellidoP();
                Object[] fila = {
                    c.getId(),
                    c.getFechaHora(),
                    c.getMesa(),
                    cliente,
                    c.getEstado(),
                    c.getTotal()
                };
                modelo.addRow(fila);
            }
            
        } catch (NegocioException ex) {
            //todo
        }
        
        reporteComandas.getTablaComandas().setModel(modelo);
        reporteComandas.getTablaComandas().getColumnModel().getColumn(0).setMinWidth(0);
        reporteComandas.getTablaComandas().getColumnModel().getColumn(0).setPreferredWidth(0);
        reporteComandas.getTablaComandas().getColumnModel().getColumn(0).setMaxWidth(0);
        reporteComandas.getTablaComandas().getColumnModel().getColumn(0).setResizable(false);
    }

    /**
     * Método que abre el reporte de clientes.
     */
    public void abrirReporteClientes(){
        ReporteClientesFORM clientes = new ReporteClientesFORM();
        ReportesControl control = new ReportesControl(clientes);
        clientes.setLocationRelativeTo(null);
        clientes.setVisible(true);
        reporteComandas.dispose();
    }
    
    /**
     * Método para limpiar los campos del reporte de Comandas
     */
    public void limpiarComandas(){
        reporteComandas.getTxtFechaInicio().setText("");
        reporteComandas.getTxtFechaFin().setText("");
    }
    
    /**
     * Método que calcula la venta total
     */
    public void obtenerVentaTotal(){
        Double total = 0.0;
        DefaultTableModel modelo = (DefaultTableModel) reporteComandas.getTablaComandas().getModel();
        int columnaVentas = 5; 

        for (int i = 0; i < modelo.getRowCount(); i++) {
            Object valor = modelo.getValueAt(i, columnaVentas);
            if (valor != null) {
                try {
                    total += (Double) valor;
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(
                            reporteClientes, 
                            "La cantidad total debe ser numérica.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        }
        
        reporteComandas.getLblVentaTotalCantidad().setText("$" + total);
    }
    
    //--------------------------------------------------------------

    /**
     * Método General que permite que al presionar el botón de regresar
     * esta se cierra y regresa al menú del administrador.
     */
    public void regresar(){
        MenuAdminFORM menu = new MenuAdminFORM();
        MenuAdministradorControl controlAdmin = new MenuAdministradorControl(menu);
        menu.setLocationRelativeTo(null);
        menu.setVisible(true);
        if (reporteComandas != null && reporteComandas.isVisible()) {
            reporteComandas.dispose();
        } else if (reporteClientes != null && reporteClientes.isVisible()) {
            reporteClientes.dispose();
        }
    }
}
