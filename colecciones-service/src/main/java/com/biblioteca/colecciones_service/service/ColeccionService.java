package com.biblioteca.colecciones_service.service;

import com.biblioteca.colecciones_service.dto.ColeccionRequest;
import com.biblioteca.colecciones_service.dto.ColeccionResponse;
import com.biblioteca.colecciones_service.entity.Coleccion;
import com.biblioteca.colecciones_service.exception.ResourceNotFoundException;
import com.biblioteca.colecciones_service.repository.ColeccionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ColeccionService {

    private final ColeccionRepository coleccionRepository;
    private static final String DEFAULT_IMAGE_URL = "uploads/default.png"; // ✅ Imagen por defecto

    public ColeccionService(ColeccionRepository coleccionRepository) {
        this.coleccionRepository = coleccionRepository;
    }

    // 🔹 Obtener todas las colecciones (públicas)
    public List<ColeccionResponse> getAll() {
        return coleccionRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // 🔹 Obtener una colección por ID
    public ColeccionResponse getById(Long id) {
        Coleccion coleccion = coleccionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Colección no encontrada con ID: " + id));
        return toResponse(coleccion);
    }

    // 🔹 Obtener colecciones privadas (solo usuarios autenticados)
    public List<ColeccionResponse> getAllPrivate() {
        return coleccionRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // 🔹 Crear una nueva colección con cantidadDisponible
    public ColeccionResponse create(ColeccionRequest request) {
        Coleccion coleccion = new Coleccion();
        coleccion.setTitulo(request.getTitulo());
        coleccion.setTipo(request.getTipo());
        coleccion.setAutor(request.getAutor());
        coleccion.setDescripcion(request.getDescripcion());
        coleccion.setCategoria(request.getCategoria());
        coleccion.setUrlRecurso(request.getUrlRecurso());

        // 📌 Validar cantidadDisponible
        if (request.getCantidadDisponible() < 0) {
            throw new IllegalArgumentException("La cantidad disponible no puede ser negativa.");
        }
        coleccion.setCantidadDisponible(request.getCantidadDisponible());

        // 📌 Asignar imagen predeterminada si no se proporciona
        String imagenPortada = (request.getImagenPortada() == null || request.getImagenPortada().isEmpty())
                ? DEFAULT_IMAGE_URL : request.getImagenPortada();
        coleccion.setImagenPortada(imagenPortada);

        coleccionRepository.save(coleccion);
        return toResponse(coleccion);
    }

    // 🔹 Actualizar una colección existente
    public void update(Long id, ColeccionRequest request) {
        Coleccion coleccion = coleccionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Colección no encontrada con ID: " + id));

        coleccion.setTitulo(request.getTitulo());
        coleccion.setTipo(request.getTipo());
        coleccion.setAutor(request.getAutor());
        coleccion.setDescripcion(request.getDescripcion());
        coleccion.setCategoria(request.getCategoria());
        coleccion.setUrlRecurso(request.getUrlRecurso());

        // 📌 Validar cantidadDisponible
        if (request.getCantidadDisponible() < 0) {
            throw new IllegalArgumentException("La cantidad disponible no puede ser negativa.");
        }
        coleccion.setCantidadDisponible(request.getCantidadDisponible());

        // 📌 Mantener la imagen anterior si no se proporciona una nueva
        String nuevaImagen = (request.getImagenPortada() == null || request.getImagenPortada().isEmpty())
                ? coleccion.getImagenPortada() : request.getImagenPortada();
        coleccion.setImagenPortada(nuevaImagen);

        coleccionRepository.save(coleccion);
    }

    // 🔹 Eliminar una colección
    public void delete(Long id) {
        Coleccion coleccion = coleccionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Colección no encontrada con ID: " + id));

        coleccionRepository.delete(coleccion);
    }

    // 🔹 Convertir entidad Coleccion a ColeccionResponse
    private ColeccionResponse toResponse(Coleccion coleccion) {
        return new ColeccionResponse(
                coleccion.getId(),
                coleccion.getTitulo(),
                coleccion.getTipo(),
                coleccion.getAutor(),
                coleccion.getCategoria(),
                coleccion.getDescripcion(),
                coleccion.getUrlRecurso(),
                coleccion.getImagenPortada(), // 📌 Siempre asegurando que tenga imagen
                coleccion.getCantidadDisponible() // 📌 Incluir cantidadDisponible en la respuesta
        );
    }

    // 🔹 Reducir stock de una colección
    public void reducirStock(Long id) {
        Coleccion coleccion = coleccionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Colección no encontrada con ID: " + id));

        if (coleccion.getCantidadDisponible() <= 0) {
            throw new IllegalArgumentException("No hay stock disponible para esta colección.");
        }

        coleccion.setCantidadDisponible(coleccion.getCantidadDisponible() - 1);
        coleccionRepository.save(coleccion);
    }

}