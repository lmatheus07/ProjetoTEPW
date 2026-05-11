package com.biblioteca.biblioteca.controller;

import com.biblioteca.biblioteca.dto.categoria.CategoriaRequestDTO;
import com.biblioteca.biblioteca.dto.categoria.CategoriaResponseDTO;
import com.biblioteca.biblioteca.service.CategoriaService;
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
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
@Tag(name = "Categorias", description = "CRUD de categorias da biblioteca")
public class CategoriaController {

    private final CategoriaService categoriaService;

    @PostMapping
    @Operation(summary = "Cadastrar categoria",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Categoria criada"),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "409", description = "Código já cadastrado")
            })
    public ResponseEntity<CategoriaResponseDTO> criar(
            @RequestBody @Valid CategoriaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoriaService.criar(dto));
    }

    @GetMapping
    @Operation(summary = "Listar todas as categorias")
    public ResponseEntity<List<CategoriaResponseDTO>> listarTodos() {
        return ResponseEntity.ok(categoriaService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar categoria por ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Categoria encontrada"),
                    @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
            })
    public ResponseEntity<CategoriaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar categoria",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Categoria atualizada"),
                    @ApiResponse(responseCode = "404", description = "Categoria não encontrada"),
                    @ApiResponse(responseCode = "409", description = "Código já cadastrado")
            })
    public ResponseEntity<CategoriaResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid CategoriaRequestDTO dto) {
        return ResponseEntity.ok(categoriaService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir categoria",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Categoria excluída"),
                    @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
            })
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        categoriaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}