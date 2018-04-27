package com.example.lenovo.selphies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class seeProfile extends AppCompatActivity {

    private TextView usernameText, descriptionText, endorseText;
    private String username, description, imagePath;
    private Long endorse;
    private ImageView profileImage;
    private RecyclerView recycler;

    private DatabaseReference userref, postReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_profile);

        usernameText = (TextView) findViewById(R.id.usernameText);
        descriptionText = (TextView) findViewById(R.id.descriptionText);
        endorseText = (TextView) findViewById(R.id.endorseText);
        profileImage = (ImageView) findViewById(R.id.profileImage);

        Intent intent = getIntent();
        final String userId = intent.getStringExtra("uid");

        userref = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        postReference = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("posts");

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(mLayoutManager);

        userref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                username = dataSnapshot.child(userId).child("username").getValue().toString();
                description = dataSnapshot.child(userId).child("description").getValue().toString();
                endorse = (Long) dataSnapshot.child(userId).child("endorse").getValue();
                imagePath = dataSnapshot.child(userId).child("profile").getValue().toString();
                usernameText.setText(username);
                descriptionText.setText(description);
                endorseText.setText(endorse.toString());
                if(imagePath != ""){
                    Picasso.get().load(imagePath).into(profileImage);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder{
        public  RecyclerViewHolder(View itemView){
            super(itemView);
            View mView = itemView;
        }

        public void setImage(String image){
            ImageView post_image = (ImageView) itemView.findViewById(R.id.postImage);
            Picasso.get().load(image).into(post_image);
        }

        public void setDescription(String description){
            EditText description_text = (EditText) itemView.findViewById(R.id.descriptionText);
            description_text.setText(description);
        }

        public void setEndorse(Long endorse){
            TextView endorse_text = (TextView) itemView.findViewById(R.id.endorseText);
            endorse_text.setText(endorse.toString());
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<ProfileFiller, seeProfile.RecyclerViewHolder> FBRA = new FirebaseRecyclerAdapter<ProfileFiller, seeProfile.RecyclerViewHolder>(

                ProfileFiller.class,
                R.layout.seeprofilerecycler,
                seeProfile.RecyclerViewHolder.class,
                postReference

        )
    }


}
