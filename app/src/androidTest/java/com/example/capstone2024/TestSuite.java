package com.example.capstone2024;

import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

import com.example.capstone2024.ui.home.HomeActivity;
import com.example.capstone2024.ui.usersetup.UserSetupActivity;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class TestSuite {

    @Rule
    public ActivityScenarioRule<UserSetupActivity> activityRule =
            new ActivityScenarioRule<>(UserSetupActivity.class);

    @Test
    public void testUserSetupActivity() {
        // Fill in the user details

        // Fill in the Name field
        onView(withId(R.id.answer1))
                .perform(typeText("John Doe"), ViewActions.closeSoftKeyboard());

        // Select an age from the spinner
        onView(withId(R.id.answer2))
                .perform(click());
        onData(anything()).inAdapterView(withId(android.R.id.list))
                .atPosition(25)
                .perform(click());

        // Fill in the current weight
        onView(withId(R.id.answer3))
                .perform(typeText("150"), ViewActions.closeSoftKeyboard());

        // Fill in the target weight
        onView(withId(R.id.answer4))
                .perform(typeText("140"), ViewActions.closeSoftKeyboard());

        // Select workout level
        onView(withId(R.id.answer5))
                .perform(click());
        onView(withText("Moderate"))
                .perform(click());

        // Select equipment
        onView(withId(R.id.answer7))
                .perform(click());
        onView(withText("Dumbbells"))
                .perform(click());

        // Select target body parts
        onView(withId(R.id.answer6Biceps))
                .perform(click());
        onView(withId(R.id.answer6Triceps))
                .perform(click());

        // Submit the form
        onView(withId(R.id.submitButton))
                .perform(click());

        // Verify if the HomeActivity is launched
        intended(hasComponent(HomeActivity.class.getName()));

        // Verify that Toast message appears for validation error (if any)
        // You can simulate a validation failure by leaving some fields empty and checking the error
        //onView(withText("This field is required"))
        //        .inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView()))))
        //        .check(matches(isDisplayed()));
    }

    @Test
    public void testEmptyFieldsValidation() {
        // Submit without filling any fields
        onView(withId(R.id.submitButton))
                .perform(click());

        // Check if validation error is shown (e.g., "This field is required")
        //onView(withText("This field is required"))
        //        .inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView()))))
        //        .check(matches(isDisplayed()));
    }
}
