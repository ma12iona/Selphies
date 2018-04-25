package com.example.lenovo.selphies;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

public class ProfileFragment extends Fragment {

    private EditText username, description;
    private ImageView profileImage;
    private Button upload, update, logout;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        final Activity activity = getActivity();

        username = (EditText) view.findViewById(R.id.usernameText);
        description = (EditText) view.findViewById(R.id.descriptionText);
        profileImage = (ImageView) view.findViewById(R.id.profileImage);
        upload = (Button) view.findViewById(R.id.uploadButton);
        update = (Button) view.findViewById(R.id.updateButton);
        logout = (Button) view.findViewById(R.id.logoutButton);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String currentUsername = dataSnapshot.child("username").getValue().toString().trim();
                String currentDescription = dataSnapshot.child("description").getValue().toString().trim();
                String currentProfilePicture = dataSnapshot.child("profile").getValue().toString();
                username.setText(currentUsername);
                description.setText(currentDescription);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUsername = username.getText().toString().trim();
                String newDescription = description.getText().toString().trim();


            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                getActivity().startActivity(new Intent(getContext(), Login.class));
            }
        });




        return view;
    }
}
