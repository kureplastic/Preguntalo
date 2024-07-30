package com.example.preguntalo.menu.ui.principal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
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
import com.example.preguntalo.Modelo.Consulta;
import com.example.preguntalo.Modelo.Puntuacion;
import com.example.preguntalo.Modelo.Rating;
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

public class PrincipalAdapter extends RecyclerView.Adapter<PrincipalAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private List<Consulta> consultas;

    public PrincipalAdapter(Context context, List<Consulta> consultas, LayoutInflater inflater) {
        this.context = context;
        this.consultas = consultas;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public PrincipalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = inflater.inflate(R.layout.item_consulta, parent, false);
        return new PrincipalAdapter.ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull PrincipalAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //aca hacer lo que quiero con cada uno de los items
        Consulta consultaEntrante = consultas.get(position);

        holder.tvTituloconsulta.setText(consultaEntrante.getTitulo());
        holder.tvTextoConsulta.setText(consultaEntrante.getTexto());
        holder.tvUpVote.setText(consultaEntrante.getPuntuacionPositiva() + "");
        holder.tvDownvote.setText(consultaEntrante.getPuntuacionNegativa() + "");

        //traer al usuario y agregarle el rating
        Usuario usuario = consultaEntrante.getUsuario();
        if(usuario != null){
            //aca renderizamos la imagen del usuario la imagen del usuario
            if(usuario.getFotoPerfil() != null){
                Glide.with(context).load(usuario.getFotoPerfil()).into(holder.imgFotoPerfil);
            }
            //llamar al metodo para obtener el rating del usuario
            ApiClientRetrofit.EndPointPreguntalo end = ApiClientRetrofit.getEndPointPreguntalo();
            Call<Rating> call = end.obtenerRatingDeUsuario(context.getSharedPreferences("token.xml",0).getString("token",""),usuario.getRatingId());
            call.enqueue(new Callback<Rating>() {
                @Override
                public void onResponse(Call<Rating> call, Response<Rating> response) {
                    if(response.isSuccessful()){
                        Rating rating = response.body();
                        usuario.setRating(rating);
                        //armar el powermenu button
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

                        holder.imgFotoPerfil.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                powerMenu.showAtCenter(holder.imgFotoPerfil);
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
        holder.btResponder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Log.d("salida consulta: ","consulta: " + consultaEntrante);
                bundle.putSerializable("consulta", consultaEntrante);
                Navigation.findNavController((Activity) context,R.id.nav_host_fragment_activity_main).navigate(R.id.navigation_Mostrar_Consulta,bundle);
            }
        });
        holder.btUpvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //primero armar la puntuacion, con la consulta y el id del usuario
                //la consulta ya la tenemos
                //el id del usuario lo obtenemos desde el shared preferences
                Puntuacion puntuacionAEnviar = new Puntuacion();
                int idUsuario = context.getSharedPreferences("userdata.xml",0).getInt("id",0);
                puntuacionAEnviar.setUsuarioId(idUsuario);
                puntuacionAEnviar.setConsulta(consultaEntrante);
                puntuacionAEnviar.setConsultaId(consultaEntrante.getId());
                puntuacionAEnviar.setPuntuacionPositiva(true);
                //luego mandamos la puntuacion en una call a la api
                ApiClientRetrofit.EndPointPreguntalo end = ApiClientRetrofit.getEndPointPreguntalo();
                Call<StringRespuesta> call = end.cambiarPuntuacion(context.getSharedPreferences("token.xml",0).getString("token",""),puntuacionAEnviar);

                call.enqueue(new Callback<StringRespuesta>() {
                    @Override
                    public void onResponse(Call<StringRespuesta> call, Response<StringRespuesta> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(context, response.body().getRespuesta(), Toast.LENGTH_SHORT).show();
                            Navigation.findNavController((Activity) context,R.id.nav_host_fragment_activity_main).navigate(R.id.navigation_vista_principal);
                        }
                    }
                    @Override
                    public void onFailure(Call<StringRespuesta> call, Throwable t) {
                        Toast.makeText(context, "Error en llamada UPVOTE", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController((Activity) context,R.id.nav_host_fragment_activity_main).navigate(R.id.navigation_vista_principal);
                        Log.d("salida error: ", "error en call de cambiar puntuacion : " + t);
                    }
                });
                //ultimo paso es corroborar una forma para que el boton cambie de color en caso de que se haya dado upvote o downvote
            }
        });
        holder.btDownvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //primero armar la puntuacion, con la consulta y el id del usuario
                //la consulta ya la tenemos
                //el id del usuario lo obtenemos desde el shared preferences
                Puntuacion puntuacionAEnviar = new Puntuacion();
                Consulta consulta = consultas.get(position);
                int idUsuario = context.getSharedPreferences("userdata.xml",0).getInt("id",0);
                puntuacionAEnviar.setUsuarioId(idUsuario);
                puntuacionAEnviar.setConsulta(consulta);
                puntuacionAEnviar.setConsultaId(consulta.getId());
                puntuacionAEnviar.setPuntuacionPositiva(false);
                //luego mandamos la puntuacion en una call a la api
                ApiClientRetrofit.EndPointPreguntalo end = ApiClientRetrofit.getEndPointPreguntalo();
                Call<StringRespuesta> call = end.cambiarPuntuacion(context.getSharedPreferences("token.xml",0).getString("token",""),puntuacionAEnviar);
                call.enqueue(new Callback<StringRespuesta>() {
                    @Override
                    public void onResponse(Call<StringRespuesta> call, Response<StringRespuesta> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(context, response.body().getRespuesta() , Toast.LENGTH_SHORT).show();
                            Navigation.findNavController((Activity) context,R.id.nav_host_fragment_activity_main).navigate(R.id.navigation_vista_principal);
                        }
                    }

                    @Override
                    public void onFailure(Call<StringRespuesta> call, Throwable t) {
                        Toast.makeText(context, "Error en llamada DOWNVOTE", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController((Activity) context,R.id.nav_host_fragment_activity_main).navigate(R.id.navigation_vista_principal);
                        Log.d("salida error: ", "error en call de cambiar puntuacion : " + t);
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() { return consultas.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvTituloconsulta, tvTextoConsulta, tvUpVote, tvDownvote;
        ImageButton btResponder, btUpvote, btDownvote;
        ImageView imgFotoPerfil;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTituloconsulta = itemView.findViewById(R.id.tvTituloconsulta);
            tvTextoConsulta = itemView.findViewById(R.id.tvTextoConsulta);
            tvUpVote = itemView.findViewById(R.id.tvUpvoteRespuesta);
            tvDownvote = itemView.findViewById(R.id.tvDownvoteRespuesta);
            btResponder = itemView.findViewById(R.id.btResponderRespuesta);
            btUpvote = itemView.findViewById(R.id.btUpvoteRespueta);
            btDownvote = itemView.findViewById(R.id.btDownvoteConsulta);
            imgFotoPerfil = itemView.findViewById(R.id.imgFotoPerfilConsulta);
        }
    }


}
