package com.example.fragmentos_1.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface ActorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertarActor(ActorEntity actor);

    @Query("SELECT * FROM actores WHERE id = :id LIMIT 1")
    ActorEntity obtenerActor(int id);
}
