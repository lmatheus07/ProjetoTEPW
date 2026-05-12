package com.biblioteca.biblioteca.service;

import com.biblioteca.biblioteca.dto.usuario.UsuarioRequestDTO;
import com.biblioteca.biblioteca.dto.usuario.UsuarioResponseDTO;
import com.biblioteca.biblioteca.exception.ResourceNotFoundException;
import com.biblioteca.biblioteca.model.Usuario;
import com.biblioteca.biblioteca.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioResponseDTO criar(UsuarioRequestDTO dto) {
        // Valida duplicidade de e-mail e CPF antes de tentar salvar
        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("E-mail já cadastrado: " + dto.email());
        }
        if (usuarioRepository.existsByCpf(dto.cpf())) {
            throw new IllegalArgumentException("CPF já cadastrado: " + dto.cpf());
        }

        Usuario usuario = Usuario.builder()
                .nome(dto.nome())
                .email(dto.email())
                .cpf(dto.cpf())
                .telefone(dto.telefone())
                .dataCadastro(LocalDate.now()) // definido automaticamente
                .build();

        return toDTO(usuarioRepository.save(usuario));
    }

    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public UsuarioResponseDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuário não encontrado: id=" + id));
        return toDTO(usuario);
    }

    public UsuarioResponseDTO atualizar(Long id, UsuarioRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuário não encontrado: id=" + id));

        // Valida duplicidade apenas se o novo valor for diferente do atual
        if (!usuario.getEmail().equals(dto.email())
                && usuarioRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("E-mail já cadastrado: " + dto.email());
        }
        if (!usuario.getCpf().equals(dto.cpf())
                && usuarioRepository.existsByCpf(dto.cpf())) {
            throw new IllegalArgumentException("CPF já cadastrado: " + dto.cpf());
        }

        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setCpf(dto.cpf());
        usuario.setTelefone(dto.telefone());
        // dataCadastro nunca é alterada no update

        return toDTO(usuarioRepository.save(usuario));
    }

    public void deletar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuário não encontrado: id=" + id);
        }
        usuarioRepository.deleteById(id);
    }

    private UsuarioResponseDTO toDTO(Usuario u) {
        return new UsuarioResponseDTO(
                u.getId(), u.getNome(), u.getEmail(),
                u.getCpf(), u.getTelefone(), u.getDataCadastro());
    }
}
