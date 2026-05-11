package com.biblioteca.biblioteca.service;

import com.biblioteca.biblioteca.dto.categoria.CategoriaRequestDTO;
import com.biblioteca.biblioteca.dto.categoria.CategoriaResponseDTO;
import com.biblioteca.biblioteca.exception.ResourceNotFoundException;
import com.biblioteca.biblioteca.model.Categoria;
import com.biblioteca.biblioteca.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaResponseDTO criar(CategoriaRequestDTO dto) {
        if (categoriaRepository.existsByCodigoIgnoreCase(dto.codigo())) {
            throw new IllegalArgumentException("Código já cadastrado: " + dto.codigo());
        }

        Categoria categoria = Categoria.builder()
                .nome(dto.nome())
                .descricao(dto.descricao())
                .codigo(dto.codigo().toUpperCase()) // padroniza o código em maiúsculas
                .build();

        return toDTO(categoriaRepository.save(categoria));
    }

    public List<CategoriaResponseDTO> listarTodos() {
        return categoriaRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public CategoriaResponseDTO buscarPorId(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Categoria não encontrada: id=" + id));
        return toDTO(categoria);
    }

    public CategoriaResponseDTO atualizar(Long id, CategoriaRequestDTO dto) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Categoria não encontrada: id=" + id));

        // Valida duplicidade de código apenas se foi alterado
        if (!categoria.getCodigo().equalsIgnoreCase(dto.codigo())
                && categoriaRepository.existsByCodigoIgnoreCase(dto.codigo())) {
            throw new IllegalArgumentException("Código já cadastrado: " + dto.codigo());
        }

        categoria.setNome(dto.nome());
        categoria.setDescricao(dto.descricao());
        categoria.setCodigo(dto.codigo().toUpperCase());

        return toDTO(categoriaRepository.save(categoria));
    }

    public void deletar(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Categoria não encontrada: id=" + id);
        }
        categoriaRepository.deleteById(id);
    }

    private CategoriaResponseDTO toDTO(Categoria c) {
        return new CategoriaResponseDTO(
                c.getId(), c.getNome(), c.getDescricao(), c.getCodigo());
    }
}
