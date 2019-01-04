package com.udacity.bakingapp;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.text.DecimalFormat;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkingUtils {

    static OkHttpClient client = new OkHttpClient();

    private static final String TAG = NetworkingUtils.class.getSimpleName();

    private NetworkingUtils() {}

    // The library OkHttp is used for making an HTTP request
    static String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static String[] getRecipeNames(String json)
    {
        try {
            JSONArray recipes = new JSONArray(json);
            String[] recipeNames = new String[recipes.length()];
            for (int i = 0; i < recipes.length(); i++) {
                JSONObject recipe = recipes.getJSONObject(i);
                recipeNames[i] = recipe.getString("name");
            }
            return recipeNames;
        } catch (JSONException je) {
            Log.e(TAG, je.toString());
            return null;
        } catch (NullPointerException ne) {Log.e(TAG, ne.toString()); return null;}
    }

    public static String[] getSteps(String json, int placeNumber)
    {

        try {
            JSONArray recipes = new JSONArray(json);
            JSONObject thisRecipe = recipes.getJSONObject(placeNumber);
            JSONArray recipeSteps = thisRecipe.getJSONArray("steps");
            String[] steps = new String[recipeSteps.length()];
            for (int i = 0; i < recipeSteps.length(); i++)
            {
                JSONObject thisStep = recipeSteps.getJSONObject(i);
                steps[i] = thisStep.getString("shortDescription");
            }
            return steps;
        } catch (JSONException je) {
            Log.e(TAG, je.toString());
            return null;
        } catch (NullPointerException ne) {Log.e(TAG, ne.toString()); return null;}
    }

    public static String[] getIngredients(String json, int place)
    {
        try {
            JSONArray recipes = new JSONArray(json);
            JSONObject thisRecipe = recipes.getJSONObject(place);
            JSONArray ingredientsJson = thisRecipe.getJSONArray("ingredients");
            String[] ingredients = new String[ingredientsJson.length()];
            for (int i = 0; i < ingredients.length; i++)
            {
                JSONObject thisIngredient = ingredientsJson.getJSONObject(i);
                DecimalFormat decimalFormat = new DecimalFormat("0.#");
                String quantity =decimalFormat.format(thisIngredient.getDouble("quantity"));
                String measure = thisIngredient.getString("measure");
                if (measure.equals("CUP") && !quantity.equals("1")) {measure = "CUPS";}
                else if (measure.equals("UNIT")) {measure = "";}
                else if (measure.equals("K")) {measure = "KG";}
                String ingredient = thisIngredient.getString("ingredient");
                ingredients[i] = quantity + " " + measure + " " + ingredient;
            }
            return ingredients;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }

    // gets the id, shortDescription, description, videoURL, and thumbnailURL for the recipe at the specified place
    public static String[][] getSpecificSteps(String json, int place)
    {
        try {
            JSONArray recipes = new JSONArray(json);
            JSONObject thisRecipe = recipes.getJSONObject(place);
            JSONArray recipeSteps = thisRecipe.getJSONArray("steps");
            String[][] steps = new String[5][recipeSteps.length()];
            for (int i = 0; i < recipeSteps.length(); i++)    // for each step
            {
                JSONObject thisStep = recipeSteps.getJSONObject(i);
                steps[0][i] = Integer.toString(thisStep.getInt("id"));
                steps[1][i] = thisStep.getString("shortDescription");
                steps[2][i] = thisStep.getString("description");
                steps[3][i] = thisStep.getString("videoURL");
                steps[4][i] = thisStep.getString("thumbnailURL");
            }
            return steps;
        } catch (JSONException je) {
            Log.e(TAG, je.toString());
            return null;
        } catch (NullPointerException ne) {Log.e(TAG, ne.toString()); return null;}
    }

    public static int getNumberOfSteps(String json, int place)
    {
        try {
            JSONArray recipes = new JSONArray(json);
            JSONObject thisRecipe = recipes.getJSONObject(place);
            JSONArray recipeSteps = thisRecipe.getJSONArray("steps");
            return recipeSteps.length();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return 0;
        }
    }

}
