package com.example.buscaminasfinal;




import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.gridlayout.widget.GridLayout;

import java.util.ArrayList;
import java.util.Collections;

public class Tablero extends AppCompatActivity  {



    //Objeto para reproducir sonidos
    MediaPlayer mp, mpGanar;

    //Widgets
    GridLayout tablero;
    int x, y;
    Button  btnBackToPast, btnFacil, btnDificil;
    TextView mostrarNumFlags;

    //Instancia del objeto la de la clase Logica
    Logica l = new Logica(this, tablero, mostrarNumFlags);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_tablero);
        l.tablero = findViewById(R.id.tablero);
        l.btnFacil = findViewById(R.id.btnFacil);
        l.btnDificil = findViewById(R.id.btnDificil2);
        l.btnBackToPast = findViewById(R.id.btnBackToPast);
        l.mostrarNumFlags = findViewById(R.id.mostrarNumFlags);
        l.mp = MediaPlayer.create(this, R.raw.click_bomba);
        l.mpGanar = MediaPlayer.create(this,R.raw.partida_ganada);
        l.btnBackToPast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                l.reiniciar();
            }
        });
        l.btnFacil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                l.empiezaJuegoFacil();
            }
        });
        l.btnDificil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                l.empiezaJuegoDificil();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int iden = item.getItemId();
        switch (iden) {
            case R.id.Facil:
                l.empiezaJuegoFacil();
                Toast.makeText(getApplicationContext(), "Modo fácil activado.",
                        Toast.LENGTH_LONG).show();
                return true;
            case R.id.Dificil:
                l.empiezaJuegoDificil();
                Toast.makeText(getApplicationContext(), "Modo difícil activado.",
                        Toast.LENGTH_LONG).show();
                return true;
            case R.id.dificultad:
                Toast.makeText(getApplicationContext(), "Seleccione una dificultad.",
                        Toast.LENGTH_LONG).show();
                return true;

            case R.id.Creditos:
                Toast.makeText(this,"Aplicación desarrollada por Hugo González Sánchez.",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.Salir:
                System.exit(0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
}







