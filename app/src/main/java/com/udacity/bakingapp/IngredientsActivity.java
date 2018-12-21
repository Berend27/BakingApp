package com.udacity.bakingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class IngredientsActivity extends AppCompatActivity {

    private String json;
    private int recipeNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);

        Button viewIntro = (Button) findViewById(R.id.view_intro);
        viewIntro.setVisibility(View.INVISIBLE);

        Intent ingredientsIntent = getIntent();
        json = ingredientsIntent.getExtras().getString(StepListActivity.JSON);
        recipeNumber = ingredientsIntent.getExtras().getInt(StepListActivity.INDEX);

        viewIntro.setVisibility(View.VISIBLE);

        String[] ingredients = NetworkingUtils.getIngredients(json, recipeNumber);

        ArrayAdapter<String> ingredientsAdapter
                = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ingredients);
        ListView listIngredients = (ListView) findViewById(R.id.ingredients_ListView);
        listIngredients.setAdapter(ingredientsAdapter);
    }

    public void launchIntro(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt(StepDetails.INDEX, recipeNumber);
        bundle.putString(StepDetails.JSON, json);
        bundle.putInt(StepDetails.STEP, 0);

        Intent intent = new Intent(this, StepDetails.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
