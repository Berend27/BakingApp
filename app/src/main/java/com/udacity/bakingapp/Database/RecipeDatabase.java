package com.udacity.bakingapp.Database;

import android.os.Build;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

@Database(version = RecipeDatabase.VERSION)
public final class RecipeDatabase {

    public static final int VERSION = 1;

    @Table(ListColumns.class) public static final String LISTS = "lists";
}
