package com.example.lenovo.selphies;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Register extends AppCompatActivity {
    private EditText username, password, confirmPassword, email;
    private Button upload, register, cancel;
    private ImageView profileImage;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setupUI();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    // database
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    private void setupUI(){
        username = (EditText) findViewById(R.id.usernameText);
        password = (EditText) findViewById(R.id.passwordText);
        confirmPassword = (EditText) findViewById(R.id.confirmText);
        email = (EditText) findViewById(R.id.emailText);
        upload = (Button) findViewById(R.id.uploadButton);
        register = (Button) findViewById(R.id.registerButton);
        cancel = (Button) findViewById(R.id.cancelButton);
        profileImage = (ImageView) findViewById(R.id.profileImage);
    }

    private Boolean validate(){
        Boolean result = false;

        String inputUsername = username.getText().toString();
        String inputPassword = password.getText().toString();
        String inputConfirmPassword = confirmPassword.getText().toString();
        String inputEmail = email.getText().toString();

        if(inputUsername.isEmpty() || inputPassword.isEmpty() || inputConfirmPassword.isEmpty() || inputEmail.isEmpty()){
            Toast.makeText(this, "Please fill in all the information.", Toast.LENGTH_SHORT).show();
        }else if(inputPassword != inputConfirmPassword){
            Toast.makeText(this, "Please fill in the correct password confirmation.", Toast.LENGTH_SHORT).show();
        } else result = true;
        return result;
    }

    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            profileImage.setImageURI(imageUri);
        }
    }
}
