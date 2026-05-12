package com.example.pf.dto.response;

import com.example.pf.models.Papel;
import com.example.pf.models.User;

import java.util.UUID;

public class ResponseUserDTO {

    private UUID id;
    private String nome;
    private String cpf;
    private Papel papel;

    public ResponseUserDTO() {
    }

    public ResponseUserDTO(UUID id, String nome, String cpf, Papel papel) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.papel = papel;
    }

    public static ResponseUserDTO from(User user) {
        return new ResponseUserDTO(
                user.getId(),
                user.getNome(),
                user.getCpf(),
                user.getPapel()
        );
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public Papel getPapel() {
        return papel;
    }
}