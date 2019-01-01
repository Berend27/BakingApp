package com.udacity.bakingapp.Database;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

@ContentProvider(authority = RecipeProvider.AUTHORITY, database = RecipeDatabase.class)
public final class RecipeProvider {
    public static final String AUTHORITY = "com.udacity.bakingapp.Database.RecipeProvider";

    static final int ALL_ITEMS = -2;  // convenience constant?

    @TableEndpoint(table = RecipeDatabase.LISTS)
    public static class Lists {
        @ContentUri(
                path = "lists",  // content path
                type = "vnd.android.cursor.dir/list",
                defaultSort = ListColumns.TITLE + " ASC")

        // content uri
        public static final Uri LISTS = Uri.parse("content://" + AUTHORITY + "/lists");
    }
}
