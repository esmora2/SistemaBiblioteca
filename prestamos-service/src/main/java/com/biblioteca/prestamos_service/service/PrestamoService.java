package com.biblioteca.prestamos_service.service;

import com.biblioteca.prestamos_service.dto.PrestamoRequest;
import com.biblioteca.prestamos_service.dto.PrestamoResponse;
import com.biblioteca.prestamos_service.entity.Prestamo;
import com.biblioteca.prestamos_service.exception.ResourceNotFoundException;
import com.biblioteca.prestamos_service.repository.PrestamoRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.biblioteca.prestamos_service.dto.ColeccionResponse;


@Service
public class PrestamoService {

    private final PrestamoRepository prestamoRepository;
    private final RestTemplate restTemplate;

    public PrestamoService(PrestamoRepository prestamoRepository, RestTemplate restTemplate) {
        this.prestamoRepository = prestamoRepository;
        this.restTemplate = restTemplate;
    }

    // 🔹 Crear un nuevo préstamo y reducir el stock en colecciones-service
    public PrestamoResponse crearPrestamo(PrestamoRequest request, String token) {
        try {
            System.out.println("📌 Recibido usuarioId (antes de guardar): " + request.getUsuarioId());

            if (request.getUsuarioId() == null || request.getColeccionId() == null) {
                throw new IllegalArgumentException("Usuario ID y Colección ID son obligatorios");
            }

            // ✅ Guardamos el usuarioId tal como lo envía el frontend (sin modificarlo)
            Prestamo prestamo = new Prestamo();
            prestamo.setUsuarioId(request.getUsuarioId().trim()); // 🔹 Asegurar que no haya espacios
            prestamo.setColeccionId(request.getColeccionId());
            prestamo.setFechaPrestamo(LocalDate.now());
            prestamo.setDevuelto(false);

            // 🔹 Guardar en la base de datos
            prestamoRepository.save(prestamo);

            // 🔹 Reducir stock en colecciones-service
            reducirStock(request.getColeccionId(), token);

            System.out.println("✅ Préstamo guardado con usuarioId: " + prestamo.getUsuarioId());

            return toResponse(prestamo, token);
        } catch (Exception e) {
            System.err.println("❌ Error en crearPrestamo: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }


    // 🔹 Llamada a colecciones-service para reducir stock con autenticación
    private void reducirStock(Long coleccionId, String token) {
        String url = "http://localhost:9092/colecciones/admin/reducir-stock/" + coleccionId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token); // ✅ Agregar token en la cabecera
        headers.set("Content-Type", "application/json");

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);

        System.out.println("📌 Respuesta reducirStock: " + response.getStatusCode());
    }

    // 🔹 Obtener el historial de préstamos de un usuario con el formato correcto
    public List<PrestamoResponse> obtenerPrestamosPorUsuario(String usuarioId, String token) { // ✅ Agregar el token
        System.out.println("📌 Buscando préstamos para usuarioId: " + usuarioId);

        List<Prestamo> prestamos = prestamoRepository.findByUsuarioId(usuarioId);

        if (prestamos.isEmpty()) {
            System.out.println("❌ No se encontraron préstamos para usuarioId: " + usuarioId);
        } else {
            System.out.println("✅ Préstamos encontrados para usuarioId: " + usuarioId);
        }

        return prestamos.stream()
                .map(prestamo -> toResponse(prestamo, token)) // ✅ Pasar el token a `toResponse()`
                .collect(Collectors.toList());
    }




    // 🔹 Obtener todos los préstamos (solo para administradores)
    public List<PrestamoResponse> obtenerTodosLosPrestamos(String token) { // ✅ Agregar el token
        return prestamoRepository.findAll()
                .stream()
                .map(prestamo -> toResponse(prestamo, token)) // ✅ Pasar el token
                .collect(Collectors.toList());
    }


    // 🔹 Renovar un préstamo (simulando una extensión del mismo en 7 días)
    public void renovarPrestamo(Long id) {
        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Préstamo no encontrado con ID: " + id));

        if (prestamo.isDevuelto()) {
            throw new IllegalArgumentException("El préstamo ya ha sido devuelto y no puede ser renovado.");
        }

        // 🔄 Extender la fecha de devolución en 7 días
        LocalDate nuevaFechaDevolucion = (prestamo.getFechaDevolucion() != null)
                ? prestamo.getFechaDevolucion().plusDays(7) // Si ya hay fecha, agregar 7 días más
                : LocalDate.now().plusDays(7); // Si no tiene fecha, asignar una nueva en 7 días

        prestamo.setFechaDevolucion(nuevaFechaDevolucion);
        prestamoRepository.save(prestamo);

        System.out.println("✅ Préstamo renovado. Nueva fecha de devolución: " + nuevaFechaDevolucion);
    }



    // 🔹 Marcar un préstamo como devuelto
    public void devolverPrestamo(Long id) {
        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Préstamo no encontrado con ID: " + id));

        if (prestamo.isDevuelto()) {
            throw new IllegalArgumentException("El préstamo ya ha sido devuelto.");
        }

        prestamo.setFechaDevolucion(LocalDate.now());
        prestamo.setDevuelto(true);
        prestamoRepository.save(prestamo);
    }

    private String obtenerTituloColeccion(Long coleccionId, String token) {
        try {
            String url = "http://localhost:9092/colecciones/user/" + coleccionId;

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token); // ✅ Agregar token en la cabecera
            headers.set("Content-Type", "application/json");

            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<ColeccionResponse> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, ColeccionResponse.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody().getTitulo();
            } else {
                System.out.println("❌ No se encontró el título para coleccionId: " + coleccionId);
                return "Título desconocido";
            }
        } catch (Exception e) {
            System.err.println("❌ Error al obtener título de coleccionId: " + coleccionId + " - " + e.getMessage());
            return "Título desconocido";
        }
    }

    // 🔹 Marcar un préstamo como devuelto (solo ADMIN)
    public void marcarComoDevuelto(Long id) {
        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Préstamo no encontrado con ID: " + id));

        if (prestamo.isDevuelto()) {
            throw new IllegalArgumentException("El préstamo ya ha sido devuelto.");
        }

        prestamo.setFechaDevolucion(LocalDate.now());
        prestamo.setDevuelto(true);
        prestamoRepository.save(prestamo);

        System.out.println("✅ Préstamo con ID " + id + " marcado como devuelto.");
    }




    // 🔹 Convertir entidad Prestamo a PrestamoResponse
    private PrestamoResponse toResponse(Prestamo prestamo, String token) {
        PrestamoResponse response = new PrestamoResponse();
        response.setId(prestamo.getId());
        response.setUsuarioId(prestamo.getUsuarioId());
        response.setColeccionId(prestamo.getColeccionId());
        response.setFechaPrestamo(prestamo.getFechaPrestamo());
        response.setFechaDevolucion(prestamo.getFechaDevolucion());
        response.setDevuelto(prestamo.isDevuelto());

        // 🔹 Obtener título de la colección desde `colecciones-service` con autenticación
        response.setTituloColeccion(obtenerTituloColeccion(prestamo.getColeccionId(), token));

        response.setNombreUsuario(null);

        return response;
    }
}