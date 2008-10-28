// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TileSetVisitor.java

package com.agilejava.rummikub.core;


public interface TileSetVisitor
{

    public abstract void beforeFirst();

    public abstract void afterLast();

    public abstract void visitJoker();

    public abstract void visitNumberedTile(int i, int j);
}
