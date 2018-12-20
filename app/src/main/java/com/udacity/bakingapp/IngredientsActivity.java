package com.udacity.bakingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
// TODO stopping point 12/19
public class IngredientsActivity extends AppCompatActivity {

    private String json;
    private int recipeNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);

        Intent ingredientsIntent = getIntent();
        json = ingredientsIntent.getExtras().getString(StepListActivity.JSON);
        recipeNumber = ingredientsIntent.getExtras().getInt(StepListActivity.INDEX);

        String[] ingredients = NetworkingUtils.getIngredients(json, recipeNumber);

        ArrayAdapter<String> ingredientsAdapter
                = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ingredients);
        ListView listIngredients = (ListView) findViewById(R.id.ingredients_ListView);
        listIngredients.setAdapter(ingredientsAdapter);
    }
}
