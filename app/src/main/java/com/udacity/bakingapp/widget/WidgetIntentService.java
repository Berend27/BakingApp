package com.udacity.bakingapp.widget;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;

import com.udacity.bakingapp.Database.RecipeProvider;

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
    private static final String ACTION_FOO = "com.udacity.bakingapp.widget.action.FOO";
    private static final String ACTION_BAZ = "com.udacity.bakingapp.widget.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.udacity.bakingapp.widget.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.udacity.bakingapp.widget.extra.PARAM2";

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

    public static void startActionListIngredients(Context context)
    {
        Intent listIngredientsIntent = new Intent(context, WidgetIntentService.class);
        listIngredientsIntent.setAction(ACTION_LIST_INGREDIENTS);
        context.startService(listIngredientsIntent);
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
                handleActionListIngredients();
            } // there could be more potential actions but in this case there aren't
        }
    }

    private void handleActionListIngredients()
    {
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(RecipeProvider.Lists.LISTS, null, null,
                null, null);

        // Extract the Ingredients using Cursor methods
        // use cursor.close(); when done with the cursor

        // call the defined provider method to update the widget

        // query that data here in this method
    }
    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
