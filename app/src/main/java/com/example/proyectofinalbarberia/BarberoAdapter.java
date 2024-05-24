package com.example.proyectofinalbarberia;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BarberoAdapter extends RecyclerView.Adapter<BarberoAdapter.BarberoViewHolder> {
    private List<Barbero> listaBarberos;
    private OnBarberoClickListener listener;

    public interface OnBarberoClickListener {
        void onBarberoClick(Barbero barbero);
    }

    public BarberoAdapter(List<Barbero> listaBarberos, OnBarberoClickListener listener) {
        this.listaBarberos = listaBarberos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BarberoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_barbero, parent, false);
        return new BarberoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BarberoViewHolder holder, int position) {
        Barbero barbero = listaBarberos.get(position);
        holder.bind(barbero, listener);
    }

    @Override
    public int getItemCount() {
        return listaBarberos.size();
    }

    public static class BarberoViewHolder extends RecyclerView.ViewHolder {
        private TextView nombreTextView;

        public BarberoViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.nombreBarberoTextView);
        }

        public void bind(Barbero barbero, OnBarberoClickListener listener) {
            nombreTextView.setText(barbero.getNombre());
            itemView.setOnClickListener(v -> listener.onBarberoClick(barbero));
        }
    }
}
