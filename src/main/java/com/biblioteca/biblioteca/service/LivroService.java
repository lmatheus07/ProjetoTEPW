package com.biblioteca.biblioteca.service;

import com.biblioteca.dto.livro.LivroRequestDTO;
import com.biblioteca.dto.livro.LivroResponseDTO;
import com.biblioteca.exception.ResourceNotFoundException;
import com.biblioteca.model.*;
import com.biblioteca.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;
    private final CategoriaRepository categoriaRepository;

    public LivroResponseDTO criar(LivroRequestDTO dto) {
        if (livroRepository.existsByIsbn(dto.isbn())) {
            throw new IllegalArgumentException("ISBN já cadastrado: " + dto.isbn());
        }

        Autor autor = autorRepository.findById(dto.autorId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Autor não encontrado: id=" + dto.autorId()));

        Categoria categoria = categoriaRepository.findById(dto.categoriaId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Categoria não encontrada: id=" + dto.categoriaId()));

        Livro livro = Livro.builder()
                .titulo(dto.titulo())
                .isbn(dto.isbn())
                .anoPublicacao(dto.anoPublicacao())
                .pdfUrl(dto.pdfUrl())
                .autor(autor)
                .categoria(categoria)
                .build();

        return toDTO(livroRepository.save(livro));
    }

    public List<LivroResponseDTO> listarTodos() {
        return livroRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public LivroResponseDTO buscarPorId(Long id) {
        return livroRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Livro não encontrado: id=" + id));
    }

    public LivroResponseDTO atualizar(Long id, LivroRequestDTO dto) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Livro não encontrado: id=" + id));

        // Valida ISBN apenas se for diferente do atual
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

        livro.setTitulo(dto.titulo());
        livro.setIsbn(dto.isbn());
        livro.setAnoPublicacao(dto.anoPublicacao());
        livro.setPdfUrl(dto.pdfUrl());
        livro.setAutor(autor);
        livro.setCategoria(categoria);

        return toDTO(livroRepository.save(livro));
    }

    public void deletar(Long id) {
        if (!livroRepository.existsById(id)) {
            throw new ResourceNotFoundException("Livro não encontrado: id=" + id);
        }
        livroRepository.deleteById(id);
    }

    private LivroResponseDTO toDTO(Livro l) {
        return new LivroResponseDTO(
                l.getId(), l.getTitulo(), l.getIsbn(),
                l.getAnoPublicacao(), l.getPdfUrl(),
                l.getAutor().getId(), l.getAutor().getNome(),
                l.getCategoria().getId(), l.getCategoria().getNome());
    }
}