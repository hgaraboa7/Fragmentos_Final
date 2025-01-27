package com.example.fragmentos_1.vista.fragmentos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import com.example.fragmentos_1.R;
import com.example.fragmentos_1.modelo.entidades.Pelicula;
import com.example.fragmentos_1.room.AppDatabase;
import com.example.fragmentos_1.room.PeliculaDao;
import com.example.fragmentos_1.room.PeliculaEntity;
import com.example.fragmentos_1.vista.actividades.SeleccionFechaActivity;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DatosPelicula extends Fragment {

    private TextView txtNombre, txtFecha, txtGenero, txtSinopsis, txtComentarios, txtFechaProximaEmision;
    private ImageView imgDetallePelicula;
    private RatingBar ratingPuntuacion;
    private Button btnEditarComentarios, btnIndicarFecha, btnGuardar;
    private Pelicula pelicula;


    private final ActivityResultLauncher<Intent> lanzadorFechaHora =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    long fechaHoraMilis = result.getData().getLongExtra("fechaHoraMilis", 0);
                    String nuevaFecha = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(fechaHoraMilis);
                    txtFechaProximaEmision.setText(nuevaFecha);


                    new Thread(() -> {
                        AppDatabase db = AppDatabase.getInstance(getContext());
                        PeliculaDao peliculaDao = db.peliculaDao();
                        peliculaDao.actualizarFechaEmision(pelicula.getId(), nuevaFecha);
                    }).start();
                }
            });

    public static DatosPelicula newInstance(Pelicula pelicula, byte[] imagenBytes) {
        DatosPelicula fragment = new DatosPelicula();
        Bundle args = new Bundle();
        args.putSerializable("pelicula", pelicula);
        args.putByteArray("imagenPelicula", imagenBytes);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pelicula = (Pelicula) getArguments().getSerializable("pelicula");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalle_pelicula, container, false);

        txtNombre = view.findViewById(R.id.txtNombreDetallePelicula);
        txtFecha = view.findViewById(R.id.txtFechaDetallePelicula);
        txtGenero = view.findViewById(R.id.txtGeneroDetallePelicula);
        txtSinopsis = view.findViewById(R.id.txtSinopsisDetallePelicula);
        txtComentarios = view.findViewById(R.id.txtComentarios);
        txtFechaProximaEmision = view.findViewById(R.id.txtFechaProximaEmision);
        imgDetallePelicula = view.findViewById(R.id.imgDetallePelicula);
        ratingPuntuacion = view.findViewById(R.id.ratingPuntuacion);
        btnEditarComentarios = view.findViewById(R.id.btnEditarComentarios);
        btnIndicarFecha = view.findViewById(R.id.btnIndicarFecha);
        btnGuardar = view.findViewById(R.id.btnGuardar);

        if (pelicula != null) {
            txtNombre.setText(pelicula.getNombre());
            txtFecha.setText(pelicula.getFecha().toString());
            txtGenero.setText(pelicula.getGenero());
            txtSinopsis.setText(pelicula.getSinopsis());

            byte[] imagenBytes = getArguments().getByteArray("imagenPelicula");
            if (imagenBytes != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(imagenBytes, 0, imagenBytes.length);
                imgDetallePelicula.setImageBitmap(bitmap);
            } else {
                imgDetallePelicula.setVisibility(View.GONE);
            }

            cargarFragmentoActores(pelicula.getId());
        }

        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(getContext());
            PeliculaDao peliculaDao = db.peliculaDao();
            PeliculaEntity peliculaCargada = peliculaDao.obtenerPelicula(pelicula.getId());

            if (peliculaCargada == null) {
                peliculaDao.insertarPelicula(new PeliculaEntity(
                        pelicula.getId(),
                        pelicula.getNombre(),
                        pelicula.getGenero(),
                        "",
                        pelicula.getSinopsis(),
                        0
                ));
            } else {
                getActivity().runOnUiThread(() -> {
                    txtFechaProximaEmision.setText(peliculaCargada.fechaEmision);
                    ratingPuntuacion.setRating(peliculaCargada.puntuacion);
                });
            }
        }).start();


         SharedPreferences prefs = getActivity().getSharedPreferences("MisPeliculas", Context.MODE_PRIVATE);
        String comentarioGuardado = prefs.getString("comentario_pelicula_" + pelicula.getId(), "");
        txtComentarios.setText(comentarioGuardado);

        btnEditarComentarios.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Editar Comentario");

            final EditText input = new EditText(getContext());
            input.setText(txtComentarios.getText().toString());
            builder.setView(input);

            builder.setPositiveButton("Guardar", (dialog, which) -> {
                String nuevoComentario = input.getText().toString();
                txtComentarios.setText(nuevoComentario);

                 SharedPreferences.Editor editor = prefs.edit();
                editor.putString("comentario_pelicula_" + pelicula.getId(), nuevoComentario);
                editor.apply();
            });

            builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
            builder.show();
        });

         btnIndicarFecha.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SeleccionFechaActivity.class);
            lanzadorFechaHora.launch(intent);
        });

         ratingPuntuacion.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            if (fromUser) {
                new Thread(() -> {
                    AppDatabase db = AppDatabase.getInstance(getContext());
                    PeliculaDao peliculaDao = db.peliculaDao();
                    peliculaDao.actualizarPuntuacion(pelicula.getId(), (int) rating);
                }).start();
            }
        });

        return view;
    }

    private void cargarFragmentoActores(int idPelicula) {
        ListaActores fragmentoActores = ListaActores.newInstance(idPelicula);
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerActores, fragmentoActores)
                .commit();
    }

    public static byte[] convertirBitmapAByteArray(Bitmap bitmap) {
        if (bitmap == null) return null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        return stream.toByteArray();
    }
}
