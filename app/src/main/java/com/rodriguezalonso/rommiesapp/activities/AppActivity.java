package com.rodriguezalonso.rommiesapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rodriguezalonso.rommiesapp.MainActivity;
import com.rodriguezalonso.rommiesapp.logica.PisoAdapter;
import com.rodriguezalonso.rommiesapp.logica.PisoListModel;
import com.rodriguezalonso.rommiesapp.R;
import com.rodriguezalonso.rommiesapp.logica.Usuario;

import java.util.ArrayList;

public class AppActivity extends AppCompatActivity {
    private Button buttonMenu;
    private EditText editTextCiudad;
    private ListView listViewPisos;
    private PisoAdapter adapter;
    private ArrayList<PisoListModel> listaPisos = new ArrayList<>();
    private FirebaseDatabase database;
    private FirebaseUser user;
    private Usuario usuario=null;
    private AlertDialog dialog;
    private String idUsuario="", idPiso=null, correo="";
    private PisoListModel pisoUsuario=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        buttonMenu = findViewById(R.id.buttonMenu);
        editTextCiudad = findViewById(R.id.editTextCiudad);
        listViewPisos = findViewById(R.id.listViewPisos);

        database = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        correo=user.getEmail();
        usuario = getUsuario(correo);
        cargarPisosFirebase();

        new Handler().postDelayed(() -> {
            adapter = new PisoAdapter(this, listaPisos);
            listViewPisos.setAdapter(adapter);
        }, 4000);
    }

    /**
     * Método para leer los pisos en Firebase y añadirlos a un ArrayList
     * para poder hacer operaciones más fácilmente
     */
    private void cargarPisosFirebase(){
        DatabaseReference ref =FirebaseDatabase.getInstance().getReference("Pisos");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String txt = ds.getValue().toString();
                    PisoListModel p = PisoListModel.parser(txt);
                    if (p!=null) {
                        listaPisos.add(p);
                        //Se hace una comprobación para ver si el usuario tiene un piso asignado
                        if (p.getContacto().equals(correo)) {
                            idPiso = ds.getKey();
                            pisoUsuario=p;
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AppActivity.this,"Error al cargar los pisos",Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Método para leer los usuarios en Firebase y devolver el perfil del usuario actual
     * @param correo
     * @return
     */
    public Usuario getUsuario(String correo){
        DatabaseReference ref =null;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference("Usuarios");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    try {
                        String txt = ds.getValue().toString();
                        Usuario u = Usuario.parser(txt);
                        if(u.getCorreo().equals(correo)){
                            usuario = u;
                            idUsuario = ds.getKey();
                        } else{
                            usuario = null;
                        }
                    } catch (Exception e) {
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return usuario;
    }

    /**
     * Método onClick() para mostrar una lista de pisos en función de la ciudad indicada
     * @param view
     */
    public void mostrarPisos(View view) {
        String ciudad = editTextCiudad.getText().toString();
        ArrayList<PisoListModel> listaFiltrada = new ArrayList<>();
        for (PisoListModel plm:listaPisos){
            if (plm.getCiudad().equals(ciudad))
                listaFiltrada.add(plm);
        }
        adapter = new PisoAdapter(this, listaFiltrada);
        listViewPisos.setAdapter(adapter);
    }

    /**
     * Método onClick() para abrir la pantalla de edición del perfil
     * Se pasan el usuario y su id junto con el correo para operar
     * más fácilmente
     * @param view
     */
    public void editarPerfil(View view) {
        Intent intent = new Intent(AppActivity.this, ProfileEditActivity.class);
        intent.putExtra("idUsuario",idUsuario);
        intent.putExtra("Usuario",usuario);
        intent.putExtra("Correo",correo);
        startActivity(intent);
    }
    /**
     * Método onClick() para abrir la pantalla de edición del piso
     * Se pasan el piso y su id junto con el correo para operar
     * más fácilmente
     * @param view
     */
    public void editarPiso(View view) {
        Intent intent = new Intent(AppActivity.this, ApartmentEditActivity.class);
        intent.putExtra("idPiso",idPiso);
        intent.putExtra("Correo",correo);
        intent.putExtra("Piso",pisoUsuario);
        startActivity(intent);
    }
    /**
     * Método onClick() para cerrar la sesión actual del usuario
     * @param view
     */
    public void cerrarSesion(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(AppActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    /**
     * Método onClick() para abrir un prompt antes de borrar la cuenta
     * El prompt es a modo de confirmación ante un cambio irreversible
     * @param view
     */
    public void promptBorrarCuenta(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AppActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.prompt_confirm_delete, null);
        builder.setView(v);
        Button cancelar = v.findViewById(R.id.buttonCancelarLI);
        Button aceptar = v.findViewById(R.id.buttonGoogleMaps);
        cancelar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                }
        );
        aceptar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        borrarDatos();
                    }
                }
        );
        dialog = builder.create();
        dialog.show();
    }

    /**
     * Método para borrar los datos del usuario antes de borrar la cuenta definitivamente
     * Primero borra los datos del perfil y luego, si existe, los datos del piso
     */
    public void borrarDatos(){
        if(user!=null){
            DatabaseReference userRef = database.getReference("Usuarios").child(idUsuario);
            userRef.removeValue().addOnCompleteListener(task1 -> {
            if (task1.isSuccessful())
                borrarDatosPiso();
            });
        }
    }
    /**
     * Método para borrar los datos del piso del usuario en caso de tener uno en su cuenta
     */
    public void borrarDatosPiso() {
        if (idPiso != null) {
            DatabaseReference apartmentRef = database.getReference("Pisos").child(idPiso);
            apartmentRef.removeValue().addOnCompleteListener(task2 -> {
                if (task2.isSuccessful()) {
                    borrarCuenta();
                }
            });
        } else{
            borrarCuenta();
        }
    }
    /**
     * Método para borrar la cuenta del usuario de Firebase
     * y volver a la pantalla inicial
     */
    public void borrarCuenta(){
        user.delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(AppActivity.this, "Cuenta borrada",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AppActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    /**
     * Método onClick() para abrir un prompt antes de borrar la cuenta
     * El prompt es a modo de confirmación ante un cambio irreversible
     * @param view
     */
    public void promptAbrirPiso(View view) {
        PisoListModel piso = new PisoListModel();
        AlertDialog.Builder builder = new AlertDialog.Builder(AppActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.prompt_piso, null);
        builder.setView(v);
        Button googleMaps = v.findViewById(R.id.buttonGoogleMaps);
        googleMaps.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*TO DO
                        String direccion = piso.getCiudad() + ", " + piso.getCalle() + " " + piso.getPortal();
                        abrirGoogleMaps(direccion);
                         */
                    }
                }
        );
        dialog = builder.create();
        dialog.show();
    }

    public void abrirGoogleMaps(String direccion) {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(direccion));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }
}