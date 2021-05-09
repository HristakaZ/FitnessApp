package com.example.fitnessapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

import models.User;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firebaseAuth = FirebaseAuth.getInstance();
        email = (EditText)findViewById(R.id.registerEmail);
        password = (EditText)findViewById(R.id.registerPassword);
    }

    public void registerUser(View view) {
        String email = this.email.getText().toString();
        String password = this.password.getText().toString();

        if (email.isEmpty()) {
            this.email.setError("Please enter an email!");
            this.email.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            this.password.setError("Please enter a password!");
            this.password.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            this.email.setError("Please provide a valid email!");
            this.email.requestFocus();
            return;
        }
        if (password.length() < 6) { //less than 6 because that is the firebase minimum for the length of the password
            this.password.setError("Please enter a password that is minimum 6 characters!");
            this.password.requestFocus();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    User user = new User(email, password);
                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().
                            getCurrentUser()
                            .getUid()).
                            setValue(user).
                            addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            try {
                                if (task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this,
                                            "The user has been registered successfully!", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            }
                            catch (Exception exception) {
                                Toast.makeText(RegisterActivity.this, "Oops, something went wrong!",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(RegisterActivity.this, "Failed to register the user!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}