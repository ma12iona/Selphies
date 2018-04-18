package com.example.lenovo.selphies;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class PostFragment extends Fragment {

    private Button post, gallery, camera;
    private ImageView image;
    private Uri imageUri;

    private static final int GALLERY_INTENT = 1;
    private static final int CAMERA_INTENT = 2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_post, container, false);

        post = (Button) view.findViewById(R.id.postButton);
        gallery = (Button) view.findViewById(R.id.galleryButton);
        camera = (Button) view.findViewById(R.id.cameraButton);
        image =(ImageView) view.findViewById(R.id.imageView);

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

                /*
                File newfile = new File("test.jpg");

                try {
                    newfile.createNewFile();
                }
                catch (IOException e)
                {

                }

                Uri outputFileUri = Uri.fromFile(newfile);*/


                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                //imageUri = cameraIntent.getData();
                //cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(cameraIntent, CAMERA_INTENT);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode,resultCode,data);
        if (resultCode == RESULT_OK && requestCode == GALLERY_INTENT) {
            imageUri = data.getData();
            image.setImageURI(imageUri);
        }
        if (resultCode == RESULT_OK && requestCode == CAMERA_INTENT) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            image.setImageBitmap(bitmap);
            //imageUri = data.getData();
            //image.setImageURI(imageUri);
        }
    }

}


