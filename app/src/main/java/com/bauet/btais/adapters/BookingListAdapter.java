package com.bauet.btais.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bauet.btais.Model.BookingModel;
import com.bauet.btais.R;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class BookingListAdapter extends RecyclerView.Adapter<BookingListAdapter.BookingViewHolder> {

    List<BookingModel> bookingModelList;
    List<DatabaseReference> bookingReferences;

    public BookingListAdapter(List<BookingModel> bookingModelList, List<DatabaseReference> bookingReferences) {
        this.bookingModelList = bookingModelList;
        this.bookingReferences =bookingReferences;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_booking, parent, false);
        return new BookingListAdapter.BookingViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {

        BookingModel booking = bookingModelList.get(position);

        holder.bookingIdValue.setText(String.valueOf(booking.getBookingId()));
        holder.hotelNameValue.setText(booking.getHotel_name());
        holder.roomIdValue.setText(String.valueOf(booking.getRoom_id()));
        holder.costPerNightValue.setText(booking.getRoom_rate());
        holder.discountValue.setText(booking.getDiscount());
        holder.userNameValue.setText(booking.getUserName());
        holder.userPhoneValue.setText(booking.getUserPhone());
        holder.bookingTimeValue.setText(booking.getDate()+" "+booking.getTime());
        holder.confirmBookingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookingReferences.get(position).removeValue();
            }
        });

    }

    @Override
    public int getItemCount() {
        return bookingModelList.size();
    }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView bookingIdValue;
        TextView hotelNameValue;
        TextView roomIdValue;
        TextView costPerNightValue;
        TextView discountValue;
        TextView userNameValue;
        TextView userPhoneValue;
        TextView bookingTimeValue;
        Button confirmBookingButton;
        public BookingViewHolder(@NonNull View itemView)
        {
            super(itemView);

            bookingIdValue = itemView.findViewById(R.id.booking_id_value);
            hotelNameValue = itemView.findViewById(R.id.hotel_name_value);
            roomIdValue = itemView.findViewById(R.id.room_id_value);
            costPerNightValue = itemView.findViewById(R.id.cost_per_night_value);
            discountValue = itemView.findViewById(R.id.discount_value);
            userNameValue = itemView.findViewById(R.id.user_name_value);
            userPhoneValue = itemView.findViewById(R.id.user_phone_value);
            bookingTimeValue = itemView.findViewById(R.id.booking_time_value);
            confirmBookingButton = itemView.findViewById(R.id.confirm_booking_button);

        }
    }
}
