package com.example.lenovo.selphies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private Button login;
    private Button register;
    private TextView wrong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.usernameText);
        password = (EditText) findViewById(R.id.passwordText);
        login = (Button) findViewById(R.id.loginButton);
        register = (Button) findViewById(R.id.registerButton);
        wrong = (TextView) findViewById(R.id.wrongText);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(username.getText().toString(), password.getText().toString());
            }
        });

    }

    private void validate(String username, String password){
        if((username.equals("Admin")) && (password.equals("1234"))){
            Intent login = new Intent(Login.this, MainActivity.class);
            startActivity(login);
        }else{
            wrong.setVisibility(View.VISIBLE);
        }
    }
}
