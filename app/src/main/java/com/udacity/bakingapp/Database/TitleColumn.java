package com.udacity.bakingapp.Database;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;

import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

public interface TitleColumn {
    @DataType(TEXT) @NotNull
    String TITLE = "title";
}
