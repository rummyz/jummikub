// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Util.java

package com.agilejava.rummikub.util;

import com.agilejava.rummikub.core.*;

public class Util
{

    public Util()
    {
    }

    public static final void copy(TileCollection source, MutableTileCollection destination)
    {
        int numberOfTiles = 0;
        for(int c = 0; c < 4; c++)
        {
            int color = Constants.ALL_COLORS[c];
            for(int n = 1; n <= 13; n++)
            {
                numberOfTiles = source.getNumberOfTiles(n, color);
                destination.setNumberOfTiles(n, color, numberOfTiles);
            }

        }

        numberOfTiles = source.getNumberOfJokers();
        destination.setNumberOfJokers(numberOfTiles);
    }
}
