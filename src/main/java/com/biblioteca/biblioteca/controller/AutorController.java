package com.biblioteca.biblioteca.controller;

import com.biblioteca.biblioteca.dto.autor.AutorRequestDTO;
import com.biblioteca.biblioteca.dto.autor.AutorResponseDTO;
import com.biblioteca.biblioteca.service.AutorService;
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
@RequestMapping("/api/autores")
@RequiredArgsConstructor
@Tag(name = "Autores", description = "CRUD de autores da biblioteca")
public class AutorController {

    private final AutorService autorService;

    @PostMapping
    @Operation(summary = "Cadastrar autor",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Autor criado"),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "409", description = "Nome já cadastrado")
            })
    public ResponseEntity<AutorResponseDTO> criar(
            @RequestBody @Valid AutorRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(autorService.criar(dto));
    }

    @GetMapping
    @Operation(summary = "Listar todos os autores")
    public ResponseEntity<List<AutorResponseDTO>> listarTodos() {
        return ResponseEntity.ok(autorService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar autor por ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Autor encontrado"),
                    @ApiResponse(responseCode = "404", description = "Autor não encontrado")
            })
    public ResponseEntity<AutorResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(autorService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar autor",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Autor atualizado"),
                    @ApiResponse(responseCode = "404", description = "Autor não encontrado"),
                    @ApiResponse(responseCode = "409", description = "Nome já cadastrado")
            })
    public ResponseEntity<AutorResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid AutorRequestDTO dto) {
        return ResponseEntity.ok(autorService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir autor",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Autor excluído"),
                    @ApiResponse(responseCode = "404", description = "Autor não encontrado")
            })
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        autorService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}