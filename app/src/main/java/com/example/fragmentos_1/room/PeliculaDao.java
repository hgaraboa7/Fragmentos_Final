package com.example.fragmentos_1.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface PeliculaDao {

    @Insert
    void insertarPelicula(PeliculaEntity pelicula);

    @Update
    void actualizarPelicula(PeliculaEntity pelicula);

    @Query("UPDATE peliculas SET fechaEmision = :nuevaFecha WHERE id = :id")
    void actualizarFechaEmision(int id, String nuevaFecha);

    @Query("UPDATE peliculas SET puntuacion = :nuevaPuntuacion WHERE id = :id")
    void actualizarPuntuacion(int id, int nuevaPuntuacion);

    @Query("SELECT * FROM peliculas WHERE id = :id LIMIT 1")
    PeliculaEntity obtenerPelicula(int id);
}
