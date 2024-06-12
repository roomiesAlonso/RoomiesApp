package com.rodriguezalonso.rommiesapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rodriguezalonso.rommiesapp.R;
import com.rodriguezalonso.rommiesapp.logica.Usuario;

public class ProfileEditActivity extends AppCompatActivity {
    private EditText editTextNombre, editTextApellidos, editTextEdad,
            editTextCiudad, editTextCentroEstudio;
    private TextView textViewCorreo;
    private DatabaseReference mDatabase;
    private String correo, id = "";
    private Usuario usuario = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        //Recojo los datos del putExtra() del AppActivity
        Intent intent = getIntent();
        usuario = (Usuario) intent.getSerializableExtra("Usuario");
        correo = intent.getStringExtra("Correo");
        id = intent.getStringExtra("idUsuario");

        editTextNombre = findViewById(R.id.editTextNombre);
        editTextApellidos = findViewById(R.id.editTextApellidos);
        editTextEdad = findViewById(R.id.editTextEdad);
        editTextCiudad = findViewById(R.id.editTextCiudad);
        editTextCentroEstudio = findViewById(R.id.editTextCentroEstudio);
        textViewCorreo = findViewById(R.id.textViewCorreo);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        rellenarCampos();
    }

    /**
     * Método para rellenar los editText del activity con los datos del usuario
     */
    private void rellenarCampos() {
        String[] nombreApellidos = new String [2];
        nombreApellidos = usuario.getNombreApellidos().split(" ");
        editTextNombre.setText(nombreApellidos[0]);
        editTextApellidos.setText(nombreApellidos[1]);
        editTextEdad.setText(usuario.getEdad());
        editTextCiudad.setText(usuario.getCiudad());
        editTextCentroEstudio.setText(usuario.getCentroEstudio());
        textViewCorreo.setText(correo);
    }

    /**
     * Método para comprobar que no hay campos vacíos
     * @return true si no hay campos vacíos y false si hay al menos uno vacío
     */
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

    /**
     * Crea un String con los datos del usuario para actualizarlo en Firebase
     * @return
     */
    private String actualizarUsuario(){
        return editTextNombre.getText().toString() + " " +
                editTextApellidos.getText().toString() + ";" +
                editTextEdad.getText().toString() + ";" +
                editTextCiudad.getText().toString() + ";" +
                editTextCentroEstudio.getText().toString() + ";" +
                textViewCorreo.getText().toString();
    }

    /**
     * Método onClick() para finalizar la edición de los datos del usuario
     * @param view
     */
    public void actualizar(View view){
        if(comprobarCampos()){
            mDatabase.child("Usuarios").child(id).setValue(actualizarUsuario());
            Intent intent = new Intent(ProfileEditActivity.this, AppActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            Toast.makeText(ProfileEditActivity.this, "Faltan datos", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Método onClick() para cancelar la edición del usuario
     * y volver a la anterior pantalla
     * @param view
     */
    public void cancelar(View view) {
        Intent intent = new Intent(ProfileEditActivity.this, AppActivity.class);
        startActivity(intent);
        finish();
    }
}