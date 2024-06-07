package com.rodriguezalonso.rommiesapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rodriguezalonso.rommiesapp.MainActivity;
import com.rodriguezalonso.rommiesapp.R;
import com.rodriguezalonso.rommiesapp.logica.Usuario;

public class ProfileEditActivity extends AppCompatActivity {
    private EditText editTextNombre, editTextApellidos, editTextEdad,
            editTextCiudad, editTextCentroEstudio;
    private TextView textViewCorreo;
    private DatabaseReference mDatabase;
    private String correo, id = "";
    private FirebaseAuth autentificacion;
    private Usuario usuario = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        editTextNombre = findViewById(R.id.editTextNombre);
        editTextApellidos = findViewById(R.id.editTextApellidos);
        editTextEdad = findViewById(R.id.editTextEdad);
        editTextCiudad = findViewById(R.id.editTextCiudad);
        editTextCentroEstudio = findViewById(R.id.editTextCentroEstudio);
        textViewCorreo = findViewById(R.id.textViewCorreo);
        try {
            autentificacion = FirebaseAuth.getInstance();
            mDatabase = FirebaseDatabase.getInstance().getReference();
            correo = autentificacion.getCurrentUser().getEmail();
            usuario = getUsuario(correo);
            rellenarCampos(usuario);
        } catch(Exception e){
            Intent intent = new Intent(ProfileEditActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
    private void rellenarCampos(Usuario usuario) {
        String[] nombreApellidos = new String [2];
        nombreApellidos = usuario.getNombreApellidos().split(" ");
        editTextNombre.setText(nombreApellidos[0]);
        editTextApellidos.setText(nombreApellidos[1]);
        editTextEdad.setText(usuario.getEdad());
        editTextCiudad.setText(usuario.getCiudad());
        editTextCentroEstudio.setText(usuario.getCentroEstudio());
        textViewCorreo.setText(usuario.getCorreo());
    }
    public Usuario getUsuario(String correo){
        DatabaseReference ref =null;
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            ref = database.getReference("Usuarios");
        }
        catch(Exception e){
        }
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    try {
                        String txt = ds.getValue().toString();
                        Usuario u = Usuario.parser(txt);
                        if(u.getCorreo().equals(correo)){
                            usuario = u;
                            id = ds.getKey();
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
    private boolean comprobarCampos(){
        if(editTextNombre.getText().toString()==null ||
                editTextApellidos.getText().toString()==null ||
                editTextEdad.getText().toString()==null ||
                editTextCiudad.getText().toString()==null ||
                editTextCentroEstudio.getText().toString()==null){
            return false;
        }
        else{
            return true;
        }
    }
    private String actualizarUsuario(){
        return editTextNombre.getText().toString() + " " +
                editTextApellidos.getText().toString() + ";" +
                editTextEdad.getText().toString() + ";" +
                editTextCiudad.getText().toString() + ";" +
                editTextCentroEstudio.getText().toString() + ";" +
                textViewCorreo.getText().toString();
    }
    public void actualizar(View view){
        if(comprobarCampos()){
            mDatabase.child("Usuarios").child(id).setValue(actualizarUsuario());
        }
        else{
            Toast.makeText(ProfileEditActivity.this, "Faltan datos", Toast.LENGTH_SHORT).show();
        }
    }
    public void cancelar(View view) {
        Intent intent = new Intent(ProfileEditActivity.this, AppActivity.class);
        startActivity(intent);
        finish();
    }
}