package com.example.preguntalo.menu.ui.estadistica;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.preguntalo.Modelo.Rating;
import com.example.preguntalo.R;
import com.example.preguntalo.databinding.FragmentEstadisticaBinding;

public class EstadisticaFragment extends Fragment {
    private FragmentEstadisticaBinding binding;

    public static EstadisticaFragment newInstance() {
        return new EstadisticaFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        EstadisticaViewModel estadisticaViewModel = new ViewModelProvider(this).get(EstadisticaViewModel.class);
        binding = FragmentEstadisticaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        estadisticaViewModel.getMutableRating().observe(getActivity(), new Observer<Rating>() {
            @Override
            public void onChanged(Rating rating) {
                binding.holderComentP.setText(rating.getPuntuacionPositiva()+"");
                binding.holderRespR.setText(rating.getRespuestasContestadas()+"");
                binding.holderConsultasR.setText(rating.getConsultasRealizadas()+"");
                binding.holderRespS.setText(rating.getRespuestasElegidas()+"");
            }
        });

        estadisticaViewModel.getMutableScore().observe(getActivity(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.tvPuntuacion.setText(aDouble.toString());
            }
        });

        return root;
    }
}