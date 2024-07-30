package com.example.preguntalo.Modelo;

import java.io.Serializable;

public class Consulta_Categoria implements Serializable {
    private int id;
    private int consultaId;
    private int categoriaId;
    private Consulta consulta;
    private Categoria categoria;

    public Consulta_Categoria(){}

    public Consulta_Categoria(int id, int consultaId, int categoriaId, Consulta consulta, Categoria categoria) {
        this.id = id;
        this.consultaId = consultaId;
        this.categoriaId = categoriaId;
        this.consulta = consulta;
        this.categoria = categoria;
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

    public int getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(int categoriaId) {
        this.categoriaId = categoriaId;
    }

    public Consulta getConsulta() {
        return consulta;
    }

    public void setConsulta(Consulta consulta) {
        this.consulta = consulta;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}
