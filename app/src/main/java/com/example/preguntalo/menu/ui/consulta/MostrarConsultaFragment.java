package com.example.preguntalo.menu.ui.consulta;

import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.preguntalo.Modelo.Consulta;
import com.example.preguntalo.Modelo.Respuesta;
import com.example.preguntalo.Modelo.Usuario;
import com.example.preguntalo.R;
import com.example.preguntalo.databinding.FragmentMostrarConsultaBinding;
import com.skydoves.powermenu.CircularEffect;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import java.util.List;

public class MostrarConsultaFragment extends Fragment {


    private FragmentMostrarConsultaBinding binding;
    private NavController navController;

    public static MostrarConsultaFragment newInstance() {
        return new MostrarConsultaFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMostrarConsultaBinding.inflate(inflater, container, false);
        MostrarConsultaViewModel mViewModel = new ViewModelProvider(this).get(MostrarConsultaViewModel.class);
        View root = binding.getRoot();
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);

        GridLayoutManager grilla = new GridLayoutManager(getContext(),1,GridLayoutManager.VERTICAL,false);
        binding.rvRespuestas.setLayoutManager(grilla);

        Bundle recuperar = getArguments();
        Consulta consultaEntrante = (Consulta)recuperar.getSerializable("consulta");
        mViewModel.llenarConsulta(consultaEntrante);
        mViewModel.CorroborarUsuarioPropietario(consultaEntrante);
        mViewModel.obtenerUsuario(consultaEntrante);
        binding.btImagenGuardada.setVisibility(View.INVISIBLE);

        mViewModel.getMutableUsuarioPropietarioDeConsulta().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean propietarioDeConsulta) {
                if(propietarioDeConsulta){
                    binding.btEditarConsulta.setVisibility(View.VISIBLE);
                    binding.btEliminarConsulta.setVisibility(View.VISIBLE);
                }else{
                    binding.btEditarConsulta.setVisibility(View.GONE);
                    binding.btEliminarConsulta.setVisibility(View.GONE);
                }
            }
        });

        binding.btEditarConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.btEditarConsulta.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.icon_success, null));
                //tambien hacer editable el titulo y el texto
                binding.etTituloConsulta.setVisibility(View.VISIBLE);
                binding.etTituloConsulta.setText(binding.tvTituloConsulta.getText());
                binding.etContenidoConsulta.setVisibility(View.VISIBLE);
                binding.etContenidoConsulta.setText(binding.tvContenido.getText());
                binding.containerTexto.setVisibility(View.INVISIBLE);
                binding.btEditarConsulta.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //aca llamamos a editar la consulta
                        Consulta consultaEditada = consultaEntrante;
                        consultaEditada.setTitulo(binding.etTituloConsulta.getText().toString());
                        consultaEditada.setTexto(binding.etContenidoConsulta.getText().toString());
                        mViewModel.editarConsulta(consultaEditada);
                        binding.btEditarConsulta.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.icon_ek311negro, null));
                        binding.etContenidoConsulta.setVisibility(View.INVISIBLE);
                        binding.etTituloConsulta.setVisibility(View.INVISIBLE);
                        binding.containerTexto.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        binding.btEliminarConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialogo para confirmar eliminar publicacion
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Eliminar Consulta");
                builder.setMessage("¿Estas seguro que quieres eliminar esta consulta?");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //aca eliminamos la consulta
                        mViewModel.eliminarConsulta(consultaEntrante);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });

        mViewModel.getMutableErrorConsulta().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.etContenidoConsulta.setError(s);
                binding.etTituloConsulta.setError(s);
            }
        });


        mViewModel.getMutableUsuarioObtenido().observe(getViewLifecycleOwner(), new Observer<Usuario>() {
            @Override
            public void onChanged(Usuario usuario) {
                if (usuario != null) {
                    if(usuario.getFotoPerfil() != null) {
                        Glide.with(getContext())
                                .load(usuario.getFotoPerfil())
                                .into(binding.imgPerfil);
                    }
                    //powermenu para el usuario de la consulta

                    PowerMenu powerMenu = new PowerMenu.Builder(getContext())
                            .addItem(new PowerMenuItem("Nombre: " + usuario.getNombre() + " " + usuario.getApellido()))
                            .addItem(new PowerMenuItem("Email: " + usuario.getEmail()))
                            .addItem(new PowerMenuItem("Puntuacion General: " + usuario.getRating().getScore() + " pts."))
                            .setAnimation(MenuAnimation.SHOW_UP_CENTER)
                            .setCircularEffect(CircularEffect.BODY)
                            .setMenuShadow(10f)
                            .setTextSize(18)
                            .setMenuColor(ResourcesCompat.getColor(getResources(), R.color.___text_color, null))
                            .setTextColor(getResources().getColor(android.R.color.white))
                            .build();

                    binding.imgPerfil.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            powerMenu.showAtCenter(binding.imgPerfil);
                        }
                    });
                }
            }
        });


        mViewModel.getMutableConsulta().observe(getViewLifecycleOwner(), new Observer<Consulta>() {
            @Override
            public void onChanged(Consulta consulta) {
                binding.tvTituloConsulta.setText(consulta.getTitulo());
                binding.tvContenido.setText(consulta.getTexto());
                binding.tvUpvoteConsulta.setText(consulta.getPuntuacionPositiva() + "");
                binding.tvDownVoteConsulta.setText(consulta.getPuntuacionNegativa() + "");
            }
        });

        mViewModel.getMutableRespuestas().observe(getViewLifecycleOwner(), new Observer<List<Respuesta>>() {
            @Override
            public void onChanged(List<Respuesta> respuestas) {
                mViewModel.getMutableComentarios().observe(getViewLifecycleOwner(), new Observer<List<Respuesta>>() {
                    @Override
                    public void onChanged(List<Respuesta> comentarios) {
                        MostrarConsultaAdapter adapter =new MostrarConsultaAdapter(getContext(),respuestas,comentarios,getLayoutInflater());
                        binding.rvRespuestas.setAdapter(adapter);
                    }
                });
            }
        });

        mViewModel.getMutableError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                binding.etResponde.setError(error);
            }
        });

        mViewModel.getMutableRenavegar().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean renavegar) {
                if(renavegar != null && renavegar){
                    Bundle nuevoBundle = new Bundle();
                    nuevoBundle.putSerializable("consulta",consultaEntrante);
                    navController.navigate(R.id.navigation_Mostrar_Consulta,nuevoBundle);
                    mViewModel.reiniciarMutableNavegacion();
                }
            }
        });

        mViewModel.getMutableConsultaEliminada().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean != null && aBoolean){
                    navController.navigate(R.id.navigation_vista_principal);
                }
            }
        });

        binding.btResponde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.publicarRespuesta(binding.etResponde.getText().toString());
                binding.etResponde.setText("");
            }
        });

        binding.btUpVoteConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.upvote(consultaEntrante);
            }
        });

        binding.btDownvoteConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.downvote(consultaEntrante);
            }
        });


        return root;
    }



}