/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.restaurantepersistencia.adapters;

import itson.restaurantedominio.Producto;
import itson.restaurantedtos.ProductoDTO;

/**
 *
 * @author nafbr
 */
public class ProductoDTOAProductoAdapter {

    public static Producto adaptar(ProductoDTO productoActualizar) {
        if (productoActualizar == null) {
            return null;
        }

        Producto producto = new Producto();
        producto.setIdProducto(productoActualizar.getId());

        if (productoActualizar.getNombre() != null) {
            producto.setNombre(productoActualizar.getNombre().trim());
        }

        producto.setDescripcion(productoActualizar.getDescripcion());
        producto.setPrecio(productoActualizar.getPrecio());
        producto.setImagen(productoActualizar.getImagen());
        
        if (productoActualizar.getTipo() != null) {
            itson.restaurantedominio.TipoProducto tipoDominio
                    = itson.restaurantedominio.TipoProducto.valueOf(productoActualizar.getTipo().name());
            producto.setTipo(tipoDominio);
        }

        if (productoActualizar.getEstado() != null) {
            itson.restaurantedominio.EstadoProducto estadoDominio
                    = itson.restaurantedominio.EstadoProducto.valueOf(productoActualizar.getEstado().name());
            producto.setEstado(estadoDominio);
        }

        return producto;
    }
}
