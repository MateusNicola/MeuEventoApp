package com.example.meueventoapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.meueventoapp.R;
import com.example.meueventoapp.adapters.GuestAdapter;
import com.example.meueventoapp.database.AppDatabase;
import com.example.meueventoapp.entity.Guest;

import java.util.List;
import java.util.stream.Collectors;

public class GuestActivity extends AppCompatActivity implements GuestAdapter.OnGuestActionListener {
    private GuestAdapter guestAdapter;
    private AppDatabase db;
    private List<Guest> guestList;
    private int eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);

        eventId = getIntent().getIntExtra("event_id", -1);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "guest-database")
                .allowMainThreadQueries()
                .build();

        guestList = db.guestDao().getAll().stream().filter(guest -> guest.eventId == eventId).collect(Collectors.toList());
        guestAdapter = new GuestAdapter(guestList, this);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(guestAdapter);

        findViewById(R.id.buttonAddGuest).setOnClickListener(v -> showAddGuestDialog());
    }

    private void showAddGuestDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_add_guest, null);
        EditText editTextName = view.findViewById(R.id.editTextName);

        builder.setView(view)
                .setTitle("Add Guest")
                .setPositiveButton("Add", (dialog, which) -> {
                    String name = editTextName.getText().toString();
                    if (!name.isEmpty()) {
                        Guest guest = new Guest();
                        guest.name = name;
                        guest.eventId = eventId;
                        guest.isConfirmed = false;
                        db.guestDao().insertAll(guest);
                        guestList.add(guest);
                        guestAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(GuestActivity.this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onGuestConfirmed(Guest guest) {
        guest.isConfirmed = true;
        db.guestDao().update(guest);
        guestAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGuestDeclined(Guest guest) {
        guest.isConfirmed = false;
        db.guestDao().update(guest);
        guestAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGuestDelete(Guest guest) {
        db.guestDao().delete(guest);
        guestList.remove(guest);
        guestAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Guest deleted", Toast.LENGTH_SHORT).show();
    }
}
