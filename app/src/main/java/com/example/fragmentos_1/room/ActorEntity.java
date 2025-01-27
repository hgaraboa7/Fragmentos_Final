package com.example.fragmentos_1.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "actores")
public class ActorEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String nombre;
    public String biografia;
}

