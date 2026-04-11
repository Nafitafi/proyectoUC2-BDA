/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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
import java.net.MalformedURLException;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author Zaira
 */
public class IngredientesControl {
    private IIngredientesBO ingredientesBO;
    private byte[] imagen = null;
    InventarioIngredientesFORM formaIngredientes;
    private JComboBox<UnidadMedida> cmbMedidaNuevo = new JComboBox<>(UnidadMedida.values());
    private JTextField txtNombreNuevo = new JTextField(100);
    private JTextField txtCantidadNueva = new JTextField();
    private JRadioButton inventariar = new JRadioButton("Inventariar");
    private JRadioButton desinventariar = new JRadioButton("Desinventariar");
    private static final Logger LOGGER = Logger.getLogger(IngredientesControl.class.getName());
    
    public IngredientesControl(InventarioIngredientesFORM formaIngredientes) {
        this.ingredientesBO = new IngredientesBO();
        this.formaIngredientes = formaIngredientes;
        
        formaIngredientes.btnAgregar().addActionListener(e -> agregarIngrediente());
        formaIngredientes.btnImagen().addActionListener(e -> agregarImagen(e));
        formaIngredientes.btnEliminar().addActionListener(e -> eliminarIngrediente());
        formaIngredientes.btnBuscar().addActionListener(e -> buscarIngrediente());
        formaIngredientes.btnActualizar().addActionListener(e -> actualizarIngrediente());
        formaIngredientes.btnStock().addActionListener(e -> gestionarStock());
        formaIngredientes.btnRegresar().addActionListener(e -> regresar());
        
        configurarComboBox();
        crearTabla();
    }
    
    
    /**
     * Método para llenar la tabla de inventario de ingredientes con los 
     * ingredientes registrados.
     */
    public void crearTabla(){
        String[] columnas = {"Id", "Nombre", "Unidad de Medida", "Stock", "Imagen"};
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
                    ing.getNombre(),
                    ing.getUnidadMedida(),
                    ing.getStock(),
                    ing.getImagen()
                };
                if (fila[4] == null){
                    fila[4] = "Sin imagen";
                }
                modelo.addRow(fila);
            }
            
        } catch (NegocioException ex) {
            LOGGER.severe(ex.getMessage());
            JOptionPane.showMessageDialog(
                    formaIngredientes, 
                    "Error al acceder a los ingredientes: " + ex.getMessage(),
                    "Error", 
                    JOptionPane.ERROR_MESSAGE
            );
        }
        
        formaIngredientes.tablaIngredientes().setModel(modelo);
        formaIngredientes.tablaIngredientes().getColumnModel().getColumn(0).setMinWidth(0);
        formaIngredientes.tablaIngredientes().getColumnModel().getColumn(0).setPreferredWidth(0);
        formaIngredientes.tablaIngredientes().getColumnModel().getColumn(0).setMaxWidth(0);
        formaIngredientes.tablaIngredientes().getColumnModel().getColumn(0).setResizable(false);
            
        formaIngredientes.tablaIngredientes().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int filaSeleccionada = formaIngredientes.tablaIngredientes().getSelectedRow();
                    if (filaSeleccionada != -1) {
                        llenarFormulario(filaSeleccionada);
                    } else {
                        limpiar();
                    }
                }
            }
        });
        
        formaIngredientes.tablaIngredientes().setRowHeight(60);
        
        formaIngredientes.tablaIngredientes().getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            protected void setValue(Object value) {
                if (value instanceof byte[]) {
                    byte[] bytes = (byte[]) value;
                    if (bytes.length > 0) {
                        ImageIcon icono = new ImageIcon(bytes);
                        Image img = icono.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                        setIcon(new ImageIcon(img));
                        setText("");
                    } else {
                        setIcon(null);
                        setText("Sin Imagen");
                    }
                } else if (value instanceof String) {
                    ImageIcon iconoOriginal;
                    String celda = value.toString();
                    try {
                        if (celda.startsWith("http")) {
                            iconoOriginal = new ImageIcon(new java.net.URL(celda));
                        } else {
                            iconoOriginal = new ImageIcon(celda);
                        }
                        Image img = iconoOriginal.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                        setIcon(new ImageIcon(img));
                        setText("");
                        
                    } catch (MalformedURLException ex){
                        LOGGER.severe(ex.getMessage());
                        JOptionPane.showMessageDialog(
                                this, 
                                "Error al obtener la imagen: No se reconoce el URL.",
                                "Error", 
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            }
        });
    }
    
     /**
     * Agrega los valores de la unidad de medida al comboBox
     */
    private void configurarComboBox(){
        DefaultComboBoxModel<UnidadMedida> modelo = new DefaultComboBoxModel<>(UnidadMedida.values());
        formaIngredientes.cmbMedida().setModel(modelo);
    }
    
     /**
     * Llena el formulario con la información del ingrediente seleccionado
     * en la tabla de ingredientes.
     * @param fila fila seleccionada de la tabla de ingredientes.
     */
    public void llenarFormulario(int fila){
        formaIngredientes.txtNombre().setText(formaIngredientes.tablaIngredientes().getValueAt(fila, 1).toString());
        String medida = formaIngredientes.tablaIngredientes().getValueAt(fila, 2).toString();
        try {
            UnidadMedida enumMedida = UnidadMedida.valueOf(medida);
            formaIngredientes.cmbMedida().setSelectedItem(enumMedida);
        } catch (IllegalArgumentException e) {
            formaIngredientes.cmbMedida().setSelectedIndex(0); 
        }
        
        formaIngredientes.txtCantidad().setText(formaIngredientes.tablaIngredientes().getValueAt(fila, 3).toString());
        Object valorCelda =  formaIngredientes.tablaIngredientes().getValueAt(fila, 4);
        
        formaIngredientes.pnlImagen().removeAll();
        byte[] imagenBytes = null;

        if (valorCelda instanceof byte[]) {
            imagenBytes = (byte[]) valorCelda;
        } 
        else if (valorCelda instanceof String) {
            try {
                ImageIcon iconoOriginal;
                String celda = valorCelda.toString();
                if (celda.startsWith("http")) {
                    iconoOriginal = new ImageIcon(new java.net.URL(celda));
                } else {
                    iconoOriginal = new ImageIcon(celda);
                }
                Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);

                formaIngredientes.lblImagen().setIcon(new ImageIcon(imagenEscalada));
                formaIngredientes.lblImagen().setText(""); 
                formaIngredientes.pnlImagen().add(formaIngredientes.lblImagen());
            } catch (MalformedURLException ex){
                LOGGER.severe(ex.getMessage());
                JOptionPane.showMessageDialog(
                        formaIngredientes, 
                        "Error al obtener la imagen: No se reconoce el URL.",
                        "Error", 
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }

        if (imagenBytes != null && imagenBytes.length > 0) {
            ImageIcon iconoOriginal = new ImageIcon(imagenBytes);
            Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);

            formaIngredientes.lblImagen().setIcon(new ImageIcon(imagenEscalada));
            formaIngredientes.lblImagen().setText(""); 
            formaIngredientes.pnlImagen().add(formaIngredientes.lblImagen());
        } else {
            formaIngredientes.lblImagen().setText("Sin imagen");
        }
        formaIngredientes.pnlImagen().revalidate();
        formaIngredientes.pnlImagen().repaint();
    }
    
    /**
     * Limpia lo escrito en el formulario si se deselecciona una fila
     */
    public void limpiar(){
        formaIngredientes.txtNombre().setText("");
        formaIngredientes.cmbMedida().setSelectedIndex(0);
        formaIngredientes.txtCantidad().setText("");
        imagen = null;
        formaIngredientes.lblImagen().setIcon(null); 
        formaIngredientes.lblImagen().setText("Sin Imagen");

        formaIngredientes.pnlImagen().removeAll();
        formaIngredientes.pnlImagen().add(formaIngredientes.lblImagen()); 

        formaIngredientes.pnlImagen().revalidate();
        formaIngredientes.pnlImagen().repaint();
    }
    
    /**
     * Método que ayuda a convertir la información del formulario a un objeto DTO.
     * @return objeto tipo IngredienteNuevoDTO con la información obtenida del
     * formulario.
     */
    private IngredienteNuevoDTO leerFormulario(){
        try {
            String nombre = formaIngredientes.txtNombre().getText();
            Double stock = Double.valueOf(formaIngredientes.txtCantidad().getText());
            UnidadMedida medida = (UnidadMedida) formaIngredientes.cmbMedida().getSelectedItem();
            byte[] imagenIngrediente = null;
            
            if (nombre.trim().isEmpty() || nombre.trim().isBlank()) {
                JOptionPane.showMessageDialog(
                        formaIngredientes, 
                        "El nombre no puede estar vacío", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                return null;
            }
            
            if (this.imagen != null){
                imagenIngrediente = this.imagen;
            }
            
            IngredienteNuevoDTO ingrediente = new IngredienteNuevoDTO(
                    nombre, 
                    medida, 
                    stock,
                    imagenIngrediente
            );
            
            if (ingrediente == null) {
                JOptionPane.showMessageDialog(
                        formaIngredientes, 
                        "Error al agregar: Datos vacíos", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                return null;
            }
            
            return ingrediente;
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    formaIngredientes, 
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
        JPanel modificar = new JPanel();
        modificar.setLayout(new GridLayout(0, 1, 3, 2));
        modificar.setSize(500,500);
        modificar.add(new JLabel("Ingrese el nuevo nombre: "));
        txtNombreNuevo.setText(formaIngredientes.txtNombre().getText());
        modificar.add(txtNombreNuevo);
        modificar.add(new JLabel("Ingrese la nueva unidad de medida: "));
        cmbMedidaNuevo.setSelectedIndex(formaIngredientes.cmbMedida().getSelectedIndex());
        modificar.add(cmbMedidaNuevo);
        modificar.add(new JLabel("Ingrese la nueva cantidad: "));
        txtCantidadNueva.setText(formaIngredientes.txtCantidad().getText());
        modificar.add(txtCantidadNueva);
        
       return modificar;
    }
    
    /**
     * Panel que recibe la cantidad a gestionarInventario
     * @return un JPanel que muestra un campo para recibir la cantidad
     * a gestionar de un ingrediente determinado.
     */
    public JPanel ventanaGestionarInventario(){
        JPanel gestionar = new JPanel();
        gestionar.setLayout(new GridLayout(0, 1, 1, 2));
        gestionar.add(new JLabel("Ingrese la cantidad: "));
        gestionar.add(txtCantidadNueva);
        ButtonGroup grupoAccion = new ButtonGroup();
        grupoAccion.add(inventariar);
        grupoAccion.add(desinventariar);

        inventariar.setSelected(true); 

        gestionar.add(inventariar);
        gestionar.add(desinventariar);

        
       return gestionar;
    }

    /**
     * Este botón abre un OptionPane que recibe el URL de la imagen deseada 
     * y se la agrega al ingrediente.
     */
    public void agregarImagen(java.awt.event.ActionEvent evt){
        javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
        javax.swing.filechooser.FileNameExtensionFilter filtro = new javax.swing.filechooser.FileNameExtensionFilter("Imágenes", "jpg", "jpeg", "png");
        fileChooser.setFileFilter(filtro);
        fileChooser.setAcceptAllFileFilterUsed(false);

        if (fileChooser.showOpenDialog((java.awt.Component)evt.getSource()) == javax.swing.JFileChooser.APPROVE_OPTION) {
            try {
                java.io.File archivo = fileChooser.getSelectedFile();
                imagen = java.nio.file.Files.readAllBytes(archivo.toPath());
                
                javax.swing.ImageIcon icono = new javax.swing.ImageIcon(imagen);
                java.awt.Image imagenEscalada = icono.getImage().getScaledInstance(
                        formaIngredientes.lblImagen().getWidth(), 
                        formaIngredientes.lblImagen().getHeight(), 
                        java.awt.Image.SCALE_SMOOTH
                );
                
                formaIngredientes.lblImagen().setIcon(new javax.swing.ImageIcon(imagenEscalada));
                formaIngredientes.lblImagen().setText(""); 
            } catch (Exception ex) {
                javax.swing.JOptionPane.showMessageDialog(
                        formaIngredientes, 
                        "Error al cargar la imagen", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Método que regresa la fila del ingrediente con el id del parámetro
     * @param idIngrediente el id del ingrediente que se busca
     * @return número entero que representa la fila donde se encuentra el 
     * ingrediente buscado. -1 si no lo encuentra.
     */
    public int buscarFilaPorId(Long idIngrediente) {
        for (int i = 0; i < formaIngredientes.tablaIngredientes().getRowCount(); i++) {
            Object valorCelda = formaIngredientes.tablaIngredientes().getValueAt(i, 0);

            if (valorCelda != null && valorCelda.toString().equals(idIngrediente.toString())) {
                return i; 
            }
        }
        return -1; 
    }
    
     /**
     * Al presioanar el botón para agregar, este llamará al método para convertir la 
     * información del formulario en un dto de ingrediente y luego intentara agregarlo
     * a la base de datos.
     */
    public void agregarIngrediente(){
        try {
            IngredienteNuevoDTO ingrediente = leerFormulario();
            if (ingrediente != null ){
                ingredientesBO.agregar(ingrediente);
                crearTabla();
                limpiar();
            }
        } catch (NegocioException ex) {
            LOGGER.severe(ex.getMessage());
            JOptionPane.showMessageDialog(
                    null, 
                    "Error al agregar ingrediente: " + ex.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    /**
     * Toma el ingrediente seleccionado y lo trata de eliminar de la base de datos.
     */
    public void eliminarIngrediente(){
        int fila = formaIngredientes.tablaIngredientes().getSelectedRow();
        if (fila != -1){
            try {
                Long id = Long.valueOf(formaIngredientes.tablaIngredientes().getValueAt(fila, 0).toString());
                
                int resultado = JOptionPane.showConfirmDialog(
                        null,
                        "¿De verdad desea eliminar este ingrediente?",
                        "Eliminar ingrediente",
                        JOptionPane.YES_NO_OPTION
                );
                if (resultado == JOptionPane.YES_OPTION){
                    ingredientesBO.eliminar(id);
                    crearTabla();
                    JOptionPane.showMessageDialog(
                            formaIngredientes, 
                            "Se eliminó el ingrediente",
                            "Eliminar ingrediente",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    JOptionPane.showMessageDialog(
                            formaIngredientes, 
                            "No se eliminó el ingrediente",
                            "Eliminar ingrediente",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }
                
            } catch (NegocioException ex) {
                LOGGER.severe(ex.getMessage());
                JOptionPane.showMessageDialog(
                   formaIngredientes, 
                   "Error al eliminar el ingrediente: " + ex.getMessage(), 
                   "Error",
                   JOptionPane.ERROR_MESSAGE
                );
            }
        } else {
            JOptionPane.showMessageDialog(
                    formaIngredientes,
                    "Selecciona una fila para eliminar el ingrediente",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    /**
     * El botón de actualizar abre el panel del método ventanaActualizar() si hay una fila
     * seleccionada, recupera la información nueva, llena el formulario con ella y luego
     * intenta actualizar el ingrediente en la base de datos.
     */
    public void actualizarIngrediente(){
        int fila = formaIngredientes.tablaIngredientes().getSelectedRow();
        if (fila != -1){
            
            int opcion = JOptionPane.showConfirmDialog(
                    formaIngredientes, 
                    ventanaActualizar(),
                    "Actualizar Ingrediente",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            
            if (opcion == JOptionPane.OK_OPTION){
                formaIngredientes.txtNombre().setText(txtNombreNuevo.getText());
                formaIngredientes.txtCantidad().setText(txtCantidadNueva.getText());
                formaIngredientes.cmbMedida().setSelectedIndex(cmbMedidaNuevo.getSelectedIndex());
                
            try {
                IngredienteNuevoDTO i = leerFormulario();
                IngredienteActualizadoDTO ingredienteActualizar = new IngredienteActualizadoDTO(
                        Long.valueOf(formaIngredientes.tablaIngredientes().getValueAt(fila, 0).toString()),
                        i.getNombre(),
                        i.getUnidadMedida(),
                        i.getStock(),
                        imagen
                );
                
                ingredientesBO.modificar(ingredienteActualizar);
                crearTabla();
                
            } catch (NegocioException ex) {
                LOGGER.severe(ex.getMessage());
                JOptionPane.showMessageDialog(
                    formaIngredientes, 
                    "Error al actualizar el ingrediente: " + ex.getMessage(),
                    "Error", 
                    JOptionPane.ERROR_MESSAGE
                );
            }
            
            } else {
                JOptionPane.showMessageDialog(
                    formaIngredientes, 
                    "Error al actualizar el ingrediente: Operación cancelada",
                    "Operación cancelada",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
            
        } else {
            JOptionPane.showMessageDialog(
                    formaIngredientes,
                    "Selecciona una fila para modificar el ingrediente",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    /**
     * Botón que permite abrir una ventana para gestionar el stock de un ingrediente
     * seleccionado.
     */
    public void gestionarStock(){
        int fila = formaIngredientes.tablaIngredientes().getSelectedRow();
        if (fila != -1){
            
            int opcion = JOptionPane.showConfirmDialog(
                    formaIngredientes, 
                    ventanaGestionarInventario(),
                    "Gestionar Stock",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            
            if (opcion == JOptionPane.OK_OPTION){
                try {
                    double cantidad = Double.parseDouble(txtCantidadNueva.getText());
                    Long id = Long.valueOf(formaIngredientes.tablaIngredientes().getValueAt(fila, 0).toString());
                    boolean operacion;
                    if (inventariar.isSelected()){
                        operacion = true;
                    } else {
                        operacion = false;
                    }
                    ingredientesBO.gestionarInventario(
                            id, 
                            cantidad,
                            operacion
                    );
                    crearTabla();
                    
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(
                            formaIngredientes, 
                            "El stock debe ser numérico.",
                            "Error", 
                            JOptionPane.ERROR_MESSAGE
                    );
                } catch (NegocioException ex) {
                    LOGGER.severe(ex.getMessage());
                    JOptionPane.showMessageDialog(
                            formaIngredientes, 
                            "Error al acceder a los ingredientes: " + ex.getMessage(),
                            "Error", 
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            } else {
                JOptionPane.showMessageDialog(
                    formaIngredientes, 
                    "Error al desinventariar el stock.",
                    "Operación cancelada",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
        } else {
            JOptionPane.showMessageDialog(
                    formaIngredientes,
                    "Selecciona una fila para desinventariar el stock de un ingrediente.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    /**
     * Este botón permite regresar al menú principal de administradores
     */
    public void regresar(){
        MenuAdminFORM menu = new MenuAdminFORM();
        menu.setLocationRelativeTo(null);
        menu.setVisible(true);
        formaIngredientes.dispose();
    }
    
     /**
     * Este botón abre un JDialog que sirve como buscador de ingredientes, el cual al
     * seleccionar un ingrediente de la tabla y aceptar, este selecciona en la ventana 
     * padre el ingrediente y llena el formulario con sus datos.
     */
    public void buscarIngrediente(){
        BuscadorInventarioJDialog buscador = new BuscadorInventarioJDialog(formaIngredientes, true, null);
        Long idBuscador = buscador.idSeleccionado;
        int filaIngrediente = buscarFilaPorId(idBuscador);
        if (filaIngrediente != -1){
            formaIngredientes.tablaIngredientes().setRowSelectionInterval(filaIngrediente, filaIngrediente);
            formaIngredientes.tablaIngredientes().scrollRectToVisible(formaIngredientes.tablaIngredientes().getCellRect(filaIngrediente, 0, true));
            formaIngredientes.tablaIngredientes().requestFocus();
            llenarFormulario(filaIngrediente);
        }
    }
}
