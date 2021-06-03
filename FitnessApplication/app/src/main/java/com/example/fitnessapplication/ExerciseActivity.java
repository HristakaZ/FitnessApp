package com.example.fitnessapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import models.Exercise;
import models.FitnessProgram;

public class ExerciseActivity extends AppCompatActivity {
    private Spinner fitnessProgramSpinner;
    private ListView exercisesListView;
    private ImageView exercisePicture;
    private Uri pictureUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private StorageReference pictureRef;
    private Button submitButton;
    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        exercisePicture = findViewById(R.id.exercisePicture);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        submitButton = findViewById(R.id.exerciseSubmitBtn);
        getIntent();
        fitnessProgramSpinner = getFitnessPrograms();
        exercisePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });
    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            pictureUri = data.getData();
            exercisePicture.setImageURI(pictureUri);
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uploadPicture();
                }
            });
        }
    }


    private void uploadPicture() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading image ...");
        progressDialog.show();

        final String randomKey = UUID.randomUUID().toString();

        pictureRef = storageReference.child("images/" + randomKey);

        imagePath = pictureRef.toString();

        pictureRef.putFile(pictureUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                Snackbar.make(findViewById(android.R.id.content), "Image uploaded",
                        Snackbar.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Failed to upload", Toast.LENGTH_LONG).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
                double progressPercentage = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                progressDialog.setMessage("Progress: " + (int) progressPercentage + "%");
            }
        });
        createExercise(submitButton);
    }


    public Spinner getFitnessPrograms() {
        fitnessProgramSpinner = findViewById(R.id.fitnessProgramOptionSpinner);
        FirebaseDatabase.getInstance().getReference()
                .child("FitnessPrograms")
                .orderByChild("user")
                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .addValueEventListener(new ValueEventListener() {
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
        String cloudImagePath = imagePath;
        fitnessProgramSpinner = findViewById(R.id.fitnessProgramOptionSpinner);
        String chosenFitnessProgram = fitnessProgramSpinner.getSelectedItem().toString();
        String currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        FitnessProgram fitnessProgram = new FitnessProgram(chosenFitnessProgram, currentUserEmail);
        Exercise exercise = new Exercise(exerciseName, cloudImagePath, fitnessProgram.getName());

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