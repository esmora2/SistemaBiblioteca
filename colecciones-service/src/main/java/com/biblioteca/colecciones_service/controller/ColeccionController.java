package com.biblioteca.colecciones_service.controller;

import com.biblioteca.colecciones_service.dto.ColeccionRequest;
import com.biblioteca.colecciones_service.dto.ColeccionResponse;
import com.biblioteca.colecciones_service.service.ColeccionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/colecciones")
@CrossOrigin(origins = "http://localhost:5173") // Permitir peticiones desde el frontend
public class ColeccionController {

    private final ColeccionService coleccionService;

    public ColeccionController(ColeccionService coleccionService) {
        this.coleccionService = coleccionService;
    }

    // 🔹 Obtener todas las colecciones (Acceso público)
    @GetMapping("/public")
    public ResponseEntity<List<ColeccionResponse>> getAllColecciones() {
        return ResponseEntity.ok(coleccionService.getAll());
    }

    // 🔹 Obtener una colección por ID (Solo usuarios autenticados)
    @GetMapping("/user/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')") // ✅ Permitir acceso autenticado
    public ResponseEntity<ColeccionResponse> getColeccionById(@PathVariable Long id) {
        return ResponseEntity.ok(coleccionService.getById(id));
    }


    // 🔹 Obtener colecciones privadas (Solo usuarios autenticados)
    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<ColeccionResponse>> getPrivateColecciones() {
        return ResponseEntity.ok(coleccionService.getAllPrivate());
    }

    // 🔹 Crear una nueva colección (Solo ADMIN)
    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ColeccionResponse> createColeccion(@RequestBody ColeccionRequest request) {
        // 📌 Si no se proporciona imagen, asignar una imagen por defecto
        if (request.getImagenPortada() == null || request.getImagenPortada().isEmpty()) {
            request.setImagenPortada("uploads/default.png");
        }

        ColeccionResponse response = coleccionService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 🔹 Actualizar una colección (Solo ADMIN)
    @PutMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> updateColeccion(@PathVariable Long id, @RequestBody ColeccionRequest request) {
        // 📌 Mantener la imagen anterior si no se proporciona una nueva
        ColeccionResponse existingColeccion = coleccionService.getById(id);
        if (request.getImagenPortada() == null || request.getImagenPortada().isEmpty()) {
            request.setImagenPortada(existingColeccion.getImagenPortada());
        }

        coleccionService.update(id, request);
        return ResponseEntity.ok(Map.of("message", "Colección actualizada correctamente"));
    }

    // 🔹 Eliminar una colección (Solo ADMIN)
    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> deleteColeccion(@PathVariable Long id) {
        coleccionService.delete(id);
        return ResponseEntity.ok(Map.of("message", "Colección eliminada correctamente"));
    }

    // 🔹 Reducir stock de una colección (llamado desde prestamos-service)
    @PutMapping("/admin/reducir-stock/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')") // 🔹 Ahora permite USER y ADMIN
    public ResponseEntity<Map<String, String>> reducirStock(@PathVariable Long id) {
        coleccionService.reducirStock(id);
        return ResponseEntity.ok(Map.of("message", "Stock reducido correctamente"));
    }


}