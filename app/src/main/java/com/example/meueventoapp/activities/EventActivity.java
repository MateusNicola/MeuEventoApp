package com.example.meueventoapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.meueventoapp.R;
import com.example.meueventoapp.database.AppDatabase;
import com.example.meueventoapp.entity.Event;

public class EventActivity extends AppCompatActivity {
    private EditText editTextEventName;
    private EditText editTextEventDate;
    private EditText editTextEventCapacity;
    private Button buttonSaveEvent;
    private Button buttonManageGuests;
    private Button buttonDeleteEvent;

    private AppDatabase db;
    private int eventId;
    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        editTextEventName = findViewById(R.id.editTextEventName);
        editTextEventDate = findViewById(R.id.editTextEventDate);
        editTextEventCapacity = findViewById(R.id.editTextEventCapacity);
        buttonSaveEvent = findViewById(R.id.buttonSaveEvent);
        buttonManageGuests = findViewById(R.id.buttonManageGuests);
        buttonDeleteEvent = findViewById(R.id.buttonDeleteEvent);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "event-database")
                .allowMainThreadQueries()
                .build();

        eventId = getIntent().getIntExtra("event_id", -1);
        if (eventId != -1) {
            event = db.eventDao().getEventById(eventId);
            if (event != null) {
                editTextEventName.setText(event.name);
                editTextEventDate.setText(event.date);
                editTextEventCapacity.setText(String.valueOf(event.peopleCount));
            }
        }

        buttonSaveEvent.setOnClickListener(v -> saveEvent());
        buttonManageGuests.setOnClickListener(v -> manageGuests());

        buttonDeleteEvent.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Delete Event")
                    .setMessage("Are you sure you want to delete this event?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        deleteEvent();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    private void saveEvent() {
        String name = editTextEventName.getText().toString();
        String date = editTextEventDate.getText().toString();
        int capacity = Integer.parseInt(editTextEventCapacity.getText().toString());

        if (name.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (eventId == -1) {
            event = new Event();
        }

        event.name = name;
        event.date = date;
        event.peopleCount = capacity;

        if (eventId == -1) {
            db.eventDao().insertAll(event);
        } else {
            db.eventDao().update(event);
        }

        Toast.makeText(this, "Event saved", Toast.LENGTH_SHORT).show();
    }

    private void manageGuests() {
        Intent intent = new Intent(this, GuestActivity.class);
        intent.putExtra("event_id", eventId);
        startActivity(intent);
    }

    private void deleteEvent() {
        if (event != null) {
            db.guestDao().deleteGuestsByEventId(eventId);
            db.eventDao().delete(event);
            Toast.makeText(this, "Event deleted", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
