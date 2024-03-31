package com.example.memorycrafters;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GaleriaService {

    // Método para realizar una captura de pantalla y almacenarla en la galería
    public static void capturarPantalla(Context context, View view) {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // Obtener las dimensiones de la vista
            int width = view.getWidth();
            int height = view.getHeight();

            // Crear un bitmap con las dimensiones de la vista
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

            // Crear un canvas con el bitmap
            Canvas canvas = new Canvas(bitmap);

            // Dibujar la vista en el canvas
            view.draw(canvas);

            // Guardar la captura de pantalla en la carpeta de imágenes
            File file = new File(Environment.getExternalStorageDirectory() + "/Pictures", "Captura_" + now + ".png");
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

            // Notificar al sistema de la nueva imagen para que aparezca en la galería
            MediaScannerConnection.scanFile(context, new String[]{file.getPath()}, new String[]{"image/png"}, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

