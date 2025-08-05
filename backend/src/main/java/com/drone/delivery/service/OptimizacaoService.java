package com.drone.delivery.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drone.delivery.model.Coordenada;
import com.drone.delivery.model.Drone;
import com.drone.delivery.model.Pedido;
import com.drone.delivery.model.Voo;
import com.drone.delivery.model.enums.StatusDrone;
import com.drone.delivery.model.enums.StatusPedido;

@Service
public class OptimizacaoService {
    
    @Autowired
    private PedidoService pedidoService;
    
    @Autowired
    private DroneService droneService;
    
    @Autowired
    private VooService vooService;
    
    // MÉTODO PRINCIPAL - Alocar pedidos aos drones
    public void alocarPedidosOtimizado() {
        // 1. Buscar pedidos pendentes ordenados por prioridade (menor pontuação = maior prioridade)
        List<Pedido> pedidosPendentes = pedidoService.buscarPedidosPendentesOrdenados();
        
        if (pedidosPendentes.isEmpty()) {
            return; // Não há pedidos para processar
        }
        
        // 2. Buscar drones disponíveis
        List<Drone> dronesDisponiveis = droneService.buscarDronesDisponiveis();
        
        if (dronesDisponiveis.isEmpty()) {
            return; // Não há drones disponíveis
        }
        
        // 3. Agrupar pedidos por drone de forma otimizada
        List<List<Pedido>> gruposPorDrone = agruparPedidosPorDrone(pedidosPendentes, dronesDisponiveis);
        
        // 4. Para cada grupo, criar voo otimizado
        for (int i = 0; i < gruposPorDrone.size(); i++) {
            List<Pedido> grupo = gruposPorDrone.get(i);
            if (!grupo.isEmpty()) {
                Drone drone = dronesDisponiveis.get(i);
                criarVooOtimizado(drone, grupo);
            }
        }
    }
    
    // Agrupar pedidos por drone respeitando capacidade/alcance
    public List<List<Pedido>> agruparPedidosPorDrone(List<Pedido> pedidos, List<Drone> drones) {
        List<List<Pedido>> grupos = new ArrayList<>();
        List<Pedido> pedidosRestantes = new ArrayList<>(pedidos);
        
        // Inicializar grupos vazios para cada drone
        for (int i = 0; i < drones.size(); i++) {
            grupos.add(new ArrayList<>());
        }
        
        // Algoritmo guloso: para cada drone, tentar adicionar pedidos
        for (int droneIndex = 0; droneIndex < drones.size() && !pedidosRestantes.isEmpty(); droneIndex++) {
            List<Pedido> grupoAtual = grupos.get(droneIndex);
            
            // Tentar adicionar pedidos ao drone atual
            for (int i = 0; i < pedidosRestantes.size(); i++) {
                Pedido pedido = pedidosRestantes.get(i);
                
                // Criar grupo temporário para teste
                List<Pedido> grupoTeste = new ArrayList<>(grupoAtual);
                grupoTeste.add(pedido);
                
                // Verificar se o grupo é válido
                if (validarGrupoPedidos(grupoTeste)) {
                    grupoAtual.add(pedido);
                    pedidosRestantes.remove(i);
                    i--; // Ajustar índice após remoção
                }
            }
        }
        
        return grupos;
    }
    
    // Otimizar sequência de entregas (algoritmo do vizinho mais próximo)
    public List<Pedido> otimizarSequenciaEntregas(List<Pedido> pedidos) {
        if (pedidos.size() <= 1) {
            return new ArrayList<>(pedidos);
        }
        
        List<Pedido> sequenciaOtima = new ArrayList<>();
        List<Pedido> naoVisitados = new ArrayList<>(pedidos);
        
        Coordenada posicaoAtual = Coordenada.base(); // Começar da base
        
        // Algoritmo do vizinho mais próximo
        while (!naoVisitados.isEmpty()) {
            Pedido maisProximo = null;
            double menorDistancia = Double.MAX_VALUE;
            
            // Encontrar o pedido mais próximo da posição atual
            for (Pedido pedido : naoVisitados) {
                double distancia = posicaoAtual.calcularDistancia(pedido.getLocalizacaoCliente());
                if (distancia < menorDistancia) {
                    menorDistancia = distancia;
                    maisProximo = pedido;
                }
            }
            
            // Adicionar à sequência e atualizar posição
            sequenciaOtima.add(maisProximo);
            naoVisitados.remove(maisProximo);
            posicaoAtual = maisProximo.getLocalizacaoCliente();
        }
        
        return sequenciaOtima;
    }
    
    // Validar se grupo de pedidos cabe em um drone
    private boolean validarGrupoPedidos(List<Pedido> pedidos) {
        if (pedidos.isEmpty()) {
            return true;
        }
        
        // Verificar peso total
        double pesoTotal = pedidos.stream()
                .mapToDouble(Pedido::getPeso)
                .sum();
        
        if (pesoTotal > 10.0) { // Capacidade máxima
            return false;
        }
        
        // Verificar distância total da rota
        double distanciaTotal = vooService.calcularDistanciaTotal(pedidos);
        
        return distanciaTotal <= 20.0; // Alcance máximo
    }
    
    // Criar voo otimizado para um drone e grupo de pedidos
    private void criarVooOtimizado(Drone drone, List<Pedido> pedidos) {
        // Otimizar sequência de entregas
        List<Pedido> sequenciaOtima = otimizarSequenciaEntregas(pedidos);
        
        // Atualizar status dos pedidos
        sequenciaOtima.forEach(pedido -> 
            pedidoService.atualizarStatus(pedido.getId(), StatusPedido.ALOCADO)
        );
        
        // Atualizar status do drone
        droneService.atualizarStatus(drone.getId(), StatusDrone.CARREGANDO);
        
        // Criar voo
        vooService.criarVoo(drone, sequenciaOtima);
    }
    
    // Método auxiliar: Calcular eficiência da alocação
    public double calcularEficienciaAlocacao() {
        List<Pedido> todosPedidos = pedidoService.buscarTodosPedidos();
        List<Voo> todosVoos = vooService.buscarTodosVoos();
        
        if (todosPedidos.isEmpty()) {
            return 0.0;
        }
        
        // Eficiência = Total de pedidos / Total de voos
        return (double) todosPedidos.size() / Math.max(todosVoos.size(), 1);
    }
    
    // Método auxiliar: Estatísticas de otimização
    public String gerarRelatorioOtimizacao() {
        List<Pedido> pedidosPendentes = pedidoService.buscarPedidosPorStatus(StatusPedido.PENDENTE);
        List<Pedido> pedidosAlocados = pedidoService.buscarPedidosPorStatus(StatusPedido.ALOCADO);
        List<Drone> dronesDisponiveis = droneService.buscarDronesDisponiveis();
        List<Voo> voosAtivos = vooService.buscarRotasAtivas();
        
        StringBuilder relatorio = new StringBuilder();
        relatorio.append("=== RELATÓRIO DE OTIMIZAÇÃO ===\n");
        relatorio.append("Pedidos Pendentes: ").append(pedidosPendentes.size()).append("\n");
        relatorio.append("Pedidos Alocados: ").append(pedidosAlocados.size()).append("\n");
        relatorio.append("Drones Disponíveis: ").append(dronesDisponiveis.size()).append("\n");
        relatorio.append("Voos Ativos: ").append(voosAtivos.size()).append("\n");
        relatorio.append("Eficiência: ").append(String.format("%.2f", calcularEficienciaAlocacao())).append(" pedidos/voo\n");
        
        return relatorio.toString();
    }
}