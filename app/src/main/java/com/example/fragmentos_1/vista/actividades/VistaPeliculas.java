package com.example.fragmentos_1.vista.actividades;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.fragmentos_1.R;
import com.example.fragmentos_1.modelo.entidades.Pelicula;
import com.example.fragmentos_1.vista.fragmentos.DatosPelicula;

import java.io.ByteArrayOutputStream;

public class VistaPeliculas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_pelicula);

        Pelicula pelicula = (Pelicula) getIntent().getSerializableExtra("pelicula");
        byte[] imagenBytes = getIntent().getByteArrayExtra("imagenPelicula");

        if (imagenBytes != null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            Bitmap bitmap = BitmapFactory.decodeByteArray(imagenBytes, 0, imagenBytes.length, options);

            if (bitmap != null) {
                imagenBytes = convertirBitmapAByteArray(bitmap);
            } else {
                imagenBytes = null;
            }
        }

        if (pelicula != null) {
            DatosPelicula detalleFragment = DatosPelicula.newInstance(pelicula, imagenBytes);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerDetalleVistaPelicula, detalleFragment)
                    .commit();
        }
    }

    private byte[] convertirBitmapAByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        return stream.toByteArray();
    }
}
