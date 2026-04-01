/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.restaurantedominio;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Clase entidad: ClienteFrecuente Representa a los clientes frecuentes en la 
 * base de datos.
 *
 * @author Andrea Lara, Nahomi Figueroa, Zaira Barajas
 */
@Entity
@Table(name = "clientes_fecuentes")
@PrimaryKeyJoinColumn(name = "id_cliente")
public class ClienteFrecuente extends Cliente {

    @Column(name = "correo", nullable = true)
    private String correo;

    @Column(name = "puntos", nullable = false)
    private Integer puntos;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDate fechaRegistro;
    
    
    /**
     * Constructor por defecto
     */
    public ClienteFrecuente() {
        super();
    }

    public ClienteFrecuente(String nombre, String apellidoP, String apellidoM, String numeroTelefono) {
        super(nombre, apellidoP, apellidoM, numeroTelefono);
        this.correo = correo;
        this.fechaRegistro = LocalDate.now();
        this.puntos = 0;//Por defecto
    }

    // Getters & Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoP() {
        return apellidoP;
    }

    public void setApellidoP(String apellidoP) {
        this.apellidoP = apellidoP;
    }

    public String getApellidoM() {
        return apellidoM;
    }

    public void setApellidoM(String apellidoM) {
        this.apellidoM = apellidoM;
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public Integer getPuntos() {
        return puntos;
    }

    public void setPuntos(Integer puntos) {
        this.puntos = puntos;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCliente != null ? idCliente.hashCode() : 0);
        return hash;
    }
}
