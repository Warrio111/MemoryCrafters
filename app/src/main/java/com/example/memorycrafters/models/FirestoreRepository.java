package com.example.memorycrafters.models;
import retrofit2.Retrofit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.converter.moshi.MoshiConverterFactory;

import java.util.List;

public class FirestoreRepository {
    private FirestoreDAO firestoreDAO;

    public FirestoreRepository(FirestoreDAO firestoreDAO) {
        this.firestoreDAO = firestoreDAO;
    }

    public void getCoinById(int id, Callback<Moneda> callback) {
        firestoreDAO.getCoinById(id).enqueue(callback);
    }

    public void getUserByUUID(String uuid, Callback<User> callback) {
        firestoreDAO.getUserByUUID(uuid).enqueue(callback);
    }

    public void getTop10Players(Callback<List<User>> callback) {
        firestoreDAO.getTop10Players().enqueue(callback);
    }
}
