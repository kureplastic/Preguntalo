package com.example.preguntalo.login;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.credentials.GetCredentialRequest;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.preguntalo.databinding.ActivityLoginBinding;
import com.example.preguntalo.menu.MenuActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.identity.googleid.GetGoogleIdOption;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private LoginActivityViewModel viewModel;

    GoogleSignInOptions gsOptions;
    GoogleSignInClient gsClient;
    private static final String WEB_CLIENT_ID = "4256910865-38ugcrvnepe10ajjaeceb86rs6dsv1pm.apps.googleusercontent.com";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(LoginActivityViewModel.class);
        //revisar si ya existe un usuario logueado
        viewModel.revisarToken();


        binding.btIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.etEmail.getText().toString();
                String password = binding.etPassword.getText().toString();
                viewModel.login(email, password);
            }
        });
        binding.btFormRegistrar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                viewModel.registrarse();
            }
        });
        viewModel.getMutableError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String error) {
                binding.tvError.setText(error);
                binding.etEmail.setError(error);
                binding.etPassword.setError(error);
            }
        });
        viewModel.getMutableLogueado().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean logueado) {
                if(logueado){
                    finish();
                }
            }
        });


        gsOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsClient = GoogleSignIn.getClient(this, gsOptions);



        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode()== Activity.RESULT_OK){
                            Intent data = result.getData();
                            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                            try {
                                task.getResult(ApiException.class);
                                //pasar la cuenta a iniciarConGoogle()
                                if(task.getResult().getEmail() != null){
                                    viewModel.iniciarConGoogle(task.getResult());
                                }
                                //Toast.makeText(LoginActivity.this, "Se Inicio Sesion con la cuenta: " + task.getResult().getDisplayName(), Toast.LENGTH_SHORT).show();
                            }catch (ApiException e){
                                Toast.makeText(LoginActivity.this, "Algo paso mal", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

        binding.btIniciaGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent SignInIintent = gsClient.getSignInIntent();
                activityResultLauncher.launch(SignInIintent);
            }
        });

        setContentView(binding.getRoot());
    }
}