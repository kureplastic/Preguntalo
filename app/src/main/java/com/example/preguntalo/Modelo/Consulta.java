package com.example.preguntalo.Modelo;

import java.io.Serializable;

public class Consulta implements Serializable {
    //consulta tiene un Id, un UsuarioId de tipo int, un Usuario de tipo Usuario, un Titulo de tipo String, un Texto de tipo String,
    //un campo "Resuelto" de tipo boolean, un campo RespuestaSeleccionada de tipo int, una Respuesta de tipo Respuesta,
    //un campo Puntuacion Positiva de tipo int y un campo Puntuacion Negativa de tipo int.


    private int id;
    private int usuarioId;
    private Usuario usuario;
    private String titulo;
    private String texto;
    private boolean resuelto;
    private int respuestaSeleccionada;
    private Respuesta respuesta;
    private int puntuacionPositiva;
    private int puntuacionNegativa;

    public Consulta() {
        this.resuelto = false;
        this.respuesta = null;
        this.puntuacionPositiva = 0;
        this.puntuacionNegativa = 0;
    }

    public Consulta(int usuarioId, Usuario usuario, String titulo, String texto) {
        this.usuarioId = usuarioId;
        this.usuario = usuario;
        this.titulo = titulo;
        this.texto = texto;
        this.resuelto = false;
        this.respuesta = null;
        this.puntuacionPositiva = 0;
        this.puntuacionNegativa = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public boolean isResuelto() {
        return resuelto;
    }

    public void setResuelto(boolean resuelto) {
        this.resuelto = resuelto;
    }

    public int getRespuestaSeleccionada() {
        return respuestaSeleccionada;
    }

    public void setRespuestaSeleccionada(int respuestaSeleccionada) {
        this.respuestaSeleccionada = respuestaSeleccionada;
    }

    public Respuesta getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(Respuesta respuesta) {
        this.respuesta = respuesta;
    }

    public int getPuntuacionPositiva() {
        return puntuacionPositiva;
    }

    public void setPuntuacionPositiva(int puntuacionPositiva) {
        this.puntuacionPositiva = puntuacionPositiva;
    }

    public int getPuntuacionNegativa() {
        return puntuacionNegativa;
    }

    public void setPuntuacionNegativa(int puntuacionNegativa) {
        this.puntuacionNegativa = puntuacionNegativa;
    }
}
