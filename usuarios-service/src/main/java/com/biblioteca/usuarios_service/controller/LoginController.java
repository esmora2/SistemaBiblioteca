package com.biblioteca.usuarios_service.controller;

import com.biblioteca.usuarios_service.dto.LoginRequest;
import com.biblioteca.usuarios_service.dto.LoginResponse;
import com.biblioteca.usuarios_service.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public/auth") // Ruta accesible sin autenticación
@CrossOrigin(origins = "http://localhost:5173")
public class LoginController {

    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request.getEmail(), request.getPassword()); // ✅ Pasar email y password correctamente
        return ResponseEntity.ok(response);
    }
}