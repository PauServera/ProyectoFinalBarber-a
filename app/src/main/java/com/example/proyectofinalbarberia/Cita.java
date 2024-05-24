package com.example.proyectofinalbarberia;

import java.time.LocalDateTime;
import java.util.Date;

public class Cita {
    private String id;
    private String idCliente;
    private String idBarberia;
    private String idBarbero;
    private Date fechaCita;

    public Cita() {
        // Constructor vacío necesario para Firestore
    }

    // Getters y setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getIdBarberia() {
        return idBarberia;
    }

    public void setIdBarberia(String idBarberia) {
        this.idBarberia = idBarberia;
    }

    public String getIdBarbero() {
        return idBarbero;
    }

    public void setIdBarbero(String idBarbero) {
        this.idBarbero = idBarbero;
    }

    public Date getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(Date fechaCita) {
        this.fechaCita = fechaCita;
    }
}
