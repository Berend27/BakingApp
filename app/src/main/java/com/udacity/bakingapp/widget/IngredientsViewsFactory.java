package com.udacity.bakingapp.widget;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.bakingapp.Database.ListColumns;
import com.udacity.bakingapp.Database.RecipeProvider;
import com.udacity.bakingapp.R;

public class IngredientsViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    // Declare a Context and a Cursor
    Context context;
    Cursor cursor;
    int cursorPlace;
    int itemCount;

    public IngredientsViewsFactory(Context appContext)
    {
        context = appContext;
        cursorPlace = 0;
        itemCount = 4;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        if (cursor != null) cursor.close();
        cursor = context.getContentResolver().query(
                RecipeProvider.Lists.LISTS,
                null, null, null, ListColumns._ID + " ASC");
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (cursor == null) return 0;
        cursor.moveToPosition(cursorPlace);
        int ingredientsIndex = cursor.getColumnIndex(ListColumns.INGREDIENTS);
        String ingredientsString = cursor.getString(ingredientsIndex);
        String[] ingredients = ingredientsString.split("&");
        itemCount = ingredients.length;
        return itemCount;
    }

    // The values of the listview items are set one at a time. This method is called each time.
    @Override
    public RemoteViews getViewAt(int position) {
        if (cursor == null || cursor.getCount() == 0) return null;
        cursor.moveToPosition(cursorPlace);
        int idIndex = cursor.getColumnIndex(ListColumns._ID);
        int nameIndex = cursor.getColumnIndex(ListColumns.TITLE);
        int ingredientsIndex = cursor.getColumnIndex(ListColumns.INGREDIENTS);
        Log.i("onPostExecute:", "cursor moved");
        String ingredientsString = cursor.getString(ingredientsIndex);
        Log.i("ingredients string", "   $ $ $" + ingredientsString);
        String[] ingredients = ingredientsString.split("&");
        itemCount = ingredients.length;
        getCount();

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_item);
        views.setTextViewText(R.id.list_item, ingredients[position]);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    // must return a value greater than zero to display the data in the ListView
    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    public void setCursorPlace(int place){
        this.cursorPlace = place;
    }
}
