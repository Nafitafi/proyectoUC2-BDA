/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package itson.restaurantepersistencia;

import itson.restaurantedominio.Producto;
import itson.restaurantedtos.EstadoProducto;
import itson.restaurantedtos.NuevoProductoDTO;
import itson.restaurantedtos.ProductoDTO;
import itson.restaurantedtos.TipoProducto;
import java.util.List;

/**
 *
 * @author nafbr
 */
public interface IProductosDAO {
    
    List<Producto> consultarTodosProductos() throws PersistenciaException;

    Producto guardar(NuevoProductoDTO productoNuevo) throws PersistenciaException;

    boolean existe(Producto producto) throws PersistenciaException;

    Producto actualizar(ProductoDTO productoActualizado) throws PersistenciaException;

    Producto buscarPorId(Long idProducto) throws PersistenciaException;

    boolean verificarDisponibilidad(Long idProducto) throws PersistenciaException;

    List<Producto> buscarPorNombreActivos(String nombreParcial) throws PersistenciaException;
    
    List<Producto> buscarPorTipo(TipoProducto tipo) throws PersistenciaException;

    /**
     * Actualiza únicamente el estado de un producto.
     *
     * * @param id El ID del producto.
     * @param nuevoEstado El nuevo estado (ej. "ACTIVO" o "INACTIVO").
     * @throws PersistenciaException Si ocurre un error en la base de datos.
     */
    void actualizarEstado(Long id, EstadoProducto nuevoEstado) throws PersistenciaException;
}
