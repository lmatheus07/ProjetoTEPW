package com.biblioteca.biblioteca.controller;

import com.biblioteca.dto.emprestimo.EmprestimoAtualizarStatusDTO;
import com.biblioteca.dto.emprestimo.EmprestimoRequestDTO;
import com.biblioteca.dto.emprestimo.EmprestimoResponseDTO;
import com.biblioteca.service.EmprestimoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/emprestimos")
@RequiredArgsConstructor
@Tag(name = "Empréstimos", description = "CRUD de empréstimos da biblioteca")
public class EmprestimoController {

    private final EmprestimoService emprestimoService;

    @PostMapping
    @Operation(summary = "Registrar empréstimo",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Empréstimo registrado"),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos ou livro já emprestado"),
                    @ApiResponse(responseCode = "404", description = "Usuário ou Livro não encontrado")
            })
    public ResponseEntity<EmprestimoResponseDTO> criar(
            @RequestBody @Valid EmprestimoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(emprestimoService.criar(dto));
    }

    @GetMapping
    @Operation(summary = "Listar todos os empréstimos")
    public ResponseEntity<List<EmprestimoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(emprestimoService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar empréstimo por ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Empréstimo encontrado"),
                    @ApiResponse(responseCode = "404", description = "Empréstimo não encontrado")
            })
    public ResponseEntity<EmprestimoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(emprestimoService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar empréstimo",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Empréstimo atualizado"),
                    @ApiResponse(responseCode = "404", description = "Empréstimo não encontrado")
            })
    public ResponseEntity<EmprestimoResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid EmprestimoRequestDTO dto) {
        return ResponseEntity.ok(emprestimoService.atualizar(id, dto));
    }

    // Rota extra: atualiza só o status (ex: devolver o livro)
    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status do empréstimo",
            description = "Use para marcar como DEVOLVIDO ou ATRASADO sem alterar os outros campos.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Status atualizado"),
                    @ApiResponse(responseCode = "400", description = "Status inválido"),
                    @ApiResponse(responseCode = "404", description = "Empréstimo não encontrado")
            })
    public ResponseEntity<EmprestimoResponseDTO> atualizarStatus(
            @PathVariable Long id,
            @RequestBody @Valid EmprestimoAtualizarStatusDTO dto) {
        return ResponseEntity.ok(emprestimoService.atualizarStatus(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir empréstimo",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Empréstimo excluído"),
                    @ApiResponse(responseCode = "404", description = "Empréstimo não encontrado")
            })
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        emprestimoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}