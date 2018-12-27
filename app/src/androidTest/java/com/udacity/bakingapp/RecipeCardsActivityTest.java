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
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.BundleMatchers.hasEntry;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtras;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.not;




@RunWith(JUnit4.class)
public class RecipeCardsActivityTest {

    public final String TITLE = "Brownies";

    // Adding the rule to indicate which activity to test
    @Rule public IntentsTestRule<RecipeCardsActivity> intentsTestRule
            = new IntentsTestRule<>(RecipeCardsActivity.class);

    // stub external intents to this activity
    @Before
    public void stubAllExternalIntents() {
        //intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
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

        // onData(anything()).inAdapterView(withId(R.id.recipe_cards)).atPosition(1).perform(click());

        /*
        intended(allOf(
                hasComponent(hasShortClassName(".bakingapp.StepListActivity")),
                toPackage("com.udacity.bakingapp")
        ));
        */



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
}
