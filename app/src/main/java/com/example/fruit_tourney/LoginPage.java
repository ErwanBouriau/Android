package com.example.fruit_tourney;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;


public class LoginPage extends AppCompatActivity {

    //FOR DATA
    // 1 - Identifier for Sign-In Activity
    private static final int RC_SIGN_IN = 123;

    // --------------------
    // ACTIONS
    // --------------------

    public void onClickLoginButton(View v) {
        // 3 - Launch Sign-In Activity when user clicked on Login Button
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else {
            this.startSignInActivity();
        }
    }

    public void onClickLogoutButton(View v) {
        FirebaseAuth.getInstance().signOut();
        finish();
        startActivity(new Intent(this, LoginPage.class));
    }

    // --------------------
    // NAVIGATION
    // --------------------

    // 2 - Launch Sign-In Activity
    private void startSignInActivity(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.AppTheme)
                        .setAvailableProviders(
                                Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build()))
                        .build(),
                RC_SIGN_IN);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login_page);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Button bouttonLogin = (Button) this.findViewById(R.id.btn_cnx);
        TextView texte = (TextView) this.findViewById(R.id.texte_co);
        Button bouttonLogout = (Button) this.findViewById(R.id.btn_deco);

        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            bouttonLogin.setText("JOUER !");
            texte.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            bouttonLogout.setVisibility(View.VISIBLE);
        }
        else {
            bouttonLogin.setText("Connexion");
            texte.setText("Non connecté");
            bouttonLogout.setVisibility(View.INVISIBLE);
        }
    }
}
