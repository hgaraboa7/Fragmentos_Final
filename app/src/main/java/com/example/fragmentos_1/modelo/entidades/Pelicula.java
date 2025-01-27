package com.example.fragmentos_1.modelo.entidades;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Pelicula implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String nombre;
    private Date fecha;
    private String sinopsis;
    private String genero;
    private String imagen;
    private transient Bitmap imagenBitmap;
    private List<Actor> actores;

    public Pelicula(int id, String nombre, Date fecha, String sinopsis, String genero, String imagen, List<Actor> actores) {
        this.id = id;
        this.nombre = nombre;
        this.fecha = fecha;
        this.sinopsis = sinopsis;
        this.genero = genero;
        this.imagen = imagen;
        this.actores = actores;
        convertirBase64ABitmap();
    }

    public Pelicula(int id, String nombre, Date fecha, String sinopsis, String genero, String imagen) {
        this.id = id;
        this.nombre = nombre;
        this.fecha = fecha;
        this.sinopsis = sinopsis;
        this.genero = genero;
        this.imagen = imagen;
        convertirBase64ABitmap();
    }

    public Pelicula(String nombre, Date fecha, String sinopsis, String genero, String imagen) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.sinopsis = sinopsis;
        this.genero = genero;
        this.imagen = imagen;
        convertirBase64ABitmap();
    }

    public Pelicula() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
        convertirBase64ABitmap();
    }

    public Bitmap getImagenBitmap() {
        return imagenBitmap;
    }

    public void setImagenBitmap(Bitmap imagenBitmap) {
        this.imagenBitmap = imagenBitmap;
    }

    public List<Actor> getActores() {
        return actores;
    }

    public void setActores(List<Actor> actores) {
        this.actores = actores;
    }

    private void convertirBase64ABitmap() {
        if (imagen != null && !imagen.isEmpty()) {
            byte[] decodedBytes = Base64.decode(imagen, Base64.DEFAULT);
            this.imagenBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        }
    }
}
