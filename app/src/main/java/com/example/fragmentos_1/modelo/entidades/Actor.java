package com.example.fragmentos_1.modelo.entidades;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import java.io.Serializable;
import java.util.Date;

public class Actor implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String nombre;
    private Date fechaNacimiento;
    private String foto;
    private transient Bitmap imagenBitmap;

    public Actor(int id, String nombre, Date fechaNacimiento, String foto) {
        this.id = id;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.foto = foto;
        convertirBase64ABitmap();
    }

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

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
        convertirBase64ABitmap();
    }

    public Bitmap getImagenBitmap() {
        return imagenBitmap;
    }

    public void setImagenBitmap(Bitmap imagenBitmap) {
        this.imagenBitmap = imagenBitmap;
    }

    private void convertirBase64ABitmap() {
        if (foto != null && !foto.isEmpty()) {
            byte[] decodedBytes = Base64.decode(foto, Base64.DEFAULT);
            this.imagenBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        }
    }
}
