package com.example.proyectofinalbarberia;

import java.time.LocalDateTime;
import java.util.Date;

public class Cita {
    private int idCita;
    private Cliente idCliente;
    private Barbero idBarbero;
    private LocalDateTime fechaCita;
    private String infoCita;

    public int getIdCita() {
        return idCita;
    }

    public void setIdCita(int idCita) {
        this.idCita = idCita;
    }

    public Cliente getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Cliente idCliente) {
        this.idCliente = idCliente;
    }

    public Barbero getIdBarbero() {
        return idBarbero;
    }

    public void setIdBarbero(Barbero idBarbero) {
        this.idBarbero = idBarbero;
    }

    public LocalDateTime getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(LocalDateTime fechaCita) {
        this.fechaCita = fechaCita;
    }

    public String getInfoCita() {
        return infoCita;
    }

    public void setInfoCita(String infoCita) {
        this.infoCita = infoCita;
    }
}
