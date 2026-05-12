package com.example.pf.dto.edit;

import java.time.LocalDate;

public class EditEventoDTO {

    private String nome;

    private LocalDate data;

    public EditEventoDTO() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }
}