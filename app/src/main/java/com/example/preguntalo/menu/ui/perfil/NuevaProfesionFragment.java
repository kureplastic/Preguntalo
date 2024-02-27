package com.example.preguntalo.menu.ui.perfil;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.preguntalo.R;
import com.example.preguntalo.databinding.FragmentNuevaProfesionBinding;

public class NuevaProfesionFragment extends Fragment {

    private FragmentNuevaProfesionBinding binding;

    public static NuevaProfesionFragment newInstance() {
        return new NuevaProfesionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        NuevaProfesionViewModel mViewModel = new ViewModelProvider(this).get(NuevaProfesionViewModel.class);
        binding = FragmentNuevaProfesionBinding.inflate(inflater,container,false);
        View root = binding.getRoot();
        return root;

    }

}