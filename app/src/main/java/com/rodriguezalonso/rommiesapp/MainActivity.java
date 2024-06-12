package com.rodriguezalonso.rommiesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rodriguezalonso.rommiesapp.activities.AppActivity;
import com.rodriguezalonso.rommiesapp.activities.ProfileActivity;
import com.rodriguezalonso.rommiesapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private AlertDialog dialog;
    private ActionBar actionBar;
    private Button buttonLogin, buttonSignin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        buttonLogin = binding.buttonLogin;
        buttonSignin = binding.buttonSignin;
    }

    /**
     * Método onClick() del botón de inicio de sesión
     * que abre un prompt para meter los datos e iniciar sesión
     * @param view
     */
    public void promptLogin(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        //Le paso un .xml que creé para mostrar el prompt como a mí me interesa
        View v = inflater.inflate(R.layout.prompt_login, null);
        builder.setView(v);
        Button cancelar = v.findViewById(R.id.buttonCancel);
        Button aceptar = v.findViewById(R.id.buttonAccept);
        EditText user = v.findViewById(R.id.editTextUser);
        EditText password = v.findViewById(R.id.editTextPassword);
        cancelar.setOnClickListener(
                new View.OnClickListener() {
                    /**
                     * Método onClick() para cerrar el prompt
                     * @param v
                     */
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                }
        );
        aceptar.setOnClickListener(
                new View.OnClickListener() {
                    /**
                     * Método onClick() para continuar con el inicio de sesión
                     * @param v
                     */
                    @Override
                    public void onClick(View v) {
                        String usuario = user.getText().toString();
                        String contraseña = password.getText().toString();
                        getUsuarioLogin(usuario,contraseña);
                    }
                }
        );
        dialog = builder.create();
        dialog.show();
    }
    /**
     * Método onClick() del botón de registro de cuenta
     * que abre un prompt para meter los datos y registrarse
     * @param view
     */
    public void promptSignin(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        //Le paso un .xml que creé para mostrar el prompt como a mí me interesa
        View v = inflater.inflate(R.layout.prompt_signin, null);
        builder.setView(v);
        Button cancelar = v.findViewById(R.id.buttonCancel);
        Button aceptar = v.findViewById(R.id.buttonAccept);
        EditText user = v.findViewById(R.id.editTextUser);
        EditText password = v.findViewById(R.id.editTextPassword);
        EditText password2 = v.findViewById(R.id.editTextPassword2);
        cancelar.setOnClickListener(
                new View.OnClickListener() {
                    /**
                     * Método onClick() para cerrar el prompt
                     * @param v
                     */
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                }
        );
        aceptar.setOnClickListener(
                new View.OnClickListener() {
                    /**
                     * Método onClick() para continuar con el registro
                     * @param v
                     */
                    @Override
                    public void onClick(View v) {
                        String usuario = user.getText().toString();
                        String contraseña1 = password.getText().toString();
                        String contraseña2 = password2.getText().toString();
                        getUsuarioSignin(usuario,contraseña1, contraseña2);
                    }
                }
        );
        dialog = builder.create();
        dialog.show();
    }

    /**
     * Método para iniciar sesión en Firebase
     * @param usuario correo del usuario para iniciar sesión
     * @param contraseña contraseña del usuario
     */
    private void getUsuarioLogin(String usuario,String contraseña){
        FirebaseAuth autentificacion = FirebaseAuth.getInstance();
        autentificacion.signInWithEmailAndPassword(usuario, contraseña).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Usuario valido", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, AppActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "Usuario o contraseña no valido", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    /**
     * Método para registrar una cuenta en Firebase
     * @param usuario correo con el que se registra el usuario
     * @param contraseña1
     * @param contraseña2
     * Las contraseñas deben ser iguales y ser mínimamente fuertes
     */
    private void getUsuarioSignin(String usuario, String contraseña1, String contraseña2){
        //Se comprueba que ambas contraseñas sean iguales
        if(contraseña1.equals(contraseña2)){
            FirebaseAuth autentificacion = FirebaseAuth.getInstance();
            autentificacion.createUserWithEmailAndPassword(usuario, contraseña1)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(MainActivity.this, "Usuario registrado ", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Usuario no registrado", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else{
            Toast.makeText(MainActivity.this, "Las contraseñas no son iguales", Toast.LENGTH_SHORT).show();
        }
   }
}