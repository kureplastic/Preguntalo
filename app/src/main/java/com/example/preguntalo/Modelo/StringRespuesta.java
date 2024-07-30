package com.example.preguntalo.Modelo;

public class StringRespuesta {
    private int id;
    private String respuesta;

    public StringRespuesta(int id, String respuesta) {
        this.id = id;
        this.respuesta = respuesta;
    }

    public StringRespuesta() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }
}

