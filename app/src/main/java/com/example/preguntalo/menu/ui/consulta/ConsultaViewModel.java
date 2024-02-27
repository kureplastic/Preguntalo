package com.example.preguntalo.menu.ui.consulta;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.preguntalo.Modelo.Categoria;
import com.example.preguntalo.Modelo.Consulta;
import com.example.preguntalo.Request.ApiClientRetrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConsultaViewModel extends AndroidViewModel {

    private Context context;
    private MutableLiveData<List<Categoria>> mutableCategorias;
    private MutableLiveData<Consulta> mutableConsulta;
    private MutableLiveData<String> mutableError;
    public ConsultaViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        mutableCategorias = new MutableLiveData<>();
        mutableConsulta = new MutableLiveData<>();
        mutableError = new MutableLiveData<>();
        //obtener categorias
        getcategorias();
    }

    public MutableLiveData<List<Categoria>> getMutableCategorias() { return mutableCategorias; }

    public MutableLiveData<Consulta> getMutableConsulta() { return mutableConsulta; }

    public MutableLiveData<String> getMutableError() { return mutableError; }

    public void crearConsulta(Consulta consulta){
        if(validarConsulta(consulta)){
            ApiClientRetrofit.EndPointPreguntalo end = ApiClientRetrofit.getEndPointPreguntalo();
            Call<Consulta> call = end.crearConsulta(context.getSharedPreferences("token.xml",0).getString("token",""),consulta);

            call.enqueue(new Callback<Consulta>() {
                @Override
                public void onResponse(Call<Consulta> call, Response<Consulta> response) {
                    if(response.isSuccessful()){
                        mutableConsulta.postValue(response.body());
                        Toast.makeText(context, "CONSULTA CREADA", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
                        Log.d("salida error: ","al crear consulta se obtuvo: "  + response);
                    }
                }

                @Override
                public void onFailure(Call<Consulta> call, Throwable t) {
                    Log.d("salida error: ","error en call de crear consulta : "  + t);
                }
            });
        }else{
            Log.d("salida error: ","falta completar campos");
            mutableError.postValue("corrobore completar los campos");
        }

    }

    private boolean validarConsulta(Consulta consulta) {
        return !consulta.getTitulo().equals("") && !consulta.getTexto().equals("");
    }

    private void getcategorias() {

        ApiClientRetrofit.EndPointPreguntalo end = ApiClientRetrofit.getEndPointPreguntalo();
        Call<List<Categoria>> call = end.obtenerCategorias(context.getSharedPreferences("token.xml",0).getString("token",""));

        call.enqueue(new Callback<List<Categoria>>() {
            @Override
            public void onResponse(Call<List<Categoria>> call, Response<List<Categoria>> response) {
                if(response.isSuccessful()){
                    mutableCategorias.postValue(response.body());
                }else{
                    Log.d("salida error: ","al traer las categorias se obtuvo: "  + response);
                }
            }
            @Override
            public void onFailure(Call<List<Categoria>> call, Throwable t) {

            }
        });
    }

}