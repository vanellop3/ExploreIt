package com.example.exploreit;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecycleView;
    private FloatingActionButton btnAddPlace;
    private FirebaseAuth firebaseAuth;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                Toast.makeText(MainActivity.this, "Signed in with another account", Toast.LENGTH_LONG).show();
                Intent k = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(k);

            case R.id.action_showall:
                new FirebaseHelper().showAll(new FirebaseHelper.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<Place> places, List<String> keys) {
                        new RecyclerVIew_Config().setConfig(mRecycleView, MainActivity.this, places, keys);
                    }
                });

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnAddPlace = (FloatingActionButton) findViewById(R.id.addPlaces);

        mRecycleView = (RecyclerView) findViewById(R.id.places);
        new FirebaseHelper().readPlaces(new FirebaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Place> places, List<String> keys) {
                new RecyclerVIew_Config().setConfig(mRecycleView, MainActivity.this, places, keys);
            }
        });

        btnAddPlace.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddPlaceActivity.class);
                startActivity(intent);
            }
        });

    }
}