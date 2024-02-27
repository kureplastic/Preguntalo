package com.example.preguntalo.menu.ui.consulta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.preguntalo.Modelo.Categoria;
import com.example.preguntalo.R;

import java.util.List;

public class CategoriaAdapter  extends RecyclerView.Adapter<CategoriaAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<Categoria> categorias;

    public CategoriaAdapter(Context context, List<Categoria> categorias, LayoutInflater inflater) {
        this.context = context;
        this.categorias = categorias;
        this.inflater = inflater;
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
        holder.tvCategoria.setText(categorias.get(position).getNombre());

    }
    @Override
    public int getItemCount() { return categorias.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvCategoria;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoria = itemView.findViewById(R.id.tvCategoria);
        }
    }
}
