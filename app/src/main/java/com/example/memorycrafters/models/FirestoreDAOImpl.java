package com.example.memorycrafters.models;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FirestoreDAOImpl implements FirestoreDAO {
    private static final String BASE_URL = "https://firestore.googleapis.com/v1/projects/memorycrafters-dda67/databases/(default)/documents/";
    private final FirestoreDAO firestoreAPI;

    public FirestoreDAOImpl() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        firestoreAPI = retrofit.create(FirestoreDAO.class);
    }


    @Override
    public Call<JsonObject> getUsers() {
        return firestoreAPI.getUsers();
    }

    @Override
    public Call<JsonObject> getUserByDocumentId(String name) {
        return firestoreAPI.getUserByDocumentId(name);
    }

    @Override
    public Call<JsonObject> getUserByUUID(String uuid) {
        return firestoreAPI.getUserByUUID(uuid);
    }

    @Override
    public Call<JsonObject> getUserByEmail(String email) {
        return firestoreAPI.getUserByEmail(email);
    }

    @Override
    public Call<JsonObject> getPartidas() {
        return firestoreAPI.getPartidas();
    }

    @Override
    public Call<JsonObject> getPartidaByDocumentId(String name) {
        return firestoreAPI.getPartidaByDocumentId(name);
    }


    @Override
    public Call<JsonObject> getUserByPartidaId(int id) {
        return firestoreAPI.getUserByPartidaId(id);
    }

    @Override
    public Call<JsonObject> createPartida(Partida partida) {
        return firestoreAPI.createPartida(partida);
    }

    @Override
    public Call<JsonObject> createMoneda(int id, Moneda moneda) {
        return firestoreAPI.createMoneda(id, moneda);
    }

    @Override
    public Call<JsonObject> createUser(int id, User user) {
        return firestoreAPI.createUser(id, user);
    }

    @Override
    public Call<JsonObject> updateMoneda(int id, Moneda moneda) {
        return firestoreAPI.updateMoneda(id, moneda);
    }

    @Override
    public Call<JsonObject> updatePartida(int id, Partida partida) {
        return firestoreAPI.updatePartida(id, partida);
    }


    @Override
    public Call<JsonObject> getMonedas() {
        return firestoreAPI.getMonedas();
    }

    @Override
    public Call<JsonObject> getCoinByDocumentId(String name) {
        return firestoreAPI.getCoinByDocumentId(name);
    }
}
