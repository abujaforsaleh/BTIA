package com.bauet.btais.adapters;

import static com.bauet.btais.constants.fromButton;

import android.content.Context;
import android.content.Intent;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bauet.btais.Model.HotelModel;
import com.bauet.btais.activities.HotelInformationActivity;
import com.bauet.btais.activities.ModifyHotelInfo;
import com.bauet.btais.activities.ModifyPlaceInfo;
import com.bauet.btais.activities.PlaceInformationActivity;
import com.bauet.btais.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;
import io.paperdb.Paper;

public class HotelListAdapter extends FirebaseRecyclerAdapter<HotelModel, HotelListAdapter.LocationViewHolder> {

    Context context;
    public HotelListAdapter(@NonNull FirebaseRecyclerOptions<HotelModel> options, Context context)
    {
        super(options);
        this.context = context;
    }
    @Override
    protected void onBindViewHolder(@NonNull LocationViewHolder holder, int position, @NonNull HotelModel model) {

        Paper.init(context);
        Log.d("hotel info", model.toString());
        String address = model.getDivision()+","+model.getDistrict()+","+model.getUpazila();
        holder.hotelName.setText(model.getHotelName());
        holder.hotelLocation.setText(address);
        holder.roomType.setText(model.getRoomType());
        holder.stayingCost.setText("৳."+model.getCostPerNight());
        Picasso.get().load(model.getImage()).into(holder.imageView);

        holder.itemParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{

                    if(Paper.book().read("user_mode").equals("admin")){

                        //Toast.makeText(context, fromButton, Toast.LENGTH_SHORT).show();
                        if(fromButton.equals("view_content")){
                            Intent intent = new Intent(v.getContext(), HotelInformationActivity.class);
                            intent.putExtra("hotel_data", model);
                            v.getContext().startActivity(intent);
                        }else{
                            Intent intent = new Intent(v.getContext(), ModifyHotelInfo.class);
                            intent.putExtra("hotel_data", model);

                            intent.putExtra("reference", getRef(position).getPath().toString());
                            v.getContext().startActivity(intent);
                        }

                    }else{

                        Intent intent = new Intent(v.getContext(), HotelInformationActivity.class);
                        intent.putExtra("hotel_data", model);
                        v.getContext().startActivity(intent);
                    }
                }catch (Exception e){
                    Log.e("error", e.toString());
                    Intent intent = new Intent(v.getContext(), HotelInformationActivity.class);
                    intent.putExtra("hotel_data", model);
                    v.getContext().startActivity(intent);
                }
            }
        });

    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_hotel, parent, false);
        return new LocationViewHolder(view);
    }

    static class LocationViewHolder extends RecyclerView.ViewHolder {
        TextView hotelName, hotelLocation, stayingCost, roomType;
        ImageView imageView;
        CardView itemParentLayout;
        public LocationViewHolder(@NonNull View itemView)
        {
            super(itemView);
            hotelName = itemView.findViewById(R.id.hotel_name);
            hotelLocation = itemView.findViewById(R.id.hotel_location);
            stayingCost = itemView.findViewById(R.id.stay_cost);
            imageView = itemView.findViewById(R.id.hotel_img);
            itemParentLayout = itemView.findViewById(R.id.itemParentLayout);
            roomType = itemView.findViewById(R.id.room_type);

        }
    }
}

