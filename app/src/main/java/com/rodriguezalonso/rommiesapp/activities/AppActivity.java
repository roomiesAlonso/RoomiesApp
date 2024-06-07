package com.rodriguezalonso.rommiesapp.activities;

import android.content.Intent;
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

    private final String default_city="Oviedo";
    private Button buttonMenu;
    private EditText editTextCiudad;
    private ListView listViewPisos;
    private PisoAdapter adapter;
    private ArrayList<PisoListModel> listaPisos = new ArrayList<>();
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private Usuario usuario=null;
    private AlertDialog dialog;
    private String idUsuario="", correo="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        buttonMenu = findViewById(R.id.buttonMenu);
        editTextCiudad = findViewById(R.id.editTextCiudad);
        listViewPisos = findViewById(R.id.listViewPisos);
        correo=FirebaseAuth.getInstance().getCurrentUser().getEmail();
        usuario = getUsuario(correo);
        cargarPisosFirebase();

        new Handler().postDelayed(() -> {
            Toast.makeText(this,"Adaptamos",Toast.LENGTH_SHORT).show();
            adapter = new PisoAdapter(this, listaPisos);
            listViewPisos.setAdapter(adapter);
        }, 4000);
    }

    private void cargarPisosFirebase(){
        DatabaseReference ref =FirebaseDatabase.getInstance().getReference("Pisos");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String txt = ds.getValue().toString();
                    PisoListModel p = PisoListModel.parser(txt);
                    listaPisos.add(p);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AppActivity.this,"Error al cargar los pisos",Toast.LENGTH_LONG).show();
            }
        });
    }

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
    public void editarPerfil(View view) {
        Intent intent = new Intent(AppActivity.this, ProfileEditActivity.class);
        intent.putExtra("Correo",correo);
        startActivity(intent);
    }
    public void editarPiso(View view) {
        Intent intent = new Intent(AppActivity.this, ApartmentEditActivity.class);
        intent.putExtra("Correo",correo);
        startActivity(intent);
    }
    public void cerrarSesion(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(AppActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    public void promptBorrarCuenta(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AppActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.prompt_confirm_delete, null);
        builder.setView(v);
        Button cancelar = v.findViewById(R.id.buttonCancel);
        Button aceptar = v.findViewById(R.id.buttonAccept);
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
                        borrarCuenta();
                    }
                }
        );
        dialog = builder.create();
        dialog.show();
    }

    public void borrarCuenta(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.delete().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    startActivity(new Intent(AppActivity.this, MainActivity.class));
                    finish();
                }
            });
        }
    }
}