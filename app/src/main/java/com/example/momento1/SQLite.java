package com.example.momento1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLite extends SQLiteOpenHelper {

    // la variable available is 0 cuando el libro esta disponible y 1 de lo contrario.
    String crearBooks = "CREATE TABLE books (idBook int primary key, name text, cost int, available int)";
    // la variable status de user toma el valor 0 para no sancionado y 1 para sancionado.
    String crearUsers = "CREATE TABLE users (idUser int primary key, name text, email text, password text, status int)";
    String crearRents = "CREATE TABLE rents (idRent int primary key, idBook int, idUser int, date text)";

    public SQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(crearBooks);
        db.execSQL(crearUsers);
        db.execSQL(crearRents);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE books");
        db.execSQL("DROP TABLE users");
        db.execSQL("DROP TABLE users");
        db.execSQL(crearBooks);
        db.execSQL(crearUsers);
        db.execSQL(crearRents);
    }
}
