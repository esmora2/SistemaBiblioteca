package com.biblioteca.signup_service.controller;

import com.biblioteca.signup_service.dto.RegistroRequest;
import com.biblioteca.signup_service.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/signup")
@CrossOrigin(origins = "http://172.191.132.105:5173")
public class SignupController {

    private final UsuarioService usuarioService;

    public SignupController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> signup(@Valid @RequestBody RegistroRequest request) {
        usuarioService.registrarUsuario(request);
        return ResponseEntity.ok(Map.of("message", "Usuario registrado correctamente"));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }


}
