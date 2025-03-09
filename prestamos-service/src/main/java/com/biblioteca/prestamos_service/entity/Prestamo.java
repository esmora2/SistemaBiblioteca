package com.biblioteca.prestamos_service.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "prestamos")
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String usuarioId; // 🔹 Cambiado de Long a String

    @Column(nullable = false)
    private Long coleccionId;

    @Column(nullable = false)
    private LocalDate fechaPrestamo;

    private LocalDate fechaDevolucion;

    @Column(nullable = false)
    private boolean devuelto = false;

    public Prestamo() {}

    public Prestamo(String usuarioId, Long coleccionId, LocalDate fechaPrestamo) {
        this.usuarioId = usuarioId;
        this.coleccionId = coleccionId;
        this.fechaPrestamo = fechaPrestamo;
        this.devuelto = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsuarioId() { // 🔹 Devuelve un String
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) { // 🔹 Guarda como String
        this.usuarioId = usuarioId;
    }

    public Long getColeccionId() {
        return coleccionId;
    }

    public void setColeccionId(Long coleccionId) {
        this.coleccionId = coleccionId;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(LocalDate fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public boolean isDevuelto() {
        return devuelto;
    }

    public void setDevuelto(boolean devuelto) {
        this.devuelto = devuelto;
    }
}