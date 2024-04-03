package com.example.memorycrafters.models;


public class Partida {

    private int id;

    private String fechaInicio;
    private String fechaFin;

    private Moneda idMoneda; // Cambiar el tipo de int a Moneda


    private User idUsuario;

    public Partida(int id, String fechaInicio,String fechaFin, Moneda idMoneda, User idUsuario) {
        this.id = id;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.idMoneda = idMoneda;
        this.idUsuario = idUsuario;
    }

    public int getId() {
        return id;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public User getIdUser() {
        return this.idUsuario;
    }

    public Moneda getIdMoneda() {
        return this.idMoneda;
    }
}
