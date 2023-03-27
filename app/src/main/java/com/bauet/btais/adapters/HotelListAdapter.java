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

import java.util.List;

import io.paperdb.Paper;

public class HotelListAdapter extends RecyclerView.Adapter<HotelListAdapter.LocationViewHolder> {

    Context context;
    List<HotelModel> options;
    List<String> paths;
    public HotelListAdapter(List<HotelModel> options, List<String> paths, Context context)
    {
        this.options = options;
        this.context = context;
        this.paths = paths;
    }
    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {

        Paper.init(context);
        //Log.d("hotel info", options.get(position).toString());
        String address = options.get(position).getDivision()+","+options.get(position).getDistrict()+","+options.get(position).getUpazila();
        holder.hotelName.setText(options.get(position).getHotelName());
        holder.hotelLocation.setText(address);
        holder.roomType.setText(options.get(position).getRoomType());
        holder.stayingCost.setText("৳."+options.get(position).getCostPerNight());
        Picasso.get().load(options.get(position).getImage()).into(holder.imageView);

        holder.itemParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{

                    if(Paper.book().read("user_mode").equals("admin")){

                        //Toast.makeText(context, fromButton, Toast.LENGTH_SHORT).show();
                        if(fromButton.equals("view_content")){
                            Intent intent = new Intent(v.getContext(), HotelInformationActivity.class);
                            intent.putExtra("hotel_data", options.get(position));
                            v.getContext().startActivity(intent);
                        }else{
                            Intent intent = new Intent(v.getContext(), ModifyHotelInfo.class);
                            intent.putExtra("hotel_data", options.get(position));

                            intent.putExtra("reference", paths.get(position));//todo find way to get reference
                            v.getContext().startActivity(intent);
                        }

                    }else{

                        Intent intent = new Intent(v.getContext(), HotelInformationActivity.class);
                        intent.putExtra("hotel_data", options.get(position));
                        v.getContext().startActivity(intent);
                    }
                }catch (Exception e){
                    Log.e("error", e.toString());
                    Intent intent = new Intent(v.getContext(), HotelInformationActivity.class);
                    intent.putExtra("hotel_data", options.get(position));
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

    @Override
    public int getItemCount() {
        return options.size();
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

