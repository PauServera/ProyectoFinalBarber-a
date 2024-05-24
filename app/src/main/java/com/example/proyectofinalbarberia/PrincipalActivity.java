package com.example.proyectofinalbarberia;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowInsetsController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrincipalActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private FirebaseFirestore db;
    private FirestoreManager databaseManager;
    private List<Barberia> listaBarberias = new ArrayList<>();
    BarberiaAdapter adapter = new BarberiaAdapter(listaBarberias,this);
    BarberiaDialogAdapter adapterDialogo = new BarberiaDialogAdapter(listaBarberias);
    private Uri imageUri;
    private ImageView imageViewBarbershop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        cambiarColorBarraDeEstado();
        databaseManager = new FirestoreManager();

        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Barberías");
        }
        toolbar.setBackgroundColor(getResources().getColor(R.color.toolbar_color));

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new BarberiaAdapter(listaBarberias, this);
        recyclerView.setAdapter(adapter);

        String uidUsuario = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference usuarioRef = FirebaseFirestore.getInstance().collection("Usuarios").document(uidUsuario);

        usuarioRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    List<String> idsBarberias = (List<String>) documentSnapshot.get("barberias");

                    listaBarberias.clear();

                    for (String idBarberia : idsBarberias) {
                        if (idBarberia != null) {
                            DocumentReference barberiaRef = FirebaseFirestore.getInstance().collection("Barberias").document(idBarberia);

                            barberiaRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot barberiaSnapshot) {
                                    if (barberiaSnapshot.exists()) {
                                        Barberia barberia = barberiaSnapshot.toObject(Barberia.class);
                                        listaBarberias.add(barberia);

                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem agregarBarberiaItem = menu.findItem(R.id.agregarbarberia);
        MenuItem agregarBarberoItem = menu.findItem(R.id.agregarBarbero);
        MenuItem establecerHorarioItem = menu.findItem(R.id.modificarHorario);

        // Obtener el rol del usuario y ajustar la visibilidad de los elementos del menú
        databaseManager.obtenerRolUsuario(new FirestoreManager.RolUsuarioCallback() {
            @Override
            public void onRolUsuarioObtenido(String rolUsuario) {
                if (rolUsuario.equals("Dueño")) {
                    establecerHorarioItem.setVisible(true);
                    agregarBarberoItem.setVisible(true);
                    agregarBarberiaItem.setVisible(true);
                } else if (rolUsuario.equals("Barbero")) {
                    establecerHorarioItem.setVisible(true);
                    agregarBarberoItem.setVisible(false);
                    agregarBarberiaItem.setVisible(true);
                } else if (rolUsuario.equals("Cliente")) {
                    agregarBarberiaItem.setVisible(true);
                    agregarBarberoItem.setVisible(false);
                    establecerHorarioItem.setVisible(false);
                }
            }

            @Override
            public void onFalloObteniendoRolUsuario(String mensajeError) {
                Toast.makeText(PrincipalActivity.this, "Ha ocurrido un error, inténtalo de nuevo.", Toast.LENGTH_SHORT).show();
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.agregarbarberia) {
            databaseManager.obtenerRolUsuario(new FirestoreManager.RolUsuarioCallback() {
                @Override
                public void onRolUsuarioObtenido(String rolUsuario) {
                    if (rolUsuario.equals("Barbero") || rolUsuario.equals("Cliente")) {
                        mostrarDialogoBuscadorBarberias();
                    } else {
                        mostrarDialogoAgregarBarberia();
                    }
                }

                @Override
                public void onFalloObteniendoRolUsuario(String mensajeError) {
                    Toast.makeText(PrincipalActivity.this, "Ha ocurrido un error, inténtalo de nuevo.", Toast.LENGTH_SHORT).show();
                }
            });
            return true;
        } else if (itemId == R.id.cerrarSesion) {
            cerrarSesion();
            return true;
        } else if (itemId == R.id.miperfil) {
            Intent intent = new Intent(PrincipalActivity.this, PerfilActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            return true;
        } else if (itemId == R.id.misCitas) {
            Intent intent = new Intent(PrincipalActivity.this, MisCitasActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            return true;
        } else if (itemId == R.id.modificarHorario) {
            Intent intent = new Intent(PrincipalActivity.this, DefinirHorarioActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            return true;
        } else if (itemId == R.id.agregarBarbero) {
            Intent intent = new Intent(PrincipalActivity.this, AgregarBarberoActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
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

    private void cerrarSesion() {
        FirebaseAuth.getInstance().signOut();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.sign_in_with_google))
                .requestEmail()
                .build();

        GoogleSignInClient googleSign = GoogleSignIn.getClient(this, gso);

        googleSign.signOut();

        Intent intent = new Intent(PrincipalActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private void mostrarDialogoAgregarBarberia() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Agregar barbería");

        View dialogView = getLayoutInflater().inflate(R.layout.dialogaddbarberia, null);
        builder.setView(dialogView);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        imageViewBarbershop = dialogView.findViewById(R.id.imageViewBarbershop);

        builder.setPositiveButton("Agregar", null);

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button btnSelectImage = dialogView.findViewById(R.id.btnSelectImage);
                btnSelectImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        seleccionarImagen();
                    }
                });

                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText editTextBarbershopName = dialogView.findViewById(R.id.editTextBarbershopName);
                        EditText editTextBarbershopLocation = dialogView.findViewById(R.id.editTextBarbershopLocation);
                        EditText editTextBarbershopPhoneNumber = dialogView.findViewById(R.id.editTextBarbershopPhoneNumber);

                        String nombreBarberia = editTextBarbershopName.getText().toString().trim();
                        String ubicacionBarberia = editTextBarbershopLocation.getText().toString().trim();
                        String telefonoBarberia = editTextBarbershopPhoneNumber.getText().toString().trim();

                        if (nombreBarberia.isEmpty()) {
                            editTextBarbershopName.setError("El nombre es obligatorio");
                            editTextBarbershopName.requestFocus();
                            return;
                        }

                        if (ubicacionBarberia.isEmpty()) {
                            editTextBarbershopLocation.setError("La ubicación es obligatoria");
                            editTextBarbershopLocation.requestFocus();
                            return;
                        }

                        if (telefonoBarberia.isEmpty()) {
                            editTextBarbershopPhoneNumber.setError("El teléfono es obligatorio");
                            editTextBarbershopPhoneNumber.requestFocus();
                            return;
                        }

                        if (!telefonoBarberia.matches("\\d{9}")) {
                            editTextBarbershopPhoneNumber.setError("El teléfono debe tener 9 dígitos");
                            editTextBarbershopPhoneNumber.requestFocus();
                            return;
                        }

                        if (imageUri != null) {
                            subirImagenAFirebaseStorage(imageUri, nombreBarberia, ubicacionBarberia, telefonoBarberia);
                        } else {
                            Toast.makeText(PrincipalActivity.this, "Selecciona una imagen para la barbería", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        dialog.show();
    }

    private void mostrarDialogoBuscadorBarberias() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("BarberTeam");

        View dialogView = getLayoutInflater().inflate(R.layout.dialogbuscarbarberia, null);
        builder.setView(dialogView);

        SearchView searchViewBarberias = dialogView.findViewById(R.id.searchViewBarberias);
        RecyclerView recyclerViewBarberias = dialogView.findViewById(R.id.recyclerViewBarberias);

        CollectionReference barberiasCollection = FirebaseFirestore.getInstance().collection("Barberias");

        barberiasCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Barberia> listaBarberias = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Barberia barberia = document.toObject(Barberia.class);
                    listaBarberias.add(barberia);
                }

                recyclerViewBarberias.setAdapter(adapterDialogo);
                recyclerViewBarberias.setLayoutManager(new LinearLayoutManager(this));

                adapterDialogo.setBarberias(listaBarberias);

                // SearchView
                searchViewBarberias.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        // Filtrar las barberías en el adaptador del diálogo según el texto ingresado
                        adapterDialogo.getFilter().filter(newText);
                        return true;
                    }
                });
            } else {
                Toast.makeText(PrincipalActivity.this, "Ha habido un error al obtener los datos.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private List<Barberia> filtrarBarberias(List<Barberia> listaCompleta, String query) {
        List<Barberia> listaFiltrada = new ArrayList<>();
        for (Barberia barberia : listaCompleta) {
            if (barberia.getNombre().toLowerCase().contains(query.toLowerCase())) {
                listaFiltrada.add(barberia);
            }
        }
        return listaFiltrada;
    }

    private String obtenerUidActualUsuario() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return (currentUser != null) ? currentUser.getUid() : null;
    }

    private void seleccionarImagen() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageViewBarbershop.setImageURI(imageUri);
        }
    }

    private void subirImagenAFirebaseStorage(Uri imageUri, String nombreBarberia, String ubicacionBarberia, String telefonoBarberia) {
        String uid = obtenerUidActualUsuario();
        if (uid != null) {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images/" + uid + "/barberia.jpg");
            storageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();
                            agregarBarberiaFirestore(nombreBarberia, ubicacionBarberia, telefonoBarberia, imageUrl);
                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(PrincipalActivity.this, "Error al subir la imagen.", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void agregarBarberiaFirestore(String nombreBarberia, String ubicacionBarberia, String telefonoBarberia, String imageUrl) {
        String uidUsuario = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (uidUsuario != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            DocumentReference usuarioRef = db.collection("Usuarios").document(uidUsuario);

            usuarioRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    List<String> barberias = (List<String>) documentSnapshot.get("barberias");
                    if (barberias != null && !barberias.isEmpty()) {
                        Toast.makeText(PrincipalActivity.this, "Imposible, ya tienes una barbería registrada.", Toast.LENGTH_LONG).show();
                    } else {
                        DocumentReference barberiaRef = db.collection("Barberias").document();
                        String uidBarberia = barberiaRef.getId();

                        List<Cita> listaCitas = new ArrayList<>();
                        List<String> listaBarberos = new ArrayList<>();
                        listaBarberos.add(uidUsuario); // Agregar el UID del dueño a la lista de barberos

                        Barberia nuevaBarberia = new Barberia(uidBarberia, nombreBarberia, ubicacionBarberia, telefonoBarberia, imageUrl, listaBarberos, listaCitas);

                        Log.d("FirestoreDebug", "Barberia: " + nuevaBarberia.getNombre());
                        Log.d("FirestoreDebug", "ListaBarberos: " + nuevaBarberia.getListaBarberos().toString());
                        Log.d("FirestoreDebug", "ListaCitas: " + nuevaBarberia.getListaCitas().toString());

                        barberiaRef.set(nuevaBarberia)
                                .addOnSuccessListener(aVoid -> {
                                    usuarioRef.update("barberias", FieldValue.arrayUnion(uidBarberia))
                                            .addOnSuccessListener(aVoid1 -> {
                                                Toast.makeText(PrincipalActivity.this, "Éxito al añadir la barbería.", Toast.LENGTH_SHORT).show();
                                            })
                                            .addOnFailureListener(e -> {
                                                Toast.makeText(PrincipalActivity.this, "Error al actualizar la información del usuario", Toast.LENGTH_SHORT).show();
                                            });
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(PrincipalActivity.this, "Error al agregar la barbería a Firestore", Toast.LENGTH_SHORT).show();
                                });
                    }
                } else {
                    Toast.makeText(PrincipalActivity.this, "Error al obtener la información del usuario", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(PrincipalActivity.this, "Error al consultar la información del usuario", Toast.LENGTH_SHORT).show();
            });
        }
    }
}
