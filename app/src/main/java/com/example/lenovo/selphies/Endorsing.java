package com.example.lenovo.selphies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Endorsing extends AppCompatActivity {

    private String fromClass, postId, userId;

    private DatabaseReference userref, postref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endorsing);

        Intent intent = getIntent();
        fromClass = intent.getStringExtra("from");
        postId = intent.getStringExtra("postId");
        userId = intent.getStringExtra("userId");

        postref = FirebaseDatabase.getInstance().getReference().child("posts").child(postId);
        userref = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

        postref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long postEndorse = (Long) dataSnapshot.child("endorse").getValue();
                postEndorse = postEndorse + 1;
                postref.child("endorse").setValue(postEndorse);
                userref.child("posts").child(postId).child("endorse").setValue(postEndorse);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        userref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long userEndorse = (Long) dataSnapshot.child("endorse").getValue();
                userEndorse = userEndorse + 1;
                userref.child("endorse").setValue(userEndorse);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(fromClass.equals("SeeProfile")){
            Intent finishEndorseIntent = new Intent(Endorsing.this, SeeProfile.class);
            finishEndorseIntent.putExtra("uid", userId);
            startActivity(finishEndorseIntent);
        } else if(fromClass.equals("Home")){
            startActivity(new Intent(Endorsing.this, MainActivity.class));
        }

    }
}
