package com.biblioteca.colecciones_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColeccionRequest {

    @NotBlank(message = "El título es obligatorio")
    private String titulo;

    @NotBlank(message = "El tipo es obligatorio")
    private String tipo; // "LIBRO", "REVISTA", "RECURSO_ELECTRONICO"

    private String autor;
    private String descripcion;

    @NotBlank(message = "La categoría es obligatoria")
    private String categoria;

    private String urlRecurso;
    private String imagenPortada;

    @Min(value = 0, message = "La cantidad disponible no puede ser negativa")
    private int cantidadDisponible; // 🔹 Nuevo campo para definir stock
}
