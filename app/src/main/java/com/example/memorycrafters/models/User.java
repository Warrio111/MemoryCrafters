package com.example.memorycrafters.models;
import com.squareup.moshi.Json;
import com.squareup.moshi.JsonClass;

@JsonClass(generateAdapter = true)
public class User {
    @Json(name = "email")
    private String email;

    @Json(name = "uuid")
    private String uuid;

    public User(String email, String uuid) {
        this.email = email;
        this.uuid = uuid;
    }
}
