package com.example.exploreit;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SinglePlaceActivity extends AppCompatActivity {
    private ImageView singelImage;
    private TextView singleTitle, singleDesc, singleAuthor, singleDate;
    String post_key = null;
    private DatabaseReference mDatabase;
    private Button deleteBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_place);
        singelImage = (ImageView) findViewById(R.id.singleImageview);
        singleTitle = (TextView) findViewById(R.id.singleTitle);
        singleAuthor = (TextView) findViewById(R.id.singleAuthor);
        singleDesc = (TextView) findViewById(R.id.singleDesc);
        singleDate = (TextView) findViewById(R.id.singleDate);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("exploreIt");
        post_key = getIntent().getExtras().getString("PostID");
        deleteBtn = (Button) findViewById(R.id.deleteBtn);
        mAuth = FirebaseAuth.getInstance();
        deleteBtn.setVisibility(View.INVISIBLE);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SinglePlaceActivity.this);
                builder.setCancelable(true);
                builder.setMessage("Do you really want to delete this place?");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mDatabase.child(post_key).removeValue();
                                Toast.makeText(getApplicationContext(), "Place was successfully deleted", Toast.LENGTH_SHORT).show();
                                Intent mainintent = new Intent(SinglePlaceActivity.this, MainActivity.class);
                                startActivity(mainintent);
                            }
                        });
                builder.setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        mDatabase.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_title = (String) dataSnapshot.child("title").getValue();
                String post_autor = (String) dataSnapshot.child("username").getValue();
                String post_desc = (String) dataSnapshot.child("desc").getValue();
                String post_image = (String) dataSnapshot.child("imageUrl").getValue();
                String post_date = (String) dataSnapshot.child("creationDate").getValue();
                String post_uid = (String) dataSnapshot.child("uid").getValue();

                singleTitle.setText(post_title);
                singleDesc.setText(post_desc);
                singleAuthor.setText("By: " + post_autor);
                singleDate.setText("On: " + post_date);
                Picasso.with(SinglePlaceActivity.this).load(post_image).into(singelImage);
                if (mAuth.getCurrentUser().getUid().equals(post_uid)) {
                    deleteBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
