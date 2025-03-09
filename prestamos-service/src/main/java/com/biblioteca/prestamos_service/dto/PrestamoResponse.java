package com.biblioteca.prestamos_service.dto;

import java.time.LocalDate;

public class PrestamoResponse {

    private Long id;
    private String usuarioId; // 🔹 Cambiado de Long a String
    private Long coleccionId;
    private String nombreUsuario;
    private String tituloColeccion;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucion;
    private boolean devuelto;

    public PrestamoResponse() {}

    public PrestamoResponse(Long id, String usuarioId, Long coleccionId, String nombreUsuario,
                            String tituloColeccion, LocalDate fechaPrestamo, LocalDate fechaDevolucion, boolean devuelto) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.coleccionId = coleccionId;
        this.nombreUsuario = nombreUsuario;
        this.tituloColeccion = tituloColeccion;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
        this.devuelto = devuelto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsuarioId() { // 🔹 Cambiado a String
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) { // 🔹 Cambiado a String
        this.usuarioId = usuarioId;
    }

    public Long getColeccionId() {
        return coleccionId;
    }

    public void setColeccionId(Long coleccionId) {
        this.coleccionId = coleccionId;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getTituloColeccion() {
        return tituloColeccion;
    }

    public void setTituloColeccion(String tituloColeccion) {
        this.tituloColeccion = tituloColeccion;
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
