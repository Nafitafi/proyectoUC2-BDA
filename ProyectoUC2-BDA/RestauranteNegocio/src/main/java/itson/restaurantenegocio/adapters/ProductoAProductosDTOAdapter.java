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
 * Clase adaptadora de entidad dominio a objeto java.
 *
 * @author Nahomi Figueroa
 */
public class ProductoAProductosDTOAdapter {

    /**
     * Convierte una lista de entidades Producto a una lista de ProductoDTO.
     *
     * @param productos Lista de entidades provenientes de la base de datos
     * (DAO).
     * @return Lista de ProductoDTO listos para la capa de presentación.
     */
    public static List<ProductoDTO> convertirAListaDTO(List<Producto> productos) {
        List<ProductoDTO> listaDTO = new ArrayList<>();

        if (productos == null || productos.isEmpty()) {
            return listaDTO;
        }
    
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
        dto.setDescripcion(p.getDescripcion());
        if (p.getTipo() != null) {
            dto.setTipo(itson.restaurantedtos.TipoProducto.valueOf(p.getTipo().name()));
        }

        if (p.getEstado() != null) {
            dto.setEstado(itson.restaurantedtos.EstadoProducto.valueOf(p.getEstado().name()));
        }
        dto.setPrecio(p.getPrecio());
        dto.setImagen(p.getImagen());
        if (p.getDetallesReceta() != null && !p.getDetallesReceta().isEmpty()) {
            List<itson.restaurantedtos.DetallesRecetaDTO> listaDetallesDTO = new ArrayList<>();
            for (itson.restaurantedominio.DetallesReceta detalleDominio : p.getDetallesReceta()) {
                itson.restaurantedtos.DetallesRecetaDTO detalleDTO = new itson.restaurantedtos.DetallesRecetaDTO();
                detalleDTO.setIdIngrediente(detalleDominio.getIngrediente().getIdIngrediente());
                detalleDTO.setCantidad(detalleDominio.getCantidadRequerida());

                listaDetallesDTO.add(detalleDTO);
            }
            dto.setDetallesReceta(listaDetallesDTO);
        }
        return dto;
    }
}
