/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.restaurantenegocio;

import itson.restaurantedominio.Producto;
import itson.restaurantedtos.DetallesRecetaDTO;
import itson.restaurantedtos.NuevoProductoDTO;
import itson.restaurantedtos.ProductoDTO;
import itson.restaurantedtos.TipoProducto;
import itson.restaurantenegocio.adapters.ProductoAProductosDTOAdapter;
import static itson.restaurantenegocio.adapters.ProductoAProductosDTOAdapter.convertirAListaDTO;
import itson.restaurantepersistencia.IProductosDAO;
import itson.restaurantepersistencia.PersistenciaException;
import itson.restaurantepersistencia.ProductosDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author Nahomi Figueroa
 */
public class ProductosBO implements IProductosBO {

    private static final Logger LOGGER = Logger.getLogger(ProductosBO.class.getName());
    private final IProductosDAO productosDAO = new ProductosDAO();

    /**
     * Registra un nuevo producto en el sistema validando las reglas de negocio
     * pertinentes.
     *
     * @param productoNuevo Objeto DTO que contiene la información del producto
     * a registrar.
     * @return El Producto (entidad).
     * @throws NegocioException Si los datos del producto no cumplen con las
     * validaciones o ocurre un error en la capa de negocio.
     */
    @Override
    public Producto agregarProducto(NuevoProductoDTO productoNuevo) throws NegocioException {
        if (productoNuevo == null) {
            throw new NegocioException("El producto a registrar no puede ser nulo.");
        }
        if (productoNuevo.getNombre() == null || productoNuevo.getNombre().trim().isEmpty()) {
            throw new NegocioException("El nombre del producto es obligatorio y no puede estar vacío.");
        }
        if (productoNuevo.getNombre().length() > 100) {
            throw new NegocioException("El nombre del producto excede el límite permitido de 100 caracteres.");
        }
        if (productoNuevo.getPrecio() == null || productoNuevo.getPrecio() <= 0) {
            throw new NegocioException("El precio del producto debe ser un valor mayor a cero.");
        }
        if (productoNuevo.getTipo() == null) {
            throw new NegocioException("Se debe especificar el tipo de producto.");
        }
        if (productoNuevo.getDetallesReceta() == null || productoNuevo.getDetallesReceta().isEmpty()) {
            throw new NegocioException("El producto debe contener al menos un ingrediente en su receta.");
        }
        for (DetallesRecetaDTO detalle : productoNuevo.getDetallesReceta()) {
            if (detalle.getIdIngrediente() == null || detalle.getIdIngrediente() <= 0) {
                throw new NegocioException("Todos los ingredientes de la receta deben ser válidos.");
            }
            if (detalle.getCantidad() == null || detalle.getCantidad() <= 0) {
                throw new NegocioException("La cantidad requerida de cada ingrediente debe ser mayor a cero.");
            }
        }
        if (productoNuevo.getImagen() != null) {
            if (productoNuevo.getImagen().length > 2097152) {
                throw new NegocioException("La imagen seleccionada es demasiado grande. El límite es 2 MB.");
            }
        }
        try {
            return productosDAO.guardar(productoNuevo);
        } catch (PersistenciaException ex) {
            LOGGER.severe(() -> "Error al guardar el producto: " + ex.getMessage());
            throw new NegocioException(ex.getMessage(), ex);
        }
    }

    /**
     * Actualiza la información o el estado de un producto ya existente en la
     * base de datos.
     *
     * @param productoActualizar Objeto DTO con los datos actualizados del
     * producto.
     * @return El Producto tras aplicar los cambios.
     * @throws NegocioException Si el producto no existe o si los nuevos datos
     * invalidan las reglas de negocio.
     */
    @Override
    public Producto modificarProducto(ProductoDTO productoActualizar) throws NegocioException {
        if (productoActualizar == null) {
            throw new NegocioException("Los datos de actualización no pueden ser nulos.");
        }
        if (productoActualizar.getId() == null || productoActualizar.getId() <= 0) {
            throw new NegocioException("El ID  del producto es inválido.");
        }
        if (productoActualizar.getImagen() != null) {
            if (productoActualizar.getImagen().length > 2097152) {
                throw new NegocioException("La nueva imagen seleccionada es demasiado grande. El límite es 2 MB.");
            }
        }
        if (productoActualizar.getNombre() != null) {
            if (productoActualizar.getNombre().trim().isEmpty()) {
                throw new NegocioException("El nombre del producto no puede quedar en blanco.");
            }
            if (productoActualizar.getNombre().length() > 100) {
                throw new NegocioException("El nombre del producto excede el límite permitido de 100 caracteres.");
            }
        }

        if (productoActualizar.getPrecio() != null && productoActualizar.getPrecio() <= 0) {
            throw new NegocioException("El precio del producto actualizado debe ser mayor a cero.");
        }

        if (productoActualizar.getDetallesReceta() != null && !productoActualizar.getDetallesReceta().isEmpty()) {
            for (DetallesRecetaDTO detalle : productoActualizar.getDetallesReceta()) {
                if (detalle.getIdIngrediente() == null || detalle.getIdIngrediente() <= 0) {
                    throw new NegocioException("Identificador de ingrediente inválido en la actualización de receta.");
                }
                if (detalle.getCantidad() == null || detalle.getCantidad() <= 0) {
                    throw new NegocioException("La cantidad de los ingredientes en la nueva receta debe ser mayor a cero.");
                }
            }
        }
        try {
            return productosDAO.actualizar(productoActualizar);
        } catch (PersistenciaException ex) {
            LOGGER.severe("No se pudo modificar el producto: " + ex.getMessage());
            throw new NegocioException(ex.getMessage(), ex);
        }
    }

