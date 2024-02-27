package com.example.preguntalo.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;

import com.example.preguntalo.R;
import com.example.preguntalo.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private LoginActivityViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(LoginActivityViewModel.class);

        binding.btIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.etEmail.getText().toString();
                String password = binding.etPassword.getText().toString();
                viewModel.login(email, password);
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
        setContentView(binding.getRoot());
    }
}