package com.example.lenovo.selphies;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class Register extends AppCompatActivity {
    private EditText username, password, confirmPassword, email;
    private Button upload, register, cancel;
    private ImageView profileImage;
    private static final int GALLERY_INTENT = 1;
    private FirebaseAuth mAuth;
    private Uri imageUri;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setupUI();

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    final String user_username = username.getText().toString().trim();
                    final String user_password = password.getText().toString().trim();
                    final String user_email = email.getText().toString().trim();


                    mAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                String user_id = mAuth.getCurrentUser().getUid();
                                ref = database.getReference("users").child(user_id);
                                ref.child("username").setValue(user_username);
                                ref.child("password").setValue(user_password);
                                ref.child("email").setValue(user_email);
                                ref.child("userId").setValue(user_id);
                                ref.child("description").setValue("");
                                ref.child("endorse").setValue(0);

                                if(imageUri != null){
                                    StorageReference filepath = storageReference.child("profile").child(imageUri.getLastPathSegment());
                                    filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                            ref.child("profile").setValue(downloadUrl.toString());
                                        }
                                    });
                                }else{
                                    ref.child("profile").setValue("none");
                                }
                                Toast.makeText(Register.this, "Registration Complete.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Register.this, Login.class));
                            }else{
                                Toast.makeText(Register.this, "Registration Failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
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
                Intent gallery = new Intent(Intent.ACTION_PICK);
                gallery.setType("image/*");
                startActivityForResult(gallery, GALLERY_INTENT);
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
        Boolean result = true;

        String inputUsername = username.getText().toString();
        String inputPassword = password.getText().toString();
        String inputConfirmPassword = confirmPassword.getText().toString();
        String inputEmail = email.getText().toString();

        if(inputUsername.isEmpty() || inputPassword.isEmpty() || inputConfirmPassword.isEmpty() || inputEmail.isEmpty()){
            Toast.makeText(this, "Please fill in all the information.", Toast.LENGTH_SHORT).show();
            result = false;
        } else if(inputPassword.length() < 6){
            Toast.makeText(this, "The password must be at least 6 characters.", Toast.LENGTH_SHORT).show();
            result = false;
        } else if(!inputPassword.equals(inputConfirmPassword)){
            Toast.makeText(this, "The password does not match with the confirmation.", Toast.LENGTH_SHORT).show();
            result = false;
        }
        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK && requestCode == GALLERY_INTENT){
            imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }

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
}
