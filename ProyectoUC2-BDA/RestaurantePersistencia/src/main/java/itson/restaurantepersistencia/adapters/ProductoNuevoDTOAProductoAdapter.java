/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.restaurantepersistencia.adapters;

import itson.restaurantedominio.Producto;
import itson.restaurantedtos.NuevoProductoDTO;

/**
 *
 * @author nafbr
 */
public class ProductoNuevoDTOAProductoAdapter {

    public static Producto adaptar(NuevoProductoDTO productoNuevo) {
        if (productoNuevo == null) {
            return null;
        }
        Producto producto = new Producto();

        if (productoNuevo.getNombre() != null) {
            producto.setNombre(productoNuevo.getNombre().trim());
        }

        producto.setDescripcion(productoNuevo.getDescripcion());
        producto.setPrecio(productoNuevo.getPrecio());

        if (productoNuevo.getTipo() != null) {
            itson.restaurantedominio.TipoProducto tipoDominio
                    = itson.restaurantedominio.TipoProducto.valueOf(productoNuevo.getTipo().name());
            producto.setTipo(tipoDominio);
        }
        producto.setEstado(itson.restaurantedominio.EstadoProducto.ACTIVO);
        return producto;
    }
}
