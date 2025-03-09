package com.biblioteca.prestamos_service.repository;

import com.biblioteca.prestamos_service.entity.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    // 🔹 Cambiado de Long a String para usuarioId
    List<Prestamo> findByUsuarioId(String usuarioId);

    // 🔹 Obtener todos los préstamos que aún no han sido devueltos
    List<Prestamo> findByDevueltoFalse();
}