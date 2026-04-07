/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.restaurantedominio;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Nahomi Figueroa
 */
@Entity
@Table(name = "productos_ingredientes")
public class ProductoIngredientes implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto_ingrediente")
    private Long idProductoIngrediente;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "id_ingrediente", nullable = false)
    private Ingrediente ingrediente;
    
    @Column(name = "cantidad_requerida", nullable = false)
    private Double cantidadRequerida;

    public ProductoIngredientes() {
    }
    
    public ProductoIngredientes(Producto producto, Ingrediente ingrediente, Double cantidadRequerida) {
        this.producto = producto;
        this.ingrediente = ingrediente;
        this.cantidadRequerida = cantidadRequerida;
    }
    
    public Long getId() {
        return idProductoIngrediente;
    }

    public void setId(Long id) {
        this.idProductoIngrediente = id;
    }

    public Long getIdProductoIngrediente() {
        return idProductoIngrediente;
    }

    public void setIdProductoIngrediente(Long idProductoIngrediente) {
        this.idProductoIngrediente = idProductoIngrediente;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Ingrediente getIngrediente() {
        return ingrediente;
    }

    public void setIngrediente(Ingrediente ingrediente) {
        this.ingrediente = ingrediente;
    }

    public Double getCantidadRequerida() {
        return cantidadRequerida;
    }

    public void setCantidadRequerida(Double cantidadRequerida) {
        this.cantidadRequerida = cantidadRequerida;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idProductoIngrediente != null ? idProductoIngrediente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductoIngredientes)) {
            return false;
        }
        ProductoIngredientes other = (ProductoIngredientes) object;
        if ((this.idProductoIngrediente == null && other.idProductoIngrediente != null) || (this.idProductoIngrediente != null && !this.idProductoIngrediente.equals(other.idProductoIngrediente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "itson.restaurantedominio.ProductoIngredientes[ id=" + idProductoIngrediente + " ]";
    }
    
}
