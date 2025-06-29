package com.example.demo.controller

import com.example.demo.dto.*
import com.example.demo.enum.StatusTarefa
import com.example.demo.service.TarefaService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/tarefas")
@CrossOrigin(origins = ["*"]) // Permite acesso de qualquer origem
class TarefaController(
    private val tarefaService: TarefaService
) {

    /**
     * 📝 Criar uma nova tarefa
     * POST /api/tarefas
     */
    @PostMapping
    fun criarTarefa(@Valid @RequestBody request: CriarTarefaRequest): ResponseEntity<TarefaResponse> {
        val tarefa = tarefaService.criarTarefa(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(tarefa)
    }

    /**
     * 📋 Listar todas as tarefas
     * GET /api/tarefas
     */
    @GetMapping
    fun listarTodasTarefas(): ResponseEntity<TarefaListResponse> {
        val tarefas = tarefaService.listarTodasTarefas()
        return ResponseEntity.ok(tarefas)
    }

    /**
     * 🔍 Buscar tarefa por ID
     * GET /api/tarefas/{id}
     */
    @GetMapping("/{id}")
    fun buscarTarefaPorId(@PathVariable id: Long): ResponseEntity<TarefaResponse> {
        val tarefa = tarefaService.buscarTarefaPorId(id)
        return ResponseEntity.ok(tarefa)
    }

    /**
     * ✏️ Atualizar uma tarefa
     * PUT /api/tarefas/{id}
     */
    @PutMapping("/{id}")
    fun atualizarTarefa(
        @PathVariable id: Long,
        @Valid @RequestBody request: AtualizarTarefaRequest
    ): ResponseEntity<TarefaResponse> {
        val tarefa = tarefaService.atualizarTarefa(id, request)
        return ResponseEntity.ok(tarefa)
    }

    /**
     * 🗑️ Remover uma tarefa
     * DELETE /api/tarefas/{id}
     */
    @DeleteMapping("/{id}")
    fun removerTarefa(@PathVariable id: Long): ResponseEntity<Map<String, String>> {
        tarefaService.removerTarefa(id)
        return ResponseEntity.ok(mapOf(
            "message" to "✅ Tarefa removida com sucesso",
            "id" to id.toString()
        ))
    }

    /**
     * 🔄 Alterar status de uma tarefa
     * PATCH /api/tarefas/{id}/status
     */
    @PatchMapping("/{id}/status")
    fun alterarStatusTarefa(
        @PathVariable id: Long,
        @Valid @RequestBody request: AlterarStatusRequest
    ): ResponseEntity<TarefaResponse> {
        val tarefa = tarefaService.alterarStatusTarefa(id, request)
        return ResponseEntity.ok(tarefa)
    }

    /**
     * 📊 Listar tarefas por status específico
     * GET /api/tarefas/status/{status}
     */
    @GetMapping("/status/{status}")
    fun listarTarefasPorStatus(@PathVariable status: StatusTarefa): ResponseEntity<TarefaListResponse> {
        val tarefas = tarefaService.listarTarefasPorStatus(status)
        return ResponseEntity.ok(tarefas)
    }

    /**
     * 🔎 Buscar tarefas por título
     * GET /api/tarefas/buscar?titulo={titulo}
     */
    @GetMapping("/buscar")
    fun buscarTarefasPorTitulo(@RequestParam titulo: String): ResponseEntity<TarefaListResponse> {
        val tarefas = tarefaService.buscarTarefasPorTitulo(titulo)
        return ResponseEntity.ok(tarefas)
    }

    /**
     * 📈 Obter estatísticas das tarefas
     * GET /api/tarefas/estatisticas
     */
    @GetMapping("/estatisticas")
    fun obterEstatisticas(): ResponseEntity<Map<String, Any>> {
        val estatisticas = tarefaService.obterEstatisticas()
        return ResponseEntity.ok(estatisticas)
    }

    /**
     * ❓ Endpoint de ajuda - mostra as regras de transição
     * GET /api/tarefas/ajuda/regras
     */
    @GetMapping("/ajuda/regras")
    fun obterRegrasDeNegocio(): ResponseEntity<Map<String, Any>> {
        val regras = mapOf(
            "fluxoDeStatus" to "PENDENTE → EM_ANDAMENTO → CONCLUIDA",
            "transicoesPermitidas" to mapOf(
                "PENDENTE" to listOf("EM_ANDAMENTO"),
                "EM_ANDAMENTO" to listOf("CONCLUIDA"),
                "CONCLUIDA" to emptyList<String>()
            ),
            "regrasEspeciais" to listOf(
                "✅ PENDENTE pode ir para EM_ANDAMENTO",
                "✅ EM_ANDAMENTO pode ir para CONCLUIDA",
                "❌ Não pode pular de PENDENTE para CONCLUIDA",
                "❌ Não pode retroceder status",
                "❌ CONCLUIDA não pode mudar de status",
                "🗑️ Tarefas CONCLUIDAS não podem ser removidas"
            ),
            "exemploDeUso" to mapOf(
                "criarTarefa" to "POST /api/tarefas com {\"titulo\": \"Minha tarefa\"}",
                "iniciarTarefa" to "PATCH /api/tarefas/1/status com {\"novoStatus\": \"EM_ANDAMENTO\"}",
                "concluirTarefa" to "PATCH /api/tarefas/1/status com {\"novoStatus\": \"CONCLUIDA\"}"
            )
        )
        return ResponseEntity.ok(regras)
    }

    /**
     * 🏠 Endpoint raiz - informações da API
     * GET /api/tarefas/info
     */
    @GetMapping("/info")
    fun obterInformacoesAPI(): ResponseEntity<Map<String, Any>> {
        val info = mapOf(
            "nome" to "🎯 Sistema de Gerenciamento de Tarefas",
            "versao" to "1.0.0",
            "descricao" to "API REST para gerenciar tarefas com controle de status",
            "statusDisponiveis" to StatusTarefa.values().map { it.name },
            "endpoints" to mapOf(
                "crud" to listOf(
                    "POST /api/tarefas - Criar",
                    "GET /api/tarefas - Listar todas",
                    "GET /api/tarefas/{id} - Buscar por ID",
                    "PUT /api/tarefas/{id} - Atualizar",
                    "DELETE /api/tarefas/{id} - Remover"
                ),
                "status" to listOf(
                    "PATCH /api/tarefas/{id}/status - Alterar status",
                    "GET /api/tarefas/status/{status} - Listar por status"
                ),
                "utilitarios" to listOf(
                    "GET /api/tarefas/buscar?titulo=X - Buscar",
                    "GET /api/tarefas/estatisticas - Estatísticas",
                    "GET /api/tarefas/ajuda/regras - Regras de negócio"
                )
            )
        )
        return ResponseEntity.ok(info)
    }
}
