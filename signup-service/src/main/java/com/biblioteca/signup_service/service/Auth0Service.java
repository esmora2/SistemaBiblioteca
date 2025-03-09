package com.biblioteca.signup_service.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;
import java.util.Map;

@Service
public class Auth0Service {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${auth0.domain}")
    private String domain;

    @Value("${auth0.client-id}")
    private String clientId;

    @Value("${auth0.client-secret}")
    private String clientSecret;

    @Value("${auth0.management-audience}")
    private String managementAudience;

    public String obtenerManagementToken() {
        String url = "https://" + domain + "/oauth/token";

        Map<String, String> body = Map.of(
                "client_id", clientId,
                "client_secret", clientSecret,
                "audience", managementAudience,
                "grant_type", "client_credentials"
        );

        ResponseEntity<Map> response = restTemplate.postForEntity(url, body, Map.class);
        return (String) response.getBody().get("access_token");
    }

    public void crearUsuarioEnAuth0(String email, String password) {
        try {
            String token = obtenerManagementToken();
            String url = "https://" + domain + "/api/v2/users"; // ✅ Asegurar que la URL sea correcta

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> body = Map.of(
                    "email", email,
                    "password", password,
                    "connection", "Username-Password-Authentication",
                    "app_metadata", Map.of("roles", List.of("USER"))
            );

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            restTemplate.postForEntity(url, request, Void.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            manejarErrorAuth0(e); // ✅ Ahora sí puede aceptar ambas excepciones
        } catch (Exception e) {
            throw new RuntimeException("Error desconocido al comunicarse con Auth0", e);
        }
    }

    // Método para capturar y traducir errores de Auth0
    private void manejarErrorAuth0(Exception e) {
        try {
            String responseBody = ((HttpClientErrorException) e).getResponseBodyAsString();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);

            String errorMensaje = jsonNode.has("message") ? jsonNode.get("message").asText() : "Error desconocido en Auth0";

            // Traducir errores comunes
            if (errorMensaje.contains("PasswordStrengthError")) {
                throw new IllegalArgumentException("Auth0 dice: La contraseña es demasiado débil. Debe contener al menos 8 caracteres, una mayúscula, un número y un símbolo.");
            } else if (errorMensaje.contains("The user already exists")) {
                throw new IllegalArgumentException("El usuario ya está registrado.");
            }

            throw new IllegalArgumentException("Error en Auth0: " + errorMensaje);
        } catch (Exception ex) {
            throw new RuntimeException("Error al procesar la respuesta de Auth0", ex);
        }
    }
}
