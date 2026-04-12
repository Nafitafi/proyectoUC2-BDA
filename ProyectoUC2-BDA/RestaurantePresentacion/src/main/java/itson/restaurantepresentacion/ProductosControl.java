/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.restaurantepresentacion;

import itson.restaurantedtos.DetallesRecetaDTO;
import itson.restaurantedtos.EstadoProducto;
import itson.restaurantedtos.NuevoProductoDTO;
import itson.restaurantedtos.ProductoDTO;
import itson.restaurantedtos.TipoProducto;
import itson.restaurantenegocio.IIngredientesBO;
import itson.restaurantenegocio.IProductosBO;
import itson.restaurantenegocio.IngredientesBO;
import itson.restaurantenegocio.NegocioException;
import itson.restaurantenegocio.ProductosBO;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Nahomi Figueroa
 */
public class ProductosControl {

    private final IProductosBO productosBO;
    private final ProductosFORM vista;

    public ProductosControl(ProductosFORM vista) {
        this.vista = vista;
        this.productosBO = new ProductosBO();
    }

    public ProductosControl() {
        this.productosBO = new ProductosBO();
        this.vista = null;
    }

    /**
     * Obtiene la lista de todos los productos registrados.
     *
     * @return Lista de ProductoDTO.
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
     * @param id El identificador del producto.
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
     * @param nombre Nombre o fragmento del nombre a buscar.
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
     * @param tipo El tipo de producto (Bebida, Platillo, etc.).
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
     *
     * @param nombre El texto a buscar en el nombre del producto.
     * @return Lista de ProductoDTO que coinciden con la búsqueda.
     * @throws NegocioException Si ocurre un error en la capa de negocio.
     */
    public List<ProductoDTO> buscarPorNombreActivos(String nombre) throws NegocioException {
        return productosBO.buscarPorNombreActivos(nombre);
    }

