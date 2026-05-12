package com.biblioteca.biblioteca.controller;

import com.biblioteca.biblioteca.dto.livro.LivroRequestDTO;
import com.biblioteca.biblioteca.dto.livro.LivroResponseDTO;
import com.biblioteca.biblioteca.service.LivroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

// controller/LivroController.java — versão atualizada com MinIO
@RestController
@RequestMapping("/api/livros")
@RequiredArgsConstructor
@Tag(name = "Livros", description = "CRUD de livros com suporte a PDF via MinIO")
public class LivroController {

    private final LivroService livroService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Cadastrar livro com PDF",
            description = "Envie os dados do livro como JSON no campo 'dados' e o PDF no campo 'arquivo'.")
    public ResponseEntity<LivroResponseDTO> criar(
            @RequestPart("dados") @Valid LivroRequestDTO dto,
            @RequestPart(value = "arquivo", required = false) MultipartFile arquivoPdf) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(livroService.criar(dto, arquivoPdf));
    }

    @GetMapping
    @Operation(summary = "Listar todos os livros")
    public ResponseEntity<List<LivroResponseDTO>> listarTodos() {
        return ResponseEntity.ok(livroService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar livro por ID")
    public ResponseEntity<LivroResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(livroService.buscarPorId(id));
    }

    // Rota exclusiva para baixar o PDF — retorna URL temporária
    @GetMapping("/{id}/download")
    @Operation(summary = "Gerar link de download do PDF",
            description = "Retorna uma URL temporária válida por 1 hora para acessar o PDF do livro.")
    public ResponseEntity<String> downloadPdf(@PathVariable Long id) {
        return ResponseEntity.ok(livroService.gerarUrlDownload(id));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Atualizar livro",
            description = "O campo 'arquivo' é opcional. Se enviado, substitui o PDF atual.")
    public ResponseEntity<LivroResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestPart("dados") @Valid LivroRequestDTO dto,
            @RequestPart(value = "arquivo", required = false) MultipartFile arquivoPdf) {

        return ResponseEntity.ok(livroService.atualizar(id, dto, arquivoPdf));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir livro e seu PDF")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        livroService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
