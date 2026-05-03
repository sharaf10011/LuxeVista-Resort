package com.luxevista.resort;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {
    private List<Booking> bookingList;

    public BookingAdapter(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookingList.get(position);
        holder.bind(booking);
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    class BookingViewHolder extends RecyclerView.ViewHolder {
        private TextView tvBookingType, tvBookingTitle, tvBookingDate, tvBookingStatus, tvBookingPrice;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBookingType = itemView.findViewById(R.id.tvBookingType);
            tvBookingTitle = itemView.findViewById(R.id.tvBookingTitle);
            tvBookingDate = itemView.findViewById(R.id.tvBookingDate);
            tvBookingStatus = itemView.findViewById(R.id.tvBookingStatus);
            tvBookingPrice = itemView.findViewById(R.id.tvBookingPrice);
        }

        public void bind(Booking booking) {
            tvBookingType.setText(booking.getType());
            tvBookingTitle.setText(booking.getTitle());
            tvBookingDate.setText(booking.getDate());
            tvBookingStatus.setText(booking.getStatus());
            tvBookingPrice.setText(String.format("$%.0f", booking.getPrice()));
        }
    }
}