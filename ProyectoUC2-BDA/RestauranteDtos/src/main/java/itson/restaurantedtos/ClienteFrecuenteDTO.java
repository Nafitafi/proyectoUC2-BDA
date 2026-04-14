/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.restaurantedtos;

import java.time.LocalDate;

/**
 *
 * @author Andrea Lara, Nahomi Figueroa, Zaira Barajas
 */
public class ClienteFrecuenteDTO {
    private Long id;
    private String nombre;
    private String apellidoP;
    private String apellidoM; 
    private String numeroTelefono;
    private String correo; 
    private Integer puntos;
    private LocalDate fechaRegistro;
    private Double TotalGastado;
    private Long visitas;

    /**
     * COnstructor de clase
     * @param id
     * @param nombre
     * @param apellidoP
     * @param apellidoM
     * @param numeroTelefono
     * @param correo
     * @param puntos
     * @param fechaRegistro 
     */
    public ClienteFrecuenteDTO(Long id, String nombre, String apellidoP, String apellidoM, String numeroTelefono, String correo, Integer puntos, LocalDate fechaRegistro) {
        this.id = id;
        this.nombre = nombre;
        this.apellidoP = apellidoP;
        this.apellidoM = apellidoM;
        this.numeroTelefono = numeroTelefono;
        this.correo = correo;
        this.puntos = puntos;
        this.fechaRegistro = fechaRegistro;
    }

    public ClienteFrecuenteDTO(Long id, String nombre, String apellidoP, String apellidoM, String numeroTelefono, String correo) {
        this.id = id;
        this.nombre = nombre;
        this.apellidoP = apellidoP;
        this.apellidoM = apellidoM;
        this.numeroTelefono = numeroTelefono;
        this.correo = correo;
    }

    
    
    //Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Integer getPuntos() {
        return puntos;
    }

    public void setPuntos(Integer puntos) {
        this.puntos = puntos;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Double getTotalGastado() {
        return TotalGastado;
    }

    public void setTotalGastado(Double TotalGastado) {
        this.TotalGastado = TotalGastado;
    }

    public Long getVisitas() {
        return visitas;
    }

    public void setVisitas(Long visitas) {
        this.visitas = visitas;
    }
    
    @Override
    public String toString() {
        return nombre +" "+apellidoP;
    }
}
