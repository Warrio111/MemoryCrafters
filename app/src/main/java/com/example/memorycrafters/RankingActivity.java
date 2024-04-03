package com.example.memorycrafters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.memorycrafters.models.FirestoreDAOImpl;
import com.example.memorycrafters.models.Moneda;
import com.example.memorycrafters.models.Partida;
import com.example.memorycrafters.models.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RankingActivity extends AppCompatActivity {
    private FirestoreDAOImpl firestoreDAOImpl;
    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        Button botonQuit = findViewById(R.id.button_quit_Ranking);
        tableLayout = findViewById(R.id.tableLayoutRanking);
        firestoreDAOImpl = new FirestoreDAOImpl();

        botonQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Cerrar la actividad
            }
        });

        //Obtener todas las partidas
        getPartidas();
    }
    private String getNullAsEmptyString(JsonElement jsonElement) {
        return jsonElement.isJsonNull() ? "" : jsonElement.getAsString();
    }
    private void getPartidas() {
        firestoreDAOImpl.getPartidas().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    JsonObject body = response.body();
                    JsonArray documents = body.getAsJsonArray("documents");
                    List<Partida> partidas = new ArrayList<>();
                    if (documents == null) {
                        return;
                    }
                    for (JsonElement documentElement : documents) {
                        JsonObject document = documentElement.getAsJsonObject();
                        String name = document.getAsJsonPrimitive("name").getAsString();

                        JsonObject fields = document.getAsJsonObject("fields");
                        int id = fields.getAsJsonObject("id").getAsJsonPrimitive("integerValue").getAsInt();
                        String fechaInicio = fields.getAsJsonObject("fechaInicio").getAsJsonPrimitive("stringValue").getAsString();
                        String fechaFin = null;
                        JsonElement fechaFinElement = fields.getAsJsonObject("fechaFin").get("stringValue");
                        if (fechaFinElement != null && fechaFinElement.isJsonPrimitive()) {
                            fechaFin = fechaFinElement.getAsString();
                        }
                        String idMoneda = fields.getAsJsonObject("idMonedas").getAsJsonPrimitive("stringValue").getAsString();

                        String[] partsMoneda = idMoneda.split("/");
                        String idDocumentMoneda = partsMoneda[partsMoneda.length - 1];
                        String idUsuario = fields.getAsJsonObject("idUsers").getAsJsonPrimitive("stringValue").getAsString();
                        String[] partsUsuario= idUsuario.split("/");
                        String idDocumentUsuario = partsUsuario[partsUsuario.length - 1];
                        Moneda moneda = getMonedaFromDocumentName(idDocumentMoneda);

                        User user = getUserFromDocumentName(idDocumentUsuario);

                        Partida partida = new Partida(id, fechaInicio, fechaFin, moneda, user);
                        partidas.add(partida);
                    }
                    displayPartidas(partidas);
                } else {
                    System.out.println("Error al obtener las partidas: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private Moneda getMonedaFromDocumentName(String name) {
        final Moneda[] moneda = {null};

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response<JsonObject> response = firestoreDAOImpl.getCoinByDocumentId(name).execute();
                    if (response.isSuccessful()) {
                        JsonObject body = response.body();
                        JsonObject fields = body.getAsJsonObject("fields");
                        int idMoneda = fields.getAsJsonObject("id").getAsJsonPrimitive("integerValue").getAsInt();
                        int cantidadMonedas = fields.getAsJsonObject("cantidadMonedas").getAsJsonPrimitive("integerValue").getAsInt();
                        moneda[0] = new Moneda(idMoneda, cantidadMonedas);
                    } else {
                        System.out.println("Error al obtener la moneda: " + response.errorBody());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

        try {
            thread.join(); // Esperar a que el hilo secundario termine antes de devolver el resultado
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return moneda[0];
    }


    private User getUserFromDocumentName(String name) {
        final User[] user = {null};

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response<JsonObject> response = firestoreDAOImpl.getUserByDocumentId(name).execute();
                    if(response.isSuccessful()){
                        JsonObject body = response.body();
                        JsonObject fields = body.getAsJsonObject("fields");
                        String uuid = fields.getAsJsonObject("uuid").getAsJsonPrimitive("stringValue").getAsString();
                        String email = fields.getAsJsonObject("email").getAsJsonPrimitive("stringValue").getAsString();
                        user[0] = new User(email, uuid);
                    } else {
                        System.out.println("Error al obtener el usuario: " + response.errorBody());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

        try {
            thread.join(); // Esperar a que el hilo secundario termine antes de devolver el resultado
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return user[0];
    }

    private void displayPartidas(List<Partida> partidas) {
        // Limpiar el TableLayout
        tableLayout.removeAllViews();

        // Mapa para almacenar el total de monedas de cada usuario
        HashMap<String, Integer> userCoinMap = new HashMap<>();

        // Calcular el total de monedas de cada usuario
        for (Partida partida : partidas) {
            // Obtener el ID del usuario y la cantidad de monedas de la partida
            String userId = partida.getIdUser().getUUID();
            int coins = partida.getIdMoneda().getCantidadMonedas();

            // Actualizar el total de monedas del usuario en el mapa
            if (userCoinMap.containsKey(userId)) {
                int totalCoins = userCoinMap.get(userId) + coins;
                userCoinMap.put(userId, totalCoins);
            } else {
                userCoinMap.put(userId, coins);
            }
        }

        // Obtener hasta un maximo de los 10 usuarios con mas puntuacion
        HashMap<String, Integer> topPlayers = getTopPlayers(userCoinMap);
        displayTopPlayers(topPlayers);
    }

    private void displayTopPlayers(HashMap<String, Integer> topPlayers) {
        // Crear una fila para la cabecera de la tabla
        TableRow headerRow = new TableRow(this);
        TextView headerRanking = new TextView(this);
        headerRanking.setText(R.string.ranking);
        headerRow.addView(headerRanking);
        TextView headerEmail = new TextView(this);
        headerEmail.setText(R.string.uuid);
        headerRow.addView(headerEmail);
        TextView headerCantidadMonedas = new TextView(this);
        headerCantidadMonedas.setText(R.string.quantityOfcurrency);
        headerRow.addView(headerCantidadMonedas);
        TextView headerPremio = new TextView(this);
        headerPremio.setText(R.string.prize);
        headerRow.addView(headerPremio);
        tableLayout.addView(headerRow);
        int i = 1;
        for (String userId : topPlayers.keySet()) {
            int coins = topPlayers.get(userId);
            TableRow row = new TableRow(this);

            TextView rankingTextView = new TextView(this);
            rankingTextView.setText(String.valueOf(i));
            row.addView(rankingTextView);

            TextView emailTextView = new TextView(this);
            emailTextView.setText(userId);
            row.addView(emailTextView);

            TextView cantidadMonedasTextView = new TextView(this);
            cantidadMonedasTextView.setText(String.valueOf(coins));
            row.addView(cantidadMonedasTextView);

            ImageView premioImageView = new ImageView(this);
            premioImageView.setLayoutParams(new TableRow.LayoutParams(20, 90));
            premioImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Drawable premioDrawable = ContextCompat.getDrawable(this, R.drawable.trophy);
            premioImageView.setImageDrawable(premioDrawable);
            isPrize(userId, hasPrize -> {
                if (hasPrize) {
                    // Si tiene premio, establecer la imagen de premio
                    premioImageView.setVisibility(View.VISIBLE);
                } else {
                    // Si no tiene premio, ocultar la imagen de premio
                    premioImageView.setVisibility(View.GONE);
                }
            });
            row.addView(premioImageView);

            // Agregar la fila al TableLayout
            tableLayout.addView(row);
            i++;
        }
    }

    private void isPrize(String uuid, PrizeCheckListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .whereEqualTo("uuid", uuid)
                .get()
                .addOnCompleteListener(task -> {
                    boolean premio = false;
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            if (document.exists()) {
                                premio = document.getBoolean("premio");
                                break; // Si encuentras el documento, no es necesario seguir iterando
                            }
                        }
                    }
                    listener.onPrizeChecked(premio);
                });
    }

    // Interfaz de devolución de llamada para notificar si hay premio o no
    interface PrizeCheckListener {
        void onPrizeChecked(boolean hasPrize);
    }

    private HashMap<String, Integer> getTopPlayers(HashMap<String, Integer> userCoinMap) {
        // Creamos una lista de usuarios ordenada por cantidad de monedas
        List<Map.Entry<String, Integer>> sortedUsers = new ArrayList<>(userCoinMap.entrySet());
        sortedUsers.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        // Creamos un nuevo mapa para almacenar los mejores jugadores
        HashMap<String, Integer> topPlayers = new HashMap<>();

        // Iterar sobre los primeros 10 usuarios o sobre todos si hay menos de 10
        int count = Math.min(sortedUsers.size(), 10);
        for (int i = 0; i < count; i++) {
            Map.Entry<String, Integer> entry = sortedUsers.get(i);
            topPlayers.put(entry.getKey(), entry.getValue());
        }

        return topPlayers;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
