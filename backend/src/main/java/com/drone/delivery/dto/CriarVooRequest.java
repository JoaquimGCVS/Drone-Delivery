package com.drone.delivery.dto;

import java.util.List;

import com.drone.delivery.model.Drone;
import com.drone.delivery.model.Pedido;

public class CriarVooRequest {
    private Drone drone;
    private List<Pedido> pedidos;
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
}
