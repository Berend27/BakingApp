package com.udacity.bakingapp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class IngredientsActivity extends AppCompatActivity
        implements IngredientsFragment.ViewIntroListener{

    private String json;
    private int recipeNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);

        Intent ingredientsIntent = getIntent();
        json = ingredientsIntent.getExtras().getString(StepListActivity.JSON);
        recipeNumber = ingredientsIntent.getExtras().getInt(StepListActivity.INDEX);

        if (savedInstanceState == null) {
            IngredientsFragment ingredientsFragment = new IngredientsFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            ingredientsFragment.setVariables(json, recipeNumber);
            fragmentManager.beginTransaction().add(R.id.ingredients_fragment, ingredientsFragment).commit();
        }
    }

    @Override
    public void introButtonClicked() {
        Bundle selected = new Bundle();
        selected.putString(StepDetails.JSON, json);
        selected.putInt(StepDetails.INDEX, recipeNumber);
        selected.putInt(StepDetails.STEP, 0);
        Intent detailedIntent = new Intent(this, DetailsActivity.class);
        detailedIntent.putExtras(selected);
        startActivity(detailedIntent);
    }
}
