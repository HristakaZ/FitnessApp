package com.example.fitnessapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

import models.User;

public class LoginActivity extends AppCompatActivity {
    private EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = (EditText)findViewById(R.id.loginEmail);
        password = (EditText)findViewById(R.id.loginPassword);
        Intent logoutIntent = getIntent();
        boolean isLogoutWanted = logoutIntent.getBooleanExtra("isLogoutWanted", false);
        try {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null && isLogoutWanted != false) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("User", user);
                startActivity(intent);
            }
        }
        catch (Exception exception) {
            Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void redirectToRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void login(View view) {
        String email = this.email.getText().toString();
        String password = this.password.getText().toString();
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>(){
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                try {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Successfully logged in!",
                                Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "Invalid email or password!",
                                Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception exception) {
                    Toast.makeText(LoginActivity.this, "Oops, something went wrong!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void logout() {
        try {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            Intent logoutFromMain = getIntent();
            if (user != null && logoutFromMain != null) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(this, "Successfully logged out!", Toast.LENGTH_LONG).show();
                Intent logoutIntent = new Intent(this, LoginActivity.class);
                logoutIntent.putExtra("isLogoutWanted", true);
                startActivity(logoutIntent);
            }
        }
        catch (Exception exception) {
            Toast.makeText(this, "Oops, something went wrong!", Toast.LENGTH_LONG).show();
        }
    }
}