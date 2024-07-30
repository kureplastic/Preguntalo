package com.example.preguntalo.Modelo;

import java.io.Serializable;

public class Puntuacion implements Serializable {

    private int id;
    private int consultaId;
    private int usuarioId;
    private int respuestaId;
    private Usuario usuario;
    private Consulta consulta;
    private Respuesta respuesta;
    private boolean puntuacionPositiva;


    public Puntuacion() {
    }

    public Puntuacion(int id, int consultaId, int usuarioId, int respuestaId, Usuario usuario, Consulta consulta, Respuesta respuesta, boolean puntuacionPositiva) {
        this.id = id;
        this.consultaId = consultaId;
        this.usuarioId = usuarioId;
        this.respuestaId = respuestaId;
        this.usuario = usuario;
        this.consulta = consulta;
        this.respuesta = respuesta;
        this.puntuacionPositiva = puntuacionPositiva;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getConsultaId() {
        return consultaId;
    }

    public void setConsultaId(int consultaId) {
        this.consultaId = consultaId;
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

    public Consulta getConsulta() {
        return consulta;
    }

    public void setConsulta(Consulta consulta) {
        this.consulta = consulta;
    }

    public boolean isPuntuacionPositiva() {
        return puntuacionPositiva;
    }

    public void setPuntuacionPositiva(boolean puntuacionPositiva) {
        this.puntuacionPositiva = puntuacionPositiva;
    }

    public int getRespuestaId() {
        return respuestaId;
    }

    public void setRespuestaId(int respuestaId) {
        this.respuestaId = respuestaId;
    }

    public Respuesta getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(Respuesta respuesta) {
        this.respuesta = respuesta;
    }
}
