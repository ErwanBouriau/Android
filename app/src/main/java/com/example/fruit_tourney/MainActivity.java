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
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

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

       // String pseudo = getIntent().getStringExtra("userLogin");
        configureNavigationViewHeader(/*pseudo*/);

    }

    private boolean manageNavigationViewItemClick(MenuItem item)
    {
        item.setChecked(true);
        drawerLayout.closeDrawers();

        if (item.getItemId() == R.id.menu_profile )
            startActivity(new Intent(this, ProfileActivity.class));

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

    public void configureNavigationViewHeader(/*String pseudo*/)
    {
        View viewheader = getLayoutInflater().inflate(R.layout.nav_header, null);

        // textViewLogin.setText(pseudo);

        navigationView.addHeaderView(viewheader);
    }
}
