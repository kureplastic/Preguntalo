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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.preguntalo.Modelo.Consulta;
import com.example.preguntalo.Modelo.Puntuacion;
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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MostrarConsultaAdapter extends RecyclerView.Adapter<MostrarConsultaAdapter.ViewHolder>{
    private Context context;
    private LayoutInflater inflater;
    private List<Respuesta> respuestas;
    private List<Respuesta> comentarios;

    public MostrarConsultaAdapter(Context context, List<Respuesta> respuestas, List<Respuesta> comentarios, LayoutInflater inflater) {
        this.context = context;
        this.respuestas = respuestas;
        this.comentarios = comentarios;
        this.inflater = inflater;
    }
    @NonNull
    @Override
    public MostrarConsultaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root =inflater.inflate(R.layout.item_respuesta,parent,false);
        return new MostrarConsultaAdapter.ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull MostrarConsultaAdapter.ViewHolder holder, int position) {
        //obtener la Respuesta
        Respuesta respuestaActual = respuestas.get(position);

        //armar grilla para el recycler view de cada respuesta
        GridLayoutManager grilla = new GridLayoutManager(context,1,GridLayoutManager.VERTICAL,false);
        holder.rvComentarios.setLayoutManager(grilla);

        //recorrer los comentarios y compararlos con la respuesta actual
        // si los comentarios pertenecen a la respuesta guardarlos en el array que se va a pasar al adapter
        List<Respuesta> comentariosDeRespuesta = new ArrayList<>();
        for (Respuesta comentario : comentarios) {
            if(comentario.getRespuestaId() == respuestaActual.getId()){
                comentariosDeRespuesta.add(comentario);
            }
        }
        RespuestaAdapter adapter = new RespuestaAdapter(context,comentariosDeRespuesta,inflater);
        holder.rvComentarios.setAdapter(adapter);


        //por cada item hacer
        holder.tvRespuesta.setText(respuestaActual.getTexto());
        holder.tvUpVoteRespuesta.setText(respuestaActual.getPuntuacionPositiva() + "");
        holder.tvDownVoteRespuesta.setText(respuestaActual.getPuntuacionNegativa() + "");

        //corroborar si el usuario logueado es el mismo que creo la consulta
        if(context.getSharedPreferences("userdata.xml",0).getInt("id",0) == respuestaActual.getConsulta().getUsuarioId()){
            holder.btElegirRespuesta.setVisibility(View.VISIBLE);
        }
        //corroborar si el usuario logueado es el mismo que respondio
        if(context.getSharedPreferences("userdata.xml",0).getInt("id",0) == respuestaActual.getUsuarioId()){
            holder.btEditarRespuesta.setVisibility(View.VISIBLE);
        }
        //obtener la consulta y corroborar si tiene una respuesta elegida para compararla con la actual
        Consulta consultaActual = respuestaActual.getConsulta();
        if(consultaActual.getRespuestaSeleccionada() == respuestaActual.getId()){
            holder.containerRespuesta.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.rectangle_selected_answer, null));
        }
        //obtener el usuario que respondio
        Usuario usuarioDeRespuesta = respuestaActual.getUsuario();
        if (usuarioDeRespuesta != null) {
            //aca renderizamos la imagen del usuario la imagen del usuario
            if (usuarioDeRespuesta.getFotoPerfil() != null) {
                Glide.with(context).load(usuarioDeRespuesta.getFotoPerfil()).into(holder.imgFotoPerfilRespuesta);
            }
            //llamar al metodo para obtener el rating del usuario
            ApiClientRetrofit.EndPointPreguntalo end = ApiClientRetrofit.getEndPointPreguntalo();
            Call<Rating> call = end.obtenerRatingDeUsuario(context.getSharedPreferences("token.xml",0).getString("token",""),usuarioDeRespuesta.getRatingId());
            call.enqueue(new Callback<Rating>() {
                @Override
                public void onResponse(Call<Rating> call, Response<Rating> response) {
                    if(response.isSuccessful()){
                        Rating rating = response.body();
                        usuarioDeRespuesta.setRating(rating);
                        //armar el powermenu button
                        PowerMenu powerMenu = new PowerMenu.Builder(context)
                                .addItem(new PowerMenuItem("Nombre: " + usuarioDeRespuesta.getNombre() + " " + usuarioDeRespuesta.getApellido()))
                                .addItem(new PowerMenuItem("Email: " + usuarioDeRespuesta.getEmail()))
                                .addItem(new PowerMenuItem("Puntuacion General: " + usuarioDeRespuesta.getRating().getScore() + " pts."))
                                .setAnimation(MenuAnimation.SHOW_UP_CENTER)
                                .setCircularEffect(CircularEffect.BODY)
                                .setMenuShadow(10f)
                                .setTextSize(18)
                                .setMenuColor(ResourcesCompat.getColor(context.getResources(), R.color.___text_color, null))
                                .setTextColor(context.getResources().getColor(android.R.color.white))
                                .build();

                        holder.imgFotoPerfilRespuesta.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                powerMenu.showAtCenter(holder.imgFotoPerfilRespuesta);
                            }
                        });
                    }
                    else{
                        Log.d("salida error: ","al obtener rating se obtuvo: "  + response);
                        Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Rating> call, Throwable t) {
                    Toast.makeText(context, "Error al obtener rating", Toast.LENGTH_SHORT).show();
                    Log.d("salida error: ","error en call de obtener rating : "  + t);
                }
            });
        }
        holder.btResponderRespuesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //abrir el campo para responder
                holder.etComentar.setVisibility(View.VISIBLE);
                holder.btComentar.setVisibility(View.VISIBLE);

            }
        });
        holder.btUpVoteRespuesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                puntuar(respuestaActual, true);
            }
        });

        holder.btDownVoteRespuesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                puntuar(respuestaActual, false);
            }
        });

        holder.btElegirRespuesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Abrir un dialogo que diga si esta seguro de elegir esa respuesta como la mas util
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Elegir Respuesta");
                builder.setMessage("¿Estas seguro que quieres elegir esta respuesta como la mas util?");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //aca elegimos la respuesta
                        elegirRespuestaComoMasUtil(respuestaActual);
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
        holder.btComentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checkear si el campo etComentar no es vacio
                if(!holder.etComentar.getText().toString().equals("")){
                    //preparar la nueva respuesta con el id de la respuesta actual
                    Respuesta comentarioRespuesta = new Respuesta();
                    comentarioRespuesta.setTexto(holder.etComentar.getText().toString());
                    comentarioRespuesta.setConsulta(consultaActual);
                    comentarioRespuesta.setConsultaId(consultaActual.getId());
                    comentarioRespuesta.setRespuestaId(respuestaActual.getId());
                    comentarioRespuesta.setUsuarioId(context.getSharedPreferences("userdata.xml",0).getInt("id",0));

                    //llamar a la api para crear la nueva respuesta
                    ApiClientRetrofit.EndPointPreguntalo end = ApiClientRetrofit.getEndPointPreguntalo();
                    Call<Respuesta> call = end.crearRespuesta(context.getSharedPreferences("token.xml",0).getString("token",""),comentarioRespuesta);
                    call.enqueue(new Callback<Respuesta>() {
                        @Override
                        public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {
                            if(response.isSuccessful()){
                                Respuesta comentarioCreado = response.body();
                                Toast.makeText(context, "Comentario agregado", Toast.LENGTH_SHORT).show();
                                //navegar al mismo fragment con un bundle creado
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("consulta",comentarioCreado.getConsulta());
                                Navigation.findNavController((Activity) context,R.id.nav_host_fragment_activity_main).navigate(R.id.navigation_Mostrar_Consulta,bundle);

                            }else{
                                Toast.makeText(context, "ERROR al crear comentario", Toast.LENGTH_SHORT).show();
                                Log.d("salida error: ","al crear comentraio se obtuvo: "  + response);
                            }
                        }
                        @Override
                        public void onFailure(Call<Respuesta> call, Throwable t) {
                            Toast.makeText(context, "Error en la llamada crear comentario", Toast.LENGTH_SHORT).show();
                            Log.d("salida error: ","error en call de crear comentario : "  + t);
                        }
                    });

                }else {
                    holder.etComentar.setError("Ingrese su comentario");
                }
            }
        });

        holder.btEditarRespuesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.etEditarRespuesta.setVisibility(View.VISIBLE);
                holder.etEditarRespuesta.setText(holder.tvRespuesta.getText().toString());
                holder.btEditarRespuesta.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.icon_success, null));
                holder.tvRespuesta.setVisibility(View.INVISIBLE);
                holder.btEditarRespuesta.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //aca llamamos a la api para editar la respuesta
                        Respuesta respuestaAEnviar = respuestaActual;
                        respuestaAEnviar.setTexto(holder.etEditarRespuesta.getText().toString());
                        ApiClientRetrofit.EndPointPreguntalo end = ApiClientRetrofit.getEndPointPreguntalo();
                        Call<Respuesta> call = end.editarRespuesta(context.getSharedPreferences("token.xml",0).getString("token",""),respuestaAEnviar);
                        call.enqueue(new Callback<Respuesta>() {
                            @Override
                            public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {
                                if(response.isSuccessful()){
                                    Toast.makeText(context, "Respuesta editada correctamente", Toast.LENGTH_SHORT).show();
                                    holder.tvRespuesta.setText(response.body().getTexto());
                                    holder.tvRespuesta.setVisibility(View.VISIBLE);
                                    holder.etEditarRespuesta.setVisibility(View.GONE);
                                    holder.btEditarRespuesta.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.icon_ek311, null));
                                }
                                else {
                                    Toast.makeText(context, "Error al editar respuesta", Toast.LENGTH_SHORT).show();
                                    Log.d("salida error: ","error en call de editar respuesta : "  + response);
                                }
                            }
                            @Override
                            public void onFailure(Call<Respuesta> call, Throwable t) {
                                Toast.makeText(context, "Error en call editar respuesta", Toast.LENGTH_SHORT).show();
                                Log.d("salida error: ","error en call de editar respuesta : "  + t);
                            }
                        });
                    }
                });
            }
        });
    }
    @Override
    public int getItemCount() { return respuestas.size(); }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvRespuesta, tvUpVoteRespuesta, tvDownVoteRespuesta;
        EditText etComentar, etEditarRespuesta;
        ImageButton btResponderRespuesta, btUpVoteRespuesta, btDownVoteRespuesta, btComentar, btElegirRespuesta, btEditarRespuesta;
        ImageView imgFotoPerfilRespuesta;
        View containerRespuesta;
        RecyclerView rvComentarios;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRespuesta = itemView.findViewById(R.id.tvTextoConsulta);
            tvUpVoteRespuesta = itemView.findViewById(R.id.tvUpvoteRespuesta);
            tvDownVoteRespuesta = itemView.findViewById(R.id.tvDownvoteRespuesta);
            imgFotoPerfilRespuesta = itemView.findViewById(R.id.imgFotoPerfilRespuesta);
            btResponderRespuesta = itemView.findViewById(R.id.btResponderRespuesta);
            btUpVoteRespuesta = itemView.findViewById(R.id.btUpvoteRespueta);
            btDownVoteRespuesta = itemView.findViewById(R.id.btDownvoteConsulta);
            etComentar = itemView.findViewById(R.id.etComentar);
            btComentar = itemView.findViewById(R.id.btComentar);
            btElegirRespuesta = itemView.findViewById(R.id.btElegirRespuesta);
            btEditarRespuesta = itemView.findViewById(R.id.btEditarRespuesta);
            containerRespuesta = itemView.findViewById(R.id.container_respuesta);
            etEditarRespuesta = itemView.findViewById(R.id.etEditarRespuesta);
            rvComentarios = itemView.findViewById(R.id.rvComentarios);
        }
    }

    private void puntuar(Respuesta respuestaEntrante, Boolean valor){
        Puntuacion puntuacionAEnviar = new Puntuacion();
        int idUsuario = context.getSharedPreferences("userdata.xml",0).getInt("id",0);
        puntuacionAEnviar.setUsuarioId(idUsuario);
        puntuacionAEnviar.setRespuesta(respuestaEntrante);
        puntuacionAEnviar.setRespuestaId(respuestaEntrante.getId());
        puntuacionAEnviar.setPuntuacionPositiva(valor);
        //luego mandamos la puntuacion en una call a la api
        ApiClientRetrofit.EndPointPreguntalo end = ApiClientRetrofit.getEndPointPreguntalo();
        Call<StringRespuesta> call = end.cambiarPuntuacionRespuesta(context.getSharedPreferences("token.xml",0).getString("token",""),puntuacionAEnviar);
        call.enqueue(new Callback<StringRespuesta>() {
            @Override
            public void onResponse(Call<StringRespuesta> call, Response<StringRespuesta> response) {
                if(response.isSuccessful()){
                    //navegar al mismo fragment para que se actualize
                    Toast.makeText(context,response.body().getRespuesta(),Toast.LENGTH_SHORT).show();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("consulta",respuestaEntrante.getConsulta());
                    Navigation.findNavController((Activity) context,R.id.nav_host_fragment_activity_main).navigate(R.id.navigation_Mostrar_Consulta,bundle);
                }
                else{
                    Log.d("salida error: ","al cambiar puntuacion se obtuvo: "  + response);
                    Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<StringRespuesta> call, Throwable t) {
                Toast.makeText(context, "Error en llamada UPVOTE", Toast.LENGTH_SHORT).show();
                Log.d("salida error: ", "error en call de cambiar puntuacion : " + t);
            }
        });
    }
    private void elegirRespuestaComoMasUtil(Respuesta respuesta) {
        //llamar a la api
        ApiClientRetrofit.EndPointPreguntalo end = ApiClientRetrofit.getEndPointPreguntalo();
        Call<StringRespuesta> call = end.elegirRespuesta(context.getSharedPreferences("token.xml", 0).getString("token", ""), respuesta);
        call.enqueue(new Callback<StringRespuesta>() {
            @Override
            public void onResponse(Call<StringRespuesta> call, Response<StringRespuesta> response) {
                if (response.isSuccessful()) {
                    //navegar al mismo fragment para que se actualize
                    Toast.makeText(context, response.body().getRespuesta(), Toast.LENGTH_SHORT).show();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("consulta", respuesta.getConsulta());
                    Navigation.findNavController((Activity) context, R.id.nav_host_fragment_activity_main).navigate(R.id.navigation_Mostrar_Consulta, bundle);
                } else {
                    Log.d("salida error: ", "al cambiar puntuacion se obtuvo: " + response);
                    Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<StringRespuesta> call, Throwable t) {
                Toast.makeText(context, "Error en llamada UPVOTE", Toast.LENGTH_SHORT).show();
                Log.d("salida error: ", "error en call de cambiar puntuacion : " + t);
            }
        });
    }
}
