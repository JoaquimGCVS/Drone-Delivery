package com.drone.delivery.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drone.delivery.model.Coordenada;
import com.drone.delivery.model.Drone;
import com.drone.delivery.model.Pedido;
import com.drone.delivery.model.Voo;
import com.drone.delivery.model.enums.StatusVoo;
import com.drone.delivery.repository.VooRepository;

@Service
public class VooService {
    
    @Autowired
    private VooRepository vooRepository;
    
    // Criar novo voo com lista de pedidos
    public Voo criarVoo(Drone drone, List<Pedido> pedidos) {
        // Validações
        if (pedidos.isEmpty()) {
            throw new IllegalArgumentException("Lista de pedidos não pode estar vazia");
        }
        
        Double pesoTotal = calcularPesoTotal(pedidos);
        if (pesoTotal > drone.getCapacidadeMaxima()) {
            throw new IllegalArgumentException("Peso total excede capacidade do drone");
        }
        
        Double distanciaTotal = calcularDistanciaTotal(pedidos);
        if (distanciaTotal > drone.getAlcanceMaximo()) {
            throw new IllegalArgumentException("Distância total excede alcance do drone");
        }
        
        Voo voo = new Voo();
        voo.setDrone(drone);
        voo.setPedidos(pedidos);
        voo.setDistanciaTotal(distanciaTotal);
        voo.setPesoTotal(pesoTotal);
        voo.setStatus(StatusVoo.PLANEJADO);
        voo.setInicioVoo(LocalDateTime.now());
        
        // Associar pedidos ao voo
        pedidos.forEach(pedido -> pedido.setVoo(voo));
        
        return vooRepository.save(voo);
    }
    
    // Calcular distância total da rota
    public Double calcularDistanciaTotal(List<Pedido> pedidos) {
        if (pedidos.isEmpty()) {
            return 0.0;
        }
        
        Coordenada base = Coordenada.base(); // (50, 50)
        Double distanciaTotal = 0.0;
        Coordenada posicaoAtual = base;
        
        // Calcular distância para cada entrega
        for (Pedido pedido : pedidos) {
            Coordenada destino = pedido.getLocalizacaoCliente();
            distanciaTotal += posicaoAtual.calcularDistancia(destino);
            posicaoAtual = destino;
        }
        
        // Adicionar distância de retorno à base
        distanciaTotal += posicaoAtual.calcularDistancia(base);
        
        return distanciaTotal;
    }
    
    // Calcular peso total dos pedidos
    public Double calcularPesoTotal(List<Pedido> pedidos) {
        return pedidos.stream()
                .mapToDouble(Pedido::getPeso)
                .sum();
    }
    
    // Validar se rota respeita alcance máximo (20km)
    public boolean validarAlcanceRota(List<Pedido> pedidos) {
        Double distanciaTotal = calcularDistanciaTotal(pedidos);
        return distanciaTotal <= 20.0; // Alcance máximo do drone
    }
    
    // Validar se peso total respeita capacidade máxima (10kg)
    public boolean validarCapacidadeRota(List<Pedido> pedidos) {
        Double pesoTotal = calcularPesoTotal(pedidos);
        return pesoTotal <= 10.0; // Capacidade máxima do drone
    }
    
    // Buscar rotas ativas/planejadas
    public List<Voo> buscarRotasAtivas() {
        return vooRepository.findByStatusIn(List.of(StatusVoo.PLANEJADO, StatusVoo.EM_ANDAMENTO));
    }
    
    // Buscar voos por status
    public List<Voo> buscarVoosPorStatus(StatusVoo status) {
        return vooRepository.findByStatus(status);
    }
    
    // Iniciar voo (atualizar status)
    public void iniciarVoo(Long vooId) {
        Voo voo = vooRepository.findById(vooId)
            .orElseThrow(() -> new RuntimeException("Voo não encontrado"));
        voo.setStatus(StatusVoo.EM_ANDAMENTO);
        voo.setInicioVoo(LocalDateTime.now());
        vooRepository.save(voo);
    }
    
    // Finalizar voo
    public void finalizarVoo(Long vooId) {
        Voo voo = vooRepository.findById(vooId)
            .orElseThrow(() -> new RuntimeException("Voo não encontrado"));
        voo.setStatus(StatusVoo.CONCLUIDO);
        voo.setFimVoo(LocalDateTime.now());
        vooRepository.save(voo);
    }
    
    // Buscar todos os voos
    public List<Voo> buscarTodosVoos() {
        return vooRepository.findAll();
    }
    
    // Buscar voos por drone
    public List<Voo> buscarVoosPorDrone(Long droneId) {
        return vooRepository.findByDroneId(droneId);
    }
}