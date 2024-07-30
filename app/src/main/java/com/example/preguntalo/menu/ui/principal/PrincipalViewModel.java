package com.example.preguntalo.menu.ui.principal;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.preguntalo.Modelo.Categoria;
import com.example.preguntalo.Modelo.Consulta;
import com.example.preguntalo.Modelo.Usuario;
import com.example.preguntalo.Request.ApiClientRetrofit;
import com.example.preguntalo.login.LoginActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.io.File;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrincipalViewModel extends AndroidViewModel {

    private Context context;
    private MutableLiveData<List<Consulta>> mutableConsultas;
    private MutableLiveData<List<Categoria>> mutableCategorias;
    private MutableLiveData<String> mutableInforme;
    private MutableLiveData<String> mutableError;
    private MutableLiveData<Boolean> mutableDeslogueado;
    private MutableLiveData<Usuario> mutableUserData;
    private MutableLiveData<Bitmap> mutableBitmap;

    public PrincipalViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        mutableCategorias = new MutableLiveData<>();
        mutableConsultas = new MutableLiveData<>();
        mutableInforme = new MutableLiveData<>();
        mutableError = new MutableLiveData<>();
        mutableDeslogueado = new MutableLiveData<>();
        mutableUserData = new MutableLiveData<>();
        mutableBitmap = new MutableLiveData<>();
        getCategorias();
        getConsultas();
    }

    public MutableLiveData<List<Categoria>> getMutableCategorias() {
        return mutableCategorias;
    }

    public MutableLiveData<List<Consulta>> getMutableConsultas() {
        return mutableConsultas;
    }

    public MutableLiveData<String> getMutableInforme() {
        return mutableInforme;
    }

    public MutableLiveData<String> getMutableError() {
        return mutableError;
    }

    public MutableLiveData<Boolean> getMutableDeslogueado() {
        return mutableDeslogueado;
    }

    public MutableLiveData<Usuario> getMutableUserData() { return mutableUserData;  }

    public MutableLiveData<Bitmap> getMutableBitmap() { return mutableBitmap;   }

    public void getCategorias() {

        ApiClientRetrofit.EndPointPreguntalo end = ApiClientRetrofit.getEndPointPreguntalo();
        Call<List<Categoria>> call = end.obtenerCategorias(context.getSharedPreferences("token.xml", 0).getString("token", ""));

        call.enqueue(new Callback<List<Categoria>>() {
            @Override
            public void onResponse(Call<List<Categoria>> call, Response<List<Categoria>> response) {
                if (response.isSuccessful()) {
                    mutableCategorias.postValue(response.body());
                    mutableInforme.postValue("");
                } else {
                    Log.d("salida error: ", "al traer las categorias se obtuvo: " + response);
                }
            }

            @Override
            public void onFailure(Call<List<Categoria>> call, Throwable t) {

            }
        });
    }

    public boolean validarBusqueda(String busqueda) {
        return !busqueda.equals("");
    }

    public void buscarPorTitulo(String busqueda) {
        if (validarBusqueda(busqueda)) {
            ApiClientRetrofit.EndPointPreguntalo end = ApiClientRetrofit.getEndPointPreguntalo();
            Call<List<Consulta>> call = end.obtenerPorTitulo(context.getSharedPreferences("token.xml", 0).getString("token", ""), busqueda);

            call.enqueue(new Callback<List<Consulta>>() {
                @Override
                public void onResponse(Call<List<Consulta>> call, Response<List<Consulta>> response) {
                    if (response.isSuccessful()) {
                        List<Consulta> consultas = response.body();
                        mutableConsultas.postValue(consultas);
                        if (consultas.isEmpty()) {
                            mutableInforme.postValue("no se encontraron consultas con este criterio");
                        } else {
                            mutableInforme.postValue("se encontraron " + consultas.size() + " consultas");
                        }

                        //tendria que recargar el fragment?
                    } else {
                        Log.d("salida error: ", "al traer las consultas se obtuvo: " + response);
                    }
                }

                @Override
                public void onFailure(Call<List<Consulta>> call, Throwable t) {

                }
            });
        } else {
            //se toma como que quiere limpiar la busqueda y traer todos nuevamente;
            getConsultas();
        }
    }

    public void getConsultas() {
        ApiClientRetrofit.EndPointPreguntalo end = ApiClientRetrofit.getEndPointPreguntalo();
        Call<List<Consulta>> call = end.obtenerConsultas(context.getSharedPreferences("token.xml", 0).getString("token", ""));

        call.enqueue(new Callback<List<Consulta>>() {
            @Override
            public void onResponse(Call<List<Consulta>> call, Response<List<Consulta>> response) {
                if (response.isSuccessful()) {
                    mutableConsultas.postValue(response.body());
                    mutableInforme.postValue("");
                } else {
                    Log.d("salida error: ", "al traer las consultas se obtuvo: " + response);
                }
            }

            @Override
            public void onFailure(Call<List<Consulta>> call, Throwable t) {
                Log.d("salida error: ", "error en call de obtener consultas : " + t);
            }
        });
    }

    public void getMisConsultas() {
        ApiClientRetrofit.EndPointPreguntalo end = ApiClientRetrofit.getEndPointPreguntalo();
        Call<List<Consulta>> call = end.obtenerPorUsuario(context.getSharedPreferences("token.xml", 0).getString("token", ""));

        call.enqueue(new Callback<List<Consulta>>() {
            @Override
            public void onResponse(Call<List<Consulta>> call, Response<List<Consulta>> response) {
                if (response.isSuccessful()) {
                    List<Consulta> consultas = response.body();
                    mutableConsultas.postValue(consultas);
                    if (consultas.isEmpty()) {
                        mutableInforme.postValue("No tienes consultas aun");
                    }
                    mutableInforme.postValue("Tienes " + consultas.size() + " consultas creadas");
                } else {
                    Log.d("salida error: ", "al traer mis consultas se obtuvo: " + response);
                }
            }

            @Override
            public void onFailure(Call<List<Consulta>> call, Throwable t) {
                Log.d("salida error: ", "error en call de obtener mis consultas: " + t);
            }
        });
    }

    public void opcionMenu(CharSequence item, Context contextoFragment) {

        if (item.equals("Cerrar Sesion")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(contextoFragment);
            builder.setTitle("Cerrando Sesion");
            builder.setMessage("¿Deseas cerrar tu sesion?");
            builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //borrar token de sharedpreferences
                    SharedPreferences sp = context.getSharedPreferences("token.xml", 0);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("token", "");
                    editor.commit();
                    //cerrar actividad principal y abrir login
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    //cerrar posible sesion de google
                    GoogleSignInOptions gso = new GoogleSignInOptions
                            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .build();
                    GoogleSignInClient client = GoogleSignIn.getClient(context, gso);
                    if (client != null) {
                        client.signOut();
                    }
                    context.startActivity(intent);
                    Toast.makeText(context, "Se cerro tu sesion", Toast.LENGTH_SHORT).show();
                    mutableDeslogueado.setValue(true);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();

        }
        if (item.equals("Mis Consultas")) {
            Toast.makeText(context, "Seleccionaste tus consultas", Toast.LENGTH_SHORT).show();
            getMisConsultas();
        }
    }

    public void buscarPorCategoria(String categoria) {
        //en una llamada a la api, buscar las consultas que tengan la misma categoria
        //y mostrarlas en el recyclerview
        //en caso de no tener consultas informarlo por el mutableInforme
        ApiClientRetrofit.EndPointPreguntalo end = ApiClientRetrofit.getEndPointPreguntalo();
        Call<List<Consulta>> call = end.obtenerPorCategoria(context.getSharedPreferences("token.xml", 0).getString("token", ""), categoria);

        call.enqueue(new Callback<List<Consulta>>() {
            @Override
            public void onResponse(Call<List<Consulta>> call, Response<List<Consulta>> response) {
                if (response.isSuccessful()) {
                    mutableConsultas.postValue(response.body());
                    mutableInforme.postValue("Para " + categoria.toUpperCase() + " se encontro: ");
                    if (response.body().isEmpty()) {
                        mutableInforme.postValue("No hay consultas para la categoria " + categoria.toUpperCase());
                    }
                } else {
                    Log.d("salida error: ", "al traer las consultas se obtuvo: " + response);
                }
            }

            @Override
            public void onFailure(Call<List<Consulta>> call, Throwable t) {
                Log.d("salida error: ", "error en call de obtener por categoria: " + t);
            }
        });
    }

    public void obtenerDatosUsuario(){
        ApiClientRetrofit.EndPointPreguntalo end = ApiClientRetrofit.getEndPointPreguntalo();
        Call<Usuario> call = end.obtenerPerfil(context.getSharedPreferences("token.xml",0).getString("token",""));

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if(response.isSuccessful()){
                    mutableUserData.postValue(response.body());
                }else{
                    Log.d("salida error: ","al traer el perfil se obtuvo:"  + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {

            }
        });
    }

    public void cargarImagen(String fileName) {
        try{
            File archivo =new File((Environment.getExternalStorageDirectory()  + "/Preguntalo"),fileName);
            Bitmap imageBitmap= BitmapFactory.decodeFile(archivo.getAbsolutePath());
            if(imageBitmap!=null) {
                mutableBitmap.setValue(imageBitmap);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}