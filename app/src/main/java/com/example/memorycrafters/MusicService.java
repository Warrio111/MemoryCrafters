package com.example.memorycrafters;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;

import java.io.IOException;

public class MusicService extends Service {
    public static final String ACTION_PLAY = "com.example.memorycrafters.action.PLAY";
    public static final String ACTION_PAUSE = "com.example.memorycrafters.action.PAUSE";
    public static final String ACTION_SET_MUSIC = "com.example.memorycrafters.action.SET_MUSIC";
    public static final String EXTRA_MUSIC_URI = "com.example.memorycrafters.extra.MUSIC_URI";

    private MediaPlayer mediaPlayer;
    private BroadcastReceiver receiver;
    
    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.sample_music);
        mediaPlayer.start();
        mediaPlayer.setLooping(true); // Repetir la música continuamente

        // Registrar el receptor de difusión para ACTION_PAUSE
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null && intent.getAction() != null) {
                    String action = intent.getAction();
                    switch (action) {
                        case ACTION_PLAY:
                            playMusic();
                            break;
                        case ACTION_PAUSE:
                            pauseMusic();
                            break;
                        case ACTION_SET_MUSIC:
                            setMusic(intent.getStringExtra(EXTRA_MUSIC_URI));
                            break;
                    }
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_PLAY);
        filter.addAction(ACTION_PAUSE);
        filter.addAction(ACTION_SET_MUSIC);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(receiver, filter, RECEIVER_EXPORTED);
        }else {
            registerReceiver(receiver, filter);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY; // El servicio seguirá funcionando hasta que sea detenido explícitamente
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop(); // Detener la reproducción de la música
            mediaPlayer.release(); // Liberar recursos
        }
        // Desregistrar el receptor de difusión
        unregisterReceiver(receiver);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private void setMusic(String musicUri) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release(); // Liberar recursos del reproductor existente
        }

        mediaPlayer = MediaPlayer.create(this, Uri.parse(musicUri));
        mediaPlayer.setLooping(true); // Repetir la música continuamente
        mediaPlayer.start(); // Iniciar la reproducción de la nueva música
    }

    // Método para iniciar la reproducción de música
    private void playMusic() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    // Método para pausar la reproducción de música
    private void pauseMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }
}
