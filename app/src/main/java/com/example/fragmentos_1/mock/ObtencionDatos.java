package com.example.fragmentos_1.mock;

import androidx.annotation.NonNull;
import android.util.Base64;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.fragmentos_1.modelo.entidades.Actor;
import com.example.fragmentos_1.modelo.entidades.Pelicula;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ObtencionDatos {

    private static final String BASE_URL = "http://10.0.2.2:8080/"; // Emulador
    private static final OkHttpClient client = new OkHttpClient();

    public interface PeliculasCallback {
        void onSuccess(ArrayList<Pelicula> peliculas);
        void onFailure(String error);
    }

    public interface ActoresCallback {
        void onSuccess(ArrayList<Actor> actores);
        void onFailure(String error);
    }

    private static ArrayList<Pelicula> listaPeliculas;

    public void obtenerListadoPeliculas(final PeliculasCallback callback) {
        if (listaPeliculas == null) {
            Request request = new Request.Builder()
                    .url(BASE_URL + "listarPeliculas")
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful() && response.body() != null) {
                        String responseData = response.body().string();
                        Gson gson = new Gson();
                        listaPeliculas = gson.fromJson(responseData, new TypeToken<ArrayList<Pelicula>>(){}.getType());

                        for (Pelicula pelicula : listaPeliculas) {
                            pelicula.setImagenBitmap(convertirBase64ABitmap(pelicula.getImagen()));
                            for (Actor actor : pelicula.getActores()) {
                                actor.setImagenBitmap(convertirBase64ABitmap(actor.getFoto()));
                            }
                        }


                        callback.onSuccess(listaPeliculas);
                    } else {
                        callback.onFailure("Error en la respuesta: " + response.code());
                    }
                }

                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    callback.onFailure("Error en la conexión: " + e.getMessage());
                }
            });
        } else {
            callback.onSuccess(listaPeliculas);
        }
    }

    public void obtenerListadoActores(int idPelicula, final ActoresCallback callback) {
        if (listaPeliculas == null) {
            obtenerListadoPeliculas(new PeliculasCallback() {
                @Override
                public void onSuccess(ArrayList<Pelicula> peliculas) {
                    filtrarActores(idPelicula, callback);
                }

                @Override
                public void onFailure(String error) {
                    callback.onFailure(error);
                }
            });
        } else {
            filtrarActores(idPelicula, callback);
        }
    }

    private void filtrarActores(int idPelicula, ActoresCallback callback) {
        ArrayList<Actor> listadoActores = new ArrayList<>();

        if (idPelicula != 0) {
            for (Pelicula pelicula : listaPeliculas) {
                if (pelicula.getId() == idPelicula) {
                    listadoActores.addAll(pelicula.getActores());
                    break;
                }
            }
        } else {
            for (Pelicula pelicula : listaPeliculas) {
                listadoActores.addAll(pelicula.getActores());
            }
        }

        callback.onSuccess(listadoActores);
    }

    public void obtenerTodosLosActores(final ActoresCallback callback) {
        if (listaPeliculas == null) {
            obtenerListadoPeliculas(new PeliculasCallback() {
                @Override
                public void onSuccess(ArrayList<Pelicula> peliculas) {
                    ArrayList<Actor> todosActores = new ArrayList<>();
                    for (Pelicula pelicula : peliculas) {
                        todosActores.addAll(pelicula.getActores());
                    }
                    callback.onSuccess(todosActores);
                }

                @Override
                public void onFailure(String error) {
                    callback.onFailure(error);
                }
            });
        } else {
            ArrayList<Actor> todosActores = new ArrayList<>();
            for (Pelicula pelicula : listaPeliculas) {
                todosActores.addAll(pelicula.getActores());
            }
            callback.onSuccess(todosActores);
        }
    }

    private Bitmap convertirBase64ABitmap(String base64String) {
        if (base64String == null || base64String.isEmpty()) return null;
        byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}
