package com.biblioteca.biblioteca.service;

import com.biblioteca.biblioteca.dto.livro.LivroRequestDTO;
import com.biblioteca.biblioteca.dto.livro.LivroResponseDTO;
import com.biblioteca.biblioteca.exception.ResourceNotFoundException;
import com.biblioteca.biblioteca.model.*;
import com.biblioteca.biblioteca.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

// service/LivroService.java  — versão atualizada com MinIO
@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;
    private final CategoriaRepository categoriaRepository;
    private final MinioService minioService; // ← injeta o MinioService

    public LivroResponseDTO criar(LivroRequestDTO dto, MultipartFile arquivoPdf) {
        if (livroRepository.existsByIsbn(dto.isbn())) {
            throw new IllegalArgumentException("ISBN já cadastrado: " + dto.isbn());
        }

        Autor autor = autorRepository.findById(dto.autorId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Autor não encontrado: id=" + dto.autorId()));

        Categoria categoria = categoriaRepository.findById(dto.categoriaId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Categoria não encontrada: id=" + dto.categoriaId()));

        // Faz o upload e guarda o nome do arquivo no banco
        String nomePdf = null;
        if (arquivoPdf != null && !arquivoPdf.isEmpty()) {
            nomePdf = minioService.uploadPdf(arquivoPdf);
        }

        Livro livro = Livro.builder()
                .titulo(dto.titulo())
                .isbn(dto.isbn())
                .anoPublicacao(dto.anoPublicacao())
                .pdfUrl(nomePdf) // salva o nome do arquivo, não a URL completa
                .autor(autor)
                .categoria(categoria)
                .build();

        return toDTO(livroRepository.save(livro));
    }

    public LivroResponseDTO atualizar(Long id, LivroRequestDTO dto, MultipartFile arquivoPdf) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Livro não encontrado: id=" + id));

        if (!livro.getIsbn().equals(dto.isbn())
                && livroRepository.existsByIsbn(dto.isbn())) {
            throw new IllegalArgumentException("ISBN já cadastrado: " + dto.isbn());
        }

        Autor autor = autorRepository.findById(dto.autorId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Autor não encontrado: id=" + dto.autorId()));

        Categoria categoria = categoriaRepository.findById(dto.categoriaId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Categoria não encontrada: id=" + dto.categoriaId()));

        // Se enviou novo PDF, deleta o antigo e faz upload do novo
        if (arquivoPdf != null && !arquivoPdf.isEmpty()) {
            if (livro.getPdfUrl() != null) {
                minioService.deletarPdf(livro.getPdfUrl());
            }
            livro.setPdfUrl(minioService.uploadPdf(arquivoPdf));
        }

        livro.setTitulo(dto.titulo());
        livro.setIsbn(dto.isbn());
        livro.setAnoPublicacao(dto.anoPublicacao());
        livro.setAutor(autor);
        livro.setCategoria(categoria);

        return toDTO(livroRepository.save(livro));
    }

    // Gera URL temporária de download para um livro
    public String gerarUrlDownload(Long id) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Livro não encontrado: id=" + id));

        if (livro.getPdfUrl() == null || livro.getPdfUrl().isBlank()) {
            throw new IllegalArgumentException("Este livro não possui PDF cadastrado.");
        }

        return minioService.gerarUrlDownload(livro.getPdfUrl());
    }

    public void deletar(Long id) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Livro não encontrado: id=" + id));

        // Remove o PDF do MinIO antes de deletar o livro do banco
        if (livro.getPdfUrl() != null) {
            minioService.deletarPdf(livro.getPdfUrl());
        }

        livroRepository.deleteById(id);
    }

    // listarTodos e buscarPorId permanecem iguais
    public List<LivroResponseDTO> listarTodos() {
        return livroRepository.findAll().stream().map(this::toDTO).toList();
    }

    public LivroResponseDTO buscarPorId(Long id) {
        return livroRepository.findById(id).map(this::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Livro não encontrado: id=" + id));
    }

    private LivroResponseDTO toDTO(Livro l) {
        return new LivroResponseDTO(
                l.getId(), l.getTitulo(), l.getIsbn(),
                l.getAnoPublicacao(), l.getPdfUrl(),
                l.getAutor().getId(), l.getAutor().getNome(),
                l.getCategoria().getId(), l.getCategoria().getNome());
    }
}