package com.example.momento1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

public class Rentas extends AppCompatActivity {
    SQLite sqLite = new SQLite(this, "dbLibrary", null, 1);
    EditText editBookId, editFecha;
    Button btnRetornar, btnRentar, btnCatalogo, btnHistorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rentas);
        getSupportActionBar().hide();
        editBookId = findViewById(R.id.editTextBookId);
        editFecha = findViewById(R.id.editTextFecha);
        btnCatalogo = findViewById(R.id.buttonVerLibros);
        btnHistorial = findViewById(R.id.buttonVerMisRentas);
        btnRentar = findViewById(R.id.buttonRentar);
        btnRetornar = findViewById(R.id.buttonRetornar);
        Intent intent = getIntent();
        int idUser = intent.getIntExtra("idUser", -1);
        int status = intent.getIntExtra("status", -1);
        

        btnCatalogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLibros = new Intent(getApplicationContext(), ListarLibros.class);
                startActivity(intentLibros);
            }
        });
        
        btnRentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idLibroString = editBookId.getText().toString();
                String fecha = editFecha.getText().toString();
                
                if (idLibroString.isEmpty() || fecha.isEmpty()) {
                    Toast.makeText(Rentas.this, "Debes rellenar ambos campos", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (status == 1) {
                    Toast.makeText(Rentas.this, "No puedes rentar debido a que estas sancionado", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                int idLibro = Integer.parseInt(idLibroString);
                Cursor cursor = buscarLibro(idLibro);
                if (!cursor.moveToFirst()){
                    Toast.makeText(Rentas.this, "No existe un libro con ese ID", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (cursor.getInt(2) == 1) {
                    Toast.makeText(Rentas.this, "El libro no está disponible", Toast.LENGTH_SHORT).show();
                    return;
                }
                prestarLibro(idUser, idLibro, fecha);
            }
        });

        btnHistorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentHistorial = new Intent(getApplicationContext(), MiHistorial.class);
                intentHistorial.putExtra("idUser", idUser);
                startActivity(intentHistorial);
            }
        });

        btnRetornar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idLibroString = editBookId.getText().toString();

                if (idLibroString.isEmpty()) {
                    Toast.makeText(Rentas.this, "¿Cual libro vas a retornar?", Toast.LENGTH_SHORT).show();
                    editBookId.requestFocus();
                    return;
                }
                int idLibro = Integer.parseInt(idLibroString);
                Cursor cursor = buscarRenta(idUser, idLibro);
                if (!cursor.moveToFirst()) {
                    Toast.makeText(Rentas.this, "No tienes este libro rentado", Toast.LENGTH_SHORT).show();
                }
                updateRenta(cursor.getInt(0), "cerrado");
                updateLibro(cursor.getInt(1), 0);
                Toast.makeText(Rentas.this, "Has retornado el libro", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Cursor buscarLibro(int id) {
        SQLiteDatabase database = sqLite.getReadableDatabase();
        String query = "SELECT name, cost, available FROM books WHERE idBook = " + id;
        Cursor cursor = database.rawQuery(query, null);
        return cursor;
    }
    
    private void prestarLibro(int idUser, int idBook, String fecha) {
        SQLiteDatabase database = sqLite.getReadableDatabase();
        ContentValues cv = new ContentValues();
        //idrent idbook iduser date
        Random random = new Random();
        cv.put("idRent", random.nextInt(10000));
        cv.put("idBook", idBook);
        cv.put("idUser", idUser);
        cv.put("date", fecha);
        database.insert("rents", null, cv);
        updateLibro(idBook, 1);
        database.close();
        Toast.makeText(this, "Has tomado el libro prestado", Toast.LENGTH_SHORT).show();
    }

    private void updateLibro(int idLibro,int  available) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("available", available);
        String whereClause = "idBook = ?";
        String[] whereArgs = { String.valueOf(idLibro)};
        SQLiteDatabase database = sqLite.getReadableDatabase();
        database.update("books", contentValues, whereClause, whereArgs);
        database.close();
    }

    private Cursor buscarRenta(int idUser, int idBook) {
        SQLiteDatabase database = sqLite.getReadableDatabase();
        String table = "rents";
        String[] columns = { "idRent", "idBook", "idUser"};
        String selection = "idBook = ? AND idUser = ? AND date != ?";
        String[] selectionArgs = {String.valueOf(idBook), String.valueOf(idUser), "cerrado"};
        return database.query(table, columns, selection, selectionArgs, null, null, null);
    }

    private void updateRenta(int idRent, String fecha) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("date", fecha);
        String whereClause = "idRent = ?";
        String[] whereArgs = { String.valueOf(idRent)};
        SQLiteDatabase database = sqLite.getReadableDatabase();
        database.update("rents", contentValues, whereClause, whereArgs);
        database.close();
    }


}