package com.example.preguntalo.menu.ui.perfil;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.preguntalo.Modelo.PassView;
import com.example.preguntalo.Modelo.Usuario;
import com.example.preguntalo.Request.ApiClientRetrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilViewModel extends AndroidViewModel {

    private Context context;
    private MutableLiveData<Usuario> mutableUsuario;
    private MutableLiveData<String> mutableErrorDatos;
    private MutableLiveData<String> mutableErrorPass;
    public PerfilViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        mutableUsuario = new MutableLiveData<>();
        mutableErrorDatos = new MutableLiveData<>();
        mutableErrorPass = new MutableLiveData<>();
        //llenar campos de usuario
        llenarCampos();
    }
    public MutableLiveData<Usuario> getMutableUsuario() {
        return mutableUsuario;
    }
    public MutableLiveData<String> getMutableErrorDatos() { return mutableErrorDatos; }
    public MutableLiveData<String> getMutableErrorPass() { return mutableErrorPass; }


    public void llenarCampos(){
        ApiClientRetrofit.EndPointPreguntalo end = ApiClientRetrofit.getEndPointPreguntalo();
        Call<Usuario> call = end.obtenerPerfil(context.getSharedPreferences("token.xml",0).getString("token",""));

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if(response.isSuccessful()){
                    mutableUsuario.postValue(response.body());
                }else{
                    Log.d("salida error: ","al traer el perfil se obtuvo:"  + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {

            }
        });
    }

    private boolean validarDatos(Usuario usuario){
        return !usuario.getNombre().equals("") &&
                !usuario.getApellido().equals("") &&
                !usuario.getDni().equals("");
    }

    private boolean validarPass(PassView passview){
        return !passview.password.equals("") &&
                !passview.newPassword.equals("") &&
                !passview.confirmPassword.equals("") &&
                passview.validarConfirmacionPassword();
    }

    public void editar(Usuario usuario){
        if(validarDatos(usuario)){
            ApiClientRetrofit.EndPointPreguntalo end = ApiClientRetrofit.getEndPointPreguntalo();
            Call<Usuario> call = end.editar(context.getSharedPreferences("token.xml",0).getString("token",""), usuario);

            call.enqueue(new Callback<Usuario>(){

                @Override
                public void onResponse(@NonNull Call<Usuario> call, @NonNull Response<Usuario> response) {

                    if(response.isSuccessful()){
                        mutableUsuario.postValue(response.body());
                        Toast.makeText(context, "Perfil editado!", Toast.LENGTH_SHORT).show();
                    }else{
                        Log.d("salida error: ","al editar el perfil se obtuvo: "  + response);
                        mutableErrorDatos.postValue("Error al editar!");
                    }
                }

                @Override
                public void onFailure(Call<Usuario> call, Throwable t) {
                    mutableErrorDatos.postValue("Error, Call OnFailure: " + t.getMessage());
                }
            });

        }else{
            mutableErrorDatos.postValue("Corrobore los campos");
        }
    }
    public void editarPassword(PassView passView){
        if(validarPass(passView)){
            ApiClientRetrofit.EndPointPreguntalo end = ApiClientRetrofit.getEndPointPreguntalo();
            Call<Usuario> call = end.editarPassword(context.getSharedPreferences("token.xml",0).getString("token",""), passView);

            call.enqueue(new Callback<Usuario>(){

                @Override
                public void onResponse(@NonNull Call<Usuario> call, @NonNull Response<Usuario> response) {

                    if(response.isSuccessful()){
                        mutableUsuario.postValue(response.body());
                        Toast.makeText(context, "Contraseña actualizada!", Toast.LENGTH_SHORT).show();
                    }else{
                        Log.d("salida error: ","al actualizar password se obtuvo: "  + response);
                        mutableErrorPass.postValue("Error al actualizar!");
                    }
                }

                @Override
                public void onFailure(Call<Usuario> call, Throwable t) {
                    mutableErrorPass.postValue("Error, Call OnFailure: " + t.getMessage());
                }
            });

        }else{
            mutableErrorPass.postValue("Corrobore los campos");
        }
    }

}