package com.example.fitnessapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;

import models.FitnessProgram;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = user.getEmail();
        Intent intent = getIntent();
        if (intent.getSerializableExtra("Exercise created") != null) {
            Toast.makeText(this, "DON'T LET YOUR MEMES BE DREAMS!", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Welcome to the ARHNULD Fitness App! Ready to become the terminator?",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void logout(View view) {
        Intent logoutIntent = new Intent(this, LoginActivity.class);
        startActivity(logoutIntent);
    }

    public void redirectToAccount(View view) {
        Intent intent = new Intent(this, AccountActivity.class);
        startActivity(intent);
    }

    public void redirectToExercise(View view) {
        Intent intent = new Intent(this, ExerciseActivity.class);
        startActivity(intent);
    }

    public void redirectToFitnessProgram(View view) {
        Intent intent = new Intent(this, FitnessProgramActivity.class);
        startActivity(intent);
    }

}