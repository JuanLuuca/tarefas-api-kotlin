package com.example.demo.service

import com.example.demo.dto.*
import com.example.demo.enum.StatusTarefa
import com.example.demo.exception.TarefaNaoEncontradaException
import com.example.demo.exception.TransicaoStatusInvalidaException
import com.example.demo.exception.ValidacaoNegocioException
import com.example.demo.model.Tarefa
import com.example.demo.repository.TarefaRepository
import org.springframework.stereotype.Service

/**
 * Service responsável pela lógica de negócio das tarefas
 */
@Service
class TarefaService(
    private val tarefaRepository: TarefaRepository
) {

    /**
     * Cria uma nova tarefa
     */
    fun criarTarefa(request: CriarTarefaRequest): TarefaResponse {
        val tarefa = Tarefa(
            id = 0L, // Será gerado pelo repository
            titulo = request.titulo,
            descricao = request.descricao,
            status = StatusTarefa.PENDENTE
        )
        
        val tarefaSalva = tarefaRepository.salvar(tarefa)
        println("✅ Nova tarefa criada: ID ${tarefaSalva.id} - ${tarefaSalva.titulo}")
        
        return TarefaResponse.fromModel(tarefaSalva)
    }

    /**
     * Busca tarefa por ID
     */
    fun buscarTarefaPorId(id: Long): TarefaResponse {
        val tarefa = tarefaRepository.buscarPorId(id)
            ?: throw TarefaNaoEncontradaException(id)
        
        return TarefaResponse.fromModel(tarefa)
    }

    /**
     * Lista todas as tarefas
     */
    fun listarTodasTarefas(): TarefaListResponse {
        val tarefas = tarefaRepository.listarTodas()
        val tarefasResponse = tarefas.map { TarefaResponse.fromModel(it) }
        
        // Gera resumo por status
        val resumoPorStatus = StatusTarefa.values().associateWith { status ->
            tarefaRepository.contarPorStatus(status)
        }
        
        return TarefaListResponse(
            tarefas = tarefasResponse,
            total = tarefas.size,
            resumoPorStatus = resumoPorStatus
        )
    }

    /**
     * Lista tarefas por status específico
     */
    fun listarTarefasPorStatus(status: StatusTarefa): TarefaListResponse {
        val tarefas = tarefaRepository.listarPorStatus(status)
        val tarefasResponse = tarefas.map { TarefaResponse.fromModel(it) }
        
        val resumoPorStatus = mapOf(status to tarefas.size)
        
        return TarefaListResponse(
            tarefas = tarefasResponse,
            total = tarefas.size,
            resumoPorStatus = resumoPorStatus
        )
    }

    /**
     * Atualiza uma tarefa
     */
    fun atualizarTarefa(id: Long, request: AtualizarTarefaRequest): TarefaResponse {
        val tarefa = tarefaRepository.buscarPorId(id)
            ?: throw TarefaNaoEncontradaException(id)

        // Valida se pelo menos um campo foi fornecido
        if (request.titulo.isNullOrBlank() && request.descricao == null) {
            throw ValidacaoNegocioException("Pelo menos um campo (título ou descrição) deve ser fornecido para atualização")
        }

        // Atualiza os dados
        tarefa.atualizar(
            novoTitulo = request.titulo?.takeIf { it.isNotBlank() },
            novaDescricao = request.descricao
        )

        val tarefaAtualizada = tarefaRepository.salvar(tarefa)
        println("📝 Tarefa atualizada: ID ${tarefaAtualizada.id} - ${tarefaAtualizada.titulo}")
        
        return TarefaResponse.fromModel(tarefaAtualizada)
    }

    /**
     * Altera o status de uma tarefa
     */
    fun alterarStatusTarefa(id: Long, request: AlterarStatusRequest): TarefaResponse {
        val tarefa = tarefaRepository.buscarPorId(id)
            ?: throw TarefaNaoEncontradaException(id)

        val statusAnterior = tarefa.status

        // Valida e executa a transição
        if (!tarefa.atualizarStatus(request.novoStatus)) {
            throw TransicaoStatusInvalidaException(statusAnterior, request.novoStatus)
        }

        val tarefaAtualizada = tarefaRepository.salvar(tarefa)
        println("🔄 Status alterado: ID ${tarefaAtualizada.id} - $statusAnterior → ${request.novoStatus}")
        
        return TarefaResponse.fromModel(tarefaAtualizada)
    }

    /**
     * Remove uma tarefa
     */
    fun removerTarefa(id: Long) {
        val tarefa = tarefaRepository.buscarPorId(id)
            ?: throw TarefaNaoEncontradaException(id)

        // Regra de negócio: não permite remover tarefas concluídas
        if (tarefa.status == StatusTarefa.CONCLUIDA) {
            throw ValidacaoNegocioException(
                "❌ Não é possível remover uma tarefa CONCLUÍDA. " +
                "Tarefas concluídas devem ser mantidas para histórico."
            )
        }

        tarefaRepository.remover(id)
        println("🗑️ Tarefa removida: ID $id - ${tarefa.titulo}")
    }

    /**
     * Busca tarefas por título
     */
    fun buscarTarefasPorTitulo(titulo: String): TarefaListResponse {
        if (titulo.isBlank()) {
            throw ValidacaoNegocioException("Título para busca não pode estar vazio")
        }

        val tarefas = tarefaRepository.buscarPorTitulo(titulo)
        val tarefasResponse = tarefas.map { TarefaResponse.fromModel(it) }
        
        val resumoPorStatus = StatusTarefa.values().associateWith { status ->
            tarefas.count { it.status == status }
        }
        
        return TarefaListResponse(
            tarefas = tarefasResponse,
            total = tarefas.size,
            resumoPorStatus = resumoPorStatus
        )
    }

    /**
     * Obtém estatísticas das tarefas
     */
    fun obterEstatisticas(): Map<String, Any> {
        val totalPendentes = tarefaRepository.contarPorStatus(StatusTarefa.PENDENTE)
        val totalEmAndamento = tarefaRepository.contarPorStatus(StatusTarefa.EM_ANDAMENTO)
        val totalConcluidas = tarefaRepository.contarPorStatus(StatusTarefa.CONCLUIDA)
        val total = tarefaRepository.contarTotal()

        val percentualConclusao = if (total > 0) {
            (totalConcluidas.toDouble() / total * 100)
        } else 0.0

        return mapOf(
            "total" to total,
            "porStatus" to mapOf(
                "PENDENTE" to totalPendentes,
                "EM_ANDAMENTO" to totalEmAndamento,
                "CONCLUIDA" to totalConcluidas
            ),
            "percentualConclusao" to String.format("%.1f%%", percentualConclusao),
            "resumo" to "📊 $total tarefas total: $totalPendentes pendentes, $totalEmAndamento em andamento, $totalConcluidas concluídas"
        )
    }
}
