package com.drone.delivery.dto;

import com.drone.delivery.model.enums.PrioridadePedido;
import com.drone.delivery.model.enums.StatusPedido;

public class PedidoResumoDTO {
    private Long id;
    private Double clienteX;
    private Double clienteY;
    private Double peso;
    private PrioridadePedido prioridade;
    private StatusPedido status;
    
    // Construtores
    public PedidoResumoDTO() {}
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Double getClienteX() {
        return clienteX;
    }
    
    public void setClienteX(Double clienteX) {
        this.clienteX = clienteX;
    }
    
    public Double getClienteY() {
        return clienteY;
    }
    
    public void setClienteY(Double clienteY) {
        this.clienteY = clienteY;
    }
    
    public Double getPeso() {
        return peso;
    }
    
    public void setPeso(Double peso) {
        this.peso = peso;
    }
    
    public PrioridadePedido getPrioridade() {
        return prioridade;
    }
    
    public void setPrioridade(PrioridadePedido prioridade) {
        this.prioridade = prioridade;
    }
    
    public StatusPedido getStatus() {
        return status;
    }
    
    public void setStatus(StatusPedido status) {
        this.status = status;
    }
}