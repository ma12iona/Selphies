package com.example.lenovo.selphies;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class HomeFragment extends Fragment {

    private RecyclerView recycler;
    private DatabaseReference databaseReference, userRef;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private Long currentPostEndorse, currentUserEndorse;
    private Boolean repeatStopper;
    private Long endorseStopper;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        final Activity activity = getActivity();

        repeatStopper = Boolean.TRUE;

        endorseStopper = Long.valueOf(-1);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(activity);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        //https://freakycoder.com/android-notes-46-how-to-reverse-recyclerview-by-adding-items-f32db1e36c51

        recycler = (RecyclerView) view.findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);

        recycler.setLayoutManager(mLayoutManager);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("posts");
        userRef = FirebaseDatabase.getInstance().getReference().child("users");

        recycler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder{
        public  RecyclerViewHolder(View itemView){
            super(itemView);
            View mView = itemView;
        }

        public void setUsername(String username){
            TextView post_username = (TextView) itemView.findViewById(R.id.usernameText);
            post_username.setText(username);
        }

        public void setDescription(String description){
            TextView post_desc = (TextView) itemView.findViewById(R.id.descText);
            post_desc.setText(description);
        }

        public void setImage(String image){
            ImageView post_image = (ImageView) itemView.findViewById(R.id.postImage);
            Picasso.get().load(image).into(post_image);
        }

        public void setEndorse(Long endorse){
            TextView post_endorse = (TextView) itemView.findViewById(R.id.endorseText);
            post_endorse.setText(endorse.toString());
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter <HomeFiller, RecyclerViewHolder> FBRA = new FirebaseRecyclerAdapter<HomeFiller, RecyclerViewHolder>(

                HomeFiller.class,
                R.layout.recyclerpage,
                RecyclerViewHolder.class,
                databaseReference

        ) {
            @Override
            protected void populateViewHolder(RecyclerViewHolder viewHolder, HomeFiller model, int position) {
                viewHolder.setUsername(model.getUsername());
                viewHolder.setDescription(model.getDesc());
                viewHolder.setImage(model.getImage());
                viewHolder.setEndorse(model.getEndorse());

                final Button endorse = viewHolder.itemView.findViewById(R.id.endorseButton);
                TextView usernameText = viewHolder.itemView.findViewById(R.id.usernameText);

                final String postId = model.getPostId();
                final String userId = model.getUserId();

                endorse.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Long postEndorse = (Long) dataSnapshot.child(postId).child("endorse").getValue();
                                databaseReference.child(postId).child("endorse").setValue(postEndorse + 1);
                                userRef.child(userId).child("posts").child(postId).child("endorse").setValue(postEndorse + 1);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        userRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Long userEndorse = (Long) dataSnapshot.child(userId).child("endorse").getValue();
                                userRef.child(userId).child("endorse").setValue(userEndorse + 1);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        Toast.makeText(getContext(), "Endorsed.", Toast.LENGTH_SHORT).show();

                    }
                });

                usernameText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent seeProfileIntent = new Intent(getContext(), seeProfile.class);
                        seeProfileIntent.putExtra("uid", userId);
                        startActivity(seeProfileIntent);
                    }
                });

            }
        };
        recycler.setAdapter(FBRA);
    }


}
