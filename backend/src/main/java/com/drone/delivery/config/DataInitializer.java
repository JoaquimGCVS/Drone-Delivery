package com.drone.delivery.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.drone.delivery.service.DroneService;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private DroneService droneService;
    
    @Override
    public void run(String... args) throws Exception {
        System.out.println("Inicializando dados do sistema...");
        
        // Inicializar drones automaticamente
        droneService.inicializarDrones();
        
        System.out.println("Sistema inicializado com sucesso!");
        System.out.println("5 drones criados e posicionados na base (50,50)");
        System.out.println("Acesse: http://localhost:8080/drones/status");
        System.out.println("Teste criando pedidos em: POST http://localhost:8080/pedidos");
    }
}
