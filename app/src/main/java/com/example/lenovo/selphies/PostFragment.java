package com.example.lenovo.selphies;


import android.content.Intent;
import android.net.Uri;
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

import static android.app.Activity.RESULT_OK;

public class PostFragment extends Fragment {

    private Button post, gallery, camera;
    private ImageView image;
    private Uri imageUri;

    private static final int GALLERY_INTENT = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_post, container, false);

        post = (Button) view.findViewById(R.id.postButton);
        gallery = (Button) view.findViewById(R.id.galleryButton);
        camera = (Button) view.findViewById(R.id.cameraButton);

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_INTENT);

            }
        });

        return inflater.inflate(R.layout.fragment_post, null);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == RESULT_OK && requestCode == GALLERY_INTENT){
            imageUri = data.getData();
            image.setImageURI(imageUri);
        }
    }
}
