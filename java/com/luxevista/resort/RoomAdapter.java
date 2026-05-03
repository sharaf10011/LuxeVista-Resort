package com.luxevista.resort;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {
    private List<Room> roomList;
    private OnRoomClickListener onRoomClickListener;

    public interface OnRoomClickListener {
        void onRoomClick(Room room);
    }

    public RoomAdapter(List<Room> roomList, OnRoomClickListener listener) {
        this.roomList = roomList;
        this.onRoomClickListener = listener;
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
        holder.bind(room);
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    class RoomViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivRoomImage;
        private TextView tvRoomName, tvRoomDescription, tvRoomPrice, tvRoomType;
        private Button btnBookRoom;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            ivRoomImage = itemView.findViewById(R.id.ivRoomImage);
            tvRoomName = itemView.findViewById(R.id.tvRoomName);
            tvRoomDescription = itemView.findViewById(R.id.tvRoomDescription);
            tvRoomPrice = itemView.findViewById(R.id.tvRoomPrice);
            tvRoomType = itemView.findViewById(R.id.tvRoomType);
            btnBookRoom = itemView.findViewById(R.id.btnBookRoom);
        }

        public void bind(Room room) {
            tvRoomName.setText(room.getName());
            tvRoomDescription.setText(room.getDescription());
            tvRoomPrice.setText(String.format("$%.0f/night", room.getPrice()));
            tvRoomType.setText(room.getType());
            ivRoomImage.setImageResource(room.getImageResource());

            btnBookRoom.setOnClickListener(v -> {
                if (onRoomClickListener != null) {
                    onRoomClickListener.onRoomClick(room);
                }
            });
        }
    }
}