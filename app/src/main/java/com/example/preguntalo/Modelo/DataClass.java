package com.example.preguntalo.Modelo;

public class DataClass {
    private String stringURL, caption;

    public DataClass() {
    }

    public DataClass(String stringURL, String caption) {
        this.stringURL = stringURL;
        this.caption = caption;
    }

    public String getStringURL() {
        return stringURL;
    }

    public String getCaption() {
        return caption;
    }
    public void setCaption(String caption) {
        this.caption = caption;
    }
    public void setStringURL(String stringURL) {

    }
}
