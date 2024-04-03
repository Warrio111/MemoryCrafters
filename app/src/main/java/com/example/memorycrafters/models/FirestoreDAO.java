package com.example.memorycrafters.models;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FirestoreDAO {

    @GET("monedas")
    Call<JsonObject> getMonedas();
    @GET("monedas/{name}")
    Call<JsonObject> getCoinByDocumentId(@Path("name") String name);
    @GET("users")
    Call<JsonObject> getUsers();
    @GET("users/{name}")
    Call<JsonObject> getUserByDocumentId(@Path("name") String name);
    @GET("partidas")
    Call<JsonObject> getPartidas();
    @GET("partidas/{name}")
    Call<JsonObject> getPartidaByDocumentId(@Path("name") String name);

    @GET("users/{uuid}")
    Call<JsonObject> getUserByUUID(@Path("uuid") String uuid);

    @GET("users/{email}")
    Call<JsonObject> getUserByEmail(@Path("email") String email);


    @GET("partidas/{id}/usuario")
    Call<JsonObject> getUserByPartidaId(@Path("id") int id);

    @POST("partidas")
    Call<JsonObject> createPartida(@Body Partida partida);

    @POST("partidas/{id}/monedas")
    Call<JsonObject> createMoneda(@Path("id") int id, @Body Moneda moneda);

    @POST("partidas/{id}/usuario")
    Call<JsonObject> createUser(@Path("id") int id, @Body User user);

    @PATCH("monedas/{id}")
    Call<JsonObject> updateMoneda(@Path("id") int id, @Body Moneda moneda);

    @PATCH("partidas/{id}")
    Call<JsonObject> updatePartida(@Path("id") int id, @Body Partida partida);


}


