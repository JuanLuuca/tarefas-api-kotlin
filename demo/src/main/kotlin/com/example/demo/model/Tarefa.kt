package com.example.demo.model

import com.example.demo.enum.StatusTarefa
import java.time.LocalDateTime

/**
 * Classe que representa uma Tarefa no sistema
 */
data class Tarefa(
    val id: Long,
    var titulo: String,
    var descricao: String? = null,
    var status: StatusTarefa = StatusTarefa.PENDENTE,
    val dataCriacao: LocalDateTime = LocalDateTime.now(),
    var dataAtualizacao: LocalDateTime = LocalDateTime.now()
) {
    /**
     * Atualiza o status da tarefa se a transição for válida
     */
    fun atualizarStatus(novoStatus: StatusTarefa): Boolean {
        return if (status.podeTransicionarPara(novoStatus)) {
            status = novoStatus
            dataAtualizacao = LocalDateTime.now()
            true
        } else {
            false
        }
    }

    /**
     * Atualiza os dados da tarefa
     */
    fun atualizar(novoTitulo: String? = null, novaDescricao: String? = null) {
        novoTitulo?.let { titulo = it }
        novaDescricao?.let { descricao = it }
        dataAtualizacao = LocalDateTime.now()
    }
}
