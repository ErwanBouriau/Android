package com.example.fruit_tourney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends BaseActivity {

    private EditText fieldName;
    private EditText fieldEmail;
    private EditText fieldPassword;

    private TextView profileName;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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


        profileName = findViewById(R.id.title_profile);
        fieldName = findViewById(R.id.field_name);
        fieldEmail = findViewById(R.id.field_email);
        fieldPassword = findViewById(R.id.field_pass);

        profileName.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

        fieldName.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        fieldEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

    }

    public void onClickDeleteButton(View v) {
        UserHelper.deleteUser(FirebaseAuth.getInstance().getCurrentUser().getUid()).addOnFailureListener(this.onFailureListener());
        Intent intent = new Intent(this, LoginPage.class);
        startActivity(intent);
    }

    public void onClickUpdateButton(View v) {
        String username = fieldName.getText().toString();
        String email = fieldEmail.getText().toString();
        String password = fieldPassword.getText().toString();
        if(!username.isEmpty()) {
            UserHelper.updateUsername(username, FirebaseAuth.getInstance().getUid());
        }
        if(!email.isEmpty()) {
            UserHelper.updateEmail(email, FirebaseAuth.getInstance().getUid());
        }
        if(!password.isEmpty()) {
            UserHelper.updatePassword(password, FirebaseAuth.getInstance().getUid());
        }
    }

    private boolean manageNavigationViewItemClick(MenuItem item)
    {
        item.setChecked(true);
        drawerLayout.closeDrawers();

        if (item.getItemId() == R.id.menu_home )
            startActivity(new Intent(this, MainActivity.class));

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
