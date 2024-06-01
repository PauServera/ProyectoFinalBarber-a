package com.example.proyectofinalbarberia;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowInsetsController;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class MisCitasActivity extends AppCompatActivity implements CitasAdapter.OnCitaInteractionListener {
    private RecyclerView recyclerView;
    private CitasAdapter adapter;
    private List<Cita> listaCitas = new ArrayList<>();
    private FirestoreManager firestoreManager;
    private String uidUsuario;
    private String rolUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_citas);
        cambiarColorBarraDeEstado();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Mis citas");
        }

        recyclerView = findViewById(R.id.recyclerViewCitas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        firestoreManager = new FirestoreManager();
        uidUsuario = firestoreManager.obtenerUidActualUsuario();
        obtenerRolYMostrarCitas();
    }

    private void obtenerRolYMostrarCitas() {
        firestoreManager.obtenerRolUsuario(new FirestoreManager.RolUsuarioCallback() {
            @Override
            public void onRolUsuarioObtenido(String rol) {
                rolUsuario = rol;
                adapter = new CitasAdapter(listaCitas, MisCitasActivity.this, rolUsuario);
                recyclerView.setAdapter(adapter);
                obtenerCitasParaRol(rolUsuario, uidUsuario);
            }

            @Override
            public void onFalloObteniendoRolUsuario(String mensajeError) {
                Toast.makeText(MisCitasActivity.this, "Error al obtener rol de usuario: " + mensajeError, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void obtenerCitasParaRol(String rol, String uid) {
        firestoreManager.obtenerCitasDeUsuario(uid, new FirestoreManager.ObtenerCitasCallback() {
            @Override
            public void onCitasObtenidas(List<String> listaCitasId) {
                listaCitas.clear();
                for (String citaId : listaCitasId) {
                    firestoreManager.getFirestoreInstance().collection("Citas").document(citaId)
                            .get().addOnSuccessListener(documentSnapshot -> {
                                if (documentSnapshot.exists()) {
                                    Cita cita = documentSnapshot.toObject(Cita.class);
                                    if (cita != null) {
                                        cita.setId(citaId);  // Asegúrate de que el ID se establece en la cita
                                        listaCitas.add(cita);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            }).addOnFailureListener(e -> {
                                Toast.makeText(MisCitasActivity.this, "Error al obtener citas: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                }
            }

            @Override
            public void onFallo(String mensajeError) {
                Toast.makeText(MisCitasActivity.this, "Error al obtener citas: " + mensajeError, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onEditCita(Cita cita) {
        // Implementar lógica para editar cita
    }

    @Override
    public void onCancelCita(Cita cita) {
        if (cita.getId() == null || cita.getId().isEmpty()) {
            Log.e("MisCitasActivity", "Cita ID is null or empty");
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Cancelar cita")
                .setMessage("¿Estás seguro que quieres cancelar la cita?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("Citas").document(cita.getId()).delete()
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(MisCitasActivity.this, "Cita cancelada", Toast.LENGTH_SHORT).show();
                                listaCitas.remove(cita);
                                adapter.notifyDataSetChanged();

                                // Obtener el email del cliente y enviar notificación
                                db.collection("Usuarios").document(cita.getIdCliente()).get().addOnSuccessListener(documentSnapshot -> {
                                    if (documentSnapshot.exists()) {
                                        String emailCliente = documentSnapshot.getString("email");
                                        if (emailCliente != null) {
                                            EmailService.sendEmail(emailCliente, "BarberTeam - Cita Cancelada", "Su cita del día " + cita.getFecha() + " a las " + cita.getHora() + " ha sido cancelada.");
                                        }
                                    }
                                });
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(MisCitasActivity.this, "Error al cancelar cita: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }


    private void cambiarColorBarraDeEstado() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            cambiarColorBarraDeEstadoAndroidR();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cambiarColorBarraDeEstadoLollipop();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void cambiarColorBarraDeEstadoLollipop() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(getResources().getColor(R.color.toolbar_color));
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void cambiarColorBarraDeEstadoAndroidR() {
        WindowInsetsController insetsController = getWindow().getInsetsController();
        if (insetsController != null) {
            insetsController.setSystemBarsAppearance(
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.toolbar_color));
        }
    }
}






