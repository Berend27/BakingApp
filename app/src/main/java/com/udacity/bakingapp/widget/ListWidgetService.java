package com.udacity.bakingapp.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class ListWidgetService extends RemoteViewsService {

    // only gets called once
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        int cursorPlace = intent.getIntExtra(BakingAppWidget.POSITION, 0);
        intent.removeExtra(BakingAppWidget.POSITION);
        IngredientsViewsFactory viewsFactory
                =  new IngredientsViewsFactory(this.getApplicationContext(), cursorPlace);
        return viewsFactory;
    }
}
