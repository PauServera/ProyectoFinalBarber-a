package com.example.proyectofinalbarberia;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import java.util.List;

public class Barberia implements Parcelable {
    private String id;
    private String nombre;
    private String ubicacion;
    private String telefono;
    private String imageUrl;
    private List<String> listaBarberos;
    private List<Cita> listaCitas;

    public Barberia() {
        this.listaBarberos = new ArrayList<>();
        this.listaCitas = new ArrayList<>();
    }

    public Barberia(String id, String nombre, String ubicacion, String telefono, String imageUrl, List<String> listaBarberos, List<Cita> listaCitas ) {
        this.id = id;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.telefono = telefono;
        this.imageUrl = imageUrl;
        this.listaBarberos = listaBarberos != null ? listaBarberos : new ArrayList<>();
        this.listaCitas = listaCitas != null ? listaCitas : new ArrayList<>();
    }

    protected Barberia(Parcel in) {
        id = in.readString();
        nombre = in.readString();
        ubicacion = in.readString();
        telefono = in.readString();
        imageUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(nombre);
        dest.writeString(ubicacion);
        dest.writeString(telefono);
        dest.writeString(imageUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Barberia> CREATOR = new Creator<Barberia>() {
        @Override
        public Barberia createFromParcel(Parcel in) {
            return new Barberia(in);
        }

        @Override
        public Barberia[] newArray(int size) {
            return new Barberia[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getListaBarberos() {
        return listaBarberos;
    }

    public void setListaBarberos(List<String> listaBarberos) {
        this.listaBarberos = listaBarberos;
    }

    public List<Cita> getListaCitas() {
        return listaCitas;
    }

    public void setListaCitas(List<Cita> listaCitas) {
        this.listaCitas = listaCitas;
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

