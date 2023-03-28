package com.example.momento1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MiHistorial extends AppCompatActivity {
    ListView listView;
    ArrayList<String> prestamos;
    Button btnRegresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_historial);
        listView = findViewById(R.id.listViewPrestamos);
        btnRegresar = findViewById(R.id.buttonRegresar);
        getSupportActionBar().hide();
        Intent intent = getIntent();
        int idUser = intent.getIntExtra("idUser", -1);
        cargarDatos(idUser);

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void cargarDatos(int idUser) {
        prestamos = leerBaseDatos(idUser);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item, R.id.viewItem, prestamos);
        listView.setAdapter(adapter);
    }

    private ArrayList<String> leerBaseDatos(int idUser) {
        ArrayList<String> prestamos = new ArrayList<>();
        SQLite sqLite = new SQLite(getApplicationContext(), "dbLibrary", null, 1);
        SQLiteDatabase database = sqLite.getReadableDatabase();
        String query = "SELECT idRent, idUser, idBook, date FROM rents WHERE idUser = " + idUser;
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                String renta = "Renta #: " + cursor.getInt(0) + "\n" + "Usuario : " +cursor.getString(1) + "\n" + "Libro: " + cursor.getInt(2) + "\n" + "Fecha : " + cursor.getString(3);
                prestamos.add(renta);
            } while (cursor.moveToNext());
        }
        database.close();
        return prestamos;
    }
}