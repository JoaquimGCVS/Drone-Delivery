package com.drone.delivery.dto;

import java.time.LocalDateTime;
import java.util.List;
import com.drone.delivery.model.enums.StatusVoo;

public class RotaResponseDTO {
    private Long vooId;
    private Long droneId;
    private String droneNome;
    private StatusVoo statusVoo;
    private Double distanciaTotal;
    private Double pesoTotal;
    private LocalDateTime inicioVoo;
    private List<PedidoResumoDTO> pedidos;
    
    // Construtores
    public RotaResponseDTO() {}
    
    // Getters e Setters
    public Long getVooId() {
        return vooId;
    }
    
    public void setVooId(Long vooId) {
        this.vooId = vooId;
    }
    
    public Long getDroneId() {
        return droneId;
    }
    
    public void setDroneId(Long droneId) {
        this.droneId = droneId;
    }
    
    public String getDroneNome() {
        return droneNome;
    }
    
    public void setDroneNome(String droneNome) {
        this.droneNome = droneNome;
    }
    
    public StatusVoo getStatusVoo() {
        return statusVoo;
    }
    
    public void setStatusVoo(StatusVoo statusVoo) {
        this.statusVoo = statusVoo;
    }
    
    public Double getDistanciaTotal() {
        return distanciaTotal;
    }
    
    public void setDistanciaTotal(Double distanciaTotal) {
        this.distanciaTotal = distanciaTotal;
    }
    
    public Double getPesoTotal() {
        return pesoTotal;
    }
    
    public void setPesoTotal(Double pesoTotal) {
        this.pesoTotal = pesoTotal;
    }
    
    public LocalDateTime getInicioVoo() {
        return inicioVoo;
    }
    
    public void setInicioVoo(LocalDateTime inicioVoo) {
        this.inicioVoo = inicioVoo;
    }
    
    public List<PedidoResumoDTO> getPedidos() {
        return pedidos;
    }
    
    public void setPedidos(List<PedidoResumoDTO> pedidos) {
        this.pedidos = pedidos;
    }
}