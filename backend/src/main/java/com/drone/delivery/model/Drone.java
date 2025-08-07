package com.drone.delivery.model;

import java.util.List;

import com.drone.delivery.model.enums.StatusDrone;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Drone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nome;
    private StatusDrone status;
    private Double capacidadeMaxima; // kg
    private Double alcanceMaximo; // km
    
    @Embedded
    private Coordenada posicaoAtual;
    
    @OneToMany(mappedBy = "drone")
    private List<Voo> voos;

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

    public Coordenada getPosicaoAtual() {
        return posicaoAtual;
    }

    public void setPosicaoAtual(Coordenada posicaoAtual) {
        this.posicaoAtual = posicaoAtual;
    }

    public List<Voo> getVoos() {
        return voos;
    }

    public void setVoos(List<Voo> voos) {
        this.voos = voos;
    }
}
