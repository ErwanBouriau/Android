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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class ProfileActivity extends BaseActivity {

    private EditText fieldName;
    private EditText fieldEmail;
    private EditText fieldPassword;
    private EditText fieldPasswordAgain;

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
        fieldPasswordAgain = findViewById(R.id.field_passAgain);


        profileName.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

        fieldName.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        fieldEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

    }

    public void onClickDeleteButton(View v) {
        if (fieldPassword.getText().toString().isEmpty()) {
            Toast toast = Toast.makeText(getApplicationContext(), "ENTREZ VOTRE MDP", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Création des credentials pour la reauthentification
        AuthCredential credential = EmailAuthProvider
                .getCredential(user.getEmail(), fieldPassword.getText().toString());

        // Reauthentification de l'utilisateur pour la suppression du compte
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // si la reauthentification passe
                        if (task.isSuccessful()) {
                            user.delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            // si le compte est supprimé
                                            if (task.isSuccessful()) {
                                                Toast toast = Toast.makeText(getApplicationContext(), "COMPTE SUPPRIMÉ", Toast.LENGTH_SHORT);
                                                toast.show();
                                                Intent intent = new Intent(getApplicationContext(), LoginPage.class);
                                                startActivity(intent);
                                            }
                                            // si le compte n'a pas pu etre supprimé
                                            else {
                                                Toast toast = Toast.makeText(getApplicationContext(), "ERREUR DE SUPPRESSION", Toast.LENGTH_SHORT);
                                                toast.show();
                                            }
                                        }
                                    });
                        }
                        // Erreur de reauthentification
                        else {
                            Toast toast = Toast.makeText(getApplicationContext(), "MDP INCORRECT", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                });
    }

    public void onClickUpdateButton(View v) {
        if (fieldPassword.getText().toString().isEmpty()) {
            Toast toast = Toast.makeText(getApplicationContext(), "ENTREZ VOTRE MDP", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if (fieldPassword.getText().toString().equals(fieldPasswordAgain.getText().toString())) {
            Toast toast = Toast.makeText(getApplicationContext(), "MDP IDENTIQUE AU PRECEDENT", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Création des credentials pour la reauthentification
        AuthCredential credential = EmailAuthProvider
                .getCredential(user.getEmail(), fieldPassword.getText().toString());

        // Reauthentification de l'utilisateur pour la suppression du compte
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // si la reauthentification passe
                        if (task.isSuccessful()) {
                            // si le champ nom n'est pas vide et différent du nom actuel
                            if (!fieldName.getText().toString().isEmpty() && !fieldName.getText().toString().equals(user.getDisplayName())) {
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(fieldName.getText().toString()).build();

                                user.updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d("update_name", "Name updated.");
                                                }
                                                else {
                                                    Toast toast = Toast.makeText(getApplicationContext(), "ERREUR", Toast.LENGTH_SHORT);
                                                    toast.show();
                                                    Log.e("error_update_name", "Name update failed.");
                                                }
                                            }
                                        });
                            }
                            // si le champ mail n'est pas vide et différent du mail actuel
                            if (!fieldEmail.getText().toString().isEmpty() && !fieldEmail.getText().toString().equals(user.getEmail())) {
                                user.updateEmail(fieldEmail.getText().toString())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d("update_mail", "User email address updated.");
                                                }
                                                else {
                                                    Toast toast = Toast.makeText(getApplicationContext(), "ERREUR", Toast.LENGTH_SHORT);
                                                    toast.show();
                                                    Log.d("error_update_mail", "User email address update failed.");
                                                }
                                            }
                                        });
                            }
                            if (!fieldPasswordAgain.getText().toString().isEmpty()){
                                    user.updatePassword(fieldPasswordAgain.getText().toString())
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d("update_password", "User password updated.");
                                                        startActivity(new Intent(getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                                    }
                                                    else {
                                                        Toast toast = Toast.makeText(getApplicationContext(), "ERREUR", Toast.LENGTH_SHORT);
                                                        toast.show();
                                                        Log.d("error_update_password", "User password update failed");
                                                    }
                                                }
                                            });
                                }
                            if (fieldPasswordAgain.getText().toString().isEmpty()) {
                                startActivity(new Intent(getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                Log.d("update_profile", "User profile successfully updated.");
                            }
                        }
                        // Erreur de reauthentification
                        else {
                            Toast toast = Toast.makeText(getApplicationContext(), "MDP INCORRECT", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                });
    }

    private boolean manageNavigationViewItemClick(MenuItem item)
    {
        item.setChecked(true);
        drawerLayout.closeDrawers();

        if (item.getItemId() == R.id.menu_home )
            startActivity(new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

        if (item.getItemId() == R.id.menu_stats )
            startActivity(new Intent(this, StatsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));


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
