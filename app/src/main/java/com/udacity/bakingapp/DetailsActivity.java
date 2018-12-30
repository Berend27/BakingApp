package com.udacity.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.udacity.bakingapp.StepDetails.INDEX;
import static com.udacity.bakingapp.StepDetails.JSON;


public class DetailsActivity extends AppCompatActivity {

    private String[][] specificSteps;

    private String json;
    private int recipeNumber;
    private int step;
    private int currentWindow;

    private long playbackPosition;

    public static final String JSON = "json";
    public static final String INDEX = "index";
    public static final String STEP = "step";
    public static final String PLAYBACK = "playback";
    public static final String WINDOW = "window";
    private static final String TAG = DetailsActivity.class.getSimpleName();

    @BindView(R.id.step_details_toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent start = getIntent();
        json = start.getExtras().getString(JSON);
        recipeNumber = start.getExtras().getInt(INDEX);

        if (savedInstanceState != null) {
            step = savedInstanceState.getInt(STEP);
            playbackPosition = savedInstanceState.getLong(PLAYBACK, 0);
            currentWindow = savedInstanceState.getInt(WINDOW, 0);
        } else {
            step = start.getExtras().getInt(STEP);
            // Initialize the Fragment
            StepDetails detailsFragment = new StepDetails();
            FragmentManager fragmentManager = getSupportFragmentManager();
            detailsFragment.setStep(step);
            detailsFragment.setSpecificSteps(json, recipeNumber);
            fragmentManager.beginTransaction().add(R.id.details_fragment, detailsFragment).commit();
        }


    }
}
