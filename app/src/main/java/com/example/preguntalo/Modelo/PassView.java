package com.example.preguntalo.Modelo;

public class PassView {
    public String password;
    public String newPassword;
    public String confirmPassword;

    public PassView() {
        password = "";
        newPassword = "";
        confirmPassword = "";
    }

    public PassView(String password, String newPassword, String confirmPassword) {
        this.password = password;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public boolean validarConfirmacionPassword() {
        return confirmPassword.equals(newPassword);
    }
}
