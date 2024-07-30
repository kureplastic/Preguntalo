package com.example.preguntalo.login;

import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;
import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.Navigation;

import com.example.preguntalo.Modelo.Usuario;
import com.example.preguntalo.R;
import com.example.preguntalo.Request.ApiClientRetrofit;
import com.example.preguntalo.menu.MenuActivity;
import com.example.preguntalo.menu.ui.principal.PrincipalFragment;
import com.example.preguntalo.registro.RegistroActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivityViewModel extends AndroidViewModel {

    private Context context;
    private MutableLiveData<String> mutableError;
    private MutableLiveData<Boolean> mutableLogueado;
    private GoogleSignInOptions gsOptions;
    private GoogleSignInClient gsClient;

    public LoginActivityViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        mutableError = new MutableLiveData<>();
        mutableLogueado = new MutableLiveData<>();
        gsOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsClient = GoogleSignIn.getClient(context, gsOptions);

    }

    private GoogleSignInAccount revisarCuentaLogueadaGoogle() {
        return GoogleSignIn.getLastSignedInAccount(context) ;
    }

    public MutableLiveData<String> getMutableError() {
        return mutableError;
    }

    public MutableLiveData<Boolean> getMutableLogueado() { return mutableLogueado; }

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
                            //guardar los datos del usuario en un shared preferences userdata.xml
                            ApiClientRetrofit.EndPointPreguntalo end = ApiClientRetrofit.getEndPointPreguntalo();
                            Call<Usuario> call2 = end.obtenerPerfil(context.getSharedPreferences("token.xml", 0).getString("token", ""));
                            call2.enqueue(new Callback<Usuario>() {
                                @Override
                                public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                                    if (response.isSuccessful()) {
                                        Log.d("salida: ", "al traer los datos del usuario se obtuvo: " + response);
                                        Usuario user = response.body();
                                        SharedPreferences sp = context.getSharedPreferences("userdata.xml", 0);
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.putString("email", user.getEmail());
                                        editor.putInt("id", user.getId());
                                        editor.apply();
                                    }else{
                                        Log.d("salida error: ", "al traer los datos del usuario se obtuvo: " + response);
                                    }
                                }

                                @Override
                                public void onFailure(Call<Usuario> call, Throwable t) {
                                    Log.d("salida error: ", "error en call de obtener perfil: " + t);
                                }
                            });

                            //abrir la actividad principal
                            Intent intent = new Intent(context, MenuActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                            mutableLogueado.setValue(true);
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

    public void registrarse() {
        Intent intent = new Intent(context, RegistroActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void iniciarConGoogle(GoogleSignInAccount cuenta) {
        Usuario usuario = new Usuario();
        usuario.setEmail(cuenta.getEmail());
        usuario.setPassword(cuenta.getId());
        usuario.setNombre(cuenta.getDisplayName().toString().split(" ")[0]);
        usuario.setApellido(cuenta.getDisplayName().toString().split(" ")[1]);
        usuario.setDni(cuenta.getId());
        //aqui tambien tenemos que guardar la foto de perfil

        //intenta registrar usuario
        ApiClientRetrofit.EndPointPreguntalo endpoint = ApiClientRetrofit.getEndPointPreguntalo();
        Call<Usuario> call = endpoint.registrar(usuario);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()) {
                    //luego de registrar el usuario, logueamos con las credenciales
                    ApiClientRetrofit.EndPointPreguntalo endpoint2 = ApiClientRetrofit.getEndPointPreguntalo();
                    Call<String> call2 = endpoint2.login(usuario);
                    call2.enqueue(new Callback<String>() {
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
                                    mutableLogueado.setValue(true);
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(context, "Error al intentar registrar cuenta desde google: " + t, Toast.LENGTH_SHORT).show();
                        }
                    });

                }else {
                    //el usuario ya estaba registrado por lo que solamente lo logueamos con las credenciales de google
                    ApiClientRetrofit.EndPointPreguntalo endpoint2 = ApiClientRetrofit.getEndPointPreguntalo();
                    Call<String> call2 = endpoint2.login(usuario);
                    call2.enqueue(new Callback<String>() {
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
                                    mutableLogueado.setValue(true);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(context, "Error al intentar loguear cuenta desde google: " + t, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(context, "Error al llamar registrar: " + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void revisarToken() {
        SharedPreferences sp = context.getSharedPreferences("token.xml", 0);
        if(sp.contains("token") && !sp.getString("token","").equals("")){
            //abrir la actividad principal
            Intent intent = new Intent(context, MenuActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            mutableLogueado.setValue(true);
        }
    }
}
