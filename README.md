# 🚁 Drone Delivery - Visão Geral e Regras do Sistema

## Tecnologias Utilizadas
- Java 21.0.6 (Oracle, LTS)
- Spring Boot 3.5.4
- Spring Data JPA
- PostgreSQL
- Maven 3.9.9
- RESTful APIs
- GitHub Copilot (auxílio no desenvolvimento)

> **Ambiente testado:**
> - Sistema operacional: Windows 11 


## Regras do Sistema
- O sistema inicializa automaticamente com 5 drones, cada um com capacidade máxima de carga (kg) e alcance máximo (km) diferentes:
    - Drone-1: capacidade 10kg, alcance 20km
    - Drone-2: capacidade 6kg, alcance 12km
    - Drone-3: capacidade 15kg, alcance 30km
    - Drone-4: capacidade 8kg, alcance 25km
    - Drone-5: capacidade 12kg, alcance 10km
- Pedidos de entrega podem ter peso maior que zero e no máximo 14kg, e destino dentro do mapa (0 ≤ x, y ≤ 100).
- Um pedido só é entregue se houver um drone disponível capaz de transportar seu peso e cobrir a distância de ida e volta.
- Cada drone só pode realizar um voo por vez e retorna à base após concluir as entregas.
- Pedidos são priorizados por "pontuação de prioridade" (peso × distância até o cliente), menor valor = maior prioridade.


## Lógica de Otimização de Alocação
- Ao criar um pedido ou finalizar um voo, o sistema tenta alocar pedidos pendentes aos drones disponíveis de forma otimizada.
- A alocação respeita a capacidade e o alcance de cada drone, agrupando pedidos compatíveis para maximizar a eficiência.
- A ordem de entrega dentro de cada voo é otimizada usando o algoritmo do vizinho mais próximo (Nearest Neighbor):
    1. O drone parte da base (50,50).
    2. Sempre escolhe o próximo pedido mais próximo da sua posição atual.
    3. Repete até entregar todos os pedidos do grupo.
    4. Retorna à base.
- A "pontuação de prioridade" de cada pedido é calculada como: `peso × distância da base até o cliente`.
- A distância é calculada por Pitágoras: `distância = sqrt((x_cliente - 50)^2 + (y_cliente - 50)^2)`.
- O sistema só agrupa pedidos em um voo se a soma dos pesos não exceder a capacidade do drone e a soma das distâncias de ida e volta não exceder o alcance do drone.
- Se um pedido não puder ser alocado imediatamente, ele permanece pendente até que um drone compatível esteja livre.

# 🚁 Tutorial Resumido - Drone Delivery (Backend)

## 1. Iniciar o Backend
```powershell
cd "c:\Users\Joaquim Vilela\Desktop\DroneDelivery\backend"
mvn spring-boot:run
```

## 2. Criar Pedido de Entrega
```powershell
$pedido = @{
    descricao = "Medicamento"
    peso = 2.0
    coordenadaDestino = @{ x = 25; y = 30 }
    prioridade = "ALTA"
} | ConvertTo-Json -Depth 10
Invoke-RestMethod -Uri "http://localhost:8080/pedidos" -Method POST -Body $pedido -ContentType "application/json"
```

## 3. Consultar Status dos Drones
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/drones/status" -Method GET | ConvertTo-Json -Depth 10
```

## 4. Consultar Todos os Pedidos
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/pedidos" -Method GET | ConvertTo-Json -Depth 10
```

## 5. Consultar Pedidos Pendentes
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/pedidos/pendentes" -Method GET | ConvertTo-Json -Depth 10
```

## 6. Consultar Rotas/Voos Ativos
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/entregas/rota" -Method GET | ConvertTo-Json -Depth 10
```

## 7. Iniciar Voo de Entrega
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/voos/{id}/iniciar" -Method PUT
```

## 8. Finalizar Voo de Entrega
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/voos/{id}/finalizar" -Method PUT
```
## 8. Criar Voo de Entrega Manualmente
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/voos/{id}/criar" -Method PUT
```
---


## Como Testar a Otimização de Alocação

- Para testar a priorização (voos de um pedido só):
    1. Deixe todos os drones ocupados (com voos planejados ou em andamento).
    2. Crie vários pedidos com pesos e distâncias diferentes.
    3. Libere um drone (finalize um voo).
    4. O sistema irá alocar o pedido de menor pontuação de prioridade (peso × distância).

- Para testar o agrupamento inteligente:
    1. Deixe todos os drones ocupados.
    2. Crie vários pedidos leves e próximos entre si (ex: peso 1kg, coordenadas próximas da base).
    3. Libere um drone.
    4. O sistema irá agrupar os pedidos compatíveis em um mesmo voo, respeitando capacidade e alcance.

**Importante:** Se houver drones livres, os pedidos são alocados imediatamente ao serem criados. Para testar a lógica de priorização e agrupamento, é necessário que todos os drones estejam ocupados antes de criar os pedidos de teste.

---

## Observação
- A otimização de alocação de pedidos aos drones ocorre automaticamente ao criar pedido ou finalizar voo.
