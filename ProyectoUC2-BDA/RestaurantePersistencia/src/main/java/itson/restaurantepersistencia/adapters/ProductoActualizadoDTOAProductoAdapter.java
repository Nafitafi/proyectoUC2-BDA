/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.restaurantepersistencia.adapters;

import itson.restaurantedominio.Producto;
import itson.restaurantedtos.ProductoActualizadoDTO;

/**
 *
 * @author nafbr
 */
public class ProductoActualizadoDTOAProductoAdapter {

    public static Producto adaptar(ProductoActualizadoDTO productoActualizar) {
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
