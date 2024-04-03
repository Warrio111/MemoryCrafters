package com.example.memorycrafters;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class Puntuaciones extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntuaciones);
        Button botonQuit = findViewById(R.id.button_quit_scores);
        databaseHelper = DatabaseHelper.getInstance(this);
        // Obtener todas las victorias de la base de datos
        HashMap<String, Integer> victorias = databaseHelper.obtenerTodasVictorias();
        String tiempoFin = "";

        TableLayout tableLayout = findViewById(R.id.tableLayoutPuntuaciones);

        // Crear una fila para la cabecera de la tabla
        TableRow headerRow = new TableRow(this);
        TextView headerFecha = new TextView(this);
        headerFecha.setText(R.string.date);
        headerRow.addView(headerFecha);
        TextView headerCantidadMonedas = new TextView(this);
        headerCantidadMonedas.setText(R.string.quantityOfcurrency);
        headerRow.addView(headerCantidadMonedas);
        TextView headerTiempoResolucion = new TextView(this);
        headerTiempoResolucion.setText(R.string.resolutionTime);
        headerRow.addView(headerTiempoResolucion);
        tableLayout.addView(headerRow);

        // Iterar sobre las victorias y agregarlas a la tabla
        for (String date : victorias.keySet()) {
            // Crear una fila para mostrar la victoria
            TableRow row = new TableRow(this);
            TextView fechaInicioTextView = new TextView(this);
            fechaInicioTextView.setText(date);
            row.addView(fechaInicioTextView);
            TextView cantidadMonedasTextView = new TextView(this);
            cantidadMonedasTextView.setText(String.valueOf(victorias.get(date)));
            row.addView(cantidadMonedasTextView);
            TextView tiempoResolucionTextView = new TextView(this);
            tiempoFin = databaseHelper.obtenerFechaFinPartida(date);
            tiempoResolucionTextView.setText(calcularDiferenciaTiempo(date, tiempoFin));
            row.addView(tiempoResolucionTextView);
            tableLayout.addView(row);
        }

        botonQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Cerrar la actividad
            }
        });
    }
    private String calcularDiferenciaTiempo(String tiempoInicio, String tiempoFin) {
        String diferenciaTiempo = "";
        try {
            // Crear objetos de tipo SimpleDateFormat para formatear las fechas
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            // Convertir las cadenas de texto en objetos de tipo Calendar
            Calendar fechaInicio = Calendar.getInstance();
            fechaInicio.setTime(Objects.requireNonNull(dateFormat.parse(tiempoInicio)));
            Calendar fechaFin = Calendar.getInstance();
            fechaFin.setTime(Objects.requireNonNull(dateFormat.parse(tiempoFin)));
            // Calcular la diferencia de tiempo en milisegundos
            long diferencia = fechaFin.getTimeInMillis() - fechaInicio.getTimeInMillis();
            // Calcular las horas, minutos y segundos de la diferencia de tiempo
            int horas = (int) (diferencia / 3600000);
            int minutos = (int) ((diferencia % 3600000) / 60000);
            int segundos = (int) ((diferencia % 60000) / 1000);
            // Formatear la diferencia de tiempo como una cadena de texto
            diferenciaTiempo = String.format(Locale.getDefault(), "%02d:%02d:%02d", horas, minutos, segundos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return diferenciaTiempo;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
