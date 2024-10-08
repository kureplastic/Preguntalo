package com.example.preguntalo.menu.ui.consulta;


import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.preguntalo.Modelo.Categoria;
import com.example.preguntalo.Modelo.Consulta;
import com.example.preguntalo.Modelo.Consulta_Categoria;
import com.example.preguntalo.Request.ApiClientRetrofit;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConsultaViewModel extends AndroidViewModel {

    private Context context;
    private MutableLiveData<List<Categoria>> mutableCategorias;
    private MutableLiveData<Consulta> mutableConsulta;
    private MutableLiveData<String> mutableError;
    private MutableLiveData<Uri> mutableImagenPerfilUri;
    private static final int REQUEST_IMAGE_PICK = 2;
    public ConsultaViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        mutableCategorias = new MutableLiveData<>();
        mutableConsulta = new MutableLiveData<>();
        mutableError = new MutableLiveData<>();
        mutableImagenPerfilUri = new MutableLiveData<>();
        //obtener categorias
        getcategorias();
    }

    public MutableLiveData<List<Categoria>> getMutableCategorias() { return mutableCategorias; }

    public MutableLiveData<Consulta> getMutableConsulta() { return mutableConsulta; }

    public MutableLiveData<String> getMutableError() { return mutableError; }

    public MutableLiveData<Uri> getMutableImagenPerfilUri() { return mutableImagenPerfilUri; }
    public void setMutableImagenPerfilUri(Uri photoUri) { mutableImagenPerfilUri.setValue(photoUri); }



    public void crearConsulta(Consulta consulta,String categoriaSeleccionada){
        if(validarConsulta(consulta)){
            //analizar si mutableImagenPerfilUri tiene valor
            if(mutableImagenPerfilUri.getValue() != null){
                //si tiene valor, comenzar el proceso de subida de archivo
            }
            ApiClientRetrofit.EndPointPreguntalo end = ApiClientRetrofit.getEndPointPreguntalo();
            Call<Consulta> call = end.crearConsulta(context.getSharedPreferences("token.xml",0).getString("token",""),consulta);

            call.enqueue(new Callback<Consulta>() {
                @Override
                public void onResponse(Call<Consulta> call, Response<Consulta> response) {
                    if(response.isSuccessful()){
                        //si categoriaSeleccionada no es null
                        if(categoriaSeleccionada != null){
                            //crear nueva llamda para generar la relacion Consulta-Categoria
                            Consulta consultaEnDB = response.body();
                            ApiClientRetrofit.EndPointPreguntalo end2 = ApiClientRetrofit.getEndPointPreguntalo();
                            Call<Consulta_Categoria> call2 = end2.crearRelacion(
                                    context.getSharedPreferences("token.xml",0).getString("token",""),
                                    consultaEnDB.getId(),
                                    consultaEnDB.getTitulo(),
                                    consultaEnDB.getTexto(),
                                    categoriaSeleccionada);

                            call2.enqueue(new Callback<Consulta_Categoria>() {
                                @Override
                                public void onResponse(Call<Consulta_Categoria> call, Response<Consulta_Categoria> response) {
                                    if(response.isSuccessful()){
                                        //esto quiere decir que ya se creo la consulta y la relacion
                                        mutableConsulta.postValue(consultaEnDB);
                                        Toast.makeText(context, "CONSULTA CREADA", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        mutableConsulta.postValue(consultaEnDB);
                                        Toast.makeText(context, "ERROR AL AGREGAR LA CATEGORIA", Toast.LENGTH_SHORT).show();
                                        Log.d("salida error: ","al crear relacion se obtuvo: "  + response);
                                    }
                                }

                                @Override
                                public void onFailure(Call<Consulta_Categoria> call, Throwable t) {
                                    Log.d("salida error: ","error en call de crear relacion : "  + t);
                                }
                            });
                        }else {
                            mutableConsulta.postValue(response.body());
                            Toast.makeText(context, "CONSULTA CREADA", Toast.LENGTH_SHORT).show();
                        }

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

    public void subirImagen(ConsultaFragment consultaFragment) {
        Intent photoPicker = new Intent();
        photoPicker.setAction(Intent.ACTION_GET_CONTENT);
        photoPicker.setType("image/*");
        consultaFragment.startActivityForResult(photoPicker, REQUEST_IMAGE_PICK);
    }


}