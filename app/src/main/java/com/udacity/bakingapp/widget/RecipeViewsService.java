package com.udacity.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.bakingapp.Database.ListColumns;
import com.udacity.bakingapp.Database.RecipeProvider;
import com.udacity.bakingapp.R;

public class RecipeViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RecipeViewsFactory(this.getApplicationContext());
    }
}

class RecipeViewsFactory implements RemoteViewsService.RemoteViewsFactory, AdapterView.OnItemClickListener {

    // Declare a Context and a Cursor
    Context context;
    private Cursor cursor;

    public RecipeViewsFactory(Context appContext)
    {
        context = appContext;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        if (cursor != null) cursor.close();
        cursor = context.getContentResolver().query(
                RecipeProvider.Lists.LISTS,
                null, null, null, ListColumns.TITLE + " ASC");
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (cursor == null) return 0;
        return cursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (cursor == null || cursor.getCount() == 0) return null;
        cursor.moveToPosition(position);
        int nameIndex = cursor.getColumnIndex(ListColumns.TITLE);

        // Creating an individual RemoteViews object, setting its text and text color
        String title = cursor.getString(nameIndex);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.step_list_item);
        views.setTextColor(R.id.list_item, context.getResources().getColor(R.color.darkBlueInk));
        views.setTextViewText(R.id.list_item, title);

        // Bundle for the intent that holds the position of the item that was clicked
        Bundle extras = new Bundle();
        extras.putInt(BakingAppWidget.POSITION, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        views.setOnClickFillInIntent(R.id.list_item, fillInIntent);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
