package com.example.memorycrafters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> imagePaths;

    public ImageAdapter(Context context, ArrayList<String> imagePaths) {
        this.context = context;
        this.imagePaths = imagePaths;
    }

    @Override
    public int getCount() {
        return imagePaths.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
            holder.imageView = convertView.findViewById(R.id.imageView);
            holder.textViewFileName = convertView.findViewById(R.id.textViewFileName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Decodificar la imagen desde la ruta de la imagen y establecerla en el ImageView
        Bitmap bitmap = BitmapFactory.decodeFile(imagePaths.get(position));
        holder.imageView.setImageBitmap(bitmap);

        // Obtener el nombre del archivo
        String fileName = imagePaths.get(position).substring(imagePaths.get(position).lastIndexOf('/') + 1);
        holder.textViewFileName.setText(fileName);

        return convertView;
    }

    static class ViewHolder {
        ImageView imageView;
        TextView textViewFileName;
    }
}

