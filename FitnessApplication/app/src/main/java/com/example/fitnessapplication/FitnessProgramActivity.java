package com.example.fitnessapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StreamDownloadTask;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import models.FitnessProgram;

public class FitnessProgramActivity extends AppCompatActivity {
    private ListView fitnessProgramsListView;
    private Button createFitnessProgramBtn;
    private Button submitCreateFitnessProgramBtn;
    private ListView myFitnessProgramExercises;
    private ImageView fitnessProgramExercisePicture;
    private String imagePath;
    ArrayAdapter<String> fitnessProgramsAdapter;
    ArrayAdapter<String> fitnessProgramsExercisesAdapter;
    StorageReference storageReference;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_fitnessprograms);
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        Intent intent = getIntent();
        this.createFitnessProgramBtn = findViewById(R.id.createFitnessProgramBtn);
        this.submitCreateFitnessProgramBtn = findViewById(R.id.submitCreateFitnessProgramBtn);

        if(intent.getStringExtra("Fitness programs") != null) {
            setFitnessProgramsInListView(savedInstanceState);
        }

        createFitnessProgramBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_fitness_program);
            }
        });

        fitnessProgramsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setFitnessProgramsNames(parent, view, position, id);
            }
        });

    }


    public void getFitnessProgramsExercisePicture(AdapterView<?> parent, View view, int position, long id) {
        try {
            setContentView(R.layout.layout_activity_fitnessprogram_exercise_picture);
            fitnessProgramExercisePicture = findViewById(R.id.fitnessProgramExercisePicture);
            Query currentExerciseQuery = FirebaseDatabase.getInstance().getReference()
                    .child("Exercises")
                    .orderByChild("name")
                    .equalTo(fitnessProgramsExercisesAdapter.getItem(position));
            currentExerciseQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    for (DataSnapshot currentExerciseSnapshot : snapshot.getChildren()) {
                        imagePath = currentExerciseSnapshot.child("cloudImagePath")
                                .getValue(String.class);
                        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(imagePath.substring(imagePath.indexOf("/images")));
                        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Got the download URL for 'users/me/profile.png'
                                Glide.with(getApplicationContext())
                                        .load(uri)
                                        .into(fitnessProgramExercisePicture);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                                Toast.makeText(FitnessProgramActivity.this, exception.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }
        catch(Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void setFitnessProgramsInListView(Bundle savedInstanceState) {
            fitnessProgramsListView = findViewById(R.id.myFitnessProgramsListView);

            databaseReference.child("FitnessPrograms").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    List<String> fitnessProgramNames = new ArrayList<String>();

                    for (DataSnapshot fitnessProgramSnapshot : snapshot.getChildren()) {
                        String fitnessProgramName = fitnessProgramSnapshot.child("name").getValue(String.class);
                        fitnessProgramNames.add(fitnessProgramName);
                    }
                    fitnessProgramsAdapter = new ArrayAdapter<String>(
                            FitnessProgramActivity.this, android.R.layout.simple_list_item_1,
                            fitnessProgramNames);
                    fitnessProgramsListView.setAdapter(fitnessProgramsAdapter);
                }
                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
    }

    public void setFitnessProgramsNames(AdapterView<?> parent, View view, int position, long id) {

         List<String> fitnessProgramExercises = new ArrayList<String>();
         setContentView(R.layout.layout_activity_displayfitnessprogram);
         myFitnessProgramExercises = findViewById(R.id.myFitnessProgramExercises);
         Query queryEquivalentFitnessProgram = databaseReference
                 .child("Exercises")
                 .orderByChild("fitnessProgram")
                 .equalTo(fitnessProgramsAdapter.getItem(position));
         queryEquivalentFitnessProgram.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    for (DataSnapshot fitnessProgramSnapshot : snapshot.getChildren()) {
                        String fitnessProgramExercise = fitnessProgramSnapshot.child("name")
                        .getValue(String.class);
                        fitnessProgramExercises.add(fitnessProgramExercise);
                    }
                    fitnessProgramsExercisesAdapter = new ArrayAdapter<String>(
                    FitnessProgramActivity.this, android.R.layout.simple_list_item_1,
                    fitnessProgramExercises);
                    myFitnessProgramExercises.setAdapter(fitnessProgramsExercisesAdapter);
                   }

                   @Override
                   public void onCancelled(@NonNull @NotNull DatabaseError error) {

                          }
                   });
            myFitnessProgramExercises.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    setContentView(R.layout.layout_activity_fitnessprogram_exercise_picture);
                    getFitnessProgramsExercisePicture(parent, view, position, id);
                }
            });
        }


    public void createFitnessProgram(View view) {
        TextView fitnessProgramNameText = findViewById(R.id.fitnessProgramName);
        String fitnessProgramName = fitnessProgramNameText.getText().toString();
        FitnessProgram fitnessProgram = new FitnessProgram(fitnessProgramName);
        DatabaseReference fitnessPrograms = databaseReference.child("FitnessPrograms");
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