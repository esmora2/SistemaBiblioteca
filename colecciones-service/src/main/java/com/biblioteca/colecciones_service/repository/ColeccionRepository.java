package com.biblioteca.colecciones_service.repository;

import com.biblioteca.colecciones_service.entity.Coleccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColeccionRepository extends JpaRepository<Coleccion, Long> {
}
