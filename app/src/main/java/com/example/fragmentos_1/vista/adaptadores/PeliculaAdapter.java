package com.example.fragmentos_1.vista.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import android.widget.ArrayAdapter;
import com.example.fragmentos_1.R;
import com.example.fragmentos_1.modelo.entidades.Pelicula;
import com.example.fragmentos_1.vista.fragmentos.DatosPelicula;
import java.io.ByteArrayOutputStream;
import java.util.List;

public class PeliculaAdapter extends ArrayAdapter<Pelicula> {

    public PeliculaAdapter(@NonNull Context context, @NonNull List<Pelicula> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.pelicula_element, parent, false);
        }

        Pelicula pelicula = getItem(position);
        if (pelicula != null) {
            TextView txtNombre = convertView.findViewById(R.id.txtNombrePelicula);
            TextView txtGenero = convertView.findViewById(R.id.txtGeneroPelicula);
            ImageView imgPelicula = convertView.findViewById(R.id.imgPelicula);

            txtNombre.setText(pelicula.getNombre());
            txtGenero.setText(pelicula.getGenero());

            Bitmap imagenPelicula = pelicula.getImagenBitmap();
            if (imagenPelicula != null) {
                imgPelicula.setImageBitmap(imagenPelicula);
            } else {
                imgPelicula.setImageResource(R.drawable.ic_launcher_foreground);
            }

            convertView.setOnClickListener(v -> {
                byte[] imagenBytes = convertirBitmapAByteArray(pelicula.getImagenBitmap());

                if (getContext() instanceof AppCompatActivity) {
                    AppCompatActivity activity = (AppCompatActivity) getContext();
                    FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragmentContainerView, DatosPelicula.newInstance(pelicula, imagenBytes));
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });
        }
        return convertView;
    }

    private byte[] convertirBitmapAByteArray(Bitmap bitmap) {
        if (bitmap == null) return null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        return stream.toByteArray();
    }
}
