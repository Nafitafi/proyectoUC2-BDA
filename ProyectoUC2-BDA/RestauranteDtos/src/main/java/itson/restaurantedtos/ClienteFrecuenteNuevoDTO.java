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
public class ClienteFrecuenteNuevoDTO {
    private String nombre;
    private String apellidoP;
    private String apellidoM; 
    private String numeroTelefono;
    private String correo; 
    private Integer puntos;
    private LocalDate fechaRegistro;

    /**
     * Constructor por defecto
     */
    public ClienteFrecuenteNuevoDTO() {
    }

    public ClienteFrecuenteNuevoDTO(String nombre, String apellidoP, String apellidoM, String numeroTelefono, String correo) {
        this.nombre = nombre;
        this.apellidoP = apellidoP;
        this.apellidoM = apellidoM;
        this.numeroTelefono = numeroTelefono;
        this.correo = correo;
    }

    
    public ClienteFrecuenteNuevoDTO(String nombre, String apellidoP, String apellidoM, String numeroTelefono, Integer puntos) {
        this.nombre = nombre;
        this.apellidoP = apellidoP;
        this.apellidoM = apellidoM;
        this.numeroTelefono = numeroTelefono;
        this.puntos = puntos;
        this.fechaRegistro = LocalDate.now();
    }

    
    public ClienteFrecuenteNuevoDTO(String nombre, String apellidoP, String apellidoM, String numeroTelefono, String correo, Integer puntos) {
        this.nombre = nombre;
        this.apellidoP = apellidoP;
        this.apellidoM = apellidoM;
        this.numeroTelefono = numeroTelefono;
        this.correo = correo;
        this.fechaRegistro = LocalDate.now();
    }
    
    

   //Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public String getApellidoP() {
        return apellidoP;
    }

    public String getApellidoM() {
        return apellidoM;
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public String getCorreo() {
        return correo;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellidoP(String apellidoP) {
        this.apellidoP = apellidoP;
    }

    public void setApellidoM(String apellidoM) {
        this.apellidoM = apellidoM;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setPuntos(Integer puntos) {
        this.puntos = puntos;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
    
    
    @Override
    public String toString() {
        return nombre +" "+apellidoP;
    }
}
