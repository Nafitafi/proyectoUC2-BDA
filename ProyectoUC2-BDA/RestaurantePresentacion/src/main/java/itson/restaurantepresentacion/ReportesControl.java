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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Zaira
 */
public class ReportesControl {
    private ReporteClientesFORM reporteClientes;
    private ReporteComandasFORM reporteComandas;
    private final IClientesFrecuentesBO clientesBO;
    private final IComandasBO comandasBO;
    private static final Logger LOGGER = Logger.getLogger(ReportesControl.class.getName());
    

    public ReportesControl(ReporteClientesFORM reporteClientes) {
        this.reporteClientes = reporteClientes;
        IComandasDAO comandasDAO = new ComandasDAO();
        this.clientesBO = new ClientesFrecuentesBO();
        this.comandasBO = new ComandasBO(comandasDAO);
        crearTablaClientes();
        
        reporteClientes.getBtnRegresar().addActionListener(e -> regresar());
        reporteClientes.getBtnReporteComandas().addActionListener(e -> abrirReporteComandas());
                
    }

    public ReportesControl(ReporteComandasFORM reporteComandas) {
        this.reporteComandas = reporteComandas;
        IComandasDAO comandasDAO = new ComandasDAO();
        this.clientesBO = new ClientesFrecuentesBO();
        this.comandasBO = new ComandasBO(comandasDAO);
        crearTablaComandas();
        
        reporteComandas.getBtnRegresar().addActionListener(e -> regresar());
        reporteComandas.getBtnReporteClientes().addActionListener(e -> abrirReporteClientes());
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
        
    }
      
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
            List<Comanda> comandas = comandasBO.buscarPorRangoFechas(null,null);
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
    }

    /**
     * Método que permite que al presionar el botón de regresar
     * esta se cierra y regresa al menú del administrador.
     */
    public void regresar(){
        MenuAdminFORM menu = new MenuAdminFORM();
        menu.setLocationRelativeTo(null);
        menu.setVisible(true);
        if (this.equals(reporteComandas)){
            reporteComandas.dispose();
        } else {
            reporteClientes.dispose();
        }
    }
    
    /**
     * Método que abre el reporte de comandas.
     */
    public void abrirReporteComandas(){
        ReporteComandasFORM comandas = new ReporteComandasFORM();
        comandas.setLocationRelativeTo(null);
        comandas.setVisible(true);
        reporteClientes.dispose();
    }
    
    /**
     * Método que abre el reporte de clientes.
     */
    public void abrirReporteClientes(){
        ReporteClientesFORM clientes = new ReporteClientesFORM();
        clientes.setLocationRelativeTo(null);
        clientes.setVisible(true);
        reporteComandas.dispose();
    }
}
