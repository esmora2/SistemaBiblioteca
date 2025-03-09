package com.biblioteca.usuarios_service.repository;

import com.biblioteca.usuarios_service.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Buscar un usuario por su email
    Optional<Usuario> findByEmail(String email);

    // Verificar si un usuario existe por su email
    boolean existsByEmail(String email);
}