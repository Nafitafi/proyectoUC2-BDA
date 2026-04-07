/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package itson.restaurantepersistencia;

import itson.restaurantedominio.Producto;
import itson.restaurantedtos.NuevoProductoDTO;
import itson.restaurantedtos.ProductoActualizadoDTO;
import itson.restaurantedtos.TipoProducto;
import java.util.List;

/**
 *
 * @author nafbr
 */
public interface IProductosDAO {
    
    Producto guardar(NuevoProductoDTO productoNuevo) throws PersistenciaException;
    
    boolean existe(Producto producto) throws PersistenciaException;
    
    Producto actualizar(ProductoActualizadoDTO productoActualizado) throws PersistenciaException;
    
    Producto buscarPorId(Long idProducto) throws PersistenciaException;
    
    boolean verificarDisponibilidad(Long idProducto) throws PersistenciaException;
    
    List<Producto> buscarProductosActivosFiltro(String nombreParcial, TipoProducto categoria) throws PersistenciaException;
}
