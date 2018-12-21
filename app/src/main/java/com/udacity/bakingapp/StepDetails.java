package com.udacity.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
// TODO stopping point 12/20  The buttons work, still need an ExoPlayer
public class StepDetails extends AppCompatActivity {

    @BindView(R.id.step_title) TextView stepTitle;
    @BindView(R.id.detailedStep) TextView stepDescription;
    @BindView(R.id.step_details_toolbar) Toolbar toolbar;
    @BindView(R.id.next_step) Button nextButton;
    @BindView(R.id.previous_step) Button previous;

    public static final String JSON = "json";
    public static final String INDEX = "index";
    public static final String STEP = "step";

    private String[][] specificSteps;

    private String json;
    private int recipeNumber;
    private int step;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_details);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent start = getIntent();
        json = start.getExtras().getString(JSON);
        recipeNumber = start.getExtras().getInt(INDEX);
        step = start.getExtras().getInt(STEP);

        if (step == NetworkingUtils.getNumberOfSteps(json, recipeNumber) - 1)
            nextButton.setVisibility(View.INVISIBLE);
        else
            nextButton.setVisibility(View.VISIBLE);
        if (step == 0)
        {
            previous.setText(R.string.Ingredients);
        }
        else if (step == 1)
            previous.setText(R.string.Introduction);
        else
            previous.setText(R.string.previous_step);

        specificSteps = NetworkingUtils.getSpecificSteps(json, recipeNumber);
        stepTitle.setText(specificSteps[1][step]);
        stepDescription.setText(specificSteps[2][step]);
    }

    public void previousStep(View view) {
        if (step > 0) {
            step = step - 1;
            stepTitle.setText(specificSteps[1][step]);
            stepDescription.setText(specificSteps[2][step]);
            if (step == 0)
                previous.setText(R.string.Ingredients);
            else if (step == 1)
                previous.setText(R.string.Introduction);
        }
        else {
            Intent intent = new Intent(this, IngredientsActivity.class);
            Bundle selected = new Bundle();
            selected.putString(JSON, json);
            selected.putInt(INDEX, recipeNumber);
            selected.putInt(StepDetails.STEP, step);
            intent.putExtras(selected);
            startActivity(intent);
        }

        if (step < NetworkingUtils.getNumberOfSteps(json, recipeNumber) - 1)
            nextButton.setVisibility(View.VISIBLE);
    }

    public void nextStep(View view) {
        if (step < NetworkingUtils.getNumberOfSteps(json, recipeNumber) - 1) {
            step = step + 1;
            if (step < NetworkingUtils.getNumberOfSteps(json, recipeNumber) - 1) {
                nextButton.setVisibility(View.VISIBLE);
            } else {
                nextButton.setVisibility(View.INVISIBLE);
            }
            stepTitle.setText(specificSteps[1][step]);
            stepDescription.setText(specificSteps[2][step]);
        }

        if (step > 1)
            previous.setText(R.string.previous_step);
        else if (step == 1)
            previous.setText(R.string.Introduction);
    }
}
