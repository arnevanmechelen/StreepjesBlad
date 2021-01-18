package com.example.streepjesblad;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView sumTextView, welcomeTextView, amountTextView;
    private Integer amount;
    private Double pricePerAmount = 0.30;
    private Button adminBtn;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    private String userId;
    private String username;
    private DocumentReference docRef;
    private String isAdmin;
    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sumTextView = (TextView) findViewById(R.id.sumTextView);
        amountTextView = (TextView) findViewById(R.id.amountTextView);
        welcomeTextView = (TextView) findViewById(R.id.welcomeTextView);
        adminBtn = (Button) findViewById(R.id.adminBtn);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        userId = mAuth.getCurrentUser().getUid();

        docRef = firestore.collection("users").document(userId);
        docRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot docSnapshot, @Nullable FirebaseFirestoreException e) {
                String username = docSnapshot.getString("username");
                String welcomeText = getString(R.string.welcome, username);
                welcomeTextView.setText(welcomeText);
                isAdmin = docSnapshot.get("isAdmin").toString();
                checkIfAdmin(isAdmin);
                amount = Integer.parseInt(docSnapshot.get("amount").toString());
                amountTextView.setText(docSnapshot.get("amount").toString());
                calculateSum();
            }
        });

        adminBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.adminBtn:
                startActivity(new Intent(this, AdminActivity.class));
                break;
        }
    }

    public void logOut(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LoginUser.class));
        finish();
    }

    public void addThree(View view) {
        amount += 3;
        updateAmount();
    }

    public void addTwo(View view) {
        amount += 2;
        updateAmount();
    }

    public void addOne(View view) {
        amount += 1;
        updateAmount();
    }

    public void updateAmount(){
        Map<String, Object> user = new HashMap<>();
        user.put("amount", amount);

        docRef.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSucces: user updated!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: " + e);
            }
        });
    }

    public void calculateSum(){
        Double sum = amount*pricePerAmount;
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(2);
        format.setCurrency(Currency.getInstance("EUR"));
        String sumText = format.format(sum);
        sumTextView.setText(sumText);
    }


    private void checkIfAdmin(String isAdmin){
        if(isAdmin.equals("1")){
            adminBtn.setVisibility(View.VISIBLE);
        }
    }
}