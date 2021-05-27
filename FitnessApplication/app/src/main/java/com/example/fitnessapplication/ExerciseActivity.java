package com.example.fitnessapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import models.Exercise;

public class ExerciseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        getIntent();
    }

    public void createExercise(View view) {
        EditText exerciseText = findViewById(R.id.exerciseText);
        String exerciseName = exerciseText.getText().toString();
        Exercise exercise = new Exercise(exerciseName);
        DatabaseReference exercises = FirebaseDatabase.getInstance().getReference("Exercises");
        exercises.push().setValue(exercise).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(ExerciseActivity.this, "Successfully created an exercise!",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ExerciseActivity.this, MainActivity.class);
                intent.putExtra("Exercise created", "Exercise created");
                startActivity(intent);
                //TO DO: redirect to the program of the created exercise(afterwards you will have a dropdown list
                // for the fitness program that the exercise will be in), for now, redirecting to main page
            }
        });
    }

}