package com.udacity.bakingapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.Arrays;

public class RecipeCardsActivity extends AppCompatActivity
    implements RecipeCardAdapter.RecipeCardClickListener {

    private RecipeCardAdapter recipesAdapter;
    private RecyclerView recipeCards;
    private FetchRecipes fetch = new FetchRecipes();
    private int numberOfRecipes = 4;

    static final String QUERY = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    private String jsonFromQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_cards);

        recipeCards = (RecyclerView) findViewById(R.id.recipe_cards);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recipeCards.setLayoutManager(layoutManager);
        recipeCards.setHasFixedSize(true);

        recipesAdapter = new RecipeCardAdapter(numberOfRecipes, this);
        // the adapter is set in onPostExecute of the AsyncTask
        fetch.execute(QUERY);

    }

    @Override
    public void onListItemClicked(int clickedItemIndex) {
        Toast.makeText(this, String.valueOf(clickedItemIndex), Toast.LENGTH_SHORT).show();
        Log.i("Item number selected:", String.valueOf(clickedItemIndex));
        String[] justTheSteps = NetworkingUtils.getSteps(jsonFromQuery, clickedItemIndex);
        if (justTheSteps.length > 0) {
            String[] steps = new String[justTheSteps.length + 1];
            steps[0] = getString(R.string.Ingredients);
            for (int i = 1; i < steps.length; i++) {
                steps[i] = justTheSteps[i - 1];
            }
            Intent selectedRecipeIntent = new Intent(this, StepListActivity.class);
            Bundle stepsBundle = new Bundle();
            stepsBundle.putStringArray(StepListActivity.STEPS_KEY, steps);
            stepsBundle.putString(StepListActivity.TITLE, recipesAdapter.getRecipeAt(clickedItemIndex));
            stepsBundle.putString(StepListActivity.JSON, jsonFromQuery);
            stepsBundle.putInt(StepListActivity.INDEX, clickedItemIndex);
            selectedRecipeIntent.putExtras(stepsBundle);
            startActivity(selectedRecipeIntent);
        }
    }

    public class FetchRecipes extends AsyncTask<String, Void, String> {

        private final String TAG = FetchRecipes.class.getSimpleName();

        @Override
        protected String doInBackground(String... strings) {
            if (strings.length == 0) return null;
            // publishProgress(1);
            try {
                return NetworkingUtils.run(strings[0]);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String jsonString) {
            if (jsonString != null) {
                jsonFromQuery = jsonString;
                recipesAdapter.setJson(jsonString);
                recipesAdapter.setRecipes();
                recipeCards.setAdapter(recipesAdapter);
            }
        }
    }
}
