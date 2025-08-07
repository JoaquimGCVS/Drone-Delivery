package com.drone.delivery.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drone.delivery.model.Coordenada;
import com.drone.delivery.model.Drone;
import com.drone.delivery.model.enums.StatusDrone;
import com.drone.delivery.repository.DroneRepository;

@Service
public class DroneService {
    
    @Autowired
    private DroneRepository droneRepository;
    
    // Buscar drones disponíveis (status = IDLE)
    public List<Drone> buscarDronesDisponiveis() {
        return droneRepository.findByStatus(StatusDrone.IDLE);
    }
    
    // Atualizar status do drone
    public void atualizarStatus(Long droneId, StatusDrone novoStatus) {
        Drone drone = droneRepository.findById(droneId)
            .orElseThrow(() -> new RuntimeException("Drone não encontrado"));
        drone.setStatus(novoStatus);
        droneRepository.save(drone);
    }
    
    // Buscar todos os drones com seus status
    public List<Drone> buscarTodosDrones() {
        return droneRepository.findAll();
    }
    
    // Inicializar drones na base (se necessário)
    public void inicializarDrones() {
        // Verificar se já existem drones
        long count = droneRepository.count();
        if (count == 0) {
            // Drone 1: padrão
            Drone d1 = new Drone();
            d1.setNome("Drone-1");
            d1.setStatus(StatusDrone.IDLE);
            d1.setCapacidadeMaxima(10.0);
            d1.setAlcanceMaximo(20.0);
            d1.setPosicaoAtual(Coordenada.base());
            droneRepository.save(d1);

            // Drone 2: menor capacidade, menor alcance
            Drone d2 = new Drone();
            d2.setNome("Drone-2");
            d2.setStatus(StatusDrone.IDLE);
            d2.setCapacidadeMaxima(6.0);
            d2.setAlcanceMaximo(12.0);
            d2.setPosicaoAtual(Coordenada.base());
            droneRepository.save(d2);

            // Drone 3: maior capacidade, maior alcance
            Drone d3 = new Drone();
            d3.setNome("Drone-3");
            d3.setStatus(StatusDrone.IDLE);
            d3.setCapacidadeMaxima(15.0);
            d3.setAlcanceMaximo(30.0);
            d3.setPosicaoAtual(Coordenada.base());
            droneRepository.save(d3);

            // Drone 4: capacidade média, alcance alto
            Drone d4 = new Drone();
            d4.setNome("Drone-4");
            d4.setStatus(StatusDrone.IDLE);
            d4.setCapacidadeMaxima(8.0);
            d4.setAlcanceMaximo(25.0);
            d4.setPosicaoAtual(Coordenada.base());
            droneRepository.save(d4);

            // Drone 5: capacidade alta, alcance baixo
            Drone d5 = new Drone();
            d5.setNome("Drone-5");
            d5.setStatus(StatusDrone.IDLE);
            d5.setCapacidadeMaxima(12.0);
            d5.setAlcanceMaximo(10.0);
            d5.setPosicaoAtual(Coordenada.base());
            droneRepository.save(d5);
        }
    }
    
    // Buscar drone por ID
    public Drone buscarPorId(Long droneId) {
        return droneRepository.findById(droneId)
            .orElseThrow(() -> new RuntimeException("Drone não encontrado"));
    }
    
    // Verificar se drone está disponível para nova missão
    public boolean isDroneDisponivel(Long droneId) {
        Drone drone = buscarPorId(droneId);
        return drone.getStatus() == StatusDrone.IDLE;
    }
    
    // Atualizar posição atual do drone
    public void atualizarPosicao(Long droneId, Coordenada novaPosicao) {
        Drone drone = buscarPorId(droneId);
        drone.setPosicaoAtual(novaPosicao);
        droneRepository.save(drone);
    }
    
    // Retornar drone para a base
    public void retornarParaBase(Long droneId) {
        Drone drone = buscarPorId(droneId);
        drone.setPosicaoAtual(Coordenada.base());
        drone.setStatus(StatusDrone.IDLE);
        droneRepository.save(drone);
    }
    
    // Buscar drones por status
    public List<Drone> buscarPorStatus(StatusDrone status) {
        return droneRepository.findByStatus(status);
    }
}