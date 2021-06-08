package com.example.myrun;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Record extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference().child("runs");

    private EditText distance;
    private EditText note;
    private Button saveButton;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        distance = findViewById(R.id.distanceText);
        saveButton = findViewById(R.id.button);
        note = findViewById(R.id.noteText);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String distanceTxt = distance.getText().toString();
                String noteTxt = note.getText().toString();

                HashMap<String, String> runMap = new HashMap<>();
                runMap.put("distance", distanceTxt);
                runMap.put("note", noteTxt);
                myRef.push().setValue(runMap);
                saveRunPressed();
            }
        });
    }

    public void saveRunPressed(){
        Intent intent = new Intent(this, ShowRuns.class);
        startActivity(intent);
    }

    public void homePressed(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void Finish(View view){
        finish();
    }

}


//        name = findViewById(R.id.name);
//        button = findViewById(R.id.button);
//
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String nameText = name.getText().toString();
//                myRef.setValue(nameText);
//            }
//        });
//
//        myRef.setValue("Hello, World!!!!!");




