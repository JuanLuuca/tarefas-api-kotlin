package com.example.demo.enum

/**
 * Enum que representa os possíveis status de uma tarefa
 */
enum class StatusTarefa {
    PENDENTE,
    EM_ANDAMENTO,
    CONCLUIDA;

    /**
     * Verifica se a transição para outro status é válida
     * Regras de negócio:
     * - PENDENTE → EM_ANDAMENTO ✅
     * - EM_ANDAMENTO → CONCLUIDA ✅
     * - PENDENTE → CONCLUIDA ❌ (não pode pular etapa)
     * - Não pode retroceder status ❌
     */
    fun podeTransicionarPara(novoStatus: StatusTarefa): Boolean {
        return when (this) {
            PENDENTE -> novoStatus == EM_ANDAMENTO
            EM_ANDAMENTO -> novoStatus == CONCLUIDA
            CONCLUIDA -> false // Tarefa concluída não pode mudar
        }
    }

    /**
     * Retorna os próximos status válidos
     */
    fun proximosStatusValidos(): List<StatusTarefa> {
        return when (this) {
            PENDENTE -> listOf(EM_ANDAMENTO)
            EM_ANDAMENTO -> listOf(CONCLUIDA)
            CONCLUIDA -> emptyList()
        }
    }
}
