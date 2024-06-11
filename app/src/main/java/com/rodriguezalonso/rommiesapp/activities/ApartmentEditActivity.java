package com.rodriguezalonso.rommiesapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rodriguezalonso.rommiesapp.R;
import com.rodriguezalonso.rommiesapp.logica.PisoListModel;

public class ApartmentEditActivity extends AppCompatActivity {
    private String correo="", idPiso="";
    private PisoListModel piso=null;
    private EditText editTextCiudad, editTextDireccion, editTextNHabitaciones;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartment_edit);
        //Recojo los datos del putExtra() del AppActivity
        Intent intent = getIntent();
        piso= (PisoListModel) intent.getSerializableExtra("Piso");
        correo = intent.getStringExtra("Correo");
        idPiso = intent.getStringExtra("idPiso");

        mDatabase = FirebaseDatabase.getInstance().getReference();

        editTextCiudad = findViewById(R.id.editTextCiudadP);
        editTextDireccion = findViewById(R.id.editTextDireccion);
        editTextNHabitaciones = findViewById(R.id.editTextHabitaciones);

        cargarDatosPiso();
    }

    /**
     * Método para rellenar los editText del activity con los datos del piso
     * Si no hay piso creado, estarán en blanco
     */
    public void cargarDatosPiso(){
        if(piso!=null){
            editTextCiudad.setText(piso.getCiudad());
            editTextDireccion.setText(piso.getDireccion());
            editTextNHabitaciones.setText(piso.getHabitaciones());
        }
    }

    /**
     * Método onClick() para cancelar la edición del piso
     * y volver a la anterior pantalla
     * @param view
     */
    public void cancelar(View view){
        Intent intent = new Intent(ApartmentEditActivity.this, AppActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Método onClick() para finalizar la edición de los datos del piso
     * Si ya existe uno, actualiza los datos, sino, crea un piso nuevo
     * @param view
     */
    public void continuar(View view) {
        String piso_txt="";
        if(comprobarCampos()){
            piso_txt = crearPiso();
            if (idPiso==null){
                mDatabase.child("Pisos").push().setValue(piso_txt + ";" + correo);
            }
            else {
                mDatabase.child("Pisos").child(idPiso).setValue(piso_txt + ";" + correo);
            }
            Intent intent = new Intent(ApartmentEditActivity.this, AppActivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(ApartmentEditActivity.this, "Piso creado", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(ApartmentEditActivity.this, "Faltan datos", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Método para comprobar que no hay campos vacíos
     * @return true si no hay campos vacíos y false si hay al menos uno vacío
     */
    private boolean comprobarCampos(){
        if(editTextCiudad.getText().toString()==null ||
                editTextDireccion.getText().toString()==null ||
                editTextNHabitaciones.getText().toString()==null){
            return false;
        } else{
            return true;
        }
    }

    /**
     * Crea un String con los datos del piso
     * para poder añadirlo o modificarlo en Firebase
     * @return
     */
    private String crearPiso(){
        return editTextCiudad.getText().toString() + ";" +
                editTextDireccion.getText().toString() + ";" +
                editTextNHabitaciones.getText().toString();
    }
}