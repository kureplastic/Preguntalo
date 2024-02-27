package com.example.preguntalo.menu.ui.consulta;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.preguntalo.Modelo.Consulta;
import com.example.preguntalo.Modelo.Rating;
import com.example.preguntalo.Modelo.Respuesta;
import com.example.preguntalo.Request.ApiClientRetrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MostrarConsultaViewModel extends AndroidViewModel {

    private Context context;
    private MutableLiveData<Consulta> mutableConsulta;
    private MutableLiveData<List<Respuesta>> mutableRespuestas;
    private MutableLiveData<String> mutableError;

    public MostrarConsultaViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        mutableConsulta = new MutableLiveData<>();
        mutableRespuestas = new MutableLiveData<>();
        mutableError = new MutableLiveData<>();
        //obtener respuestas desde la llamada de la consulta
    }

    public MutableLiveData<Consulta> getMutableConsulta() { return mutableConsulta; }
    public MutableLiveData<List<Respuesta>> getMutableRespuestas() { return mutableRespuestas; }

    public MutableLiveData<String> getMutableError() { return mutableError; }

    public void llenarConsulta(Consulta consulta) {
        ApiClientRetrofit.EndPointPreguntalo end = ApiClientRetrofit.getEndPointPreguntalo();
        Call<Consulta> call = end.getConsulta(context.getSharedPreferences("token.xml",0).getString("token",""),consulta.getId());

        call.enqueue(new Callback<Consulta>() {
            @Override
            public void onResponse(Call<Consulta> call, Response<Consulta> response) {
                if(response.isSuccessful()){
                    mutableConsulta.postValue(response.body());
                    //obtener respuestas
                    getRespuestas(response.body());

                }else {
                    Log.d("salida error: ","al llenar consulta se obtuvo: "  + response);
                    Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Consulta> call, Throwable t) {

                Log.d("salida error: ","error en call de llenar consulta : "  + t);
            }
        });

    }

    public boolean validarRespuesta(String texto) { return !texto.equals("");  }

    public void publicarRespuesta(String texto) {
        if(validarRespuesta(texto)){
            Respuesta respuesta = new Respuesta();
            respuesta.setConsultaId(mutableConsulta.getValue().getId());
            respuesta.setTexto(texto);
            respuesta.setUsuarioId(1);
            respuesta.setRespuestaId(0);
            ApiClientRetrofit.EndPointPreguntalo end = ApiClientRetrofit.getEndPointPreguntalo();
            Call<Respuesta> call = end.crearRespuesta(context.getSharedPreferences("token.xml",0).getString("token",""),respuesta);

            call.enqueue(new Callback<Respuesta>() {
                @Override
                public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {
                    if(response.isSuccessful()){
                        getRespuestas(mutableConsulta.getValue());
                    }
                }

                @Override
                public void onFailure(Call<Respuesta> call, Throwable t) {

                }
            });

        }else{
            mutableError.postValue("Escriba su respuesta");
        }
    }

    private void getRespuestas(Consulta consulta) {
        ApiClientRetrofit.EndPointPreguntalo end = ApiClientRetrofit.getEndPointPreguntalo();
        Call<List<Respuesta>> call = end.getRespuestasDeConsulta(context.getSharedPreferences("token.xml",0).getString("token",""),consulta.getId());

        call.enqueue(new Callback<List<Respuesta>>() {
            @Override
            public void onResponse(Call<List<Respuesta>> call, Response<List<Respuesta>> response) {
                if(response.isSuccessful()){
                    List<Respuesta> respuestas = response.body();
                    mutableRespuestas.postValue(respuestas);
                    if(respuestas.isEmpty()){
                        Toast.makeText(context, "Sé el primero en responder!", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Log.d("salida error: ","al obtener respuestas se obtuvo: "  + response);
                    Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Respuesta>> call, Throwable t) {

            }
        });
    }

    public void upvote(Consulta consulta) {
        Toast.makeText(context, "Diste UPVOTE", Toast.LENGTH_SHORT).show();
    }

    public void downvote(Consulta consulta) {
        Toast.makeText(context, "Diste DOWNVOTE", Toast.LENGTH_SHORT).show();
    }
}