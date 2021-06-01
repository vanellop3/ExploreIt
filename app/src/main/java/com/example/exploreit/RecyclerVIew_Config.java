package com.example.exploreit;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerVIew_Config {
    public Context mContext;
    private PlacesAdapter placesAdapter;

    public void setConfig(RecyclerView recyclerView, Context context, List<Place> places, List<String> keys) {
        mContext = context;
        placesAdapter = new PlacesAdapter(places, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(placesAdapter);
    }

    class PlaceItemView extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView description;
        private ImageView image;

        private String key;

        public PlaceItemView(ViewGroup parent) {
            super(LayoutInflater.from(mContext).
                    inflate(R.layout.card_items, parent, false));

            title = (TextView) itemView.findViewById(R.id.post_title_txtview);
            description = (TextView) itemView.findViewById(R.id.post_desc_txtview);
            image = (ImageView) itemView.findViewById(R.id.post_image);

        }

        public void bind(Place place, String key) {
            title.setText(place.getTitle());
            description.setText(place.getDesc());
            String imageUri = place.getImageUrl();
            ImageView ivBasicImage = (ImageView) itemView.findViewById(R.id.post_image);
            Picasso.with(mContext).load(imageUri).into(ivBasicImage);
            this.key = key;
        }

    }

    class PlacesAdapter extends RecyclerView.Adapter<PlaceItemView> {
        private List<Place> placeList;
        private List<String> keys;

        public PlacesAdapter(List<Place> placeList, List<String> keys) {
            this.placeList = placeList;
            this.keys = keys;
        }

        @NonNull
        @Override
        public PlaceItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new PlaceItemView(parent);
        }

        @Override
        public void onBindViewHolder(PlaceItemView holder, int position) {
            holder.bind(placeList.get(position), keys.get(position));
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // perform ur click here
                    Intent singleActivity = new Intent(mContext, SinglePlaceActivity.class);
                    singleActivity.putExtra("PostID", holder.key);
                    Log.i("postIdd", String.valueOf(holder.key));
                    mContext.startActivity(singleActivity);
                }
            });
        }

        @Override
        public int getItemCount() {
            return placeList.size();
        }
    }

}
