package com.example.streepjesblad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;


public class AdminActivity extends AppCompatActivity{


    private RecyclerView firestoreList;
    private FirebaseFirestore firestore;
    private Double pricePerAmount = 0.30;
    private FirestoreRecyclerAdapter adapter;
    public static final String TAG = "AdminActivity";
    private Button homeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        firestore = FirebaseFirestore.getInstance();
        firestoreList = (RecyclerView) findViewById(R.id.firestoreList);
        homeBtn = (Button) findViewById(R.id.homeBtn);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //Query
        Query query = firestore.collection("users").orderBy("username");
        //RecyclerOptions
        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<User, UserViewHolder>(options) {
            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_single, parent, false);
                return new UserViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull User model) {
                holder.listName.setText(model.getUsername());
                String amount = calculateSum(model.getAmount());
                holder.listAmount.setText(amount);
            }
        };

        firestoreList.setHasFixedSize(true);
        firestoreList.setLayoutManager(new LinearLayoutManager(this));
        firestoreList.setAdapter(adapter);
    }



    private class UserViewHolder extends RecyclerView.ViewHolder {

        private TextView listName;
        private TextView listAmount;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            listName = itemView.findViewById(R.id.listName);
            listAmount = itemView.findViewById(R.id.listAmount);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    public String calculateSum(long amount){
        Double sum = amount*pricePerAmount;
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(2);
        format.setCurrency(Currency.getInstance("EUR"));
        String sumText = format.format(sum);
        return sumText;
    }

    public void reset(View view){
        final Map<String, Object> user = new HashMap<>();
        user.put("amount",0);

        firestore.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String userId  = document.getId();
                                DocumentReference docRef =  firestore.collection("users").document(userId);
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
                        }else{
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}