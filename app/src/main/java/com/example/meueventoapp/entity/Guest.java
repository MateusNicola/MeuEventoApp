package com.example.meueventoapp.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Guest {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public int eventId;

    public boolean isConfirmed;
}
