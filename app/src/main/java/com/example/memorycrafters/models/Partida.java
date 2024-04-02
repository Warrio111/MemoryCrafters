package com.example.memorycrafters.models;


public class Partida {

    private int id;


    private String fecha;


    private Moneda idMoneda; // Cambiar el tipo de int a Moneda


    private User idUsuario;

    public Partida(int id, String fecha, Moneda idMoneda, User idUsuario) {
        this.id = id;
        this.fecha = fecha;
        this.idMoneda = idMoneda;
        this.idUsuario = idUsuario;
    }

    public int getId() {
        return id;
    }

    public String getFecha() {
        return fecha;
    }

    public User getIdUser() {
        return this.idUsuario;
    }

    public Moneda getIdMoneda() {
        return this.idMoneda;
    }
}