    /**
     * Recupera la información detallada de un producto mediante su ID.
     *
     * @param idProducto ID del producto a buscar.
     * @return Un ProductoDTO con la información del producto encontrado.
     * @throws NegocioException Si no se encuentra un producto con el ID
     * proporcionado.
     */
    @Override
    public ProductoDTO obtenerProductoPorId(Long idProducto) throws NegocioException {
        if (idProducto == null || idProducto <= 0) {
            throw new NegocioException("El ID del producto proporcionado es inválido.");
        }

        try {
            Producto producto = productosDAO.buscarPorId(idProducto);
            return ProductoAProductosDTOAdapter.convertirADTO(producto);
        } catch (PersistenciaException ex) {
            LOGGER.severe("Error en negocio al consultar ID " + idProducto + ": " + ex.getMessage());
            throw new NegocioException(ex.getMessage(), ex);
        }
    }

    /**
     * Verifica si un producto está marcado como activo y si cuenta con el stock
     * suficiente de sus ingredientes para ser procesado.
     *
     * @param idProducto Identificador único del producto a verificar.
     * @return True si el producto está disponible y tiene stock, False en caso
     * contrario.
     * @throws NegocioException Si ocurre un error al consultar el inventario o
     * el estado del producto.
     */
    @Override
    public boolean verificarDisponibilidad(Long idProducto) throws NegocioException {
        if (idProducto == null || idProducto <= 0) {
            throw new NegocioException("El ID del producto es necesario para verificar su disponibilidad.");
        }

        try {
            return productosDAO.verificarDisponibilidad(idProducto);
        } catch (PersistenciaException ex) {
            LOGGER.severe("Error en negocio al verificar disponibilidad: " + ex.getMessage());
            throw new NegocioException("No se pudo comprobar la disponibilidad del producto. Verifique los datos e intente de nuevo.", ex);
        }
    }

    /**
     * Busca productos que se encuentren en estado activo y cuyo nombre o
     * categoría coincidan con el criterio de búsqueda.
     *
     * @param nombre Cadena de texto para filtrar la búsqueda por coincidencia.
     * @return Una lista de ProductoDTO que cumplen con los criterios de
     * búsqueda.
     * @throws NegocioException Si ocurre un error durante la consulta.
     */
    @Override
    public List<ProductoDTO> buscarPorNombreActivos(String nombre) throws NegocioException {
        try {
            if (nombre == null || nombre.trim().isEmpty()) {
                return new ArrayList<>();
            }
            List<Producto> productos = productosDAO.buscarPorNombreActivos(nombre);
            return convertirAListaDTO(productos);
        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al realizar la búsqueda por nombre.", ex);
        }
    }

    /**
     * Recupera el catálogo completo de productos registrados en el sistema,
     * independientemente de su estado.
     *
     * @return Una lista con todos los ProductoDTO existentes.
     * @throws NegocioException Si ocurre un error al recuperar la información.
     */
    @Override
    public List<ProductoDTO> obtenerTodosLosProductos() throws NegocioException {
        try {
            List<Producto> productos = productosDAO.consultarTodosProductos();
            return convertirAListaDTO(productos);
        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al obtener el catálogo de productos.", ex);
        }
    }

    /**
     * Filtra y recupera los productos pertenecientes a un tipo o categoría
     * específica.
     *
     * @param tipo El TipoProducto por el cual se desea filtrar.
     * @return Una lista de ProductoDTO que pertenecen al tipo especificado.
     * @throws NegocioException Si el tipo de producto es nulo o ocurre un error
     * en la búsqueda.
     */
    @Override
    public List<ProductoDTO> buscarPorTipo(TipoProducto tipo) throws NegocioException {
        try {
            List<Producto> productos = productosDAO.buscarPorTipo(tipo);
            return convertirAListaDTO(productos);
        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al filtrar por categoría.", ex);
        }
    }
}
