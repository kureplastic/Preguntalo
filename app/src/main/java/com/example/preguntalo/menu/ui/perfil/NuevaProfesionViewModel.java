package com.example.preguntalo.menu.ui.perfil;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.preguntalo.Modelo.Validacion;
import com.example.preguntalo.Request.ApiClientRetrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NuevaProfesionViewModel extends AndroidViewModel {

    private Context context;
    private MutableLiveData<Validacion> mutableValidacion;
    private MutableLiveData<String> mutableError;
    public NuevaProfesionViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        mutableValidacion = new MutableLiveData<>();
        mutableError = new MutableLiveData<>();
    }

    public MutableLiveData<Validacion> getMutableValidacion() { return mutableValidacion; }
    public MutableLiveData<String> getMutableError() { return mutableError; }

    private boolean validarEntrada(Validacion validacion){
        if(validacion.getTitulo().isEmpty() || validacion.getEntidadOtorgante().isEmpty() || validacion.getDescripcion().isEmpty()){
            mutableError.setValue("Complete los campos");
            return false;
        }
        mutableError.setValue(null);
        return true;
    }

    public void crearValidacion(Validacion validacion){
        if(validarEntrada(validacion)){
            //llamar a la api para intentar crear la validacion de la profesion
            ApiClientRetrofit.EndPointPreguntalo end = ApiClientRetrofit.getEndPointPreguntalo();
            Call<Validacion> call = end.crearValidacion(context.getSharedPreferences("token.xml",0).getString("token",""),validacion);
            call.enqueue(new Callback<Validacion>() {
                @Override
                public void onResponse(Call<Validacion> call, Response<Validacion> response) {
                    if(response.isSuccessful()){
                        mutableValidacion.setValue(response.body());
                    }else{
                        Toast.makeText(context, "Ya tienes una profesion registrada", Toast.LENGTH_SHORT).show();
                        Log.d("salida error: ","al crear la validacion de profesion se obtuvo:"  + response.message());
                    }
                }

                @Override
                public void onFailure(Call<Validacion> call, Throwable t) {
                    Toast.makeText(context, "Error en call de agregar la profesion", Toast.LENGTH_SHORT).show();
                    Log.d("salida error: ","al crear la validacion de profesion se obtuvo:"  + t.getMessage());
                }
            });
        }
    }


}