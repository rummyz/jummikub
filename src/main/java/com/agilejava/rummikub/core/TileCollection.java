// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TileCollection.java

package com.agilejava.rummikub.core;


// Referenced classes of package com.agilejava.rummikub.core:
//            TileSetVisitor

public interface TileCollection
{

    public abstract int getNumberOfTiles(int i, int j);

    public abstract int getNumberOfJokers();

    public abstract void acceptTileSetVisitor(TileSetVisitor tilesetvisitor);
}
