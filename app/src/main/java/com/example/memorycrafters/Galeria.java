package com.example.memorycrafters;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.util.ArrayList;

public class Galeria extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_STORAGE = 100;

    private GridView gridView;
    private ListView listView;
    private ImageAdapter imageAdapter;
    private ArrayList<String> imagePaths;
    private Button botonSalir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_item);

        listView = findViewById(R.id.listView);
        botonSalir = findViewById(R.id.botonSalirGaleria);
        imagePaths = new ArrayList<>();

        // Verificar permisos de almacenamiento
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_STORAGE);
        } else {
            // Mostrar las imágenes almacenadas
            mostrarImagenes();
        }
        botonSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Cerrar la actividad
            }
        });
    }

    // Método para obtener las imágenes almacenadas y mostrarlas en el ListView
    private void mostrarImagenes() {
        imagePaths.clear();
        File directory = new File(Environment.getExternalStorageDirectory() + "/Pictures");
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    imagePaths.add(file.getAbsolutePath());
                }
            }
        }
        imageAdapter = new ImageAdapter(this, imagePaths);
        listView.setAdapter(imageAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mostrarImagenes();
            } else {
                Toast.makeText(this, "Permiso de almacenamiento denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }
}


