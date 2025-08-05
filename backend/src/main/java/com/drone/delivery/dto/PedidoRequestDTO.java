package com.drone.delivery.dto;

import com.drone.delivery.model.enums.PrioridadePedido;

public class PedidoRequestDTO {
    private Double clienteX;
    private Double clienteY;
    private Double peso;
    private PrioridadePedido prioridade;
    
    // Construtores
    public PedidoRequestDTO() {}
    
    public PedidoRequestDTO(Double clienteX, Double clienteY, Double peso, PrioridadePedido prioridade) {
        this.clienteX = clienteX;
        this.clienteY = clienteY;
        this.peso = peso;
        this.prioridade = prioridade;
    }
    
    // Getters e Setters
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
}