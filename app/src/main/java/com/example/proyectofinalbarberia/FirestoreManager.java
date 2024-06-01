package com.example.proyectofinalbarberia;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FirestoreManager {

    private static FirebaseFirestore firestoreInstance;
    private FirebaseAuth auth;

    public FirestoreManager() {
        firestoreInstance = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    public static void initializeFirestore(Context context) {
        if (firestoreInstance == null) {
            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build();
            firestoreInstance = FirebaseFirestore.getInstance();
            firestoreInstance.setFirestoreSettings(settings);
        }
    }

    public static FirebaseFirestore getFirestoreInstance() {
        if (firestoreInstance == null) {
            throw new IllegalStateException("Firestore no ha sido inicializado. Llama a initializeFirestore primero.");
        }
        return firestoreInstance;
    }

    public String obtenerUidActualUsuario() {
        FirebaseUser currentUser = auth.getCurrentUser();
        return (currentUser != null) ? currentUser.getUid() : null;
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

        DocumentReference usuarioRef = firestoreInstance.collection("Usuarios").document(uidUsuario);

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

    public void agregarBarberiaALista(Barberia barberia) {
        String uidUsuario = obtenerUidActualUsuario();

        if (uidUsuario != null) {
            DocumentReference usuarioRef = firestoreInstance.collection("Usuarios").document(uidUsuario);

            usuarioRef.update("barberias", FieldValue.arrayUnion(barberia.getNombre()))
                    .addOnSuccessListener(aVoid -> {
                        // éxito
                    })
                    .addOnFailureListener(e -> {
                        // error
                    });
        }
    }

    public interface BarberiaCallback {
        void onBarberiaObtenida(Barberia barberia);
        void onFalloObteniendoBarberia(String mensajeError);
    }

    public void obtenerInformacionBarberia(String idBarberia, BarberiaCallback callback) {
        DocumentReference barberiaRef = firestoreInstance.collection("Barberias").document(idBarberia);

        barberiaRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Barberia barberia = documentSnapshot.toObject(Barberia.class);
                if (barberia != null) {
                    callback.onBarberiaObtenida(barberia);
                } else {
                    callback.onFalloObteniendoBarberia("Error al convertir datos de barbería");
                }
            } else {
                callback.onFalloObteniendoBarberia("No se encontró la barbería con ID: " + idBarberia);
            }
        }).addOnFailureListener(e -> {
            callback.onFalloObteniendoBarberia("Error al obtener datos de la barbería: " + e.getMessage());
        });
    }

    public void eliminarBarberia(String idBarberia, String rolUsuario, final OnEliminarBarberiaListener listener) {
        if (rolUsuario != null && rolUsuario.equals("Dueño")) {
            eliminarBarberiaDeListaUsuario(idBarberia, rolUsuario, listener);
            eliminarBarberiaDeColeccion(idBarberia, listener);
        } else {
            eliminarBarberiaDeListaUsuario(idBarberia, rolUsuario, listener);
        }
    }

    private void eliminarBarberiaDeListaUsuario(String idBarberia, String rolUsuario, final OnEliminarBarberiaListener listener) {
        String uidUsuario = obtenerUidActualUsuario();

        if (uidUsuario != null) {
            DocumentReference usuarioRef = firestoreInstance.collection("Usuarios").document(uidUsuario);

            usuarioRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            if (document.contains("barberias")) {
                                @SuppressWarnings("unchecked")
                                List<String> barberias = (List<String>) document.get("barberias");
                                if (barberias != null && barberias.contains(idBarberia)) {
                                    barberias.remove(idBarberia);

                                    usuarioRef.update("barberias", barberias)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        listener.onBarberiaEliminada();
                                                    } else {
                                                        listener.onEliminarBarberiaError("Error al actualizar el documento del usuario");
                                                    }
                                                }
                                            });
                                } else {
                                    listener.onEliminarBarberiaError("La barbería no está en la lista del usuario");
                                }
                            } else {
                                listener.onEliminarBarberiaError("No se encontró la lista de barberías en el documento del usuario");
                            }
                        } else {
                            listener.onEliminarBarberiaError("No se encontró el documento del usuario");
                        }
                    } else {
                        listener.onEliminarBarberiaError("Error al obtener el documento del usuario: " + task.getException().getMessage());
                    }
                }
            });
        }
    }

    private void eliminarBarberiaDeColeccion(String idBarberia, final OnEliminarBarberiaListener listener) {
        FirebaseFirestore.getInstance().collection("Barberias").document(idBarberia)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    listener.onBarberiaEliminada();
                })
                .addOnFailureListener(e -> {
                    listener.onEliminarBarberiaError("Error al eliminar la barbería de la colección: " + e.getMessage());
                });
    }

    public void modificarBarberia(String idBarberia, Barberia barberiaModificada, final OnModificarBarberiaListener listener) {
        firestoreInstance.collection("Barberias").document(idBarberia)
                .set(barberiaModificada)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            listener.onBarberiaModificada();
                        } else {
                            listener.onModificarBarberiaError("Error al modificar la barbería: " + task.getException().getMessage());
                        }
                    }
                });
    }

    public interface OnEliminarBarberiaListener {
        void onBarberiaEliminada();
        void onEliminarBarberiaError(String mensajeError);
    }

    public interface OnModificarBarberiaListener {
        void onBarberiaModificada();
        void onModificarBarberiaError(String mensajeError);
    }

    public void obtenerUidUsuarioPorCorreo(String correoElectronico, UidUsuarioCallback callback) {
        firestoreInstance.collection("Usuarios")
                .whereEqualTo("email", correoElectronico)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String uidUsuario = document.getId();
                            callback.onUidUsuarioObtenido(uidUsuario);
                            return;
                        }
                        callback.onUidUsuarioObtenido(null);
                    } else {
                        callback.onFalloObteniendoUidUsuario("Error al obtener el UID del usuario");
                    }
                });
    }

    public interface UidUsuarioCallback {
        void onUidUsuarioObtenido(String uidUsuario);

        void onFalloObteniendoUidUsuario(String mensajeError);
    }

    public void agregarBarberiaAUsuario(String uidUsuario, String uidBarberia, AgregarBarberiaCallback callback) {
        DocumentReference usuarioRef = firestoreInstance.collection("Usuarios").document(uidUsuario);

        usuarioRef.update("barberias", FieldValue.arrayUnion(uidBarberia))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        callback.onBarberiaAgregada();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onFalloAgregandoBarberia(e.getMessage());
                    }
                });
    }

    public void agregarBarberoABarberia(String uidBarberia, String uidBarbero, AgregarBarberoCallback callback) {
        DocumentReference barberiaRef = firestoreInstance.collection("Barberias").document(uidBarberia);

        barberiaRef.update("listaBarberos", FieldValue.arrayUnion(uidBarbero))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        callback.onBarberoAgregado();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onFalloAgregandoBarbero(e.getMessage());
                    }
                });
    }

    public interface AgregarBarberiaCallback {
        void onBarberiaAgregada();
        void onFalloAgregandoBarberia(String mensajeError);
    }

    public interface AgregarBarberoCallback {
        void onBarberoAgregado();
        void onFalloAgregandoBarbero(String mensajeError);
    }

    public void agregarCitaAUsuario(String uidUsuario, String uidCita) {
        DocumentReference usuarioRef = firestoreInstance.collection("Usuarios").document(uidUsuario);
        usuarioRef.update("listaCitas", FieldValue.arrayUnion(uidCita))
                .addOnSuccessListener(aVoid -> Log.d("FirestoreManager", "Cita agregada a la lista de usuario"))
                .addOnFailureListener(e -> Log.e("FirestoreManager", "Error al agregar cita a la lista de usuario", e));
    }

    public void obtenerCitasDeUsuario(String uidUsuario, ObtenerCitasCallback callback) {
        DocumentReference usuarioRef = firestoreInstance.collection("Usuarios").document(uidUsuario);
        usuarioRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                List<String> listaCitas = (List<String>) documentSnapshot.get("listaCitas");
                if (listaCitas != null) {
                    callback.onCitasObtenidas(listaCitas);
                } else {
                    callback.onFallo("Lista de citas vacía");
                }
            } else {
                callback.onFallo("Documento de usuario no encontrado");
            }
        }).addOnFailureListener(e -> callback.onFallo("Error al obtener citas del usuario: " + e.getMessage()));
    }

    public interface ObtenerCitasCallback {
        void onCitasObtenidas(List<String> listaCitas);
        void onFallo(String mensajeError);
    }

    public void obtenerCitasParaRol(String rol, String uid, ObtenerCitasCallback callback) {
        CollectionReference citasRef = firestoreInstance.collection("Citas");
        Query query;
        if ("Dueño".equals(rol)) {
            query = citasRef.whereEqualTo("idBarberia", uid);
        } else if ("Barbero".equals(rol)) {
            query = citasRef.whereEqualTo("idBarbero", uid);
        } else { // Cliente
            query = citasRef.whereEqualTo("idCliente", uid);
        }

        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<String> listaCitas = new ArrayList<>();
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                listaCitas.add(documentSnapshot.getId());
            }
            callback.onCitasObtenidas(listaCitas);
        }).addOnFailureListener(e -> {
            callback.onFallo(e.getMessage());
        });
    }
}
