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
     * Registra un nuevo producto validando las reglas de negocio.
     * @param productoNuevo
     * @return 
     * @throws itson.restaurantenegocio.NegocioException
     */
    Producto agregarProducto(NuevoProductoDTO productoNuevo) throws NegocioException;

    /**
     * Actualiza la información o el estado de un producto existente.
     * @param productoActualizar
     * @return 
     * @throws itson.restaurantenegocio.NegocioException
     */
    Producto modificarProducto(ProductoDTO productoActualizar) throws NegocioException;

    /**
     * Recupera un producto por su identificador.
     * @param idProducto
     * @return 
     * @throws itson.restaurantenegocio.NegocioException
     */
    Producto obtenerProductoPorId(Long idProducto) throws NegocioException;

    /**
     * Verifica si un producto está activo y tiene stock suficiente de sus ingredientes.
     * @param idProducto
     * @return 
     * @throws itson.restaurantenegocio.NegocioException
     */
    boolean verificarDisponibilidad(Long idProducto) throws NegocioException;

    /**
     * Busca productos activos filtrados por coincidencia de nombre o categoría.
     * @param nombre
     * @return 
     * @throws itson.restaurantenegocio.NegocioException
     */
    List<ProductoDTO> buscarPorNombreActivos(String nombre) throws NegocioException;
    
    List<ProductoDTO> obtenerTodosLosProductos() throws NegocioException;
    
    List<ProductoDTO> buscarPorTipo(TipoProducto tipo) throws NegocioException;
}
