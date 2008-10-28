// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TileSetPrinter.java

package com.agilejava.rummikub.util;

import com.agilejava.rummikub.core.TileSet;
import java.io.PrintWriter;
import java.util.List;

// Referenced classes of package com.agilejava.rummikub.util:
//            TileSetPrinterVisitor

public class TileSetPrinter
{

    public TileSetPrinter()
    {
    }

    public static final void printTileSets(List tileSets)
    {
        TileSet tileSet = null;
        com.agilejava.rummikub.core.TileSetVisitor visitor = new TileSetPrinterVisitor(new PrintWriter(System.out));
        for(int i = 0; i < tileSets.size(); i++)
        {
            tileSet = (TileSet)tileSets.get(i);
            tileSet.acceptVisitor(visitor);
        }

    }
}
