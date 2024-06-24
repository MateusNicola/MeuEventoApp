package com.example.meueventoapp.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.meueventoapp.dao.EventDao;
import com.example.meueventoapp.dao.GuestDao;
import com.example.meueventoapp.entity.Event;
import com.example.meueventoapp.entity.Guest;

@Database(entities = {Guest.class, Event.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract GuestDao guestDao();
    public abstract EventDao eventDao();
}
