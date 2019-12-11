package com.example.fruit_tourney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class TournoiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournoi);

        String tab[] = new String[20];

    }
}

//    // LA BDD
//    // Access a Cloud Firestore instance from your Activity
//    FirebaseFirestore db = FirebaseFirestore.getInstance();
//    // Create a new user with a first and last name
//    Map<String, Object> victoire = new HashMap<>();
//        victoire.put("IdFruit", "Banane");
//                victoire.put("IdUser", FirebaseAuth.getInstance().getCurrentUser().getUid());
//
//                // Add a new document with a generated ID
//                db.collection("Victoires")
//                .add(victoire)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//@Override
//public void onSuccess(DocumentReference documentReference) {
//        Log.d("success_add_victories", "DocumentSnapshot added with ID: " + documentReference.getId());
//        }
//        })
//        .addOnFailureListener(new OnFailureListener() {
//@Override
//public void onFailure(@NonNull Exception e) {
//        Log.e("err_add_victory", "Error adding document", e);
//        }
//        });
