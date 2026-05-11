package com.biblioteca.biblioteca.service;

import com.biblioteca.biblioteca.dto.autor.AutorRequestDTO;
import com.biblioteca.biblioteca.dto.autor.AutorResponseDTO;
import com.biblioteca.biblioteca.exception.ResourceNotFoundException;
import com.biblioteca.biblioteca.model.Autor;
import com.biblioteca.biblioteca.repository.AutorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AutorService {

    private final AutorRepository autorRepository;

    public AutorResponseDTO criar(AutorRequestDTO dto) {
        if (autorRepository.existsByNomeIgnoreCase(dto.nome())) {
            throw new IllegalArgumentException("Autor já cadastrado com o nome: " + dto.nome());
        }

        Autor autor = Autor.builder()
                .nome(dto.nome())
                .nascimento(dto.nascimento())
                .bio(dto.bio())
                .nacionalidade(dto.nacionalidade())
                .build();

        return toDTO(autorRepository.save(autor));
    }

    public List<AutorResponseDTO> listarTodos() {
        return autorRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public AutorResponseDTO buscarPorId(Long id) {
        Autor autor = autorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Autor não encontrado: id=" + id));
        return toDTO(autor);
    }

    public AutorResponseDTO atualizar(Long id, AutorRequestDTO dto) {
        Autor autor = autorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Autor não encontrado: id=" + id));

        // Valida duplicidade de nome apenas se foi alterado
        if (!autor.getNome().equalsIgnoreCase(dto.nome())
                && autorRepository.existsByNomeIgnoreCase(dto.nome())) {
            throw new IllegalArgumentException("Autor já cadastrado com o nome: " + dto.nome());
        }

        autor.setNome(dto.nome());
        autor.setNascimento(dto.nascimento());
        autor.setBio(dto.bio());
        autor.setNacionalidade(dto.nacionalidade());

        return toDTO(autorRepository.save(autor));
    }

    public void deletar(Long id) {
        if (!autorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Autor não encontrado: id=" + id);
        }
        autorRepository.deleteById(id);
    }

    private AutorResponseDTO toDTO(Autor a) {
        return new AutorResponseDTO(
                a.getId(), a.getNome(),
                a.getNascimento(), a.getBio(), a.getNacionalidade());
    }
}