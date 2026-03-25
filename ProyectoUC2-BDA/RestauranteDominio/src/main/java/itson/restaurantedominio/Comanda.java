/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package itson.restaurantedominio;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.*;

/**
 * Clase entidad: Comanda Representa las comandas creadas en la base de datos.
 *
 * @author Andrea Lara, Nahomi Figueroa, Zaira Barajas
 */
@Entity
@Table(name= "comandas")
public class Comanda implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_comanda")
    private Long id;
    
    @Column(name = "folio", nullable = false, unique = true)
    private String folio;
    
    @Column(name = "fecha_hora", nullable = false)
    private LocalDate fechaHora;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_comanda", nullable = false)
    private EstadoComanda estado;
    
    @Column(name = "total_acumulado", nullable = false)
    private Double totalAcumulado;
    
    @ManyToOne
    @JoinColumn(name="id_cliente", nullable =false )
    private ClienteFrecuente cliente;
    
    
    /**
     * Constructor por defecto
     */
    public Comanda() {
    }

    /**
     * Constructor con todo menos id y comandas
     */
    public Comanda(String folio, LocalDate fechaHora, EstadoComanda estado, Double totalAcumulado) {
        this.folio = folio;
        this.fechaHora = fechaHora;
        this.estado = estado;
        this.totalAcumulado = totalAcumulado;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public LocalDate getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDate fechaHora) {
        this.fechaHora = fechaHora;
    }

    public EstadoComanda getEstado() {
        return estado;
    }

    public void setEstado(EstadoComanda estado) {
        this.estado = estado;
    }

    public Double getTotalAcumulado() {
        return totalAcumulado;
    }

    public void setTotalAcumulado(Double totalAcumulado) {
        this.totalAcumulado = totalAcumulado;
    }

    public ClienteFrecuente getCliente() {
        return cliente;
    }

    public void setCliente(ClienteFrecuente cliente) {
        this.cliente = cliente;
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
        if (!(object instanceof Comanda)) {
            return false;
        }
        Comanda other = (Comanda) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "itson.restaurantedominio.Comanda[ id=" + id + " ]";
    }

}
