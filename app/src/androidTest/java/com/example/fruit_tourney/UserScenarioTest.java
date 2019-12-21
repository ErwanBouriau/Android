package com.example.fruit_tourney;

import android.view.Gravity;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

public class UserScenarioTest {

    @Rule
    public ActivityTestRule<LoginPage> activityRule =
            new ActivityTestRule<>(LoginPage.class);

    @Test
    public void userScenario() {
        // On connecte l'utilisateur
        onView(withId(R.id.btn_cnx)).check(matches(withText("Connexion")));
        onView(withId(R.id.btn_cnx)).perform(click());

        UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mDevice.pressBack();

        onView(withHint("E-mail")).perform(typeText("test@test.com"), closeSoftKeyboard());
        onView(withText("Suivant")).perform(click());

        onView(withHint("Mot de passe")).perform(typeText("test38"), closeSoftKeyboard());
        onView(withId(2131230810)).perform(click());

        onView(withId(R.id.btn_cnx)).perform(click());

        // On débute un tournoi à 8 fruits
        onView(withId(R.id.btn_8)).check(matches(withText("Commencer un tournoi à 8")));
        onView(withId(R.id.btn_16)).check(matches(withText("Commencer un tournoi à 16")));
        onView(withId(R.id.btn_8)).perform(click());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.fruit1)).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.fruit2)).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.fruit1)).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.fruit2)).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.fruit1)).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.fruit2)).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.fruit1)).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.boutonRetour)).perform(click());

        onView(withId(R.id.btn_8)).check(matches(withText("Commencer un tournoi à 8")));
        onView(withId(R.id.btn_16)).check(matches(withText("Commencer un tournoi à 16")));

        onView(withId(R.id.home_drawer))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open()); // Open Drawer

        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.menu_stats));

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.home_drawer))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open()); // Open Drawer

        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.menu_deco));

    }

}