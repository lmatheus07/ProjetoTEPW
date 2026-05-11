package com.biblioteca.biblioteca.repository;

import com.biblioteca.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Usado pelo service para checar duplicidade antes de salvar
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);

    // Busca opcional para login ou consulta por e-mail
    Optional<Usuario> findByEmail(String email);
}
