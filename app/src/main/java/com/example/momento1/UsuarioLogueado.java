package com.example.momento1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UsuarioLogueado extends AppCompatActivity {
    SQLite sqLite = new SQLite(this, "dbLibrary", null, 1);
    Button btnEditar, btnEliminar, btnRentas;
    TextView textId, textStatus;
    EditText editNombre, editCorreo, editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_logueado);
        getSupportActionBar().hide();
        textId = findViewById(R.id.textViewUserId);
        btnEditar = findViewById(R.id.buttonEditar);
        btnEliminar = findViewById(R.id.buttonEliminar);
        editNombre = findViewById(R.id.editTextUserName);
        editCorreo = findViewById(R.id.editTextEmail);
        editPassword = findViewById(R.id.editTextPassword);
        textStatus = findViewById(R.id.textViewStatus);
        btnRentas = findViewById(R.id.buttonRentas);
        Intent intentUsuarios = getIntent();

        int id = intentUsuarios.getIntExtra("idUser", -1);
        int status = intentUsuarios.getIntExtra("status", -1);
        textId.setText(String.valueOf(id));
        editNombre.setText(intentUsuarios.getStringExtra("name"));
        editCorreo.setText(intentUsuarios.getStringExtra("email"));
        editPassword.setText(intentUsuarios.getStringExtra("password"));
        textStatus.setText(((status == 0) ? "activo" : "sancionado"));

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreString = editNombre.getText().toString();
                String correoString = editCorreo.getText().toString();
                String passwordString = editPassword.getText().toString();

                if (nombreString.isEmpty() || passwordString.isEmpty() || correoString.isEmpty()) {
                    Toast.makeText(UsuarioLogueado.this, "Rellena todos los campos si quieres editar", Toast.LENGTH_SHORT).show();
                    return;
                }
                updateUser(id, nombreString, correoString, passwordString);
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUser(id);
                Toast.makeText(UsuarioLogueado.this, "Has eliminado tu cuenta", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        btnRentas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRentas = new Intent(getApplicationContext(), Rentas.class);
                intentRentas.putExtra("idUser", id);
                intentRentas.putExtra("status", status);
                startActivity(intentRentas);
            }
        });
    }

    private void updateUser(int id, String name, String email, String password) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("email", email);
        contentValues.put("password", password);
        String whereClause = "idUser = ?";
        String[] whereArgs = { String.valueOf(id)};

        SQLiteDatabase database = sqLite.getReadableDatabase();
        database.update("users", contentValues, whereClause, whereArgs);
        database.close();
        Toast.makeText(this, "Has actualizado tus datos", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void deleteUser(int id) {
        SQLiteDatabase database = sqLite.getReadableDatabase();
        String whereClause = "idUser = ?";
        String[] whereArgs = { String.valueOf(id)};
        database.delete("users", whereClause, whereArgs);
    }
}