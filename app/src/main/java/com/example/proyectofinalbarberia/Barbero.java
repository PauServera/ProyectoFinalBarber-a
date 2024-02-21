package com.example.proyectofinalbarberia;

public class Barbero extends Usuario{
    private String nombre;
    private Barberia barberia;
    private boolean disponibilidad;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Barberia getBarberia() {
        return barberia;
    }

    public void setBarberia(Barberia barberia) {
        this.barberia = barberia;
    }

    public boolean isDisponible() {
        return disponibilidad;
    }

    public void setDisponible(boolean disponibilidad) {
        this.disponibilidad = disponibilidad;
    }
}
