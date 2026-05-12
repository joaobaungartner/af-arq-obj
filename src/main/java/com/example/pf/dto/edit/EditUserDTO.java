package com.example.pf.dto.edit;

import com.example.pf.models.Papel;

public class EditUserDTO {

    private String nome;

    private String cpf;

    private Papel papel;

    public EditUserDTO() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Papel getPapel() {
        return papel;
    }

    public void setPapel(Papel papel) {
        this.papel = papel;
    }
}