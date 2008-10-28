// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SolverServlet.java

package com.agilejava.rummikub.wui;

import com.agilejava.rummikub.core.*;
import com.agilejava.rummikub.solvers.lpsolve.LPSolveBasedSolver;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class SolverServlet extends HttpServlet
{
    private static class HtmlRenderingVisitor
        implements TileSetVisitor
    {

        public void beforeFirst()
        {
            writer.write("<table cellspacing=\"0\" border=\"0\">");
            writer.write("<tr>");
        }

        public void afterLast()
        {
            writer.write("</tr>");
            writer.write("</table>");
        }

        public void visitJoker()
        {
            writeTileHeader();
            writer.write("J");
            writeTileFooter();
        }

        public void visitNumberedTile(int number, int color)
        {
            writeTileHeader();
            String cl = null;
            switch(color)
            {
            case 2: // '\002'
                cl = "black-tile";
                break;

            case 1: // '\001'
                cl = "red-tile";
                break;

            case 3: // '\003'
                cl = "yellow-tile";
                break;

            case 4: // '\004'
                cl = "blue-tile";
                break;
            }
            writer.write("<span class=\"");
            writer.write(cl);
            writer.write("\">");
            writer.write(Integer.toString(number));
            writer.write("</span>");
            writeTileFooter();
        }

        private void writeTileHeader()
        {
            writer.write("<td>");
            writer.write("<div class=\"tile-top\">");
            writer.write("<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\">");
            writer.write("<tr><td class=\"tile-top\" width=\"37\" height=\"37\" valign=\"middle\" align=\"center\">");
        }

        private void writeTileFooter()
        {
            writer.write("</td></tr>");
            writer.write("<tr><td class=\"tile-bottom\" width=\"37\" height=\"37\" valign=\"middle\" align=\"center\">");
            writer.write("</td></tr>");
            writer.write("</table>");
        }

        private PrintWriter writer;

        public HtmlRenderingVisitor(Writer writer)
        {
            this.writer = new PrintWriter(writer);
        }
    }


    public SolverServlet()
    {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        response.setContentType("text/html");
        Writer writer = response.getWriter();
        DefaultTileCollection tileCollection = new DefaultTileCollection();
        String name = null;
        int value = 0;
        for(int c = 0; c < 4; c++)
        {
            for(int n = 1; n <= 13; n++)
            {
                name = "tiles-" + c + "-" + n;
                value = Integer.parseInt(request.getParameter(name));
                tileCollection.setNumberOfTiles(n, Constants.ALL_COLORS[c], value);
            }

        }

        value = Integer.parseInt(request.getParameter("jokers"));
        tileCollection.setNumberOfJokers(value);
        TileSetSolver solver = new LPSolveBasedSolver();
        solver.setStrategy(Integer.parseInt(request.getParameter("strategy")));
        Solution solution = solver.solve(tileCollection);
        TileSetVisitor visitor = new HtmlRenderingVisitor(writer);
        writer.write("<html>");
        writer.write("<head>");
        writer.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"/rummikub/css/pretty-table.css\">");
        writer.write("<title>Solution</title>");
        writer.write("</head>");
        writer.write("<body>");
        writer.write("<img src=\"/rummikub/img/jummikub.png\"><br>");
        writer.write("<p>Solution:</p>");
        solution.acceptTileSetVisitor(visitor);
        writer.write("<p>Left-over:</p>");
        solution.getLeftOver().acceptTileSetVisitor(visitor);
        writer.write("</body>");
        writer.write("</html>");
    }
}
