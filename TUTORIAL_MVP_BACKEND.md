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

---

## Observação
- A otimização de alocação de pedidos aos drones ocorre automaticamente ao criar pedido ou finalizar voo.
