package com.example.preguntalo.menu.ui.consulta;

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
import android.widget.Toast;

import com.example.preguntalo.Modelo.Consulta;
import com.example.preguntalo.Modelo.Respuesta;
import com.example.preguntalo.R;
import com.example.preguntalo.databinding.FragmentMostrarConsultaBinding;

import java.util.List;

public class MostrarConsultaFragment extends Fragment {


    private FragmentMostrarConsultaBinding binding;

    public static MostrarConsultaFragment newInstance() {
        return new MostrarConsultaFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMostrarConsultaBinding.inflate(inflater, container, false);
        MostrarConsultaViewModel mViewModel = new ViewModelProvider(this).get(MostrarConsultaViewModel.class);
        View root = binding.getRoot();

        GridLayoutManager grilla = new GridLayoutManager(getContext(),1,GridLayoutManager.VERTICAL,false);
        binding.rvRespuestas.setLayoutManager(grilla);

        Bundle recuperar = getArguments();
        mViewModel.llenarConsulta((Consulta)recuperar.getSerializable("consulta"));

        mViewModel.getMutableConsulta().observe(getViewLifecycleOwner(), new Observer<Consulta>() {
            @Override
            public void onChanged(Consulta consulta) {
                binding.tvTituloConsulta.setText(consulta.getTitulo());
                binding.tvContenido.setText(consulta.getTexto());
                binding.tvUpvote.setText(consulta.getPuntuacionPositiva() + "");
                binding.tvDownVote.setText(consulta.getPuntuacionNegativa() + "");
            }
        });

        mViewModel.getMutableRespuestas().observe(getViewLifecycleOwner(), new Observer<List<Respuesta>>() {
            @Override
            public void onChanged(List<Respuesta> respuestas) {
                MostrarConsultaAdapter adapter =new MostrarConsultaAdapter(getContext(),respuestas,getLayoutInflater());
                binding.rvRespuestas.setAdapter(adapter);
            }
        });

        mViewModel.getMutableError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                binding.etResponde.setError(error);
            }
        });

        binding.btResponde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.publicarRespuesta(binding.etResponde.getText().toString());
                Toast.makeText(getContext(), "Consulta publicada", Toast.LENGTH_SHORT).show();
                binding.etResponde.setText("");
            }
        });

        binding.btUpVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.upvote((Consulta)recuperar.getSerializable("consulta"));
            }
        });

        binding.btDownvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.downvote((Consulta)recuperar.getSerializable("consulta"));
            }
        });


        return root;
    }



}