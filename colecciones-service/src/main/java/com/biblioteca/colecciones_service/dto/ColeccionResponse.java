package com.biblioteca.colecciones_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ColeccionResponse {
    private Long id;
    private String titulo; // ✅ Asegurar que este campo esté en la respuesta
    private String tipo;
    private String autor;
    private String categoria;
    private String descripcion;
    private String urlRecurso;
    private String imagenPortada;
    private int cantidadDisponible;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}

