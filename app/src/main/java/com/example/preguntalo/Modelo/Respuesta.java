package com.example.preguntalo.Modelo;

public class Respuesta {
    //respuesta necesita un Id de tipo int, un UsuarioId de tipo int, un Usuario de tipo Usuario,
    //un Texto de tipo String, un PuntuacionPositiva de tipo int, un PuntuacionNegativa de tipo int,
    //un ConsultaId de tipo int, un Consulta de tipo Consulta, un RespuestaId de tipo int
    //y un Respuesta de tipo Respuesta
    private int id;
    private int usuarioId;
    private Usuario usuario;
    private String texto;
    private int puntuacionPositiva;
    private int puntuacionNegativa;
    private int consultaId;
    private Consulta consulta;
    private int respuestaId;

    public Respuesta(){}

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

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
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

    public int getConsultaId() {
        return consultaId;
    }

    public void setConsultaId(int consultaId) {
        this.consultaId = consultaId;
    }

    public Consulta getConsulta() {
        return consulta;
    }

    public void setConsulta(Consulta consulta) {
        this.consulta = consulta;
    }

    public int getRespuestaId() {
        return respuestaId;
    }

    public void setRespuestaId(int respuestaId) {
        this.respuestaId = respuestaId;
    }

}
