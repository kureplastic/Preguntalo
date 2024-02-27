package com.example.preguntalo.menu.ui.consulta;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.preguntalo.Modelo.Respuesta;
import com.example.preguntalo.R;

import java.util.List;

public class MostrarConsultaAdapter extends RecyclerView.Adapter<MostrarConsultaAdapter.ViewHolder>{
    private Context context;
    private LayoutInflater inflater;
    private List<Respuesta> respuestas;

    public MostrarConsultaAdapter(Context context, List<Respuesta> respuestas, LayoutInflater inflater) {
        this.context = context;
        this.respuestas = respuestas;
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
        //por cada item hacer
        holder.tvRespuesta.setText(respuestas.get(position).getTexto());
        holder.tvUpVote.setText(respuestas.get(position).getPuntuacionPositiva() + "");
        Log.d("salida puntuacion: ",respuestas.get(position).getPuntuacionNegativa() + "");
        holder.tvDownvote.setText(respuestas.get(position).getPuntuacionNegativa() + "");
    }

    @Override
    public int getItemCount() { return respuestas.size(); }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvRespuesta, tvUpVote, tvDownvote;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRespuesta = itemView.findViewById(R.id.tvTextoConsulta);
            tvUpVote = itemView.findViewById(R.id.tvUpVote);
            tvDownvote = itemView.findViewById(R.id.tvDownvote);
        }
    }

}
