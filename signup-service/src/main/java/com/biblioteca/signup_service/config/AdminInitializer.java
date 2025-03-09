package com.biblioteca.signup_service.config;

import com.biblioteca.signup_service.entity.Usuario;
import com.biblioteca.signup_service.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;

    public AdminInitializer(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void run(String... args) {
        String emailAdmin = "admin@example.com";
        String passwordAdmin = "Admin123!!";

        if (!usuarioRepository.existsByEmail(emailAdmin)) {
            Usuario admin = new Usuario();
            admin.setEmail(emailAdmin);
            admin.setNombre("Administrador");
            admin.setPassword(hashSHA256(passwordAdmin)); // Encriptar con SHA-256
            admin.setRol("ADMIN"); // Asignar el rol de ADMIN

            usuarioRepository.save(admin);
            System.out.println("✅ Usuario ADMIN creado correctamente: " + emailAdmin);
        } else {
            System.out.println("⚠️ El usuario ADMIN ya existe en la base de datos.");
        }
    }

    // Método para encriptar con SHA-256 (igual que en UsuarioService)
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
