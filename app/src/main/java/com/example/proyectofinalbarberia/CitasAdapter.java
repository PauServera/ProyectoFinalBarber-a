package com.example.proyectofinalbarberia;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

public class CitasAdapter extends RecyclerView.Adapter<CitasAdapter.CitasViewHolder> {
    private List<Cita> listaCitas;
    private OnCitaInteractionListener listener;
    private String rolUsuario;

    public CitasAdapter(List<Cita> listaCitas, OnCitaInteractionListener listener, String rolUsuario) {
        this.listaCitas = listaCitas;
        this.listener = listener;
        this.rolUsuario = rolUsuario;
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
        holder.bind(cita, listener, rolUsuario);
    }

    @Override
    public int getItemCount() {
        return listaCitas.size();
    }

    public static class CitasViewHolder extends RecyclerView.ViewHolder {
        TextView textViewFecha, textViewHora, textViewBarbero, textViewCliente;
        Button btnEditar, btnCancelar;

        public CitasViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewFecha = itemView.findViewById(R.id.textViewFecha);
            textViewHora = itemView.findViewById(R.id.textViewHora);
            textViewBarbero = itemView.findViewById(R.id.textViewBarbero);
            textViewCliente = itemView.findViewById(R.id.textViewCliente);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnCancelar = itemView.findViewById(R.id.btnCancelar);
        }

        public void bind(Cita cita, OnCitaInteractionListener listener, String rolUsuario) {
            textViewFecha.setText("Fecha: " + cita.getFecha());
            textViewHora.setText("Hora: " + cita.getHora());

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Usuarios").document(cita.getIdBarbero()).get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String nombreBarbero = documentSnapshot.getString("nombre");
                    textViewBarbero.setText("Barbero: " + nombreBarbero);
                }
            });

            db.collection("Usuarios").document(cita.getIdCliente()).get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String nombreCliente = documentSnapshot.getString("nombre");
                    textViewCliente.setText("Cliente: " + nombreCliente);
                }
            });

            if ("Cliente".equals(rolUsuario)) {
                btnEditar.setVisibility(View.GONE);
                btnCancelar.setVisibility(View.GONE);
            } else {
                btnEditar.setVisibility(View.VISIBLE);
                btnCancelar.setVisibility(View.VISIBLE);
            }

            btnEditar.setOnClickListener(v -> listener.onEditCita(cita));
            btnCancelar.setOnClickListener(v -> listener.onCancelCita(cita));
        }
    }

    public interface OnCitaInteractionListener {
        void onEditCita(Cita cita);
        void onCancelCita(Cita cita);
    }
}









