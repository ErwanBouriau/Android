package com.example.fruit_tourney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class TournoiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournoi);

        ArrayList<StorageReference> allReferences = new ArrayList<StorageReference>();
        initialize(allReferences);

//        ImageView image1 = this.findViewById(R.id.fruit1);
//        GlideApp.with(this)
//                .load(allReferences.get(0))
//                .into(image1);
//        ImageView image2 = this.findViewById(R.id.fruit2);
//        GlideApp.with(this)
//                .load(allReferences.get(1))
//                .into(image2);

        System.out.println(allReferences);
    }

    public void initialize(final ArrayList<StorageReference> refs) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        storageRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference image : listResult.getItems()) {
                    refs.add(image);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("listeAll failed !");
            }
        });
    }

}
