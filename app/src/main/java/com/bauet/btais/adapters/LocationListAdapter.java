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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bauet.btais.Model.LocationModel;
import com.bauet.btais.ModifyPlaceInfo;
import com.bauet.btais.PlaceInformationActivity;
import com.bauet.btais.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;
import io.paperdb.Paper;

public class LocationListAdapter extends FirebaseRecyclerAdapter<LocationModel, LocationListAdapter.LocationViewHolder> {

    Context context;
    public LocationListAdapter(@NonNull FirebaseRecyclerOptions<LocationModel> options, Context context)
    {
        super(options);
        this.context = context;
    }
    @Override
    protected void onBindViewHolder(@NonNull LocationViewHolder holder, int position, @NonNull LocationModel model) {

        Paper.init(context);
        Log.d("context", fromButton);
        String address = model.getDivision()+","+model.getDistrict()+","+model.getUpazila();
        holder.placeName.setText(model.getPlaceName());
        holder.placeLocation.setText(address);
        holder.tourCost.setText("৳."+model.getTravelCost());
        Picasso.get().load(model.getImage()).into(holder.imageView);
        holder.itemParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{

                    if(Paper.book().read("user_mode").equals("admin")){

                        //Toast.makeText(context, fromButton, Toast.LENGTH_SHORT).show();
                        if(fromButton.equals("view_content")){
                            Intent intent = new Intent(v.getContext(), PlaceInformationActivity.class);
                            intent.putExtra("location_data", model);
                            v.getContext().startActivity(intent);
                        }else{
                            Intent intent = new Intent(v.getContext(), ModifyPlaceInfo.class);
                            intent.putExtra("location_data", model);

                            intent.putExtra("reference", getRef(position).getPath().toString());
                            v.getContext().startActivity(intent);
                        }

                    }else{

                        Intent intent = new Intent(v.getContext(), PlaceInformationActivity.class);
                        intent.putExtra("location_data", model);
                        v.getContext().startActivity(intent);
                    }
                }catch (Exception e){
                    Log.e("error", e.toString());
                    Intent intent = new Intent(v.getContext(), PlaceInformationActivity.class);
                    intent.putExtra("location_data", model);
                    v.getContext().startActivity(intent);
                }


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

