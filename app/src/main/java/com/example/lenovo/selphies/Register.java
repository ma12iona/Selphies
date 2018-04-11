package com.example.lenovo.selphies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class Register extends AppCompatActivity {
    private EditText Username;
    private EditText Password;
    private EditText ConfirmPassword;
    private EditText Email;
    private Button Upload;
    private Button Register;
    private Button Cancel;
    private ImageView ProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }
}
