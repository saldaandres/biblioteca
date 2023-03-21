package com.example.momento1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Usuarios extends AppCompatActivity {

    SQLite sqLite = new SQLite(this, "dbLibrary", null, 1);
    Button btnIngresar, btnBuscar, btnAgregar;
    EditText editIdUsuario, editNombre, editCorreo, editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);
        getSupportActionBar().hide();
        btnAgregar = findViewById(R.id.buttonAgregar);
        btnBuscar = findViewById(R.id.buttonBuscar);
        btnIngresar = findViewById(R.id.buttonIngresar);
        btnIngresar.setEnabled(false);
        editNombre = findViewById(R.id.editTextUserName);
        editIdUsuario = findViewById(R.id.editTextUserId);
        editCorreo = findViewById(R.id.editTextEmail);
        editPassword = findViewById(R.id.editTextPassword);
        
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreString = editNombre.getText().toString();
                String correoString = editCorreo.getText().toString();
                String passwordString = editPassword.getText().toString();
                String idUsuarioString = editIdUsuario.getText().toString();
                if (nombreString.isEmpty() || correoString.isEmpty() || passwordString.isEmpty() || idUsuarioString.isEmpty()) {
                    Toast.makeText(Usuarios.this, "Debes rellenar todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                int idUsuario = Integer.parseInt(idUsuarioString);
                Cursor cursor = buscarUsuario(idUsuario);
                if (cursor.moveToFirst()) {
                    Toast.makeText(Usuarios.this, "Este ID de usuario ya existe, escoge otro", Toast.LENGTH_SHORT).show();
                    return;
                }

                ContentValues contentValues = new ContentValues();
                contentValues.put("idUser", idUsuario);
                contentValues.put("name", nombreString);
                contentValues.put("email", correoString);
                contentValues.put("password", passwordString);
                contentValues.put("status", 0);
                Toast.makeText(Usuarios.this, "Usuario creado con exito", Toast.LENGTH_SHORT).show();
                guardarUsuario(contentValues);
            }
        });
        
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idString = editIdUsuario.getText().toString();
                
                if (idString.isEmpty()) {
                    Toast.makeText(Usuarios.this, "Ingresa el ID del usuario que quieres buscar", Toast.LENGTH_SHORT).show();
                    return;
                }
                int idUsuario = Integer.parseInt(idString);
                Cursor cursor = buscarUsuario(idUsuario);
                if (!cursor.moveToFirst()) {
                    Toast.makeText(Usuarios.this, "No existe un usuario con este ID", Toast.LENGTH_SHORT).show();
                    return;
                }
                editNombre.setText(cursor.getString(0));
                editCorreo.setText(cursor.getString(1));
                editPassword.requestFocus();
                btnIngresar.setEnabled(true);
            }
        });

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passwordString = editPassword.getText().toString();
                String idString = editIdUsuario.getText().toString();

                if (idString.isEmpty() || passwordString.isEmpty()) {
                    Toast.makeText(Usuarios.this, "Debes rellenar ID y Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                int idUsuario = Integer.parseInt(idString);
                Cursor cursor = buscarUsuario(idUsuario);
                if (!cursor.moveToFirst()) {
                    Toast.makeText(Usuarios.this, "El ID ingresado no corresponde a ningun usuario", Toast.LENGTH_SHORT).show();
                    return;
                }
                String passwordDatabase = cursor.getString(2);
                if (!passwordDatabase.equals(passwordString)) {
                    Toast.makeText(Usuarios.this, "Password incorrecto", Toast.LENGTH_SHORT).show();
                    return;
                };
                Intent intent = new Intent(getApplicationContext(), UsuarioLogueado.class);
                intent.putExtra("idUser", idUsuario);
                intent.putExtra("name", cursor.getString(0));
                intent.putExtra("email", cursor.getString(1));
                intent.putExtra("password", cursor.getString(2));
                intent.putExtra("status", Integer.parseInt(cursor.getString(3)));
                startActivity(intent);
            }
        });



    }
    
    private void guardarUsuario(ContentValues cv){
        SQLiteDatabase database = sqLite.getReadableDatabase();
        database.insert("users", null, cv);
        database.close();
    }

    private Cursor buscarUsuario(int id) {
        SQLiteDatabase database = sqLite.getReadableDatabase();
        String query = "SELECT name, email, password, status FROM users WHERE idUser = " + id;
        Cursor cursor = database.rawQuery(query, null);
        return cursor;
    }
}