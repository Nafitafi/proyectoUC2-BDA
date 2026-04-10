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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
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
 * Interfaz gráfica para el inventario de ingredientes.
 * @author Zaira Paola Barajas Díaz
 */
public class InventarioIngredientes extends javax.swing.JFrame {
    private IIngredientesBO ingredientesBO = new IngredientesBO();
    byte[] imagen = null;
    private JComboBox<UnidadMedida> cmbMedidaNuevo = new JComboBox<>(UnidadMedida.values());
    private JTextField txtNombreNuevo = new JTextField(100);
    private JTextField txtCantidadNueva = new JTextField();
    private JRadioButton inventariar = new JRadioButton("Inventariar");
    private JRadioButton desinventariar = new JRadioButton("Desinventariar");;
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
                    this, 
                    "Error al acceder a los ingredientes: " + ex.getMessage(),
                    "Error", 
                    JOptionPane.ERROR_MESSAGE
            );
        }
        
        tablaIngredientes.setModel(modelo);
            
        tablaIngredientes.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int filaSeleccionada = tablaIngredientes.getSelectedRow();
                    if (filaSeleccionada != -1) {
                        llenarFormulario(filaSeleccionada);
                    } else {
                        limpiar();
                    }
                }
            }
        });
        
        tablaIngredientes.setRowHeight(60);
        
        tablaIngredientes.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
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
     * Llena el formulario con la información de la tabla de ingredientes.
     */
    public void llenarFormulario(int fila){
        txtNombre.setText(tablaIngredientes.getValueAt(fila, 1).toString());
        String medida = tablaIngredientes.getValueAt(fila, 2).toString();
        try {
            UnidadMedida enumMedida = UnidadMedida.valueOf(medida);
            cmbMedida.setSelectedItem(enumMedida);
        } catch (IllegalArgumentException e) {
            cmbMedida.setSelectedIndex(0); 
        }
        
        txtCantidad.setText(tablaIngredientes.getValueAt(fila, 3).toString());
        Object valorCelda =  tablaIngredientes.getValueAt(fila, 4);
        
        pnlImagen.removeAll();
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

                lblImagen.setIcon(new ImageIcon(imagenEscalada));
                lblImagen.setText(""); 
                pnlImagen.add(lblImagen);
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

        if (imagenBytes != null && imagenBytes.length > 0) {
            ImageIcon iconoOriginal = new ImageIcon(imagenBytes);
            Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);

            lblImagen.setIcon(new ImageIcon(imagenEscalada));
            lblImagen.setText(""); 
            pnlImagen.add(lblImagen);
        } else {
            lblImagen.setText("Sin imagen");
        }
        pnlImagen.revalidate();
        pnlImagen.repaint();
    }
    
    /**
     * Limpia lo escrito en el formulario si se deselecciona una fila
     */
    public void limpiar(){
        txtNombre.setText("");
        cmbMedida.setSelectedIndex(0);
        txtCantidad.setText("");
        imagen = null;
        lblImagen.setIcon(null); 
        lblImagen.setText("Sin Imagen");

        pnlImagen.removeAll();
        pnlImagen.add(lblImagen); 

        pnlImagen.revalidate();
        pnlImagen.repaint();
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
            byte[] imagenIngrediente = null;
            
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
            
            IngredienteNuevoDTO ingrediente = new IngredienteNuevoDTO(
                    nombre, 
                    medida, 
                    stock,
                    imagenIngrediente
            );
            
            if (ingrediente == null) {
                JOptionPane.showMessageDialog(
                        this, 
                        "Error al agregar: Datos vacíos", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                return null;
            }
            
            return ingrediente;
            
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
        JPanel modificar = new JPanel();
        modificar.setLayout(new GridLayout(0, 1, 3, 2));
        modificar.setSize(500,500);
        modificar.add(new JLabel("Ingrese el nuevo nombre: "));
        txtNombreNuevo.setText(txtNombre.getText());
        modificar.add(txtNombreNuevo);
        modificar.add(new JLabel("Ingrese la nueva unidad de medida: "));
        cmbMedidaNuevo.setSelectedIndex(cmbMedida.getSelectedIndex());
        modificar.add(cmbMedidaNuevo);
        modificar.add(new JLabel("Ingrese la nueva cantidad: "));
        txtCantidadNueva.setText(txtCantidad.getText());
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
     * Método que regresa la fila del ingrediente con el id del parámetro
     * @param idIngrediente el id del ingrediente que se busca
     * @return número entero que representa la fila donde se encuentra el 
     * ingrediente buscado. -1 si no lo encuentra.
     */
    public int buscarFilaPorId(Long idIngrediente) {
        for (int i = 0; i < tablaIngredientes.getRowCount(); i++) {
            Object valorCelda = tablaIngredientes.getValueAt(i, 0);

            if (valorCelda != null && valorCelda.toString().equals(idIngrediente.toString())) {
                return i; 
            }
        }
        return -1; 
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFileChooser1 = new javax.swing.JFileChooser();
        pnlEncabezado = new javax.swing.JPanel();
        btnRegresar = new javax.swing.JButton();
        btnBuscar = new javax.swing.JButton();
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
        btnStock = new javax.swing.JButton();
        cmbMedida = new javax.swing.JComboBox<>();
        pnlImagen = new javax.swing.JPanel();
        lblImagen = new javax.swing.JLabel();
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

        btnBuscar.setBackground(new java.awt.Color(255, 255, 255));
        btnBuscar.setForeground(new java.awt.Color(51, 51, 51));
        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlEncabezadoLayout = new javax.swing.GroupLayout(pnlEncabezado);
        pnlEncabezado.setLayout(pnlEncabezadoLayout);
        pnlEncabezadoLayout.setHorizontalGroup(
            pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEncabezadoLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(btnRegresar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnBuscar)
                .addGap(36, 36, 36))
        );
        pnlEncabezadoLayout.setVerticalGroup(
            pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEncabezadoLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(pnlEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRegresar)
                    .addComponent(btnBuscar))
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

        btnStock.setBackground(new java.awt.Color(255, 179, 168));
        btnStock.setForeground(new java.awt.Color(51, 51, 51));
        btnStock.setText("Gestionar stock");
        btnStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStockActionPerformed(evt);
            }
        });

        cmbMedida.setBackground(new java.awt.Color(255, 255, 255));

        pnlImagen.setBackground(new java.awt.Color(255, 255, 255));
        pnlImagen.setForeground(new java.awt.Color(51, 51, 51));
        pnlImagen.setPreferredSize(new java.awt.Dimension(100, 100));

        lblImagen.setBackground(new java.awt.Color(255, 255, 255));
        lblImagen.setForeground(new java.awt.Color(51, 51, 51));

        javax.swing.GroupLayout pnlImagenLayout = new javax.swing.GroupLayout(pnlImagen);
        pnlImagen.setLayout(pnlImagenLayout);
        pnlImagenLayout.setHorizontalGroup(
            pnlImagenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblImagen, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
        );
        pnlImagenLayout.setVerticalGroup(
            pnlImagenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblImagen, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
        );

        btnImagen.setBackground(new java.awt.Color(255, 255, 255));
        btnImagen.setForeground(new java.awt.Color(51, 51, 51));
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
            .addGroup(pnlFormularioLayout.createSequentialGroup()
                .addGap(87, 87, 87)
                .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlFormularioLayout.createSequentialGroup()
                .addGap(0, 21, Short.MAX_VALUE)
                .addComponent(btnAgregar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnActualizar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlFormularioLayout.createSequentialGroup()
                        .addComponent(btnEliminar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnStock))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlFormularioLayout.createSequentialGroup()
                        .addComponent(btnImagen)
                        .addGap(34, 34, 34)))
                .addGap(24, 24, 24))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlFormularioLayout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(pnlFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtNombre)
                    .addGroup(pnlFormularioLayout.createSequentialGroup()
                        .addGroup(pnlFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblMedida, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbMedida, 0, 170, Short.MAX_VALUE)
                            .addComponent(txtCantidad))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pnlImagen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(66, 66, 66))
        );
        pnlFormularioLayout.setVerticalGroup(
            pnlFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFormularioLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(lblTitulo)
                .addGap(18, 18, 18)
                .addComponent(lblNombre)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlFormularioLayout.createSequentialGroup()
                        .addComponent(lblMedida)
                        .addGap(9, 9, 9)
                        .addComponent(cmbMedida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblCantidad)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pnlImagen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnImagen)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAgregar)
                    .addComponent(btnActualizar)
                    .addComponent(btnEliminar)
                    .addComponent(btnStock))
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
                .addComponent(scrollTabla, javax.swing.GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlFormulario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlEncabezado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(scrollTabla, javax.swing.GroupLayout.DEFAULT_SIZE, 463, Short.MAX_VALUE)
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
                txtCantidad.setText(txtCantidadNueva.getText());
                cmbMedida.setSelectedIndex(cmbMedidaNuevo.getSelectedIndex());
                
            try {
                IngredienteNuevoDTO i = leerFormulario();
                IngredienteActualizadoDTO ingredienteActualizar = new IngredienteActualizadoDTO(
                        Long.valueOf(tablaIngredientes.getValueAt(fila, 0).toString()),
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
                    this, 
                    "Error al actualizar el ingrediente: " + ex.getMessage(),
                    "Error", 
                    JOptionPane.ERROR_MESSAGE
                );
            }
            
            } else {
                JOptionPane.showMessageDialog(
                    this, 
                    "Error al actualizar el ingrediente: Operación cancelada",
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
    }//GEN-LAST:event_btnAgregarActionPerformed

    /**
     * Este botón abre un OptionPane que recibe el URL de la imagen deseada 
     * y se la agrega al ingrediente.
     * @param evt click en el botón agregar imagen
     */
    private void btnImagenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImagenActionPerformed
        javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
        javax.swing.filechooser.FileNameExtensionFilter filtro = new javax.swing.filechooser.FileNameExtensionFilter("Imágenes", "jpg", "jpeg", "png");
        fileChooser.setFileFilter(filtro);
        fileChooser.setAcceptAllFileFilterUsed(false);

        if (fileChooser.showOpenDialog((java.awt.Component)evt.getSource()) == javax.swing.JFileChooser.APPROVE_OPTION) {
            try {
                java.io.File archivo = fileChooser.getSelectedFile();
                imagen = java.nio.file.Files.readAllBytes(archivo.toPath());
                
                javax.swing.ImageIcon icono = new javax.swing.ImageIcon(imagen);
                java.awt.Image imagenEscalada = icono.getImage().getScaledInstance(lblImagen.getWidth(), lblImagen.getHeight(), java.awt.Image.SCALE_SMOOTH);
                
                lblImagen.setIcon(new javax.swing.ImageIcon(imagenEscalada));
                lblImagen.setText(""); 
            } catch (Exception ex) {
                javax.swing.JOptionPane.showMessageDialog(
                        this, 
                        "Error al cargar la imagen", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
            }
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
                            this, 
                            "Se eliminó el ingrediente",
                            "Eliminar ingrediente",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    JOptionPane.showMessageDialog(
                            this, 
                            "No se eliminó el ingrediente",
                            "Eliminar ingrediente",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }
                
            } catch (NegocioException ex) {
                LOGGER.severe(ex.getMessage());
                JOptionPane.showMessageDialog(
                   null, 
                   "Error al eliminar el ingrediente: " + ex.getMessage(), 
                   "Error",
                   JOptionPane.ERROR_MESSAGE
                );
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

    /**
     * Botón que permite abrir una ventana para gestionar el stock de un ingrediente
     * seleccionado.
     * @param evt click en el botón gestionar stock
     */
    private void btnStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStockActionPerformed
        int fila = tablaIngredientes.getSelectedRow();
        if (fila != -1){
            
            int opcion = JOptionPane.showConfirmDialog(this, 
                    ventanaGestionarInventario(),
                    "Gestionar Stock",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            
            if (opcion == JOptionPane.OK_OPTION){
                try {
                    double cantidad = Double.parseDouble(txtCantidadNueva.getText());
                    Long id = Long.valueOf(tablaIngredientes.getValueAt(fila, 0).toString());
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
                            this, 
                            "El stock debe ser numérico.",
                            "Error", 
                            JOptionPane.ERROR_MESSAGE
                    );
                } catch (NegocioException ex) {
                    LOGGER.severe(ex.getMessage());
                    JOptionPane.showMessageDialog(
                            this, 
                            "Error al acceder a los ingredientes: " + ex.getMessage(),
                            "Error", 
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            } else {
                JOptionPane.showMessageDialog(
                    this, 
                    "Error al desinventariar el stock.",
                    "Operación cancelada",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona una fila para desinventariar el stock de un ingrediente.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }//GEN-LAST:event_btnStockActionPerformed

    /**
     * Este botón abre un JDialog que sirve como buscador de ingredientes, el cual al
     * seleccionar un ingrediente de la tabla y aceptar, este selecciona en la ventana 
     * padre el ingrediente y llena el formulario con sus datos.
     * @param evt click en el botón buscar
     */
    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        BuscadorInventarioJDialog buscador = new BuscadorInventarioJDialog(this, true, null);
        Long idBuscador = buscador.idSeleccionado;
        int filaIngrediente = buscarFilaPorId(idBuscador);
        if (filaIngrediente != -1){
            tablaIngredientes.setRowSelectionInterval(filaIngrediente, filaIngrediente);
            tablaIngredientes.scrollRectToVisible(tablaIngredientes.getCellRect(filaIngrediente, 0, true));
            tablaIngredientes.requestFocus();
            llenarFormulario(filaIngrediente);
        }
    }//GEN-LAST:event_btnBuscarActionPerformed

   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnImagen;
    private javax.swing.JButton btnRegresar;
    private javax.swing.JButton btnStock;
    private javax.swing.JComboBox<UnidadMedida> cmbMedida;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel lblCantidad;
    private javax.swing.JLabel lblImagen;
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
