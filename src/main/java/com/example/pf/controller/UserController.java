package com.example.pf.controller;

import com.example.pf.dto.edit.EditUserDTO;
import com.example.pf.dto.response.ResponseEventoDTO;
import com.example.pf.dto.response.ResponseUserDTO;
import com.example.pf.dto.save.SaveUserDTO;
import com.example.pf.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping({"/users", "/usuarios"})
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<ResponseUserDTO> listar() {
        return userService.listar();
    }

    @GetMapping("/{id}")
    public ResponseUserDTO buscarPorId(@PathVariable UUID id) {
        return userService.buscarPorId(id);
    }

    @GetMapping("/{id}/eventos")
    public List<ResponseEventoDTO> listarEventosDoUsuario(@PathVariable UUID id) {
        return userService.listarEventosDoUsuario(id);
    }

    @PostMapping
    public ResponseEntity<ResponseUserDTO> criar(
            @RequestHeader(value = "X-USER-ID", required = false) UUID authUserId,
            @RequestBody @Valid SaveUserDTO dto
    ) {
        ResponseUserDTO response = userService.criar(authUserId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseUserDTO atualizar(
            @RequestHeader(value = "X-USER-ID", required = false) UUID authUserId,
            @PathVariable UUID id,
            @RequestBody @Valid EditUserDTO dto
    ) {
        return userService.atualizar(authUserId, id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @RequestHeader(value = "X-USER-ID", required = false) UUID authUserId,
            @PathVariable UUID id
    ) {
        userService.deletar(authUserId, id);
        return ResponseEntity.noContent().build();
    }
}