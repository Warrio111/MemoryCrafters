package com.example.memorycrafters;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.memorycrafters.models.Moneda;
import com.example.memorycrafters.models.Partida;
import com.example.memorycrafters.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;
public class Juego extends AppCompatActivity {

    // Variable de permiso necesaria para las capturas de pantalla
    private static final int REQUEST_PERMISSION_STORAGE = 100;
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
            {   updatearMonedas(puntuacion);
                //databaseHelper.registrarPartidaYMonedas(puntuacion);
                //ArrayList<String> partidas = databaseHelper.obtenerPartidas();
                actualizarMonedasAsync(puntuacion);
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
                //actualizarTableroAsync(numeroPrimero);
                databaseHelper.updateCardVisibility(numeroPrimero,1);

                if(aciertos == imagenes.length){
                    databaseHelper.finalizarPartida();
                    finalizarPartida();
                    String title = getResources().getString(R.string.youWin);
                    String message = getResources().getString(R.string.congratulationsMessage);
                    mNotificationHelper.showVictoryNotification(title, message);
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        // Si el permiso no ha sido concedido, solicítalo
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_STORAGE);
                    }
                    // Permiso ya concedido, procede con la captura de pantalla
                    GaleriaService.capturarPantalla(getApplicationContext(), getWindow().getDecorView().getRootView());

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
    // Método para actualizar las monedas de forma asíncrona
    @SuppressLint("CheckResult")
    public void actualizarMonedasAsync(int cantidadMonedas) {
        Completable.fromAction(() -> {
                    // Actualizar las monedas en la base de datos
                    databaseHelper.actualizarMonedas(cantidadMonedas);
                })
                .subscribeOn(Schedulers.io()) // Realizar la operación en un hilo de background
                .observeOn(AndroidSchedulers.mainThread()) // Observar el resultado en el hilo principal
                .doOnComplete(() -> {
                    // Manejar la finalización exitosa de la actualización
                    String successMessage = getResources().getString(R.string.succesfullUpdatingCoins);
                    Toast.makeText(getApplicationContext(), successMessage, Toast.LENGTH_SHORT).show();
                })
                .doOnError(throwable -> {
                    // Manejar cualquier error durante la actualización
                    String errorMessage = getResources().getString(R.string.errorUpdatingCoins);
                    Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    throwable.printStackTrace();
                })
                .subscribe(); // Suscribirse al Completable
    }
    @SuppressLint("CheckResult")
    public void actualizarTableroAsync(int cardID) {
        Completable.fromAction(() -> {
                    // Actualizar las monedas en la base de datos
                    databaseHelper.updateCardVisibility(cardID,1);
                })
                .subscribeOn(Schedulers.io()) // Realizar la operación en un hilo de background
                .observeOn(AndroidSchedulers.mainThread()) // Observar el resultado en el hilo principal
                .doOnComplete(() -> {
                    // Manejar la finalización exitosa de la actualización
                    String successMessage = getResources().getString(R.string.boardUpdatedSuccessfully);
                    Toast.makeText(getApplicationContext(), successMessage, Toast.LENGTH_SHORT).show();
                })
                .doOnError(throwable -> {
                    // Manejar cualquier error durante la actualización
                    String errorMessage = getResources().getString(R.string.errorUpdatingBoard);
                    Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    throwable.printStackTrace();
                })
                .subscribe(); // Suscribirse
    }
    private void createPartida() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            String userUUID = currentUser.getUid();
            String userDisplayName = currentUser.getDisplayName();

