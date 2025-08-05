package com.drone.delivery.dto;

import com.drone.delivery.model.enums.StatusDrone;

public class DroneStatusDTO {
    private Long id;
    private String nome;
    private StatusDrone status;
    private Double capacidadeMaxima;
    private Double alcanceMaximo;
    private Double posicaoX;
    private Double posicaoY;
    
    // Construtores
    public DroneStatusDTO() {}
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public StatusDrone getStatus() {
        return status;
    }
    
    public void setStatus(StatusDrone status) {
        this.status = status;
    }
    
    public Double getCapacidadeMaxima() {
        return capacidadeMaxima;
    }
    
    public void setCapacidadeMaxima(Double capacidadeMaxima) {
        this.capacidadeMaxima = capacidadeMaxima;
    }
    
    public Double getAlcanceMaximo() {
        return alcanceMaximo;
    }
    
    public void setAlcanceMaximo(Double alcanceMaximo) {
        this.alcanceMaximo = alcanceMaximo;
    }
    
    public Double getPosicaoX() {
        return posicaoX;
    }
    
    public void setPosicaoX(Double posicaoX) {
        this.posicaoX = posicaoX;
    }
    
    public Double getPosicaoY() {
        return posicaoY;
    }
    
    public void setPosicaoY(Double posicaoY) {
        this.posicaoY = posicaoY;
    }
}