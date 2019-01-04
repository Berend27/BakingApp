package com.udacity.bakingapp;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.BundleMatchers.hasEntry;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtras;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.not;




@RunWith(JUnit4.class)
public class RecipeCardsActivityTest {

    public final String TITLE = "Brownies";
    public final String crustPrep = "Prep the cookie crust.";

    // Adding the rule to indicate which activity to test
    // an IntentsTestRule is needed because of the RecyclerView and for intent verification
    @Rule public IntentsTestRule<RecipeCardsActivity> intentsTestRule
            = new IntentsTestRule<>(RecipeCardsActivity.class);

    // stub external intents to this activity (for practice purposes)
    @Before
    public void stubAllExternalIntents() {
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    @Test
    public void recipeCardTest()
    {
        // test clicking the second card, this line works
        onView(ViewMatchers.withId(R.id.recipe_cards))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1,
                        click()));

        Log.i("register-class_name", StepListActivity.class.getName());
        Log.i("register-short_name", StepListActivity.class.getSimpleName());
        Log.i("register-package_name", StepListActivity.class.getPackage().toString());

        // this line works
        intended(hasComponent(StepListActivity.class.getName()));
    }

    // this works too
    @Test
    public void selectedTitleTest() {
        // test clicking the second card, this line works
        onView(ViewMatchers.withId(R.id.recipe_cards))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1,
                        click()));

        // Verify an Intent is sent with the correct information in it (Intent Verification)
        //intended(assertThat(intent).extras().string(StepListActivity.TITLE).isEqualTo(TITLE));
        intended(allOf(hasExtras(hasEntry(StepListActivity.TITLE, TITLE))));
    }

    // Testing that when a step to make the cheese cake is selected, the fragment loads with proper data
    // Two fragments are tested, the step list fragment and the step details fragment
    @Test
    public void stepSelectionTest() {
        onView(ViewMatchers.withId(R.id.recipe_cards))
                .perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));

        // The step list fragment is added statically in XML, so it can be tested like an activity.
        // This fragment contains a ListView so onData is used
        onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(3).perform(click());

        // The next fragment is added dynamically, though it can be tested the same way
        onView(withId(R.id.step_title)).check(matches(withText(crustPrep)));

    }
}
