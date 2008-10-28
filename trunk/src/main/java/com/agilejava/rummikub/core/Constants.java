// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Constants.java

package com.agilejava.rummikub.core;


public class Constants
{

    public Constants()
    {
    }

    public static final int COLOR_RED = 1;
    public static final int COLOR_BLACK = 2;
    public static final int COLOR_YELLOW = 3;
    public static final int COLOR_BLUE = 4;
    public static final int ALL_COLORS[] = {
        1, 2, 3, 4
    };
    public static final String ALL_COLOR_LABELS[] = {
        "red", "black", "yellow", "blue"
    };
    public static final int STRATEGY_MAXIMIZE_NR_OF_TILES = 1;
    public static final int STRATEGY_MAXIMIZE_TOTAL = 2;
    public static final int ALL_STRATEGIES[] = {
        1, 2
    };
    public static final String ALL_STRATEGY_LABELS[] = {
        "Maximize the number of tiles used in the solution", "Maximize the totals"
    };

}
