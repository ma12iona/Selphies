package com.example.lenovo.selphies;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



/**
This class is the login page of the application
 */
public class Login extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private Button login;
    private Button register;
    private Button bypass;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupUI();

        mAuth = FirebaseAuth.getInstance();
        /*if(mAuth.getCurrentUser() != null){
            mAuth.signOut();
        }*/

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null){
                    startActivity(new Intent(Login.this, MainActivity.class));
                }
            }
        };

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });

    }

    /**
    This method is override to let user login if there is a cache data
     */
    @Override
    protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    /**
    This is an initialization method
     */
    private void setupUI(){
        email = (EditText) findViewById(R.id.emailText);
        password = (EditText) findViewById(R.id.passwordText);
        login = (Button) findViewById(R.id.loginButton);
        register = (Button) findViewById(R.id.registerButton);
    }

    /**
    This method is use to check if the user exist when logging in
     */
    private void validate(){

        String user_email = email.getText().toString();
        String user_password = password.getText().toString();

        if(TextUtils.isEmpty(user_email) || TextUtils.isEmpty(user_password)){
            Toast.makeText(Login.this, "Please fill in the email and password.", Toast.LENGTH_SHORT).show();
        }else{
            mAuth.signInWithEmailAndPassword(user_email,user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        Toast.makeText(Login.this, "Incorrect email or password.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed(){

    }
}
