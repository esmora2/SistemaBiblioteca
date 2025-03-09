package com.biblioteca.signup_service.service;

import com.biblioteca.signup_service.dto.RegistroRequest;
import com.biblioteca.signup_service.entity.Usuario;
import com.biblioteca.signup_service.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final Auth0Service auth0Service;

    public UsuarioService(UsuarioRepository usuarioRepository, Auth0Service auth0Service) {
        this.usuarioRepository = usuarioRepository;
        this.auth0Service = auth0Service;
    }

    public void registrarUsuario(RegistroRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        // Encriptar la contraseña
        String passwordEncriptada = hashSHA256(request.getPassword());

        // Crear usuario en Auth0
        auth0Service.crearUsuarioEnAuth0(request.getEmail(), request.getPassword());

        // Guardar usuario en base local con la contraseña encriptada
        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncriptada);
        usuarioRepository.save(usuario);
    }

    // Método para encriptar con SHA-256
    private String hashSHA256(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            // Convertir bytes a cadena hexadecimal
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0'); // Asegura que sean 2 caracteres
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al encriptar la contraseña", e);
        }
    }
}
