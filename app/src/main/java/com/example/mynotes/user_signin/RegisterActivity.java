package com.example.mynotes.user_signin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mynotes.MainActivity;
import com.example.mynotes.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private Button regBtn;
    private TextInputLayout inpName, inpEmail, inpPass;

    private FirebaseAuth fAuth;
    private DatabaseReference fUserDatabase;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        regBtn = findViewById(R.id.reg_btn);
        inpName = findViewById(R.id.inp_reg_name);
        inpEmail = findViewById(R.id.inp_reg_email);
        inpPass = findViewById(R.id.inp_reg_pass);

        fAuth = FirebaseAuth.getInstance();
        fUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uname = inpName.getEditText().getText().toString().trim();
                String uemail = inpEmail.getEditText().getText().toString().trim();
                String upass = inpPass.getEditText().getText().toString().trim();

                registerUser(uname, uemail, upass);

            }
        });
    }

    private void registerUser(final String name, String email, String pass)
    {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait, Processing your Request.......");
        progressDialog.show();

        fAuth.createUserWithEmailAndPassword(email, pass ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    fUserDatabase.child(fAuth.getCurrentUser().getUid())
                            .child("basic").child("name").setValue(name).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                progressDialog.dismiss();
                                Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(mainIntent);
                                finish();
                                Toast.makeText(RegisterActivity.this, "User Created Successfully", Toast.LENGTH_SHORT).show();

                            }
                            else{
                                progressDialog.dismiss();
                                Toast.makeText(RegisterActivity.this, "Error !!!!!" + task.getException().getMessage() ,Toast.LENGTH_SHORT ).show();
                            }
                        }
                    });


                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "Error !!!!!" + task.getException().getMessage() ,Toast.LENGTH_SHORT ).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}