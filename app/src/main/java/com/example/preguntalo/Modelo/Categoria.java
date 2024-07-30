package com.example.preguntalo.Modelo;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Categoria implements Serializable {

    private int id;
    private String nombre;

    public Categoria() {
    }

    public Categoria(String nombre) {
        this.nombre = nombre;
    }

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

    @NonNull
    @Override
    public String toString() {
        return getNombre();
    }
}


