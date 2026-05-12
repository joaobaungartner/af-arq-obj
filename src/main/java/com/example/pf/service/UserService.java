package com.example.pf.service;

import com.example.pf.dto.edit.EditUserDTO;
import com.example.pf.dto.response.ResponseEventoDTO;
import com.example.pf.dto.response.ResponseUserDTO;
import com.example.pf.dto.save.SaveUserDTO;
import com.example.pf.exception.ForbiddenException;
import com.example.pf.models.Evento;
import com.example.pf.models.Papel;
import com.example.pf.models.User;
import com.example.pf.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<ResponseUserDTO> listar() {
        return userRepository.findAll()
                .stream()
                .map(ResponseUserDTO::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ResponseUserDTO buscarPorId(UUID id) {
        User user = buscarUserOuFalhar(id);
        return ResponseUserDTO.from(user);
    }

    @Transactional(readOnly = true)
    public List<ResponseEventoDTO> listarEventosDoUsuario(UUID userId) {
        User user = buscarUserOuFalhar(userId);

        return user.getEventos()
                .stream()
                .map(ResponseEventoDTO::from)
                .toList();
    }

    @Transactional
    public ResponseUserDTO criar(UUID authUserId, SaveUserDTO dto) {
        exigirAdmin(authUserId);

        validarNome(dto.getNome());
        validarCpf(dto.getCpf());

        if (userRepository.existsByCpf(dto.getCpf())) {
            throw new IllegalArgumentException("Já existe um usuário cadastrado com esse CPF");
        }

        User user = new User(
                dto.getNome(),
                dto.getCpf(),
                dto.getPapel()
        );

        User salvo = userRepository.save(user);

        return ResponseUserDTO.from(salvo);
    }

    @Transactional
    public ResponseUserDTO atualizar(UUID authUserId, UUID id, EditUserDTO dto) {
        exigirAdmin(authUserId);

        User user = buscarUserOuFalhar(id);

        if (dto.getNome() != null) {
            validarNome(dto.getNome());
            user.setNome(dto.getNome());
        }

        if (dto.getCpf() != null) {
            validarCpf(dto.getCpf());

            Optional<User> userComMesmoCpf = userRepository.findByCpf(dto.getCpf());

            if (userComMesmoCpf.isPresent() && !userComMesmoCpf.get().getId().equals(id)) {
                throw new IllegalArgumentException("Já existe outro usuário cadastrado com esse CPF");
            }

            user.setCpf(dto.getCpf());
        }

        if (dto.getPapel() != null) {
            user.setPapel(dto.getPapel());
        }

        User atualizado = userRepository.save(user);

        return ResponseUserDTO.from(atualizado);
    }

    @Transactional
    public void deletar(UUID authUserId, UUID id) {
        exigirAdmin(authUserId);

        User user = buscarUserOuFalhar(id);

        for (Evento evento : new HashSet<>(user.getEventos())) {
            evento.removerParticipante(user);
        }

        userRepository.delete(user);
    }

    private User buscarUserOuFalhar(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
    }

    private void exigirAdmin(UUID authUserId) {
        if (authUserId == null) {
            throw new ForbiddenException("Header X-USER-ID é obrigatório");
        }

        User authUser = userRepository.findById(authUserId)
                .orElseThrow(() -> new ForbiddenException("Usuário do header X-USER-ID não encontrado"));

        if (authUser.getPapel() != Papel.ADMIN) {
            throw new ForbiddenException("Apenas usuários ADMIN podem realizar essa ação");
        }
    }

    private void validarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome é obrigatório");
        }
    }

    private void validarCpf(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new IllegalArgumentException("O CPF é obrigatório");
        }
    }
}