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

public class AdministrarLibros extends AppCompatActivity {
    SQLite sqLite = new SQLite(this, "dbLibrary", null, 1);
    EditText editBookId, editBookCost, editBookAvailable;
    EditText editBookName;
    Button btnAgregarLibro, btnBuscarLibro;
    Button btnEliminarLibro, btnListarLibros;
    Button btnEditarLibro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrar_libros);
        getSupportActionBar().hide();
        editBookId = findViewById(R.id.editTextBookId);
        editBookName = findViewById(R.id.editTextBookName);
        editBookAvailable = findViewById(R.id.editTextBookAvailable);
        editBookCost = findViewById(R.id.editTextBookCost);
        btnEliminarLibro = findViewById(R.id.buttonEliminarLibros);
        btnEditarLibro = findViewById(R.id.buttonEditarLibro);
        btnAgregarLibro = findViewById(R.id.buttonAgregarLibro);
        btnBuscarLibro = findViewById(R.id.buttonBuscar);
        btnListarLibros = findViewById(R.id.buttonListarLibros);
        editBookAvailable.setEnabled(false);
        btnEditarLibro.setEnabled(false);
        btnEliminarLibro.setEnabled(false);

        btnAgregarLibro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idLibroString = editBookId.getText().toString();
                String nombre = editBookName.getText().toString();
                String costoString = editBookCost.getText().toString();
                if (idLibroString.isEmpty() || nombre.isEmpty() || costoString.isEmpty()) {
                    Toast.makeText(AdministrarLibros.this, "Rellena todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }
                int idLibro = Integer.parseInt(idLibroString);
                Cursor cursor = buscarLibro(idLibro);
                if (cursor.moveToFirst()) {
                    Toast.makeText(AdministrarLibros.this, "ID de Libro no disponible", Toast.LENGTH_SHORT).show();
                    return;
                }

                ContentValues cv = new ContentValues();
                cv.put("idBook", idLibro);
                cv.put("name", nombre);
                cv.put("cost", Integer.parseInt(costoString));
                cv.put("available", 0);
                guardarLibro(cv);
                Toast.makeText(AdministrarLibros.this, "Libro ingresado con exito", Toast.LENGTH_SHORT).show();
            }
        });

        btnBuscarLibro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idLibroString = editBookId.getText().toString();
                if (idLibroString.isEmpty()) {
                    Toast.makeText(AdministrarLibros.this, "Digita el ID del libro a buscar", Toast.LENGTH_SHORT).show();
                    editBookId.requestFocus();
                    return;
                }
                int idLibro = Integer.parseInt(idLibroString);
                Cursor cursor = buscarLibro(idLibro);
                if (!cursor.moveToFirst()) {
                    Toast.makeText(AdministrarLibros.this, "No existe un libro con este ID", Toast.LENGTH_SHORT).show();
                    editBookId.requestFocus();
                    editBookName.setText("");
                    editBookCost.setText("");
                    editBookAvailable.setText("");
                    btnEditarLibro.setEnabled(false);
                    editBookAvailable.setEnabled(false);
                    btnEliminarLibro.setEnabled(false);
                    return;
                }
                editBookName.setText(cursor.getString(0));
                editBookCost.setText(String.valueOf(cursor.getString(1)));
                editBookAvailable.setText((cursor.getInt(2)) == 0 ? "Disponible" : "No disponible");
                btnEditarLibro.setEnabled(true);
                editBookAvailable.setEnabled(true);
                btnEliminarLibro.setEnabled(true);
            }
        });

        btnEliminarLibro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idBook = editBookId.getText().toString();
                if (idBook.isEmpty()) {
                    Toast.makeText(AdministrarLibros.this, "Digita el ID del libro a borrar", Toast.LENGTH_SHORT).show();
                    return;
                }
                Cursor cursor = buscarLibro(Integer.parseInt(idBook));
                if (!cursor.moveToFirst()) {
                    Toast.makeText(AdministrarLibros.this, "No hay un libro con este ID", Toast.LENGTH_SHORT).show();
                    return;
                }
                eliminarLibro(Integer.parseInt(idBook));
                Toast.makeText(AdministrarLibros.this, "Libro eliminado", Toast.LENGTH_SHORT).show();
                editBookId.setText("");
                editBookCost.setText("");
                editBookName.setText("");
                btnEditarLibro.setEnabled(false);
                editBookAvailable.setEnabled(false);
                btnEliminarLibro.setEnabled(false);
            }
        });

        btnEditarLibro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idString = editBookId.getText().toString();
                String nombre = editBookName.getText().toString();
                String costoString = editBookCost.getText().toString();
                String availableString = editBookAvailable.getText().toString();

                if (idString.isEmpty() || nombre.isEmpty() || costoString.isEmpty() || availableString.isEmpty()) {
                    Toast.makeText(AdministrarLibros.this, "Todos los datos son obligatorios", Toast.LENGTH_SHORT).show();
                    return;
                }
                int idLibro = Integer.parseInt(idString);
                int costo = Integer.parseInt(costoString);
                int available = Integer.parseInt(availableString);
                updateLibro(idLibro, nombre, costo, available);
            }
        });

        btnListarLibros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentListar = new Intent(getApplicationContext(), ListarLibros.class);
                startActivity(intentListar);
            }
        });

    }
    private void guardarLibro(ContentValues cv){
        SQLiteDatabase database = sqLite.getReadableDatabase();
        database.insert("books", null, cv);
        database.close();
    }

    private Cursor buscarLibro(int id) {
        SQLiteDatabase database = sqLite.getReadableDatabase();
        String query = "SELECT name, cost, available FROM books WHERE idBook = " + id;
        Cursor cursor = database.rawQuery(query, null);
        return cursor;
    }

    private void eliminarLibro(int id) {
        SQLiteDatabase database = sqLite.getReadableDatabase();
        String whereClause = "idBook = ?";
        String[] whereArgs = { String.valueOf(id)};
        database.delete("books", whereClause, whereArgs);
    }

    private void updateLibro(int idLibro, String name, int cost, int  available) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("cost", cost);
        contentValues.put("available", available);
        String whereClause = "idBook = ?";
        String[] whereArgs = { String.valueOf(idLibro)};

        SQLiteDatabase database = sqLite.getReadableDatabase();
        database.update("books", contentValues, whereClause, whereArgs);
        database.close();
        Toast.makeText(this, "Has actualizado el libro", Toast.LENGTH_SHORT).show();
        finish();
    }
}