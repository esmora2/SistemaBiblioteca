package com.biblioteca.usuarios_service.controller;

import com.biblioteca.usuarios_service.entity.Usuario;
import com.biblioteca.usuarios_service.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.beans.factory.annotation.Value;

@RestController
@RequestMapping("/admin/usuarios") // Solo ADMIN puede acceder
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final JwtDecoder jwtDecoder;

    public UsuarioController(UsuarioService usuarioService, @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String issuerUri) {
        this.usuarioService = usuarioService;
        this.jwtDecoder = JwtDecoders.fromIssuerLocation(issuerUri);
    }


    private String extraerEmailDesdeToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Token inválido o no proporcionado.");
        }

        try {
            String token = authHeader.substring(7); // Eliminar "Bearer "
            Jwt jwt = jwtDecoder.decode(token);

            // 🔹 Verifica qué claim contiene el email (puede variar)
            if (jwt.getClaim("https://biblioteca.com/email") != null) {
                return jwt.getClaim("https://biblioteca.com/email"); // Ajusta según la estructura del token
            } else if (jwt.getClaim("email") != null) {
                return jwt.getClaim("email");
            } else {
                throw new RuntimeException("No se encontró el email en el token.");
            }

        } catch (JwtException e) {
            throw new RuntimeException("Error al decodificar el token.", e);
        }
    }

    // 🔹 Obtener todos los usuarios (Solo ADMIN)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Usuario>> getAllUsuarios() {
        return ResponseEntity.ok(usuarioService.getAllUsuarios());
    }

    // 🔹 Obtener usuario por ID (Solo ADMIN)
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.getUsuarioById(id));
    }

    // 🔹 Crear usuario (Solo ADMIN)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> createUsuario(@RequestBody Usuario usuario) {
        usuarioService.createUsuario(usuario);
        return ResponseEntity.ok(Map.of("message", "Usuario creado correctamente"));
    }

    // 🔹 Actualizar usuario (Solo ADMIN)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> updateUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        usuarioService.updateUsuario(id, usuario);
        return ResponseEntity.ok(Map.of("message", "Usuario actualizado correctamente"));
    }

    // 🔹 Eliminar usuario (Solo ADMIN)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> deleteUsuario(@PathVariable Long id) {
        usuarioService.deleteUsuario(id);
        return ResponseEntity.ok(Map.of("message", "Usuario eliminado correctamente"));
    }

    // 🔹 Nuevo endpoint público para obtener un usuario por ID
    // ✅ Ruta pública correcta
    @GetMapping("/public/{id}") // Esto hará que la ruta sea "/admin/usuarios/public/{id}"
    public ResponseEntity<Usuario> getUsuarioByIdPublic(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.getUsuarioById(id));
    }

    // 🔹 Permite que el usuario autenticado obtenga su información
    @GetMapping("/usuarios/me")
    @PreAuthorize("isAuthenticated()") // ✅ Cualquier usuario autenticado puede acceder
    public ResponseEntity<Usuario> getUsuarioActual(@RequestHeader("Authorization") String authHeader) {
        // Extraer el email del token (Auth0 lo proporciona en el claim)
        String emailUsuario = extraerEmailDesdeToken(authHeader);

        // Buscar usuario en la base de datos
        Usuario usuario = usuarioService.getUsuarioByEmail(emailUsuario);
        return ResponseEntity.ok(usuario);
    }



}