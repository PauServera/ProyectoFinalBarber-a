package com.example.proyectofinalbarberia;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BarberiaAdapter extends RecyclerView.Adapter<BarberiaAdapter.BarberiaViewHolder> {

    private List<Barberia> barberias;

    public BarberiaAdapter(List<Barberia> barberias) {
        this.barberias = barberias;
    }

    @NonNull
    @Override
    public BarberiaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_barberia_adapter, parent, false);
        return new BarberiaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BarberiaViewHolder holder, int position) {
        Barberia barberia = barberias.get(position);
        holder.nombreTextView.setText(barberia.getNombre());
        holder.ubicacionTextView.setText(barberia.getUbicacion());
    }

    @Override
    public int getItemCount() {
        return barberias.size();
    }

    public static class BarberiaViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTextView;
        TextView ubicacionTextView;

        public BarberiaViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.nombreTextView);
            ubicacionTextView = itemView.findViewById(R.id.ubicacionTextView);
        }
    }
}
