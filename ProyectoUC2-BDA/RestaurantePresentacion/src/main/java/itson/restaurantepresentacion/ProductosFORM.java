/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package itson.restaurantepresentacion;

import itson.restaurantedtos.EstadoProducto;
import itson.restaurantedtos.ProductoDTO;
import itson.restaurantedtos.TipoProducto;
import itson.restaurantenegocio.NegocioException;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Image;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author nafbr
 */
public class ProductosFORM extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ProductosFORM.class.getName());
    private ProductosControl control;

    /**
     * Creates new form ProductosFORM
     */
    public ProductosFORM() {
        control = new ProductosControl();
        initComponents();
        cbxFiltroTipo.addActionListener(e -> cargarTablaProductos());
        configurarTabla();
        cargarTablaProductos();
    }
    /**
     * Configura el renderizador y editor para las columnas especiales
     */
    private void configurarTabla() {
        tblProductos.getColumnModel().getColumn(6).setCellRenderer(new RenderizadorAcciones());
        tblProductos.getColumnModel().getColumn(6).setCellEditor((TableCellEditor) new EditorAcciones(new JCheckBox()));
        tblProductos.getColumnModel().getColumn(6).setPreferredWidth(200);
        tblProductos.getColumnModel().getColumn(6).setMinWidth(200);
        tblProductos.getColumnModel().getColumn(0).setPreferredWidth(40);
        tblProductos.getColumnModel().getColumn(0).setMaxWidth(40);
        tblProductos.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            protected void setValue(Object value) {
                if (value instanceof ImageIcon) {
                    setIcon((ImageIcon) value);
                    setText(""); 
                } else {
                    setIcon(null);
                    setText((value != null) ? value.toString() : "Sin Imagen");
                }
            }
        });
        tblProductos.setRowHeight(50);
    }
    /**
     * Llena la tabla con los datos de la BD de forma óptima
     */
    public void cargarTablaProductos() {
        DefaultTableModel modelo = (DefaultTableModel) tblProductos.getModel();
        if (tblProductos.isEditing()) {
            tblProductos.getCellEditor().stopCellEditing();
        }
        
        modelo.setRowCount(0); // Limpiar la tabla antes de cargar

        try {
            String filtroSeleccionado = (String) cbxFiltroTipo.getSelectedItem();
            List<ProductoDTO> listaProductos;

            if (filtroSeleccionado == null || filtroSeleccionado.equals("Todos")) {
                listaProductos = control.consultarTodosProductos();
            } else {
                TipoProducto tipo = TipoProducto.valueOf(filtroSeleccionado);
                listaProductos = control.buscarPorTipo(tipo);
            }

            for (ProductoDTO p : listaProductos) {
                Object celdaImagen = "Sin Imagen"; 
                if (p.getImagen() != null && p.getImagen().length > 0) {
                    try {
                        ImageIcon iconoOriginal = new ImageIcon(p.getImagen());
                        Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                        celdaImagen = new ImageIcon(imagenEscalada);
                    } catch (Exception e) {
                        logger.warning("No se pudo renderizar la imagen del producto ID: " + p.getId());
                    }
                }

                // Armar la fila
                Object[] fila = new Object[7];
                fila[0] = p.getId();
                fila[1] = celdaImagen; 
                fila[2] = p.getNombre() != null ? p.getNombre() : "N/A";
                fila[3] = p.getTipo() != null ? p.getTipo().name() : "N/A";
                fila[4] = p.getPrecio() != null ? String.format("$%.2f", p.getPrecio()) : "$0.00";
                fila[5] = p.getEstado() != null ? p.getEstado().name() : "N/A";
                fila[6] = ""; 

                modelo.addRow(fila);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar los productos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlPrincipal = new javax.swing.JPanel();
        pnlBanner = new javax.swing.JPanel();
        btnRegresar = new javax.swing.JButton();
        lblProductos = new javax.swing.JLabel();
        btnBuscarProducto = new javax.swing.JButton();
        cbxFiltroTipo = new javax.swing.JComboBox<>();
        lblBuscar = new javax.swing.JLabel();
        btnAgregarProducto = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProductos = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Productos");

        pnlPrincipal.setBackground(new java.awt.Color(253, 244, 227));

        pnlBanner.setBackground(new java.awt.Color(254, 178, 26));

        btnRegresar.setBackground(new java.awt.Color(237, 63, 39));
        btnRegresar.setForeground(new java.awt.Color(255, 255, 255));
        btnRegresar.setText("Regresar");

        javax.swing.GroupLayout pnlBannerLayout = new javax.swing.GroupLayout(pnlBanner);
        pnlBanner.setLayout(pnlBannerLayout);
        pnlBannerLayout.setHorizontalGroup(
            pnlBannerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBannerLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(btnRegresar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlBannerLayout.setVerticalGroup(
            pnlBannerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBannerLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(btnRegresar)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        lblProductos.setBackground(new java.awt.Color(19, 70, 134));
        lblProductos.setFont(new java.awt.Font("Segoe UI Emoji", 1, 24)); // NOI18N
        lblProductos.setForeground(new java.awt.Color(19, 70, 134));
        lblProductos.setText("Productos");

        btnBuscarProducto.setBackground(new java.awt.Color(19, 70, 134));
        btnBuscarProducto.setForeground(new java.awt.Color(255, 255, 255));
        btnBuscarProducto.setText("Buscar producto por nombre");
        btnBuscarProducto.addActionListener(this::btnBuscarProductoActionPerformed);

        cbxFiltroTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos", "PLATILLO", "BEBIDA", "POSTRE", "EXTRA", " " }));

        lblBuscar.setFont(new java.awt.Font("Segoe UI Emoji", 2, 12)); // NOI18N
        lblBuscar.setForeground(new java.awt.Color(19, 70, 134));
        lblBuscar.setText("Filtrar por tipo:");

        btnAgregarProducto.setBackground(new java.awt.Color(19, 70, 134));
        btnAgregarProducto.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregarProducto.setText("Agregar producto nuevo");
        btnAgregarProducto.addActionListener(this::btnAgregarProductoActionPerformed);

        tblProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Imagen", "Nombre", "Tipo", "Precio", "Estado ", "Acciones"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblProductos);

        javax.swing.GroupLayout pnlPrincipalLayout = new javax.swing.GroupLayout(pnlPrincipal);
        pnlPrincipal.setLayout(pnlPrincipalLayout);
        pnlPrincipalLayout.setHorizontalGroup(
            pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlBanner, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnlPrincipalLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlPrincipalLayout.createSequentialGroup()
                        .addComponent(lblProductos)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPrincipalLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1)
                            .addGroup(pnlPrincipalLayout.createSequentialGroup()
                                .addComponent(btnBuscarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(85, 85, 85)
                                .addComponent(lblBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbxFiltroTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 130, Short.MAX_VALUE)
                                .addComponent(btnAgregarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(28, 28, 28))))
        );
        pnlPrincipalLayout.setVerticalGroup(
            pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPrincipalLayout.createSequentialGroup()
                .addComponent(pnlBanner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblProductos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBuscarProducto)
                    .addComponent(cbxFiltroTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblBuscar)
                    .addComponent(btnAgregarProducto))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 59, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarProductoActionPerformed
        BuscadorProductosJDialog buscador = new BuscadorProductosJDialog(this, true);
        buscador.setVisible(true);
        ProductoDTO producto = buscador.getProductoSeleccionado();
        
        if (producto != null) {
            DefaultTableModel modelo = (DefaultTableModel) tblProductos.getModel();
            modelo.setRowCount(0); 
            Object celdaImagen = "Sin Imagen"; 
            if (producto.getImagen() != null && producto.getImagen().length > 0) {
                try {
                    ImageIcon iconoOriginal = new ImageIcon(producto.getImagen());
                    Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                    celdaImagen = new ImageIcon(imagenEscalada);
                } catch (Exception e) {
                    logger.warning("No se pudo renderizar la imagen del producto ID: " + producto.getId());
                }
            }
            modelo.addRow(new Object[]{
                producto.getId(),
                celdaImagen,
                producto.getNombre() != null ? producto.getNombre() : "N/A",
                producto.getTipo() != null ? producto.getTipo().name() : "N/A",
                producto.getPrecio() != null ? String.format("$%.2f", producto.getPrecio()) : "$0.00",
                producto.getEstado() != null ? producto.getEstado().name() : "N/A",
                ""
            });
        } else {
            cargarTablaProductos();
        }
    }//GEN-LAST:event_btnBuscarProductoActionPerformed

    private void btnAgregarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarProductoActionPerformed
        control.abrirRegistroProducto(this);
    }//GEN-LAST:event_btnAgregarProductoActionPerformed

    /**
     * Método auxiliar para darle un diseño plano y moderno a los botones.
     */
    private void estilizarBoton(JButton boton, java.awt.Color colorFondo) {
        boton.setBackground(colorFondo);
        boton.setForeground(java.awt.Color.WHITE);
        boton.setFocusPainted(false); 
        boton.setBorderPainted(false); 
        boton.setOpaque(true);
        boton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR)); // Cursor de manita jeje
        boton.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
    }

    /**
     * Dibuja los botones en la celda de "Acciones".
     */
    private class RenderizadorAcciones extends DefaultTableCellRenderer {

        private JPanel pnlAcciones = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5)); 
        private JButton btnModificar = new JButton("Modificar");
        private JButton btnEstado = new JButton();

        public RenderizadorAcciones() {
            estilizarBoton(btnModificar, new java.awt.Color(19, 70, 134)); 
            estilizarBoton(btnEstado, java.awt.Color.GRAY); 

            pnlAcciones.add(btnModificar);
            pnlAcciones.add(btnEstado);
            pnlAcciones.setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (isSelected) {
                pnlAcciones.setBackground(table.getSelectionBackground());
            } else {
                pnlAcciones.setBackground(table.getBackground());
            }

            String estado = (String) table.getModel().getValueAt(row, 5);
            if ("ACTIVO".equals(estado)) {
                btnEstado.setText("Desactivar");
                btnEstado.setBackground(new java.awt.Color(237, 63, 39)); 
            } else {
                btnEstado.setText("Activar");
                btnEstado.setBackground(new java.awt.Color(46, 204, 113)); 
            }

            return pnlAcciones;
        }
    }

    /**
     * Da la funcionalidad de clic a los botones en la celda de "Acciones" y mantiene el estilo
     */
    private class EditorAcciones extends DefaultCellEditor {

        private JPanel pnlAcciones = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        private JButton btnModificar = new JButton("Modificar");
        private JButton btnEstado = new JButton();

        private Long idProductoActual;
        private String estadoActual;

        public EditorAcciones(JCheckBox checkBox) {
            super(checkBox);
            
            estilizarBoton(btnModificar, new java.awt.Color(19, 70, 134));
            estilizarBoton(btnEstado, java.awt.Color.GRAY);

            pnlAcciones.add(btnModificar);
            pnlAcciones.add(btnEstado);
            pnlAcciones.setOpaque(true);

            btnModificar.addActionListener(e -> {
                fireEditingStopped(); 
                JFrame ventana = control.abrirActualizarProducto(idProductoActual);
                ventana.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                        cargarTablaProductos(); 
                    }
                });
                ventana.setVisible(true);
            });
            
            btnEstado.addActionListener(e -> {
                fireEditingStopped();
                try {
                    EstadoProducto nuevoEstado = "ACTIVO".equals(estadoActual) ? EstadoProducto.INACTIVO : EstadoProducto.ACTIVO;
                    control.actualizarEstado(idProductoActual, nuevoEstado);
                    cargarTablaProductos();
                    JOptionPane.showMessageDialog(pnlAcciones, "Estado actualizado con éxito.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(pnlAcciones, "Error al cambiar estado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            idProductoActual = (Long) table.getModel().getValueAt(row, 0);
            estadoActual = (String) table.getModel().getValueAt(row, 5);

            if ("ACTIVO".equals(estadoActual)) {
                btnEstado.setText("Desactivar");
                btnEstado.setBackground(new java.awt.Color(237, 63, 39)); 
            } else {
                btnEstado.setText("Activar");
                btnEstado.setBackground(new java.awt.Color(46, 204, 113)); 
            }

            pnlAcciones.setBackground(table.getSelectionBackground());
            return pnlAcciones;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarProducto;
    private javax.swing.JButton btnBuscarProducto;
    private javax.swing.JButton btnRegresar;
    private javax.swing.JComboBox<String> cbxFiltroTipo;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblBuscar;
    private javax.swing.JLabel lblProductos;
    private javax.swing.JPanel pnlBanner;
    private javax.swing.JPanel pnlPrincipal;
    private javax.swing.JTable tblProductos;
    // End of variables declaration//GEN-END:variables
}
