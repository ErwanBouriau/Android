package com.example.fruit_tourney;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ProfileActivityTest {

    @Rule
    public ActivityTestRule<ProfileActivity> activityRule =
            new ActivityTestRule<>(ProfileActivity.class);

    @Test
    public void noPassword() {
//        onView(withId(R.id.btn_update))
//                .perform(typeText("blabla"), closeSoftKeyboard());

//        onView(withId(R.id.btn_update)).perform(click());
//
//        onView(withText("ENTREZ VOTRE MDP")).check(matches(isDisplayed()));

    }

}