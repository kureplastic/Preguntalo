package com.example.preguntalo.Modelo;

public class Validacion {
    //validacion tiene un id de tipo int, un Titulo, una entidadOtorgante y una descripcion de tipo String y un confirmada de tipo boolean
    private int id;
    private String titulo;
    private String entidadOtorgante;
    private String descripcion;
    private boolean confirmada;

    public Validacion(){}

    public Validacion(int id, String titulo, String entidadOtorgante, String descripcion, boolean confirmada) {
        this.id = id;
        this.titulo = titulo;
        this.entidadOtorgante = entidadOtorgante;
        this.descripcion = descripcion;
        this.confirmada = confirmada;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getEntidadOtorgante() {
        return entidadOtorgante;
    }

    public void setEntidadOtorgante(String entidadOtorgante) {
        this.entidadOtorgante = entidadOtorgante;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isConfirmada() {
        return confirmada;
    }

    public void setConfirmada(boolean confirmada) {
        this.confirmada = confirmada;
    }
}
