/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.restaurantepresentacion;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
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
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
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
        reporteClientes.getBtnLimpiar().addActionListener(e ->  limpiarClientes());
        reporteClientes.getBtnGenerarPDF().addActionListener(e -> generarPDFClientes());
        reporteClientes.getBtnGenerarReporte().addActionListener(e -> filtrarClientes());
                
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
        crearTablaComandas(null, null);
        
        reporteComandas.getBtnRegresar().addActionListener(e -> regresar());
        reporteComandas.getBtnReporteClientes().addActionListener(e -> abrirReporteClientes());
        reporteComandas.getBtnLimpiar().addActionListener(e -> limpiarComandas());
        reporteComandas.getBtnGenerarPDF().addActionListener(e -> generarPDFComandas());
        reporteComandas.getBtnGenerarReporte().addActionListener(e -> filtrarComandas());
    }
    
    /**
     * Método que filtra la tabla de clientes según el nombre y el mínimo de visitas
     */
    public void filtrarClientes() {
        String nombreBuscado = reporteClientes.getTxtNombre().getText().trim().toLowerCase();
        String seleccionVisitas = (String) reporteClientes.getCmbMinimoVisitas().getSelectedItem();
        
        int minimoVisitas = 0;
        if (seleccionVisitas != null && !seleccionVisitas.equals("Todas")) {
            minimoVisitas = Integer.parseInt(seleccionVisitas);
        }
        
        final int minVisitasFinal = minimoVisitas;

        RowFilter<DefaultTableModel, Integer> filtroPersonalizado = new RowFilter<DefaultTableModel, Integer>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                String nombreFila = entry.getStringValue(1).toLowerCase();
                int visitasFila = Integer.parseInt(entry.getStringValue(2));

                boolean coincideNombre = nombreBuscado.isEmpty() || nombreFila.contains(nombreBuscado);
                boolean coincideVisitas = visitasFila >= minVisitasFinal;

                return coincideNombre && coincideVisitas;
            }
        };
        buscadorClientes.setRowFilter(filtroPersonalizado);
    }
    
    /**
     * Método que lee las fechas del LGoodDatePicker y actualiza la tabla
     */
    public void filtrarComandas() {
        LocalDate fechaInicio = reporteComandas.getDtpFechaInicio().getDate(); 
        LocalDate fechaFin = reporteComandas.getDtpFechaFin().getDate();

        if (fechaInicio != null && fechaFin != null) {
            if (fechaInicio.isAfter(fechaFin)) {
                JOptionPane.showMessageDialog(reporteComandas, 
                    "La fecha de inicio no puede ser mayor a la fecha de fin.", 
                    "Fechas inválidas", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        crearTablaComandas(fechaInicio, fechaFin);
        obtenerVentaTotal(); 
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
            LOGGER.log(Level.SEVERE, "Error al cargar los clientes", ex);
            JOptionPane.showMessageDialog(reporteClientes, "No se pudieron cargar los clientes.", "Error", JOptionPane.ERROR_MESSAGE);
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
     * Método para manejar el botón de Generar PDF en Clientes.
     */
    public void generarPDFClientes() {
        JFileChooser fileChooser = new javax.swing.JFileChooser();
        fileChooser.setDialogTitle("Guardar Reporte de Clientes");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos PDF", "pdf"));

        if (fileChooser.showSaveDialog(reporteClientes) == JFileChooser.APPROVE_OPTION) {
            String ruta = fileChooser.getSelectedFile().getAbsolutePath();
            if (!ruta.toLowerCase().endsWith(".pdf")) ruta += ".pdf";

            try {
                String filtroNombre = reporteClientes.getTxtNombre().getText().isEmpty() ? "Todos" : reporteClientes.getTxtNombre().getText();
                String filtroVisitas = reporteClientes.getCmbMinimoVisitas().getSelectedItem() != null ? reporteClientes.getCmbMinimoVisitas().getSelectedItem().toString() : "0";
                String subtitulo = "Filtro por Nombre: " + filtroNombre + "\nMínimo de Visitas: " + filtroVisitas;
                
                exportarTablaAPDF(ruta, reporteClientes.getTablaClientes(), "Reporte de Clientes Frecuentes", subtitulo, "");
                
                JOptionPane.showMessageDialog(reporteClientes, "PDF generado con éxito en:\n" + ruta, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(reporteClientes, "Error al generar PDF: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                LOGGER.log(Level.SEVERE, "Error al generar PDF de clientes", e);
            }
        }
    }

    /**
     * Método para manejar el botón de Generar PDF en Comandas.
     */
    public void generarPDFComandas() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Reporte de Comandas");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos PDF", "pdf"));

        if (fileChooser.showSaveDialog(reporteComandas) == JFileChooser.APPROVE_OPTION) {
            String ruta = fileChooser.getSelectedFile().getAbsolutePath();
            if (!ruta.toLowerCase().endsWith(".pdf")) ruta += ".pdf";

            try {
                String subtitulo = "Periodo: " + reporteComandas.getDtpFechaInicio().getText() + " al " + reporteComandas.getDtpFechaFin().getText();
                String total = "Venta Total: " + reporteComandas.getLblVentaTotalCantidad().getText();
                
                exportarTablaAPDF(ruta, reporteComandas.getTablaComandas(), "Reporte de Comandas", subtitulo, total);
                
                JOptionPane.showMessageDialog(reporteComandas, "PDF generado con éxito en:\n" + ruta, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(reporteComandas, "Error al generar PDF: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                LOGGER.log(Level.SEVERE, "Error al generar PDF de comandas", e);
            }
        }
    }

    /**
     * Método de utilería privado para construir el PDF como tal.
     */
    private void exportarTablaAPDF(String rutaDestino, JTable tabla, String tituloReporte, String subtitulo, String piePagina) throws Exception {
        Document documento = new Document();
        PdfWriter.getInstance(documento, new FileOutputStream(rutaDestino));
        documento.open();

        Font fuenteTitulo = com.itextpdf.text.FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph titulo = new Paragraph(tituloReporte + "\n\n", fuenteTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        documento.add(titulo);

        if (subtitulo != null && !subtitulo.isEmpty()) {
            documento.add(new Paragraph(subtitulo + "\n\n"));
        }

        int columnas = tabla.getColumnCount();
        PdfPTable tablaPDF = new PdfPTable(columnas);
        tablaPDF.setWidthPercentage(100);

        Font fuenteCabecera = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        for (int i = 0; i < columnas; i++) {
            PdfPCell celda = new PdfPCell(new Phrase(tabla.getColumnName(i), fuenteCabecera));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            tablaPDF.addCell(celda);
        }

        for (int filas = 0; filas < tabla.getRowCount(); filas++) {
            for (int cols = 0; cols < columnas; cols++) {
                Object valor = tabla.getValueAt(filas, cols);
                tablaPDF.addCell(valor == null ? "" : valor.toString());
            }
        }
        documento.add(tablaPDF);

        if (piePagina != null && !piePagina.isEmpty()) {
            Font fuenteTotal = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            Paragraph total = new Paragraph("\n" + piePagina, fuenteTotal);
            total.setAlignment(Element.ALIGN_RIGHT);
            documento.add(total);
        }

        documento.close();
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
    public void crearTablaComandas(LocalDate inicio, LocalDate fin) {
        String[] columnas = { "Id", "Fecha y Hora", "Mesa", "Cliente", "Estado", "Total"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0){
          @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }  
        };
        
        try{
            List<Comanda> comandas = comandasBO.obtenerComandasParaReporte(inicio, fin);
            
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
            LOGGER.log(Level.SEVERE, "Error al buscar comandas", ex);
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
        reporteComandas.getDtpFechaInicio().clear();
        reporteComandas.getDtpFechaFin().clear();
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
