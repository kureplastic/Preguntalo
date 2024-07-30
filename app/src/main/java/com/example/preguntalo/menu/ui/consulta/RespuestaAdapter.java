package com.example.preguntalo.menu.ui.consulta;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.preguntalo.Modelo.Rating;
import com.example.preguntalo.Modelo.Respuesta;
import com.example.preguntalo.Modelo.StringRespuesta;
import com.example.preguntalo.Modelo.Usuario;
import com.example.preguntalo.R;
import com.example.preguntalo.Request.ApiClientRetrofit;
import com.skydoves.powermenu.CircularEffect;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RespuestaAdapter extends RecyclerView.Adapter<RespuestaAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<Respuesta> comentarios;
    public RespuestaAdapter(Context context, List<Respuesta> respuestas, LayoutInflater inflater) {
        this.context = context;
        this.inflater = inflater;
        this.comentarios = respuestas;
    }
    @NonNull
    @Override
    public RespuestaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root =inflater.inflate(R.layout.item_comentario,parent,false);
        return new RespuestaAdapter.ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull RespuestaAdapter.ViewHolder holder, int position) {
        //por cada item hacer
        Respuesta comentarioActual = comentarios.get(position);
        holder.tvComentario.setText(comentarioActual.getTexto());
        //obtener el usuario
        Usuario usuario = comentarioActual.getUsuario();
        if(usuario != null) {
            if(usuario.getFotoPerfil() != null) {
                Glide.with(context).load(usuario.getFotoPerfil()).into(holder.imgPFPComentario);
            }
            //obtener el rating de la base
            ApiClientRetrofit.EndPointPreguntalo end = ApiClientRetrofit.getEndPointPreguntalo();
            Call<Rating> call = end.obtenerRatingDeUsuario(context.getSharedPreferences("token.xml",0).getString("token",""),usuario.getRatingId());
            call.enqueue(new Callback<Rating>() {
                @Override
                public void onResponse(Call<Rating> call, Response<Rating> response) {
                    //armar el powermenu para el icono de pfp
                    if(response.isSuccessful()){
                        Rating rating = response.body();
                        usuario.setRating(rating);
                        PowerMenu powerMenu = new PowerMenu.Builder(context)
                                .addItem(new PowerMenuItem("Nombre: " + usuario.getNombre() + " " + usuario.getApellido()))
                                .addItem(new PowerMenuItem("Email: " + usuario.getEmail()))
                                .addItem(new PowerMenuItem("Puntuacion General: " + usuario.getRating().getScore() + " pts."))
                                .setAnimation(MenuAnimation.SHOW_UP_CENTER)
                                .setCircularEffect(CircularEffect.BODY)
                                .setMenuShadow(10f)
                                .setTextSize(18)
                                .setMenuColor(ResourcesCompat.getColor(context.getResources(), R.color.___text_color, null))
                                .setTextColor(context.getResources().getColor(android.R.color.white))
                                .build();
                        holder.imgPFPComentario.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                powerMenu.showAtCenter(holder.imgPFPComentario);
                            }
                        });
                    } else {
                        Toast.makeText(context, "Error al obtener el rating del usuario", Toast.LENGTH_SHORT).show();
                        Log.d("salida error: ","al obtener rating se obtuvo: "  + response);
                    }
                }

                @Override
                public void onFailure(Call<Rating> call, Throwable t) {
                    Toast.makeText(context, "Error en call rating del usuario", Toast.LENGTH_SHORT).show();
                    Log.d("salida error: ","error en call de obtener rating : "  + t);
                }
            });
        }else{
            Toast.makeText(context, "Error al obtener el usuario", Toast.LENGTH_SHORT).show();
        }
        if(comentarioActual.getUsuarioId() == context.getSharedPreferences("userdata.xml",0).getInt("id",0)){
            holder.btEliminarComentario.setVisibility(View.VISIBLE);
        }

        holder.btEliminarComentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialogo para ver si estas seguro de eliminar el comentario
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Eliminar Comentario");
                builder.setMessage("¿Estas seguro que quiere eliminar su comentario?");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //eliminar el comentario
                        ApiClientRetrofit.EndPointPreguntalo end = ApiClientRetrofit.getEndPointPreguntalo();
                        Call<StringRespuesta> call = end.eliminarRespuesta(context.getSharedPreferences("token.xml",0).getString("token",""),comentarioActual.getId());
                        call.enqueue(new Callback<StringRespuesta>() {
                            @Override
                            public void onResponse(Call<StringRespuesta> call, Response<StringRespuesta> response) {
                                if(response.isSuccessful()){
                                    Toast.makeText(context, "Comentario eliminado", Toast.LENGTH_SHORT).show();
                                    //renavegar al frame
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("consulta",comentarioActual.getConsulta());
                                    try {
                                        Navigation.findNavController((Activity) context,R.id.nav_host_fragment_activity_main).navigate(R.id.navigation_Mostrar_Consulta,bundle);
                                    }catch (Exception e){
                                        Toast.makeText(context,"Error al volver",Toast.LENGTH_SHORT).show();
                                        Log.d("salida error: ","error al renavegar al frame : " + e);
                                    }

                                }else{
                                    Toast.makeText(context, "Error al eliminar comentario", Toast.LENGTH_SHORT).show();
                                    Log.d("salida error: ","al eliminar comentario se obtuvo: "  + response);
                                }
                            }
                            @Override
                            public void onFailure(Call<StringRespuesta> call, Throwable t) {
                                Toast.makeText(context, "Error en la call eliminar", Toast.LENGTH_SHORT).show();
                                Log.d("salida error: ","error en call de eliminar consulta : " + t);
                            }
                        });
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
    }

    @Override
    public int getItemCount() {
        return comentarios.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvComentario;
        ImageView imgPFPComentario;
        ImageButton btEliminarComentario;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvComentario = itemView.findViewById(R.id.tvComentario);
            imgPFPComentario = itemView.findViewById(R.id.imgPFPcomentario);
            btEliminarComentario = itemView.findViewById(R.id.btEliminarComentario);
        }
    }
}
