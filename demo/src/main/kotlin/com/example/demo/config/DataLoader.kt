package com.example.demo.config

import com.example.demo.enum.StatusTarefa
import com.example.demo.model.Tarefa
import com.example.demo.repository.TarefaRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

/**
 * Carrega dados iniciais de exemplo
 */
@Component
class DataLoader(
    private val tarefaRepository: TarefaRepository
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        println("🚀 Iniciando aplicação de Gerenciamento de Tarefas...")
        
        // Cria tarefas de exemplo
        criarTarefasDeExemplo()
        
        println("\n" + "=".repeat(60))
        println("✅ APLICAÇÃO INICIADA COM SUCESSO!")
        println("🌐 Acesse: http://localhost:8080/api/tarefas/info")
        println("📖 Regras: http://localhost:8080/api/tarefas/ajuda/regras")
        println("=".repeat(60))
    }

    private fun criarTarefasDeExemplo() {
        val tarefasExemplo = listOf(
            Tarefa(
                id = 0L,
                titulo = "Implementar autenticação",
                descricao = "Adicionar sistema de login e autenticação JWT",
                status = StatusTarefa.EM_ANDAMENTO
            ),
            Tarefa(
                id = 0L,
                titulo = "Escrever documentação da API",
                descricao = "Criar documentação completa dos endpoints REST",
                status = StatusTarefa.PENDENTE
            ),
            Tarefa(
                id = 0L,
                titulo = "Configurar deploy automático",
                descricao = "Implementar CI/CD com GitHub Actions",
                status = StatusTarefa.PENDENTE
            ),
            Tarefa(
                id = 0L,
                titulo = "Testes unitários do service",
                descricao = "Escrever testes para validar regras de negócio",
                status = StatusTarefa.CONCLUIDA
            ),
            Tarefa(
                id = 0L,
                titulo = "Validação de entrada de dados",
                descricao = "Implementar validações nos DTOs",
                status = StatusTarefa.CONCLUIDA
            )
        )

        tarefasExemplo.forEach { tarefa ->
            tarefaRepository.salvar(tarefa)
        }

        println("📊 ${tarefasExemplo.size} tarefas de exemplo criadas:")
        tarefasExemplo.forEach { tarefa ->
            val emoji = when(tarefa.status) {
                StatusTarefa.PENDENTE -> "⏳"
                StatusTarefa.EM_ANDAMENTO -> "🔄" 
                StatusTarefa.CONCLUIDA -> "✅"
            }
            println("   $emoji ${tarefa.titulo} (${tarefa.status})")
        }
    }
}
