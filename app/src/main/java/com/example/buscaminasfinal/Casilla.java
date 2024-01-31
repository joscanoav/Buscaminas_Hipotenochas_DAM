package com.example.buscaminasfinal;

import android.content.Context;
import android.widget.Button;

import androidx.annotation.NonNull;

public class Casilla extends androidx.appcompat.widget.AppCompatButton {

    // La clase casilla hereda de Button, con esto podemos hacer un array de botones y darles las propiedades
    // que queramos, como los flag de bandera, mina, coordenadas, si el botón está pulsado o no, etc...

    // Se declaran las variables necesarias
    private int id;
    private int coordenadaFila;
    private int coordenadaColumna;
    private boolean mina;
    private boolean activa;
    private boolean bandera;
    private int minasAlrededor;
    private boolean tieneMinaAlrededor;
    private boolean banderita;



    public Casilla(@NonNull Context context,int coordenadaFila, int coordenadaColumna ) {
        super(context);
        this.coordenadaFila = coordenadaFila;
        this.coordenadaColumna = coordenadaColumna;
        this.minasAlrededor = 0;
        this.activa = true;
        this.mina = false;
        this.bandera = false;
        this.tieneMinaAlrededor = false;
        this.banderita = false;
    }

    // Getter y Setter

    public int getCoordenadaFila() {

        return coordenadaFila;
    }


    public int getCoordenadaColumna() {

        return coordenadaColumna;
    }

    public void setSinBandera() {

        this.banderita = false;
    }

    public boolean isBanderirta() {

        return banderita;
    }


    public boolean isMina() {

        return mina;
    }

    public void setMina(boolean mina) {

        this.mina = mina;
    }

    public void setBanderita(){
        this.banderita = banderita;
    }

    public boolean getTieneMinaAlrededor(){
        return tieneMinaAlrededor;
    }

    public void setTieneMinaAlrededor(){
        this.tieneMinaAlrededor = true;
    }

    public void setActiva() {
        this.activa = true;
    }

    public void setInactiva(){
        this.activa = false;
    }

    public boolean isActiva() {
        return activa;
    }

    public int getMinasAlrededor() {

        return minasAlrededor;
    }

    public void setminasAlrededor(int minasAlrededor) {

        this.minasAlrededor = minasAlrededor;
    }

    public void contarMinasColindantes(){
        this.minasAlrededor++;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {

        this.id = id;
    }



}

/*
*
* public Casilla(int coordenadaFila, int coordenadaColumna, int id) {
        this.coordenadaFila = coordenadaFila;
        this.coordenadaColumna = coordenadaColumna;
        this.id = id;
        this.activa = true;
        this.mina = false;
        this.bandera = false;
    }
*
* */
