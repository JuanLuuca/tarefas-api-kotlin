package com.example.demo.exception

import com.example.demo.enum.StatusTarefa

/**
 * Exceção para tarefa não encontrada
 */
class TarefaNaoEncontradaException(id: Long) : 
    RuntimeException("Tarefa com ID $id não encontrada")

/**
 * Exceção para transição de status inválida
 */
class TransicaoStatusInvalidaException(
    statusAtual: StatusTarefa,
    novoStatus: StatusTarefa
) : RuntimeException(
    "❌ Transição inválida: não é possível mudar de '$statusAtual' para '$novoStatus'. " +
    "Transições válidas a partir de '$statusAtual': ${statusAtual.proximosStatusValidos()}"
)

/**
 * Exceção para validação de negócio
 */
class ValidacaoNegocioException(message: String) : RuntimeException(message)
