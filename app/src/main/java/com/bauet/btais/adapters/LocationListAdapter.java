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
import com.bauet.btais.Model.LocationModel;
import com.bauet.btais.activities.ModifyPlaceInfo;
import com.bauet.btais.activities.PlaceInformationActivity;
import com.bauet.btais.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.paperdb.Paper;

public class LocationListAdapter extends RecyclerView.Adapter<LocationListAdapter.LocationViewHolder> {

    Context context;
    List<LocationModel> options;
    public LocationListAdapter(List<LocationModel> options, Context context)
    {
        this.context = context;
        this.options = options;
    }
    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {

        Paper.init(context);
        Log.d("context", fromButton);
        String address = options.get(position).getDivision()+","+options.get(position).getDistrict()+","+options.get(position).getUpazila();
        holder.placeName.setText(options.get(position).getPlaceName());
        holder.placeLocation.setText(address);
        holder.tourCost.setText("৳."+options.get(position).getTravelCost());
        Picasso.get().load(options.get(position).getImage()).into(holder.imageView);
        holder.itemParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{

                    if(Paper.book().read("user_mode").equals("admin")){

                        //Toast.makeText(context, fromButton, Toast.LENGTH_SHORT).show();
                        if(fromButton.equals("view_content")){
                            Intent intent = new Intent(v.getContext(), PlaceInformationActivity.class);
                            intent.putExtra("location_data", options.get(position));
                            v.getContext().startActivity(intent);
                        }else{
                            Intent intent = new Intent(v.getContext(), ModifyPlaceInfo.class);
                            intent.putExtra("location_data", options.get(position));

                            //intent.putExtra("reference", getRef(position).getPath().toString());//todo find way to get reference
                            v.getContext().startActivity(intent);
                        }

                    }else{

                        Intent intent = new Intent(v.getContext(), PlaceInformationActivity.class);
                        intent.putExtra("location_data", options.get(position));
                        v.getContext().startActivity(intent);
                    }
                }catch (Exception e){
                    Log.e("error", e.toString());
                    Intent intent = new Intent(v.getContext(), PlaceInformationActivity.class);
                    intent.putExtra("location_data", options.get(position));
                    v.getContext().startActivity(intent);
                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return options.size();
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_place, parent, false);
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

