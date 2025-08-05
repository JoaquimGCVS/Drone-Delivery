package com.drone.delivery.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class Coordenada {
    private Double x;
    private Double y;

    public Coordenada() {}

    public Coordenada(Double x, Double y) {
        this.x = x;
        this.y = y;
    }

    // Método para criar a base fixa
    public static Coordenada base() {
        return new Coordenada(50.0, 50.0);
    }

    public Double calcularDistancia(Coordenada outra) {
        return Math.sqrt(Math.pow(outra.x - this.x, 2) + Math.pow(outra.y - this.y, 2));
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        if (x < 0 || x > 100) {
            throw new IllegalArgumentException("Coordenada X deve estar entre 0 e 100");
        }
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        if (y < 0 || y > 100) {
            throw new IllegalArgumentException("Coordenada Y deve estar entre 0 e 100");
        }
        this.y = y;
    }
}
