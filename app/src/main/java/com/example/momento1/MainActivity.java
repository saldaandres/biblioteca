package com.example.momento1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button buttonUser, buttonAdminLibros;
    Button buttonAdminUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        buttonAdminLibros = findViewById(R.id.buttonAdministrarLibros);
        buttonUser = findViewById(R.id.buttonUsuarios);
        buttonAdminUsers = findViewById(R.id.buttonAdmininistrarUsuarios);

        buttonUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentUsuarios = new Intent(getApplicationContext(), Usuarios.class);
                startActivity(intentUsuarios);
            }
        });

        buttonAdminLibros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAdminLibros = new Intent(getApplicationContext(), AdministrarLibros.class);
                startActivity(intentAdminLibros);
            }
        });

        buttonAdminUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAdminUsers = new Intent(getApplicationContext(), AdministrarUsuarios.class);
                startActivity(intentAdminUsers);
            }
        });



    }
}