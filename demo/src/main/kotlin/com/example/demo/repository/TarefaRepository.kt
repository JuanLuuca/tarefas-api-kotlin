package com.example.demo.repository

import com.example.demo.enum.StatusTarefa
import com.example.demo.model.Tarefa
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

/**
 * Repository em memória para gerenciar tarefas
 * Simula um banco de dados usando estruturas em memória
 */
@Repository
class TarefaRepository {
    
    private val tarefas = ConcurrentHashMap<Long, Tarefa>()
    private val contadorId = AtomicLong(1)

    /**
     * Salva uma nova tarefa
     */
    fun salvar(tarefa: Tarefa): Tarefa {
        val id = if (tarefa.id == 0L) contadorId.getAndIncrement() else tarefa.id
        val tarefaComId = tarefa.copy(id = id)
        tarefas[id] = tarefaComId
        return tarefaComId
    }

    /**
     * Busca tarefa por ID
     */
    fun buscarPorId(id: Long): Tarefa? {
        return tarefas[id]
    }

    /**
     * Lista todas as tarefas
     */
    fun listarTodas(): List<Tarefa> {
        return tarefas.values.sortedByDescending { it.dataCriacao }
    }

    /**
     * Lista tarefas por status
     */
    fun listarPorStatus(status: StatusTarefa): List<Tarefa> {
        return tarefas.values
            .filter { it.status == status }
            .sortedByDescending { it.dataCriacao }
    }

    /**
     * Busca tarefas por título (contém)
     */
    fun buscarPorTitulo(titulo: String): List<Tarefa> {
        return tarefas.values
            .filter { it.titulo.contains(titulo, ignoreCase = true) }
            .sortedByDescending { it.dataCriacao }
    }

    /**
     * Remove uma tarefa
     */
    fun remover(id: Long): Boolean {
        return tarefas.remove(id) != null
    }

    /**
     * Conta tarefas por status
     */
    fun contarPorStatus(status: StatusTarefa): Int {
        return tarefas.values.count { it.status == status }
    }

    /**
     * Conta total de tarefas
     */
    fun contarTotal(): Int {
        return tarefas.size
    }

    /**
     * Verifica se existe tarefa com o ID
     */
    fun existe(id: Long): Boolean {
        return tarefas.containsKey(id)
    }

    /**
     * Limpa todas as tarefas (útil para testes)
     */
    fun limparTodas() {
        tarefas.clear()
        contadorId.set(1)
    }
}
