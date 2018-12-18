package com.udacity.bakingapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkingUtils {

    static OkHttpClient client = new OkHttpClient();

    private static final String TAG = NetworkingUtils.class.getSimpleName();

    private NetworkingUtils() {}

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

    /*
    public static String[] getPoster(String json)
    {
        try {
            JSONObject queryResults = new JSONObject(json);
            JSONArray movies = queryResults.getJSONArray("results");
            String[] posters = new String[movies.length()];
            for (int i = 0; i < movies.length(); i++) {
                JSONObject movie = movies.getJSONObject(i);
                posters[i] = "http://image.tmdb.org/t/p/w185/" + movie.getString("poster_path");
            }
            return posters;
        } catch (JSONException je) {
            Log.e(TAG, je.toString());
            return null;
        } catch (NullPointerException ne) {Log.e(TAG, ne.toString()); return null;}

    }
     */
}
