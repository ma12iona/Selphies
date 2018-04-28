package com.example.lenovo.selphies;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class LeaderboardFragment extends Fragment {

    private RecyclerView recycler;

    private DatabaseReference userref;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        final Activity activity = getActivity();

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(activity);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        //https://freakycoder.com/android-notes-46-how-to-reverse-recyclerview-by-adding-items-f32db1e36c51

        recycler = (RecyclerView) view.findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);

        recycler.setLayoutManager(mLayoutManager);

        userref = FirebaseDatabase.getInstance().getReference().child("users");


        return view;
    }


    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            View mView = itemView;
        }

        public void setUsername(String username) {
            TextView post_username = (TextView) itemView.findViewById(R.id.usernameText);
            post_username.setText(username);
        }

        public void setDescription(String description) {
            TextView post_description = (TextView) itemView.findViewById(R.id.descriptionText);
            post_description.setText(description);
        }

        public void setProfile(String profile) {
            ImageView user_profile = (ImageView) itemView.findViewById(R.id.userProfile);
            Picasso.get().load(profile).into(user_profile);
        }

        public void setEndorse(Long endorse) {
            TextView post_endorse = (TextView) itemView.findViewById(R.id.endorseText);
            post_endorse.setText(endorse.toString());
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<LeaderFiller, LeaderboardFragment.RecyclerViewHolder> FBRA = new FirebaseRecyclerAdapter<LeaderFiller, LeaderboardFragment.RecyclerViewHolder>(

                LeaderFiller.class,
                R.layout.leaderrecycler,
                LeaderboardFragment.RecyclerViewHolder.class,
                userref.orderByChild("endorse")

        ) {
            @Override
            protected void populateViewHolder(LeaderboardFragment.RecyclerViewHolder viewHolder, LeaderFiller model, int position) {
                viewHolder.setUsername(model.getUsername());
                viewHolder.setDescription(model.getDescription());
                viewHolder.setProfile(model.getProfile());
                viewHolder.setEndorse(model.getEndorse());

                Button viewProfile = viewHolder.itemView.findViewById(R.id.profileButton);

                final String userId = model.getUserId();
                viewProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent seeProfileIntent = new Intent(getContext(), SeeProfile.class);
                        seeProfileIntent.putExtra("uid", userId);
                        startActivity(seeProfileIntent);
                    }
                });
            }
        };
        recycler.setAdapter(FBRA);
    }
}
