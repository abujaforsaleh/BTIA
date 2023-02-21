package com.bauet.btais.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bauet.btais.Model.LocationModel;
import com.bauet.btais.PlaceInformationActivity;
import com.bauet.btais.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;


public class LocationListAdapter extends FirebaseRecyclerAdapter<LocationModel, LocationListAdapter.LocationViewHolder> {

    public LocationListAdapter(@NonNull FirebaseRecyclerOptions<LocationModel> options)
    {

        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull LocationViewHolder holder, int position, @NonNull LocationModel model) {

        String address = model.getDivision()+","+model.getDistrict()+","+model.getUpazila();
        holder.placeName.setText(model.getPlaceName());
        holder.placeLocation.setText(address);
        holder.tourCost.setText("৳."+model.getTravelCost());
        Picasso.get().load(model.getImage()).into(holder.imageView);
        holder.itemParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PlaceInformationActivity.class);
                v.getContext().startActivity(intent);
            }
        });


    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_list_item, parent, false);
        return new LocationViewHolder(view);
    }


    static class LocationViewHolder extends RecyclerView.ViewHolder {
        TextView placeName, placeLocation, tourCost;
        ImageView imageView;
        CardView itemParentLayout;
        public LocationViewHolder(@NonNull View itemView)
        {
            super(itemView);

            placeName = itemView.findViewById(R.id.place_name);
            placeLocation = itemView.findViewById(R.id.place_location);
            tourCost = itemView.findViewById(R.id.tour_cost);
            imageView = itemView.findViewById(R.id.img_id);
            itemParentLayout = itemView.findViewById(R.id.itemParentLayout);

        }
    }
}

