package com.udacity.bakingapp;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.udacity.bakingapp.Database.ListColumns;
import com.udacity.bakingapp.Database.RecipeProvider;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import java.util.Arrays;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

public class RecipeCardsActivity extends AppCompatActivity
    implements RecipeCardAdapter.RecipeCardClickListener {

    private RecipeCardAdapter recipesAdapter;
    private RecyclerView recipeCards;
    private FetchRecipes fetch = new FetchRecipes();
    private BackupJson backupTask = new BackupJson();
    private int numberOfRecipes = 4;

    static final String QUERY = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    private String jsonFromQuery = "";
    private String jsonFromDatabase = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_cards);

        recipeCards = (RecyclerView) findViewById(R.id.recipe_cards);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recipeCards.setLayoutManager(layoutManager);
        recipeCards.setHasFixedSize(true);

        try {
            recipesAdapter = new RecipeCardAdapter(numberOfRecipes, this);
        } catch (Exception e) {}
        // the adapter is set in onPostExecute of the AsyncTask
        fetch.execute(QUERY);

    }

    @Override
    public void onListItemClicked(int clickedItemIndex) {
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
                String json = NetworkingUtils.run(strings[0]);
                return json;
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.toString());
                Log.i(TAG, "The query returned null");
                return null;
            }
        }

        @Override
        protected void onPostExecute(String jsonString) {
            if (jsonString != null) {
                jsonFromQuery = jsonString;

                // adding to the database
                if (!jsonFromQuery.isEmpty() && jsonFromQuery != null) {
                    recipesAdapter.setJson(jsonString);
                    recipesAdapter.setRecipes();
                    recipeCards.setAdapter(recipesAdapter);

                    String[] recipes = NetworkingUtils.getRecipeNames(jsonString);
                    ContentValues contentValues = new ContentValues();
                    for (int i = 0; i < numberOfRecipes; i++) {
                        contentValues.put(ListColumns._ID, i);
                        contentValues.put(ListColumns.TITLE, recipes[i]);
                        Log.i("Title:", recipes[i]);
                        contentValues.put(ListColumns.JSON, jsonString);  // offline backup
                        String ingredients = "";
                        String[] array;
                        array = NetworkingUtils.getIngredients(jsonString, i);
                        // setting up a string that will be split on "&"
                        try {
                            for (int j = 0; j < array.length; j++) {
                                ingredients = ingredients + array[j] + "&";
                            }
                        } catch (Exception e) {}
                        contentValues.put(ListColumns.INGREDIENTS, ingredients);

                        try {
                            getContentResolver().insert(RecipeProvider.Lists.LISTS, contentValues);
                        } catch (Exception e)
                        {
                            Log.i(TAG, "Duplicate values?");
                        }
                    }
                }
                } else {
                Log.i(TAG, "No online access to the JSON");
                backupTask.execute();

            }
        }
    }

    public class BackupJson extends AsyncTask<Void, Void, Cursor>
    {

        @Override
        protected Cursor doInBackground(Void... voids) {
            ContentResolver resolver = getContentResolver();
            Cursor cursor = resolver.query(RecipeProvider.Lists.LISTS, null, null, null, null);
            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor)
        {
            Log.i("BackupJson", "Getting JSON from a database");
            super.onPostExecute(cursor);
            cursor.moveToFirst();
            try {
                jsonFromDatabase = cursor.getString(3);
            } catch (Exception e) {
                Log.i("BackupJson", "There was a problem");
                e.printStackTrace();
            }
            cursor.close();

            jsonFromQuery = jsonFromDatabase;
            if (jsonFromDatabase != null && !jsonFromDatabase.isEmpty()) {
                try {
                    recipesAdapter.setJson(jsonFromDatabase);
                    recipesAdapter.setRecipes();
                    recipeCards.setAdapter(recipesAdapter);
                } catch (Exception e) {}
            }
        }
    }
}
