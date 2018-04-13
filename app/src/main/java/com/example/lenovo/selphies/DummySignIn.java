package com.example.lenovo.selphies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class DummySignIn extends AppCompatActivity {

    private Button logout;
    private TextView success, logged;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy_sign_in);

        setupUI();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signout();
            }
        });

    }

    private void setupUI() {
        logout = (Button) findViewById(R.id.logoutButton);
        success = (TextView) findViewById(R.id.success);
        logged = (TextView) findViewById(R.id.logged);

        mAuth = FirebaseAuth.getInstance();
        success.setText(mAuth.getCurrentUser().getEmail().toString());
        logged.setText("Logged in as " + mAuth.getCurrentUser().getDisplayName().toString());
    }

    private  void signout(){
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();

        startActivity(new Intent(DummySignIn.this, Login.class));
    }

}
