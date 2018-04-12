package com.example.lenovo.selphies;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
    private EditText username, password, confirmPassword, email;
    private Button upload, register, cancel;
    private ImageView profileImage;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    private FirebaseAuth firebaseAuth;

    private FirebaseDatabase database;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setupUI();

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    // database
                    String user_username = username.getText().toString().trim();
                    String user_password = password.getText().toString().trim();
                    String user_email = email.getText().toString().trim();

                    ref = database.getReference("users").child(user_username);
                    ref.child("username").setValue(user_username);
                    ref.child("password").setValue(user_password);
                    ref.child("email").setValue(user_email);

                    Toast.makeText(Register.this, "Registration Complete.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Register.this, Login.class));


                    /*
                    firebaseAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(Register.this, "Registration Complete.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Register.this, Login.class));
                            }else{
                                Toast.makeText(Register.this, "Registration Failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    */
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
