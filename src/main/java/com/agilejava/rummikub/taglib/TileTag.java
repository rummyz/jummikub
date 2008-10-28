// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TileTag.java

package com.agilejava.rummikub.taglib;

import com.agilejava.rummikub.core.Solution;
import java.io.IOException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

public class TileTag extends TagSupport
{

    public TileTag()
    {
    }

    public void setAttribute(String attribute)
    {
        this.attribute = attribute;
    }

    public String getAttribute()
    {
        return attribute;
    }

    public int doEndTag()
        throws JspException
    {
        try
        {
            java.io.PrintWriter writer = super.pageContext.getResponse().getWriter();
            Solution solution = (Solution)super.pageContext.getRequest().getAttribute("solution");
            return 0;
        }
        catch(IOException ioe)
        {
            throw new JspException();
        }
    }

    private String attribute;
}
