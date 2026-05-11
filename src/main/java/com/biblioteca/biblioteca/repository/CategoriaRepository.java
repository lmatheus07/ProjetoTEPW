package com.biblioteca.biblioteca.repository;

import com.biblioteca.biblioteca.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    // Verifica duplicidade de código antes de salvar
    boolean existsByCodigoIgnoreCase(String codigo);

    // Filtro por nome para buscas parciais
    List<Categoria> findByNomeContainingIgnoreCase(String nome);
}