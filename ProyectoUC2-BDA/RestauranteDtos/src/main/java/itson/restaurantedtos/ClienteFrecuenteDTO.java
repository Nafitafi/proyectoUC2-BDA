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
    private String nombre;
    private String apellidoP;
    private String apellidoM; 
    private String numeroTelefono;
    private String correo; 
    private LocalDate fechaRegistro;

    /**
     * Constructor por defecto
     */
    public ClienteFrecuenteDTO() {
    }

    public ClienteFrecuenteDTO(String nombre, String apellidoP, String apellidoM, String numeroTelefono) {
        this.nombre = nombre;
        this.apellidoP = apellidoP;
        this.apellidoM = apellidoM;
        this.numeroTelefono = numeroTelefono;
        this.fechaRegistro = LocalDate.now();
    }

    
    public ClienteFrecuenteDTO(String nombre, String apellidoP, String apellidoM, String numeroTelefono, String correo) {
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
    
    
    
}
