package com.example.preguntalo.menu.ui.perfil;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.preguntalo.Modelo.Validacion;
import com.example.preguntalo.R;
import com.example.preguntalo.databinding.FragmentNuevaProfesionBinding;

public class NuevaProfesionFragment extends Fragment {

    private FragmentNuevaProfesionBinding binding;
    private NuevaProfesionViewModel profesionVModel;

    public static NuevaProfesionFragment newInstance() {
        return new NuevaProfesionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        NuevaProfesionViewModel mViewModel = new ViewModelProvider(this).get(NuevaProfesionViewModel.class);
        binding = FragmentNuevaProfesionBinding.inflate(inflater,container,false);
        profesionVModel = new ViewModelProvider(this).get(NuevaProfesionViewModel.class);
        View root = binding.getRoot();


        binding.btPublicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validacion validacionAEnviar = new Validacion();
                validacionAEnviar.setTitulo(binding.etTitulo.getText().toString());
                validacionAEnviar.setEntidadOtorgante(binding.etEntidadOtorgante.getText().toString());
                validacionAEnviar.setDescripcion(binding.etDescripcion.getText().toString());
                profesionVModel.crearValidacion(validacionAEnviar);
            }
        });
        profesionVModel.getMutableError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                binding.etTitulo.setError(error);
                binding.etEntidadOtorgante.setError(error);
                binding.etDescripcion.setError(error);
            }
        });

        profesionVModel.getMutableValidacion().observe(getViewLifecycleOwner(), new Observer<Validacion>() {
            @Override
            public void onChanged(Validacion validacion) {
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment_activity_main).navigate(R.id.navigation_perfil);
                Toast.makeText(getContext(), "Profesion agregada", Toast.LENGTH_SHORT).show();
            }
        });


        return root;
    }

}