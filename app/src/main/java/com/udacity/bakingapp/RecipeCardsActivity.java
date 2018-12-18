package com.udacity.bakingapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

public class RecipeCardsActivity extends AppCompatActivity
    implements RecipeCardAdapter.RecipeCardClickListener {

    private RecipeCardAdapter recipesAdapter;
    private RecyclerView recipeCards;
    private FetchRecipes fetch = new FetchRecipes();
    private int numberOfRecipes = 4;

    static final String QUERY = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

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
                recipesAdapter.setJson(jsonString);
                recipesAdapter.setRecipes();
                recipeCards.setAdapter(recipesAdapter);
            }
        }
    }
}
