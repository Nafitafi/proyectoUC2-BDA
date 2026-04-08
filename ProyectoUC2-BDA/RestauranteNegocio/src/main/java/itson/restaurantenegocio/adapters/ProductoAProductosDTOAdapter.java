/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.restaurantenegocio.adapters;

import itson.restaurantedominio.Producto;
import itson.restaurantedtos.ProductoDTO;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Nahomi Figueroa
 */
public class ProductoAProductosDTOAdapter {
    /**
     * Convierte una lista de entidades Producto a una lista de ProductoDTO.
     *
     * @param productos Lista de entidades provenientes de la base de datos (DAO).
     * @return Lista de ProductoDTO listos para la capa de presentación.
     */
    public static List<ProductoDTO> convertirAListaDTO(List<Producto> productos) {
        List<ProductoDTO> listaDTO = new ArrayList<>();
        
        // Validación de seguridad por si la base de datos no devuelve nada
        if (productos == null || productos.isEmpty()) {
            return listaDTO;
        }

        // Recorremos la lista y usamos el método individual para convertir cada uno
        for (Producto producto : productos) {
            listaDTO.add(convertirADTO(producto));
        }
        
        return listaDTO;
    }
    
    /**
     * Convierte una sola entidad Producto a ProductoDTO.
     *
     * @param p Entidad Producto.
     * @return ProductoDTO.
     */
    public static ProductoDTO convertirADTO(Producto p) {
        if (p == null) {
            return null;
        }
        
        ProductoDTO dto = new ProductoDTO();
        dto.setId(p.getId());
        dto.setNombre(p.getNombre());
        if (p.getTipo() != null) {
            dto.setTipo(itson.restaurantedtos.TipoProducto.valueOf(p.getTipo().name()));
        }
        
        if (p.getEstado() != null) {
            dto.setEstado(itson.restaurantedtos.EstadoProducto.valueOf(p.getEstado().name()));
        }
        dto.setPrecio(p.getPrecio());
        dto.setImagen(p.getImagen());   
        
        return dto;
    }
}
