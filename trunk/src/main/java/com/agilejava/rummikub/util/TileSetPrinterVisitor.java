// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TileSetPrinterVisitor.java

package com.agilejava.rummikub.util;

import com.agilejava.rummikub.core.Constants;
import com.agilejava.rummikub.core.TileSetVisitor;
import java.io.PrintWriter;
import java.util.Arrays;

public class TileSetPrinterVisitor
    implements TileSetVisitor
{

    public TileSetPrinterVisitor(PrintWriter writer)
    {
        this.writer = writer;
    }

    public void afterLast()
    {
    }

    public void beforeFirst()
    {
        writer.println();
    }

    public void visitJoker()
    {
        writer.print("[joker] ");
    }

    public void visitNumberedTile(int number, int color)
    {
        int index = Arrays.binarySearch(Constants.ALL_COLORS, color);
        writer.print("[" + Constants.ALL_COLOR_LABELS[index] + " " + number + "] ");
    }

    private PrintWriter writer;
}
