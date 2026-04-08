/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.restaurantedtos;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author Andrea Lara, Nahomi Figueroa, Zaira Barajas
 */
public class ComandaDTO {

    private String folio;
    private Long idMesa;
    private Long idCliente;
    private List<DetalleComandaDTO> detalles;

    private LocalDateTime fechahora;
    private EstadoComanda estadoComanda;
    private Double total;

    public ComandaDTO() {
    }

    public ComandaDTO(Long idMesa, Long idCliente, List<DetalleComandaDTO> detalles) {
        this.idMesa = idMesa;
        this.idCliente = idCliente;
        this.detalles = detalles;
    }

    public ComandaDTO(String folio, Long idMesa, Long idCliente, List<DetalleComandaDTO> detalles) {
        this.folio = folio;
        this.idMesa = idMesa;
        this.idCliente = idCliente;
        this.detalles = detalles;
    }

    public ComandaDTO(String folio, Long idMesa, Long idCliente, List<DetalleComandaDTO> detalles, LocalDateTime fechahora, EstadoComanda estadoComanda, Double total) {
        this.folio = folio;
        this.idMesa = idMesa;
        this.idCliente = idCliente;
        this.detalles = detalles;
        this.fechahora = fechahora;
        this.estadoComanda = estadoComanda;
        this.total = total;
    }

    public String getFolio() {
        return folio;
    }

    public Long getIdMesa() {
        return idMesa;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public List<DetalleComandaDTO> getDetalles() {
        return detalles;
    }

    public LocalDateTime getFechahora() {
        return fechahora;
    }

    public EstadoComanda getEstadoComanda() {
        return estadoComanda;
    }

    public Double getTotal() {
        return total;
    }

}
