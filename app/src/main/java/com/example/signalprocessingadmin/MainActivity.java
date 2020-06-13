package com.example.signalprocessingadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.buttonReport:
                intent = new Intent(MainActivity.this, ReportActivity.class);
                break;
            case R.id.buttonWaitAnimal:
                intent = new Intent(MainActivity.this, WaitAnimalActivity.class);
                break;
            case R.id.buttonUniv:
                intent = new Intent(MainActivity.this, AddUniversityActivity.class);
                break;
        }
        startActivity(intent);
    }
}
