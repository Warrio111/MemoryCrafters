package com.example.memorycrafters;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

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

        TableLayout tableLayout = findViewById(R.id.tableLayoutPuntuaciones);

        // Crear una fila para la cabecera de la tabla
        TableRow headerRow = new TableRow(this);
        TextView headerFecha = new TextView(this);
        headerFecha.setText(R.string.date);
        headerRow.addView(headerFecha);
        TextView headerCantidadMonedas = new TextView(this);
        headerCantidadMonedas.setText(R.string.quantityOfcurrency);
        headerRow.addView(headerCantidadMonedas);
        tableLayout.addView(headerRow);

        // Iterar sobre las victorias y agregarlas a la tabla
        for (String date : victorias.keySet()) {
            // Crear una fila para mostrar la victoria
            TableRow row = new TableRow(this);
            TextView fechaTextView = new TextView(this);
            fechaTextView.setText(date);
            row.addView(fechaTextView);
            TextView cantidadMonedasTextView = new TextView(this);
            cantidadMonedasTextView.setText(String.valueOf(victorias.get(date)));
            row.addView(cantidadMonedasTextView);
            tableLayout.addView(row);
        }

        botonQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Cerrar la actividad
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
