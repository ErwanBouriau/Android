package com.example.fruit_tourney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;


import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class TournoiActivity extends AppCompatActivity {
    private ArrayList<StorageReference> allReferences;
    private ArrayList<StorageReference> selected;
    private int compteur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournoi);
        this.allReferences = new ArrayList<StorageReference>();
        this.selected = new ArrayList<StorageReference>();
        compteur = 0;

        initialize();


    }

    public void initialize() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        storageRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference image : listResult.getItems()) {
                    allReferences.add(image);
                }

                remplirSelected(allReferences);
                loadImage();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("listeAll failed !");
            }
        });
    }

    public void loadImage() {
        ImageView image1 = this.findViewById(R.id.fruit1);
        GlideApp.with(this)
                .load(selected.get(0))
                .into(image1);
        ImageView image2 = this.findViewById(R.id.fruit2);
        GlideApp.with(this)
                .load(selected.get(1))
                .into(image2);
    }

    public void remplirSelected(ArrayList<StorageReference> refs) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i=0; i<25; i++) {
            list.add(i);
        }
        Collections.shuffle(list);
        for (int i=0; i<8; i++) {
            selected.add(refs.get(list.get(i)));
        }
    }

    public void onClickImage(View v) {
        TextView round = this.findViewById(R.id.round);
        compteur++;
        switch (compteur){
            case 1:
                round.setText("Round 2 - Quart de finale");
                break;
            case 2:
                round.setText("Round 3 - Quart de finale");
                break;
            case 3:
                round.setText("Round 4 - Quart de finale");
                break;
            case 4:
                round.setText("Round 1 - Demi-finale");
                break;
            case 5:
                round.setText("Round 2 - Demi-finale");
                break;
            case 6:
                round.setText("Finale");
                break;
            case 7:
                round.setText("Vainqueur");
                break;
        }
        if(compteur < 7){
            switch(v.getId()) {
                case R.id.fruit1:
                    selected.add(selected.get(0));
                    selected.remove(0);
                    selected.remove(0);
                    loadImage();
                    break;
                case R.id.fruit2:
                    selected.add(selected.get(1));
                    selected.remove(0);
                    selected.remove(0);
                    loadImage();
                    break;
            }
        }
        else {
            ImageView imageFinale = this.findViewById(R.id.vs);
            ImageView image1 = this.findViewById(R.id.fruit1);
            ImageView image2 = this.findViewById(R.id.fruit2);
            image1.setVisibility(View.INVISIBLE);
            image2.setVisibility(View.INVISIBLE);
            imageFinale.getLayoutParams().height = 700;
            imageFinale.getLayoutParams().width = 700;
            imageFinale.requestLayout();
            if(v.getId() == R.id.fruit1) {
                GlideApp.with(this)
                        .load(selected.get(0))
                        .into(imageFinale);
            }
            else {
                GlideApp.with(this)
                        .load(selected.get(1))
                        .into(imageFinale);
            }
        }
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
