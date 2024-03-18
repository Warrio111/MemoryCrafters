package com.example.memorycrafters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
public class Musica extends Activity {
    private static final int REQUEST_CODE_SELECT_MUSIC = 1001;
    private MediaPlayer mediaPlayer;
    private Button playButton;
    private Button pauseButton;
    private AudioManager audioManager;
    private PhoneStateListener phoneStateListener;

    private Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.musica);

        // Inicializar los botones
        playButton = findViewById(R.id.playButton);
        pauseButton = findViewById(R.id.pauseButton);

        // Inicializar el reproductor de música
        mediaPlayer = MediaPlayer.create(this, R.raw.sample_music);

        // Configurar el botón de reproducción
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playMusic();
            }
        });

        // Configurar el botón de pausa
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseMusic();
            }
        });

        animation = AnimationUtils.loadAnimation(this, R.anim.move_animation);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);
                if (state == TelephonyManager.CALL_STATE_RINGING) {
                    // Pausar la reproducción de música cuando se recibe una llamada
                    pauseMusic();
                } else if (state == TelephonyManager.CALL_STATE_IDLE) {
                    // Reanudar la reproducción de música cuando la llamada termina
                    playMusic();
                }
            }
        };
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    // Método para iniciar la reproducción de música
    private void playMusic() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    // Método para pausar la reproducción de música
    private void pauseMusic() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }
    private void selectBackgroundMusic() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/*");
        startActivityForResult(intent, REQUEST_CODE_SELECT_MUSIC);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_MUSIC && resultCode == RESULT_OK && data != null) {
            Uri selectedMusicUri = data.getData();
            // Inicializar el reproductor de música con la música seleccionada
            try {
                mediaPlayer.setDataSource(this, selectedMusicUri);
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Liberar los recursos del reproductor de música
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
