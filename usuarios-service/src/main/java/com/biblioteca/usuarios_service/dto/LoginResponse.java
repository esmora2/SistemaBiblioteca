package com.biblioteca.usuarios_service.dto;

public class LoginResponse {

    private String accessToken;
    private String idToken;
    private String rol; // ✅ Nuevo campo para el rol del usuario

    public LoginResponse() {}

    public LoginResponse(String accessToken, String idToken, String rol) {
        this.accessToken = accessToken;
        this.idToken = idToken;
        this.rol = rol;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}