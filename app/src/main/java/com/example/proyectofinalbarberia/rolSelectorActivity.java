package com.example.proyectofinalbarberia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
                        mostrarDialogoNombre();
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
                        mostrarDialogoNombre();
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
                        mostrarDialogoNombre();
                    }
                });
            }
        });
    }

    private void mostrarDialogoNombre() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialognombre, null);

        final EditText editTextNombre = dialogView.findViewById(R.id.editTextNombre);

        builder.setView(dialogView)
                .setTitle("Introducir Nombre")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String nombre = editTextNombre.getText().toString();
                        Toast.makeText(rolSelectorActivity.this, "!Bienvenido, " + nombre + "!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                        //Guardar nombre en firebase
                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        CollectionReference usuariosCollection = FirebaseFirestore.getInstance().collection("Usuarios");
                        DocumentReference usuarioDocument = usuariosCollection.document(userId);

                        usuarioDocument.update("nombre", nombre)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(rolSelectorActivity.this, "!Bienvenido, " + nombre + "!", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(rolSelectorActivity.this, "Error al actualizar el nombre", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                });

                        //Redirijo a la actividad principal
                        Intent intent = new Intent(rolSelectorActivity.this, PrincipalActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}