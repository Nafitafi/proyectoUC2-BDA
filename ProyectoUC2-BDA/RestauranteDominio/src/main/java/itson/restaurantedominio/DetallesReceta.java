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
 * Clase entidad: Detalles receta.
 * 
 * Se encarga de la relación entre productos e ingredientes.
 * 
 * @author Nahomi Figueroa
 */
@Entity
@Table(name = "detalles_receta")
public class DetallesReceta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalles_receta")
    private Long idDetalleReceta;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "id_ingrediente", nullable = false)
    private Ingrediente ingrediente;
    
    @Column(name = "cantidad_requerida", nullable = false)
    private Double cantidadRequerida;

    public DetallesReceta() {
    }
    
    public DetallesReceta(Producto producto, Ingrediente ingrediente, Double cantidadRequerida) {
        this.producto = producto;
        this.ingrediente = ingrediente;
        this.cantidadRequerida = cantidadRequerida;
    }
    
    public Long getId() {
        return idDetalleReceta;
    }

    public void setId(Long id) {
        this.idDetalleReceta = id;
    }

    public Long getIdDetalleReceta() {
        return idDetalleReceta;
    }

    public void setIdDetalleReceta(Long idDetalleReceta) {
        this.idDetalleReceta = idDetalleReceta;
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
        hash += (idDetalleReceta != null ? idDetalleReceta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetallesReceta)) {
            return false;
        }
        DetallesReceta other = (DetallesReceta) object;
        if ((this.idDetalleReceta == null && other.idDetalleReceta != null) || (this.idDetalleReceta != null && !this.idDetalleReceta.equals(other.idDetalleReceta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "itson.restaurantedominio.ProductoIngredientes[ id=" + idDetalleReceta + " ]";
    }
    
}
