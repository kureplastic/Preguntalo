package com.example.preguntalo.menu.ui.consulta;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.preguntalo.Modelo.Categoria;
import com.example.preguntalo.Modelo.Consulta;
import com.example.preguntalo.R;
import com.example.preguntalo.databinding.FragmentConsultaBinding;

import java.util.List;

public class ConsultaFragment extends Fragment {

    private FragmentConsultaBinding binding;

    public static ConsultaFragment newInstance() {
        return new ConsultaFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentConsultaBinding.inflate(inflater, container, false);
        ConsultaViewModel viewModel = new ViewModelProvider(this).get(ConsultaViewModel.class);
        View root = binding.getRoot();

        GridLayoutManager grilla = new GridLayoutManager(getContext(),1,GridLayoutManager.HORIZONTAL,false);
        binding.rvCategorias.setLayoutManager(grilla);


        viewModel.getMutableCategorias().observe(getActivity(), new Observer<List<Categoria>>() {
            @Override
            public void onChanged(List<Categoria> categorias) {
                CategoriaAdapter adapter =new CategoriaAdapter(getContext(),categorias,getLayoutInflater());
                binding.rvCategorias.setAdapter(adapter);
            }
        });

        binding.btCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Consulta consulta = new Consulta();
                consulta.setTitulo(binding.editTitulo.getText().toString());
                consulta.setTexto(binding.editDescripcion.getText().toString());
                viewModel.crearConsulta(consulta);
            }
        });

        viewModel.getMutableError().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                binding.editTitulo.setError(error);
                binding.editDescripcion.setError(error);
            }
        });

        viewModel.getMutableConsulta().observe(getActivity(), new Observer<Consulta>() {
            @Override
            public void onChanged(Consulta consulta) {
                //enviar a fragment MostrarConsulta con consulta dentro de un bundle
                Log.d("salida consulta: ",consulta.toString());
                Bundle bundle = new Bundle();
                bundle.putSerializable("consulta",consulta);
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment_activity_main).navigate(R.id.navigation_Mostrar_Consulta,bundle);

            }
        });

        return root;
    }


}