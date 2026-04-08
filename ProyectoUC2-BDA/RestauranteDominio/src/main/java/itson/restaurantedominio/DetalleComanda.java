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
 * Clase entidad: DetallesComanda Representa los productos que correspondan a
 * una comanda en la base de datos
 *
 * @author Andrea Lara, Nahomi Figueroa, Zaira Barajas
 */
@Entity
@Table(name = "detalles_comandas")
public class DetalleComanda implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_comanda")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @Column(name = "cantidad", nullable = false)
    private int cantidad;

    @Column(name = "comentario", nullable = true)
    private String comentario;

    @ManyToOne
    @JoinColumn(name = "id_comanda", nullable = false)
    private Comanda comanda;

    @Column(name = "precio", nullable = false)
    private Double precio;

    @Column(name = "subtotal", nullable = false)
    private double subtotal;

    public DetalleComanda() {
    }

    public DetalleComanda(Long id, Producto producto, int cantidad, String comentario, Comanda comanda, Double precio, double subtotal) {
        this.id = id;
        this.producto = producto;
        this.cantidad = cantidad;
        this.comentario = comentario;
        this.comanda = comanda;
        this.precio = precio;
        this.subtotal = subtotal;
    }

    public DetalleComanda(Producto producto, int cantidad, String comentario, Comanda comanda, Double precio, double subtotal) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.comentario = comentario;
        this.comanda = comanda;
        this.precio = precio;
        this.subtotal = subtotal;
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

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
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
