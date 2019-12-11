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
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;


public class StatsActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        drawerLayout = findViewById(R.id.home_drawer);
        navigationView = findViewById(R.id.nav_view);
        FlexboxLayout fruitLayout = findViewById(R.id.fruits_layout);


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

        for(int i=1; i<=10; i++) {
            LinearLayout linearTMP = (LinearLayout) getLayoutInflater().inflate(R.layout.stats_fruit_layout,null);

            TextView name = linearTMP.findViewById(R.id.fruit_name);
            name.setText("Salade");

            TextView stat = linearTMP.findViewById(R.id.fruit_stat);
            stat.setText("100%");

            ImageView img = linearTMP.findViewById(R.id.imageview_stat_fruit);
            img.setImageResource(R.drawable.salade_de_fruit);
            fruitLayout.addView(linearTMP);
        }

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
