package com.example.preguntalo.registro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;

import com.example.preguntalo.Modelo.Usuario;
import com.example.preguntalo.R;
import com.example.preguntalo.databinding.ActivityRegistroBinding;

public class RegistroActivity extends AppCompatActivity {

    private ActivityRegistroBinding binding;
    private RegistroActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegistroBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(RegistroActivityViewModel.class);


        binding.btIniciaSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.btRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Usuario user = new Usuario();
                user.setEmail(binding.etEmailRegistro.getText().toString());
                user.setNombre(binding.etNombreRegistro.getText().toString());
                user.setApellido(binding.etApellidoRegistro.getText().toString());
                user.setDni(binding.etDNIRegistro.getText().toString());
                user.setPassword(binding.etPasswordRegistro.getText().toString());

                //limpiar los edit text seteados con error
                binding.tvErrorRegistro.setText("");
                binding.tvPasswordErrorRegistro.setText("");
                binding.etEmailRegistro.setError(null);
                binding.etNombreRegistro.setError(null);
                binding.etApellidoRegistro.setError(null);
                binding.etDNIRegistro.setError(null);
                binding.etPasswordRegistro.setError(null);
                binding.etRepetirRegistro.setError(null);

                viewModel.registrar(user,binding.etPasswordRegistro.getText().toString(),binding.etRepetirRegistro.getText().toString());

            }
        });

        viewModel.getMutablePasswordError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String error) {
                binding.tvPasswordErrorRegistro.setText(error);
                binding.etPasswordRegistro.setError("");
                binding.etRepetirRegistro.setError("");
            }
        });

        viewModel.getMutableRegistroError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String error) {
                binding.tvErrorRegistro.setText(error);
                binding.etEmailRegistro.setError("");
                binding.etNombreRegistro.setError("");
                binding.etApellidoRegistro.setError("");
                binding.etDNIRegistro.setError("");
            }
        });
        viewModel.getMutableEmailError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String error) {
                binding.tvErrorRegistro.setText(error);
                binding.etEmailRegistro.setError(error);
            }
        });
        setContentView(binding.getRoot());
    }
}