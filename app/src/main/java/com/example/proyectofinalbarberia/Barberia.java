package com.example.proyectofinalbarberia;

import java.util.List;

import java.util.List;

public class Barberia {
    private String id;
    private String nombre;
    private String ubicacion;
    private String telefono;
    private String imageUrl;

    public Barberia() {
    }

    public Barberia(String id, String nombre, String ubicacion, String telefono, String imageUrl) {
        this.id = id;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.telefono = telefono;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

