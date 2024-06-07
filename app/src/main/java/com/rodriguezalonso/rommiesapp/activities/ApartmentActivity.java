package com.rodriguezalonso.rommiesapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rodriguezalonso.rommiesapp.R;

public class ApartmentActivity extends AppCompatActivity {
    private Button buttonContinuar, buttonOmitir;
    private EditText editTextCiudad, editTextCalle, editTextPortal, editTextPiso, editTextNHabitaciones;
    private String correo = FirebaseAuth.getInstance().getCurrentUser().getEmail();
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartment);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        buttonContinuar = findViewById(R.id.buttonContinuarP);
        buttonOmitir = findViewById(R.id.buttonCancelarEP);
        editTextCiudad = findViewById(R.id.editTextCiudadP);
        editTextCalle = findViewById(R.id.editTextCalle);
        editTextPortal = findViewById(R.id.editTextPortal);
        editTextPiso = findViewById(R.id.editTextPiso);
        editTextNHabitaciones = findViewById(R.id.editTextHabitaciones);
    }
    public void continuar(View view) {
        String piso = "";
        if(comprobarCampos()){
            piso = crearPiso();
            mDatabase.child("Pisos").push().setValue(piso + ";" + correo);
            Intent intent = new Intent(ApartmentActivity.this, AppActivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(ApartmentActivity.this, "Piso creado", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(ApartmentActivity.this, "Faltan datos", Toast.LENGTH_SHORT).show();
        }
    }
    public void omitir(View view){
        Intent intent = new Intent(ApartmentActivity.this, AppActivity.class);
        startActivity(intent);
        finish();
    }
    private boolean comprobarCampos(){
        if(editTextCiudad.getText().toString()==null ||
            editTextCalle.getText().toString()==null ||
            editTextPortal.getText().toString()==null ||
            editTextPiso.getText().toString()==null ||
            editTextNHabitaciones.getText().toString()==null){
            return false;
        } else{
            return true;
        }
    }
    private String crearPiso(){
        return editTextCiudad.getText().toString() + ";" +
                editTextCalle.getText().toString() + " " +
                editTextPortal.getText().toString() + ", " +
                editTextPiso.getText().toString() + ";" +
                editTextNHabitaciones.getText().toString();
    }
}