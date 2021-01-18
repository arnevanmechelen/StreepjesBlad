package com.example.streepjesblad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener{

    private EditText passwordEditText, emailEditText, nameEditText, rePasswordEditText;
    private Button registerBtn;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private String userId;

    public static final String TAG = "RegisterUser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        nameEditText = (EditText) findViewById(R.id.nameEditText);
        nameEditText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                nameEditText.setError(null);
            }
        });
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        emailEditText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                emailEditText.setError(null);
            }
        });
        rePasswordEditText = (EditText) findViewById(R.id.rePasswordEditText);
        rePasswordEditText.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                rePasswordEditText.setError(null);
            }
        });
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        passwordEditText.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                passwordEditText.setError(null);
            }
        });



        registerBtn = (Button) findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                registerUser();
            }
        });
    }



    private void registerUser() {
        final String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        final String username = nameEditText.getText().toString().trim();
        String password2 = rePasswordEditText.getText().toString().trim();

        if (username.isEmpty()) {
            nameEditText.setError("Username is required!");
            nameEditText.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            emailEditText.setError("Email is required!");
            emailEditText.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Please provide valid email!");
            emailEditText.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            passwordEditText.setError("Password is required");
            passwordEditText.requestFocus();
            return;
        }

        if (password.length() < 6) {
            passwordEditText.setError("Password length should be at least 6 characters!");
            passwordEditText.requestFocus();
            return;
        }

        if (!checkPasswordsMatch(password, password2)){
            rePasswordEditText.setError("Passwords don't match!");
            rePasswordEditText.requestFocus();
            return;
        }

        //register the user to firebase
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RegisterUser.this, "User created successfully", Toast.LENGTH_SHORT).show();

                    userId = mAuth.getCurrentUser().getUid();
                    DocumentReference docRef = firestore.collection("users").document(userId);
                    Map<String, Object> user = new HashMap<>();
                    user.put("username", username);
                    user.put("email", email);
                    user.put("amount", 0);
                    user.put("isAdmin", "0");
                    docRef.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "onSuccess: user profile is created for " + userId);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: " + e.toString());
                        }
                    });

                    startActivity(new Intent(getApplicationContext(), LoginUser.class));
                } else {
                    Toast.makeText(RegisterUser.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    public boolean checkPasswordsMatch(String password1, String password2){
        if (password1.equals(password2)){
            return true;
        }else{
            return false;
        }
    }

}