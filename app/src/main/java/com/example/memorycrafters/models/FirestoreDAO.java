package com.example.memorycrafters.models;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FirestoreDAO {

    @GET("partidas/top10")
    Call<List<User>> getTop10Players();

    @GET("partidas/{id}")
    Call<Partida> getPartidaById(@Path("id") int id);
    @GET("monedas")
    Call<List<Moneda>> getMonedas();
    @GET("monedas/{id}")
    Call<Moneda> getCoinById(@Path("id") int id);

    @GET("users")
    Call<List<User>> getUsers();

    @GET("users/{uuid}")
    Call<User> getUserByUUID(@Path("uuid") String uuid);

    @GET("users/{email}")
    Call<User> getUserByEmail(@Path("email") String email);

    @GET("partidas")
    Call<List<Partida>> getPartidas();

    @GET("partidas/{id}/monedas")
    Call<List<Moneda>> getMonedasByPartidaId(@Path("id") int id);

    @GET("partidas/{id}/usuario")
    Call<User> getUserByPartidaId(@Path("id") int id);

    @POST("partidas")
    Call<Partida> createPartida(@Body Partida partida);

    @POST("partidas/{id}/monedas")
    Call<Moneda> createMoneda(@Path("id") int id, @Body Moneda moneda);

    @POST("partidas/{id}/usuario")
    Call<User> createUser(@Path("id") int id, @Body User user);

    @PATCH("monedas/{id}")
    Call<Moneda> updateMoneda(@Path("id") int id, @Body Moneda moneda);

    @PATCH("partidas/{id}")
    Call<Partida> updatePartida(@Path("id") int id, @Body Partida partida);


}


