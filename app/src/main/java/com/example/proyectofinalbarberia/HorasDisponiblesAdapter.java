package com.example.proyectofinalbarberia;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HorasDisponiblesAdapter extends RecyclerView.Adapter<HorasDisponiblesAdapter.HorasViewHolder> {

    private List<String> listaHoras;
    private OnHoraClickListener listener;
    private int selectedPosition = -1;

    public HorasDisponiblesAdapter(List<String> listaHoras, OnHoraClickListener listener) {
        this.listaHoras = listaHoras;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HorasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hora_disponible, parent, false);
        return new HorasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorasViewHolder holder, int position) {
        String hora = listaHoras.get(position);
        holder.textViewHora.setText(hora);

        holder.itemView.setBackgroundResource(selectedPosition == position ? R.color.selected_item_color : android.R.color.transparent);

        holder.itemView.setOnClickListener(v -> {
            listener.onHoraClick(hora);
            notifyItemChanged(selectedPosition);
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(selectedPosition);
        });
    }

    @Override
    public int getItemCount() {
        return listaHoras.size();
    }

    public void setListaHoras(List<String> listaHoras) {
        this.listaHoras = listaHoras;
        notifyDataSetChanged();
        Log.d("HorasDisponiblesAdapter", "Lista de horas actualizada: " + listaHoras);
    }

    public static class HorasViewHolder extends RecyclerView.ViewHolder {
        TextView textViewHora;

        public HorasViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewHora = itemView.findViewById(R.id.textViewHora);
        }
    }

    public interface OnHoraClickListener {
        void onHoraClick(String hora);
    }
}
