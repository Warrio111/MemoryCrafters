package com.example.memorycrafters.models;
import com.squareup.moshi.Json;
import com.squareup.moshi.JsonClass;
@JsonClass(generateAdapter = true)
public class Moneda {
    @Json(name = "id")
    private int id;

    @Json(name = "cantidadMonedas")
    private int cantidadMonedas;

    public Moneda(int id, int cantidad) {
        this.id = id;
        this.cantidadMonedas = cantidad;
    }
}
