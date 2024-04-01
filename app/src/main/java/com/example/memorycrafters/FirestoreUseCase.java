package com.example.memorycrafters;

import com.example.memorycrafters.models.FirestoreRepository;
import com.example.memorycrafters.models.Moneda;
import com.example.memorycrafters.models.User;
import retrofit2.Callback;
import java.util.List;

public class FirestoreUseCase {
    private FirestoreRepository firestoreRepository;

    public FirestoreUseCase(FirestoreRepository firestoreRepository) {
        this.firestoreRepository = firestoreRepository;
    }
    public void getCoinById(int id, Callback<Moneda> callback) {
        firestoreRepository.getCoinById(id, callback);
    }

    public void getUserByUUID(String uuid, Callback<User> callback) {
        firestoreRepository.getUserByUUID(uuid, callback);
    }

    public void getTop10Players(Callback<List<User>> callback) {
        firestoreRepository.getTop10Players(callback);
    }
}
