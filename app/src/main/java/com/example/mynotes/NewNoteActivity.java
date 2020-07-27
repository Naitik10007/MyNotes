package com.example.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class NewNoteActivity extends AppCompatActivity {

    private Button btnCreate;
    private EditText etTitle, etContent;
    private Toolbar mToolbar;
    private Menu mainMenu;

    private DatabaseReference fNoteDatabase;
    private FirebaseAuth fAuth;
    private String noteID ;

    private boolean isExist; //variable to check note exists or not for editing purpose

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        try{
            noteID = getIntent().getStringExtra("noteId");

            if(!noteID.trim().equals("")){
                isExist = true;
            }else{
                isExist = false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        btnCreate = findViewById(R.id.new_note_btn);
        etTitle = findViewById(R.id.new_note_title);
        etContent = findViewById(R.id.new_note_content);
        //mToolbar = findViewById(R.id.new_note_toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fAuth = FirebaseAuth.getInstance();
        fNoteDatabase = FirebaseDatabase.getInstance().getReference().child("Notes").child(fAuth.getCurrentUser().getUid());


        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = etTitle.getText().toString().trim();
                String content = etContent.getText().toString().trim();
                // step to check both fields are not empty
                if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content)){
                    createNote(title,content);
                }else{
                    Snackbar.make(view,"Fill Empty Fields", Snackbar.LENGTH_SHORT).show();
                }

            }
        });

        putData();
    }

    private void putData(){
        if(isExist){
            fNoteDatabase.child(noteID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("title") && dataSnapshot.hasChild("content")) {
                        String title = dataSnapshot.child("title").getValue().toString();
                        String content = dataSnapshot.child("content").getValue().toString();

                        etTitle.setText(title);
                        etContent.setText(content);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void createNote(String title, String content){

        if(fAuth.getCurrentUser() != null) {
            //Updating a note
            if(isExist){
                Map updateMap = new HashMap();
                updateMap.put("title", etTitle.getText().toString().trim());
                updateMap.put("content", etContent.getText().toString().trim());
                updateMap.put("timestamp", ServerValue.TIMESTAMP);

                fNoteDatabase.child(noteID).updateChildren(updateMap);
                Toast.makeText(this,"Note Updated Successfully",Toast.LENGTH_SHORT).show();
            }
            // creating a new note
            else {
                final DatabaseReference newNoteRef = fNoteDatabase.push();
                // using map instead of child
                final Map noteMap = new HashMap();
                noteMap.put("title", title);
                noteMap.put("content", content);
                noteMap.put("timestamp", ServerValue.TIMESTAMP);

                Thread mainThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        newNoteRef.setValue(noteMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(NewNoteActivity.this, "Note added to Database", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(NewNoteActivity.this, "ERROR ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }

                });
                mainThread.start();
            }

        }else{
            Toast.makeText(this,"User is not Signed in Properly", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.new_note_menu, menu);
        mainMenu = menu;

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.new_note_delete_btn:
                if(isExist){
                    deleteNote();
                }else{
                    Toast.makeText(this, "Nothing to Delete", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return true;
    }
    private void deleteNote(){
        fNoteDatabase.child(noteID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(NewNoteActivity.this,"Note Is Deleted Successfully. ",Toast.LENGTH_SHORT).show();
                    noteID = "no";
                    finish();
                }else{
                    Log.e("NewNoteActivity",task.getException().getMessage().toString());
                    Toast.makeText(NewNoteActivity.this,"ERROR !" + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}