            databaseHelper = DatabaseHelper.getInstance(this);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            // First of all create a new document in the collection monedas
            Moneda moneda = new Moneda(databaseHelper.obtenerUltimaMonedaId(), 0);
            User user = new User(userEmail,userUUID);
            Partida partida = new Partida(databaseHelper.obtenerUltimaPartidaId(),obtenerFechaActual(),null,null,null);
            db.collection("monedas")
                    .add(moneda)
                    .addOnSuccessListener(documentReference -> {
                        // Then create a new document in the collection users
                        db.collection("users")
                                .add(user)
                                .addOnSuccessListener(documentReference1 -> {
                                    // Finally create a new document in the collection partidas
                                    db.collection("partidas")
                                            .add(partida)
                                            .addOnSuccessListener(documentReference2 -> {
                                                // Update the partida with the id of the moneda and the user
                                                db.collection("partidas")
                                                        .document(documentReference2.getId())
                                                        .update("idMonedas", documentReference.getPath(), "idUsers", documentReference1.getPath());
                                            });
                                    db.collection("users")
                                            .document(documentReference1.getId())
                                            .update("premio", false);
                                });
                    });
        }
    }

    private void updatearMonedas(int cantidadMonedas) {
        // Actualizar las monedas en la base de datos
        int monedaIdSqlite = databaseHelper.obtenerUltimaMonedaId();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("monedas")
                .whereEqualTo("id", monedaIdSqlite)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Actualizar el campo "cantidad" del documento encontrado
                            document.getReference().update("cantidadMonedas", cantidadMonedas)
                                    .addOnSuccessListener(aVoid -> {
                                        // La actualización se realizó con éxito
                                        String successMessage = getResources().getString(R.string.succesfullUpdatingCoins);
                                        Toast.makeText(getApplicationContext(), successMessage, Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        // Ocurrió un error al intentar actualizar el documento
                                        String errorMessage = getResources().getString(R.string.errorUpdatingCoins);
                                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    });
                        }
                    } else {
                        // Ocurrió un error al intentar buscar el documento
                        String errorMessage = getResources().getString(R.string.errorUpdatingCoins);
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        Exception e = task.getException();
                        if (e != null) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void finalizarPartida()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        int ultimaPartidaID = databaseHelper.obtenerUltimaPartidaId();
        String fichaFinal = obtenerFechaActual();
        db.collection("partidas")
                .whereEqualTo("id", ultimaPartidaID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Actualizar el campo "fechaFin" del documento encontrado
                            document.getReference().update("urlPremio", "https://cdn-icons-png.flaticon.com/512/6303/6303576.png", "fechaFin", fichaFinal)
                                    .addOnSuccessListener(aVoid -> {
                                        // La actualización se realizó con éxito
                                        String successMessage = "Partida Fecha Fin Actualizada con éxito";
                                        Toast.makeText(getApplicationContext(), successMessage, Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        // Ocurrió un error al intentar actualizar el documento
                                        String errorMessage = "Partida Fecha Fin No Actualizada";
                                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    });
                        }
                    } else {
                        // Ocurrió un error al intentar buscar el documento
                        String errorMessage = "Ocurrió un error al intentar buscar el documento";
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        Exception e = task.getException();
                        if (e != null) {
                            e.printStackTrace();
                        }
                    }
                });
        db.collection("users")
                .whereEqualTo("uuid", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Actualizar el campo "premio" del documento encontrado
                            document.getReference().update("premio", true)
                                    .addOnSuccessListener(aVoid -> {
                                        // La actualización se realizó con éxito
                                        String successMessage = "Usuario Premio Actualizado con éxito";
                                        Toast.makeText(getApplicationContext(), successMessage, Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        // Ocurrió un error al intentar actualizar el documento
                                        String errorMessage = "Usuario Premio No Actualizado";
                                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    });
                        }
                    } else {
                        // Ocurrió un error al intentar buscar el documento
                        String errorMessage = "Ocurrió un error al intentar buscar el documento";
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        Exception e = task.getException();
                        if (e != null) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public String obtenerFechaActual() {
        // Obtener la fecha y hora actual
        Calendar calendar = Calendar.getInstance();
        // Formatear la fecha y hora actual como una cadena de texto
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    private void init(){
        cargarTablero();
        cargarBotones();
        cargarTexto();
        cargarImagenes();
        databaseHelper = DatabaseHelper.getInstance(this);
        databaseHelper.inicializarPartida(imagenes.length);
        createPartida();
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
