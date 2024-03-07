package com.example.proyectofinalbarberia;

import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreManager {

    private static FirebaseFirestore firestoreInstance;

    public static void initializeFirestore(Context context) {
        if (firestoreInstance == null) {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setProjectId("barberiaproyecto-1fed8")
                    .setApplicationId("com.example.proyectofinalbarberia")
                    .setApiKey("93:ec:88:7f:90:2e:7c:7e:38:c7:2f:a8:a4:03:3c:13:bb:df:bd:c4")
                    .build();
            FirebaseApp.initializeApp(context, options);
            firestoreInstance = FirebaseFirestore.getInstance();
        }
    }

    public static FirebaseFirestore getFirestoreInstance() {
        if (firestoreInstance == null) {
            throw new IllegalStateException("Firestore no ha sido inicializado. Llama a initializeFirestore primero.");
        }
        return firestoreInstance;
    }

    public interface RolUsuarioCallback {
        void onRolUsuarioObtenido(String rolUsuario);
        void onFalloObteniendoRolUsuario(String mensajeError);
    }

    public void obtenerRolUsuario(RolUsuarioCallback callback) {
        String uidUsuario = obtenerUidActualUsuario();

        if (uidUsuario == null) {
            callback.onFalloObteniendoRolUsuario("UID de usuario nulo");
            return;
        }

        DocumentReference usuarioRef = FirebaseFirestore.getInstance().collection("Usuarios").document(uidUsuario);

        usuarioRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String rolUsuario = document.getString("rol");

                    if (rolUsuario != null) {
                        callback.onRolUsuarioObtenido(rolUsuario);
                    } else {
                        callback.onFalloObteniendoRolUsuario("Rol de usuario nulo");
                    }
                } else {
                    callback.onFalloObteniendoRolUsuario("El documento no existe");
                }
            } else {
                callback.onFalloObteniendoRolUsuario("Error al obtener los datos: " + task.getException().getMessage());
            }
        });
    }

    private String obtenerUidActualUsuario() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return (currentUser != null) ? currentUser.getUid() : null;
    }
}