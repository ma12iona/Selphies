package com.example.lenovo.selphies;


import android.app.Activity;
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
import com.google.firebase.database.FirebaseDatabase;

public class ProfileFragment extends Fragment {

    private EditText username, description;
    private ImageView profileImage;
    private Button upload, update, logout;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

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
        //username.setText();


        return view;
    }
}
