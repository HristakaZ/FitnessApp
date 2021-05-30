package com.example.fitnessapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class AccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        getIntent();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = user.getEmail();
        TextView userEmailTextView = (TextView) findViewById(R.id.userEmail);
        userEmailTextView.setText(userEmail);
    }

    public void redirectToFitnessPrograms(View view) {
        Intent intent = new Intent(this, FitnessProgramActivity.class);
        intent.putExtra("Fitness programs", "Fitness programs");
        startActivity(intent);
    }

    public void redirectToExercise(View view) {
        Intent intent = new Intent(this, ExerciseActivity.class);
        startActivity(intent);
    }

}

