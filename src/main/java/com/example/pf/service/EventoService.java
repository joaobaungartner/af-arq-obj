package com.example.pf.service;

import com.example.pf.dto.edit.EditEventoDTO;
import com.example.pf.dto.response.ResponseEventoDTO;
import com.example.pf.dto.response.ResponseUserDTO;
import com.example.pf.dto.save.SaveEventoDTO;
import com.example.pf.exception.ForbiddenException;
import com.example.pf.models.Evento;
import com.example.pf.models.Papel;
import com.example.pf.models.User;
import com.example.pf.repository.EventoRepository;
import com.example.pf.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;
    private final UserRepository userRepository;

    public EventoService(EventoRepository eventoRepository, UserRepository userRepository) {
        this.eventoRepository = eventoRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<ResponseEventoDTO> listar() {
        return eventoRepository.findAll()
                .stream()
                .map(ResponseEventoDTO::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ResponseEventoDTO buscarPorId(UUID id) {
        Evento evento = buscarEventoOuFalhar(id);
        return ResponseEventoDTO.from(evento);
    }

    @Transactional
    public ResponseEventoDTO criar(UUID authUserId, SaveEventoDTO dto) {
        exigirAdmin(authUserId);

        validarNome(dto.getNome());

        if (dto.getData() == null) {
            throw new IllegalArgumentException("A data do evento é obrigatória");
        }

        Evento evento = new Evento(
                dto.getNome(),
                dto.getData()
        );

        Evento salvo = eventoRepository.save(evento);

        return ResponseEventoDTO.from(salvo);
    }

    @Transactional
    public ResponseEventoDTO atualizar(UUID authUserId, UUID id, EditEventoDTO dto) {
        exigirAdmin(authUserId);

        Evento evento = buscarEventoOuFalhar(id);

        if (dto.getNome() != null) {
            validarNome(dto.getNome());
            evento.setNome(dto.getNome());
        }

        if (dto.getData() != null) {
            evento.setData(dto.getData());
        }

        Evento atualizado = eventoRepository.save(evento);

        return ResponseEventoDTO.from(atualizado);
    }

    @Transactional
    public void deletar(UUID authUserId, UUID id) {
        exigirAdmin(authUserId);

        Evento evento = buscarEventoOuFalhar(id);

        eventoRepository.delete(evento);
    }

    @Transactional
    public ResponseEventoDTO adicionarParticipante(UUID authUserId, UUID eventoId, UUID userId) {
        exigirAdmin(authUserId);

        Evento evento = buscarEventoOuFalhar(eventoId);
        User user = buscarUserOuFalhar(userId);

        evento.adicionarParticipante(user);

        Evento atualizado = eventoRepository.save(evento);

        return ResponseEventoDTO.from(atualizado);
    }

    @Transactional
    public ResponseEventoDTO removerParticipante(UUID authUserId, UUID eventoId, UUID userId) {
        exigirAdmin(authUserId);

        Evento evento = buscarEventoOuFalhar(eventoId);
        User user = buscarUserOuFalhar(userId);

        evento.removerParticipante(user);

        Evento atualizado = eventoRepository.save(evento);

        return ResponseEventoDTO.from(atualizado);
    }

    @Transactional(readOnly = true)
    public List<ResponseUserDTO> listarParticipantes(UUID eventoId) {
        Evento evento = buscarEventoOuFalhar(eventoId);

        return evento.getParticipantes()
                .stream()
                .map(ResponseUserDTO::from)
                .toList();
    }

    private Evento buscarEventoOuFalhar(UUID id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado"));
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
            throw new IllegalArgumentException("O nome do evento é obrigatório");
        }
    }
}