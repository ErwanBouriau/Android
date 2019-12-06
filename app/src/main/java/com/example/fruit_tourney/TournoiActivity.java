package com.example.fruit_tourney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;


public class TournoiActivity extends AppCompatActivity {
    private ArrayList<StorageReference> allReferences;
    private ArrayList<StorageReference> selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournoi);
        this.allReferences = new ArrayList<StorageReference>();
        this.selected = new ArrayList<StorageReference>();

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
}
