/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.restaurantepersistencia;

import itson.restaurantedominio.Ingrediente;
import itson.restaurantedominio.Producto;
import itson.restaurantedominio.ProductoIngredientes;
import itson.restaurantedtos.DetallesRecetaDTO;
import itson.restaurantedtos.EstadoProducto;
import itson.restaurantedtos.NuevoProductoDTO;
import itson.restaurantedtos.ProductoActualizadoDTO;
import itson.restaurantedtos.TipoProducto;
import itson.restaurantepersistencia.adapters.ProductoActualizadoDTOAProductoAdapter;
import itson.restaurantepersistencia.adapters.ProductoNuevoDTOAProductoAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

/**
 *
 * @author Nahomi Figueroa
 */
public class ProductosDAO implements IProductosDAO {

    private static final Logger LOGGER = Logger.getLogger(ProductosDAO.class.getName());

    @Override
    public Producto guardar(NuevoProductoDTO productoNuevo) throws PersistenciaException {
        if (productoNuevo == null) {
            throw new PersistenciaException("El no puede ser nulo.");
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
            List<ProductoIngredientes> receta = new ArrayList<>();
            for (DetallesRecetaDTO detalle : productoNuevo.getDetallesReceta()) {
                if (detalle.getCantidad() == null || detalle.getCantidad() <= 0) {
                    throw new PersistenciaException("La cantidad de los ingredientes debe ser mayor a cero.");
                }
                Ingrediente ingredienteRef = entityManager.getReference(Ingrediente.class, detalle.getIdIngrediente());
                receta.add(new ProductoIngredientes(producto, ingredienteRef, detalle.getCantidad()));
            }
            producto.setIngredientesRequeridos(receta);
            entityManager.getTransaction().begin();
            entityManager.persist(producto);
            entityManager.getTransaction().commit();

            return producto;

        } catch (PersistenceException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("No fue posible registrar el producto.");
        }
    }

    @Override
    public Producto actualizar(ProductoActualizadoDTO productoActualizar) throws PersistenciaException {
        if (productoActualizar == null || productoActualizar.getId() == null) {
            throw new PersistenciaException("Datos insuficientes para actualizar el producto.");
        }
        EntityManager entityManager = ManejadorConexiones.crearEntityManager();

        try {
            Producto productoExistente = entityManager.find(Producto.class, productoActualizar.getId());
            if (productoExistente == null) {
                throw new PersistenciaException("No se encontró el producto a actualizar.");
            }

            Producto productoNuevosDatos = ProductoActualizadoDTOAProductoAdapter.adaptar(productoActualizar);
            if (productoNuevosDatos.getNombre() != null) {
                if (productoNuevosDatos.getNombre().length() > 100) {
                    throw new PersistenciaException("El nombre no puede tener más de 100 caracteres.");
                }
                if (existe(productoNuevosDatos)) {
                    throw new PersistenciaException("Ya existe otro producto activo con ese nombre.");
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

            if (productoActualizar.getDetallesReceta() != null && !productoActualizar.getDetallesReceta().isEmpty()) {
                productoExistente.getIngredientesRequeridos().clear();
                for (DetallesRecetaDTO detalle : productoActualizar.getDetallesReceta()) {
                    if (detalle.getCantidad() == null || detalle.getCantidad() <= 0) {
                        throw new PersistenciaException("La cantidad de los ingredientes debe ser mayor a cero.");
                    }
                    Ingrediente ingredienteRef = entityManager.getReference(Ingrediente.class, detalle.getIdIngrediente());
                    productoExistente.getIngredientesRequeridos().add(new ProductoIngredientes(productoExistente, ingredienteRef, detalle.getCantidad()));
                }
            }

            entityManager.getTransaction().begin();
            entityManager.merge(productoExistente);
            entityManager.getTransaction().commit();

            return productoExistente;

        } catch (PersistenceException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("No fue posible actualizar el producto.");
        }
    }

    @Override
    public Producto buscarPorId(Long idProducto) throws PersistenciaException {
        EntityManager entityManager = ManejadorConexiones.crearEntityManager();
        try {
            Producto producto = entityManager.find(Producto.class, idProducto);
            if (producto == null) {
                throw new PersistenciaException("No se encontró el producto con el ID especificado.");
            }
            return producto;
        } catch (PersistenceException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("No fue posible consultar el producto.");
        }
    }

    @Override
    public boolean verificarDisponibilidad(Long idProducto) throws PersistenciaException {
        EntityManager entityManager = ManejadorConexiones.crearEntityManager();
        try {
            Producto producto = entityManager.find(Producto.class, idProducto);
            
            if (producto == null || producto.getEstado() != itson.restaurantedominio.EstadoProducto.ACTIVO) {
                return false;
            }
            
            for (ProductoIngredientes pi : producto.getIngredientesRequeridos()) {
                if (pi.getIngrediente().getStock() < pi.getCantidadRequerida()) {
                    return false; 
                }
            }
            return true;
            
        } catch (PersistenceException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("No fue posible verificar la disponibilidad del producto.");
        }
    }

    @Override
    public List<Producto> buscarProductosActivosFiltro(String nombreParcial, TipoProducto categoria) throws PersistenciaException {
        EntityManager entityManager = ManejadorConexiones.crearEntityManager();
        try {
            StringBuilder jpql = new StringBuilder("SELECT p FROM Producto p WHERE p.estado = :estado");
            
            if (nombreParcial != null && !nombreParcial.trim().isEmpty()) {
                jpql.append(" AND LOWER(p.nombre) LIKE LOWER(:nombre)");
            }
            if (categoria != null) {
                jpql.append(" AND p.tipo = :categoria");
            }
            
            TypedQuery<Producto> query = entityManager.createQuery(jpql.toString(), Producto.class);
            query.setParameter("estado", itson.restaurantedominio.EstadoProducto.ACTIVO);
            
            if (nombreParcial != null && !nombreParcial.trim().isEmpty()) {
                query.setParameter("nombre", "%" + nombreParcial.trim() + "%");
            }
            
            if (categoria != null) {
                itson.restaurantedominio.TipoProducto categoriaDominio = 
                    itson.restaurantedominio.TipoProducto.valueOf(categoria.name());
                query.setParameter("categoria", categoriaDominio);
            }
            
            return query.getResultList();
        }  catch (PersistenceException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("No fue posible consultar los productos.");
        }
    }

    @Override
    public boolean existe(Producto producto) throws PersistenciaException {
        EntityManager entityManager = ManejadorConexiones.crearEntityManager();
        try {
            String jpql = "SELECT COUNT(p) FROM Producto p WHERE LOWER(p.nombre) = LOWER(:nom) AND p.estado = :estado";

            if (producto.getIdProducto() != null) {
                jpql += " AND p.idProducto != :id";
            }

            TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class).setParameter("nom", producto.getNombre()).setParameter("estado", itson.restaurantedominio.EstadoProducto.ACTIVO);
            
            if (producto.getIdProducto() != null) {
                query.setParameter("id", producto.getIdProducto());
            }

            return query.getSingleResult() > 0;
        } catch (PersistenceException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("No fue posible consultar si el producto existe.");
        }
    }

}
