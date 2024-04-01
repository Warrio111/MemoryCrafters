package com.example.memorycrafters.models;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class FirestoreDAOImpl implements FirestoreDAO {
    private static final String BASE_URL = "tu_url_base_aqui/";
    private final FirestoreDAO firestoreAPI;

    public FirestoreDAOImpl() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

        firestoreAPI = retrofit.create(FirestoreDAO.class);
    }

    @Override
    public Call<Moneda> getCoinById(int id) {
        return firestoreAPI.getCoinById(id);
    }

    @Override
    public Call<List<User>> getUsers() {
        return null;
    }

    @Override
    public Call<User> getUserByUUID(String uuid) {
        return firestoreAPI.getUserByUUID(uuid);
    }

    @Override
    public Call<User> getUserByEmail(String email) {
        return null;
    }

    @Override
    public Call<List<Partida>> getPartidas() {
        return null;
    }

    @Override
    public Call<List<Moneda>> getMonedasByPartidaId(int id) {
        return null;
    }

    @Override
    public Call<User> getUserByPartidaId(int id) {
        return null;
    }

    @Override
    public Call<Partida> createPartida(Partida partida) {
        return null;
    }

    @Override
    public Call<Moneda> createMoneda(int id, Moneda moneda) {
        return null;
    }

    @Override
    public Call<User> createUser(int id, User user) {
        return null;
    }

    @Override
    public Call<Moneda> updateMoneda(int id, Moneda moneda) {
        return null;
    }

    @Override
    public Call<Partida> updatePartida(int id, Partida partida) {
        return null;
    }

    @Override
    public Call<List<User>> getTop10Players() {
        return firestoreAPI.getTop10Players();
    }

    @Override
    public Call<Partida> getPartidaById(int id) {
        return null;
    }

    @Override
    public Call<List<Moneda>> getMonedas() {
        return null;
    }
}
