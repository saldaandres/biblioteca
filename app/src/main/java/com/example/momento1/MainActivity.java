package com.example.momento1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button buttonUser, buttonAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        buttonAdmin = findViewById(R.id.buttonAdministrarLibros);
        buttonUser = findViewById(R.id.buttonUsuarios);

        buttonUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentUsuarios = new Intent(getApplicationContext(), Usuarios.class);
                startActivity(intentUsuarios);
            }
        });

        buttonAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAdmin = new Intent(getApplicationContext(), AdministrarLibros.class);
                startActivity(intentAdmin);
            }
        });

    }
}