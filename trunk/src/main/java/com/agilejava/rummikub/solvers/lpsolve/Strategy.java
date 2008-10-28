// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Strategy.java

package com.agilejava.rummikub.solvers.lpsolve;


public interface Strategy
{

    public abstract int getType();

    public abstract void setGoal(double ad[]);
}
