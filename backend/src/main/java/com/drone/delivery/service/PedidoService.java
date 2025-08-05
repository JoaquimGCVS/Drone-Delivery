package com.drone.delivery.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drone.delivery.model.Coordenada;
import com.drone.delivery.model.Pedido;
import com.drone.delivery.model.enums.PrioridadePedido;
import com.drone.delivery.model.enums.StatusPedido;
import com.drone.delivery.repository.PedidoRepository;

@Service
public class PedidoService {
    
    @Autowired
    private PedidoRepository pedidoRepository;
    
    // Criar novo pedido e calcular prioridade
    public Pedido criarPedido(Double clienteX, Double clienteY, Double peso, PrioridadePedido prioridade) {
        // Validações básicas
        if (peso <= 0 || peso > 10) {
            throw new IllegalArgumentException("Peso deve estar entre 0 e 10kg");
        }
        
        Coordenada localizacaoCliente = new Coordenada(clienteX, clienteY);
        
        Pedido pedido = new Pedido();
        pedido.setLocalizacaoCliente(localizacaoCliente);
        pedido.setPeso(peso);
        pedido.setPrioridade(prioridade);
        pedido.setStatus(StatusPedido.PENDENTE);
        pedido.setDataCriacao(LocalDateTime.now());
        
        // Calcular pontuação de prioridade
        Double pontuacao = calcularPontuacaoPrioridade(localizacaoCliente, peso);
        pedido.setPontuacaoPrioridade(pontuacao);
        
        return pedidoRepository.save(pedido);
    }
    
    // Calcular pontuação de prioridade (peso × distância)
    public Double calcularPontuacaoPrioridade(Coordenada localizacao, Double peso) {
        Coordenada base = Coordenada.base(); // (50, 50)
        Double distancia = base.calcularDistancia(localizacao);
        return peso * distancia; // Menor pontuação = maior prioridade
    }
    
    // Buscar pedidos pendentes ordenados por prioridade
    public List<Pedido> buscarPedidosPendentesOrdenados() {
        return pedidoRepository.findByStatusOrderByPontuacaoPrioridadeAsc(StatusPedido.PENDENTE);
    }
    
    // Buscar pedidos por status
    public List<Pedido> buscarPedidosPorStatus(StatusPedido status) {
        return pedidoRepository.findByStatus(status);
    }
    
    // Método adicional: Atualizar status do pedido
    public void atualizarStatus(Long pedidoId, StatusPedido novoStatus) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        pedido.setStatus(novoStatus);
        pedidoRepository.save(pedido);
    }
    
    // Método adicional: Buscar todos os pedidos
    public List<Pedido> buscarTodosPedidos() {
        return pedidoRepository.findAll();
    }
}