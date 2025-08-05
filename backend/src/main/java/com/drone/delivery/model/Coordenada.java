package com.drone.delivery.model;

public class Coordenada {
    private Double x;
    private Double y;
    
    public Double calcularDistancia(Coordenada outra) {
        return Math.sqrt(Math.pow(outra.x - this.x, 2) + Math.pow(outra.y - this.y, 2));
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }
}
