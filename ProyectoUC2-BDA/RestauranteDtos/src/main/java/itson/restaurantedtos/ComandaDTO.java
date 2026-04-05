/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.restaurantedtos;

import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Andrea Lara, Nahomi Figueroa, Zaira Barajas
 */
public class ComandaDTO {

    private String folio;
    private LocalDate fechaHora;
    private String estado;
    private double total;
    private int numeroMesa;
    private Long idCliente;
    private List<DetalleComandaDTO> detalles;

    public ComandaDTO(String folio, LocalDate fechaHora, String estado, double total, int numeroMesa, Long idCliente, List<DetalleComandaDTO> detalles) {
        this.folio = folio;
        this.fechaHora = fechaHora;
        this.estado = estado;
        this.total = total;
        this.numeroMesa = numeroMesa;
        this.idCliente = idCliente;
        this.detalles = detalles;
    }

    public String getFolio() {
        return folio;
    }

    public LocalDate getFechaHora() {
        return fechaHora;
    }

    public String getEstado() {
        return estado;
    }

    public double getTotal() {
        return total;
    }

    public int getNumeroMesa() {
        return numeroMesa;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public List<DetalleComandaDTO> getDetalles() {
        return detalles;
    }

    
    

}
