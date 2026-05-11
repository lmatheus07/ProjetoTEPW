package com.biblioteca.biblioteca.repository;

import com.biblioteca.biblioteca.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {

    // Verifica duplicidade de nome antes de salvar
    boolean existsByNomeIgnoreCase(String nome);

    // Filtro por nome para buscas parciais
    List<Autor> findByNomeContainingIgnoreCase(String nome);
}