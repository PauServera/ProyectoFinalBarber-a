package com.example.proyectofinalbarberia;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowInsetsController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class PerfilActivity extends AppCompatActivity {

    private TextView textViewName;
    private TextView textViewContactInfo;
    private TextView textViewPhone;
    private FirebaseFirestore firestore;
    private FirestoreManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        cambiarColorBarraDeEstado();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Mi perfil");
        }

        textViewName = findViewById(R.id.textViewName);
        textViewContactInfo = findViewById(R.id.textViewContactInfo);
        textViewPhone = findViewById(R.id.textViewPhone);
        firestore = FirebaseFirestore.getInstance();
        Button botoneditarPerfil = findViewById(R.id.buttonEditProfile);

        ImageView imageViewProfile = findViewById(R.id.imageViewProfile);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference userRef = firestore.collection("Usuarios").document(uid);

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String imageUrl = documentSnapshot.getString("urlImagenPerfil");

                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        Glide.with(PerfilActivity.this /* Context */)
                                .load(imageUrl)
                                .circleCrop()
                                .into(imageViewProfile);
                    } else {
                        //si la imagen no está disponible
                    }
                }
            }
        });

        botoneditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoEditarPerfil();
            }
        });

        getUserInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profilemenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.barberias) {
            Intent intent = new Intent(PerfilActivity.this, PrincipalActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.cambiarcontraseña) {
            abrirDialogoCambiarContraseña();
            return true;
        } else if (id == R.id.editarperfil) {
            mostrarDialogoEditarPerfil();
            return true;
        } else if (id == R.id.cerrarSesion) {
            cerrarSesion();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


    private void getUserInfo() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        firestore.collection("Usuarios").document(uid).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String nombre = document.getString("nombre");
                            String email = document.getString("email");
                            String telefono = document.getString("telefono");

                            textViewName.setText(nombre);
                            textViewContactInfo.setText("Correo electrónico: " + email);
                            textViewPhone.setText("Teléfono: " + telefono);
                        } else {
                            Toast.makeText(PerfilActivity.this, "No se encontró información del usuario", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(PerfilActivity.this, "Error al obtener información del usuario: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
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

        Intent intent = new Intent(PerfilActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
    private void abrirDialogoCambiarContraseña() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && user.getProviderData() != null) {
            for (UserInfo userInfo : user.getProviderData()) {
                if (userInfo.getProviderId().equals(GoogleAuthProvider.PROVIDER_ID)) {
                    // La cuenta está vinculada a Google
                    mostrarMensajeCuentaVinculadaGoogle();
                    return;
                }
            }
        }
        // Si no está vinculada a Google, abrir el diálogo de cambio de contraseña
        AlertDialog dialog = createCambiarContraseñaDialog();
        dialog.show();
    }


    private AlertDialog createCambiarContraseñaDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialogcambiarpass, null);

        EditText editTextCurrentPassword = dialogView.findViewById(R.id.editTextCurrentPassword);
        EditText editTextNewPassword = dialogView.findViewById(R.id.editTextNewPassword);
        EditText editTextConfirmPassword = dialogView.findViewById(R.id.editTextConfirmPassword);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setCancelable(true)
                .setPositiveButton("Cambiar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String currentPassword = editTextCurrentPassword.getText().toString();
                        String newPassword = editTextNewPassword.getText().toString();
                        String confirmPassword = editTextConfirmPassword.getText().toString();
                        cambiarContrasena(currentPassword, newPassword, confirmPassword);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        // Crear el diálogo
        AlertDialog dialog = builder.create();

        // Cambiar el color del texto de los botones a blanco
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                if (positiveButton != null) {
                    positiveButton.setTextColor(Color.WHITE);
                }
                Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                if (negativeButton != null) {
                    negativeButton.setTextColor(Color.WHITE);
                }
            }
        });

        return dialog;
    }


    private void cambiarContrasena(String currentPassword, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(PerfilActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);
        user.reauthenticate(credential)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        user.updatePassword(newPassword)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Contraseña cambiada exitosamente
                                        Toast.makeText(PerfilActivity.this, "Contraseña cambiada correctamente", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Error al cambiar la contraseña
                                        Toast.makeText(PerfilActivity.this, "Error al cambiar la contraseña: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error de reautenticación
                        Toast.makeText(PerfilActivity.this, "Error de reautenticación: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void mostrarMensajeCuentaVinculadaGoogle() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cuenta vinculada a Google");
        builder.setMessage("Tu cuenta está vinculada a Google. No es posible cambiar la contraseña desde esta aplicación.");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void mostrarDialogoEditarPerfil() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialogeditarperfil, null);
        builder.setView(dialogView);

        Button btnCambiarNombre = dialogView.findViewById(R.id.btnCambiarNombre);
        Button btnCambiarCorreo = dialogView.findViewById(R.id.btnCambiarCorreo);
        Button btnCambiarImagen = dialogView.findViewById(R.id.btnCambiarImagen);
        Button btnCambiarTelefono = dialogView.findViewById(R.id.btnCambiarTelefono);


        AlertDialog dialog = builder.create();
        dialog.show();

        btnCambiarNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                mostrarDialogoCambiarNombre();
            }
        });

        btnCambiarCorreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                mostrarDialogoCambiarCorreo();
            }
        });

        btnCambiarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                seleccionarImagenPerfil();
            }
        });

        btnCambiarTelefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                mostrarDialogoCambiarTelefono();
            }
        });
    }

    private void mostrarDialogoCambiarNombre() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialogcambiarnombre, null);
        builder.setView(dialogView);

        EditText editTextNuevoNombre = dialogView.findViewById(R.id.editTextNuevoNombre);

        builder.setPositiveButton("Cambiar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nuevoNombre = editTextNuevoNombre.getText().toString().trim();
                if (!nuevoNombre.isEmpty()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        // Actualizar el nombre en Firebase Authentication
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(nuevoNombre)
                                .build();

                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // Actualizar el nombre en Firestore
                                            String uid = user.getUid();
                                            DocumentReference userRef = firestore.collection("Usuarios").document(uid);
                                            userRef.update("nombre", nuevoNombre)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(PerfilActivity.this, "Nombre cambiado exitosamente", Toast.LENGTH_SHORT).show();
                                                                finish();
                                                                startActivity(getIntent());
                                                            } else {
                                                                Toast.makeText(PerfilActivity.this, "Error al cambiar el nombre en Firestore: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                        } else {
                                            Toast.makeText(PerfilActivity.this, "Error al cambiar el nombre en Authentication: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                } else {
                    Toast.makeText(PerfilActivity.this, "Por favor ingresa un nuevo nombre", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    private void mostrarDialogoCambiarCorreo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialogcambiarcorreo, null);
        builder.setView(dialogView);

        EditText editTextNuevoCorreo = dialogView.findViewById(R.id.editTextNuevoCorreo);

        builder.setPositiveButton("Cambiar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nuevoCorreo = editTextNuevoCorreo.getText().toString().trim();
                if (!nuevoCorreo.isEmpty()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        user.updateEmail(nuevoCorreo)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(PerfilActivity.this, "Correo cambiado exitosamente", Toast.LENGTH_SHORT).show();
                                            finish();
                                            startActivity(getIntent());
                                        } else {
                                            Toast.makeText(PerfilActivity.this, "Error al cambiar el correo: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                } else {
                    Toast.makeText(PerfilActivity.this, "Por favor ingresa un nuevo correo", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }


    private static final int REQUEST_IMAGE_PICK = 1;

    private void seleccionarImagenPerfil() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                eliminarImagenAnteriorYSubirNuevaImagen(imageUri);
            } else {
                Toast.makeText(this, "Error al seleccionar la imagen. Inténtalo de nuevo.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void eliminarImagenAnteriorYSubirNuevaImagen(Uri imageUri) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            firestore.collection("Usuarios").document(user.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        String previousImageUrl = documentSnapshot.getString("urlImagenPerfil");
                        if (previousImageUrl != null && !previousImageUrl.isEmpty()) {
                            StorageReference previousImageRef = FirebaseStorage.getInstance().getReferenceFromUrl(previousImageUrl);
                            previousImageRef.delete()
                                    .addOnSuccessListener(aVoid -> {
                                        cargarNuevaImagen(imageUri);
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(PerfilActivity.this, "Error al eliminar la imagen anterior: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            // No hay imagen anterior, cargar directamente la nueva imagen
                            cargarNuevaImagen(imageUri);
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(PerfilActivity.this, "Error al obtener la URL de la imagen anterior: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void cargarNuevaImagen(Uri imageUri) {
        if (imageUri == null) {
            Toast.makeText(this, "Error: La URI de la imagen es nula.", Toast.LENGTH_SHORT).show();
            return;
        }
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageRef.child("images/" + UUID.randomUUID().toString());
        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                            firestore.collection("Usuarios").document(user.getUid())
                                    .update("urlImagenPerfil", imageUrl)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(PerfilActivity.this, "Imagen de perfil actualizada", Toast.LENGTH_SHORT).show();
                                        // Actualiza la actividad para reflejar los cambios
                                        finish();
                                        startActivity(getIntent());
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(PerfilActivity.this, "Error al actualizar la imagen de perfil: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }
                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(PerfilActivity.this, "Error al subir la imagen: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void mostrarDialogoCambiarTelefono() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialogcambiartelefono, null);
        builder.setView(dialogView);

        EditText editTextNuevoTelefono = dialogView.findViewById(R.id.editTextNuevoTelefono);

        builder.setPositiveButton("Cambiar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nuevoTelefono = editTextNuevoTelefono.getText().toString().trim();
                if (!nuevoTelefono.isEmpty()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                        firestore.collection("Usuarios").document(user.getUid())
                                .update("telefono", nuevoTelefono)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(PerfilActivity.this, "Teléfono cambiado exitosamente", Toast.LENGTH_SHORT).show();
                                    getUserInfo(); // Actualiza la información del usuario en la pantalla
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(PerfilActivity.this, "Error al cambiar el teléfono: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    }
                } else {
                    Toast.makeText(PerfilActivity.this, "Por favor ingresa un nuevo teléfono", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }
}
