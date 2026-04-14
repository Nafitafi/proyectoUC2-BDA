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
public class ClienteFrecuenteActualizadoDTO {
    private Long id;
    private String nombre;
    private String apellidoP;
    private String apellidoM; 
    private String numeroTelefono;
    private String correo; 
    private LocalDate fechaRegistro;

    /**
     * Constructor por defecto
     */
    public ClienteFrecuenteActualizadoDTO() {
    }

    public ClienteFrecuenteActualizadoDTO(Long id, String nombre, String apellidoP, String apellidoM, String numeroTelefono, String correo) {
        this.id = id;
        this.nombre = nombre;
        this.apellidoP = apellidoP;
        this.apellidoM = apellidoM;
        this.numeroTelefono = numeroTelefono;
        this.correo = correo;
    }

    public ClienteFrecuenteActualizadoDTO(Long id, String nombre, String apellidoP, String apellidoM, String numeroTelefono) {
        this.id = id;
        this.nombre = nombre;
        this.apellidoP = apellidoP;
        this.apellidoM = apellidoM;
        this.numeroTelefono = numeroTelefono;
    }
    
    //Getters y Setters
    public Long getId() {    
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }
    
    @Override
    public String toString() {
        return nombre +" "+apellidoP;
    }
    
}
