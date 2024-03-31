package com.example.memorycrafters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LanguageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        Button spanishButton = findViewById(R.id.spanishButton);
        Button englishButton = findViewById(R.id.englishButton);
        Button exitButton = findViewById(R.id.exitLanguageButton);
        // Listener para el botón de español
        spanishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchLanguage("es");
            }
        });

        // Listener para el botón de inglés
        englishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchLanguage("en");
            }
        });
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // Método para cambiar el idioma
    private void switchLanguage(String languageCode) {
        LanguageUtils.changeLanguage(this, languageCode);
        // Reiniciar la actividad principal y esto se hace porque sino los
        // recursos previamente cargados no se actualizaran
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

