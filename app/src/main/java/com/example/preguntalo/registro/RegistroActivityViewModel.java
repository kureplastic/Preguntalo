package com.example.preguntalo.registro;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.preguntalo.Modelo.Usuario;
import com.example.preguntalo.Request.ApiClientRetrofit;
import com.example.preguntalo.menu.MenuActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroActivityViewModel extends AndroidViewModel {

    private Context context;
    private MutableLiveData<String> mutableRegistroError;
    private MutableLiveData<String> mutablePasswordError;
    private MutableLiveData<String> mutableEmailError;
    public RegistroActivityViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        mutableRegistroError = new MutableLiveData<>();
        mutablePasswordError = new MutableLiveData<>();
        mutableEmailError = new MutableLiveData<>();
    }
    public MutableLiveData<String> getMutableRegistroError() { return mutableRegistroError;  }
    public MutableLiveData<String> getMutablePasswordError() { return mutablePasswordError;  }
    public MutableLiveData<String> getMutableEmailError() { return mutableEmailError;  }

    public void registrar(Usuario user, String pass, String repetirpass) {

        if (validarCampos(user) && validarPassword(pass, repetirpass)) {
            //TODO: -buscar mail en base para saber si ya esta registrado,
            //TODO: -crear usuario,
            //TODO: -loguear usuario,
            //TODO: -redirigir a main activity.
            ApiClientRetrofit.EndPointPreguntalo end = ApiClientRetrofit.getEndPointPreguntalo();
            Call<Usuario> call = end.registrar(user);
            call.enqueue(new Callback<Usuario>() {

                @Override
                public void onResponse(Call<Usuario> call, Response<Usuario> response) {

                    if (response.isSuccessful()) {
                        //ahora loguear el usuario con otra call al finalizar esta
                        ApiClientRetrofit.EndPointPreguntalo end1 = ApiClientRetrofit.getEndPointPreguntalo();
                        Call<String> call2 = end1.login(user);
                        call2.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                if(response.isSuccessful()){
                                    if (response.body() != null) {
                                        //guardar el token
                                        Log.d("salida call: ", "aca esta tratando de loguear");
                                        SharedPreferences sp = context.getSharedPreferences("token.xml", 0);
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.putString("token", "Bearer " + response.body());
                                        editor.commit();
                                        //abrir la actividad principal
                                        Intent intent = new Intent(context, MenuActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        context.startActivity(intent);
                                    }
                                }
                                else{
                                    mutableEmailError.setValue("No se pudo loguear");
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Toast.makeText(context, "Error al intentar loguear: " + t, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    else {
                        mutableEmailError.setValue("Ya existe un usuario con ese email");
                    }
                }

                @Override
                public void onFailure(Call<Usuario> call, Throwable t) {
                    Toast.makeText(context, "Error al intentar registrar: " + t, Toast.LENGTH_LONG).show();
                    mutableEmailError.setValue(t.getMessage());
                }
            });
            }
        }

    private boolean validarCampos(Usuario user) {
        if (user.getEmail().isEmpty() || user.getNombre().isEmpty() || user.getApellido().isEmpty() || user.getDni().isEmpty()) {
            mutableRegistroError.setValue("Completar todos los campos");
            return false;
        }
        return true;
    }

    private boolean validarPassword(String pass, String repetirpass) {
        if (!pass.equals(repetirpass)) {
            mutablePasswordError.setValue("Las contraseñas no coinciden");
            return false;
        }
        if(pass.isEmpty() || repetirpass.isEmpty()){
            mutablePasswordError.setValue("Completar ambos campos");
            return false;
        }
        //agregar niveles de seguridad al password
        return true;
    }

    //
}
