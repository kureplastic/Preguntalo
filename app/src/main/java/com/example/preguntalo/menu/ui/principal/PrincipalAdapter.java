package com.example.preguntalo.menu.ui.principal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.preguntalo.Modelo.Consulta;
import com.example.preguntalo.R;

import java.util.List;

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
        holder.tvTituloconsulta.setText(consultas.get(position).getTitulo());
        holder.tvTextoConsulta.setText(consultas.get(position).getTexto());
        holder.tvUpVote.setText(consultas.get(position).getPuntuacionPositiva() + "");
        holder.tvDownvote.setText(consultas.get(position).getPuntuacionNegativa() + "");
        holder.btResponder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Consulta consulta = consultas.get(position);
                Log.d("salida consulta: ","consulta: " + consulta);
                bundle.putSerializable("consulta", consulta);
                Navigation.findNavController((Activity) context,R.id.nav_host_fragment_activity_main).navigate(R.id.navigation_Mostrar_Consulta,bundle);
            }
        });
        holder.btUpvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Diste UPVOTE", Toast.LENGTH_SHORT).show();
            }
        });
        holder.btDownvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Diste DOWNVOTE", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() { return consultas.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvTituloconsulta, tvTextoConsulta, tvUpVote, tvDownvote;
        ImageButton btResponder, btUpvote, btDownvote;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTituloconsulta = itemView.findViewById(R.id.tvTituloconsulta);
            tvTextoConsulta = itemView.findViewById(R.id.tvTextoConsulta);
            tvUpVote = itemView.findViewById(R.id.tvUpVote);
            tvDownvote = itemView.findViewById(R.id.tvDownvote);
            btResponder = itemView.findViewById(R.id.btResponder);
            btUpvote = itemView.findViewById(R.id.btUpvote);
            btDownvote = itemView.findViewById(R.id.btDownvote);
        }
    }


}
