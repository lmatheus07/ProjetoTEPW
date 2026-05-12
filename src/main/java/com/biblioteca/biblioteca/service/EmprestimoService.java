package com.biblioteca.biblioteca.service;

import com.biblioteca.biblioteca.dto.emprestimo.EmprestimoAtualizarStatusDTO;
import com.biblioteca.biblioteca.dto.emprestimo.EmprestimoRequestDTO;
import com.biblioteca.biblioteca.dto.emprestimo.EmprestimoResponseDTO;
import com.biblioteca.biblioteca.exception.ResourceNotFoundException;
import com.biblioteca.biblioteca.model.*;
import com.biblioteca.biblioteca.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;
    private final UsuarioRepository usuarioRepository;
    private final LivroRepository livroRepository;

    public EmprestimoResponseDTO criar(EmprestimoRequestDTO dto) {
        // Impede empréstimo de um livro que já está ativo
        if (emprestimoRepository.existsByLivroIdAndStatus(dto.livroId(), "ATIVO")) {
            throw new IllegalArgumentException(
                    "Livro id=" + dto.livroId() + " já possui um empréstimo ativo.");
        }

        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuário não encontrado: id=" + dto.usuarioId()));

        Livro livro = livroRepository.findById(dto.livroId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Livro não encontrado: id=" + dto.livroId()));

        Emprestimo emprestimo = Emprestimo.builder()
                .usuario(usuario)
                .livro(livro)
                .dataEmprestimo(LocalDate.now()) // sempre hoje
                .dataDevolucao(dto.dataDevolucao())
                .status("ATIVO")                 // sempre começa como ATIVO
                .build();

        return toDTO(emprestimoRepository.save(emprestimo));
    }

    public List<EmprestimoResponseDTO> listarTodos() {
        return emprestimoRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public EmprestimoResponseDTO buscarPorId(Long id) {
        return emprestimoRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Empréstimo não encontrado: id=" + id));
    }

    // PUT padrão: atualiza devolução e status juntos
    public EmprestimoResponseDTO atualizar(Long id, EmprestimoRequestDTO dto) {
        Emprestimo emprestimo = emprestimoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Empréstimo não encontrado: id=" + id));

        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuário não encontrado: id=" + dto.usuarioId()));

        Livro livro = livroRepository.findById(dto.livroId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Livro não encontrado: id=" + dto.livroId()));

        emprestimo.setUsuario(usuario);
        emprestimo.setLivro(livro);
        emprestimo.setDataDevolucao(dto.dataDevolucao());

        return toDTO(emprestimoRepository.save(emprestimo));
    }

    // PATCH: atualiza apenas o status (ex: devolver um livro)
    public EmprestimoResponseDTO atualizarStatus(Long id, EmprestimoAtualizarStatusDTO dto) {
        Emprestimo emprestimo = emprestimoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Empréstimo não encontrado: id=" + id));

        emprestimo.setStatus(dto.status());
        return toDTO(emprestimoRepository.save(emprestimo));
    }

    public void deletar(Long id) {
        if (!emprestimoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Empréstimo não encontrado: id=" + id);
        }
        emprestimoRepository.deleteById(id);
    }

    private EmprestimoResponseDTO toDTO(Emprestimo e) {
        return new EmprestimoResponseDTO(
                e.getId(),
                e.getDataEmprestimo(),
                e.getDataDevolucao(),
                e.getStatus(),
                e.getUsuario().getId(),
                e.getUsuario().getNome(),
                e.getLivro().getId(),
                e.getLivro().getTitulo());
    }
}
