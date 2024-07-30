package com.example.preguntalo.menu.ui.consulta;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.preguntalo.Modelo.Categoria;
import com.example.preguntalo.Modelo.SharedViewModel;
import com.example.preguntalo.R;
import com.example.preguntalo.menu.ui.principal.PrincipalFragment;

import java.util.List;
import java.util.concurrent.Executor;

public class CategoriaAdapter  extends RecyclerView.Adapter<CategoriaAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<Categoria> categorias;
    private SharedViewModel sharedViewModel;

    public CategoriaAdapter(Context context, List<Categoria> categorias, LayoutInflater inflater,SharedViewModel sharedViewModel) {
        this.context = context;
        this.categorias = categorias;
        this.inflater = inflater;

        this.sharedViewModel = sharedViewModel;
    }
    @NonNull
    @Override
    public CategoriaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root =inflater.inflate(R.layout.item_categoria,parent,false);
        return new CategoriaAdapter.ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //por cada item hacer
        //holder.tvCategoria.setText(categorias.get(position).getNombre());
        holder.btCategoria.setText(categorias.get(position).getNombre());

        holder.btCategoria.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //cambiar el sharedVIewModel con la categoria
                sharedViewModel.setCategoriaSeleccionada(categorias.get(holder.getAdapterPosition()).getNombre());
                Toast.makeText(context,"Se ha seleccionado: " + categorias.get(holder.getAdapterPosition()).getNombre(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    @Override
    public int getItemCount() { return categorias.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvCategoria;
        Button btCategoria;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btCategoria = itemView.findViewById(R.id.btCategoria);
            tvCategoria = itemView.findViewById(R.id.tvCategoria);


        }
    }
}
