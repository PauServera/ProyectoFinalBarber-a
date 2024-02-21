package com.example.proyectofinalbarberia;

import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreManager {

    private static FirebaseFirestore firestoreInstance;

    // Método para inicializar Firebase y obtener la instancia de Firestore
    public static void initializeFirestore(Context context) {
        if (firestoreInstance == null) {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setProjectId("tu-proyecto-id")
                    .setApplicationId("tu-application-id")
                    .setApiKey("tu-api-key")
                    .build();
            FirebaseApp.initializeApp(context, options);
            firestoreInstance = FirebaseFirestore.getInstance();
        }
    }

    // Método para obtener la instancia de Firestore
    public static FirebaseFirestore getFirestoreInstance() {
        if (firestoreInstance == null) {
            throw new IllegalStateException("Firestore no ha sido inicializado. Llama a initializeFirestore primero.");
        }
        return firestoreInstance;
    }
}