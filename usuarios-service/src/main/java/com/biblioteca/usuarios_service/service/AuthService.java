package com.biblioteca.usuarios_service.service;

import com.biblioteca.usuarios_service.dto.LoginResponse;
import com.biblioteca.usuarios_service.entity.Usuario;
import com.biblioteca.usuarios_service.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class AuthService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final UsuarioRepository usuarioRepository;

    @Value("${auth0.domain}")
    private String domain;

    @Value("${auth0.client-id}")
    private String clientId;

    @Value("${auth0.client-secret}")
    private String clientSecret;

    @Value("${auth0.audience}")
    private String audience;

    public AuthService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public LoginResponse login(String email, String password) {
        String url = "https://" + domain + "/oauth/token";

        Map<String, String> body = Map.of(
                "client_id", clientId,
                "client_secret", clientSecret,
                "audience", audience,
                "grant_type", "password",
                "username", email,
                "password", password,
                "scope", "openid profile email",
                "realm", "Username-Password-Authentication"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, requestEntity, Map.class);

        // ✅ Verificar qué datos devuelve Auth0
        System.out.println("Respuesta de Auth0: " + response.getBody());

        String accessToken = (String) response.getBody().get("access_token");
        String idToken = (String) response.getBody().get("id_token");

        // ✅ Buscar el usuario en la base de datos y obtener su rol
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado en la base de datos"));

        String userRole = usuario.getRol(); // 📌 Obtener el rol de la base de datos

        return new LoginResponse(accessToken, idToken, userRole);
    }
}
