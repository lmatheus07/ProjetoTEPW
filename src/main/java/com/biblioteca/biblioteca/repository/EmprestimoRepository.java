package com.biblioteca.repository;

import com.biblioteca.model.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

    List<Emprestimo> findByUsuarioId(Long usuarioId);
    List<Emprestimo> findByLivroId(Long livroId);
    List<Emprestimo> findByStatus(String status);

    // Verifica se o livro já está emprestado (status ATIVO)
    boolean existsByLivroIdAndStatus(Long livroId, String status);
}