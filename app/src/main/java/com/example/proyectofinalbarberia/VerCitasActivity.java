package com.example.proyectofinalbarberia;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class VerCitasActivity extends AppCompatActivity implements CitasAdapter.OnCitaInteractionListener {

    private RecyclerView recyclerViewCitas;
    private CitasAdapter adapter;
    private List<Cita> listaCitas = new ArrayList<>();
    private FirebaseFirestore db;
    private FirestoreManager firestoreManager = new FirestoreManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_citas);

        // Configurar el Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        db = FirebaseFirestore.getInstance();

        recyclerViewCitas = findViewById(R.id.recyclerViewCitas);
        recyclerViewCitas.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CitasAdapter(listaCitas, this);
        recyclerViewCitas.setAdapter(adapter);

        cargarCitas();
    }

    private void cargarCitas() {
        String uidCliente = firestoreManager.obtenerUidActualUsuario();
        db.collection("Citas")
                .whereEqualTo("idCliente", uidCliente)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    listaCitas.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Cita cita = document.toObject(Cita.class);
                        listaCitas.add(cita);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(VerCitasActivity.this, "Error al cargar las citas", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onEditCita(Cita cita) {
        // Implementar la lógica para editar la cita
        // Puedes abrir una nueva actividad o un diálogo donde el usuario pueda cambiar la fecha y hora
    }

    @Override
    public void onCancelCita(Cita cita) {
        db.collection("Citas").document(cita.getId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(VerCitasActivity.this, "Cita cancelada", Toast.LENGTH_SHORT).show();
                    cargarCitas();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(VerCitasActivity.this, "Error al cancelar la cita", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
