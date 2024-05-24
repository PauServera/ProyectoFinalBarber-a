package com.example.proyectofinalbarberia;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DefinirHorarioActivity extends AppCompatActivity {

    private Button btnGuardarHorario;
    private TimePicker timePickerInicio, timePickerFin;
    private Spinner spinnerDia;
    private FirebaseFirestore db;
    private FirestoreManager firestoreManager = new FirestoreManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_definir_horario);

        // Inicializar Firestore
        db = FirebaseFirestore.getInstance();

        // Referenciar los elementos del diseño
        timePickerInicio = findViewById(R.id.timePickerInicio);
        timePickerFin = findViewById(R.id.timePickerFin);
        btnGuardarHorario = findViewById(R.id.btnGuardarHorario);
        spinnerDia = findViewById(R.id.spinnerDia);

        // Configurar botón de guardar horario
        btnGuardarHorario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarHorario();
            }
        });
    }

    private void guardarHorario() {
        // Obtener el UID del usuario actual
        String userId = firestoreManager.obtenerUidActualUsuario();
        if (userId == null) {
            Toast.makeText(DefinirHorarioActivity.this, "Error al obtener el UID del usuario", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener el día seleccionado y los horarios
        String dia = spinnerDia.getSelectedItem().toString();
        int horaInicio = timePickerInicio.getHour();
        int minutoInicio = timePickerInicio.getMinute();
        int horaFin = timePickerFin.getHour();
        int minutoFin = timePickerFin.getMinute();

        // Formatear las horas en cadenas HH:mm
        String inicio = String.format("%02d:%02d", horaInicio, minutoInicio);
        String fin = String.format("%02d:%02d", horaFin, minutoFin);

        // Validar que la hora de fin sea posterior a la hora de inicio
        if (validarHorario(horaInicio, minutoInicio, horaFin, minutoFin)) {
            // Crear el mapa de horarios
            Map<String, Object> horario = new HashMap<>();
            horario.put("inicio", inicio);
            horario.put("fin", fin);

            // Actualizar el horario en Firestore
            db.collection("Usuarios").document(userId)
                    .update("horarioPersonal." + dia, horario)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(DefinirHorarioActivity.this, "Horario guardado", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(DefinirHorarioActivity.this, "Error al guardar horario", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(DefinirHorarioActivity.this, "La hora de fin debe ser posterior a la hora de inicio", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validarHorario(int horaInicio, int minutoInicio, int horaFin, int minutoFin) {
        return !(horaFin < horaInicio || (horaFin == horaInicio && minutoFin <= minutoInicio));
    }
}
