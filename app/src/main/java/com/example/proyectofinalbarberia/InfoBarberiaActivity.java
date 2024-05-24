package com.example.proyectofinalbarberia;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowInsetsController;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CalendarView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class InfoBarberiaActivity extends AppCompatActivity {

    private ImageView imageViewBarberia;
    private TextView textViewNombre, textViewTelefono, textViewUbicacion, textViewTitle;
    private Toolbar toolbarDetalleBarberia;
    private CalendarView calendarView;
    private FirestoreManager firestoreManager;
    private String uidBarberia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_barberia);
        cambiarColorBarraDeEstado();

        firestoreManager = new FirestoreManager();

        Intent intent = getIntent();
        Barberia barberia = intent.getParcelableExtra("BARBERIA_INFO");
        uidBarberia = barberia != null ? barberia.getId() : null;

        imageViewBarberia = findViewById(R.id.imageViewBarberia);
        textViewNombre = findViewById(R.id.textViewNombre);
        textViewTelefono = findViewById(R.id.textViewTelefono);
        textViewUbicacion = findViewById(R.id.textViewUbicacion);
        textViewTitle = findViewById(R.id.title);
        toolbarDetalleBarberia = findViewById(R.id.toolbar);
        calendarView = findViewById(R.id.calendarView);

        setSupportActionBar(toolbarDetalleBarberia);
        int toolbarColor = getResources().getColor(R.color.toolbar_color);
        getSupportActionBar().setTitle("");
        toolbarDetalleBarberia.setBackgroundColor(toolbarColor);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                onDaySelected(year, month, dayOfMonth);
            }
        });

        firestoreManager.obtenerRolUsuario(new FirestoreManager.RolUsuarioCallback() {
            @Override
            public void onRolUsuarioObtenido(String rolUsuario) {
                if (rolUsuario != null && rolUsuario.equals("Dueño") || rolUsuario.equals("Barbero")) {
                    textViewTitle.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFalloObteniendoRolUsuario(String mensajeError) {
                Toast.makeText(InfoBarberiaActivity.this, "Error al obtener el rol del usuario: " + mensajeError, Toast.LENGTH_SHORT).show();
            }
        });

        if (barberia != null) {
            Glide.with(this).load(barberia.getImageUrl()).into(imageViewBarberia);
            textViewNombre.setText(barberia.getNombre());
            textViewTelefono.setText(barberia.getTelefono());
            textViewUbicacion.setText(barberia.getUbicacion());
        }
    }

    private void onDaySelected(int year, int month, int dayOfMonth) {
        Intent intent = new Intent(InfoBarberiaActivity.this, SeleccionarBarberoActivity.class);
        intent.putExtra("UID_BARBERIA", uidBarberia);
        intent.putExtra("SELECTED_DATE", String.format("%d/%d/%d", dayOfMonth, month + 1, year));
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
