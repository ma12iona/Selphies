package com.example.lenovo.selphies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SeeProfile extends AppCompatActivity {

    private TextView usernameText, descriptionText, endorseText;
    private String username, description, imagePath, userId;
    private Long endorse;
    private ImageView profileImage;
    private RecyclerView recycler;

    private DatabaseReference userref, postref, postReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_profile);

        usernameText = (TextView) findViewById(R.id.usernameText);
        descriptionText = (TextView) findViewById(R.id.descriptionText);
        endorseText = (TextView) findViewById(R.id.endorseText);
        profileImage = (ImageView) findViewById(R.id.profileImage);

        Intent intent = getIntent();
        userId = intent.getStringExtra("uid");
        Log.v("user",userId);

        userref = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        postref = FirebaseDatabase.getInstance().getReference().child("posts");
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

                username = dataSnapshot.child("username").getValue().toString();
                description = dataSnapshot.child("description").getValue().toString();
                endorse = (Long) dataSnapshot.child("endorse").getValue();
                imagePath = dataSnapshot.child("profile").getValue().toString();
                usernameText.setText(username);
                descriptionText.setText(description);
                endorseText.setText(endorse.toString());
                if(!imagePath.equals("none")){
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
            ImageView post_image = (ImageView) itemView.findViewById(R.id.userProfile);
            Picasso.get().load(image).into(post_image);
        }

        public void setDescription(String description){
            TextView description_text = (TextView) itemView.findViewById(R.id.descriptionText);
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
        Log.v("here","aaaaaaaaaaa");
        FirebaseRecyclerAdapter<ProfileFiller, SeeProfile.RecyclerViewHolder> FBRA = new FirebaseRecyclerAdapter<ProfileFiller, SeeProfile.RecyclerViewHolder>(

                ProfileFiller.class,
                R.layout.seeprofilerecycler,
                SeeProfile.RecyclerViewHolder.class,
                postReference

        ){
            @Override
            protected void populateViewHolder(SeeProfile.RecyclerViewHolder viewHolder, ProfileFiller model, int position) {
                viewHolder.setImage(model.getImage());
                viewHolder.setDescription(model.getDesc());
                viewHolder.setEndorse(model.getEndorse());

                final Long postEndorse = model.getEndorse();
                final String postId = model.getPostId();
                Log.v("xxx",postId);

                final Button endorseButton = viewHolder.itemView.findViewById(R.id.endorseButton);

                userref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final Long userEndorse = (Long) dataSnapshot.child("endorse").getValue();
                        Log.v("endorse2", userEndorse.toString());

                        endorseButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                postref.child(postId).child("endorse").setValue(postEndorse + 1);
                                userref.child("posts").child(postId).child("endorse").setValue(postEndorse + 1);
                                userref.child("endorse").setValue(userEndorse + 1);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

/*
                endorseButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent endorseIntent = new Intent(SeeProfile.this, Endorsing.class);
                        endorseIntent.putExtra("postId", postId);
                        endorseIntent.putExtra("userId", userId);
                        endorseIntent.putExtra("from","SeeProfile");
                        startActivity(endorseIntent);

                    }
                });
*/

            }


        };
        recycler.setAdapter(FBRA);
    }

    @Override
    public void onBackPressed(){
        startActivity(new Intent(SeeProfile.this, MainActivity.class));
    }


}
