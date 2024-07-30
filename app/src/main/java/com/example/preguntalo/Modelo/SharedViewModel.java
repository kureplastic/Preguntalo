package com.example.preguntalo.Modelo;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<String> categoriaSeleccionada = new MutableLiveData<>();

    public MutableLiveData<String> getCategoriaSeleccionada() {
        return this.categoriaSeleccionada;
    }

    public void setCategoriaSeleccionada(String categoria) {
        this.categoriaSeleccionada.setValue(categoria);
    }

    public void resetearCategoria() {
        this.categoriaSeleccionada.setValue(null);
    }
}
