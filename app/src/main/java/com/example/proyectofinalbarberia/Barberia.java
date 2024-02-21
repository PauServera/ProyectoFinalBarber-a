package com.example.proyectofinalbarberia;

import java.util.List;

public class Barberia {
    private int id;
    private String nombre;
    private Dueño dueñoBarberia;
    private List<Barbero> barberos;
    private List<Cliente> clientes;
    private List<Cita> citas;
    private String ubicacion;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Dueño getDueñoBarberia() {
        return dueñoBarberia;
    }

    public void setDueñoBarberia(Dueño dueñoBarberia) {
        this.dueñoBarberia = dueñoBarberia;
    }

    public List<Barbero> getBarberos() {
        return barberos;
    }

    public void setBarberos(List<Barbero> barberos) {
        this.barberos = barberos;
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
    }

    public List<Cita> getCitas() {
        return citas;
    }

    public void setCitas(List<Cita> citas) {
        this.citas = citas;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
}
