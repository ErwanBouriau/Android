package com.example.fruit_tourney;

import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withInputType;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

public class LoginPageTest {

    @Rule
    public ActivityTestRule<LoginPage> activityRule =
            new ActivityTestRule<>(LoginPage.class);

    @Test
    public void logUser() {
        onView(withId(R.id.btn_cnx)).check(matches(withText("Connexion")));

        onView(withId(R.id.btn_cnx)).perform(click());


        onView(withHint("E-mail")).perform(typeText("blabla"), closeSoftKeyboard());

    }
}