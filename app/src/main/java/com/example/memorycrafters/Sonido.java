package com.example.memorycrafters;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;

public class Sonido {

    private SoundPool soundPool;
    private int soundCorrecto;
    private int soundIncorrecto;
    private Context context;

    public Sonido(Context context) {
        this.context = context;
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .setMaxStreams(2) // 2 streams para los dos sonidos
                .build();

        // Cargar los sonidos
        soundCorrecto = soundPool.load(context, R.raw.ficha_correcta, 1);
        soundIncorrecto = soundPool.load(context, R.raw.ficha_incorrecta, 1);
    }


    // Método publico para reproducir un sonido dado su SoundAnimation
    public void reproducirSonido(SoundAnimation soundAnimation) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        float actualVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = actualVolume / maxVolume;

        switch (soundAnimation)
        {
            case Ficha_Correcta:
                soundPool.play(soundCorrecto, volume, volume, 1, 0, 1f);
                break;
            case Ficha_Incorrecta:
                soundPool.play(soundIncorrecto, volume, volume, 1, 0, 1f);
            default:
                break;
        }

    }
}
