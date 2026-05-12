package com.example.pf.dto.save;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class SaveEventoDTO {

    @NotBlank(message = "O nome do evento é obrigatório")
    private String nome;

    @NotNull(message = "A data do evento é obrigatória")
    private LocalDate data;

    public SaveEventoDTO() {
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