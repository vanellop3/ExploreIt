package com.example.exploreit;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class FirebaseHelper {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferencePlaces;
    private List<Place> places = new ArrayList<>();

    public interface DataStatus {
        void DataIsLoaded(List<Place> places, List<String> keys);
    }


    public void showAll(final DataStatus dataStatus) {
        mReferencePlaces.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                places.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : snapshot.getChildren()) {
                    if (keyNode.child("uid") != null) {
                        keys.add(keyNode.getKey());
                        Place place = keyNode.getValue(Place.class);
                        places.add(place);
                    }
                }
                dataStatus.DataIsLoaded(places, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public FirebaseHelper() {
        mDatabase = FirebaseDatabase.getInstance();
        mReferencePlaces = mDatabase.getReference().child("/exploreIt");
    }

    public void readPlaces(final DataStatus dataStatus) {
        mReferencePlaces.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                places.clear();
                String loggedUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : snapshot.getChildren()) {
                    if (keyNode.child("uid").getValue() != null) {
                        String creator = keyNode.child("uid").getValue().toString();
                        if (loggedUser.equals(creator)) {
                            keys.add(keyNode.getKey());
                            Place place = keyNode.getValue(Place.class);
                            places.add(place);
                        }
                    }
                }
                dataStatus.DataIsLoaded(places, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            Log.i("Database error appear", String.valueOf(error));
            }
        });
    }
}
