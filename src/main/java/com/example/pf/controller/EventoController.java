package com.example.pf.controller;

import com.example.pf.dto.edit.EditEventoDTO;
import com.example.pf.dto.response.ResponseEventoDTO;
import com.example.pf.dto.response.ResponseUserDTO;
import com.example.pf.dto.save.SaveEventoDTO;
import com.example.pf.service.EventoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping({"/eventos", "/events"})
public class EventoController {

    private final EventoService eventoService;

    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @GetMapping
    public List<ResponseEventoDTO> listar() {
        return eventoService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEventoDTO buscarPorId(@PathVariable UUID id) {
        return eventoService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<ResponseEventoDTO> criar(
            @RequestHeader(value = "X-USER-ID", required = false) UUID authUserId,
            @RequestBody @Valid SaveEventoDTO dto
    ) {
        ResponseEventoDTO response = eventoService.criar(authUserId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEventoDTO atualizar(
            @RequestHeader(value = "X-USER-ID", required = false) UUID authUserId,
            @PathVariable UUID id,
            @RequestBody @Valid EditEventoDTO dto
    ) {
        return eventoService.atualizar(authUserId, id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @RequestHeader(value = "X-USER-ID", required = false) UUID authUserId,
            @PathVariable UUID id
    ) {
        eventoService.deletar(authUserId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{eventoId}/participantes")
    public List<ResponseUserDTO> listarParticipantes(@PathVariable UUID eventoId) {
        return eventoService.listarParticipantes(eventoId);
    }

    @PostMapping("/{eventoId}/participantes/{userId}")
    public ResponseEventoDTO adicionarParticipante(
            @RequestHeader(value = "X-USER-ID", required = false) UUID authUserId,
            @PathVariable UUID eventoId,
            @PathVariable UUID userId
    ) {
        return eventoService.adicionarParticipante(authUserId, eventoId, userId);
    }

    @DeleteMapping("/{eventoId}/participantes/{userId}")
    public ResponseEventoDTO removerParticipante(
            @RequestHeader(value = "X-USER-ID", required = false) UUID authUserId,
            @PathVariable UUID eventoId,
            @PathVariable UUID userId
    ) {
        return eventoService.removerParticipante(authUserId, eventoId, userId);
    }
}