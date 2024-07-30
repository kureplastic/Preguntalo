package com.example.preguntalo.Request;

import com.example.preguntalo.Modelo.Categoria;
import com.example.preguntalo.Modelo.Consulta;
import com.example.preguntalo.Modelo.Consulta_Categoria;
import com.example.preguntalo.Modelo.PassView;
import com.example.preguntalo.Modelo.Puntuacion;
import com.example.preguntalo.Modelo.Rating;
import com.example.preguntalo.Modelo.Respuesta;
import com.example.preguntalo.Modelo.StringRespuesta;
import com.example.preguntalo.Modelo.Usuario;
import com.example.preguntalo.Modelo.Validacion;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
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
        @POST("Usuarios/registrar")
        Call<Usuario> registrar(@Body Usuario usuario);
        @GET("Usuarios/Perfil")
        Call<Usuario> obtenerPerfil(@Header("Authorization") String token);
        @PUT("Usuarios/Editar")
        Call<Usuario> editarUsuario(@Header("Authorization") String token, @Body Usuario usuario);
        @PUT("Usuarios/EditarPassword")
        Call<Usuario> editarPassword(@Header("Authorization") String token,@Body PassView passView);
        @PUT("Usuarios/EditarFotoPerfil")
        Call<Usuario> editarFotoPerfil(@Header("Authorization") String token, @Body String url);
        @POST("Usuarios/CrearValidacion")
        Call<Validacion> crearValidacion(@Header("Authorization") String token, @Body Validacion validacion);
        @GET("Ratings/Obtener")
        Call<Rating> obtenerRating(@Header("Authorization")String token);
        @GET("Ratings/Obtener/{id}")
        Call<Rating> obtenerRatingDeUsuario(@Header("Authorization")String token,@Path("id") int id);
        @POST("Ratings/CambiarPuntuacion")
        Call<StringRespuesta> cambiarPuntuacion(@Header("Authorization")String token, @Body Puntuacion puntuacion);
        @POST("Ratings/CambiarPuntuacionRespuesta")
        Call<StringRespuesta> cambiarPuntuacionRespuesta(@Header("Authorization")String token,@Body Puntuacion puntuacion);
        @GET("Categorias/Obtener")
        Call<List<Categoria>> obtenerCategorias(@Header("Authorization") String token);
        @POST("Consultas/Crear")
        Call<Consulta> crearConsulta(@Header("Authorization") String token,@Body Consulta consulta);
        @PUT("Consultas/Editar")
        Call<Consulta> editarConsulta(@Header("Authorization")String token,@Body Consulta consulta);
        @HTTP(method = "DELETE", hasBody = true, path = "Consultas/Eliminar")
        Call<StringRespuesta> eliminarConsulta(@Header("Authorization")String token,@Body Consulta consulta);
        @GET("Consultas/Obtener/{id}")
        Call<Consulta> getConsulta(@Header("Authorization")String token,@Path("id") int id);
        @GET("Respuestas/ObtenerDeConsulta/{id}")
        Call<List<Respuesta>> getRespuestasDeConsulta(@Header("Authorization")String token,@Path("id") int id);
        @GET("Consultas/ObtenerTodas")
        Call<List<Consulta>> obtenerConsultas(@Header("Authorization")String token);
        @FormUrlEncoded
        @POST("Consultas/ObtenerPorTitulo")
        Call<List<Consulta>> obtenerPorTitulo(@Header("Authorization")String token, @Field("busqueda") String busqueda);
        @POST("Consultas/ObtenerPorCategoria")
        Call<List<Consulta>> obtenerPorCategoria(@Header("Authorization")String token,@Body String categoria );
        @GET("Consultas/ObtenerPorUsuario")
        Call<List<Consulta>> obtenerPorUsuario(@Header("Authorization")String token);
        @POST("Consultas/ElegirRespuesta")
        Call<StringRespuesta> elegirRespuesta(@Header("Authorization")String token,@Body Respuesta respuesta);
        @FormUrlEncoded
        @POST("Consultas/CrearRelacionConsultaCategoria")
        Call<Consulta_Categoria> crearRelacion(@Header("Authorization")String token, @Field("nueva.Id") int id, @Field("nueva.Titulo") String titulo, @Field("nueva.Texto") String Texto, @Field("categoria") String categoria);
        @POST("Respuestas/Crear")
        Call<Respuesta> crearRespuesta(@Header("Authorization")String token,@Body Respuesta respuesta);

        @PUT("Respuestas/Editar")
        Call<Respuesta> editarRespuesta(@Header("Authorization") String token,@Body Respuesta respuesta);
        @DELETE("Respuestas/Eliminar/{id}")
        Call<StringRespuesta> eliminarRespuesta(@Header("Authorization")String token,@Path("id") int id);
    }
}
