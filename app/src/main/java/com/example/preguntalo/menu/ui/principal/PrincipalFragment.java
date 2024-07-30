package com.example.preguntalo.menu.ui.principal;

import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.preguntalo.Modelo.Categoria;
import com.example.preguntalo.Modelo.Consulta;
import com.example.preguntalo.Modelo.SharedViewModel;
import com.example.preguntalo.Modelo.Usuario;
import com.example.preguntalo.R;
import com.example.preguntalo.databinding.FragmentPrincipalBinding;
import com.example.preguntalo.menu.ui.consulta.CategoriaAdapter;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import java.util.List;

public class PrincipalFragment extends Fragment {

    private FragmentPrincipalBinding binding;
    private SharedViewModel sharedViewModel;

    public static PrincipalFragment newInstance() {
        return new PrincipalFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentPrincipalBinding.inflate(inflater, container, false);
        PrincipalViewModel viewModel = new ViewModelProvider(this).get(PrincipalViewModel.class);
        View root = binding.getRoot();
        viewModel.obtenerDatosUsuario();


        //sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        sharedViewModel.getCategoriaSeleccionada().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String categoria) {
                if(categoria != null) {
                    viewModel.buscarPorCategoria(categoria);
                    sharedViewModel.resetearCategoria();
                }
            }
        });

        //crear el menu
        PowerMenu powerMenu = new PowerMenu.Builder(getContext())
                .addItem(new PowerMenuItem("Mis Consultas",false))
                .addItem(new PowerMenuItem("Cerrar Sesion",false))
                .setAnimation(MenuAnimation.SHOW_UP_CENTER)
                .setMenuRadius(10f)
                .setMenuShadow(10f)
                .setTextSize(22)
                .setMenuColor(ResourcesCompat.getColor(getResources(), R.color._bg__boton_iniciar_sesion_color, null))
                .setTextColor(getResources().getColor(android.R.color.white))
                .setSelectedTextColor(getResources().getColor(android.R.color.black))
                .build();
        //funcion del menu
        OnMenuItemClickListener<PowerMenuItem> onMenuItemClickListener = (position, item) -> {
            powerMenu.setSelectedPosition(position); // cambia la seleccion
            //aca hacer accion
            viewModel.opcionMenu(item.title,getContext());
            powerMenu.dismiss();
        };
        //setea la funcion
        powerMenu.setOnMenuItemClickListener(onMenuItemClickListener);

        viewModel.getMutableUserData().observe(getActivity(), new Observer<Usuario>() {
            @Override
            public void onChanged(Usuario usuario) {
                viewModel.cargarImagen(usuario.getFotoPerfil());
                binding.tvNombreUsuario.setText(usuario.getNombre());
                if (usuario.getFotoPerfil() != null) {
                    //Glide.with(getContext()).load(usuario.getFotoPerfil()).into(binding.btMenuUsuario);
                }
            }
        });

        binding.btMenuUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                powerMenu.showAsDropDown(binding.btMenuUsuario);
            }
        });

        GridLayoutManager grilla = new GridLayoutManager(getContext(),1,GridLayoutManager.HORIZONTAL,false);
        binding.rvCategorias.setLayoutManager(grilla);

        GridLayoutManager grilla2 = new GridLayoutManager(getContext(),1,GridLayoutManager.VERTICAL,false);
        binding.rvConsultas.setLayoutManager(grilla2);

        viewModel.getMutableCategorias().observe(getActivity(), new Observer<List<Categoria>>() {
            @Override
            public void onChanged(List<Categoria> categorias) {
                CategoriaAdapter adapter =new CategoriaAdapter(getContext(),categorias,getLayoutInflater(),sharedViewModel);
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

        viewModel.getMutableDeslogueado().observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean deslogueado) {
                if(deslogueado){
                    getActivity().finish();
                }
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

    @Override
    public void onResume() {
        super.onResume();
        sharedViewModel.resetearCategoria();
    }

    @Override
    public void onPause() {
        super.onPause();
        sharedViewModel.resetearCategoria();
    }
}