package com.example.fruit_tourney;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;

public abstract class BaseActivity extends AppCompatActivity {

    protected OnFailureListener onFailureListener(){
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), getString(R.string.message_erreur_inconnue), Toast.LENGTH_LONG).show();
            }
        };
    }
}
