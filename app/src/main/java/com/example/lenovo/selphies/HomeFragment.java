package com.example.lenovo.selphies;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class HomeFragment extends Fragment {

    private RecyclerView recycler;
    private DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        final Activity activity = getActivity();

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(activity);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        //https://freakycoder.com/android-notes-46-how-to-reverse-recyclerview-by-adding-items-f32db1e36c51

        recycler = (RecyclerView) view.findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);
        //recycler.setLayoutManager(new LinearLayoutManager(activity));
        recycler.setLayoutManager(mLayoutManager);
        //databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("posts");

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
            }
        };
        recycler.setAdapter(FBRA);
    }
}
