package com.example.upvote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class activity_feature_upvote extends AppCompatActivity {

    private String TAG = "Upvote";
    private FirestoreRecyclerAdapter adapter;
    private RecyclerView mLeaderboardList;
    private FirebaseFirestore firebaseFirestore;
    private EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feature_upvote);
        mLeaderboardList = findViewById(R.id.recyclerview);
        firebaseFirestore = FirebaseFirestore.getInstance();
        Query query = firebaseFirestore.collection("feature_upvote").orderBy("upvote_count", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Upvote_model> options = new FirestoreRecyclerOptions.Builder<Upvote_model>()
                .setQuery(query, Upvote_model.class)
                .build();

//        firebaseFirestore.collection("feature_upvote")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//
//                            }
//
//                        } else {
//                            Log.d("Query", "Error getting documents: ", task.getException());
//                        }
//                    }
//                });

        adapter = new FirestoreRecyclerAdapter<Upvote_model, activity_feature_upvote.UpvoteViewHolder>(options) {
            @NonNull
            @Override
            public activity_feature_upvote.UpvoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.upvote_feature_data, parent, false);
                return new activity_feature_upvote.UpvoteViewHolder(view);
            }


            @Override
            protected void onBindViewHolder(@NonNull activity_feature_upvote.UpvoteViewHolder upvoteViewHolder, int i, @NonNull Upvote_model leaderboardModel) {

                //upvoteViewHolder.image.setText(leaderboardModel.getFirstName() + " " + leaderboardModel.getLastName());
                upvoteViewHolder.title.setText(String.valueOf(leaderboardModel.getTitle()));
                upvoteViewHolder.description.setText(String.valueOf(leaderboardModel.getDescription()));
                upvoteViewHolder.upvote_count.setText(String.valueOf(leaderboardModel.getUpvoteCount()));
            }

        };

        mLeaderboardList.setHasFixedSize(true);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mLeaderboardList.setLayoutManager(linearLayoutManager);
        mLeaderboardList.setAdapter(adapter);
    }

    private class UpvoteViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;
        private TextView title;
        private TextView upvote_count;
        private TextView description;

        public UpvoteViewHolder(@NonNull View itemView) {
            super(itemView);
            image = findViewById(R.id.image);
            title = findViewById(R.id.title);
            description = findViewById(R.id.description);
            upvote_count = findViewById(R.id.upvoteCount);

        }
    }


    public void update_upvote(View view) {
        final String title = "test title";
        final int[] upvote_count = {0};
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("feature_upvote")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String test_title = (String) document.get("title");
                                if (test_title.trim() == title) {
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
        String timestamp = sdf.format(new Date());
        Map<String, Object> feature_upvote_user = new HashMap<>();
        feature_upvote_user.put("feature", title);
        feature_upvote_user.put("sequence", upvote_count[0] + 1);
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

