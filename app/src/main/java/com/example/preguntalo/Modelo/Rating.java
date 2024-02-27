package com.example.preguntalo.Modelo;

public class Rating {
    // Rating tiene un id de tipo int, un PuntuacionPositiva de tipo int, un PuntuacionNegativa de tipo int, un RespuestasElegidas de tipo int,
    //un RespuestasContestadas de tipo int y un ConsultasRealizadas de tipo int.

    private int id;
    private int puntuacionPositiva;
    private int puntuacionNegativa;
    private int respuestasElegidas;
    private int respuestasContestadas;
    private int consultasRealizadas;

    //se deberia tener una tabla
    private final int multipl_RE = 10;
    private final int multipl_RC = 3;
    private final double multipl_CR = 0.5;
    private final int multipl_PP = 5;

    public Rating() {
        this.puntuacionPositiva = 0;
        this.puntuacionNegativa = 0;
        this.respuestasElegidas = 0;
        this.respuestasContestadas = 0;
        this.consultasRealizadas = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getRespuestasElegidas() {
        return respuestasElegidas;
    }

    public void setRespuestasElegidas(int respuestasElegidas) {
        this.respuestasElegidas = respuestasElegidas;
    }

    public int getRespuestasContestadas() {
        return respuestasContestadas;
    }

    public void setRespuestasContestadas(int respuestasContestadas) {
        this.respuestasContestadas = respuestasContestadas;
    }

    public int getConsultasRealizadas() {
        return consultasRealizadas;
    }

    public void setConsultasRealizadas(int consultasRealizadas) {
        this.consultasRealizadas = consultasRealizadas;
    }

    public double getScore() {
        return (double) ( (this.puntuacionPositiva * multipl_PP - this.puntuacionNegativa) + this.respuestasElegidas * multipl_RE + this.respuestasContestadas * multipl_RC + this.consultasRealizadas * multipl_CR) / 100;
    }
}
