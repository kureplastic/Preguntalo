package com.example.preguntalo.Modelo;

import java.io.Serializable;

public class Usuario implements Serializable {
    //usuario necesita un id de tipo int, un Apellido de tipo String, un Nombre de tipo String, un DNI de tipo String,
    // un email de tipo String, un Password de tipo String, un FotoPerfil de tipo String, un Objeto rating de tipo Rating
    // y un Objeto Validacion de tipo Validacion
    private int id;
    private String apellido;
    private String nombre;
    private String dni;
    private String email;
    private String password;
    private String fotoPerfil;
    private Rating rating;
    private int ratingId;
    private int validacionId;
    private Validacion validacion;

    public Usuario() {
    }


    public Usuario(String Email, String Password) {
        this.email = Email;
        this.password = Password;
        this.apellido = "";
        this.nombre = "";
        this.dni = "";
        this.fotoPerfil = "";
        this.rating = new Rating();
        this.validacion = new Validacion();
        this.ratingId = 0;
        this.validacionId = 0;
    }

    public int getRatingId() {
        return ratingId;
    }

    public void setRatingId(int ratingId) {
        this.ratingId = ratingId;
    }

    public int getValidacionId() {
        return validacionId;
    }

    public void setValidacionId(int validacionId) {
        this.validacionId = validacionId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public Validacion getValidacion() {
        return validacion;
    }

    public void setValidacion(Validacion validacion) {
        this.validacion = validacion;
    }
}
