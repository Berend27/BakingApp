package com.udacity.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepListActivity extends AppCompatActivity implements StepListFragment.Listener {

    public static final String STEPS_KEY = "steps";
    public static final String TITLE = "title";
    public static final String INDEX = "index";
    public static final String JSON = "json";
    public static final String STEP = "step";
    public static final String PLAYBACK = "playback";
    public static final String WINDOW = "window";
    private static final String TAG = DetailsActivity.class.getSimpleName();

    private final Context context = this;

    String[] steps;
    private String title;
    private String json;
    private int index;
    private int recipeNumber;
    private int step;
    private int currentWindow;

    private long playbackPosition;

    private StepDetails detailsFragment;
    private IngredientsFragment ingredientsFragment;
    private FragmentManager fragmentManager;

    private boolean tablet;
    private boolean detailsDisplayed;

    // @BindView(R.id.step_list_toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_list);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.step_list_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        try {
            title = intent.getExtras().getString(TITLE);
            steps = intent.getExtras().getStringArray(STEPS_KEY);
            index = intent.getExtras().getInt(INDEX);
            json = intent.getExtras().getString(JSON);
        } catch(NullPointerException ne) {
            if (savedInstanceState != null)
            {
                title = savedInstanceState.getString(TITLE);
                steps = savedInstanceState.getStringArray(STEPS_KEY);
            }
        }
        recipeNumber = index;

        if (findViewById(R.id.details_or_ingredients_fragment) != null)  // if a tablet is being used
        {
            tablet = true;
            if (savedInstanceState == null) {
                StepListFragment listFragment = (StepListFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.step_list);
                // call setter methods for json, index number, and title
                listFragment.setUpListFragment(json, index, title, steps);

                step = 0;
                detailsDisplayed = false;
                // Initialize the Fragment
                ingredientsFragment = new IngredientsFragment();
                fragmentManager = getSupportFragmentManager();
                ingredientsFragment.setVariables(json, index);
                fragmentManager.beginTransaction().add(R.id.details_or_ingredients_fragment, ingredientsFragment).commit();
            } else {
                step = savedInstanceState.getInt(STEP);
                playbackPosition = savedInstanceState.getLong(PLAYBACK, 0);
                currentWindow = savedInstanceState.getInt(WINDOW, 0);
            }
        }
        else
        {
            tablet = false;
            if (savedInstanceState == null) {
                StepListFragment listFragment = (StepListFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.step_list);
                // call setter methods for json, index number, and title
                listFragment.setUpListFragment(json, index, title, steps);
            }
        }



    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        savedInstanceState.putString(TITLE, title);
        savedInstanceState.putStringArray(STEPS_KEY, steps);
        savedInstanceState.putInt(STEP, step);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void itemClicked(int position) {
        final String FINAL_JSON = json;
        Intent detailedIntent;
        if (position == 0) {   // if "Ingredients" is selected
            detailsDisplayed = false;
            detailedIntent = new Intent(context, IngredientsActivity.class);
        }
        else {
            detailsDisplayed = true;
            detailedIntent = new Intent(context, DetailsActivity.class);
            step = position - 1;    // since "Ingredients" takes up position 0
        }
        if (tablet)
        {
            if (detailsDisplayed) {
                detailsFragment = new StepDetails();
                detailsFragment.setStep(step);
                detailsFragment.setSpecificSteps(json, recipeNumber);
                fragmentManager.beginTransaction().replace(R.id.details_or_ingredients_fragment, detailsFragment).commit();
            } else {
                ingredientsFragment.setVariables(json, recipeNumber);
                fragmentManager.beginTransaction().replace(R.id.details_or_ingredients_fragment, ingredientsFragment).commit();
            }
        }
        else {
            Bundle selected = new Bundle();
            selected.putString(JSON, FINAL_JSON);
            selected.putInt(INDEX, index);
            selected.putInt(StepDetails.STEP, position);
            detailedIntent.putExtras(selected);
            startActivity(detailedIntent);
        }

    }
}
