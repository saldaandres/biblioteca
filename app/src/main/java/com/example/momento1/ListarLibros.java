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

public class ListarLibros extends AppCompatActivity {
    ListView listView;
    ArrayList<String> libros;
    Button btnRegresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_libros);
        listView = findViewById(R.id.listViewLibros);
        btnRegresar = findViewById(R.id.buttonRegresar);
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
        libros = leerBaseDatos();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item, R.id.viewItem, libros);
        listView.setAdapter(adapter);
    }

    private ArrayList<String> leerBaseDatos() {
        ArrayList<String> libros = new ArrayList<>();
        SQLite sqLite = new SQLite(getApplicationContext(), "dbLibrary", null, 1);
        SQLiteDatabase database = sqLite.getReadableDatabase();
        String query = "SELECT idBook, name, cost, available FROM books";
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                String stringAvailable = (cursor.getInt(3) == 0 ? "Disponible" : "No disponible");
                String libro = "ID: " + cursor.getInt(0) + "\n" +cursor.getString(1) + "\n" + "$ " + cursor.getInt(2) + "\n" + stringAvailable;
                libros.add(libro);
            } while (cursor.moveToNext());
        }
        database.close();
        return libros;
    }
}