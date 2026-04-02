/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package itson.restaurantepresentacion;

import itson.restaurantedominio.Ingrediente;
import itson.restaurantedtos.IngredienteActualizadoDTO;
import itson.restaurantedtos.IngredienteNuevoDTO;
import itson.restaurantedtos.UnidadMedida;
import itson.restaurantenegocio.IIngredientesBO;
import itson.restaurantenegocio.IngredientesBO;
import itson.restaurantenegocio.NegocioException;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 * Interfaz gráfica para el inventario de ingredientes.
 * @author Zaira Paola Barajas Díaz
 */
public class InventarioIngredientes extends javax.swing.JFrame {
    private IIngredientesBO ingredientesBO = new IngredientesBO();
    String imagen = null;
    private JTextField txtNombreNuevo = new JTextField(100);
    private JTextField txtCantidadNueva = new JTextField();
    private static final Logger LOGGER = Logger.getLogger(InventarioIngredientes.class.getName());

    /**
     * Creates new form InventarioIngredientes
     */
    public InventarioIngredientes() {
        initComponents();
        configurarComboBox();
        crearTabla();
    }
    
    /**
     * Método para llenar la tabla de inventario de ingredientes con los 
     * ingredientes registrados.
     */
    public void crearTabla(){
        String[] columnas = {"Id", "Nombre", "Unidad de Medida", "Stock"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0){
          @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }  
        };
        
        try {
            List<Ingrediente> ingredientes = ingredientesBO.recuperarIngredientes();
            for (Ingrediente ing: ingredientes){
                Object[] fila = {
                    ing.getIdIngrediente(),
                    ing.getImagen(),
                    ing.getUnidadMedida(),
                    ing.getStock()
                };
                modelo.addRow(fila);
            }
            
        } catch (NegocioException ex) {
            LOGGER.severe(ex.getMessage());
            //todo
        }
        
        tablaIngredientes.setModel(modelo);
            
