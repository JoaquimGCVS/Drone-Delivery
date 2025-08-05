package com.drone.delivery.model;

import java.time.LocalDateTime;

import com.drone.delivery.model.enums.PrioridadePedido;
import com.drone.delivery.model.enums.StatusPedido;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Embedded
    private Coordenada localizacaoCliente;
    
    private Double peso;
    private PrioridadePedido prioridade;
    private StatusPedido status;
    private Double pontuacaoPrioridade; // peso × distância
    private LocalDateTime dataCriacao;
    
    @ManyToOne
    private Voo voo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Coordenada getLocalizacaoCliente() {
        return localizacaoCliente;
    }

    public void setLocalizacaoCliente(Coordenada localizacaoCliente) {
        this.localizacaoCliente = localizacaoCliente;
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

    public Double getPontuacaoPrioridade() {
        return pontuacaoPrioridade;
    }

    public void setPontuacaoPrioridade(Double pontuacaoPrioridade) {
        this.pontuacaoPrioridade = pontuacaoPrioridade;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Voo getVoo() {
        return voo;
    }

    public void setVoo(Voo voo) {
        this.voo = voo;
    }
}
