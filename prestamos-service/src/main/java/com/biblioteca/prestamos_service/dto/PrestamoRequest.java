package com.biblioteca.prestamos_service.dto;

import jakarta.validation.constraints.NotNull;

public class PrestamoRequest {

    @NotNull(message = "El ID del usuario es obligatorio")
    private String usuarioId; // 🔹 Cambiado de Long a String

    @NotNull(message = "El ID de la colección es obligatorio")
    private Long coleccionId;

    public PrestamoRequest() {}

    public PrestamoRequest(String usuarioId, Long coleccionId) { // 🔹 Cambiar el tipo
        this.usuarioId = usuarioId;
        this.coleccionId = coleccionId;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getColeccionId() {
        return coleccionId;
    }

    public void setColeccionId(Long coleccionId) {
        this.coleccionId = coleccionId;
    }
}