package com.example.fragmentos_1.vista.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.fragmentos_1.R;
import com.example.fragmentos_1.mock.ObtencionDatos;
import com.example.fragmentos_1.modelo.entidades.Actor;
import com.example.fragmentos_1.vista.adaptadores.ActorAdapter;

import java.util.ArrayList;

public class ListadoActoresActivity extends AppCompatActivity {

    private ListView listViewActores;
    private ActorAdapter adapter;
    private ArrayList<Actor> listaActores = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_actores);

        listViewActores = findViewById(R.id.listViewActores);
        adapter = new ActorAdapter(this, listaActores);
        listViewActores.setAdapter(adapter);

        cargarTodosLosActores();

        listViewActores.setOnItemClickListener((parent, view, position, id) -> {
            Actor actorSeleccionado = listaActores.get(position);
            Intent intent = new Intent(this, VistaActor.class);
            intent.putExtra("actor", actorSeleccionado);
            startActivity(intent);
        });
    }

    private void cargarTodosLosActores() {
        new ObtencionDatos().obtenerListadoActores(0, new ObtencionDatos.ActoresCallback() {
            @Override
            public void onSuccess(ArrayList<Actor> actores) {
                runOnUiThread(() -> {
                    listaActores.clear();
                    listaActores.addAll(actores);
                    adapter.notifyDataSetChanged();
                });
            }

            @Override
            public void onFailure(String error) {
                runOnUiThread(() ->
                        Toast.makeText(ListadoActoresActivity.this, "Error al cargar actores: " + error, Toast.LENGTH_LONG).show()
                );
            }
        });
    }
}
