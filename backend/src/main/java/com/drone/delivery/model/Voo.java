package com.drone.delivery.model;

import java.time.LocalDateTime;
import java.util.List;

import com.drone.delivery.model.enums.StatusVoo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Voo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    private Drone drone;
    
    @OneToMany(mappedBy = "voo")
    private List<Pedido> pedidos;
    
    private Double distanciaTotal;
    private Double pesoTotal;
    private StatusVoo status;
    private LocalDateTime inicioVoo;
    private LocalDateTime fimVoo;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Drone getDrone() {
        return drone;
    }
    public void setDrone(Drone drone) {
        this.drone = drone;
    }
    public List<Pedido> getPedidos() {
        return pedidos;
    }
    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
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
    public StatusVoo getStatus() {
        return status;
    }
    public void setStatus(StatusVoo status) {
        this.status = status;
    }
    public LocalDateTime getInicioVoo() {
        return inicioVoo;
    }
    public void setInicioVoo(LocalDateTime inicioVoo) {
        this.inicioVoo = inicioVoo;
    }
    public LocalDateTime getFimVoo() {
        return fimVoo;
    }
    public void setFimVoo(LocalDateTime fimVoo) {
        this.fimVoo = fimVoo;
    }
}
