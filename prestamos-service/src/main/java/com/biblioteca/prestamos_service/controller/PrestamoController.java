package com.biblioteca.prestamos_service.controller;

import com.biblioteca.prestamos_service.dto.PrestamoRequest;
import com.biblioteca.prestamos_service.dto.PrestamoResponse;
import com.biblioteca.prestamos_service.service.PrestamoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/prestamos")
@CrossOrigin(origins = "http://172.191.132.105:5173") // Permitir acceso desde el frontend
public class PrestamoController {

    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    // 🔹 Crear un préstamo (Solo usuarios autenticados)
    @PostMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PrestamoResponse> crearPrestamo(
            @RequestBody PrestamoRequest request,
            @RequestHeader("Authorization") String token) {  // ✅ Recibir token del frontend
        return ResponseEntity.ok(prestamoService.crearPrestamo(request, token));
    }

    // 🔹 Obtener historial de préstamos de un usuario autenticado
    @GetMapping("/user/{usuarioId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<PrestamoResponse>> obtenerPrestamosUsuario(
            @PathVariable String usuarioId,
            @RequestHeader("Authorization") String token) { // ✅ Recibir token

        System.out.println("📌 Recibida petición para usuarioId: " + usuarioId);
        return ResponseEntity.ok(prestamoService.obtenerPrestamosPorUsuario(usuarioId, token)); // ✅ Pasar el token
    }




    // 🔹 Obtener historial de todos los préstamos (Solo ADMIN)
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PrestamoResponse>> obtenerTodosLosPrestamos(
            @RequestHeader("Authorization") String token) { // ✅ Recibir el token

        return ResponseEntity.ok(prestamoService.obtenerTodosLosPrestamos(token)); // ✅ Pasar el token
    }


    // 🔹 Renovar un préstamo (Solo usuarios autenticados)
    @PutMapping("/user/renovar/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, String>> renovarPrestamo(@PathVariable Long id) {
        prestamoService.renovarPrestamo(id);
        return ResponseEntity.ok(Map.of("message", "Préstamo renovado correctamente"));
    }

    // 🔹 Devolver un préstamo (Solo usuarios autenticados)
    @PutMapping("/user/devolver/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, String>> devolverPrestamo(@PathVariable Long id) {
        prestamoService.devolverPrestamo(id);
        return ResponseEntity.ok(Map.of("message", "Préstamo devuelto correctamente"));
    }

    // 🔹 Marcar un préstamo como devuelto (Solo ADMIN)
    @PutMapping("/admin/devolver/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> marcarComoDevuelto(@PathVariable Long id) {
        prestamoService.marcarComoDevuelto(id);
        return ResponseEntity.ok(Map.of("message", "Préstamo marcado como devuelto"));
    }

}