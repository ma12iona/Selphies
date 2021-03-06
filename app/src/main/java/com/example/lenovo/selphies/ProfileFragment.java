package com.example.lenovo.selphies;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import static android.app.Activity.RESULT_OK;

/**
 * This class is the profile setting page of the application
 */
public class ProfileFragment extends Fragment {

    private EditText username, description;
    private ImageView profileImage;
    private Button upload, update, logout;
    private TextView endorseText;

    private String currentUsername, currentDescription, currentProfilePicture, userId;
    private Long currentEndorse;

    private static final int GALLERY_INTENT = 1;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference, postReference, deleteReference;
    private StorageReference storageReference;
    private Uri imageUri;

    private RecyclerView recycler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        final Activity activity = getActivity();

        username = (EditText) view.findViewById(R.id.usernameText);
        description = (EditText) view.findViewById(R.id.descriptionText);
        profileImage = (ImageView) view.findViewById(R.id.profileImage);
        endorseText = (TextView) view.findViewById(R.id.endorseText);
        upload = (Button) view.findViewById(R.id.uploadButton);
        update = (Button) view.findViewById(R.id.updateButton);
        logout = (Button) view.findViewById(R.id.logoutButton);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userId = user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        deleteReference = FirebaseDatabase.getInstance().getReference().child("posts");
        storageReference = FirebaseStorage.getInstance().getReference();

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(activity);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        recycler = (RecyclerView) view.findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(mLayoutManager);
        postReference = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("posts");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentUsername = dataSnapshot.child("username").getValue().toString().trim();
                currentDescription = dataSnapshot.child("description").getValue().toString().trim();
                currentProfilePicture = dataSnapshot.child("profile").getValue().toString();
                currentEndorse = (Long) dataSnapshot.child("endorse").getValue();
                username.setText(currentUsername);
                description.setText(currentDescription);
                endorseText.setText(currentEndorse.toString());
                if(!currentProfilePicture.equals("none")){
                    Picasso.get().load(currentProfilePicture).into(profileImage);
                }
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

                databaseReference.child("username").setValue(newUsername);
                databaseReference.child("description").setValue(newDescription);

                if(imageUri!=null){
                    if(currentProfilePicture != ""){
                        StorageReference photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(currentProfilePicture);
                        photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });

                        StorageReference filepath = storageReference.child("profile").child(imageUri.getLastPathSegment());
                        filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                databaseReference.child("profile").setValue(downloadUrl.toString());
                            }
                        });
                    }
                }
                Toast.makeText(getContext(), "Update Completed.", Toast.LENGTH_SHORT).show();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent logoutIntent = new Intent(getContext(), Login.class);
                logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getActivity().startActivity(logoutIntent);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK);
                gallery.setType("image/*");
                startActivityForResult(gallery, GALLERY_INTENT);
            }
        });

        return view;
    }

    /**
     * This method is mainly for cropping the image
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK && requestCode == GALLERY_INTENT) {
            imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(getActivity());
        }
    }

    /**
     * This method is use for setting the image in the page
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void getImage(int requestCode, int resultCode, Intent data){
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                Uri resultUri = result.getUri();
                profileImage.setImageURI(resultUri);
                imageUri = resultUri;
            }else if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
            }
        }
    }

    /**
     * This class is the recycle view that will show the post in the page
     */
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
            EditText description_text = (EditText) itemView.findViewById(R.id.descriptionText);
            description_text.setText(description);
        }

        public void setEndorse(Long endorse){
            TextView endorse_text = (TextView) itemView.findViewById(R.id.endorseText);
            endorse_text.setText(endorse.toString());
        }

    }

    /**
     * This method is use to retrieve post from the firebase and populate in recycle view
     */
    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<ProfileFiller, ProfileFragment.RecyclerViewHolder> FBRA = new FirebaseRecyclerAdapter<ProfileFiller, ProfileFragment.RecyclerViewHolder>(

                ProfileFiller.class,
                R.layout.profilerecycler,
                ProfileFragment.RecyclerViewHolder.class,
                postReference

        ) {
            @Override
            protected void populateViewHolder(final ProfileFragment.RecyclerViewHolder viewHolder, ProfileFiller model, int position) {
                viewHolder.setImage(model.getImage());
                viewHolder.setDescription(model.getDesc());
                viewHolder.setEndorse(model.getEndorse());

                final String postId = model.getPostId();

                Button delete = viewHolder.itemView.findViewById(R.id.deleteButton);
                Button edit = viewHolder.itemView.findViewById(R.id.editButton);

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        postReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                final Long thisPostEndorse = (Long) dataSnapshot.child(postId).child("endorse").getValue();

                                if(dataSnapshot.child(postId).exists()){
                                    String path = dataSnapshot.child(postId).child("image").getValue().toString();

                                    StorageReference picpath = FirebaseStorage.getInstance().getReferenceFromUrl(path);
                                    picpath.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            databaseReference.child("endorse").setValue(currentEndorse - thisPostEndorse);
                                            postReference.child(postId).removeValue();
                                            deleteReference.child(postId).removeValue();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });

                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText descriptionText = (EditText) viewHolder.itemView.findViewById(R.id.descriptionText);
                        String newDescription = descriptionText.getText().toString();
                        postReference.child(postId).child("desc").setValue(newDescription);
                        deleteReference.child(postId).child("desc").setValue(newDescription);

                    }
                });

            }
        };
        recycler.setAdapter(FBRA);
    }
}
