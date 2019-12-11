package com.example.fruit_tourney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StatsActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FlexboxLayout fruitLayout;

    private ArrayList<StorageReference> allReferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        this.allReferences = new ArrayList<>();

        drawerLayout = findViewById(R.id.home_drawer);
        navigationView = findViewById(R.id.nav_view);
        fruitLayout = findViewById(R.id.fruits_layout);


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
                    stat.setText("100%");

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

    public void configureNavigationViewHeader()
    {
        View viewheader = getLayoutInflater().inflate(R.layout.nav_header, null);
        TextView textViewLogin = viewheader.findViewById(R.id.navdrawer_name);

        textViewLogin.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

        navigationView.addHeaderView(viewheader);
    }
}
