package com.example.proyectofinalbarberia;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsetsController;
import android.widget.Toast;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class SeleccionarBarberoActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BarberoAdapter adapter;
    private List<Barbero> listaBarberos = new ArrayList<>();
    private FirebaseFirestore db;
    private String uidBarberia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_barbero);
        cambiarColorBarraDeEstado();

        uidBarberia = getIntent().getStringExtra("UID_BARBERIA");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Seleccionar barbero:");
        }

        recyclerView = findViewById(R.id.recyclerViewBarberos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BarberoAdapter(listaBarberos, this::onBarberoSelected);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        cargarBarberos();
    }

    private void cargarBarberos() {
        DocumentReference barberiaRef = db.collection("Barberias").document(uidBarberia);
        barberiaRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Barberia barberia = documentSnapshot.toObject(Barberia.class);
                if (barberia != null) {
                    List<String> listaBarberosUID = barberia.getListaBarberos();
                    listaBarberos.clear();

                    for (String uidBarbero : listaBarberosUID) {
                        DocumentReference barberoRef = db.collection("Usuarios").document(uidBarbero);
                        barberoRef.get().addOnSuccessListener(barberoSnapshot -> {
                            if (barberoSnapshot.exists()) {
                                Barbero barbero = barberoSnapshot.toObject(Barbero.class);
                                barbero.setId(uidBarbero);
                                listaBarberos.add(barbero);
                                adapter.notifyDataSetChanged();
                            }
                        }).addOnFailureListener(e -> {
                            Toast.makeText(SeleccionarBarberoActivity.this, "Error al cargar el barbero con UID: " + uidBarbero, Toast.LENGTH_SHORT).show();
                        });
                    }
                }
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(SeleccionarBarberoActivity.this, "Error al cargar los barberos", Toast.LENGTH_SHORT).show();
        });
    }



    private void onBarberoSelected(Barbero barbero) {
        Intent intent = new Intent(SeleccionarBarberoActivity.this, SeleccionarHoraActivity.class);
        intent.putExtra("UID_BARBERIA", uidBarberia);
        intent.putExtra("SELECTED_DATE", getIntent().getStringExtra("SELECTED_DATE"));
        intent.putExtra("UID_BARBERO", barbero.getId()); // Pasar el UID del barbero seleccionado
        startActivity(intent);
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

