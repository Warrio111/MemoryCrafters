package com.example.memorycrafters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button play, salir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_MemoryCrafters);
        setContentView(R.layout.activity_main);
        play = findViewById(R.id.botonMainJugar);
        salir = findViewById(R.id.botonMainSalir);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("iniciando juego...");
                iniciarJuego();
            }
        });

        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        startService(new Intent(this, MusicService.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Manejar los clics en los elementos del menú
        int id = item.getItemId();
        if (id == R.id.action_profile) {
            //showProfile();
            return true;
        } else if (id == R.id.action_music) {
            showMusic();
            return true;
        } else if(id == R.id.action_captures) {
            showCaptures();
            return true;
        } else if (id == R.id.action_calendar) {
            showCalendar();
            return true;
        } else if (id == R.id.action_help) {
            showHelp();
            return true;
        } else if (id == R.id.action_language) {
            showLanguage();
            return true;
        } else if (id == R.id.action_score) {
            showScore();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void iniciarJuego(){
        Intent i = new Intent(this, Juego.class);
        startActivity(i);
    }
//    private void showProfile(){
//        Intent i = new Intent(this, Perfil.class);
//        startActivity(i);
//    }
    private void showMusic(){
        Intent i = new Intent(this, Musica.class);
        startActivity(i);
    }
    private void showCaptures(){
        Intent i = new Intent(this, Galeria.class);
        startActivity(i);
    }
     private void showCalendar(){
        Intent i = new Intent(this, Calendario.class);
        startActivity(i);
    }
    private void showHelp(){
        Intent i = new Intent(this, HelpActivity.class);
        startActivity(i);
    }
    private void showLanguage(){
        Intent i = new Intent(this, LanguageActivity.class);
        startActivity(i);
    }
    private void showScore(){
        Intent i = new Intent(this, Puntuaciones.class);
        startActivity(i);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, MusicService.class));
    }

}