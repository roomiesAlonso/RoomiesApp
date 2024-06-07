package com.rodriguezalonso.rommiesapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rodriguezalonso.rommiesapp.MainActivity;
import com.rodriguezalonso.rommiesapp.R;

public class ProfileActivity extends AppCompatActivity {
    private EditText editTextNombre, editTextApellidos, editTextEdad,
            editTextCiudad, editTextCentroEstudio, editTextCorreo;
    private DatabaseReference mDatabase;
    private String correo;
    private FirebaseAuth autentificacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        editTextNombre = findViewById(R.id.editTextNombre);
        editTextApellidos = findViewById(R.id.editTextApellidos);
        editTextEdad = findViewById(R.id.editTextEdad);
        editTextCiudad = findViewById(R.id.editTextCiudad);
        editTextCentroEstudio = findViewById(R.id.editTextCentroEstudio);
        editTextCorreo = findViewById(R.id.editTextCorreo);
        try {
            autentificacion = FirebaseAuth.getInstance();
            mDatabase = FirebaseDatabase.getInstance().getReference();
            correo = autentificacion.getCurrentUser().getEmail();
            editTextCorreo.setText(correo);
        } catch(Exception e){
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
    public void continuar(View view) {
        String usuario = "";
        if(comprobarCampos()){
            usuario = crearUsuario();
            mDatabase.child("Usuarios").push().setValue(usuario);
            Intent intent = new Intent(ProfileActivity.this, ApartmentActivity.class);
            startActivity(intent);
            Toast.makeText(ProfileActivity.this, "Usuario creado", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(ProfileActivity.this, "Faltan datos", Toast.LENGTH_SHORT).show();
        }
    }
    private boolean comprobarCampos(){
        if(editTextNombre.getText().toString()==null ||
            editTextApellidos.getText().toString()==null ||
            editTextCorreo.getText().toString()==null ||
            editTextEdad.getText().toString()==null ||
            editTextCiudad.getText().toString()==null ||
            editTextCentroEstudio.getText().toString()==null){
            return false;
        }
        else{
            return true;
        }
    }
    private String crearUsuario(){
        return editTextNombre.getText().toString() + " " +
            editTextApellidos.getText().toString() + ";" +
            editTextEdad.getText().toString() + ";" +
            editTextCiudad.getText().toString() + ";" +
            editTextCentroEstudio.getText().toString() + ";" +
            editTextCorreo.getText().toString();
    }
    public void cancelar(View view) {
        FirebaseUser user = autentificacion.getCurrentUser();
        if(user!=null){
            user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(ProfileActivity.this, "Error al borrar la cuenta", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}