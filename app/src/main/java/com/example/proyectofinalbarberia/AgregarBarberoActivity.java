package com.example.proyectofinalbarberia;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowInsetsController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class AgregarBarberoActivity extends AppCompatActivity {

    private EditText editTextCorreoBarbero;
    private Button btnAgregarBarbero;
    private FirebaseFirestore db;
    private FirestoreManager firestoreManager;
    private String uidBarberiaActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_barbero);

        db = FirebaseFirestore.getInstance();
        firestoreManager = new FirestoreManager();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Añadir barbero");
        }

        editTextCorreoBarbero = findViewById(R.id.editTextCorreoBarbero);
        btnAgregarBarbero = findViewById(R.id.btnAgregarBarbero);

        btnAgregarBarbero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerUidBarberiaUsuarioActual(new UidBarberiaCallback() {
                    @Override
                    public void onUidBarberiaObtenido(String uidBarberia) {
                        uidBarberiaActual = uidBarberia;
                        agregarBarbero();
                    }

                    @Override
                    public void onFalloObteniendoUidBarberia(String mensajeError) {
                        // error al obtener el UID de la barbería
                    }
                });

            }
        });
    }

    private void obtenerUidBarberiaUsuarioActual(final UidBarberiaCallback callback) {
        String uidUsuarioActual = firestoreManager.obtenerUidActualUsuario();
        if (uidUsuarioActual != null) {
            db.collection("Usuarios").document(uidUsuarioActual)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                List<String> barberias = (List<String>) documentSnapshot.get("barberias");
                                if (barberias != null && !barberias.isEmpty()) {
                                    String uidBarberia = barberias.get(0); // Si el usuario solo puede tener una barbería
                                    callback.onUidBarberiaObtenido(uidBarberia);
                                } else {
                                    callback.onFalloObteniendoUidBarberia("El usuario no tiene ninguna barbería asociada");
                                }
                            } else {
                                callback.onFalloObteniendoUidBarberia("No se encontró el documento del usuario actual");
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            callback.onFalloObteniendoUidBarberia("Error al obtener el UID de la barbería: " + e.getMessage());
                        }
                    });
        } else {
            callback.onFalloObteniendoUidBarberia("No se pudo obtener el UID del usuario actual");
        }
    }

    public interface UidBarberiaCallback {
        void onUidBarberiaObtenido(String uidBarberia);
        void onFalloObteniendoUidBarberia(String mensajeError);
    }

    private void agregarBarbero() {
        String correoBarbero = editTextCorreoBarbero.getText().toString().trim();

        if (TextUtils.isEmpty(correoBarbero)) {
            Toast.makeText(this, "Por favor, ingresa el correo electrónico del barbero", Toast.LENGTH_SHORT).show();
            return;
        }

        // Paso 1: Obtener el UID del usuario por su correo electrónico
        firestoreManager.obtenerUidUsuarioPorCorreo(correoBarbero, new FirestoreManager.UidUsuarioCallback() {
            @Override
            public void onUidUsuarioObtenido(String uidUsuario) {
                if (uidUsuario != null) {
                    // Paso 2: Actualizar el campo "barberias" del usuario
                    firestoreManager.agregarBarberiaAUsuario(uidUsuario, uidBarberiaActual, new FirestoreManager.AgregarBarberiaCallback() {
                        @Override
                        public void onBarberiaAgregada() {
                            // Paso 3: Actualizar el campo "barberos" de la barbería
                            firestoreManager.agregarBarberoABarberia(uidBarberiaActual, uidUsuario, new FirestoreManager.AgregarBarberoCallback() {
                                @Override
                                public void onBarberoAgregado() {
                                    Toast.makeText(AgregarBarberoActivity.this, "Barbero agregado con éxito", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFalloAgregandoBarbero(String mensajeError) {
                                    Toast.makeText(AgregarBarberoActivity.this, "Error al agregar el barbero a la barbería: " + mensajeError, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onFalloAgregandoBarberia(String mensajeError) {
                            Toast.makeText(AgregarBarberoActivity.this, "Error al agregar la barbería al usuario: " + mensajeError, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(AgregarBarberoActivity.this, "El correo electrónico ingresado no corresponde a ningún usuario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFalloObteniendoUidUsuario(String mensajeError) {
                Toast.makeText(AgregarBarberoActivity.this, "Error al obtener el UID del barbero: " + mensajeError, Toast.LENGTH_SHORT).show();
            }
        });
    }

}

