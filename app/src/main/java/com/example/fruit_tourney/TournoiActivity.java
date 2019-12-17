package com.example.fruit_tourney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Correspond à la page du tournoi de fruit où on aura la possibilité de choisir entre deux fruits
 * en cliquant sur son préféré. Un texte indique la position dans le tournoi.
 */
public class TournoiActivity extends AppCompatActivity {
    private ArrayList<StorageReference> allReferences;
    private ArrayList<StorageReference> selected;
    private int compteur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournoi);
        this.allReferences = new ArrayList<>();
        this.selected = new ArrayList<>();
        compteur = 0;

        TextView round = this.findViewById(R.id.round);
        if(getIntent().getIntExtra("tailleTournoi", 8) == 16) {
            round.setText("Round 1 - Huitième de finale");
        }

        initialize();

    }


    /**
     * Initialise le tableau 'allReferences' avec toutes les références des images de fruits
     */
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

    /**
     * Prend les deux premières références d'images du tableau 'selected' et les charges dans 'fruit1' et 'fruit2'
     */
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

    /**
     * Remplit aléatoirement le tableau 'selected' avec des références prit dans 'allRefs'
     * avec 8 ou 16 fruits suivant le tournoi que l'on a choisit.
     * @param allRefs Tableau contenant toutes les références d'images des fruits
     */
    public void remplirSelected(ArrayList<StorageReference> allRefs) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i=0; i<25; i++) {
            list.add(i);
        }
        Collections.shuffle(list);
        if(getIntent().getIntExtra("tailleTournoi", 8) == 8) {
            for (int i=0; i<8; i++) {
                selected.add(allRefs.get(list.get(i)));
            }
        }
        else {
            for (int i=0; i<16; i++) {
                selected.add(allRefs.get(list.get(i)));
            }
        }

    }


    /**
     * Ajoute dans la base de donnée Firestore une nouvelle ligne pour la victoire du fruit avec l'id de l'utilisateur
     * @param fruit Correspond au fruit qui a gagné le tournoi
     */
    public void addVictory(String fruit) {
        String fruitId = "failed";
        // On récupère le nom du fruit dans la référence avec du regex
        Pattern pattern = Pattern.compile("\\/\\w+.(?=\\.)");
        Matcher matcher = pattern.matcher(fruit);
        if (matcher.find())
        {
            fruitId = matcher.group(0);
            // On supprime le /
            fruitId = fruitId.substring(1);
        }

    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    // Create a new user with a first and last name
    Map<String, Object> victoire = new HashMap<>();
        victoire.put("IdFruit", fruitId);
                victoire.put("IdUser", FirebaseAuth.getInstance().getCurrentUser().getUid());

                // Add a new document with a generated ID
                db.collection("Victoires")
                .add(victoire)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                            Log.d("success_add_victories", "DocumentSnapshot added with ID: " + documentReference.getId());
                            }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                            Log.e("err_add_victory", "Error adding document", e);
                            }
                });
    }

    /**
     * Sauvegarde le fruit sur lequel on a cliqué en passant sa référence à la fin du tableau 'selected'
     * puis supprime les deux premières refs pour pouvoir 'loadImage' sur les prochains fruits.
     * Incrémente aussi le compteur de tour pour afficher le texte correspondant.
     * Lorsque le compteur indique la fin du tournoi, 'addVictory' est appellé sur le fruit vainqueur
     * et un bouton 'retour à l'accueil' apparaît.
     * @param v Correspond à la view de l'image sur laquelle on clique
     */
    public void onClickImage(View v) {
        TextView round = this.findViewById(R.id.round);
        compteur++;
        if(getIntent().getIntExtra("tailleTournoi", 8) == 8) {
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
                Button bouton = this.findViewById(R.id.boutonRetour);
                bouton.setVisibility(View.VISIBLE);
                image1.setVisibility(View.INVISIBLE);
                image2.setVisibility(View.INVISIBLE);
                imageFinale.getLayoutParams().height = 700;
                imageFinale.getLayoutParams().width = 700;
                imageFinale.requestLayout();
                if(v.getId() == R.id.fruit1) {
                    GlideApp.with(this)
                            .load(selected.get(0))
                            .into(imageFinale);
                    addVictory(String.valueOf(selected.get(0)));
                }
                else {
                    GlideApp.with(this)
                            .load(selected.get(1))
                            .into(imageFinale);
                    addVictory(String.valueOf(selected.get(1)));
                }
            }
        }
        else {
            switch (compteur){
                case 1:
                    round.setText("Round 2 - Huitième de finale");
                    break;
                case 2:
                    round.setText("Round 3 - Huitième de finale");
                    break;
                case 3:
                    round.setText("Round 4 - Huitième de finale");
                    break;
                case 4:
                    round.setText("Round 5 - Huitième de finale");
                    break;
                case 5:
                    round.setText("Round 6 - Huitième de finale");
                    break;
                case 6:
                    round.setText("Round 7 - Huitième de finale");
                    break;
                case 7:
                    round.setText("Round 8 - Huitième de finale");
                    break;
                case 8:
                    round.setText("Round 1 - Quart de finale");
                    break;
                case 9:
                    round.setText("Round 2 - Quart de finale");
                    break;
                case 10:
                    round.setText("Round 3 - Quart de finale");
                    break;
                case 11:
                    round.setText("Round 4 - Quart de finale");
                    break;
                case 12:
                    round.setText("Round 1 - Demi-finale");
                    break;
                case 13:
                    round.setText("Round 2 - Demi-finale");
                    break;
                case 14:
                    round.setText("Finale");
                    break;
                case 15:
                    round.setText("Vainqueur");
                    break;
            }
            if(compteur < 15){
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
                Button bouton = this.findViewById(R.id.boutonRetour);
                bouton.setVisibility(View.VISIBLE);
                image1.setVisibility(View.INVISIBLE);
                image2.setVisibility(View.INVISIBLE);
                imageFinale.getLayoutParams().height = 700;
                imageFinale.getLayoutParams().width = 700;
                imageFinale.requestLayout();
                if(v.getId() == R.id.fruit1) {
                    GlideApp.with(this)
                            .load(selected.get(0))
                            .into(imageFinale);
                    addVictory(String.valueOf(selected.get(0)));
                }
                else {
                    GlideApp.with(this)
                            .load(selected.get(1))
                            .into(imageFinale);
                    addVictory(String.valueOf(selected.get(1)));
                }

            }

        }

    }

    /**
     * Fait un retour vers la page d'accueil une fois le tournoi fini
     * @param v Correspond à la view du boutton
     */
    public void onClickBoutonRetour(View v) {
        Intent intent = new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}

