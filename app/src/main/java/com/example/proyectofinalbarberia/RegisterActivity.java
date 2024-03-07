package com.example.proyectofinalbarberia;

import static android.content.ContentValues.TAG;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private Button btnRegister;
    private TextView textViewLogin;
    private FirebaseAuth mAuth;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        cambiarColorBarraDeEstado();
        mAuth = FirebaseAuth.getInstance();

        //Registro
        btnRegister = findViewById(R.id.btnRegister);
        editTextEmail = findViewById(R.id.editTextEmailRegister);
        editTextPassword = findViewById(R.id.editTextPasswordRegister);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextPassword.getText().toString().equals(editTextConfirmPassword.getText().toString())) {
                    String email = editTextEmail.getText().toString();
                    String password = editTextPassword.getText().toString();
                    registrarUsuario(email, password);
                } else{
                    Toast.makeText(RegisterActivity.this, "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Botón para ir al inicio de sesión
        textViewLogin = findViewById(R.id.textViewLogin);
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    //Registro en firebase
    public void registrarUsuario(String email, String password) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Debes rellenar la información de registro", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() >= 6) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d("TAG", "createUserWithEmail:success");
                                Intent intent = new Intent(RegisterActivity.this, rolSelectorActivity.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(RegisterActivity.this, "Registro exitoso.", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.w("TAG", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegisterActivity.this, "Registro fallido.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(RegisterActivity.this, "La contraseña debe tener mínimo 6 caracteres.", Toast.LENGTH_SHORT).show();
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
}