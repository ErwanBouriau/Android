package com.example.fruit_tourney;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;


/**
 * Correspond à la page de login où on peut se créer un compte grâce à Firebase
 * ou se connecter si l'on en possède déjà un.
 */
public class LoginPage extends AppCompatActivity {

    //FOR DATA
    // 1 - Identifier for Sign-In Activity
    private static final int RC_SIGN_IN = 123;

    // --------------------
    // ACTIONS
    // --------------------

    /**
     * Si l'utilisateur est déjà connecté, le redirige vers la 'MainActivity'
     * sinon appelle 'startSignInActivity'.
     * @param v Bouton 'connexion'
     */
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

    /**
     * Déconnecte l'utilisateur et rafraichit la page.
     * @param v Bouton 'deconnexion'
     */
    public void onClickLogoutButton(View v) {
        FirebaseAuth.getInstance().signOut();
        finish();
        startActivity(new Intent(this, LoginPage.class));
    }

    // --------------------
    // NAVIGATION
    // --------------------

    /**
     *Ouvre une page de création de compte auto-géré par Firebase qui va ajouter l'utilisateur
     * dans la section authentification de Firebase.
     */
    private void startSignInActivity(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.EmailBuilder().build()))
                        .build(),
                RC_SIGN_IN);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login_page);
    }

    /**
     * Lors du rechargement de la page, si l'utilisateur est connecté, le bouton 'connexion' devient 'JOUER !',
     * son nom est affiché et le bouton 'déconnexion' apparaît. Sinon 'non connecté' est affiché.
     */
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
