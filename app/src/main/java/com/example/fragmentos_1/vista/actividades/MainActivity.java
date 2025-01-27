package com.example.fragmentos_1.vista.actividades;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fragmentos_1.R;
import com.example.fragmentos_1.modelo.entidades.Pelicula;
import com.example.fragmentos_1.vista.fragmentos.DatosPelicula;
import com.example.fragmentos_1.vista.fragmentos.ListaActores;
import com.example.fragmentos_1.vista.fragmentos.ListaPeliculas;

public class MainActivity extends AppCompatActivity implements ListaPeliculas.OnPeliculaSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerView, new ListaPeliculas())
                    .commit();
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_acciones, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_peliculas) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerView, new ListaPeliculas())
                    .commit();
            return true;
        } else if (id == R.id.menu_actores) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerView, ListaActores.newInstance(-1))
                    .commit();
            return true;
        }

        else if (id == R.id.menu_sincronizar) {
            Toast.makeText(this, "Sincronizacion no implementada", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.menu_salir) {
            finishAffinity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onPeliculaSelected(Pelicula pelicula) {
        boolean isTablet = (getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;

        byte[] imagenBytes = DatosPelicula.convertirBitmapAByteArray(pelicula.getImagenBitmap());

        if (isTablet) {
            DatosPelicula detalleFragment = DatosPelicula.newInstance(pelicula, imagenBytes);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerDetalle, detalleFragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, VistaPeliculas.class);
            intent.putExtra("pelicula", pelicula);
            intent.putExtra("imagenPelicula", imagenBytes);
            startActivity(intent);
        }
    }


}
