package com.udacity.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.RecipeCardsActivity;
// TODO stopping point 1/2 The widget's ListView works. It needs to allow users to choose a recipe
/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidget extends AppWidgetProvider {

    static RemoteViews views;

    public static final String INGREDIENTS = "ingredients";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, String ingredientsList) {

        CharSequence widgetText = context.getString(R.string.Ingredients);

        Intent widgetIntent = new Intent(context, WidgetIntentService.class);
        widgetIntent.setAction(WidgetIntentService.ACTION_LIST_INGREDIENTS);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, widgetIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Construct the RemoteViews object with the ListView by calling a defined method
        try {
            views = getIngredientsRemoteView(context.getApplicationContext());
        } catch (Exception e) {}
        views.setTextViewText(R.id.appwidget_text, widgetText);
        // setting the click handler to the pending intent (button, PendingIntent)
        views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateIngredientsWidget(Context context, AppWidgetManager appWidgetManager,
                                               int[] appWidgetIds, String ingredients) {
        // There may be multiple widgets active, so update all of them
        for (int widgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, widgetId, ingredients);
        }
    }

    // needed for the widget's list view
    private static RemoteViews getIngredientsRemoteView(Context context)
    {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);

        // Set the ListWidgetService intent to act as the adapter for the ListView
        Intent intent = new Intent(context, ListWidgetService.class);
        views.setRemoteAdapter(R.id.widget_list_view, intent);

        return views;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        WidgetIntentService.startActionListIngredients(context);

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

