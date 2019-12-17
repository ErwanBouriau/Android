package com.example.fruit_tourney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Correspond à la page d'accueil principale où on retrouve le drawer avec l'accès à la page profil, statistique
 * et la possibilité de se déconnecter. On retrouve aussi l'accès aux tournois 8 et 16 fruits.
 */
public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.home_drawer);
        navigationView = findViewById(R.id.nav_view);


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

    }

    /**
     * Redirige vers la page 'TournoiActivity' et commence un tournoi à 8 fruits.
     * @param v Bouton tournoi 8
     */
    public void onClickTournoi8(View v) {
            Intent intent = new Intent(this, TournoiActivity.class);
            intent.putExtra("tailleTournoi", 8);
            startActivity(intent);
    }

    /**
     * Redirige vers la page 'TournoiActivity' et commence un tournoi à 16 fruits.
     * @param v Bouton tournoi 16
     */
    public void onClickTournoi16(View v) {
        Intent intent = new Intent(this, TournoiActivity.class);
        intent.putExtra("tailleTournoi", 16);
        startActivity(intent);
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

        if (item.getItemId() == R.id.menu_profile )
            startActivity(new Intent(this, ProfileActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

        if (item.getItemId() == R.id.menu_stats )
            startActivity(new Intent(this, StatsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

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
