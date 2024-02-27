package com.example.preguntalo.menu.ui.estadistica;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.preguntalo.Modelo.Rating;
import com.example.preguntalo.Request.ApiClientRetrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EstadisticaViewModel extends AndroidViewModel {

    private Context context;
    private MutableLiveData<Double> mutableScore;
    private MutableLiveData<Rating> mutableRating;
    public EstadisticaViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        mutableScore = new MutableLiveData<>();
        mutableRating = new MutableLiveData<>();
        //completar valores
        llenarValores();
    }

    public MutableLiveData<Double> getMutableScore() { return mutableScore;  }
    public MutableLiveData<Rating> getMutableRating() { return mutableRating; }

    private void llenarValores() {
        ApiClientRetrofit.EndPointPreguntalo end = ApiClientRetrofit.getEndPointPreguntalo();
        Call<Rating> call = end.obtenerRating(context.getSharedPreferences("token.xml",0).getString("token",""));

        call.enqueue(new Callback<Rating>() {
            @Override
            public void onResponse(Call<Rating> call, Response<Rating> response) {
                if(response.isSuccessful()){
                    mutableRating.postValue(response.body());
                    mutableScore.postValue(response.body().getScore());
                }else{
                    Log.d("salida error: ","al traer el perfil se obtuvo: "  + response);
                }
            }

            @Override
            public void onFailure(Call<Rating> call, Throwable t) {

            }
        });
    }
    private void calcularScore(){
        mutableScore.postValue(mutableRating.getValue().getScore());
    }

}