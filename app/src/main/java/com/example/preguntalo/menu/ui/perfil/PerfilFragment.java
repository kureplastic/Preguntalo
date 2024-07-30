package com.example.preguntalo.menu.ui.perfil;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.preguntalo.Modelo.PassView;
import com.example.preguntalo.Modelo.Usuario;
import com.example.preguntalo.Modelo.Validacion;
import com.example.preguntalo.R;
import com.example.preguntalo.databinding.FragmentPerfilBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PerfilFragment extends Fragment {

    private FragmentPerfilBinding binding;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    private static final int REQUEST_PERMISSIONS = 3;

    public static PerfilFragment newInstance() {
        return new PerfilFragment();
    }
    PerfilViewModel perfilViewModel;
    final private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("images");
    final private StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        perfilViewModel = new ViewModelProvider(this).get(PerfilViewModel.class);
        binding = FragmentPerfilBinding.inflate(inflater,container,false);
        View root = binding.getRoot();

        perfilViewModel.getMutableUsuario().observe(getActivity(), new Observer<Usuario>() {
            @Override
            public void onChanged(Usuario usuario) {
                binding.etApellido.setText(usuario.getApellido());
                binding.etNombre.setText(usuario.getNombre());
                binding.etDNI.setText(usuario.getDni());
                perfilViewModel.verificarImagen(usuario.getFotoPerfil());
                if(usuario.getFotoPerfil()!=null){
                    //usamos glide para cargar la imagen del usuario
                    Glide
                            .with(getContext())
                            .load(usuario.getFotoPerfil())
                            .into(binding.imgFotoPerfil);
                }
                else{
                    binding.imgFotoPerfil.setImageDrawable(getResources().getDrawable(R.drawable.defaultpfp));
                }
                cambiarEstadoDatos("Editar",false);
                //intentamos cargar la imagen
                //perfilViewModel.cargarImagen(usuario.getFotoPerfil());
            }
        });

        perfilViewModel.getMutableErrorDatos().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                binding.tvErrorDatos.setText(error);
                binding.etApellido.setError(error);
                binding.etDNI.setError(error);
                binding.etNombre.setError(error);
            }
        });

        perfilViewModel.getMutableErrorPass().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                binding.tvErrorPass.setText(error);
                binding.etPass.setError(error);
                binding.etNewPass.setError(error);
                binding.etConfirmPass.setError(error);
            }
        });

        perfilViewModel.getMutableExisteValidacion().observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean existeValidacion) {
                if(existeValidacion){
                    binding.btAgregarProfesion.setVisibility(View.GONE);
                    binding.containerProfesion.setVisibility(View.VISIBLE);
                }
            }
        });

        perfilViewModel.getMutableValidacion().observe(getActivity(), new Observer<Validacion>() {
            @Override
            public void onChanged(Validacion validacion) {
                binding.tvTitulo.setText(validacion.getTitulo());
                binding.tvEntidadOtorgante.setText(validacion.getEntidadOtorgante());
                binding.tvDescripcion.setText(validacion.getDescripcion());
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

                        perfilViewModel.editarUsuario(usuario);
                        //
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

        binding.imgFotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                perfilViewModel.subirImagen(PerfilFragment.this);
            }
        });

        binding.btSubirImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                perfilViewModel.subirImagenAFirebase(databaseReference, storageReference);
            }
        });
        perfilViewModel.getMutableProgressBar().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean enProgreso) {
                if (enProgreso) {
                    binding.progressBar.setVisibility(View.VISIBLE);
                } else {
                    binding.progressBar.setVisibility(View.GONE);
                }
            }
        });

        binding.btEditarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //abrir opcion de subir imagen o tomar foto
                perfilViewModel.mostrarDialogoParaFoto(getContext(), PerfilFragment.this);
            }
        });

        perfilViewModel.getMutableImagenPerfilUri().observe(getActivity(), new Observer<Uri>() {
            @Override
            public void onChanged(Uri uri) {
                if (uri != null) {
                    binding.imgFotoPerfil.setImageURI(uri);
                }
            }
        });

        perfilViewModel.getMutableBitmap().observe(getActivity(), new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                if (bitmap != null) {
                    binding.imgFotoPerfil.setImageBitmap(bitmap);
                }
            }
        });
        perfilViewModel.getMutableRenavegar().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean renavegar) {
                if(renavegar){
                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main).navigate(R.id.navigation_perfil);
                }
            }
        });

        requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE},
                REQUEST_PERMISSIONS);

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE:
                    perfilViewModel.setMutableImagenPerfilUri(data.getData());
                    break;
                case REQUEST_IMAGE_PICK:
                    if (data != null) {
                        Uri photoUri = data.getData();
                        perfilViewModel.setMutableImagenPerfilUri(photoUri);
                        //esto es par guardar en base mysql
                        /*
                        Toast.makeText(getActivity(), "Se modifico la imagen de perfil", Toast.LENGTH_SHORT).show();
                        perfilViewModel.guardarImagenEnCarpeta(getContext(), photoUri);
                        */
                    }
                    break;
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(getActivity(), "Se necesitan permisos par acceder a la camara y almacenamiento.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void cambiarEstadoDatos(String textoBoton, boolean estado){
        binding.btEditar.setText(textoBoton);
        binding.etApellido.setEnabled(estado);
        binding.etDNI.setEnabled(estado);
        binding.etNombre.setEnabled(estado);
    }

}