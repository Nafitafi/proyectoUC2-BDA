/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package itson.restaurantepresentacion;

import itson.restaurantedominio.Ingrediente;
import itson.restaurantedtos.DetallesRecetaDTO;
import itson.restaurantedtos.IngredienteActualizadoDTO;
import itson.restaurantedtos.ProductoActualizadoDTO;
import itson.restaurantedtos.UnidadMedida;
import itson.restaurantenegocio.IIngredientesBO;
import itson.restaurantenegocio.IngredientesBO;
import itson.restaurantenegocio.NegocioException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 * JDialog auxiliar que sirve como validador de productos establecidos con receta
 * o como buscador de ingredientes para productos nuevos sin receta.
 * @author Zaira
 */
public class BuscadorInventarioJDialog extends javax.swing.JDialog {
    IIngredientesBO ingredientesBO;
    DefaultTableModel modelo;
    ProductoActualizadoDTO producto;
    TableRowSorter<DefaultTableModel> buscador;
    boolean suficiente = false;
    private static final Logger LOGGER = Logger.getLogger(BuscadorInventarioJDialog.class.getName());
    
    
    
    /**
     * Constructor para el JDialog del buscador de inventario
     * @param parent ventana o frame que invoca este diálogo.
     * @param modal define si el diálogo bloquea a la ventana padre.
     * @param producto objeto de ProductoDTO que va a validar si sus ingredientes están en
     * existencia. null en caso de que se desee utilizar el buscador por su cuenta.
     */
    public BuscadorInventarioJDialog(java.awt.Frame parent, boolean modal, ProductoActualizadoDTO producto) {
        super(parent, modal);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initComponents();
        ingredientesBO = new IngredientesBO();
        this.producto = producto;
        cmbColumna.addItem("Nombre");
        cmbColumna.addItem("Unidad");
        cmbColumna.addItem("Stock");
        crearTabla();
        
        modelo = (DefaultTableModel) tablaIngredientes.getModel();
        buscador = new TableRowSorter<>(modelo);
        tablaIngredientes.setRowSorter(buscador);
        
        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filtrar(); }
            public void removeUpdate(DocumentEvent e) { filtrar(); }
            public void changedUpdate(DocumentEvent e) { filtrar(); }
        
            
        });
        
        cmbColumna.addActionListener(e -> filtrar());  
        
        
        
        tablaIngredientes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int fila = tablaIngredientes.getSelectedRow();
                    if (fila != -1) {
                        int modeloFila = tablaIngredientes.convertRowIndexToModel(fila);
                        //todo
                    }
                }
            }
        });
        
        setVisible(true);
    }

    /**
     * Método que crea una tabla a partir de la receta de un platillo y muestra 
     * los datos de los ingredientes, la cantidad necesaria por la receta y el estado en el que
     * se encuentran los ingredientes al comparar el stock actual con la cantidad necesaria.
     * 
     * En caso de que el platillo no tenga receta, significa que es nuevo por lo que el buscador
     * muestra los ingredientes de forma singular.
     */
    public void crearTabla(){
        if (producto != null && producto.getDetallesReceta()!= null){
            //PARA VALIDAR UN PRODUCTO CON RECETA YA ESTABLECIDA
            String[] columnas = {"Id", "Nombre", "Unidad", "Stock Actual", "Cantidad Necesaria", "Estado"};

            modelo = new DefaultTableModel(columnas, 0){
                @Override
                  public boolean isCellEditable(int row, int column) {
                      return false;
                  }  
             };

            for (DetallesRecetaDTO r: producto.getDetallesReceta()){
                Long idIngrediente = r.getIdIngrediente();
                try {
                    Ingrediente ing = ingredientesBO.buscar(idIngrediente);

                    double stockActual = ing.getStock();
                    double stockNecesario = r.getCantidad();

                    String estado = (stockActual>=stockNecesario) ? "Disponible" : "Insuficiente";

                    Object[] fila = {
                        ing.getIdIngrediente(),
                        ing.getNombre(),
                        ing.getUnidadMedida(),
                        stockActual,
                        stockNecesario,
                        estado
                    };
                    modelo.addRow(fila);
                } catch (NegocioException ex) {
                    LOGGER.severe(ex.getMessage());
                    JOptionPane.showMessageDialog(
                        this, 
                        "Error al acceder a los ingredientes.", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE
                );
                }
            }
            
            tablaIngredientes.setModel(modelo);
            
            tablaIngredientes.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {

                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                    int modeloFila = table.convertRowIndexToModel(row);
                    String estado = table.getModel().getValueAt(modeloFila, 5).toString();

                    if (estado.equalsIgnoreCase("insuficiente")){
                        c.setForeground(Color.RED);
                        c.setFont(c.getFont().deriveFont(Font.BOLD));
                    } else {
                        c.setForeground(new Color(0, 150, 0));
                        c.setFont(c.getFont().deriveFont(Font.PLAIN));
                    }

                    if (isSelected) {
                        c.setBackground(table.getSelectionBackground());
                        c.setForeground(table.getSelectionForeground());
                    } else {
                        c.setBackground(Color.WHITE);
                    }

                    return c;
                    }
            });
            
        } else {
            //BUSCADOR DE INGREDIENTES PARA PRODUCTOS NUEVOS
            try {
                String[] columnas = {"Id", "Nombre", "Unidad", "Stock Actual"};
                modelo = new DefaultTableModel(columnas, 0){
                    @Override
                      public boolean isCellEditable(int row, int column) {
                          return false;
                      }  
                };
                
                List<Ingrediente> ingredientes = ingredientesBO.recuperarIngredientes();
                for (Ingrediente ingrediente: ingredientes){
                    Object[] fila = {
                        ingrediente.getIdIngrediente(),
                        ingrediente.getNombre(),
                        ingrediente.getUnidadMedida(),
                        ingrediente.getStock()
                    };
                
                    modelo.addRow(fila);
                }
                
                tablaIngredientes.setModel(modelo);
                
            } catch (NegocioException ex) {
                LOGGER.severe(ex.getMessage());
                JOptionPane.showMessageDialog(
                        this, 
                        "Error al recuperar ingredientes", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
            } 
        }

        
        
    }
    
    /**
     * Método que toma el valor de la selección del combo box y el texto de búsqueda
     * para filtrar los ingredientes de la tabla.
     */
    public void filtrar(){
        String busqueda = txtBuscar.getText();
        int columnaAFiltrar = 1;

        if (cmbColumna.getSelectedIndex() == 1){
            columnaAFiltrar = 2;
        }

        if (cmbColumna.getSelectedIndex() == 2){
            columnaAFiltrar = 3;
        }

        if (busqueda.trim().length() == 0) {
            buscador.setRowFilter(null);
        } else {
            buscador.setRowFilter(RowFilter.regexFilter("(?i)" + busqueda, columnaAFiltrar));
        }

    }
    
    /**
     * Método que va a regresar los ingredientes ya validados de un producto
     * con una receta ya establecida
     * @return la lista de objetos Receta validada
     */
    public List<DetallesRecetaDTO> getIngredientesValidados() {
        return suficiente ? producto.getDetallesReceta(): null;
    }
    
    /**
     * Verifica si hay productos suficientes en el inventario para cumplir
     * con las necesidades de la receta del producto
     * @return 
     */
    private boolean hayStockSuficiente(){
        try {
            for (DetallesRecetaDTO r: producto.getDetallesReceta()){
                    Ingrediente ingrediente = ingredientesBO.buscar(r.getIdIngrediente());
                    if (ingrediente.getStock() < r.getCantidad()){
                        return false;
                    }

            }
        } catch (NegocioException ex){
            LOGGER.severe(ex.getMessage());
            JOptionPane.showMessageDialog(
                    this, 
                    "Error al acceder a los ingredientes.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE
            );
        }
        
        return true;
    }
    
    /**
     * Método que regresa el ingrediente individual seleccionado de la tabla
     * @return ingredienteDTO seleccionado
     */
    public IngredienteActualizadoDTO getIngredienteSeleccionado(){
        int fila = tablaIngredientes.getSelectedRow();
            if (fila != -1) {
                int modeloFila = tablaIngredientes.convertRowIndexToModel(fila);
                Long idIngrediente = Long.valueOf(tablaIngredientes.getValueAt(modeloFila, 0).toString());
                String nombre = tablaIngredientes.getValueAt(modeloFila, 1).toString();
                UnidadMedida unidad = (UnidadMedida) tablaIngredientes.getValueAt(modeloFila, 2);
                double stock = Double.valueOf(tablaIngredientes.getValueAt(modeloFila, 3).toString());
                
                IngredienteActualizadoDTO ingredienteNuevo = new IngredienteActualizadoDTO(
                        idIngrediente,
                        nombre,
                        unidad,
                        stock
                );
                
                 return ingredienteNuevo;
            }
            
        return null;
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlBuscador = new javax.swing.JPanel();
        lblBuscarInventario = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        scrollTabla = new javax.swing.JScrollPane();
        tablaIngredientes = new javax.swing.JTable();
        cmbColumna = new javax.swing.JComboBox<>();
        btnAceptar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Buscador de Ingredientes");

        pnlBuscador.setBackground(new java.awt.Color(19, 70, 134));

        lblBuscarInventario.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblBuscarInventario.setForeground(new java.awt.Color(255, 255, 255));
        lblBuscarInventario.setText("Buscar en el inventario:");

        txtBuscar.setBackground(new java.awt.Color(255, 255, 255));
        txtBuscar.setForeground(new java.awt.Color(51, 51, 51));

        scrollTabla.setBackground(new java.awt.Color(255, 255, 255));
        scrollTabla.setForeground(new java.awt.Color(51, 51, 51));

        tablaIngredientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        scrollTabla.setViewportView(tablaIngredientes);

        btnAceptar.setText("Aceptar");
        btnAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAceptarActionPerformed(evt);
            }
        });

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlBuscadorLayout = new javax.swing.GroupLayout(pnlBuscador);
        pnlBuscador.setLayout(pnlBuscadorLayout);
        pnlBuscadorLayout.setHorizontalGroup(
            pnlBuscadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBuscadorLayout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(pnlBuscadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblBuscarInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlBuscadorLayout.createSequentialGroup()
                        .addComponent(cmbColumna, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(scrollTabla, javax.swing.GroupLayout.PREFERRED_SIZE, 387, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(74, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlBuscadorLayout.createSequentialGroup()
                .addGap(100, 100, 100)
                .addComponent(btnCancelar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAceptar)
                .addGap(113, 113, 113))
        );
        pnlBuscadorLayout.setVerticalGroup(
            pnlBuscadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBuscadorLayout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(lblBuscarInventario)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlBuscadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbColumna, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(scrollTabla, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlBuscadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAceptar)
                    .addComponent(btnCancelar))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlBuscador, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlBuscador, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Al aceptar, si se dió en el parámetro un producto con una receta, este valida que el 
     * stock sea suficiente. De lo contrario, solo verifica que se haya seleccionado un
     * producto de la lista.
     * @param evt click en el botón aceptar.
     */
    private void btnAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarActionPerformed
        if (producto != null && producto.getDetallesReceta()!= null){
            if (hayStockSuficiente()){
                suficiente = true;
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(
                        this, 
                        "Algunos ingredientes no tienen stock suficiente.", 
                        "Error de Inventario", 
                        JOptionPane.ERROR_MESSAGE
                );
            }
            
        } else {
            if (getIngredienteSeleccionado() == null){
                JOptionPane.showMessageDialog(
                        this, 
                        "No se seleccionó ningún ingrediente.", 
                        "Error de Selección", 
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }//GEN-LAST:event_btnAceptarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAceptar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JComboBox<String> cmbColumna;
    private javax.swing.JLabel lblBuscarInventario;
    private javax.swing.JPanel pnlBuscador;
    private javax.swing.JScrollPane scrollTabla;
    private javax.swing.JTable tablaIngredientes;
    private javax.swing.JTextField txtBuscar;
    // End of variables declaration//GEN-END:variables
}
