/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.restaurantepersistencia;

import itson.restaurantedominio.Ingrediente;
import itson.restaurantedominio.Producto;
import itson.restaurantedominio.DetallesReceta;
import itson.restaurantedtos.DetallesRecetaDTO;
import itson.restaurantedtos.EstadoProducto;
import itson.restaurantedtos.NuevoProductoDTO;
import itson.restaurantedtos.ProductoDTO;
import itson.restaurantedtos.TipoProducto;
import itson.restaurantepersistencia.adapters.ProductoDTOAProductoAdapter;
import itson.restaurantepersistencia.adapters.ProductoNuevoDTOAProductoAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

/**
 * Implementación de la interfaz IProductosDAO. Maneja las operaciones de
 * persistencia para la entidad Producto, gestionando el acceso, búsqueda y
 * modificación de productos en la base de datos mediante JPA.
 *
 * @author Nahomi Figueroa
 */
public class ProductosDAO implements IProductosDAO {

    private static final Logger LOGGER = Logger.getLogger(ProductosDAO.class.getName());

    /**
     * Inserta un nuevo registro de producto en la base de datos.
     *
     * @param productoNuevo Objeto DTO con la información necesaria para el
     * nuevo registro.
     * @return El Producto persistido, incluyendo su ID generado.
     * @throws PersistenciaException Si hay un error en la inserción o violación
     * de restricciones.
     */
    @Override
    public Producto guardar(NuevoProductoDTO productoNuevo) throws PersistenciaException {
        if (productoNuevo == null) {
            throw new PersistenciaException("El producto no puede ser nulo.");
        }
        if (productoNuevo.getNombre() == null || productoNuevo.getNombre().trim().isEmpty()) {
            throw new PersistenciaException("El nombre del producto no puede estar vacío.");
        }
        if (productoNuevo.getNombre().length() > 100) {
            throw new PersistenciaException("El nombre no puede tener más de 100 caracteres.");
        }
        if (productoNuevo.getPrecio() == null || productoNuevo.getPrecio() <= 0) {
            throw new PersistenciaException("El precio debe ser mayor a cero.");
        }
        if (productoNuevo.getTipo() == null) {
            throw new PersistenciaException("El tipo de producto no puede estar vacío.");
        }
        if (productoNuevo.getDetallesReceta() == null || productoNuevo.getDetallesReceta().isEmpty()) {
            throw new PersistenciaException("El producto debe tener al menos un ingrediente en su receta.");
        }

        EntityManager entityManager = ManejadorConexiones.crearEntityManager();

        try {
            Producto producto = ProductoNuevoDTOAProductoAdapter.adaptar(productoNuevo);
            if (existe(producto)) {
                throw new PersistenciaException("Ya existe un producto activo con ese nombre.");
            }
            List<DetallesReceta> receta = new ArrayList<>();
            for (DetallesRecetaDTO detalle : productoNuevo.getDetallesReceta()) {
                if (detalle.getCantidad() == null || detalle.getCantidad() <= 0) {
                    throw new PersistenciaException("La cantidad de los ingredientes debe ser mayor a cero.");
                }
                Ingrediente ingredienteRef = entityManager.getReference(Ingrediente.class, detalle.getIdIngrediente());
                receta.add(new DetallesReceta(producto, ingredienteRef, detalle.getCantidad()));
            }
            producto.setDetallesReceta(receta);
            entityManager.getTransaction().begin();
            entityManager.persist(producto);
            entityManager.getTransaction().commit();

            return producto;

        } catch (PersistenceException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("No fue posible registrar el producto.");
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }

    /**
     * Modifica los datos de un registro de producto existente.
     *
     * @param productoActualizar DTO con los nuevos valores a persistir.
     * @return El Producto con los cambios aplicados.
     * @throws PersistenciaException Si el producto no se encuentra o la
     * actualización falla.
     */
    @Override
    public Producto actualizar(ProductoDTO productoActualizar) throws PersistenciaException {
        if (productoActualizar == null || productoActualizar.getId() == null) {
            throw new PersistenciaException("Datos insuficientes para actualizar el producto.");
        }
        EntityManager entityManager = ManejadorConexiones.crearEntityManager();

        try {
            entityManager.getTransaction().begin();
            Producto productoExistente = entityManager.find(Producto.class, productoActualizar.getId());
            if (productoExistente == null) {
                throw new PersistenciaException("No se encontró el producto a actualizar.");
            }
            Producto productoNuevosDatos = ProductoDTOAProductoAdapter.adaptar(productoActualizar);

            if (productoNuevosDatos.getNombre() != null) {
                if (productoNuevosDatos.getNombre().length() > 100) {
                    throw new PersistenciaException("El nombre no puede tener más de 100 caracteres.");
                }
                productoExistente.setNombre(productoNuevosDatos.getNombre());
            }

            if (productoNuevosDatos.getDescripcion() != null) {
                productoExistente.setDescripcion(productoNuevosDatos.getDescripcion());
            }
            if (productoNuevosDatos.getPrecio() != null) {
                if (productoNuevosDatos.getPrecio() <= 0) {
                    throw new PersistenciaException("El precio debe ser mayor a cero.");
                }
                productoExistente.setPrecio(productoNuevosDatos.getPrecio());
            }
            if (productoNuevosDatos.getTipo() != null) {
                productoExistente.setTipo(productoNuevosDatos.getTipo());
            }
            if (productoNuevosDatos.getEstado() != null) {
                productoExistente.setEstado(productoNuevosDatos.getEstado());
            }
            if (productoNuevosDatos.getImagen() != null) {
                productoExistente.setImagen(productoNuevosDatos.getImagen());
            }

            if (productoActualizar.getDetallesReceta() != null && !productoActualizar.getDetallesReceta().isEmpty()) {
                productoExistente.getDetallesReceta().clear();
                for (itson.restaurantedtos.DetallesRecetaDTO detalle : productoActualizar.getDetallesReceta()) {
                    if (detalle.getCantidad() == null || detalle.getCantidad() <= 0) {
                        throw new PersistenciaException("La cantidad de los ingredientes debe ser mayor a cero.");
                    }
                    Ingrediente ingredienteRef = entityManager.getReference(Ingrediente.class, detalle.getIdIngrediente());
                    productoExistente.getDetallesReceta().add(new DetallesReceta(productoExistente, ingredienteRef, detalle.getCantidad()));
                }
            }

            entityManager.getTransaction().commit();

            return productoExistente;

        } catch (PersistenceException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("No fue posible actualizar el producto");
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }

    /**
     * Encuentra un producto específico mediante su ID.
     *
     * @param idProducto ID del producto.
     * @return El Producto encontrado o null si no existe.
     * @throws PersistenciaException Si ocurre un error en la búsqueda.
     */
    @Override
    public Producto buscarPorId(Long idProducto) throws PersistenciaException {
        EntityManager entityManager = ManejadorConexiones.crearEntityManager();
        try {
            Producto producto = entityManager.find(Producto.class, idProducto);
            if (producto == null) {
                throw new PersistenciaException("No se encontró el producto con el ID especificado.");
            }
            // Forzamos la carga de la receta antes de cerrar la conexión
            producto.getDetallesReceta().size();
            return producto;
        } catch (PersistenceException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("No fue posible consultar el producto de id: " + idProducto);
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }

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
    @Override
    public boolean verificarDisponibilidad(Long idProducto) throws PersistenciaException {
        EntityManager entityManager = ManejadorConexiones.crearEntityManager();
        try {
            Producto producto = entityManager.find(Producto.class, idProducto);

            if (producto == null || producto.getEstado() != itson.restaurantedominio.EstadoProducto.ACTIVO) {
                return false;
            }

            for (DetallesReceta pi : producto.getDetallesReceta()) {
                if (pi.getIngrediente().getStock() < pi.getCantidadRequerida()) {
                    return false;
                }
            }
            return true;

        } catch (PersistenceException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("No fue posible verificar la disponibilidad del producto.");
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }

    /**
     * Realiza una búsqueda de productos activos cuyo nombre contenga la cadena
     * proporcionada.
     *
     * @param nombre Fragmento de texto para filtrar por nombre.
     * @return Lista de productos que coinciden con el criterio y están activos.
     * @throws PersistenciaException Si ocurre un error en la ejecución del
     * filtro.
     */
    @Override
    public List<Producto> buscarPorNombreActivos(String nombre) throws PersistenciaException {
        EntityManager entityManager = ManejadorConexiones.crearEntityManager();
        try {
            String jpql = "SELECT DISTINCT p FROM Producto p LEFT JOIN FETCH p.detallesReceta WHERE p.estado = :estado AND LOWER(p.nombre) LIKE LOWER(:nombre)";
            TypedQuery<Producto> query = entityManager.createQuery(jpql, Producto.class);
            query.setParameter("estado", itson.restaurantedominio.EstadoProducto.ACTIVO);
            query.setParameter("nombre", "%" + nombre.trim() + "%");
            return query.getResultList();
        } catch (PersistenceException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("No fue posible consultar a los productos.");
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }

    /**
     * Determina si un producto ya existe en el almacén de datos basándose en
     * sus atributos únicos.
     *
     * @param producto El objeto a verificar.
     * @return true si el producto ya existe, false en caso contrario.
     * @throws PersistenciaException Si ocurre un error durante la consulta de
     * existencia.
     */
    @Override
    public boolean existe(Producto producto) throws PersistenciaException {
        EntityManager entityManager = ManejadorConexiones.crearEntityManager();
        try {
            String jpql = "SELECT COUNT(p) FROM Producto p WHERE LOWER(p.nombre) = LOWER(:nom) AND p.estado = :estado";

            if (producto.getIdProducto() != null) {
                jpql += " AND p.idProducto != :id";
            }

            TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class)
                    .setParameter("nom", producto.getNombre())
                    .setParameter("estado", itson.restaurantedominio.EstadoProducto.ACTIVO);

            if (producto.getIdProducto() != null) {
                query.setParameter("id", producto.getIdProducto());
            }

            return query.getSingleResult() > 0;
        } catch (PersistenceException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("No fue posible consultar si el producto existe.");
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }

    /**
     * Actualiza únicamente el estado de un producto.
     *
     * @param id El ID del producto.
     * @param nuevoEstado El nuevo estado (ej. "ACTIVO" o "INACTIVO").
     * @throws PersistenciaException Si ocurre un error en la base de datos.
     */
    @Override
    public void actualizarEstado(Long id, EstadoProducto nuevoEstado) throws PersistenciaException {
        EntityManager entityManager = ManejadorConexiones.crearEntityManager();
        try {
            entityManager.getTransaction().begin();
            Producto producto = entityManager.find(Producto.class, id);
            if (producto != null) {
                itson.restaurantedominio.EstadoProducto estadoConvertido = itson.restaurantedominio.EstadoProducto.valueOf(nuevoEstado.name());
                producto.setEstado(estadoConvertido);
                entityManager.merge(producto);
            }
            entityManager.getTransaction().commit();
        } catch (PersistenceException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("No fue posible actualizar el estado del producto.");
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }

    /**
     * Recupera la lista completa de productos almacenados en la base de datos.
     *
     * @return Una lista de todos los objetos Producto.
     * @throws PersistenciaException Si ocurre un error técnico al consultar la
     * base de datos.
     */
    @Override
    public List<Producto> consultarTodosProductos() throws PersistenciaException {
        EntityManager entityManager = ManejadorConexiones.crearEntityManager();
        try {
            String jpql = "SELECT DISTINCT p FROM Producto p LEFT JOIN FETCH p.detallesReceta";
            TypedQuery<Producto> query = entityManager.createQuery(jpql, Producto.class);
            return query.getResultList();
        } catch (PersistenceException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("No fué posible consultar los productos.");
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }

    /**
     * Filtra los productos almacenados de acuerdo a su categoría o tipo.
     *
     * @param tipo El TipoProducto por el cual filtrar.
     * @return Lista de productos pertenecientes a la categoría especificada.
     * @throws PersistenciaException Si ocurre un error durante la consulta.
     */
    @Override
    public List<Producto> buscarPorTipo(TipoProducto tipo) throws PersistenciaException {
        EntityManager entityManager = ManejadorConexiones.crearEntityManager();
        try {
            StringBuilder jpql = new StringBuilder("SELECT DISTINCT p FROM Producto p LEFT JOIN FETCH p.detallesReceta");

            if (tipo != null) {
                jpql.append(" WHERE p.tipo = :tipo");
            }

            TypedQuery<Producto> query = entityManager.createQuery(jpql.toString(), Producto.class);

            if (tipo != null) {
                itson.restaurantedominio.TipoProducto tipoDominio = itson.restaurantedominio.TipoProducto.valueOf(tipo.name());
                query.setParameter("tipo", tipoDominio);
            }

            return query.getResultList();
        } catch (PersistenceException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("No fué posible consultar los productos.");
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }
}
