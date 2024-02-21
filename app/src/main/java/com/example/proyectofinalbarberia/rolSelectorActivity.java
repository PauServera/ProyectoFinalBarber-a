package com.example.proyectofinalbarberia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class rolSelectorActivity extends AppCompatActivity {
    private String rol = "cliente";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rol_selector);

        LinearLayout layoutOwner = findViewById(R.id.layout_owner);
        LinearLayout layoutBarber = findViewById(R.id.layout_barber);
        LinearLayout layoutClient = findViewById(R.id.layout_client);

        // Dueño
        layoutOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rol = "Dueño";
                String authId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                String nombre = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

                List<String> barberias = new ArrayList<>();
                boolean disponibilidad = true;

                Map<String, Object> usuario = new HashMap<>();
                usuario.put("email", email);
                usuario.put("nombre", nombre);
                usuario.put("barberias", barberias);
                usuario.put("rol", rol);
                usuario.put("disponibilidad", disponibilidad);



                DocumentReference ref = db.collection("Usuarios").document(authId);

                ref.set(usuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(rolSelectorActivity.this, PrincipalActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

            }
        });
        //Barbero
        layoutBarber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rol = "Barbero";

                String authId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                String nombre = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

                List<String> barberias = new ArrayList<>();
                boolean disponibilidad = true;

                Map<String, Object> usuario = new HashMap<>();
                usuario.put("email", email);
                usuario.put("nombre", nombre);
                usuario.put("barberias", barberias);
                usuario.put("rol", rol);
                usuario.put("disponibilidad", disponibilidad);



                DocumentReference ref = db.collection("Usuarios").document(authId);

                ref.set(usuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(rolSelectorActivity.this, PrincipalActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

            }
        });
        //Cliente
        layoutClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String authId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                String nombre = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

                List<String> barberias = new ArrayList<>();

                Map<String, Object> usuario = new HashMap<>();
                usuario.put("email", email);
                usuario.put("nombre", nombre);
                usuario.put("barberias", barberias);
                usuario.put("rol", rol);



                DocumentReference ref = db.collection("Usuarios").document(authId);

                ref.set(usuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(rolSelectorActivity.this, PrincipalActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }
}