    /**
     * Método para abrir la ventana de registro de un producto nuevo.
     */
    public void abrirRegistroProducto() {
        RegistroProductoFORM registroForm = new RegistroProductoFORM();
        registroForm.setLocationRelativeTo(null);
        registroForm.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                vista.cargarTablaProductos();
            }
        });
        registroForm.setVisible(true);
    }

    /**
     * Método para abrir la ventana de actualización de un producto específico.
     *
     * @param idProducto El ID del producto que se va a editar.
     * @return
     */
    public JFrame abrirActualizarProducto(Long idProducto) {
        ActualizarProductoFORM actualizar = new ActualizarProductoFORM(idProducto);
        actualizar.setLocationRelativeTo(null);
        return actualizar;
    }

    /**
     * Método que los datos crudos desde la vista, los transforma, arma el DTO y
     * delega la validación/guardado a la capa de Negocio.
     */
    public void guardarNuevoProducto(String nombre, String descripcion, String precioStr, String tipoStr,
            byte[] imagenBytes, List<Object[]> ingredientes, JFrame vista) {

        if (nombre == null || nombre.trim().isEmpty() || precioStr == null || precioStr.trim().isEmpty() || ingredientes.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Nombre, precio y al menos un ingrediente son obligatorios.", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {

            Double precio = Double.valueOf(precioStr);
            TipoProducto tipoProducto = TipoProducto.valueOf(tipoStr);

            NuevoProductoDTO productoNuevo = new NuevoProductoDTO();
            productoNuevo.setNombre(nombre.trim());
            productoNuevo.setDescripcion(descripcion.trim());
            productoNuevo.setPrecio(precio);
            productoNuevo.setTipo(tipoProducto);
            productoNuevo.setImagen(imagenBytes);

            List<DetallesRecetaDTO> listaReceta = new ArrayList<>();
            for (Object[] fila : ingredientes) {
                DetallesRecetaDTO detalle = new DetallesRecetaDTO();
                detalle.setIdIngrediente((Long) fila[0]);
                detalle.setCantidad(((Float) fila[1]).doubleValue());
                listaReceta.add(detalle);
            }
            productoNuevo.setDetallesReceta(listaReceta);

            productosBO.agregarProducto(productoNuevo);

            JOptionPane.showMessageDialog(vista, "¡Producto guardado exitosamente!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            new ProductosFORM().setVisible(true);
            vista.dispose();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista, "El precio debe ser un número válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(vista, "Seleccione un Tipo de Producto válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(vista, ex.getMessage(), "Aviso de Negocio", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Ocurrió un error inesperado: " + ex.getMessage(), "Error del Sistema", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método auxiliar para obtener el producto a editar.
     * 
     * @param idProducto
     * @param vistaDialog
     * @return 
     */
    public ProductoDTO obtenerProductoParaEdicion(Long idProducto, JFrame vistaDialog) {
        try {
            return productosBO.obtenerProductoPorId(idProducto);
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(vistaDialog, "Aviso: " + ex.getMessage(), "Aviso de Negocio", JOptionPane.WARNING_MESSAGE);
            return null;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vistaDialog, "Error al cargar los datos del producto: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    /**
     * Método que se encarga de obtener los ingredientes de la tabla
     * @param detalles
     * @param vistaDialog
     * @return 
     */
    public List<Object[]> obtenerFilasIngredientesTabla(List<DetallesRecetaDTO> detalles, JFrame vistaDialog) {
        List<Object[]> filas = new ArrayList<>();
        if (detalles == null || detalles.isEmpty()) {
            return filas;
        }

        try {
            IIngredientesBO ingredientesBO = new IngredientesBO();

            for (DetallesRecetaDTO detalle : detalles) {
                itson.restaurantedominio.Ingrediente ingrediente = ingredientesBO.buscar(detalle.getIdIngrediente());
                if (ingrediente != null) {
                    filas.add(new Object[]{
                        detalle.getIdIngrediente(),
                        ingrediente.getNombre(),
                        detalle.getCantidad(),
                        ingrediente.getUnidadMedida(),
                        "Borrar"
                    });
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vistaDialog, "Error al cargar los detalles de la receta: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return filas;
    }

    /**
     * Método que se comunica con la BO para registrar un producto neuvo
     * @param idProducto
     * @param nombre
     * @param descripcion
     * @param precioStr
     * @param tipoStr
     * @param estadoStr
     * @param imagenBytes
     * @param ingredientes
     * @param vistaDialog 
     */
    public void guardarActualizacionProducto(Long idProducto, String nombre, String descripcion, String precioStr,
            String tipoStr, String estadoStr, byte[] imagenBytes, List<Object[]> ingredientes, JFrame vistaDialog) {

        if (nombre.isEmpty() || precioStr.isEmpty() || ingredientes.isEmpty()) {
            JOptionPane.showMessageDialog(vistaDialog, "Nombre, Precio y al menos un ingrediente son obligatorios.", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Double precio = Double.valueOf(precioStr);
            if (precio <= 0) {
                JOptionPane.showMessageDialog(vistaDialog, "El precio debe ser mayor a 0.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
                return;
            }

            TipoProducto tipoProducto = TipoProducto.valueOf(tipoStr.toUpperCase());
            EstadoProducto estadoEnum = EstadoProducto.valueOf(estadoStr.toUpperCase());

            ProductoDTO productoActualizado = new ProductoDTO();
            productoActualizado.setId(idProducto);
            productoActualizado.setNombre(nombre);
            productoActualizado.setDescripcion(descripcion);
            productoActualizado.setPrecio(precio);
            productoActualizado.setTipo(tipoProducto);
            productoActualizado.setEstado(estadoEnum);
            productoActualizado.setImagen(imagenBytes);

            List<DetallesRecetaDTO> listaReceta = new ArrayList<>();
            for (Object[] fila : ingredientes) {
                DetallesRecetaDTO detalle = new DetallesRecetaDTO();
                detalle.setIdIngrediente((Long) fila[0]);
                detalle.setCantidad(Double.valueOf(fila[1].toString()));
                listaReceta.add(detalle);
            }
            productoActualizado.setDetallesReceta(listaReceta);

            productosBO.modificarProducto(productoActualizado);

            JOptionPane.showMessageDialog(vistaDialog, "¡Producto actualizado exitosamente!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            vistaDialog.dispose();
            new ProductosFORM().setVisible(true);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vistaDialog, "Verifique que el precio y las cantidades sean números válidos.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(vistaDialog, "Seleccione un Tipo/Estado de Producto válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(vistaDialog, ex.getMessage(), "Aviso de Negocio", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vistaDialog, "Ocurrió un error inesperado: " + ex.getMessage(), "Error del Sistema", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void regresarMenu() {
        MenuAdminFORM menuAdmin = new MenuAdminFORM();
        new MenuAdministradorControl(menuAdmin);
        menuAdmin.setLocationRelativeTo(null);
        menuAdmin.setVisible(true);
    }
}
