const API_BASE = 'http://localhost:8080';
        let drones = [];
        let rotas = [];

        // Função para atualizar timestamp
        function updateTimestamp() {
            document.getElementById('lastUpdate').textContent = 
                `Última atualização: ${new Date().toLocaleTimeString()}`;
        }

        // Função para carregar drones
        async function carregarDrones() {
            try {
                const response = await fetch(`${API_BASE}/drones/status`);
                drones = await response.json();
                
                const dronesList = document.getElementById('dronesList');
                dronesList.innerHTML = drones.map(drone => `
                    <div class="drone-item">
                        <div>
                            <strong>${drone.nome}</strong><br>
                            <small>Posição: (${drone.posicaoX}, ${drone.posicaoY})</small><br>
                            <small>Capacidade: ${drone.capacidadeMaxima}kg | Alcance: ${drone.alcanceMaximo}km</small>
                        </div>
                        <span class="drone-status status-${drone.status}">${drone.status}</span>
                    </div>
                `).join('');
                
            } catch (error) {
                document.getElementById('dronesList').innerHTML = 
                    '<div class="error">Erro ao carregar drones</div>';
                console.error('Erro:', error);
            }
        }

        // Função para carregar rotas
        async function carregarRotas() {
            try {
                const response = await fetch(`${API_BASE}/entregas/rota`);
                rotas = await response.json();
                
                const rotasList = document.getElementById('rotasList');
                
                if (rotas.length === 0) {
                    rotasList.innerHTML = '<div class="loading">Nenhuma rota ativa no momento</div>';
                    return;
                }
                
                rotasList.innerHTML = rotas.map(rota => `
                    <div class="rota-item">
                        <div class="rota-header">
                            <h4>🚁 ${rota.droneNome} - Voo #${rota.vooId}</h4>
                            <span class="drone-status status-${rota.statusVoo}">${rota.statusVoo}</span>
                        </div>
                        <p><strong>Distância:</strong> ${rota.distanciaTotal.toFixed(2)}km | 
                           <strong>Peso:</strong> ${rota.pesoTotal}kg</p>
                        <p><strong>Pedidos:</strong></p>
                        ${rota.pedidos.map(pedido => `
                            <div class="pedido-item">
                                📦 Pedido #${pedido.id} - (${pedido.clienteX}, ${pedido.clienteY}) 
                                - ${pedido.peso}kg - ${pedido.prioridade}
                            </div>
                        `).join('')}
                    </div>
                `).join('');
                
            } catch (error) {
                document.getElementById('rotasList').innerHTML = 
                    '<div class="error">Erro ao carregar rotas</div>';
                console.error('Erro:', error);
            }
        }

        // Função para gerar mapa ASCII
        function gerarMapaASCII() {
            let mapa = '';
            
            // Header do mapa
            mapa += '🗺️  MAPA DA CIDADE (100x100km)\n';
            mapa += '═'.repeat(50) + '\n\n';
            
            // Base central
            mapa += '🏢 BASE DE OPERAÇÕES: (50, 50)\n\n';
            
            // Drones
            mapa += '🚁 DRONES:\n';
            drones.forEach(drone => {
                mapa += `   ${drone.nome}: (${drone.posicaoX}, ${drone.posicaoY}) - ${drone.status}\n`;
            });
            
            // Rotas ativas
            if (rotas.length > 0) {
                mapa += '\n🗺️ ROTAS ATIVAS:\n';
                rotas.forEach(rota => {
                    mapa += `   ${rota.droneNome}: ${rota.distanciaTotal.toFixed(1)}km\n`;
                    rota.pedidos.forEach(pedido => {
                        mapa += `     └─ 📦 (${pedido.clienteX}, ${pedido.clienteY}) ${pedido.peso}kg\n`;
                    });
                });
            }
            
            // Grid simplificado
            mapa += '\n📍 GRID DE ENTREGAS:\n';
            mapa += '   0    25    50    75   100\n';
            mapa += '0  ┌─────┬─────┬─────┬─────┐\n';
            mapa += '   │     │     │ 🏢  │     │\n';
            mapa += '50 ├─────┼─────┼─────┼─────┤\n';
            mapa += '   │     │     │     │     │\n';
            mapa += '100└─────┴─────┴─────┴─────┘\n';
            
            if (rotas.length > 0) {
                mapa += '\n📦 ENTREGAS ATIVAS:\n';
                rotas.forEach(rota => {
                    rota.pedidos.forEach(pedido => {
                        const x = Math.floor(pedido.clienteX / 25);
                        const y = Math.floor(pedido.clienteY / 25);
                        mapa += `   📦 Quadrante (${x},${y}) - ${pedido.peso}kg\n`;
                    });
                });
            }
            
            document.getElementById('mapaASCII').textContent = mapa;
        }

        // Função para criar pedido
        async function criarPedido(event) {
            event.preventDefault();
            
            const dados = {
                clienteX: parseFloat(document.getElementById('clienteX').value),
                clienteY: parseFloat(document.getElementById('clienteY').value),
                peso: parseFloat(document.getElementById('peso').value),
                prioridade: document.getElementById('prioridade').value
            };
            
            try {
                const response = await fetch(`${API_BASE}/pedidos`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(dados)
                });
                
                if (response.ok) {
                    const resultado = await response.json();
                    document.getElementById('errorMessage').innerHTML = 
                        `<div style="background: #d4edda; color: #155724; padding: 10px; border-radius: 5px;">
                            ✅ Pedido #${resultado.id} criado com sucesso! 
                            Prioridade: ${resultado.pontuacaoPrioridade.toFixed(2)}
                        </div>`;
                    
                    // Limpar formulário
                    document.getElementById('pedidoForm').reset();
                    
                    // Atualizar dados
                    await atualizarDados();
                    
                } else {
                    throw new Error('Erro ao criar pedido');
                }
                
            } catch (error) {
                document.getElementById('errorMessage').innerHTML = 
                    '<div class="error">❌ Erro ao criar pedido. Verifique os dados.</div>';
                console.error('Erro:', error);
            }
        }

        // Função principal para atualizar todos os dados
        async function atualizarDados() {
            await Promise.all([
                carregarDrones(),
                carregarRotas()
            ]);
            gerarMapaASCII();
            updateTimestamp();
        }

        // Event listeners
        document.getElementById('pedidoForm').addEventListener('submit', criarPedido);

        // Inicialização
        document.addEventListener('DOMContentLoaded', async () => {
            await atualizarDados();
            
            // Atualizar a cada 3 segundos
            setInterval(atualizarDados, 3000);
        });