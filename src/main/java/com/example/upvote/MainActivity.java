package com.example.upvote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private String TAG="Upvote: ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final String title="test title";
        final int[] upvote_count = {0};
        db.collection("feature_upvote")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String test_title= (String) document.get("title");
                                if(test_title.trim()==title)
                                {
                                    upvote_count[0] = (int) document.get("upvote_count");
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        // Create a new user with a first, middle, and last name
        SimpleDateFormat sdf = new SimpleDateFormat("[yyyy-MM-dd EEE HH:mm]");
        String timestamp=sdf.format(new Date());
        Map<String, Object> feature_upvote_user = new HashMap<>();
        feature_upvote_user.put("feature", title);
        feature_upvote_user.put("sequence", upvote_count[0]+1);
        feature_upvote_user.put("timestamp", timestamp);
        feature_upvote_user.put("uid", "user.getUid()");

        // Add a new document with a generated ID
        db.collection("feature_upvote_users")
                .add(feature_upvote_user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }
}