package com.biblioteca.prestamos_service.dto;

public class ColeccionResponse {
    private Long id;
    private String titulo; // ✅ Este campo es necesario para obtener el título del libro

    // 🔹 Constructor vacío
    public ColeccionResponse() {}

    // 🔹 Constructor con parámetros
    public ColeccionResponse(Long id, String titulo) {
        this.id = id;
        this.titulo = titulo;
    }

    // 🔹 Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
