package com.example.preguntalo.login;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class LoginActivityViewModel extends AndroidViewModel {

    private Context context;
    private MutableLiveData<String> mutableError;

    public LoginActivityViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        mutableError = new MutableLiveData<>();
    }

    public MutableLiveData<String> getMutableError() {
        return mutableError;
    }

    public void login(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            mutableError.setValue("Completar los campos");
        } else {
            Usuario usuario = new Usuario(email, password);
            ApiClientRetrofit.EndPointPreguntalo end = ApiClientRetrofit.getEndPointPreguntalo();
            Call<String> call = end.login(usuario);

            call.enqueue(new Callback<String>() {

                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            //guardar el token
                            SharedPreferences sp = context.getSharedPreferences("token.xml", 0);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("token", "Bearer " + response.body());
                            editor.commit();
                            //abrir la actividad principal
                            Intent intent = new Intent(context, MenuActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    } else {
                        mutableError.setValue("Corrobore usuario y/o contraseña!");
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(context, "Error al llamar login: " + t, Toast.LENGTH_LONG).show();
                    mutableError.setValue(t.getMessage());
                }
            });
        }
    }
}
