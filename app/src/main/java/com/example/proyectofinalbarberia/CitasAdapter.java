package com.example.proyectofinalbarberia;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import com.google.firebase.firestore.FirebaseFirestore;


public class CitasAdapter extends RecyclerView.Adapter<CitasAdapter.CitasViewHolder> {

    private List<Cita> listaCitas;
    private OnCitaInteractionListener listener;
    private FirebaseFirestore db;

    public CitasAdapter(List<Cita> listaCitas, OnCitaInteractionListener listener) {
        this.listaCitas = listaCitas;
        this.listener = listener;
        this.db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public CitasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cita, parent, false);
        return new CitasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CitasViewHolder holder, int position) {
        Cita cita = listaCitas.get(position);
        holder.bind(cita, listener, db);
    }

    @Override
    public int getItemCount() {
        return listaCitas.size();
    }

    public static class CitasViewHolder extends RecyclerView.ViewHolder {
        TextView textViewFecha, textViewBarbero;
        Button btnEditar, btnCancelar;

        public CitasViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewFecha = itemView.findViewById(R.id.textViewFecha);
            textViewBarbero = itemView.findViewById(R.id.textViewBarbero);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnCancelar = itemView.findViewById(R.id.btnCancelar);
        }

        public void bind(Cita cita, OnCitaInteractionListener listener, FirebaseFirestore db) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            textViewFecha.setText(sdf.format(cita.getFechaCita()));

            // Obtener y mostrar el nombre del barbero
            db.collection("Usuarios").document(cita.getIdBarbero()).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String nombreBarbero = documentSnapshot.getString("nombre");
                            textViewBarbero.setText(nombreBarbero);
                        }
                    });

            btnEditar.setOnClickListener(v -> listener.onEditCita(cita));
            btnCancelar.setOnClickListener(v -> listener.onCancelCita(cita));
        }
    }

    public interface OnCitaInteractionListener {
        void onEditCita(Cita cita);
        void onCancelCita(Cita cita);
    }
}

