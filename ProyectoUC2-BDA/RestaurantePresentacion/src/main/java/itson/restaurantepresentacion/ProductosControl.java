/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.restaurantepresentacion;

import itson.restaurantedominio.Producto;
import itson.restaurantedtos.EstadoProducto;
import itson.restaurantedtos.ProductoDTO;
import itson.restaurantedtos.TipoProducto;
import itson.restaurantenegocio.IProductosBO;
import itson.restaurantenegocio.NegocioException;
import itson.restaurantenegocio.ProductosBO;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author nafbr
 */
public class ProductosControl {

    private final IProductosBO productosBO;

    public ProductosControl() {
        this.productosBO = new ProductosBO();
    }

    /**
     * Obtiene la lista de todos los productos registrados.
     *
     * * @return Lista de ProductoDTO.
     * @throws NegocioException Si ocurre un error en la capa de negocio.
     */
    public List<ProductoDTO> consultarTodosProductos() throws Exception {
        try {
            return productosBO.obtenerTodosLosProductos();
        } catch (NegocioException ex) {
            throw new Exception(ex.getMessage(), ex);
        }
    }

    /**
     * Actualiza únicamente el estado de un producto (Activo/Inactivo).
     *
     * * @param id El identificador del producto.
     * @param nuevoEstado El nuevo estado a asignar.
     * @throws Exception Si ocurre un error en la capa de negocio.
     */
    public void actualizarEstado(Long id, EstadoProducto nuevoEstado) throws Exception {
        try {
            ProductoDTO productoActualizar = new ProductoDTO();
            productoActualizar.setId(id);
            productoActualizar.setEstado(nuevoEstado);

            productosBO.modificarProducto(productoActualizar);
        } catch (NegocioException ex) {
            throw new Exception(ex.getMessage(), ex);
        }
    }

    /**
     * Busca productos activos que coincidan con el nombre dado.
     *
     * * @param nombre Nombre o fragmento del nombre a buscar.
     * @return Lista de ProductoDTO.
     * @throws Exception Si ocurre un error en la capa de negocio.
     */
    public List<ProductoDTO> buscarPorNombre(String nombre) throws Exception {
        try {
            return productosBO.buscarPorNombreActivos(nombre);
        } catch (NegocioException ex) {
            throw new Exception(ex.getMessage(), ex);
        }
    }

    /**
     * Filtra los productos por su categoría/tipo.
     *
     * * @param tipo El tipo de producto (Bebida, Platillo, etc.).
     * @return Lista de ProductoDTO.
     * @throws Exception Si ocurre un error en la capa de negocio.
     */
    public List<ProductoDTO> buscarPorTipo(TipoProducto tipo) throws Exception {
        try {
            return productosBO.buscarPorTipo(tipo);
        } catch (NegocioException ex) {
            throw new Exception(ex.getMessage(), ex);
        }
    }

    /**
     * Busca los productos activos que coincidan con el nombre proporcionado.
     * * @param nombre El texto a buscar en el nombre del producto.
     * @return Lista de ProductoDTO que coinciden con la búsqueda.
     * @throws NegocioException Si ocurre un error en la capa de negocio.
     */
    public List<ProductoDTO> buscarPorNombreActivos(String nombre) throws NegocioException {
        // productosBO debe estar instanciado en el constructor de tu controlador
        return productosBO.buscarPorNombreActivos(nombre);
    }

    
    public void abrirRegistroProducto(ProductosFORM vista) {
        RegistroProductoFORM registroForm = new RegistroProductoFORM();
        registroForm.setLocationRelativeTo(null); 
        registroForm.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                vista.cargarTablaProductos(); 
            }
        });
        registroForm.setVisible(true);
    }
}
