package com.example.preguntalo.menu.ui.principal;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.preguntalo.Modelo.Categoria;
import com.example.preguntalo.Modelo.Consulta;
import com.example.preguntalo.databinding.FragmentPrincipalBinding;
import com.example.preguntalo.menu.ui.consulta.CategoriaAdapter;

import java.util.List;

public class PrincipalFragment extends Fragment {

    private FragmentPrincipalBinding binding;

    public static PrincipalFragment newInstance() {
        return new PrincipalFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentPrincipalBinding.inflate(inflater, container, false);
        PrincipalViewModel viewModel = new ViewModelProvider(this).get(PrincipalViewModel.class);
        View root = binding.getRoot();

        GridLayoutManager grilla = new GridLayoutManager(getContext(),1,GridLayoutManager.HORIZONTAL,false);
        binding.rvCategorias.setLayoutManager(grilla);

        GridLayoutManager grilla2 = new GridLayoutManager(getContext(),1,GridLayoutManager.VERTICAL,false);
        binding.rvConsultas.setLayoutManager(grilla2);

        viewModel.getMutableCategorias().observe(getActivity(), new Observer<List<Categoria>>() {
            @Override
            public void onChanged(List<Categoria> categorias) {
                CategoriaAdapter adapter =new CategoriaAdapter(getContext(),categorias,getLayoutInflater());
                binding.rvCategorias.setAdapter(adapter);
            }
        });

        viewModel.getMutableConsultas().observe(getActivity(), new Observer<List<Consulta>>() {
            @Override
            public void onChanged(List<Consulta> consultas) {
                PrincipalAdapter adapter =new PrincipalAdapter(getContext(),consultas,getLayoutInflater());
                binding.rvConsultas.setAdapter(adapter);
            }
        });

        viewModel.getMutableInforme().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String informe) {
                binding.tvInforme.setText(informe);
            }
        });

        viewModel.getMutableError().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                binding.etConsulta.setError(error);
            }
        });

        binding.btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.buscarPorTitulo(binding.etConsulta.getText().toString());
            }
        });

        return root;
    }
}