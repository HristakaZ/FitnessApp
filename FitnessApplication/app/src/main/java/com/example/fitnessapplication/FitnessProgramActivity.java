package com.example.fitnessapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import models.FitnessProgram;

public class FitnessProgramActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness_program);
    }

    public void createFitnessProgram(View view) {
        TextView fitnessProgramNameText = findViewById(R.id.fitnessProgramName);
        String fitnessProgramName = fitnessProgramNameText.getText().toString();
        FitnessProgram fitnessProgram = new FitnessProgram(fitnessProgramName);
        DatabaseReference fitnessPrograms = FirebaseDatabase.getInstance().getReference("FitnessPrograms");
        fitnessPrograms.push().setValue(fitnessProgram).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(FitnessProgramActivity.this, "Successfully created a fitness program!",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(FitnessProgramActivity.this, MainActivity.class);
                intent.putExtra("Fitness program created", "Fitness program created");
                startActivity(intent);
            }
        });
    }

}