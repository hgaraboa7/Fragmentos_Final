package com.example.fragmentos_1.vista.fragmentos;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.fragmentos_1.R;
import com.example.fragmentos_1.mock.ObtencionDatos;
import com.example.fragmentos_1.modelo.entidades.Actor;
import com.example.fragmentos_1.vista.actividades.VistaActor;
import com.example.fragmentos_1.vista.adaptadores.ActorAdapter;

import java.util.ArrayList;

public class ListaActores extends Fragment {

    private ListView listViewActores;
    private ActorAdapter adapter;
    private ArrayList<Actor> listaActores = new ArrayList<>();
    private int idPelicula = -1;

    public static ListaActores newInstance(int idPelicula) {
        ListaActores fragment = new ListaActores();
        Bundle args = new Bundle();
        args.putInt("idPelicula", idPelicula);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idPelicula = getArguments().getInt("idPelicula", -1);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_actores, container, false);
        listViewActores = view.findViewById(R.id.listViewActores);

        adapter = new ActorAdapter(getContext(), listaActores);
        listViewActores.setAdapter(adapter);

        if (idPelicula == -1) {
            new ObtencionDatos().obtenerTodosLosActores(new ObtencionDatos.ActoresCallback() {
                @Override
                public void onSuccess(ArrayList<Actor> actores) {
                    getActivity().runOnUiThread(() -> {
                        listaActores.clear();
                        listaActores.addAll(actores);
                        adapter.notifyDataSetChanged();
                    });
                }

                @Override
                public void onFailure(String error) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Error al obtener actores: " + error, Toast.LENGTH_LONG).show();
                    });
                }
            });
        } else {
            new ObtencionDatos().obtenerListadoActores(idPelicula, new ObtencionDatos.ActoresCallback() {
                @Override
                public void onSuccess(ArrayList<Actor> actores) {
                    getActivity().runOnUiThread(() -> {
                        listaActores.clear();
                        listaActores.addAll(actores);
                        adapter.notifyDataSetChanged();
                    });
                }

                @Override
                public void onFailure(String error) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Error al obtener actores: " + error, Toast.LENGTH_LONG).show();
                    });
                }
            });
        }

        listViewActores.setOnItemClickListener((parent, view1, position, id) -> {
            Actor actorSeleccionado = listaActores.get(position);
            Intent intent = new Intent(getActivity(), VistaActor.class);
            intent.putExtra("actor", actorSeleccionado);
            startActivity(intent);

        });

        return view;
    }
}
