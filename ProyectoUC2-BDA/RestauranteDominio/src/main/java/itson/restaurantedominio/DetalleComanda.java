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
import javax.persistence.ManyToOne;

/**
 * Clase entidad: DetallesComanda Representa los productos que correspondan a 
 * una comanda en la base de datos
 *
 * @author Andrea Lara, Nahomi Figueroa, Zaira Barajas
 */
@Entity
public class DetalleComanda implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Producto producto;

    @Column(name = "cantidad")
    private int cantidad;

    @Column(name = "comentario")
    private String comentario;

    @ManyToOne
    private Comanda comanda;

    public DetalleComanda() {
    }

    public DetalleComanda(Producto producto, int cantidad, String comentario, Comanda comanda) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.comentario = comentario;
        this.comanda = comanda;
    }

    public DetalleComanda(Long id, Producto producto, int cantidad, String comentario, Comanda comanda) {
        this.id = id;
        this.producto = producto;
        this.cantidad = cantidad;
        this.comentario = comentario;
        this.comanda = comanda;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Comanda getComanda() {
        return comanda;
    }

    public void setComanda(Comanda comanda) {
        this.comanda = comanda;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetalleComanda)) {
            return false;
        }
        DetalleComanda other = (DetalleComanda) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "itson.restaurantedominio.DetalleComanda[ id=" + id + " ]";
    }

}
