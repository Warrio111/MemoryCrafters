package com.example.memorycrafters;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Calendario extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private HashMap<String, Integer> victorias; // Almacenar las victorias obtenidas de la base de datos
    private Button exitCalendarioButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendario_layout);

        databaseHelper = DatabaseHelper.getInstance(this);
        victorias = databaseHelper.obtenerTodasVictorias(); // Obtener todas las victorias de la base de datos
        exitCalendarioButton = findViewById(R.id.exitCalendarButton);
        CalendarView calendarView = findViewById(R.id.calendarView);

        // Configurar un listener para el evento de selección de fecha
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Obtener la fecha seleccionada
                String selectedDate = formatDate(year, month, dayOfMonth);

                // Mostrar las victorias en una tabla si existen para esa fecha
                mostrarVictorias(selectedDate);
            }
        });

        // Obtener el primer día del mes y el último día del mes
        Calendar calendar = Calendar.getInstance();
        calendarView.setMaxDate(calendar.getTimeInMillis());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        long minDate = calendar.getTimeInMillis();
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        long maxDate = calendar.getTimeInMillis();
        calendarView.setMinDate(minDate);
        calendarView.setMaxDate(maxDate);

        // Mostrar las victorias en el calendario
        for (String date : victorias.keySet()) {
            long dateInMillis = getDateInMillis(date);
            if (dateInMillis >= minDate && dateInMillis <= maxDate) {
                // Resaltar el día en el calendario si hay una victoria asociada
                calendarView.setDate(dateInMillis, true, true);
            }
        }
        exitCalendarioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void mostrarVictorias(String selectedDate) {
        // Crear una tabla para mostrar las victorias
        TableLayout tableLayout = findViewById(R.id.tableLayout);
        tableLayout.removeAllViews(); // Limpiar la tabla antes de agregar nuevas filas

        // Crear una fila para la cabecera de la tabla
        TableRow headerRow = new TableRow(this);
        TextView headerFecha = new TextView(this);
        headerFecha.setText(R.string.date);
        headerRow.addView(headerFecha);
        TextView headerCantidadMonedas = new TextView(this);
        headerCantidadMonedas.setText(R.string.quantityOfcurrency);
        headerRow.addView(headerCantidadMonedas);
        tableLayout.addView(headerRow);

        // Iterar sobre las victorias y mostrar las que corresponden a la fecha seleccionada
        for (String date : victorias.keySet()) {
            // Obtener el Year-Month-Day de la fecha
            String[] dateParts = date.split("-");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date dateformated = null;
            try {
                dateformated = dateFormat.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            // Extraer el año, mes y día de la fecha completa
            Calendar calendar = Calendar.getInstance();
            assert dateformated != null;
            calendar.setTime(dateformated);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH); // Se resta 1 ya que en Calendar los meses empiezan desde 0
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            String[] selectedDateParts = selectedDate.split("-");
            int selectedYear = Integer.parseInt(selectedDateParts[0]);
            int selectedMonth = Integer.parseInt(selectedDateParts[1]);
            int selectedDay = Integer.parseInt(selectedDateParts[2]);
            if (year == selectedYear && month + 1 == selectedMonth && day == selectedDay) {
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
        }

        // Mostrar un mensaje si no hay victorias registradas para la fecha seleccionada
        if (tableLayout.getChildCount() == 1) {
            String message = getResources().getString(R.string.noVictories) + " " + selectedDate;
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }


    private String formatDate(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    private long getDateInMillis(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            return dateFormat.parse(dateString).getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
