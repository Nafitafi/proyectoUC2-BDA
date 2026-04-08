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

    @Override
    public Producto obtenerProductoPorId(Long idProducto) throws NegocioException {
        if (idProducto == null || idProducto <= 0) {
            throw new NegocioException("El ID del producto proporcionado es inválido.");
        }

        try {
            return productosDAO.buscarPorId(idProducto);
        } catch (PersistenciaException ex) {
            LOGGER.severe("Error en negocio al consultar ID " + idProducto + ": " + ex.getMessage());
            throw new NegocioException(ex.getMessage(), ex);
        }
    }

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

    @Override
    public List<ProductoDTO> buscarPorNombreActivos(String nombre) throws NegocioException {
        try {
            if (nombre == null || nombre.trim().isEmpty()) {
                return new ArrayList<>(); // O puedes lanzar excepción/traer todos, según prefieras
            }
            List<Producto> productos = productosDAO.buscarPorNombreActivos(nombre);
            return convertirAListaDTO(productos);
        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al realizar la búsqueda por nombre.", ex);
        }
    }

    @Override
    public List<ProductoDTO> obtenerTodosLosProductos() throws NegocioException {
        try {
            List<Producto> productos = productosDAO.consultarTodosProductos();
            return convertirAListaDTO(productos); 
        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al obtener el catálogo de productos.", ex);
        }
    }

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
