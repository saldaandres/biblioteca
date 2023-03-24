package com.example.momento1;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class ListarUsuarios extends AppCompatActivity {
    Button btnRegresar;
    ArrayList<String> usuarios;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_usuarios);
        btnRegresar = findViewById(R.id.buttonRegresar);
        listView = findViewById(R.id.listViewUsuarios);
        getSupportActionBar().hide();
        cargarDatos();

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void cargarDatos() {
        usuarios = leerBaseDatos();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item, R.id.viewItem, usuarios);
        listView.setAdapter(adapter);
    }

    private ArrayList<String> leerBaseDatos() {
        ArrayList<String> usuarios = new ArrayList<>();
        SQLite sqLite = new SQLite(getApplicationContext(), "dbLibrary", null, 1);
        SQLiteDatabase database = sqLite.getReadableDatabase();
        String query = "SELECT idUser, name, email, status FROM users";
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                String stringStatus = (cursor.getInt(3) == 0 ? "Activo" : "Sancionado");
                String libro = "ID: " + cursor.getInt(0) + "\n" +cursor.getString(1) + "\n" + cursor.getString(2) + "\n" + stringStatus;
                usuarios.add(libro);
            } while (cursor.moveToNext());
        }
        database.close();
        return usuarios;
    }

}