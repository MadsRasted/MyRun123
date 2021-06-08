package com.example.myrun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements Updatable{

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference().child("profile");
    private TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name = findViewById(R.id.nameTextview);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String nameTxt = snapshot.child("name").getValue(String.class);
                name.setText("Welcome " + nameTxt);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed : " + error.getCode());
            }
        });
    }

    public void RecordRunPressed(View view){
        Intent intent = new Intent(this, Record.class);
        startActivity(intent);
    }
    
    public void ViewRunsPressed(View view){
        Intent intent = new Intent(this, ShowRuns.class);
        startActivity(intent);
    }

    public void CreateProfilePressed(View view){
        Intent intent = new Intent(this, MyProfile.class);
        startActivity(intent);
    }

    @Override
    public void update(Object o){

    }
}