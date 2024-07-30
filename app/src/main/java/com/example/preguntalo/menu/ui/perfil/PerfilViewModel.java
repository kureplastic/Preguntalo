package com.example.preguntalo.menu.ui.perfil;

import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;

import android.app.AlertDialog;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.preguntalo.Modelo.DataClass;
import com.example.preguntalo.Modelo.PassView;
import com.example.preguntalo.Modelo.Usuario;
import com.example.preguntalo.Modelo.Validacion;
import com.example.preguntalo.Request.ApiClientRetrofit;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilViewModel extends AndroidViewModel {

    private Context context;
    private MutableLiveData<Usuario> mutableUsuario;
    private MutableLiveData<String> mutableErrorDatos;
    private MutableLiveData<String> mutableErrorPass;
    private MutableLiveData<Uri> mutableImagenPerfilUri;
    private MutableLiveData<Bitmap> mutableBitmap;
    private MutableLiveData<Boolean> mutableExisteValidacion;
    private MutableLiveData<Validacion> mutableValidacion;
    private MutableLiveData<Boolean> mutableProgressBar;
    private MutableLiveData<Boolean> mutableRenavegar;
    private MutableLiveData<Boolean> mutableVerificarImagen;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;

    public PerfilViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        mutableUsuario = new MutableLiveData<>();
        mutableErrorDatos = new MutableLiveData<>();
        mutableErrorPass = new MutableLiveData<>();
        mutableImagenPerfilUri = new MutableLiveData<>();
        mutableBitmap = new MutableLiveData<>();
        mutableExisteValidacion = new MutableLiveData<>();
        mutableValidacion = new MutableLiveData<>();
        mutableProgressBar = new MutableLiveData<>();
        mutableRenavegar = new MutableLiveData<>();
        mutableVerificarImagen = new MutableLiveData<>();
        //llenar campos de usuario
        llenarCampos();
    }
    public MutableLiveData<Usuario> getMutableUsuario() {
        return mutableUsuario;
    }
    public MutableLiveData<String> getMutableErrorDatos() { return mutableErrorDatos; }
    public MutableLiveData<String> getMutableErrorPass() { return mutableErrorPass; }
    public MutableLiveData<Uri> getMutableImagenPerfilUri() { return mutableImagenPerfilUri; }
    public void setMutableImagenPerfilUri(Uri uri) { mutableImagenPerfilUri.setValue(uri);  }
    public MutableLiveData<Bitmap> getMutableBitmap(){return mutableBitmap;}
    public MutableLiveData<Boolean> getMutableExisteValidacion() { return mutableExisteValidacion; }
    public MutableLiveData<Validacion> getMutableValidacion() { return mutableValidacion; }
    public MutableLiveData<Boolean> getMutableProgressBar() { return mutableProgressBar; }
    public MutableLiveData<Boolean> getMutableRenavegar() { return mutableRenavegar; }
    public MutableLiveData<Boolean> getMutableVerificarImagen() { return mutableVerificarImagen; }


    public void guardarFotoPerfil(String path) {
        ApiClientRetrofit.EndPointPreguntalo end = ApiClientRetrofit.getEndPointPreguntalo();
        Call<Usuario> call = end.editarFotoPerfil(context.getSharedPreferences("token.xml",0).getString("token",""),path);

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if(response.isSuccessful()){
                    mutableUsuario.postValue(response.body());
                }else{
                    Log.d("salida error: ","al editarFotoPerfil se obtuvo:"  + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {

            }
        });
    }


    public void llenarCampos(){
        ApiClientRetrofit.EndPointPreguntalo end = ApiClientRetrofit.getEndPointPreguntalo();
        Call<Usuario> call = end.obtenerPerfil(context.getSharedPreferences("token.xml",0).getString("token",""));

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if(response.isSuccessful()){
                    mutableUsuario.postValue(response.body());
                    //corroborar validaciones
                    corroborarProfesion(response.body());
                }else{
                    Log.d("salida error: ","al traer el perfil se obtuvo:"  + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {

            }
        });
    }

    private boolean validarDatos(Usuario usuario){
        return !usuario.getNombre().equals("") &&
                !usuario.getApellido().equals("") &&
                !usuario.getDni().equals("");
    }

    private boolean validarPass(PassView passview){
        return !passview.password.equals("") &&
                !passview.newPassword.equals("") &&
                !passview.confirmPassword.equals("") &&
                passview.validarConfirmacionPassword();
    }

    public void editarUsuario(Usuario usuario) {
        //validar si los campos de usuario estan completos
        if(validarDatos(usuario)){
            Usuario usuarioAEnviar = mutableUsuario.getValue();
            usuarioAEnviar.setNombre(usuario.getNombre());
            usuarioAEnviar.setApellido(usuario.getApellido());
            usuarioAEnviar.setDni(usuario.getDni());
            //llamar a la api para editar el usuario
            ApiClientRetrofit.EndPointPreguntalo end = ApiClientRetrofit.getEndPointPreguntalo();
            Call<Usuario> usuarioCall = end.editarUsuario(context.getSharedPreferences("token.xml",0).getString("token",""),usuarioAEnviar);

            usuarioCall.enqueue(new Callback<Usuario>() {
                @Override
                public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                    if(response.isSuccessful()){
                        if(response.body() != null){
                            mutableUsuario.setValue(response.body());
                            Toast.makeText(context, "Usuario editado exitosamente", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(context, "Error al editar usuario", Toast.LENGTH_SHORT).show();
                            mutableErrorDatos.setValue("Error al intentar editar");
                            Log.d("salida error: ","al editar usuario se obtuvo: "  + response);
                        }
                    }else{
                        Toast.makeText(context, "Error al editar usuario", Toast.LENGTH_SHORT).show();
                        Log.d("salida error: ","al editar usuario se obtuvo: "  + response);
                    }
                }

                @Override
                public void onFailure(Call<Usuario> call, Throwable t) {
                    Toast.makeText(context, "Error en la call editar", Toast.LENGTH_SHORT).show();
                    Log.d("salida error: ","error en call de editar usuario : " + t);
                }
            });
        }else{
            mutableErrorDatos.setValue("Corrobore los campos");
        }
    }
    public void editarPassword(PassView passView){
        if(validarPass(passView)){
            ApiClientRetrofit.EndPointPreguntalo end = ApiClientRetrofit.getEndPointPreguntalo();
            Call<Usuario> call = end.editarPassword(context.getSharedPreferences("token.xml",0).getString("token",""), passView);

            call.enqueue(new Callback<Usuario>(){

                @Override
                public void onResponse(@NonNull Call<Usuario> call, @NonNull Response<Usuario> response) {

                    if(response.isSuccessful()){
                        mutableUsuario.postValue(response.body());
                        Toast.makeText(context, "Contraseña actualizada!", Toast.LENGTH_SHORT).show();
                    }else{
                        Log.d("salida error: ","al actualizar password se obtuvo: "  + response);
                        mutableErrorPass.postValue("Error al actualizar!");
                    }
                }

                @Override
                public void onFailure(Call<Usuario> call, Throwable t) {
                    mutableErrorPass.postValue("Error, Call OnFailure: " + t.getMessage());
                }
            });

        }else{
            mutableErrorPass.postValue("Corrobore los campos");
        }
    }

    private void corroborarProfesion(Usuario usuario){
        if(usuario.getValidacion() != null){
            mutableExisteValidacion.postValue(true);
            mutableValidacion.postValue(usuario.getValidacion());
        }
    }

    public void mostrarDialogoParaFoto(Context contexto, final PerfilFragment perfilFragment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
        builder.setTitle("Elija una imagen de perfil");
        builder.setItems(new CharSequence[]{"Elegir de la Galeria", "Cancelar"},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                /*
                                try {
                                    abrirCamara(contexto, perfilFragment);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                */
                                elegirDesdeGaleria(perfilFragment);
                                break;
                            case 1:
                                dialog.dismiss();
                                //elegirDesdeGaleria(perfilFragment);
                                break;
                            case 2:
                                dialog.dismiss();
                                break;
                        }
                    }
                });
        builder.show();
    }

    private void elegirDesdeGaleria(PerfilFragment perfilFragment) {
        Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        perfilFragment.startActivityForResult(pickPhotoIntent, REQUEST_IMAGE_PICK);
    }

    public void guardarImagenEnCarpeta(Context context, Uri imageUri) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
            if (inputStream != null) {
                File storageDir = new File(Environment.getExternalStorageDirectory()  + "/Preguntalo");
                if (!storageDir.exists()) {
                    storageDir.mkdirs();
                }
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "_";
                File file = new File(storageDir, imageFileName + ".jpg");
                OutputStream outputStream = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                outputStream.flush();
                outputStream.close();
                inputStream.close();
                guardarFotoPerfil(imageFileName + ".jpg");
                mutableImagenPerfilUri.setValue(Uri.fromFile(file));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void abrirCamara(Context contexto, PerfilFragment perfilFragment) throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(contexto.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = crearArchivoImagen();
            } catch (IOException ex) {
                Toast.makeText(context, "Error al guardar la foto", Toast.LENGTH_SHORT).show();
                throw new RuntimeException(ex);
            }
            if (photoFile != null) {
                // guardamos la imagen
                Uri photoUri = FileProvider.getUriForFile(contexto, "com.example.preguntalo.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                // inicio intent TakePictureIntent
                perfilFragment.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
    }
}

    private File crearArchivoImagen() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.getExternalStorageDirectory()  + "/Preguntalo");
        return File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
    }
    public void cargarImagen(String fileName) {
        try{
            File archivo =new File((Environment.getExternalStorageDirectory()  + "/Preguntalo"),fileName);
            Bitmap imageBitmap= BitmapFactory.decodeFile(archivo.getAbsolutePath());
            if(imageBitmap!=null) {
                mutableBitmap.setValue(imageBitmap);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    public void subirImagen(PerfilFragment perfilFragment) {
        Intent photoPicker = new Intent();
        photoPicker.setAction(Intent.ACTION_GET_CONTENT);
        photoPicker.setType("image/*");
        perfilFragment.startActivityForResult(photoPicker, REQUEST_IMAGE_PICK);

    }

    public void subirImagenAFirebase(DatabaseReference databaseReference, StorageReference storageReference) {
        if (mutableImagenPerfilUri.getValue() != null) {
            comenzarEnvio(mutableImagenPerfilUri.getValue(), databaseReference, storageReference);
        }else{
            Toast.makeText(context, "Por favor seleccione una imagen", Toast.LENGTH_SHORT).show();
        }
    }

    private void comenzarEnvio(Uri uri, DatabaseReference databaseReference, StorageReference storageReference) {
        String caption = "caption";
        final StorageReference imageReference = storageReference.child(System.currentTimeMillis() + "." + obtenerExtensionDeFile(uri));

        imageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        DataClass datos = new DataClass(uri.toString(),caption);
                        String key = databaseReference.push().getKey();
                        databaseReference.child(key).setValue(datos);
                        //aca guardar en base de datos mysql el uri como string
                        mutableProgressBar.setValue(false);
                        guardarFotoPerfil(uri.toString());
                        Toast.makeText(context, "Imagen guardada exitosamente", Toast.LENGTH_SHORT).show();
                        mutableRenavegar.setValue(true);
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                mutableProgressBar.setValue(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mutableProgressBar.setValue(false);
                Toast.makeText(context, "Error al subir imagen", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String obtenerExtensionDeFile(Uri fileUri) {
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(fileUri));
    }

    public void verificarImagen(String fotoPerfil) {
        if (fotoPerfil != null) {
            mutableVerificarImagen.setValue(true);
        }
    }
}