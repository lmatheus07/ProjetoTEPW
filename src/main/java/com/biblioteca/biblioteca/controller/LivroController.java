package com.biblioteca.biblioteca.controller;

import com.biblioteca.dto.livro.LivroRequestDTO;
import com.biblioteca.dto.livro.LivroResponseDTO;
import com.biblioteca.service.LivroService;
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
@RequestMapping("/api/livros")
@RequiredArgsConstructor
@Tag(name = "Livros", description = "CRUD de livros da biblioteca")
public class LivroController {

    private final LivroService livroService;

    @PostMapping
    @Operation(summary = "Cadastrar livro",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Livro criado"),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "404", description = "Autor ou Categoria não encontrado"),
                    @ApiResponse(responseCode = "409", description = "ISBN já cadastrado")
            })
    public ResponseEntity<LivroResponseDTO> criar(
            @RequestBody @Valid LivroRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(livroService.criar(dto));
    }

    @GetMapping
    @Operation(summary = "Listar todos os livros")
    public ResponseEntity<List<LivroResponseDTO>> listarTodos() {
        return ResponseEntity.ok(livroService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar livro por ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Livro encontrado"),
                    @ApiResponse(responseCode = "404", description = "Livro não encontrado")
            })
    public ResponseEntity<LivroResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(livroService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar livro",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Livro atualizado"),
                    @ApiResponse(responseCode = "404", description = "Livro, Autor ou Categoria não encontrado"),
                    @ApiResponse(responseCode = "409", description = "ISBN já cadastrado")
            })
    public ResponseEntity<LivroResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid LivroRequestDTO dto) {
        return ResponseEntity.ok(livroService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir livro",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Livro excluído"),
                    @ApiResponse(responseCode = "404", description = "Livro não encontrado")
            })
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        livroService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}