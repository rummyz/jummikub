// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   LPSolveBasedSolver.java

package com.agilejava.rummikub.solvers.lpsolve;

import com.agilejava.rummikub.core.*;
import com.agilejava.rummikub.util.Util;
import com.cflex.util.lpSolve.LpModel;
import com.cflex.util.lpSolve.LpSolver;
import java.io.PrintStream;
import java.util.*;

// Referenced classes of package com.agilejava.rummikub.solvers.lpsolve:
//            Strategy

public class LPSolveBasedSolver
    implements TileSetSolver
{
    private class MaximizeTotalStrategy
        implements Strategy
    {

        public int getType()
        {
            return 2;
        }

        public void setGoal(double v[])
        {
            Arrays.fill(v, 0.0D);
            for(int c = 0; c < 4; c++)
            {
                for(int n = 1; n <= 13; n++)
                {
                    v[LPSolveBasedSolver.getIndexNrOfTilesParticipating(n, Constants.ALL_COLORS[c])] = n;
                    v[LPSolveBasedSolver.getIndexNrOfTilesRepresentedByJokers(n, Constants.ALL_COLORS[c])] = 0.0D;
                }

            }

        }

        private MaximizeTotalStrategy()
        {
        }

    }

    private class MaximizeNrOfTilesStrategy
        implements Strategy
    {

        public int getType()
        {
            return 1;
        }

        public void setGoal(double v[])
        {
            Arrays.fill(v, 0.0D);
            for(int c = 0; c < 4; c++)
            {
                for(int n = 1; n <= 13; n++)
                {
                    v[LPSolveBasedSolver.getIndexNrOfTilesParticipating(n, Constants.ALL_COLORS[c])] = 1.0D;
                    v[LPSolveBasedSolver.getIndexNrOfTilesRepresentedByJokers(n, Constants.ALL_COLORS[c])] = -1D;
                }

            }

        }

        private MaximizeNrOfTilesStrategy()
        {
        }

    }

    private static final class SubstractingTileCollectionListener
        implements TileCollectionListener
    {

        public void beforeChangingNumberOfJokers(int delta)
        {
            int current = tileCollection.getNumberOfJokers();
            current -= delta;
            tileCollection.setNumberOfJokers(current);
        }

        public void beforeChangingNumberOfTiles(int number, int color, int delta)
        {
            int current = tileCollection.getNumberOfTiles(number, color);
            current -= delta;
            tileCollection.setNumberOfTiles(number, color, current);
        }

        private DefaultTileCollection tileCollection;

        public SubstractingTileCollectionListener(DefaultTileCollection tileCollection)
        {
            this.tileCollection = tileCollection;
        }
    }

    private static class TileData
    {

        public int tilesInRun;
        public int tilesInGroup;
        public int tilesTotal;
        public int tilesJokers;

        private TileData()
        {
        }

    }


    public LPSolveBasedSolver()
    {
        strategy = strategies[0];
    }

    public int[] getStrategies()
    {
        int result[] = new int[strategies.length];
        for(int i = 0; i < result.length; i++)
            result[i] = strategies[i].getType();

        return result;
    }

    public int getStrategy()
    {
        return strategy.getType();
    }

    public void setStrategy(int type)
    {
        for(int i = 0; i < strategies.length; i++)
            if(strategies[i].getType() == type)
            {
                strategy = strategies[i];
                return;
            }

        throw new RuntimeException("Invalid strategy: " + type);
    }

    public Solution solve(TileCollection collection)
    {
        try
        {
            int cols = 208;
            int rows = 0;
            LpModel lpIn = new LpModel(0, 208);
            LpSolver lpSolve = new LpSolver(lpIn);
            double v[] = new double[209];
            populateLPModel(lpIn, v, collection, strategy);
            int result = lpSolve.solve();
            if(result == 0)
                return getSolutionFromLpModel(lpIn, collection);
            else
                return new Solution(Collections.EMPTY_LIST, collection);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return new Solution(Collections.EMPTY_LIST, collection);
    }

    private static void populateLPModel(LpModel lpIn, double v[], TileCollection collection, Strategy strategy)
        throws Exception
    {
        addGoal(v, lpIn, strategy);
        for(int c = 0; c < Constants.ALL_COLORS.length; c++)
        {
            for(int n = 1; n <= 13; n++)
            {
                addConstraintType1(v, lpIn, n, Constants.ALL_COLORS[c]);
                addConstraintType2(v, lpIn, n, Constants.ALL_COLORS[c]);
                addConstraintType3(v, lpIn, n, Constants.ALL_COLORS[c]);
                addConstraintType4(v, lpIn, n, Constants.ALL_COLORS[c]);
                addConstraintType5(v, lpIn, n, Constants.ALL_COLORS[c]);
                addTileConstraint(v, lpIn, n, Constants.ALL_COLORS[c], collection.getNumberOfTiles(n, Constants.ALL_COLORS[c]));
            }

        }

        addJokerConstraint(v, lpIn, collection.getNumberOfJokers());
        addIntegerStatements(lpIn);
        setVariableNames(lpIn);
    }

    private static int getIndexNrOfTilesParticipatingInRun(int number, int color)
        throws Exception
    {
        return getTileIndex(number, color, 0);
    }

    private static int getIndexNrOfTilesParticipatingInGroup(int number, int color)
    {
        return getTileIndex(number, color, 1);
    }

    private static int getIndexNrOfTilesRepresentedByJokers(int number, int color)
    {
        return getTileIndex(number, color, 2);
    }

    private static int getIndexNrOfTilesParticipating(int number, int color)
    {
        return getTileIndex(number, color, 3);
    }

    private static int getTileIndex(int number, int color, int pane)
    {
        int factor = Arrays.binarySearch(Constants.ALL_COLORS, color);
        return ((factor * 13 + number) - 1) + pane * 52 + 1;
    }

    private static void addConstraintType1(double v[], LpModel lpIn, int number, int color)
        throws Exception
    {
        Arrays.fill(v, 0.0D);
        if(number >= 3)
            v[getIndexNrOfTilesParticipatingInRun(number - 2, color)] = 1.0D;
        if(number <= 12)
            v[getIndexNrOfTilesParticipatingInRun(number + 1, color)] = 1.0D;
        v[getIndexNrOfTilesParticipatingInRun(number, color)] = -1D;
        lpIn.addConstraint(v, 2, 0.0D);
    }

    private static void addConstraintType2(double v[], LpModel lpIn, int number, int color)
        throws Exception
    {
        Arrays.fill(v, 0.0D);
        if(number >= 2)
            v[getIndexNrOfTilesParticipatingInRun(number - 1, color)] = 1.0D;
        if(number <= 11)
            v[getIndexNrOfTilesParticipatingInRun(number + 2, color)] = 1.0D;
        v[getIndexNrOfTilesParticipatingInRun(number, color)] = -1D;
        lpIn.addConstraint(v, 2, 0.0D);
    }

    private static void addConstraintType3(double v[], LpModel lpIn, int number, int color)
        throws Exception
    {
        Arrays.fill(v, 0.0D);
        if(number >= 2)
            v[getIndexNrOfTilesParticipatingInRun(number - 1, color)] = 1.0D;
        if(number <= 12)
            v[getIndexNrOfTilesParticipatingInRun(number + 1, color)] = 1.0D;
        v[getIndexNrOfTilesParticipatingInRun(number, color)] = -1D;
        lpIn.addConstraint(v, 2, 0.0D);
    }

    private static void addConstraintType4(double v[], LpModel lpIn, int number, int color)
        throws Exception
    {
        Arrays.fill(v, 0.0D);
        int indexBlack = getIndexNrOfTilesParticipatingInGroup(number, 2);
        int indexBlue = getIndexNrOfTilesParticipatingInGroup(number, 4);
        int indexYellow = getIndexNrOfTilesParticipatingInGroup(number, 3);
        int indexRed = getIndexNrOfTilesParticipatingInGroup(number, 1);
        int indexCurrent = getIndexNrOfTilesParticipatingInGroup(number, color);
        v[indexBlack] = 1.0D;
        v[indexYellow] = 1.0D;
        v[indexBlue] = 1.0D;
        v[indexRed] = 1.0D;
        v[indexCurrent] = -2D;
        lpIn.addConstraint(v, 2, 0.0D);
    }

    private static void addConstraintType5(double v[], LpModel lpIn, int number, int color)
        throws Exception
    {
        Arrays.fill(v, 0.0D);
        v[getIndexNrOfTilesParticipating(number, color)] = -1D;
        v[getIndexNrOfTilesParticipatingInRun(number, color)] = 1.0D;
        v[getIndexNrOfTilesParticipatingInGroup(number, color)] = 1.0D;
        v[getIndexNrOfTilesRepresentedByJokers(number, color)] = -1D;
        lpIn.addConstraint(v, 1, 0.0D);
    }

    private static void addJokerConstraint(double v[], LpModel lpIn, int jokers)
        throws Exception
    {
        Arrays.fill(v, 0.0D);
        for(int c = 0; c < 4; c++)
        {
            for(int n = 1; n <= 13; n++)
                v[getIndexNrOfTilesRepresentedByJokers(n, Constants.ALL_COLORS[c])] = 1.0D;

        }

        lpIn.addConstraint(v, 0, jokers);
    }

    private static void addTileConstraint(double v[], LpModel lpIn, int number, int color, int numberOfTiles)
        throws Exception
    {
        Arrays.fill(v, 0.0D);
        v[getIndexNrOfTilesParticipating(number, color)] = 1.0D;
        lpIn.addConstraint(v, 0, numberOfTiles);
    }

    private static void addGoal(double v[], LpModel lpIn, Strategy strategy)
        throws Exception
    {
        strategy.setGoal(v);
        lpIn.setObjFn(v);
        lpIn.setMaximum();
    }

    private static void addIntegerStatements(LpModel lpIn)
        throws Exception
    {
        for(int c = 0; c < 4; c++)
        {
            for(int n = 1; n <= 13; n++)
            {
                lpIn.setInt(getIndexNrOfTilesParticipating(n, Constants.ALL_COLORS[c]), 1);
                lpIn.setInt(getIndexNrOfTilesParticipatingInRun(n, Constants.ALL_COLORS[c]), 1);
                lpIn.setInt(getIndexNrOfTilesParticipatingInGroup(n, Constants.ALL_COLORS[c]), 1);
                lpIn.setInt(getIndexNrOfTilesRepresentedByJokers(n, Constants.ALL_COLORS[c]), 1);
            }

        }

    }

    public static final void setVariableNames(LpModel lpIn)
        throws Exception
    {
        char paneIds[] = {
            'a', 'b', 'd', 'c'
        };
        String numberIds[] = {
            "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", 
            "11", "12", "13"
        };
        for(int c = 0; c < Constants.ALL_COLORS.length; c++)
        {
            for(int n = 1; n <= 13; n++)
            {
                for(int p = 0; p <= 3; p++)
                {
                    int index = getTileIndex(n, Constants.ALL_COLORS[c], p);
                    char paneId = paneIds[p];
                    String colorId = Constants.ALL_COLOR_LABELS[c];
                    String numberId = numberIds[n - 1];
                    String name = "" + paneId + "_" + colorId + "_" + numberId;
                    lpIn.setColumnName(lpIn, index, name);
                }

            }

        }

    }

    public static final Solution getSolutionFromLpModel(LpModel lpIn, TileCollection tileCollection)
        throws Exception
    {
        List tileSets = new ArrayList();
        DefaultTileCollection leftOver = new DefaultTileCollection();
        Util.copy(tileCollection, leftOver);
        TileCollectionListener listener = new SubstractingTileCollectionListener(leftOver);
        TileData tileData[][] = interpretSolution(lpIn);
        TileData element = null;
        for(int n = 0; n < 13; n++)
        {
            for(int c = 0; c < Constants.ALL_COLORS.length; c++)
            {
                for(element = tileData[c][n]; element.tilesInGroup > 0;)
                    tileSets.add(createGroupTileSet(tileData, n, c, listener));

                while(element.tilesInRun > 0) 
                    tileSets.add(createRunTileSet(tileData, n, c, listener));
            }

        }

        return new Solution(tileSets, leftOver);
    }

    private static final TileSet createGroupTileSet(TileData tileData[][], int n, int c, TileCollectionListener listener)
    {
        GroupTileSet tileSet = new GroupTileSet(n + 1, listener);
        int numberOfTwos = 0;
        boolean excluded[] = new boolean[4];
        for(int i = 0; i < 4; i++)
            if(tileData[i][n].tilesInGroup == 2)
            {
                excluded[i] = true;
                numberOfTwos++;
                tileData[i][n].tilesInGroup--;
                if(tileData[i][n].tilesJokers > 0)
                {
                    tileData[i][n].tilesJokers--;
                    tileSet.addJoker();
                } else
                {
                    tileSet.addColor(Constants.ALL_COLORS[i]);
                }
            } else
            {
                excluded[i] = false;
            }

        if(numberOfTwos != 0)
        {
            int needed = Math.max(3 - numberOfTwos, 0);
            for(int i = 0; i < 4 && needed > 0 && !excluded[i]; i++)
                if(tileData[i][n].tilesInGroup == 1)
                {
                    needed--;
                    tileData[i][n].tilesInGroup--;
                    if(tileData[i][n].tilesJokers > 0)
                    {
                        tileData[i][n].tilesJokers--;
                        tileSet.addJoker();
                    } else
                    {
                        tileSet.addColor(Constants.ALL_COLORS[i]);
                    }
                }

        } else
        {
            for(int i = 0; i < 4; i++)
                if(tileData[i][n].tilesInGroup == 1)
                {
                    tileData[i][n].tilesInGroup--;
                    if(tileData[i][n].tilesJokers > 0)
                    {
                        tileData[i][n].tilesJokers--;
                        tileSet.addJoker();
                    } else
                    {
                        tileSet.addColor(Constants.ALL_COLORS[i]);
                    }
                }

        }
        return tileSet;
    }

    private static final TileSet createRunTileSet(TileData tileData[][], int n, int c, TileCollectionListener listener)
    {
        int i = n;
        int threshold = tileData[c][n].tilesInRun;
        RunTileSet tileSet = new RunTileSet(Constants.ALL_COLORS[c], listener);
        TileData element = tileData[c][i];
        do
        {
            threshold = Math.max(threshold, element.tilesInRun);
            if(element.tilesJokers > 0)
            {
                element.tilesJokers--;
                tileSet.addTile(i + 1, true);
            } else
            {
                tileSet.addTile(i + 1, false);
            }
            element.tilesInRun--;
        } while(++i < 13 && (element = tileData[c][i]).tilesInRun >= threshold);
        return tileSet;
    }

    public static final TileData[][] interpretSolution(LpModel lpIn)
        throws Exception
    {
        TileData result[][] = new TileData[4][13];
        TileData element = null;
        int color = -1;
        int number = -1;
        for(int c = 0; c < Constants.ALL_COLORS.length; c++)
        {
            for(int n = 0; n < 13; n++)
            {
                color = Constants.ALL_COLORS[c];
                number = n + 1;
                element = new TileData();
                element.tilesInGroup = (int)lpIn.getBestSolution(lpIn.getRows() + getIndexNrOfTilesParticipatingInGroup(number, color));
                element.tilesInRun = (int)lpIn.getBestSolution(lpIn.getRows() + getIndexNrOfTilesParticipatingInRun(number, color));
                element.tilesJokers = (int)lpIn.getBestSolution(lpIn.getRows() + getIndexNrOfTilesRepresentedByJokers(number, color));
                element.tilesTotal = (int)lpIn.getBestSolution(lpIn.getRows() + getIndexNrOfTilesParticipating(number, color));
                result[c][n] = element;
            }

        }

        return result;
    }

    public static final void printTileData(TileData tileData[][])
    {
        TileData element = null;
        for(int i = 0; i < 4; i++)
        {
            System.out.print(Constants.ALL_COLOR_LABELS[i]);
            System.out.print("\t");
        }

        System.out.println();
        for(int n = 0; n < 13; n++)
        {
            for(int i = 0; i < 4; i++)
            {
                element = tileData[i][n];
                System.out.print("[");
                System.out.print(element.tilesInRun);
                System.out.print(",");
                System.out.print(element.tilesInGroup);
                System.out.print(",");
                System.out.print(element.tilesJokers);
                System.out.print(",");
                System.out.print(element.tilesTotal);
                System.out.print("]\t");
            }

            System.out.println();
        }

    }

    private Strategy strategies[] = {
        new MaximizeNrOfTilesStrategy(), new MaximizeTotalStrategy()
    };
    private Strategy strategy;


}
