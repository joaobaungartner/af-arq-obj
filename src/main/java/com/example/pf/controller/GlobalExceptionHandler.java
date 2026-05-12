package com.example.pf.controller;

import com.example.pf.exception.ForbiddenException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(EntityNotFoundException exception) {
        return buildError(
                HttpStatus.NOT_FOUND,
                "Recurso não encontrado",
                exception.getMessage(),
                null
        );
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiError> handleForbidden(ForbiddenException exception) {
        return buildError(
                HttpStatus.FORBIDDEN,
                "Acesso negado",
                exception.getMessage(),
                null
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleBadRequest(IllegalArgumentException exception) {
        return buildError(
                HttpStatus.BAD_REQUEST,
                "Requisição inválida",
                exception.getMessage(),
                null
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException exception) {
        Map<String, String> campos = new HashMap<>();

        for (FieldError erro : exception.getBindingResult().getFieldErrors()) {
            campos.put(erro.getField(), erro.getDefaultMessage());
        }

        return buildError(
                HttpStatus.BAD_REQUEST,
                "Erro de validação",
                "Existem campos inválidos na requisição",
                campos
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleJsonError(HttpMessageNotReadableException exception) {
        return buildError(
                HttpStatus.BAD_REQUEST,
                "JSON inválido",
                "Verifique o corpo da requisição",
                null
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(MethodArgumentTypeMismatchException exception) {
        return buildError(
                HttpStatus.BAD_REQUEST,
                "Parâmetro inválido",
                "Verifique se o ID enviado está no formato UUID",
                null
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrity(DataIntegrityViolationException exception) {
        return buildError(
                HttpStatus.BAD_REQUEST,
                "Erro de integridade",
                "Algum dado enviado viola uma regra do banco de dados",
                null
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception exception) {
        return buildError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro interno",
                "Ocorreu um erro inesperado no servidor",
                null
        );
    }

    private ResponseEntity<ApiError> buildError(
            HttpStatus status,
            String erro,
            String mensagem,
            Map<String, String> campos
    ) {
        ApiError apiError = new ApiError(
                status.value(),
                erro,
                mensagem,
                LocalDateTime.now(),
                campos
        );

        return ResponseEntity.status(status).body(apiError);
    }

    public record ApiError(
            int status,
            String erro,
            String mensagem,
            LocalDateTime timestamp,
            Map<String, String> campos
    ) {
    }
}