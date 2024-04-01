package com.example.memorycrafters.models;
import com.squareup.moshi.Json;
import com.squareup.moshi.JsonClass;

@JsonClass(generateAdapter = true)
public class Partida {
    @Json(name = "id")
    private int id;

    @Json(name = "fecha")
    private String fecha;

    @Json(name = "idMonedas")
    private Moneda idMonedas;

    @Json(name = "idUsuario")
    private User idUsuario;

    public Partida(int id, String fecha, Moneda idMonedas, User idUsuario) {
        this.id = id;
        this.fecha = fecha;
        this.idMonedas = idMonedas;
        this.idUsuario = idUsuario;
    }
}
