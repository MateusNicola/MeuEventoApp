package com.example.meueventoapp.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.meueventoapp.entity.Guest;

import java.util.List;

@Dao
public interface GuestDao {
    @Query("SELECT * FROM guest")
    List<Guest> getAll();

    @Insert
    void insertAll(Guest... guests);

    @Delete
    void delete(Guest guest);

    @Update
    void update(Guest guest);

    @Query("DELETE FROM guest WHERE eventId = :eventId")
    void deleteGuestsByEventId(int eventId);  // Novo método para deletar convidados pelo ID do evento
}
