package com.biblioteca.colecciones_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "colecciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Coleccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String tipo; // "LIBRO", "REVISTA", "RECURSO_ELECTRONICO"

    private String autor;
    private String descripcion;

    @Column(nullable = false)
    private String categoria;

    private String urlRecurso; // Enlace si es un recurso electrónico
    private String imagenPortada; // 🔹 Imagen de la colección

    @Column(nullable = false)
    private int cantidadDisponible; // 🔹 Nuevo campo para el stock disponible
}
