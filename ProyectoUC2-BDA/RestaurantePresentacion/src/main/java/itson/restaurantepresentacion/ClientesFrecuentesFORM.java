/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package itson.restaurantepresentacion;

import itson.restaurantedtos.ClienteFrecuenteDTO;
import itson.restaurantenegocio.ClientesFrecuentesBO;
import itson.restaurantenegocio.NegocioException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author nafbr
 */
public class ClientesFrecuentesFORM extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ClientesFrecuentesFORM.class.getName());
    private ClientesFrecuentesBO clientesBO;

    /**
     * Creates new form ClientesFrecuentesFORM
     */
    public ClientesFrecuentesFORM() {
        initComponents();
        clientesBO = new ClientesFrecuentesBO();
        cargarTabla("");
        jTable1.getColumnModel().getColumn(7).setCellRenderer(new BotonRenderer());
        jTable1.getColumnModel().getColumn(7).setCellEditor(new BotonModificar(new javax.swing.JCheckBox(), jTable1));
        jTable1.setRowHeight(30);
        buscadorClientesPanelFORM1.addBuscadorKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                // Obtenemos lo que el usuario escribió
                String texto = buscadorClientesPanelFORM1.getTextoBusqueda();
                // Actualizamos la tabla
                cargarTabla(texto);
            }
        });
    }

    private void cargarTabla(String filtro) {
        try {
            List<ClienteFrecuenteDTO> listaClientes = clientesBO.buscarClientes(filtro);
            DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
            modelo.setRowCount(0);

            // Recorremos la lista de clientes y agregamos filas a la tabla
            for (ClienteFrecuenteDTO cliente : listaClientes) {
                Object[] fila = {
                    cliente.getNombre(),
                    cliente.getApellidoP(),
                    cliente.getApellidoM(),
                    cliente.getNumeroTelefono(),
                    cliente.getCorreo(),
                    cliente.getFechaRegistro(),
                    cliente.getPuntos(),
                    "Acciones"
                };
                modelo.addRow(fila);
            }
        } catch (NegocioException ex) {
            logger.severe("Error al cargar la tabla: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Hubo un error al cargar los clientes.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    class BotonRenderer extends javax.swing.JButton implements javax.swing.table.TableCellRenderer {

        public BotonRenderer() {
            setOpaque(true);
            setBackground(new java.awt.Color(254, 178, 26));
            setForeground(java.awt.Color.WHITE);
            setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        }

        public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value,boolean isSelected, boolean hasFocus,int row, int column) {
            if (value == null) {
                this.setText("Modificar");
            } else {
                this.setText(value.toString());
            }
            return this;
        }
    }

    class BotonModificar extends javax.swing.DefaultCellEditor {

        protected javax.swing.JButton button;
        private String label;
        private boolean isPushed;
        private int currentRow;
        private javax.swing.JTable tabla;

        public BotonModificar(javax.swing.JCheckBox checkBox, javax.swing.JTable tabla) {
            super(checkBox);
            this.tabla = tabla;
            button = new javax.swing.JButton();
            button.setOpaque(true);
            button.setBackground(new java.awt.Color(254, 178, 26));
            button.setForeground(java.awt.Color.WHITE);
            button.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        @Override
        public java.awt.Component getTableCellEditorComponent(javax.swing.JTable table, Object value,boolean isSelected, int row, int column) {
            this.currentRow = row;
            if (value == null) {
                this.label = "Editar";
            } else {
                this.label = value.toString();
            }
            this.button.setText(this.label);
            this.isPushed = true;
            return this.button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                String telefonoClic = tabla.getValueAt(currentRow, 3).toString();
                String nombreClic = tabla.getValueAt(currentRow, 0).toString();

                javax.swing.JOptionPane.showMessageDialog(button, "logica genial para abrir la modificación y asi wow");
            }
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
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

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        pnlBuscador = new javax.swing.JPanel();
        buscadorClientesPanelFORM1 = new itson.restaurantepresentacion.BuscadorClientesPanelFORM();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Clientes Frecuentes");
        setBackground(new java.awt.Color(253, 244, 227));

        jPanel1.setBackground(new java.awt.Color(253, 244, 227));

        jPanel2.setBackground(new java.awt.Color(19, 70, 134));

        jLabel1.setBackground(new java.awt.Color(253, 244, 227));
        jLabel1.setFont(new java.awt.Font("Segoe UI Emoji", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(253, 244, 227));
        jLabel1.setText("Menú");

        jButton1.setBackground(new java.awt.Color(19, 70, 134));
        jButton1.setFont(new java.awt.Font("Segoe UI Historic", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(253, 244, 227));
        jButton1.setText("Inventario");
        jButton1.setBorder(null);
        jButton1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        jButton2.setBackground(new java.awt.Color(19, 70, 134));
        jButton2.setFont(new java.awt.Font("Segoe UI Historic", 1, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(253, 244, 227));
        jButton2.setText("Gestionar platillos");
        jButton2.setBorder(null);
        jButton2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        jButton3.setBackground(new java.awt.Color(254, 178, 26));
        jButton3.setFont(new java.awt.Font("Segoe UI Historic", 1, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(253, 244, 227));
        jButton3.setText("Clientes");
        jButton3.setBorder(null);
        jButton3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        jButton4.setBackground(new java.awt.Color(19, 70, 134));
        jButton4.setFont(new java.awt.Font("Segoe UI Historic", 1, 14)); // NOI18N
        jButton4.setForeground(new java.awt.Color(253, 244, 227));
        jButton4.setText("Reportes");
        jButton4.setToolTipText("");
        jButton4.setBorder(null);
        jButton4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        jButton5.setBackground(new java.awt.Color(19, 70, 134));
        jButton5.setFont(new java.awt.Font("Segoe UI Historic", 1, 12)); // NOI18N
        jButton5.setForeground(new java.awt.Color(253, 244, 227));
        jButton5.setText("Reportes de comandas");
        jButton5.setToolTipText("");
        jButton5.setBorder(null);
        jButton5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton5.addActionListener(this::jButton5ActionPerformed);

        jButton6.setBackground(new java.awt.Color(19, 70, 134));
        jButton6.setFont(new java.awt.Font("Segoe UI Historic", 1, 12)); // NOI18N
        jButton6.setForeground(new java.awt.Color(253, 244, 227));
        jButton6.setText("Reportes de clientes");
        jButton6.setToolTipText("");
        jButton6.setBorder(null);
        jButton6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton6.addActionListener(this::jButton6ActionPerformed);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton5)
                    .addComponent(jButton6))
                .addGap(20, 20, 20))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel2.setBackground(new java.awt.Color(19, 70, 134));
        jLabel2.setFont(new java.awt.Font("Segoe UI Emoji", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(19, 70, 134));
        jLabel2.setText("Clientes");

        pnlBuscador.setLayout(new java.awt.BorderLayout());

        jLabel3.setFont(new java.awt.Font("Segoe UI Emoji", 2, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(19, 70, 134));
        jLabel3.setText("Buscar clientes por su nombre, número de teléfono o correo:");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nombre(s)", "Apellido Paterno", "Apellido Materno", "Teléfono", "Correo", "Fecha Registro", "Puntos", "Acción"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(buscadorClientesPanelFORM1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 730, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(511, 511, 511)
                                .addComponent(pnlBuscador, javax.swing.GroupLayout.PREFERRED_SIZE, 501, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 382, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(339, 339, 339)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel2)
                .addGap(20, 20, 20)
                .addComponent(pnlBuscador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buscadorClientesPanelFORM1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(102, 102, 102)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 931, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton6ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private itson.restaurantepresentacion.BuscadorClientesPanelFORM buscadorClientesPanelFORM1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JPanel pnlBuscador;
    // End of variables declaration//GEN-END:variables
}
