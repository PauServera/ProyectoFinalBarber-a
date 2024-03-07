package com.example.proyectofinalbarberia;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowInsetsController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.FileReader;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private Button btnLogin;
    private TextView textviewRegister;
    private FirebaseAuth mAuth;
    private EditText editTextemail;
    private EditText editTextpassword;
    private SignInButton btnGoogleSignIn;

    private static final int RC_SIGN_IN = 123;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cambiarColorBarraDeEstado();
        mAuth = FirebaseAuth.getInstance();
        editTextemail = findViewById(R.id.editTextEmail);
        editTextpassword = findViewById(R.id.editTextPassword);

        //Login
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextemail.getText().toString();
                String password = editTextpassword.getText().toString();
                iniciarSesion(email, password);
            }
        });

        // Para ir a la pantalla de registro
        textviewRegister = findViewById(R.id.textViewRegister);
        textviewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // inicio de sesión con Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        btnGoogleSignIn = findViewById(R.id.btnGoogleSignIn);
        btnGoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        activityResultLauncher.launch(signInIntent);
    }
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            o -> {
                if (o.getResultCode() == RESULT_OK) {
                    Intent data = o.getData();
                    GoogleSignInAccount account;
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    try {
                        account = task.getResult(ApiException.class);
                        firebaseAuthWithGoogle(account);
                    } catch (ApiException ignored) {
                    }
                }
            });

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        String usuarioActual = obtenerUidActualUsuario();
                        verificarExistenciaUidEnFirestore(usuarioActual);
                    } else {
                        FirebaseAuthException e = (FirebaseAuthException) task.getException();
                        Toast.makeText(this, Objects.requireNonNull(e).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Método para iniciar sesión con Firebase
    public void iniciarSesion(String email, String password) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Debes rellenar la información de inicio de sesión.", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "signInWithEmail:success");
                            Intent intent = new Intent(MainActivity.this, PrincipalActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(MainActivity.this, "¡Bienvenido de nuevo!", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Inicio de sesión fallido", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Otros métodos para cambiar el color de la barra de estado
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

    private void verificarExistenciaUidEnFirestore(String uid) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usuariosRef = db.collection("Usuarios");

        usuariosRef.document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // El UID ya existe en la colección "Usuarios"
                        // Redirigir a otra actividad
                        Intent otroIntent = new Intent(MainActivity.this, PrincipalActivity.class);
                        startActivity(otroIntent);
                        finish();
                    } else {
                        Intent intentRegistro = new Intent(MainActivity.this, rolSelectorActivity.class);
                        startActivity(intentRegistro);
                        finish();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Error al verificar UID en Firestore", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String obtenerUidActualUsuario() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return (currentUser != null) ? currentUser.getUid() : null;
    }
}
