package com.example.btmini.adapter;

import android.content.res.ColorStateList;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btmini.R;
import com.example.btmini.model.Room;
import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;
import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    private List<Room> roomList;
    private OnRoomActionListener listener;
    private DecimalFormat formatter = new DecimalFormat("###,###,### VNĐ");

    public interface OnRoomActionListener {
        void onEdit(Room room, int position);
        void onDelete(Room room, int position);
    }

    public RoomAdapter(List<Room> roomList, OnRoomActionListener listener) {
        this.roomList = roomList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = roomList.get(position);
        int availableColor = holder.itemView.getContext().getColor(R.color.status_available);
        int rentedColor = holder.itemView.getContext().getColor(R.color.status_rented);
        if (room.getImageUri() != null && !room.getImageUri().isEmpty()) {
            holder.imageViewRoom.setImageURI(Uri.parse(room.getImageUri()));
        } else {
            holder.imageViewRoom.setImageResource(R.drawable.ic_default_room);
        }

        holder.textViewRoomName.setText(room.getName());
        holder.textViewRoomPrice.setText("Giá: " + formatter.format(room.getPrice()));
        
        if (room.isRented()) {
            holder.textViewRoomStatus.setText("Đã thuê");
            holder.textViewRoomStatus.setBackgroundTintList(ColorStateList.valueOf(rentedColor));
            holder.textViewTenantInfo.setVisibility(View.VISIBLE);
            holder.textViewTenantInfo.setText("Người thuê: " + room.getTenantName() + " (" + room.getPhoneNumber() + ")");
        } else {
            holder.textViewRoomStatus.setText("Còn trống");
            holder.textViewRoomStatus.setBackgroundTintList(ColorStateList.valueOf(availableColor));
            holder.textViewTenantInfo.setVisibility(View.GONE);
        }

        holder.buttonEdit.setOnClickListener(v -> listener.onEdit(room, position));
        holder.buttonDelete.setOnClickListener(v -> listener.onDelete(room, position));
        
        holder.itemView.setOnClickListener(v -> listener.onEdit(room, position));
        holder.itemView.setOnLongClickListener(v -> {
            listener.onDelete(room, position);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView textViewRoomName, textViewRoomPrice, textViewRoomStatus, textViewTenantInfo;
        MaterialButton buttonEdit, buttonDelete;
        ImageView imageViewRoom;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewRoomName = itemView.findViewById(R.id.textViewRoomName);
            textViewRoomPrice = itemView.findViewById(R.id.textViewRoomPrice);
            textViewRoomStatus = itemView.findViewById(R.id.textViewRoomStatus);
            textViewTenantInfo = itemView.findViewById(R.id.textViewTenantInfo);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
            imageViewRoom = itemView.findViewById(R.id.imageViewRoom);
        }
    }
}
