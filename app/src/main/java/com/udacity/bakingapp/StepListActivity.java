package com.udacity.bakingapp;

import android.content.Context;
import android.content.Intent;
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

public class StepListActivity extends AppCompatActivity {

    public static final String STEPS_KEY = "steps";
    public static final String TITLE = "title";
    public static final String INDEX = "index";
    public static final String JSON = "json";

    private final Context context = this;

    String[] steps;
    private String title;
    private String json;
    private int index;
    private Bundle instanceState;

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

        if (savedInstanceState == null)
        {
            StepListFragment listFragment = (StepListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.step_list);
            // call setter methods for json, index number, and title
            listFragment.setUpListFragment(json, index, title, steps);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        savedInstanceState.putString(TITLE, title);
        savedInstanceState.putStringArray(STEPS_KEY, steps);
        super.onSaveInstanceState(savedInstanceState);
    }
}
