// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DefaultTileCollection.java

package com.agilejava.rummikub.core;

import java.util.Arrays;

// Referenced classes of package com.agilejava.rummikub.core:
//            DefaultTileCollectionListener, MutableTileCollection, TileCollectionListener, Constants, 
//            TileSetVisitor

public class DefaultTileCollection
    implements MutableTileCollection
{

    private DefaultTileCollection(int numberOfJokers, int numberOfTiles[][])
    {
        this.numberOfJokers = numberOfJokers;
        this.numberOfTiles = numberOfTiles;
    }

    public DefaultTileCollection()
    {
        this(((TileCollectionListener) (new DefaultTileCollectionListener())));
    }

    public DefaultTileCollection(TileCollectionListener listener)
    {
        this(0, new int[13][4]);
        this.listener = listener;
    }

    public void setNumberOfJokers(int numberOfJokers)
    {
        listener.beforeChangingNumberOfJokers(numberOfJokers - this.numberOfJokers);
        this.numberOfJokers = numberOfJokers;
    }

    public void setNumberOfTiles(int number, int color, int numberOfTiles)
    {
        int index = getColorIndex(color);
        int delta = numberOfTiles - this.numberOfTiles[number - 1][index];
        listener.beforeChangingNumberOfTiles(number, color, delta);
        this.numberOfTiles[number - 1][index] = numberOfTiles;
    }

    public int getNumberOfTiles(int number, int color)
    {
        int index = getColorIndex(color);
        return numberOfTiles[number - 1][index];
    }

    public int getNumberOfJokers()
    {
        return numberOfJokers;
    }

    private static final int getColorIndex(int color)
    {
        return Arrays.binarySearch(Constants.ALL_COLORS, color);
    }

    public void acceptTileSetVisitor(TileSetVisitor visitor)
    {
        visitor.beforeFirst();
        for(int c = 0; c < 4; c++)
        {
            for(int n = 0; n < 13; n++)
            {
                for(int i = 0; i < numberOfTiles[n][c]; i++)
                    visitor.visitNumberedTile(n + 1, Constants.ALL_COLORS[c]);

            }

        }

        for(int i = 0; i < numberOfJokers; i++)
            visitor.visitJoker();

        visitor.afterLast();
    }

    private int numberOfJokers;
    private int numberOfTiles[][];
    private TileCollectionListener listener;
}
