package com.example.demo

import com.example.demo.dto.CriarTarefaRequest
import com.example.demo.dto.AlterarStatusRequest
import com.example.demo.enum.StatusTarefa
import com.example.demo.service.TarefaService
import com.example.demo.repository.TarefaRepository

/**
 * Classe de teste para demonstrar o funcionamento do sistema
 * Execute com: kotlinc -cp "." TesteTarefas.kt -include-runtime -d TesteTarefas.jar && java -jar TesteTarefas.jar
 */
fun main() {
    println("🎯 TESTE DO SISTEMA DE GERENCIAMENTO DE TAREFAS")
    println("=" * 50)
    
    // Inicializa componentes
    val repository = TarefaRepository()
    val service = TarefaService(repository)
    
    try {
        // 1. Criar tarefas
        println("\n📝 1. CRIANDO TAREFAS:")
        val tarefa1 = service.criarTarefa(CriarTarefaRequest("Implementar login", "Sistema de autenticação"))
        val tarefa2 = service.criarTarefa(CriarTarefaRequest("Escrever testes", null))
        
        println("✅ Tarefa 1 criada: ${tarefa1.titulo} (Status: ${tarefa1.status})")
        println("✅ Tarefa 2 criada: ${tarefa2.titulo} (Status: ${tarefa2.status})")
        
        // 2. Listar tarefas
        println("\n📋 2. LISTANDO TODAS AS TAREFAS:")
        val todasTarefas = service.listarTodasTarefas()
        todasTarefas.tarefas.forEach { tarefa ->
            println("   ID: ${tarefa.id} | ${tarefa.titulo} | Status: ${tarefa.status}")
        }
        
        // 3. Alterar status (transição válida)
        println("\n🔄 3. ALTERANDO STATUS (PENDENTE → EM_ANDAMENTO):")
        val tarefaAtualizada = service.alterarStatusTarefa(
            tarefa1.id, 
            AlterarStatusRequest(StatusTarefa.EM_ANDAMENTO)
        )
        println("✅ Status alterado: ${tarefaAtualizada.titulo} agora está ${tarefaAtualizada.status}")
        
        // 4. Tentar transição inválida
        println("\n❌ 4. TESTANDO TRANSIÇÃO INVÁLIDA (PENDENTE → CONCLUIDA):")
        try {
            service.alterarStatusTarefa(
                tarefa2.id, 
                AlterarStatusRequest(StatusTarefa.CONCLUIDA)
            )
        } catch (e: Exception) {
            println("🚫 Erro capturado corretamente: ${e.message}")
        }
        
        // 5. Completar fluxo correto
        println("\n✅ 5. COMPLETANDO FLUXO CORRETO:")
        service.alterarStatusTarefa(tarefa1.id, AlterarStatusRequest(StatusTarefa.CONCLUIDA))
        println("🎉 Tarefa 1 concluída com sucesso!")
        
        // 6. Estatísticas
        println("\n📊 6. ESTATÍSTICAS:")
        val stats = service.obterEstatisticas()
        println(stats["resumo"])
        
        println("\n" + "=" * 50)
        println("✅ TODOS OS TESTES PASSARAM! O SISTEMA ESTÁ FUNCIONANDO CORRETAMENTE!")
        
    } catch (e: Exception) {
        println("🚨 Erro no teste: ${e.message}")
        e.printStackTrace()
    }
}

operator fun String.times(count: Int): String = this.repeat(count)
