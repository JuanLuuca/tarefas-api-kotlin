# 📋 Tarefas API - Kotlin Spring Boot

Uma API REST completa para gerenciamento de tarefas desenvolvida em **Kotlin** com **Spring Boot**.

## 🚀 Tecnologias Utilizadas

- **Kotlin** - Linguagem de programação
- **Spring Boot** - Framework principal
- **Spring Data JPA** - Persistência de dados
- **H2 Database** - Banco de dados em memória
- **Maven** - Gerenciamento de dependências

## 🎯 Endpoints Disponíveis

### 📝 CRUD Básico
- `POST /api/tarefas` → Criar nova tarefa
- `GET /api/tarefas` → Listar todas as tarefas
- `GET /api/tarefas/{id}` → Buscar tarefa por ID
- `PUT /api/tarefas/{id}` → Atualizar tarefa completa
- `DELETE /api/tarefas/{id}` → Remover tarefa

### 🔄 Gerenciamento de Status
- `PATCH /api/tarefas/{id}/status` → Alterar apenas o status
- `GET /api/tarefas/status/{status}` → Listar tarefas por status

### 🔍 Busca e Estatísticas
- `GET /api/tarefas/buscar?titulo=X` → Buscar tarefas por título
- `GET /api/tarefas/estatisticas` → Ver estatísticas das tarefas
- `GET /api/tarefas/ajuda/regras` → Ver regras de negócio

## 📊 Status Disponíveis

- `PENDENTE` - Tarefa criada, aguardando início
- `EM_PROGRESSO` - Tarefa sendo executada
- `CONCLUIDA` - Tarefa finalizada com sucesso
- `CANCELADA` - Tarefa cancelada

## 🛠️ Como Executar

### Pré-requisitos
- Java 17 ou superior
- Maven 3.6+

### Executando a aplicação

1. Clone o repositório:
```bash
git clone https://github.com/JuanLuuca/tarefas-api-kotlin.git
cd tarefas-api-kotlin
```

2. Execute a aplicação:
```bash
./mvnw spring-boot:run
```

3. A API estará disponível em: `http://localhost:8080`

## 📋 Exemplo de Uso

### Criar uma nova tarefa:
```json
POST /api/tarefas
{
  "titulo": "Estudar Kotlin",
  "descricao": "Aprender conceitos básicos do Kotlin",
  "prioridade": "ALTA"
}
```

### Resposta:
```json
{
  "id": 1,
  "titulo": "Estudar Kotlin",
  "descricao": "Aprender conceitos básicos do Kotlin",
  "status": "PENDENTE",
  "prioridade": "ALTA",
  "dataCriacao": "2025-06-29T10:30:00",
  "dataAtualizacao": "2025-06-29T10:30:00"
}
```

## 🔧 Funcionalidades

- ✅ CRUD completo de tarefas
- ✅ Validação de dados de entrada
- ✅ Tratamento de exceções personalizado
- ✅ Busca por título (case-insensitive)
- ✅ Filtros por status
- ✅ Estatísticas de tarefas
- ✅ Alteração isolada de status
- ✅ Dados de exemplo pré-carregados

## 🏗️ Estrutura do Projeto

```
src/main/kotlin/com/example/demo/
├── controller/     # Controladores REST
├── service/        # Lógica de negócio  
├── repository/     # Acesso a dados
├── entity/         # Entidades JPA
├── dto/           # Data Transfer Objects
├── enum/          # Enumerações
├── exception/     # Exceções customizadas
├── config/        # Configurações
└── model/         # Modelos de domínio
```

## 👨‍💻 Autor

Desenvolvido por **JuanLuuca**

---

⭐ Se este projeto foi útil para você, considere dar uma estrela no repositório!
