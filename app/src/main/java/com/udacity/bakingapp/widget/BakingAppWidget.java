package com.udacity.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.RecipeCardsActivity;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidget extends AppWidgetProvider {

    static RemoteViews views;
    public static int recipeNumber;
    public static int widgetId;

    public static boolean selectionMade = false;

    public static final String INGREDIENTS = "ingredients";

    public static final String SELECTED_RECIPE = "com.udacity.bakingapp.widget.action.SELECTED_RECIPE";
    public static final String POSITION = "position";
    public static final String TITLE = "title";

    private static final String TAG = BakingAppWidget.class.getSimpleName();

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.Ingredients);

        Intent widgetIntent = new Intent(context, WidgetIntentService.class);
        widgetIntent.setAction(WidgetIntentService.ACTION_LIST_INGREDIENTS);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, widgetIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // intent to switch recipes
        Intent buttonIntent = new Intent(context, WidgetIntentService.class);
        buttonIntent.setAction(WidgetIntentService.ACTION_SWITCH_RECIPES);
        PendingIntent pendingIntent2 = PendingIntent.getService(context, 0, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        widgetId = appWidgetId;

        if (selectionMade) {
            // Construct the RemoteViews object with the ListView by calling a defined method
            try {
                views = getIngredientsRemoteView(context.getApplicationContext(), recipeNumber);
                views.setViewVisibility(R.id.switch_recipes, View.VISIBLE);
                views.setOnClickPendingIntent(R.id.switch_recipes, pendingIntent2);
            } catch (Exception e) { }
        } else {
            widgetText = context.getString(R.string.recipes);
            try {
                views = getRecipesRemoteView(context.getApplicationContext());
                // Allowing individual grid items to respond to clicks
                Intent selectedIntent = new Intent(context, BakingAppWidget.class);
                selectedIntent.setAction(BakingAppWidget.SELECTED_RECIPE);
                selectedIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

                PendingIntent selectedPendingIntent = PendingIntent.getBroadcast(context, 0, selectedIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                views.setPendingIntentTemplate(R.id.widget_grid_view, selectedPendingIntent);
            } catch (Exception e) { }
        }
        views.setTextViewText(R.id.appwidget_text, widgetText);
        // setting the click handler to the pending intent (button, PendingIntent)
        //views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);

        // Allowing individual grid items to respond to clicks
        Intent selectedIntent = new Intent(context, BakingAppWidget.class);
        selectedIntent.setAction(BakingAppWidget.SELECTED_RECIPE);
        selectedIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        PendingIntent selectedPendingIntent = PendingIntent.getBroadcast(context, 0, selectedIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_grid_view, selectedPendingIntent);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    // Called when the BroadcastReceiver receives an Intent broadcast.
    // Checks to see whether the intent's action is TOAST_ACTION. If it is, the app widget
    // displays a Toast message for the current item.
    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        if (intent.getAction().equals(SELECTED_RECIPE)) {
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            recipeNumber = intent.getIntExtra(POSITION, 0);
            selectionMade = true;
            String title = intent.getStringExtra(TITLE);
            updateAppWidget(context, mgr, widgetId );

            Toast.makeText(context, title, Toast.LENGTH_LONG).show();
        }
        super.onReceive(context, intent);
    }

    public static void updateIngredientsWidget(Context context, AppWidgetManager appWidgetManager,
                                               int[] appWidgetIds, String ingredients) {
        // There may be multiple widgets active, so update all of them
        for (int widgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, widgetId);
        }
    }

    public static void switchRecipesWidget(Context context, AppWidgetManager appWidgetManager,
                                               int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        selectionMade = false;
        for (int widgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, widgetId);
        }
    }

    // needed for the widget's list view, gets called every time the ListView is shown
    private static RemoteViews getIngredientsRemoteView(Context context, int gridPosition)
    {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);

        // Set the ListWidgetService intent to act as the adapter for the ListView
        Log.i(TAG, "Sending an intent to the remote views service");
        Intent intent = new Intent(context, ListWidgetService.class);
        intent.putExtra(POSITION, gridPosition);
        IngredientsViewsFactory.cursorPlace = gridPosition;  // using a static variable to set the cursor place
        views.setRemoteAdapter(R.id.widget_list_view, intent);

        return views;
    }

    private static RemoteViews getRecipesRemoteView(Context context)
    {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_selection_widget);

        Intent intent = new Intent(context, RecipeViewsService.class);
        views.setRemoteAdapter(R.id.widget_grid_view, intent);


        return views;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        WidgetIntentService.startActionListIngredients(context, recipeNumber);

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}

