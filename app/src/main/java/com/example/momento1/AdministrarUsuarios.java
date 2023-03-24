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

public class AdministrarUsuarios extends AppCompatActivity {
    SQLite sqLite = new SQLite(this, "dbLibrary", null, 1);
    Button btnSancionar, btnPerdonar;
    Button btnListarUsuarios;
    EditText editUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrar_usuarios);
        getSupportActionBar().hide();
        btnListarUsuarios = findViewById(R.id.buttonListarUsuarios);
        btnSancionar = findViewById(R.id.buttonSancionar);
        btnPerdonar = findViewById(R.id.buttonPerdonar);
        editUserId = findViewById(R.id.editTextUserId);

        btnListarUsuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListarUsuarios.class);
                startActivity(intent);
            }
        });

        btnSancionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stringId = editUserId.getText().toString();
                if (stringId.isEmpty()) {
                    Toast.makeText(AdministrarUsuarios.this, "ID del usuario a sancionar", Toast.LENGTH_SHORT).show();
                    editUserId.requestFocus();
                    return;
                }
                int id = Integer.parseInt(stringId);
                Cursor cursor = buscarUsuario(id);
                if (!cursor.moveToFirst()) {
                    Toast.makeText(AdministrarUsuarios.this, "Usuario no existe", Toast.LENGTH_SHORT).show();
                    editUserId.requestFocus();
                    return;
                }
                if (cursor.getInt(1) == 1) {
                    Toast.makeText(AdministrarUsuarios.this, "El usuario ya esta sancionado", Toast.LENGTH_SHORT).show();
                    return;
                }
                cambiarStatus(id, 1);
                Toast.makeText(getApplicationContext(), "Has sancionado al usuario", Toast.LENGTH_SHORT).show();
            }
        });

        btnPerdonar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stringId = editUserId.getText().toString();
                if (stringId.isEmpty()) {
                    Toast.makeText(AdministrarUsuarios.this, "ID del usuario a perdonar", Toast.LENGTH_SHORT).show();
                    editUserId.requestFocus();
                    return;
                }
                int id = Integer.parseInt(stringId);
                Cursor cursor = buscarUsuario(id);
                if (!cursor.moveToFirst()) {
                    Toast.makeText(AdministrarUsuarios.this, "Usuario no existe", Toast.LENGTH_SHORT).show();
                    editUserId.requestFocus();
                    return;
                }
                if (cursor.getInt(1) == 0) {
                    Toast.makeText(AdministrarUsuarios.this, "El usuario no tiene sanción", Toast.LENGTH_SHORT).show();
                    return;
                }
                cambiarStatus(id, 0);
                Toast.makeText(getApplicationContext(), "Has levantado la sanción", Toast.LENGTH_SHORT).show();
            }
        });
    }

   private void cambiarStatus(int id, int nuevoStatus) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", nuevoStatus);
        String whereClause = "idUser = ?";
        String[] whereArgs = { String.valueOf(id)};

        SQLiteDatabase database = sqLite.getReadableDatabase();
        database.update("users", contentValues, whereClause, whereArgs);
        database.close();
    }

    private Cursor buscarUsuario(int id) {
        SQLiteDatabase database = sqLite.getReadableDatabase();
        String query = "SELECT name, status FROM users WHERE idUser = " + id;
        Cursor cursor = database.rawQuery(query, null);
        return cursor;
    }
}