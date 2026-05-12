package com.example.pf.dto.response;

import com.example.pf.models.Evento;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class ResponseEventoDTO {

    private UUID id;
    private String nome;
    private LocalDate data;
    private List<ResponseUserDTO> participantes;

    public ResponseEventoDTO() {
    }

    public ResponseEventoDTO(UUID id, String nome, LocalDate data, List<ResponseUserDTO> participantes) {
        this.id = id;
        this.nome = nome;
        this.data = data;
        this.participantes = participantes;
    }

    public static ResponseEventoDTO from(Evento evento) {
        List<ResponseUserDTO> participantes = evento.getParticipantes()
                .stream()
                .map(ResponseUserDTO::from)
                .toList();

        return new ResponseEventoDTO(
                evento.getId(),
                evento.getNome(),
                evento.getData(),
                participantes
        );
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public LocalDate getData() {
        return data;
    }

    public List<ResponseUserDTO> getParticipantes() {
        return participantes;
    }
}