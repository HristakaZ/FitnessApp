package com.example.fitnessapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = user.getEmail();
        Toast.makeText(this, "Welcome to the ARHNULD Fitness App! Ready to become the terminator?",
                Toast.LENGTH_LONG).show();
    }

    public void logout(View view) {
        Intent logoutIntent = new Intent(this, LoginActivity.class);
        startActivity(logoutIntent);
    }

    public void redirectToAccount(View view) {
        Intent intent = new Intent(this, AccountActivity.class);
        startActivity(intent);
    }

}