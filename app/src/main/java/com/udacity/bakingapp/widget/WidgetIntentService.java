package com.udacity.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import com.udacity.bakingapp.Database.ListColumns;
import com.udacity.bakingapp.Database.RecipeProvider;
import com.udacity.bakingapp.R;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class WidgetIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_LIST_INGREDIENTS = "com.udacity.bakingapp.widget.action.LIST_INGREDIENTS";
    public static final String ACTION_SWITCH_RECIPES = "com.udacity.bakingapp.widget.action.SWITCH_RECIPES";
    private static final String ACTION_FOO = "com.udacity.bakingapp.widget.action.FOO";
    private static final String ACTION_BAZ = "com.udacity.bakingapp.widget.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.udacity.bakingapp.widget.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.udacity.bakingapp.widget.extra.PARAM2";

    public static String ingredients;
    public static final String CURSOR_PLACE = "cursorPlace";


    public WidgetIntentService() {
        super("WidgetIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, WidgetIntentService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    public static void startActionListIngredients(Context context, int number)
    {
        Intent listIngredientsIntent = new Intent(context, WidgetIntentService.class);
        listIngredientsIntent.setAction(ACTION_LIST_INGREDIENTS);
        listIngredientsIntent.putExtra(CURSOR_PLACE, number);
        context.startService(listIngredientsIntent);
    }

    public static void startActionSwitchRecipes(Context context)
    {
        Intent switchIntent = new Intent(context, WidgetIntentService.class);
        switchIntent.setAction(ACTION_SWITCH_RECIPES);
        context.startService(switchIntent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, WidgetIntentService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_LIST_INGREDIENTS.equals(action)) {
                int cursorPlace = intent.getIntExtra(CURSOR_PLACE, 0);
                handleActionListIngredients(cursorPlace);
            } else if (ACTION_SWITCH_RECIPES.equals(action)) {
                handleActionSwitchRecipes();
            }
        }
    }

    private void handleActionListIngredients(int cursorPlace)
    {
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(RecipeProvider.Lists.LISTS, null, null,
                null, ListColumns.TITLE + " ASC");

        // Extract the Ingredients using Cursor methods
        // use cursor.close(); when done with the cursor
        cursor.moveToPosition(cursorPlace);
        Log.i("onPostExecute:", "cursor moved");
        String ingredientsString = cursor.getString(2);
        Log.i("ingredients string", "   $ $ $" + ingredientsString);
        ingredients = ingredientsString;
       // Toast.makeText(this, ingredientsString, Toast.LENGTH_LONG).show();
        cursor.close();

        // call the defined provider method to update the widget
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        int[] ids = manager.getAppWidgetIds(new ComponentName(this, BakingAppWidget.class));
        manager.notifyAppWidgetViewDataChanged(ids, R.id.widget_list_view);
        BakingAppWidget.updateIngredientsWidget(this, manager, ids, ingredientsString);
        // query that data here in this method
    }

    private void handleActionSwitchRecipes()
    {
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        int[] ids = manager.getAppWidgetIds(new ComponentName(this, BakingAppWidget.class));
        BakingAppWidget.switchRecipesWidget(this, manager, ids);
    }

    public static String getIngredients() {
        return ingredients;
    }
}