        tablaIngredientes.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int filaSeleccionada = tablaIngredientes.getSelectedRow();
                    if (filaSeleccionada != -1) {
                        llenarFormulario(filaSeleccionada);
                    }
                }
            }
        });
    }
    
    /**
     * Llena el formulario con la información de la tabla de ingredientes.
     */
    public void llenarFormulario(int fila){
        txtNombre.setText(tablaIngredientes.getValueAt(fila, 1).toString());
        cmbMedida.setSelectedItem(tablaIngredientes.getValueAt(fila, 2));
        txtCantidad.setText(tablaIngredientes.getValueAt(fila, 3).toString());
    }
    
    /**
     * Agrega los valores de la unidad de medida al comboBox
     */
    private void configurarComboBox(){
        DefaultComboBoxModel<UnidadMedida> modelo = new DefaultComboBoxModel<>(UnidadMedida.values());
        cmbMedida.setModel(modelo);
    }
    
    /**
     * Método que ayuda a convertir la información del formulario a un objeto DTO.
     * @return objeto tipo IngredienteNuevoDTO con la información obtenida del
     * formulario.
     */
    private IngredienteNuevoDTO leerFormulario(){
        try {
            String nombre = txtNombre.getText();
            Double stock = Double.valueOf(txtCantidad.getText());
            UnidadMedida medida = (UnidadMedida) cmbMedida.getSelectedItem();
            String imagenIngrediente = null;
            
            if (nombre.trim().isEmpty() || nombre.trim().isBlank()) {
                JOptionPane.showMessageDialog(
                        this, 
                        "El nombre no puede estar vacío", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                return null;
            }
            
            if (this.imagen != null){
                imagenIngrediente = this.imagen;
            }
            
            return new IngredienteNuevoDTO(nombre, medida, stock, imagenIngrediente);
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this, 
                    "El stock debe ser numérico",
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    
    /**
     * Panel para obtener la información nueva 
     * @return un JPanel que muestra campos a llenar de información
     * nueva para actualizar el ingrediente.
     */
    public JPanel ventanaActualizar(){
        //TODO: INCLUYE LA IMAGEN
        JPanel modificar = new JPanel();
        modificar.setLayout(new GridLayout(0, 1, 3, 2));
        modificar.add(new JLabel("Ingrese el nuevo nombre: "));
        modificar.add(txtNombreNuevo);
        modificar.add(new JLabel("Ingrese la nueva unidad de medida: "));
        modificar.add(cmbMedida);
        modificar.add(new JLabel("Ingrese la nueva cantidad: "));
        modificar.add(txtCantidadNueva);
        
       return modificar;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlEncabezado = new javax.swing.JPanel();
        btnRegresar = new javax.swing.JButton();
        pnlFormulario = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        lblNombre = new javax.swing.JLabel();
        lblMedida = new javax.swing.JLabel();
        lblCantidad = new javax.swing.JLabel();
        txtCantidad = new javax.swing.JTextField();
        txtNombre = new javax.swing.JTextField();
        btnAgregar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnDesinventariar = new javax.swing.JButton();
        cmbMedida = new javax.swing.JComboBox<>();
        pnlImagen = new javax.swing.JPanel();
        btnImagen = new javax.swing.JButton();
        scrollTabla = new javax.swing.JScrollPane();
        tablaIngredientes = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Inventario de Ingredientes");

        pnlEncabezado.setBackground(new java.awt.Color(254, 178, 26));

        btnRegresar.setBackground(new java.awt.Color(237, 63, 39));
        btnRegresar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnRegresar.setForeground(new java.awt.Color(255, 255, 255));
        btnRegresar.setText("Regresar");

        javax.swing.GroupLayout pnlEncabezadoLayout = new javax.swing.GroupLayout(pnlEncabezado);
        pnlEncabezado.setLayout(pnlEncabezadoLayout);
        pnlEncabezadoLayout.setHorizontalGroup(
            pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEncabezadoLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(btnRegresar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlEncabezadoLayout.setVerticalGroup(
            pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEncabezadoLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(btnRegresar)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        pnlFormulario.setBackground(new java.awt.Color(19, 70, 134));

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(255, 255, 255));
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setText("Inventario de Ingredientes");

        lblNombre.setText("Nombre:");

        lblMedida.setText("Unidad de medida: ");

        lblCantidad.setText("Cantidad:");

        txtCantidad.setBackground(new java.awt.Color(255, 255, 255));
        txtCantidad.setToolTipText("");

        txtNombre.setBackground(new java.awt.Color(255, 255, 255));

        btnAgregar.setBackground(new java.awt.Color(255, 255, 255));
        btnAgregar.setForeground(new java.awt.Color(51, 51, 51));
        btnAgregar.setText("Agregar");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });

        btnActualizar.setBackground(new java.awt.Color(255, 255, 255));
        btnActualizar.setForeground(new java.awt.Color(51, 51, 51));
        btnActualizar.setText("Actualizar");
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });

        btnEliminar.setBackground(new java.awt.Color(255, 179, 168));
        btnEliminar.setForeground(new java.awt.Color(51, 51, 51));
        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnDesinventariar.setBackground(new java.awt.Color(255, 179, 168));
        btnDesinventariar.setForeground(new java.awt.Color(51, 51, 51));
        btnDesinventariar.setText("Desinventariar");

        cmbMedida.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout pnlImagenLayout = new javax.swing.GroupLayout(pnlImagen);
        pnlImagen.setLayout(pnlImagenLayout);
        pnlImagenLayout.setHorizontalGroup(
            pnlImagenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        pnlImagenLayout.setVerticalGroup(
            pnlImagenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 84, Short.MAX_VALUE)
        );

        btnImagen.setText("Agregar Imagen");
        btnImagen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImagenActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlFormularioLayout = new javax.swing.GroupLayout(pnlFormulario);
        pnlFormulario.setLayout(pnlFormularioLayout);
        pnlFormularioLayout.setHorizontalGroup(
            pnlFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlFormularioLayout.createSequentialGroup()
                .addGroup(pnlFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlFormularioLayout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addGroup(pnlFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlFormularioLayout.createSequentialGroup()
                                .addComponent(txtNombre)
                                .addGap(90, 90, 90))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlFormularioLayout.createSequentialGroup()
                                .addGroup(pnlFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lblNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblMedida, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtCantidad)
                                    .addComponent(cmbMedida, 0, 150, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(pnlImagen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(44, 44, 44))))
                    .addGroup(pnlFormularioLayout.createSequentialGroup()
                        .addGap(0, 24, Short.MAX_VALUE)
                        .addComponent(btnAgregar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnActualizar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnEliminar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDesinventariar)))
                .addGap(24, 24, 24))
            .addGroup(pnlFormularioLayout.createSequentialGroup()
                .addGap(87, 87, 87)
                .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlFormularioLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnImagen)
                .addGap(60, 60, 60))
        );
        pnlFormularioLayout.setVerticalGroup(
            pnlFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFormularioLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(lblTitulo)
                .addGap(18, 18, 18)
                .addComponent(lblNombre)
                .addGap(18, 18, 18)
                .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addGroup(pnlFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlFormularioLayout.createSequentialGroup()
                        .addComponent(lblMedida)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbMedida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addComponent(lblCantidad)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pnlImagen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnImagen)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAgregar)
                    .addComponent(btnActualizar)
                    .addComponent(btnEliminar)
                    .addComponent(btnDesinventariar))
                .addGap(22, 22, 22))
        );

        scrollTabla.setBackground(new java.awt.Color(255, 255, 255));

        tablaIngredientes.setBackground(new java.awt.Color(255, 255, 255));
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlEncabezado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(scrollTabla, javax.swing.GroupLayout.DEFAULT_SIZE, 389, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlFormulario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlEncabezado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(scrollTabla, javax.swing.GroupLayout.DEFAULT_SIZE, 451, Short.MAX_VALUE)
                    .addComponent(pnlFormulario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * El botón de actualizar abre el panel del método ventanaActualizar() si hay una fila
     * seleccionada, recupera la información nueva, llena el formulario con ella y luego
     * intenta actualizar el ingrediente en la base de datos.
     * @param evt click en el botón actualizar.
     */
    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        //TODO: INCLUYE LA IMAGEN
        int fila = tablaIngredientes.getSelectedRow();
        if (fila != -1){
            
            int opcion = JOptionPane.showConfirmDialog(
                    this, 
                    ventanaActualizar(),
                    "Actualizar Ingrediente",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            
            if (opcion == JOptionPane.OK_OPTION){
                txtNombre.setText(txtNombreNuevo.getText());
                String seleccion = (String) cmbMedida.getSelectedItem();
                txtCantidad.setText(txtCantidadNueva.getText());
                
            try {
                IngredienteNuevoDTO i = leerFormulario();
                IngredienteActualizadoDTO ingredienteActualizar = new IngredienteActualizadoDTO(
                        Long.valueOf(tablaIngredientes.getValueAt(fila, 0).toString()),
                        i.getNombre(),
                        i.getUnidadMedida(),
                        i.getStock()
                );
                
                ingredientesBO.modificar(ingredienteActualizar);
                        
            } catch (NegocioException ex) {
                LOGGER.severe(ex.getMessage());
                JOptionPane.showMessageDialog(
                    this, 
                    "Error al actualizar el ingrediente.",
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
            
            } else {
                JOptionPane.showMessageDialog(
                    this, 
                    "Error al actualizar el ingrediente.",
                    "Operación cancelada",
                    JOptionPane.INFORMATION_MESSAGE
                    );
            }
            
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona una fila para modificar el ingrediente",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }//GEN-LAST:event_btnActualizarActionPerformed

    /**
     * Al presioanr el botón para agregar, este llamará al método para convertir la 
     * información del formulario en un dto de ingrediente y luego intentara agregarlo
     * a la base de datos.
     * @param evt click al botón Agregar
     */
    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        try {
            ingredientesBO.agregar(leerFormulario());
        } catch (NegocioException ex) {
            LOGGER.severe(ex.getMessage());
            JOptionPane.showMessageDialog(
                    null, 
                    "Error al agregar ingrediente: " + ex.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }//GEN-LAST:event_btnAgregarActionPerformed

    /**
     * Este botón abre un OptionPane que recibe el URL de la imagen deseada 
     * y se la agrega al ingrediente.
     * @param evt click en el botón agregar imagen
     */
    private void btnImagenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImagenActionPerformed
       //TODO: agrega la imagen al ingrediente
        imagen = JOptionPane.showInputDialog(
               null, 
               "Por favor, ingrese el URL de la imagen", 
               "URL de Imagen",
               JOptionPane.QUESTION_MESSAGE);
       
       try {
           URL url = new URL (imagen);
           Image imagen = ImageIO.read(url);
           
           JLabel lblImagen = new JLabel(new ImageIcon(imagen));
           pnlImagen.add(lblImagen);
           
       } catch (IOException ex) {
            LOGGER.severe(ex.getMessage());
            JOptionPane.showMessageDialog(
               null, 
               "Error al recuperar la imagen del URL", 
               "Error en la imagen",
               JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnImagenActionPerformed

    /**
     * Toma el ingrediente seleccionado y lo trata de eliminar de la base de datos.
     * @param evt click en el botón eliminar.
     */
    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        int fila = tablaIngredientes.getSelectedRow();
        if (fila != -1){
            try {
                Long id = Long.valueOf(tablaIngredientes.getValueAt(fila, 0).toString());
                ingredientesBO.eliminar(id);
            } catch (NegocioException ex) {
                LOGGER.severe(ex.getMessage());
                JOptionPane.showMessageDialog(
                   null, 
                   "Error al eliminar el ingrediente", 
                   "Error",
                   JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona una fila para eliminar el ingrediente",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }//GEN-LAST:event_btnEliminarActionPerformed

   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnDesinventariar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnImagen;
    private javax.swing.JButton btnRegresar;
    private javax.swing.JComboBox<UnidadMedida> cmbMedida;
    private javax.swing.JLabel lblCantidad;
    private javax.swing.JLabel lblMedida;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JPanel pnlEncabezado;
    private javax.swing.JPanel pnlFormulario;
    private javax.swing.JPanel pnlImagen;
    private javax.swing.JScrollPane scrollTabla;
    private javax.swing.JTable tablaIngredientes;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables
}
