package com.example.meueventoapp.adapters;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meueventoapp.R;
import com.example.meueventoapp.entity.Guest;

import java.util.List;

public class GuestAdapter extends RecyclerView.Adapter<GuestAdapter.GuestViewHolder> {
    private List<Guest> guestList;
    private OnGuestActionListener listener;

    public interface OnGuestActionListener {
        void onGuestConfirmed(Guest guest);
        void onGuestDeclined(Guest guest);
        void onGuestDelete(Guest guest);  // Novo método para exclusão
    }

    public GuestAdapter(List<Guest> guestList, OnGuestActionListener listener) {
        this.guestList = guestList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public GuestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_guest, parent, false);
        return new GuestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GuestViewHolder holder, int position) {
        Guest guest = guestList.get(position);
        holder.textViewGuestName.setText(guest.name);
        holder.buttonConfirm.setBackgroundColor(guest.isConfirmed ? 0xFF00FF00 : 0xFFFFFFFF); // Verde se confirmado
        holder.buttonDecline.setBackgroundColor(!guest.isConfirmed ? 0xFFFF0000 : 0xFFFFFFFF); // Vermelho se não confirmado

        holder.buttonConfirm.setOnClickListener(v -> {
            listener.onGuestConfirmed(guest);
            notifyDataSetChanged();
        });

        holder.buttonDecline.setOnClickListener(v -> {
            listener.onGuestDeclined(guest);
            notifyDataSetChanged();
        });

        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(holder.itemView.getContext())
                    .setTitle("Delete Guest")
                    .setMessage("Are you sure you want to delete this guest?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        listener.onGuestDelete(guest);
                        notifyDataSetChanged();
                    })
                    .setNegativeButton("No", null)
                    .show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return guestList.size();
    }

    static class GuestViewHolder extends RecyclerView.ViewHolder {
        TextView textViewGuestName;
        Button buttonConfirm;
        Button buttonDecline;

        public GuestViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewGuestName = itemView.findViewById(R.id.textViewGuestName);
            buttonConfirm = itemView.findViewById(R.id.buttonConfirm);
            buttonDecline = itemView.findViewById(R.id.buttonDecline);
        }
    }
}
