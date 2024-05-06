package com.example.proyectofinalbarberia;

import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class BarberiaDialogAdapter extends RecyclerView.Adapter<BarberiaDialogAdapter.BarberiaViewHolder> implements Filterable {

    private List<Barberia> listaBarberias;
    private List<Barberia> listaBarberiasFiltradas;

    public BarberiaDialogAdapter(List<Barberia> barberias) {
        this.listaBarberias = barberias;
        this.listaBarberiasFiltradas = new ArrayList<>(barberias);
    }

    @NonNull
    @Override
    public BarberiaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_dialog_adapter, parent, false);
        return new BarberiaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BarberiaViewHolder holder, int position) {
        Barberia barberia = listaBarberiasFiltradas.get(position);
        holder.nombreTextView.setText(barberia.getNombre());
        holder.ubicacionTextView.setText(barberia.getUbicacion());

        int colorDefault = Color.TRANSPARENT;
        holder.itemView.setBackgroundColor(colorDefault);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int colorSeleccionado = Color.parseColor("#838383");
                holder.itemView.setBackgroundColor(colorSeleccionado);

                agregarBarberiaALista(barberia);

                DialogInterface dialog = (DialogInterface) v.getTag();
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaBarberiasFiltradas.size();
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

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String filtro = charSequence.toString().toLowerCase().trim();
                listaBarberiasFiltradas = new ArrayList<>();

                if (!filtro.isEmpty()) {
                    for (Barberia barberia : listaBarberias) {
                        if (barberia.getNombre().toLowerCase().contains(filtro)) {
                            listaBarberiasFiltradas.add(barberia);
                        }
                    }
                } else {
                    // Si el filtro está vacío, mostrar todas las barberías
                    listaBarberiasFiltradas.addAll(listaBarberias);
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listaBarberiasFiltradas;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listaBarberiasFiltradas = (List<Barberia>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void agregarBarberiaALista(Barberia barberia) {
        String uidUsuario = obtenerUidActualUsuario();

        if (uidUsuario != null) {
            DocumentReference usuarioRef = FirebaseFirestore.getInstance().collection("Usuarios").document(uidUsuario);

            usuarioRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    List<String> barberiasActuales = (List<String>) documentSnapshot.get("barberias");

                    if (barberiasActuales == null || !barberiasActuales.contains(barberia.getId())) {
                        // Si no está en la lista, añadirlo
                        usuarioRef.update("barberias", FieldValue.arrayUnion(barberia.getId()))
                                .addOnSuccessListener(aVoid -> {
                                    // Éxito al añadir la barbería
                                })
                                .addOnFailureListener(e -> {
                                    // Error al añadir la barbería
                                });
                    } else {
                        // La barbería ya está en la lista
                        Log.e("BarberiaDialogAdapter", "La barbería ya está en la lista del usuario.");
                    }
                }
            }).addOnFailureListener(e -> {
                // Error al obtener la lista de barberias del usuario
                Log.e("BarberiaDialogAdapter", "Error al obtener la lista de barberías del usuario.", e);
            });
        }
    }

    private String obtenerUidActualUsuario() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return (currentUser != null) ? currentUser.getUid() : null;
    }

    public void setBarberias(List<Barberia> barberias) {
        this.listaBarberias = barberias;
        this.listaBarberiasFiltradas = new ArrayList<>(barberias);  // Inicializar la lista filtrada
        notifyDataSetChanged();  // Notificar al adaptador que los datos han cambiado
    }
}
