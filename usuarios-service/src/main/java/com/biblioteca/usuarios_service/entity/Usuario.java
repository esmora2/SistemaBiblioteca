package com.biblioteca.usuarios_service.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios") // 📌 Asegura que usa la misma tabla creada en signup-service
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String password;  // 📌 La contraseña ya está almacenada en la base de datos

    @Column(nullable = false)
    private String rol;  // 📌 Puede ser "USER" o "ADMIN"

    public Usuario() {}

    public Usuario(String email, String nombre, String password, String rol) {
        this.email = email;
        this.nombre = nombre;
        this.password = password;
        this.rol = rol;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}