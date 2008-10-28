// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   GroupTileSet.java

package com.agilejava.rummikub.core;


// Referenced classes of package com.agilejava.rummikub.core:
//            DefaultTileCollectionListener, TileSet, TileCollectionListener, TileSetVisitor

public class GroupTileSet
    implements TileSet
{

    public GroupTileSet(int number, TileCollectionListener listener)
    {
        this.number = number;
        colors = new int[4];
        size = 0;
        jokers = 0;
        this.listener = listener;
    }

    public GroupTileSet(int number)
    {
        this(number, ((TileCollectionListener) (new DefaultTileCollectionListener())));
    }

    public int getType()
    {
        return 2;
    }

    public void addColor(int color)
    {
        listener.beforeChangingNumberOfTiles(number, color, 1);
        colors[size++] = color;
    }

    public void addJoker()
    {
        listener.beforeChangingNumberOfJokers(1);
        jokers++;
    }

    public void acceptVisitor(TileSetVisitor visitor)
    {
        visitor.beforeFirst();
        for(int i = 0; i < size; i++)
            visitor.visitNumberedTile(number, colors[i]);

        for(int i = 0; i < jokers; i++)
            visitor.visitJoker();

        visitor.afterLast();
    }

    private int number;
    private int colors[];
    private int size;
    private int jokers;
    private TileCollectionListener listener;
}
