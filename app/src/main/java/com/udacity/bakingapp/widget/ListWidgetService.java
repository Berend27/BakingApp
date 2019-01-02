package com.udacity.bakingapp.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class ListWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientsViewsFactory(this.getApplicationContext());
    }
}
// TODO Stopping point 6:53