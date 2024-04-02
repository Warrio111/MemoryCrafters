package com.example.memorycrafters.models;


public class Moneda {

    private int id;


    private int cantidadMonedas;

    public Moneda(int id, int cantidad) {
        this.id = id;
        this.cantidadMonedas = cantidad;
    }

    public int getId() {
        return this.id;
    }

    public int getCantidadMonedas() {
        return this.cantidadMonedas;
    }
}
