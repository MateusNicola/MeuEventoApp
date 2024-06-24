package com.example.meueventoapp.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.meueventoapp.entity.Event;

import java.util.List;

@Dao
public interface EventDao {
    @Query("SELECT * FROM event")
    List<Event> getAll();

    @Query("SELECT * FROM event WHERE id = :eventId")
    Event getEventById(int eventId);

    @Insert
    void insertAll(Event... events);

    @Delete
    void delete(Event event);

    @Update
    void update(Event event);
}
