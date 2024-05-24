package com.example.proyectofinalbarberia;

import java.util.ArrayList;
import java.util.List;

public class Barbero {
    private String id;
    private String nombre;
    private String email;
    private List<String> listaCitas;

    public Barbero() {
        // Constructor vacío necesario para Firebase
    }

    public Barbero(String uid, String nombre, String email, List<String> listaCitas) {
        this.id = uid;
        this.nombre = nombre;
        this.email = email;
        this.listaCitas = listaCitas != null ? listaCitas : new ArrayList<>();
    }

    // Getters y setters para todos los campos...
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getListaCitas() {
        return listaCitas;
    }

    public void setListaCitas(List<String> listaCitas) {
        this.listaCitas = listaCitas;
    }
}
