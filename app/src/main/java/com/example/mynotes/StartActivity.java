package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.mynotes.user_signin.LoginActivity;
import com.example.mynotes.user_signin.RegisterActivity;
import com.google.firebase.auth.FirebaseAuth;

public class StartActivity extends AppCompatActivity {

    private Button regBtn, loginBtn;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        regBtn = findViewById(R.id.register_start);
        loginBtn = findViewById(R.id.login_start);

        fAuth = FirebaseAuth.getInstance();
        updateUI();

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private void register(){
        Intent registerIntent = new Intent(StartActivity.this, RegisterActivity.class);
        startActivity(registerIntent);
    }

    private void login(){
        Intent loginIntent = new Intent(StartActivity.this, LoginActivity.class);
        startActivity(loginIntent);

    }

    // simple step to check whether the user is null or not
    private void updateUI(){
        if (fAuth.getCurrentUser() != null)
        {
            Log.i("StartActivity", "fAuth!=null");
            Intent startIntent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(startIntent);
            finish();
        }
        else
        {
            Log.i("StartActivity", "fAuth == null");
        }

    }
}

