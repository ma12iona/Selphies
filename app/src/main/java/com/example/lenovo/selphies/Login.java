package com.example.lenovo.selphies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity {
    private EditText Username;
    private EditText Password;
    private Button Login;
    private Button Register;
    private TextView Wrong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Username = (EditText) findViewById(R.id.usernameText);
        Password = (EditText) findViewById(R.id.passwordText);
        Login = (Button) findViewById(R.id.loginButton);
        Register = (Button) findViewById(R.id.registerButton);
        Wrong = (TextView) findViewById(R.id.wrongText);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(Username.getText().toString(), Password.getText().toString());
            }
        });

    }

    private void validate(String username, String password){
        if((username.equals("Admin")) && (password.equals("1234"))){
            Intent login = new Intent(Login.this, MainActivity.class);
            startActivity(login);
        }else{
            Wrong.setVisibility(View.VISIBLE);
        }
    }
}
