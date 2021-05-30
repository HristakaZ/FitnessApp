package com.example.fitnessapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import models.Exercise;
import models.FitnessProgram;

public class ExerciseActivity extends AppCompatActivity {
    private Spinner fitnessProgramSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        getIntent();
        fitnessProgramSpinner = getFitnessPrograms();
    }

    public Spinner getFitnessPrograms() {
        fitnessProgramSpinner = findViewById(R.id.fitnessProgramOptionSpinner);
        FirebaseDatabase.getInstance().getReference().child("FitnessPrograms").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                List<String> fitnessPrograms = new ArrayList<String>();

                for (DataSnapshot fitnessProgramSnapshot : snapshot.getChildren()) {
                    String fitnessProgramName = fitnessProgramSnapshot.child("name").getValue(String.class);
                    fitnessPrograms.add(fitnessProgramName);
                }
                ArrayAdapter<String> fitnessProgramAdapter = new ArrayAdapter<String>(
                        ExerciseActivity.this, android.R.layout.simple_spinner_item, fitnessPrograms);
                fitnessProgramAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                fitnessProgramSpinner.setAdapter(fitnessProgramAdapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        return fitnessProgramSpinner;
    }

    public void createExercise(View view) {
        EditText exerciseText = findViewById(R.id.exerciseText);
        String exerciseName = exerciseText.getText().toString();
        fitnessProgramSpinner = findViewById(R.id.fitnessProgramOptionSpinner);
        String chosenFitnessProgram = fitnessProgramSpinner.getSelectedItem().toString();
        FitnessProgram fitnessProgram = new FitnessProgram(chosenFitnessProgram);
        Exercise exercise = new Exercise(exerciseName, fitnessProgram);

        DatabaseReference exercises = FirebaseDatabase.getInstance().getReference("Exercises");
        exercises.push().setValue(exercise).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(ExerciseActivity.this, "Successfully created an exercise!",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ExerciseActivity.this, MainActivity.class);
                intent.putExtra("Exercise created", "Exercise created");
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(ExerciseActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}