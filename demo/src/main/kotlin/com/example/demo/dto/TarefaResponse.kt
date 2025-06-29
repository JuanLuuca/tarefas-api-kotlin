package com.example.demo.dto

import com.example.demo.enum.StatusTarefa
import com.example.demo.model.Tarefa
import java.time.LocalDateTime

/**
 * DTO de resposta para uma tarefa
 */
data class TarefaResponse(
    val id: Long,
    val titulo: String,
    val descricao: String?,
    val status: StatusTarefa,
    val dataCriacao: LocalDateTime,
    val dataAtualizacao: LocalDateTime,
    val proximosStatusValidos: List<StatusTarefa>
) {
    companion object {
        fun fromModel(tarefa: Tarefa): TarefaResponse {
            return TarefaResponse(
                id = tarefa.id,
                titulo = tarefa.titulo,
                descricao = tarefa.descricao,
                status = tarefa.status,
                dataCriacao = tarefa.dataCriacao,
                dataAtualizacao = tarefa.dataAtualizacao,
                proximosStatusValidos = tarefa.status.proximosStatusValidos()
            )
        }
    }
}

/**
 * DTO para lista de tarefas
 */
data class TarefaListResponse(
    val tarefas: List<TarefaResponse>,
    val total: Int,
    val resumoPorStatus: Map<StatusTarefa, Int>
)
