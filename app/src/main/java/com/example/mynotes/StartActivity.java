package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

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
                Animation anim = AnimationUtils.loadAnimation(StartActivity.this,R.anim.fadeout);
                regBtn.startAnimation(anim);
                register();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation anim = AnimationUtils.loadAnimation(StartActivity.this,R.anim.fadeout);
                loginBtn.startAnimation(anim);
                login();
            }
        });
    }

    private void register(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait......");
        progressDialog.show();
        Intent registerIntent = new Intent(StartActivity.this, RegisterActivity.class);
        startActivity(registerIntent);
    }

    private void login(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait.......");
        progressDialog.show();
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

