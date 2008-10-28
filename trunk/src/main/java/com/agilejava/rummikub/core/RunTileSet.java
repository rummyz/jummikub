// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   RunTileSet.java

package com.agilejava.rummikub.core;


// Referenced classes of package com.agilejava.rummikub.core:
//            DefaultTileCollectionListener, TileSet, TileCollectionListener, TileSetVisitor

public class RunTileSet
    implements TileSet
{

    public RunTileSet(int color, TileCollectionListener listener)
    {
        this.color = color;
        tiles = new int[13];
        this.listener = listener;
    }

    public RunTileSet(int color)
    {
        this(color, ((TileCollectionListener) (new DefaultTileCollectionListener())));
    }

    public void addTile(int number, boolean joker)
    {
        if(joker)
            listener.beforeChangingNumberOfJokers(1);
        else
            listener.beforeChangingNumberOfTiles(number, color, 1);
        tiles[number - 1] = joker ? 1 : 2;
    }

    public void acceptVisitor(TileSetVisitor visitor)
    {
        visitor.beforeFirst();
        for(int i = 0; i < tiles.length; i++)
            switch(tiles[i])
            {
            case 1: // '\001'
                visitor.visitJoker();
                break;

            case 2: // '\002'
                visitor.visitNumberedTile(i + 1, color);
                break;
            }

        visitor.afterLast();
    }

    public int getType()
    {
        return 1;
    }

    private static final int UNDEFINED = 0;
    private static final int JOKER = 1;
    private static final int NUMBERED_TILE = 2;
    private int color;
    private int tiles[];
    private TileCollectionListener listener;
}
