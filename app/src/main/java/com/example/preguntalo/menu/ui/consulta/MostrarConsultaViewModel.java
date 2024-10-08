package com.example.preguntalo.menu.ui.consulta;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.preguntalo.Modelo.Consulta;
import com.example.preguntalo.Modelo.Puntuacion;
import com.example.preguntalo.Modelo.Rating;
import com.example.preguntalo.Modelo.Respuesta;
import com.example.preguntalo.Modelo.StringRespuesta;
import com.example.preguntalo.Modelo.Usuario;
import com.example.preguntalo.Request.ApiClientRetrofit;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MostrarConsultaViewModel extends AndroidViewModel {

    private Context context;
    private MutableLiveData<Consulta> mutableConsulta;
    private MutableLiveData<List<Respuesta>> mutableRespuestas;
    private MutableLiveData<List<Respuesta>> mutableComentarios;
    private MutableLiveData<String> mutableError;
    private MutableLiveData<String> mutableErrorConsulta;
    private MutableLiveData<Boolean> mutableRenavegar;
    private MutableLiveData<Usuario> mutableUsuarioObtenido;
    private MutableLiveData<Boolean> mutableUsuarioPropietarioDeConsulta;
    private MutableLiveData<Boolean> mutableConsultaEliminada;
    private MutableLiveData<String> mutableImagen;

    public MostrarConsultaViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        mutableConsulta = new MutableLiveData<>();
        mutableRespuestas = new MutableLiveData<>();
        mutableComentarios = new MutableLiveData<>();
        mutableError = new MutableLiveData<>();
        mutableErrorConsulta = new MutableLiveData<>();
        mutableRenavegar = new MutableLiveData<>();
        mutableUsuarioObtenido = new MutableLiveData<>();
        mutableUsuarioPropietarioDeConsulta = new MutableLiveData<>();
        mutableConsultaEliminada = new MutableLiveData<>();
        mutableImagen = new MutableLiveData<>();
        //obtener respuestas desde la llamada de la consulta
    }

    public MutableLiveData<Consulta> getMutableConsulta() { return mutableConsulta; }
    public MutableLiveData<List<Respuesta>> getMutableRespuestas() {return mutableRespuestas;}
    public MutableLiveData<List<Respuesta>> getMutableComentarios() {return mutableComentarios;}
    public MutableLiveData<String> getMutableError() { return mutableError; }
    public MutableLiveData<String> getMutableErrorConsulta() { return mutableErrorConsulta; }
    public MutableLiveData<Boolean> getMutableRenavegar() { return mutableRenavegar; }
    public MutableLiveData<Usuario> getMutableUsuarioObtenido() {return mutableUsuarioObtenido; }
    public MutableLiveData<Boolean> getMutableUsuarioPropietarioDeConsulta() { return mutableUsuarioPropietarioDeConsulta;   }
    public MutableLiveData<Boolean> getMutableConsultaEliminada() {return mutableConsultaEliminada;}
    public MutableLiveData<String> getMutableImagen() {  return mutableImagen; }

    public void llenarConsulta(Consulta consulta) {
        ApiClientRetrofit.EndPointPreguntalo end = ApiClientRetrofit.getEndPointPreguntalo();
        Call<Consulta> call = end.getConsulta(context.getSharedPreferences("token.xml", 0).getString("token", ""), consulta.getId());

        call.enqueue(new Callback<Consulta>() {
            @Override
            public void onResponse(Call<Consulta> call, Response<Consulta> response) {
                if (response.isSuccessful()) {
                    Consulta consulta = response.body();
                    mutableConsulta.postValue(consulta);
                    if(consulta.getImagenConsulta() != null){
                        mutableImagen.setValue(consulta.getImagenConsulta());
                    }

                    //obtener respuestas
                    getRespuestas(response.body());

                } else {
                    Log.d("salida error: ", "al llenar consulta se obtuvo: " + response);
                    Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Consulta> call, Throwable t) {

                Log.d("salida error: ", "error en call de llenar consulta : " + t);
            }
        });

    }
    public boolean validarRespuesta(String texto) {
        return !texto.equals("");
    }

    public void publicarRespuesta(String texto) {
        if (validarRespuesta(texto)) {
            Respuesta respuesta = new Respuesta();
            respuesta.setConsultaId(mutableConsulta.getValue().getId());
            respuesta.setTexto(texto);
            respuesta.setUsuarioId(1);
            respuesta.setRespuestaId(0);
            ApiClientRetrofit.EndPointPreguntalo end = ApiClientRetrofit.getEndPointPreguntalo();
            Call<Respuesta> call = end.crearRespuesta(context.getSharedPreferences("token.xml", 0).getString("token", ""), respuesta);

            call.enqueue(new Callback<Respuesta>() {
                @Override
                public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {
                    if (response.isSuccessful()) {
                        getRespuestas(mutableConsulta.getValue());
                        Toast.makeText(context, "Respuesta publicada", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Respuesta> call, Throwable t) {

                }
            });

        } else {
            mutableError.postValue("Escriba su respuesta");
        }
    }

    private void getRespuestas(Consulta consulta) {
        ApiClientRetrofit.EndPointPreguntalo end = ApiClientRetrofit.getEndPointPreguntalo();
        Call<List<Respuesta>> call = end.getRespuestasDeConsulta(context.getSharedPreferences("token.xml", 0).getString("token", ""), consulta.getId());

        call.enqueue(new Callback<List<Respuesta>>() {
            @Override
            public void onResponse(Call<List<Respuesta>> call, Response<List<Respuesta>> response) {
                if (response.isSuccessful()) {
                    //separar las respuestas en dos listas, una de respuestas y otra de comentarios
                    List<Respuesta> respuestas = response.body();
                    List<Respuesta> comentarios = obtenerComentarios(respuestas);
                    List<Respuesta> respuestasSinComentarios = removerComentarios(respuestas);
                    mutableComentarios.postValue(comentarios);

                    //si la consulta tiene una respuesta seleccionada ordenar con la respuesta seleccionada primero
                    Consulta consulta = mutableConsulta.getValue();
                    if(!Integer.valueOf(consulta.getRespuestaSeleccionada()).equals(null)) {
                        mutableRespuestas.postValue(ordenarPorRespuestaSeleccionada(respuestasSinComentarios,consulta.getRespuestaSeleccionada()));
                    }else {
                        mutableRespuestas.postValue(respuestasSinComentarios);
                    }
                    if (respuestas.isEmpty()) {
                        Toast.makeText(context, "Sé el primero en responder!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d("salida error: ", "al obtener respuestas se obtuvo: " + response);
                    Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Respuesta>> call, Throwable t) {

            }
        });
    }

    private List<Respuesta> obtenerComentarios(List<Respuesta> respuestas) {
        List<Respuesta> comentarios = new ArrayList<>();
        for (Respuesta respuesta : respuestas) {
            if(!Integer.valueOf(respuesta.getRespuestaId()).equals(0)) {
                comentarios.add(respuesta);
            }
        }
        return comentarios;
    }
    private List<Respuesta> removerComentarios(List<Respuesta> respuestas) {
        List<Respuesta> respuestasSinComentarios = new ArrayList<>();
        for (Respuesta respuesta : respuestas) {
            if(Integer.valueOf(respuesta.getRespuestaId()).equals(0)) {
                respuestasSinComentarios.add(respuesta);
            }
        }
        return respuestasSinComentarios;
    }

    private List<Respuesta> ordenarPorRespuestaSeleccionada(List<Respuesta> respuestas, int idRespuestaSeleccionada) {
        List<Respuesta> respuestasOrdenadas = new ArrayList<>();
        for (Respuesta respuesta : respuestas) {
            if (respuesta.getId() == idRespuestaSeleccionada) {
                respuestasOrdenadas.add(0, respuesta);
            } else {
                respuestasOrdenadas.add(respuesta);
            }
        }
        return respuestasOrdenadas;
    }

    public void upvote(Consulta consulta) {
        puntuar(consulta, true);

    }

    public void downvote(Consulta consulta) {
        puntuar(consulta, false);
    }

    public void reiniciarMutableNavegacion() {
        mutableRenavegar.postValue(false);
    }

    public void puntuar(Consulta consulta, Boolean valor) {
        //armar puntuacion
        Puntuacion puntuacionAEnviar = new Puntuacion();
        int idUsuario = context.getSharedPreferences("userdata.xml", 0).getInt("id", 0);
        puntuacionAEnviar.setConsultaId(consulta.getId());
        puntuacionAEnviar.setUsuarioId(idUsuario);
        puntuacionAEnviar.setConsulta(consulta);
        //ponerle el valor de puntuacion
        puntuacionAEnviar.setPuntuacionPositiva(valor);
        //llamar a la api
        ApiClientRetrofit.EndPointPreguntalo end = ApiClientRetrofit.getEndPointPreguntalo();
        Call<StringRespuesta> call = end.cambiarPuntuacion(context.getSharedPreferences("token.xml", 0).getString("token", ""), puntuacionAEnviar);
        call.enqueue(new Callback<StringRespuesta>() {
            @Override
            public void onResponse(Call<StringRespuesta> call, Response<StringRespuesta> response) {
                if (response.isSuccessful()) {
                    StringRespuesta respuestaEntrante = response.body();
                    Toast.makeText(context, respuestaEntrante.getRespuesta(), Toast.LENGTH_SHORT).show();
                    //navegar al mismo fragment
                    mutableRenavegar.setValue(true);
                }
            }

            @Override
            public void onFailure(Call<StringRespuesta> call, Throwable t) {
                Toast.makeText(context, "Error en llamada DOWNVOTE", Toast.LENGTH_SHORT).show();
                Log.d("salida error: ", "error en call de cambiar puntuacion : " + t);
                //navegar al mismo fragment
                mutableRenavegar.setValue(true);
            }
        });
    }

    public void obtenerUsuario(Consulta consultaObtenida) {
        Usuario user = consultaObtenida.getUsuario();
        int ratingId = user.getRatingId();
        ApiClientRetrofit.EndPointPreguntalo end = ApiClientRetrofit.getEndPointPreguntalo();
        Call<Rating> call = end.obtenerRatingDeUsuario(context.getSharedPreferences("token.xml", 0).getString("token", ""), ratingId);
        call.enqueue(new Callback<Rating>() {
            @Override
            public void onResponse(Call<Rating> call, Response<Rating> response) {
                if (response.isSuccessful()) {
                    Rating rating = response.body();
                    user.setRating(rating);
                    mutableUsuarioObtenido.postValue(user);
                } else {
                    Log.d("salida error: ", "al obtener rating se obtuvo: " + response);
                    Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Rating> call, Throwable t) {
                Toast.makeText(context, "Error al obtener rating", Toast.LENGTH_SHORT).show();
                Log.d("salida error: ", "error en call de obtener rating : " + t);
            }
        });
    }

    public void CorroborarUsuarioPropietario(Consulta consultaObtenida) {
        if (consultaObtenida.getUsuarioId() == context.getSharedPreferences("userdata.xml", 0).getInt("id", 0)) {
            mutableUsuarioPropietarioDeConsulta.postValue(true);
        } else {
            mutableUsuarioPropietarioDeConsulta.postValue(false);
        }
    }

    public void editarConsulta(Consulta consulta) {
        if (!(consulta.getTexto().equals("") && consulta.getTitulo().equals(""))) {
            //llamar a la api
            ApiClientRetrofit.EndPointPreguntalo end = ApiClientRetrofit.getEndPointPreguntalo();
            Call<Consulta> call = end.editarConsulta(context.getSharedPreferences("token.xml", 0).getString("token", ""), consulta);
            call.enqueue(new Callback<Consulta>() {
                @Override
                public void onResponse(Call<Consulta> call, Response<Consulta> response) {
                    if (response.isSuccessful()) {
                        mutableConsulta.postValue(response.body());
                        //navegar al mismo fragment
                        mutableRenavegar.setValue(true);
                        Toast.makeText(context, "Consulta editada", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("salida error: ", "al editar consulta se obtuvo: " + response);
                        Toast.makeText(context, "Error al editar consulta", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Consulta> call, Throwable t) {
                    Toast.makeText(context, "Error al editar consulta", Toast.LENGTH_SHORT).show();
                    Log.d("salida error: ", "error en call de editar consulta : " + t);
                }
            });
        } else {
            mutableErrorConsulta.setValue("llene los campos de consulta");
        }
    }

    public void eliminarConsulta(Consulta consulta) {
        //llamar a la api
        ApiClientRetrofit.EndPointPreguntalo end = ApiClientRetrofit.getEndPointPreguntalo();
        Call<StringRespuesta> call = end.eliminarConsulta(context.getSharedPreferences("token.xml", 0).getString("token", ""), consulta);
        call.enqueue(new Callback<StringRespuesta>() {
            @Override
            public void onResponse(Call<StringRespuesta> call, Response<StringRespuesta> response) {
                if (response.isSuccessful()) {
                    //navegar hacia main frame
                    mutableConsultaEliminada.setValue(true);
                    Toast.makeText(context, response.body().getRespuesta(), Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("salida error: ", "al eliminar consulta se obtuvo: " + response);
                    Toast.makeText(context, "Error al eliminar consulta", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<StringRespuesta> call, Throwable t) {
                Log.d("salida error: ", "error en call de eliminar consulta : " + t);
                Toast.makeText(context, "Error al llamar eliminar consulta", Toast.LENGTH_SHORT).show();
            }
        });
    }
}