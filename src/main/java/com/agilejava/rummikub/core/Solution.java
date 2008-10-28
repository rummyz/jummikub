// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Solution.java

package com.agilejava.rummikub.core;

import java.util.List;

// Referenced classes of package com.agilejava.rummikub.core:
//            TileSet, TileCollection, TileSetVisitor

public class Solution
{

    public Solution(List tileSets, TileCollection leftOver)
    {
        this.tileSets = tileSets;
        this.leftOver = leftOver;
    }

    public int getNumberOfTileSets()
    {
        return tileSets.size();
    }

    public TileSet getTileSet(int index)
    {
        return (TileSet)tileSets.get(index);
    }

    public TileCollection getLeftOver()
    {
        return leftOver;
    }

    public void acceptTileSetVisitor(TileSetVisitor visitor)
    {
        TileSet tileSet = null;
        for(int i = 0; i < tileSets.size(); i++)
        {
            tileSet = (TileSet)tileSets.get(i);
            tileSet.acceptVisitor(visitor);
        }

    }

    private List tileSets;
    private TileCollection leftOver;
}
