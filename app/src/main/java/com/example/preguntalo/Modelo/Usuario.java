package com.example.preguntalo.Modelo;

public class Usuario {
    //usuario necesita un id de tipo int, un Apellido de tipo String, un Nombre de tipo String, un DNI de tipo String,
    // un email de tipo String, un Password de tipo String, un FotoPerfil de tipo String, un Objeto rating de tipo Rating
    // y un Objeto Validacion de tipo Validacion
    private int id;
    private String apellido;
    private String nombre;
    private String dni;
    private String email;
    private String password;
    private String foto_perfil;
    private Rating rating;
    private Validacion validacion;

    public Usuario() {
        this.rating = new Rating();
        this.validacion = new Validacion();
    }

    public Usuario(int id, String apellido, String nombre, String dni, String email, String password, String foto_perfil) {
        this.id = id;
        this.apellido = apellido;
        this.nombre = nombre;
        this.dni = dni;
        this.email = email;
        this.password = password;
        this.foto_perfil = foto_perfil;
        this.rating = new Rating();
        this.validacion = new Validacion();
    }

    public Usuario(String Email, String Password) {
        this.email = Email;
        this.password = Password;
        this.apellido = "";
        this.nombre = "";
        this.dni = "";
        this.foto_perfil = "";
        this.rating = new Rating();
        this.validacion = new Validacion();
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

    public String getFoto_perfil() {
        return foto_perfil;
    }

    public void setFoto_perfil(String foto_perfil) {
        this.foto_perfil = foto_perfil;
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
