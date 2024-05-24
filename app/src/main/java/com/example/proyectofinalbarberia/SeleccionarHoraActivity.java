package com.example.proyectofinalbarberia;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SeleccionarHoraActivity extends AppCompatActivity {

    private RecyclerView recyclerViewHorasDisponibles;
    private HorasDisponiblesAdapter adapter;
    private List<String> listaHorasDisponibles = new ArrayList<>();
    private FirebaseFirestore db;
    private String uidBarberia, selectedDate, uidBarbero;
    private String horaSeleccionada;
    private static final String TAG = "SeleccionarHoraActivity";
    private FirestoreManager firestoreManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_hora);

        firestoreManager = new FirestoreManager();

        db = FirebaseFirestore.getInstance();
        uidBarberia = getIntent().getStringExtra("UID_BARBERIA");
        selectedDate = getIntent().getStringExtra("SELECTED_DATE");
        uidBarbero = getIntent().getStringExtra("UID_BARBERO");

        recyclerViewHorasDisponibles = findViewById(R.id.recyclerViewHorasDisponibles);
        recyclerViewHorasDisponibles.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HorasDisponiblesAdapter(listaHorasDisponibles, this::onHoraSelected);
        recyclerViewHorasDisponibles.setAdapter(adapter);

        Button btnConfirmarCita = findViewById(R.id.btnConfirmarCita);
        btnConfirmarCita.setOnClickListener(v -> confirmarCita());

        cargarHorasDisponibles();
    }

    private void cargarHorasDisponibles() {
        db.collection("Usuarios").document(uidBarbero)
                .get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Map<String, Object> horarioPersonal = (Map<String, Object>) documentSnapshot.get("horarioPersonal");
                        if (horarioPersonal != null) {
                            String dayOfWeek = getDayOfWeek(selectedDate);
                            if (horarioPersonal.containsKey(dayOfWeek)) {
                                Map<String, String> horarioDelDia = (Map<String, String>) horarioPersonal.get(dayOfWeek);
                                String inicio = horarioDelDia.get("inicio");
                                String fin = horarioDelDia.get("fin");
                                List<String> horasDisponibles = generarListaHoras(inicio, fin);

                                Log.d(TAG, "Horas disponibles generadas: " + horasDisponibles);

                                // Log de los parámetros de la consulta
                                Log.d(TAG, "Consultando citas para idBarberia: " + uidBarberia + " y fecha: " + selectedDate);

                                db.collection("Citas")
                                        .whereEqualTo("idBarberia", uidBarberia)
                                        .whereEqualTo("fecha", selectedDate)
                                        .get()
                                        .addOnSuccessListener(queryDocumentSnapshots -> {
                                            List<String> horasReservadas = new ArrayList<>();
                                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                                String horaReservada = doc.getString("hora");
                                                horasReservadas.add(horaReservada);
                                                Log.d(TAG, "Hora reservada obtenida: " + horaReservada);
                                            }

                                            Log.d(TAG, "Horas reservadas obtenidas: " + horasReservadas);

                                            List<String> horasFiltradas = new ArrayList<>();
                                            for (String hora : horasDisponibles) {
                                                if (!horasReservadas.contains(hora)) {
                                                    horasFiltradas.add(hora);
                                                } else {
                                                    Log.d(TAG, "Hora filtrada (reservada): " + hora);
                                                }
                                            }

                                            Log.d(TAG, "Horas disponibles después del filtrado: " + horasFiltradas);

                                            adapter.setListaHoras(horasFiltradas);

                                        }).addOnFailureListener(e -> {
                                            Log.e(TAG, "Error al obtener horas reservadas", e);
                                        });
                            } else {
                                Toast.makeText(this, "El barbero no tiene horario definido para este día", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "El barbero no tiene horario definido", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "El barbero no tiene horario definido", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {
                    Log.e(TAG, "Error al obtener horario del barbero", e);
                });
    }

    private String getDayOfWeek(String date) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date dateObj = format.parse(date);
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
            return dayFormat.format(dateObj).toLowerCase();
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    private List<String> generarListaHoras(String inicio, String fin) {
        List<String> horas = new ArrayList<>();
        try {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
            Date dateInicio = format.parse(inicio);
            Date dateFin = format.parse(fin);
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateInicio);

            while (cal.getTime().before(dateFin)) {
                horas.add(format.format(cal.getTime()));
                cal.add(Calendar.MINUTE, 30); // Assuming 30 minutes intervals, adjust if needed
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return horas;
    }

    private void onHoraSelected(String hora) {
        horaSeleccionada = hora;
    }

    private void confirmarCita() {
        if (horaSeleccionada != null) {
            try {
                Map<String, Object> cita = new HashMap<>();
                cita.put("idBarberia", uidBarberia);
                cita.put("idCliente", firestoreManager.obtenerUidActualUsuario());
                cita.put("fecha", selectedDate);
                cita.put("hora", horaSeleccionada);

                db.collection("Citas")
                        .add(cita)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(SeleccionarHoraActivity.this, "Cita creada con éxito", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .addOnFailureListener(e -> Toast.makeText(SeleccionarHoraActivity.this, "Error al crear la cita", Toast.LENGTH_SHORT).show());
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "Error al confirmar la cita", e);
            }
        } else {
            Toast.makeText(this, "Por favor seleccione una hora", Toast.LENGTH_SHORT).show();
        }
    }
}