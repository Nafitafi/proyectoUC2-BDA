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
 * Interfaz que define las operaciones de persistencia para la entidad Producto.
 * Proporciona métodos para el acceso, búsqueda y modificación de productos en
 * el almacén de datos.
 *
 * @author Nahomi Figueroa
 */
public interface IProductosDAO {

    /**
     * Recupera la lista completa de productos almacenados en la base de datos.
     *
     * @return Una lista de todos los objetos Producto.
     * @throws PersistenciaException Si ocurre un error técnico al consultar la
     * base de datos.
     */
    List<Producto> consultarTodosProductos() throws PersistenciaException;

    /**
     * Inserta un nuevo registro de producto en la base de datos.
     *
     * @param productoNuevo Objeto DTO con la información necesaria para el
     * nuevo registro.
     * @return El Producto persistido, incluyendo su identificador generado.
     * @throws PersistenciaException Si hay un error en la inserción o violación
     * de restricciones.
     */
    Producto guardar(NuevoProductoDTO productoNuevo) throws PersistenciaException;

    /**
     * Determina si un producto ya existe en el almacén de datos basándose en
     * sus atributos únicos.
     *
     * @param producto El objeto a verificar.
     * @return true si el producto ya existe, false en caso contrario.
     * @throws PersistenciaException Si ocurre un error durante la consulta de
     * existencia.
     */
    boolean existe(Producto producto) throws PersistenciaException;

    /**
     * Modifica los datos de un registro de producto existente.
     *
     * @param productoActualizado DTO con los nuevos valores a persistir.
     * @return El Producto con los cambios aplicados.
     * @throws PersistenciaException Si el producto no se encuentra o la
     * actualización falla.
     */
    Producto actualizar(ProductoDTO productoActualizado) throws PersistenciaException;

    /**
     * Encuentra un producto específico mediante su ID.
     *
     * @param idProducto ID del producto.
     * @return El Producto encontrado o null si no existe.
     * @throws PersistenciaException Si ocurre un error en la búsqueda.
     */
    Producto buscarPorId(Long idProducto) throws PersistenciaException;

    /**
     * Consulta el estado y los recursos asociados a un producto para determinar
     * si puede ser vendido.
     *
     * @param idProducto ID del producto a verificar.
     * @return true si está disponible en inventario y activo, false de lo
     * contrario.
     * @throws PersistenciaException Si ocurre un error al acceder a las tablas
     * de inventario o productos.
     */
    boolean verificarDisponibilidad(Long idProducto) throws PersistenciaException;

    /**
     * Realiza una búsqueda de productos activos cuyo nombre contenga la cadena
     * proporcionada.
     *
     * @param nombreParcial Fragmento de texto para filtrar por nombre.
     * @return Lista de productos que coinciden con el criterio y están activos.
     * @throws PersistenciaException Si ocurre un error en la ejecución del
     * filtro.
     */
    List<Producto> buscarPorNombreActivos(String nombreParcial) throws PersistenciaException;

    /**
     * Filtra los productos almacenados de acuerdo a su categoría o tipo.
     *
     * * @param tipo El TipoProducto por el cual filtrar.
     * @return Lista de productos pertenecientes a la categoría especificada.
     * @throws PersistenciaException Si ocurre un error durante la consulta.
     */
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
