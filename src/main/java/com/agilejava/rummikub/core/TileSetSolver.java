// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TileSetSolver.java

package com.agilejava.rummikub.core;


// Referenced classes of package com.agilejava.rummikub.core:
//            TileCollection, Solution

public interface TileSetSolver
{

    public abstract int[] getStrategies();

    public abstract Solution solve(TileCollection tilecollection);

    public abstract void setStrategy(int i);

    public abstract int getStrategy();
}
