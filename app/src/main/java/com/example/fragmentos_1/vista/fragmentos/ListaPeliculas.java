package com.example.fragmentos_1.vista.fragmentos;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.example.fragmentos_1.R;
import com.example.fragmentos_1.mock.ObtencionDatos;
import com.example.fragmentos_1.modelo.entidades.Pelicula;
import com.example.fragmentos_1.vista.adaptadores.PeliculaAdapter;
import java.util.ArrayList;

public class ListaPeliculas extends Fragment {

    private ListView listViewPeliculas;
    private PeliculaAdapter adapter;
    private ArrayList<Pelicula> listaPeliculas;
    private OnPeliculaSelectedListener listener;  // Referencia a la actividad

    public interface OnPeliculaSelectedListener {
        void onPeliculaSelected(Pelicula pelicula);
    }

    @Override
    public void onAttach(@NonNull android.content.Context context) {
        super.onAttach(context);
        if (context instanceof OnPeliculaSelectedListener) {
            listener = (OnPeliculaSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString() );
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_peliculas, container, false);
        listViewPeliculas = view.findViewById(R.id.listViewPeliculas);

        listaPeliculas = new ArrayList<>();
        adapter = new PeliculaAdapter(requireContext(), listaPeliculas);
        listViewPeliculas.setAdapter(adapter);

        listViewPeliculas.setOnItemClickListener((parent, view1, position, id) -> {
            if (listener != null) {
                listener.onPeliculaSelected(listaPeliculas.get(position));
            }
        });

        new ObtencionDatos().obtenerListadoPeliculas(new ObtencionDatos.PeliculasCallback() {
            @Override
            public void onSuccess(ArrayList<Pelicula> peliculas) {
                if (getActivity() == null) return;
                getActivity().runOnUiThread(() -> {
                    listaPeliculas.clear();
                    listaPeliculas.addAll(peliculas);
                    adapter.notifyDataSetChanged();
                });
            }

            @Override
            public void onFailure(String error) {
                if (getActivity() == null) return;
                getActivity().runOnUiThread(() -> System.err.println("Error al obtener películas: " + error));
            }
        });

        return view;
    }
}
