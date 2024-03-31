package com.example.memorycrafters;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.annotation.Nullable;

import java.io.IOException;

public class Musica extends Activity {

    private static final int REQUEST_CODE_SELECT_MUSIC = 1001;
    private Button playButton, pauseButton, exitButton, selectMusicButton;

    private AudioManager audioManager;
    private SeekBar volumeSeekBar;
    private Sonido sonido;
    private PhoneStateListener phoneStateListener;

    private Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.musica);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        volumeSeekBar = findViewById(R.id.volumeSeekBar);
        volumeSeekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        volumeSeekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

        // Configurar el listener para el SeekBar de volumen
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        // Inicializar los botones
        playButton = findViewById(R.id.playButton);
        pauseButton = findViewById(R.id.pauseButton);
        exitButton = findViewById(R.id.exitButton);
        selectMusicButton = findViewById(R.id.selectMusicButton);

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
        // Configurar el botón de seleccionar musica
        selectMusicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectBackgroundMusic();
            }
        });

        // Configurar el botón de salida
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
        sonido = new Sonido(this);
        // Iniciar el servicio de música
        startService(new Intent(this, MusicService.class));
    }
    public void executeSoundAnimation(SoundAnimation soundAnimation)
    {
        sonido.reproducirSonido(soundAnimation);
    }

    // Método para iniciar la reproducción de música
    private void playMusic() {
        Intent intent = new Intent(MusicService.ACTION_PLAY);
        sendBroadcast(intent);
    }

    // Método para pausar la reproducción de música
    private void pauseMusic() {
        Intent intent = new Intent(MusicService.ACTION_PAUSE);
        sendBroadcast(intent);
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
            Intent intent = new Intent(MusicService.ACTION_SET_MUSIC);
            assert selectedMusicUri != null;
            intent.putExtra(MusicService.EXTRA_MUSIC_URI, selectedMusicUri.toString());
            sendBroadcast(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
