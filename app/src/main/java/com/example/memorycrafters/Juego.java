package com.example.memorycrafters;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.ActionBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;

public class Juego extends AppCompatActivity {

    // variables para los componentes de la vista
    ImageButton imb00, imb01, imb02, imb03, imb04, imb05, imb06, imb07, imb08, imb09, imb10, imb11, imb12, imb13, imb14, imb15;
    ImageButton[] tablero = new ImageButton[16];
    Button  botonSalir;
    TextView textoPuntuacion;
    int puntuacion;
    int aciertos;
    //imagenes
    int[] imagenes;
    int fondo;

    //variables del juego
    ArrayList<Integer> arrayDesordenado;
    ImageButton primero;
    int numeroPrimero, numeroSegundo;
    boolean blockFlag = false;
    final Handler handler = new Handler();

    DatabaseHelper databaseHelper;
    private Sonido sonidoAnimaciones;
    private NotificationHelper mNotificationHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.juego);
        // Mostrar ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true); // Mostrar botón de retroceso en la ActionBar
        }
        databaseHelper = new DatabaseHelper(this);
        sonidoAnimaciones = new Sonido(this);
        mNotificationHelper = new NotificationHelper(this);
        init();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_navigation, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Manejar los clics en los elementos del menú
        int id = item.getItemId();
        if (id == R.id.action_music) {
            Intent i = new Intent(this, Musica.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void cargarTablero(){
        imb00 = findViewById(R.id.boton00);
        imb01 = findViewById(R.id.boton01);
        imb02 = findViewById(R.id.boton02);
        imb03 = findViewById(R.id.boton03);
        imb04 = findViewById(R.id.boton04);
        imb05 = findViewById(R.id.boton05);
        imb06 = findViewById(R.id.boton06);
        imb07 = findViewById(R.id.boton07);
        imb08 = findViewById(R.id.boton08);
        imb09 = findViewById(R.id.boton09);
        imb10 = findViewById(R.id.boton10);
        imb11 = findViewById(R.id.boton11);
        imb12 = findViewById(R.id.boton12);
        imb13 = findViewById(R.id.boton13);
        imb14 = findViewById(R.id.boton14);
        imb15 = findViewById(R.id.boton15);

        tablero[0] = imb00;
        tablero[1] = imb01;
        tablero[2] = imb02;
        tablero[3] = imb03;
        tablero[4] = imb04;
        tablero[5] = imb05;
        tablero[6] = imb06;
        tablero[7] = imb07;
        tablero[8] = imb08;
        tablero[9] = imb09;
        tablero[10] = imb10;
        tablero[11] = imb11;
        tablero[12] = imb12;
        tablero[13] = imb13;
        tablero[14] = imb14;
        tablero[15] = imb15;
    }

    private void cargarBotones(){
        botonSalir = findViewById(R.id.botonJuegoSalir);
        botonSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //databaseHelper.registrarPartidaYMonedas(puntuacion);
                //ArrayList<String> partidas = databaseHelper.obtenerPartidas();
                registrarPartidaYMonedasAsync(puntuacion);
                finish();
            }
        });
    }

    private void cargarTexto(){
        textoPuntuacion = findViewById(R.id.texto_puntuacion);
        puntuacion = 0;
        aciertos = 0;
        textoPuntuacion.setText("" + puntuacion);
    }

    private void cargarImagenes(){
        imagenes = new int[]{
                R.drawable.la0,
                R.drawable.la1,
                R.drawable.la2,
                R.drawable.la3,
                R.drawable.la4,
                R.drawable.la5,
                R.drawable.la6,
                R.drawable.la7
        };
        fondo = R.drawable.fondo;
    }

    private ArrayList<Integer> barajar(int longitud){
        ArrayList<Integer> result = new ArrayList<Integer>();
        for(int i=0; i<longitud*2; i++){
            result.add(i % longitud);
        }
        Collections.shuffle(result);
        // System.out.println(Arrays.toString(result.toArray()));
        return result;
    }

    private void comprobar(int i, final ImageButton imgb){
        if(primero == null){
            primero = imgb;
            primero.setScaleType(ImageView.ScaleType.CENTER_CROP);
            primero.setImageResource(imagenes[arrayDesordenado.get(i)]);
            primero.setEnabled(false);
            numeroPrimero = arrayDesordenado.get(i);
        } else {
            blockFlag = true;
            imgb.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imgb.setImageResource(imagenes[arrayDesordenado.get(i)]);
            imgb.setEnabled(false);
            numeroSegundo = arrayDesordenado.get(i);
            if(numeroPrimero == numeroSegundo){
                sonidoAnimaciones.reproducirSonido(SoundAnimation.Ficha_Correcta);
                primero = null;
                blockFlag = false;
                aciertos++;
                puntuacion++;
                textoPuntuacion.setText("" + puntuacion);
                if(aciertos == imagenes.length){
                    mNotificationHelper.showVictoryNotification("¡Has ganado!", "¡Felicidades! Has conseguido todas las parejas. ¡Sigue así!");
                    GaleriaService.capturarPantalla(getApplicationContext(), getWindow().getDecorView().getRootView());
                }
            } else {
                sonidoAnimaciones.reproducirSonido(SoundAnimation.Ficha_Incorrecta);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        primero.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        primero.setImageResource(fondo);
                        primero.setEnabled(true);
                        imgb.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        imgb.setImageResource(fondo);
                        imgb.setEnabled(true);
                        blockFlag = false;
                        primero = null;
                        puntuacion--;
                        textoPuntuacion.setText("" + puntuacion);
                    }
                }, 1000);
            }
        }
    }
    @SuppressLint("CheckResult")
    private void registrarPartidaYMonedasAsync(int cantidadMonedas) {
        Completable.fromAction(() -> {
                    databaseHelper.registrarPartidaYMonedas(cantidadMonedas);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> {
                    Toast.makeText(getApplicationContext(), "Partida registrada correctamente", Toast.LENGTH_SHORT).show();
                })
                .doOnError(throwable -> {
                    Toast.makeText(getApplicationContext(), "Error al registrar la partida", Toast.LENGTH_SHORT).show();
                    throwable.printStackTrace();
                })
                .subscribe(); // Suscribirse al Completable
    }
    private void init(){
        cargarTablero();
        cargarBotones();
        cargarTexto();
        cargarImagenes();
        arrayDesordenado = barajar(imagenes.length);
        // Este proceso posiciona las imagenes en el tablero
        for(int i=0; i<tablero.length; i++){
            tablero[i].setScaleType(ImageView.ScaleType.CENTER_CROP);
            tablero[i].setImageResource(imagenes[arrayDesordenado.get(i)]);
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (ImageButton imageButton : tablero) {
                    imageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imageButton.setImageResource(fondo);
                }
            }
        }, 2000);
        for(int i=0; i<tablero.length; i++) {
            final int j = i;
            tablero[i].setEnabled(true);
            tablero[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!blockFlag)
                        comprobar(j, tablero[j]);
                }
            });
        }

    }

}
