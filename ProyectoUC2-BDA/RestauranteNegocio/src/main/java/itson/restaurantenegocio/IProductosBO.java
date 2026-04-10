/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package itson.restaurantenegocio;

import itson.restaurantedominio.Producto;
import itson.restaurantedtos.NuevoProductoDTO;
import itson.restaurantedtos.ProductoDTO;
import itson.restaurantedtos.TipoProducto;
import java.util.List;

/**
 *
 * @author Nahomi Figueroa
 */
public interface IProductosBO {

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
    Producto agregarProducto(NuevoProductoDTO productoNuevo) throws NegocioException;

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
    Producto modificarProducto(ProductoDTO productoActualizar) throws NegocioException;

    /**
     * Recupera la información detallada de un producto mediante su ID.
     *
     * @param idProducto ID del producto a buscar.
     * @return Un ProductoDTO con la información del producto encontrado.
     * @throws NegocioException Si no se encuentra un producto con el ID
     * proporcionado.
     */
    ProductoDTO obtenerProductoPorId(Long idProducto) throws NegocioException;

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
    boolean verificarDisponibilidad(Long idProducto) throws NegocioException;

    /**
     * Busca productos que se encuentren en estado activo y cuyo nombre o
     * categoría coincidan con el criterio de búsqueda.
     *
     * @param nombre Cadena de texto para filtrar la búsqueda por coincidencia.
     * @return Una lista de ProductoDTO que cumplen con los criterios de
     * búsqueda.
     * @throws NegocioException Si ocurre un error durante la consulta.
     */
    List<ProductoDTO> buscarPorNombreActivos(String nombre) throws NegocioException;

    /**
     * Recupera el catálogo completo de productos registrados en el sistema,
     * independientemente de su estado.
     *
     * @return Una lista con todos los ProductoDTO existentes.
     * @throws NegocioException Si ocurre un error al recuperar la información.
     */
    List<ProductoDTO> obtenerTodosLosProductos() throws NegocioException;

    /**
     * Filtra y recupera los productos pertenecientes a un tipo o categoría
     * específica.
     *
     * @param tipo El TipoProducto por el cual se desea filtrar.
     * @return Una lista de ProductoDTO que pertenecen al tipo especificado.
     * @throws NegocioException Si el tipo de producto es nulo o ocurre un error
     * en la búsqueda.
     */
    List<ProductoDTO> buscarPorTipo(TipoProducto tipo) throws NegocioException;
}
