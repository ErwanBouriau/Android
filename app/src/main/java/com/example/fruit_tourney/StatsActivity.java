package com.example.fruit_tourney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Page des statistiques où on voit le pourcentage de réussite de chaque fruit
 */
public class StatsActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FlexboxLayout fruitLayout;
    private TextView titleStats;
    private Button buttonChange;

    private ArrayList<StorageReference> allReferences;
    private CollectionReference fruitsRef;
    private Query query;

    private int gameCount;
    private ArrayList<String> allVictories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        allReferences = new ArrayList<>();
        allVictories = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        fruitsRef = db.collection("Victoires");
        query = fruitsRef.whereEqualTo("IdUser", FirebaseAuth.getInstance().getCurrentUser().getUid());

        drawerLayout = findViewById(R.id.home_drawer);
        navigationView = findViewById(R.id.nav_view);
        fruitLayout = findViewById(R.id.fruits_layout);
        titleStats = findViewById(R.id.title_stats);
        buttonChange = findViewById(R.id.btn_change);

        if (getIntent().getBooleanExtra("stats_persos", true)) {
            buttonChange.setText("globales");
            titleStats.setText("Mes Statistiques");
        } else {
            buttonChange.setText("mes stats");
            titleStats.setText("Statistiques Globales");
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_menu_burger);

        /**
         * NavigationView
         */

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                return manageNavigationViewItemClick(menuItem);
            }
        })  ;

        configureNavigationViewHeader();
        initializeStats();
    }

    /**
     * Initialise le tableau 'allVictories' avec le nom de chaque fruit
     * et 'gameCount' avec le nombre de partie jouée par l'utilisateur
     */
    public void initializeStats() {
        // on vérifie si les stats à afficher sont persos ou non
        if (getIntent().getBooleanExtra("stats_persos", true)) {
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        gameCount = task.getResult().size();
                        // On rempli le tableau avec les fruits victorieux
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            allVictories.add(document.getData().get("IdFruit").toString());
                            Log.d("datas_get", allVictories.toString());
                            // On initialise les cartes
                        }
                        initializeCards(allVictories);
                    } else {
                        Log.d("pouet", "Error getting documents: ", task.getException());
                    }
                }
            });
        }
        else {
            fruitsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        // on vide le tableau des victoires
                        allVictories.clear();

                        gameCount = task.getResult().size();
                        // On rempli le tableau avec les fruits victorieux
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            allVictories.add(document.getData().get("IdFruit").toString());
                            Log.d("datas_get", allVictories.toString());
                            // On initialise les cartes
                        }
                        initializeCards(allVictories);
                    } else {
                        Log.d("pouet", "Error getting documents: ", task.getException());
                    }
                }
            });
        }
    }

    /**
     * Affiche toutes les cartes de fruits avec leurs images et noms correspondants
     * ainsi que le pourcentage de victoire par rapport à 'gameCount'
     * @param tabVictoires Correspond au tableau 'allVictories'
     */
    public void initializeCards(final ArrayList<String> tabVictoires) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        final DecimalFormat df = new DecimalFormat("##.##");
        storageRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference image : listResult.getItems()) {
                    allReferences.add(image);
                }

                for(int i=0; i < allReferences.size(); i++) {
                    LinearLayout linearTMP = (LinearLayout) getLayoutInflater().inflate(R.layout.stats_fruit_layout,null);

                    // On récupère le nom du fruit dans la référence avec du regex
                    String fruitName = "fruit";
                    Pattern pattern = Pattern.compile("\\/\\w+.(?=\\.)");
                    Matcher matcher = pattern.matcher(String.valueOf(allReferences.get(i)));
                    if (matcher.find())
                    {
                        fruitName = matcher.group(0);
                        // On supprime le /
                        fruitName = fruitName.substring(1);
                    }
                    TextView name = linearTMP.findViewById(R.id.fruit_name);
                    name.setText(fruitName);

                    TextView stat = linearTMP.findViewById(R.id.fruit_stat);
                    int occurrences = Collections.frequency(tabVictoires, fruitName);
                    if (occurrences == 0) {
                        stat.setText("0%");
                    }
                    else {
                        stat.setText(df.format((float) occurrences / (float) gameCount * 100) + "%");
                    }

                    ImageView img = linearTMP.findViewById(R.id.imageview_stat_fruit);
                    GlideApp.with(getApplicationContext())
                            .load(allReferences.get(i))
                            .into(img);
                    fruitLayout.addView(linearTMP);

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("listeAll failed !");
            }
        });
    }

    public void onClickStatsChange(View v) {
        Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

        if (getIntent().getBooleanExtra("stats_persos", true)) {
            // On recharge l'activité
            intent.putExtra("stats_persos", false);
            finish();
            startActivityForResult(intent, 0);
            overridePendingTransition(0,0); //0 for no animation
        } else {
            // On recharge l'activité
            intent.putExtra("stats_persos", true);
            finish();
            startActivityForResult(intent, 0);
            overridePendingTransition(0,0); //0 for no animation
        }
    }

    /**
     * Gère les cliques au sein du drawer
     * @param item Correspond aux textes des différentes pages du drawer sur lesquelles on clique.
     * @return retourne true
     */
    private boolean manageNavigationViewItemClick(MenuItem item)
    {
        item.setChecked(true);
        drawerLayout.closeDrawers();

        if (item.getItemId() == R.id.menu_home )
            startActivity(new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

        if (item.getItemId() == R.id.menu_profile )
            startActivity(new Intent(this, ProfileActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

        if (item.getItemId() == R.id.menu_deco) {
            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(this, LoginPage.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }

        return true;

    }

    /**
     * Gère l'ouverture et la fermeture du drawer
     * @param item icone menu
     * @return renvoie un booléen suivant si le drawer est ouvert ou pas.
     */
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home) {
            if(drawerLayout.isDrawerOpen(navigationView) == false){
                drawerLayout.openDrawer(GravityCompat.START);
            }
            else {
                drawerLayout.closeDrawer(GravityCompat.START);
            }

        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Gère l'affichage au sein du drawer et affiche le nom de l'utilisateur.
     */
    public void configureNavigationViewHeader()
    {
        View viewheader = getLayoutInflater().inflate(R.layout.nav_header, null);
        TextView textViewLogin = viewheader.findViewById(R.id.navdrawer_name);

        textViewLogin.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

        navigationView.addHeaderView(viewheader);
    }
}
