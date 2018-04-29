package com.example.lenovo.selphies;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.test.mock.MockPackageManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

/**
 * This class is use when the user want to post
 */
public class PostFragment extends Fragment {

    private Button post, gallery, camera;
    private ImageView image;
    private Uri imageUri;
    private File file;
    private EditText description;
    private String userId, location;
    private FirebaseUser user;
    private String username;
    private GPSTracker gps;
    private double latitude, longitude;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference postref, userref;
    private StorageReference storageReference;

    private static final int GALLERY_INTENT = 1;
    private static final int CAMERA_INTENT = 2;
    private static final int WRITE_EXTERNAL_REQUEST_CODE = 3;
    private static final int REQUEST_CODE_PERMISSION = 4;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_post, container, false);
        final Activity activity = getActivity();

        try{
            if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISSION);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        post = (Button) view.findViewById(R.id.postButton);
        gallery = (Button) view.findViewById(R.id.galleryButton);
        camera = (Button) view.findViewById(R.id.cameraButton);
        image = (ImageView) view.findViewById(R.id.imageView);
        description = (EditText) view.findViewById(R.id.descriptionText);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        user = mAuth.getCurrentUser();
        userId = user.getUid();
        postref = database.getReference("posts");
        userref = database.getReference("users");

        storageReference = FirebaseStorage.getInstance().getReference();

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_INTENT);
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    takePhoto();
                else
                    ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_REQUEST_CODE);

            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gps = new GPSTracker(view.getContext());
                if(gps.canGetLocation){
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                }else{
                    gps.showSettingAlert();
                }

                userref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        username = dataSnapshot.child(userId).child("username").getValue().toString();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                final String desc = description.getText().toString().trim();

                if(!TextUtils.isEmpty(desc) && imageUri != null){
                    StorageReference filePath = storageReference.child("posts").child(imageUri.getLastPathSegment());
                    filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            DatabaseReference newPost = postref.push();
                            newPost.child("desc").setValue(desc);
                            newPost.child("image").setValue(downloadUrl.toString());
                            newPost.child("endorse").setValue(0);
                            newPost.child("username").setValue(username);
                            newPost.child("userId").setValue(userId);
                            newPost.child("postId").setValue(newPost.getKey());
                            newPost.child("latitude").setValue(latitude);
                            newPost.child("longitude").setValue(longitude);

                            DatabaseReference userPost = userref.child(userId).child("posts").child(newPost.getKey());
                            userPost.child("desc").setValue(desc);
                            userPost.child("image").setValue(downloadUrl.toString());
                            userPost.child("endorse").setValue(0);
                            userPost.child("username").setValue(username);
                            userPost.child("userId").setValue(userId);
                            userPost.child("postId").setValue(newPost.getKey());
                            userPost.child("latitude").setValue(latitude);
                            userPost.child("longitude").setValue(longitude);
                        }
                    });
                    Toast.makeText(getContext(), "Post Completed.", Toast.LENGTH_SHORT).show();
                    getActivity().startActivity(new Intent(getContext(), MainActivity.class));
                } else{
                    Toast.makeText(getContext(), "Please choose the photo and add description.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    /**
     * This method is use to access the camera
     */
    public void takePhoto() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File cameraFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");
        try {
            file = File.createTempFile(String.valueOf(System.currentTimeMillis()), ".jpg", cameraFolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageUri = FileProvider.getUriForFile(getActivity(),
                "com.example.lenovo.selphies.provider",
                file);

        cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);
        cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(cameraIntent, CAMERA_INTENT);
    }

    /**
     * This method is use to access the external files
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_EXTERNAL_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            takePhoto();
        }
    }

    /**
     * This method is use to take action for some intent
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == GALLERY_INTENT) {
            imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(getActivity());
        }

        if (resultCode == RESULT_OK && requestCode == CAMERA_INTENT) {

            File file = new File(imageUri.getPath());
            try {
                InputStream ims = new FileInputStream(file);
                image.setImageBitmap(BitmapFactory.decodeStream(ims));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            // ScanFile so it will be appeared on Gallery
            MediaScannerConnection.scanFile(getContext(),
                    new String[]{imageUri.getPath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });
            //Reference: https://inthecheesefactory.com/blog/how-to-share-access-to-file-with-fileprovider-on-android-nougat/en
        }


    }

    /**
     * This method is use to set the image
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void getImage(int requestCode, int resultCode, Intent data){
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                Uri resultUri = result.getUri();
                image.setImageURI(resultUri);
                imageUri = resultUri;
            }else if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
            }
        }

    }
}


