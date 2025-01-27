package com.example.fragmentos_1.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "peliculas")
public class PeliculaEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String nombre;
    public String genero;
    public String fechaEmision;
    public String sinopsis;
    public int puntuacion;

    public PeliculaEntity(int id, String nombre, String genero, String fechaEmision, String sinopsis, int puntuacion) {
        this.id = id;
        this.nombre = nombre;
        this.genero = genero;
        this.fechaEmision = fechaEmision;
        this.sinopsis = sinopsis;
        this.puntuacion = puntuacion;
    }
}
