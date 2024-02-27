package com.example.preguntalo.Request;

import com.example.preguntalo.Modelo.Categoria;
import com.example.preguntalo.Modelo.Consulta;
import com.example.preguntalo.Modelo.PassView;
import com.example.preguntalo.Modelo.Rating;
import com.example.preguntalo.Modelo.Respuesta;
import com.example.preguntalo.Modelo.Usuario;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public class ApiClientRetrofit {

    private static final String PATH="http://192.168.0.11:5247/";
    private static  EndPointPreguntalo EndPointPreguntalo;

    public static EndPointPreguntalo getEndPointPreguntalo(){

        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(PATH)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        EndPointPreguntalo=retrofit.create(EndPointPreguntalo.class);

        return EndPointPreguntalo;
    }

    public interface EndPointPreguntalo {

        @POST("Usuarios/login")
        Call<String> login(@Body Usuario usuario);

        @GET("Usuarios/Perfil")
        Call<Usuario> obtenerPerfil(@Header("Authorization") String token);

        @PUT("Usuarios/Editar")
        Call<Usuario> editar(@Header("Authorization") String token, @Body Usuario usuario);

        @PUT("Usuarios/EditarPassword")
        Call<Usuario> editarPassword(@Header("Authorization") String token,@Body PassView passView);

        @GET("Ratings/Obtener")
        Call<Rating> obtenerRating(@Header("Authorization")String token);

        @GET("Categorias/Obtener")
        Call<List<Categoria>> obtenerCategorias(@Header("Authorization") String token);

        @POST("Consultas/Crear")
        Call<Consulta> crearConsulta(@Header("Authorization") String token,@Body Consulta consulta);

        @GET("Consultas/Obtener/{id}")
        Call<Consulta> getConsulta(@Header("Authorization")String token,@Path("id") int id);

        @GET("Respuestas/ObtenerDeConsulta/{id}")
        Call<List<Respuesta>> getRespuestasDeConsulta(@Header("Authorization")String token,@Path("id") int id);


        @GET("Consultas/ObtenerTodas")
        Call<List<Consulta>> obtenerConsultas(@Header("Authorization")String token);

        @FormUrlEncoded
        @POST("Consultas/ObtenerPorTitulo")
        Call<List<Consulta>> obtenerPorTitulo(@Header("Authorization")String token, @Field("busqueda") String busqueda);
        @POST("Respuestas/Crear")
        Call<Respuesta> crearRespuesta(@Header("Authorization")String token,@Body Respuesta respuesta);
    }
}
