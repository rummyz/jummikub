// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MutableTileCollection.java

package com.agilejava.rummikub.core;


// Referenced classes of package com.agilejava.rummikub.core:
//            TileCollection

public interface MutableTileCollection
    extends TileCollection
{

    public abstract void setNumberOfTiles(int i, int j, int k);

    public abstract void setNumberOfJokers(int i);
}
