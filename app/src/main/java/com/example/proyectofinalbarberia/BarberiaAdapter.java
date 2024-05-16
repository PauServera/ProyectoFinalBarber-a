package com.example.proyectofinalbarberia;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class BarberiaAdapter extends RecyclerView.Adapter<BarberiaAdapter.BarberiaViewHolder> implements Filterable {

    private List<Barberia> barberias;
    private List<Barberia> barberiasFiltradas;
    private String selectedBarberiaId;
    private Context context;
    private FirestoreManager manager;
    private String rolUsuarioActual;
    private OnBarberiaClickListener onBarberiaClickListener;

    public BarberiaAdapter(List<Barberia> barberias, Context context) {
        this.barberias = barberias;
        this.context = context;
        this.barberiasFiltradas = new ArrayList<>(barberias);
        this.manager = new FirestoreManager();
        obtenerRolUsuarioActual();
    }

    private void obtenerRolUsuarioActual() {
        manager.obtenerRolUsuario(new FirestoreManager.RolUsuarioCallback() {
            @Override
            public void onRolUsuarioObtenido(String rolUsuario) {
                rolUsuarioActual = rolUsuario;
                notifyDataSetChanged();
            }

            @Override
            public void onFalloObteniendoRolUsuario(String mensajeError) {
                Toast.makeText(context, "Error al obtener el rol del usuario: " + mensajeError, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setOnBarberiaClickListener(OnBarberiaClickListener listener) {
        this.onBarberiaClickListener = listener;
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

        int colorDefault = Color.TRANSPARENT;
        holder.itemView.setBackgroundColor(colorDefault);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedBarberiaId = barberia.getId();

                manager.obtenerInformacionBarberia(selectedBarberiaId, new FirestoreManager.BarberiaCallback() {
                    @Override
                    public void onBarberiaObtenida(Barberia barberia) {
                        if (onBarberiaClickListener != null) {
                            onBarberiaClickListener.onBarberiaClick(barberia);
                        }

                        // InfoBarberiaActivity
                        Intent intent = new Intent(context, InfoBarberiaActivity.class);
                        intent.putExtra("BARBERIA_INFO", barberia);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onFalloObteniendoBarberia(String mensajeError) {
                        // manejar el fallo
                    }
                });

                if (onBarberiaClickListener != null) {
                    onBarberiaClickListener.onBarberiaClick(barberia);
                }
            }
        });

        // Editar
        holder.btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Editar " + barberia.getNombre(), Toast.LENGTH_SHORT).show();
            }
        });

        // Eliminar
        if (rolUsuarioActual != null && (rolUsuarioActual.equals("Cliente") || rolUsuarioActual.equals("Barbero"))) {
            holder.btnBorrar.setVisibility(View.VISIBLE);
            holder.btnEditar.setVisibility(View.INVISIBLE);
        } else {
            holder.btnBorrar.setVisibility(View.VISIBLE);
            holder.btnEditar.setVisibility(View.VISIBLE);
        }

        holder.btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mostrar un diálogo de confirmación antes de eliminar la barbería
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirmar eliminación");
                builder.setMessage("¿Estás seguro de que deseas eliminar esta barbería?");
                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Eliminar la barbería
                        String barberiaId = barberia.getId();
                        manager.eliminarBarberia(barberiaId, rolUsuarioActual, new FirestoreManager.OnEliminarBarberiaListener() {
                            @Override
                            public void onBarberiaEliminada() {
                                Toast.makeText(context, "Barbería " + barberia.getNombre() + " eliminada", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onEliminarBarberiaError(String mensajeError) {
                                Toast.makeText(context, "Error al eliminar la barbería: " + mensajeError, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                builder.setNegativeButton("Cancelar", null);
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return barberias.size();
    }

    public static class BarberiaViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTextView;
        TextView ubicacionTextView;
        ImageButton btnEditar;
        ImageButton btnBorrar;

        public BarberiaViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.nombreTextView);
            ubicacionTextView = itemView.findViewById(R.id.ubicacionTextView);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnBorrar = itemView.findViewById(R.id.btnBorrar);
        }
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String filtro = charSequence.toString().toLowerCase().trim();
                barberiasFiltradas = new ArrayList<>();

                if (filtro.isEmpty()) {
                    barberiasFiltradas.addAll(barberias);
                } else {
                    for (Barberia barberia : barberias) {
                        if (barberia.getNombre().toLowerCase().contains(filtro)) {
                            barberiasFiltradas.add(barberia);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = barberiasFiltradas;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                barberiasFiltradas = (List<Barberia>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface OnBarberiaClickListener {
        void onBarberiaClick(Barberia barberia);
    }
}