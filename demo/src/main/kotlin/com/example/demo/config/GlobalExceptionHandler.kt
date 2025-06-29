package com.example.demo.config

import com.example.demo.exception.TarefaNaoEncontradaException
import com.example.demo.exception.TransicaoStatusInvalidaException
import com.example.demo.exception.ValidacaoNegocioException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime

/**
 * Handler global para tratamento de exceções
 */
@RestControllerAdvice
class GlobalExceptionHandler {

    data class ErrorResponse(
        val timestamp: LocalDateTime = LocalDateTime.now(),
        val status: Int,
        val error: String,
        val message: String,
        val details: Map<String, Any>? = null
    )

    /**
     * Tarefa não encontrada
     */
    @ExceptionHandler(TarefaNaoEncontradaException::class)
    fun handleTarefaNaoEncontrada(ex: TarefaNaoEncontradaException): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            status = HttpStatus.NOT_FOUND.value(),
            error = "Tarefa Não Encontrada",
            message = ex.message ?: "Tarefa não encontrada"
        )
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error)
    }

    /**
     * Transição de status inválida
     */
    @ExceptionHandler(TransicaoStatusInvalidaException::class)
    fun handleTransicaoStatusInvalida(ex: TransicaoStatusInvalidaException): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Transição de Status Inválida",
            message = ex.message ?: "Transição de status não permitida"
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error)
    }

    /**
     * Validação de negócio
     */
    @ExceptionHandler(ValidacaoNegocioException::class)
    fun handleValidacaoNegocio(ex: ValidacaoNegocioException): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Erro de Validação",
            message = ex.message ?: "Erro de validação de negócio"
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error)
    }

    /**
     * Erros de validação de campos
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationErrors(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val errors = mutableMapOf<String, String>()
        
        ex.bindingResult.allErrors.forEach { error ->
            val fieldName = (error as FieldError).field
            val errorMessage = error.defaultMessage ?: "Erro de validação"
            errors[fieldName] = errorMessage
        }

        val error = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Dados Inválidos",
            message = "Os dados fornecidos contêm erros de validação",
            details = mapOf("camposComErro" to errors)
        )
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error)
    }

    /**
     * Exceções gerais
     */
    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<ErrorResponse> {
        println("🚨 Erro não tratado: ${ex.message}")
        ex.printStackTrace()
        
        val error = ErrorResponse(
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            error = "Erro Interno",
            message = "Ocorreu um erro inesperado. Tente novamente."
        )
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error)
    }
}
