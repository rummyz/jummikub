// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TileSet.java

package com.agilejava.rummikub.core;


// Referenced classes of package com.agilejava.rummikub.core:
//            TileSetVisitor

public interface TileSet
{

    public abstract int getType();

    public abstract void acceptVisitor(TileSetVisitor tilesetvisitor);

    public static final int TYPE_RUN = 1;
    public static final int TYPE_GROUP = 2;
}
