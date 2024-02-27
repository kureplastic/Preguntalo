package com.example.preguntalo.menu.ui.perfil;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.preguntalo.Modelo.PassView;
import com.example.preguntalo.Modelo.Usuario;
import com.example.preguntalo.R;
import com.example.preguntalo.databinding.FragmentPerfilBinding;

public class PerfilFragment extends Fragment {

    private FragmentPerfilBinding binding;

    public static PerfilFragment newInstance() {
        return new PerfilFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        PerfilViewModel perfilViewModel = new ViewModelProvider(this).get(PerfilViewModel.class);
        binding = FragmentPerfilBinding.inflate(inflater,container,false);
        View root = binding.getRoot();

        perfilViewModel.getMutableUsuario().observe(getActivity(), new Observer<Usuario>() {
            @Override
            public void onChanged(Usuario usuario) {
                binding.etApellido.setText(usuario.getApellido());
                binding.etNombre.setText(usuario.getNombre());
                binding.etDNI.setText(usuario.getDni());
            }
        });

        perfilViewModel.getMutableErrorDatos().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                binding.tvErrorDatos.setText(error);
                binding.etApellido.setError(error);
                binding.etDNI.setError(error);
                binding.etNombre.setError(error);
            }
        });

        perfilViewModel.getMutableErrorPass().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                binding.tvErrorPass.setText(error);
                binding.etPass.setError(error);
                binding.etNewPass.setError(error);
                binding.etConfirmPass.setError(error);
            }
        });

        binding.btEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarEstadoDatos("Guardar",true);
                //luego agregar un listener al mismo boton para guardar los datos modificados
                binding.btEditar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Usuario usuario = new Usuario();
                        usuario.setApellido(binding.etApellido.getText().toString());
                        usuario.setNombre(binding.etNombre.getText().toString());
                        usuario.setDni(binding.etDNI.getText().toString());
                        usuario.setEmail("original");
                        usuario.setPassword("original");

                        perfilViewModel.editar(usuario);
                        cambiarEstadoDatos("Editar",false);
                    }
                });
            }
        });

        binding.btCambiarPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PassView passView = new PassView(
                        binding.etPass.getText().toString(),
                        binding.etNewPass.getText().toString(),
                        binding.etConfirmPass.getText().toString()
                );
                perfilViewModel.editarPassword(passView);
                binding.etPass.setText("");
                binding.etNewPass.setText("");
                binding.etConfirmPass.setText("");
            }
        });

        binding.btAgregarProfesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //abrir fragment nueva_profesion
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main).navigate(R.id.navigation_profesion);
            }
        });




        return root;
    }

    private void cambiarEstadoDatos(String textoBoton, boolean estado){
        binding.btEditar.setText(textoBoton);
        binding.etApellido.setEnabled(estado);
        binding.etDNI.setEnabled(estado);
        binding.etNombre.setEnabled(estado);
    }
}