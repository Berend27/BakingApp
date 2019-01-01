package com.udacity.bakingapp.Database;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;

import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

public interface IngredientsColumn {
        @DataType(TEXT) @NotNull
        String INGREDIENTS = "ingredients";
}
