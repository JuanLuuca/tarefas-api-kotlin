package com.example.demo.dto

import com.example.demo.enum.StatusTarefa
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

/**
 * DTO para criar uma nova tarefa
 */
data class CriarTarefaRequest(
    @field:NotBlank(message = "Título é obrigatório")
    @field:Size(min = 1, max = 100, message = "Título deve ter entre 1 e 100 caracteres")
    val titulo: String,

    @field:Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    val descricao: String? = null
)

/**
 * DTO para atualizar uma tarefa
 */
data class AtualizarTarefaRequest(
    @field:Size(min = 1, max = 100, message = "Título deve ter entre 1 e 100 caracteres")
    val titulo: String? = null,

    @field:Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    val descricao: String? = null
)

/**
 * DTO para alterar status
 */
data class AlterarStatusRequest(
    val novoStatus: StatusTarefa
)
