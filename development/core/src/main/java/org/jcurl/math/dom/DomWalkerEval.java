/*
 * jcurl curling simulation framework 
 * Copyright (C) 2005 M. Rohrmoser
 * 
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation; either version 2 of the License, or (at your
 * option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.jcurl.math.dom;

import java.util.Map;

/**
 * Compute the value of the expression.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class DomWalkerEval extends DomWalker {

    public static double eval(final MathDom.Node n) {
        return eval(n, null);
    }

    public static double eval(final MathDom.Node n, final Map params) {
        final DomWalkerEval w = new DomWalkerEval(params);
        w.walk(n);
        return w.doubleValue();
    }

    private final Map params;

    private double v = 0;

    public DomWalkerEval() {
        this(null);
    }

    public DomWalkerEval(Map params) {
        this.params = params;
    }

    public double doubleValue() {
        return v;
    }

    public void reset() {
        v = 0;
    }

    public void walk(MathDom.BinaryOp n) {
        v = 0;
        final double l;
        if (n.op == '=') {
            l = 0;
        } else {
            walk(n.left);
            l = v;
            v = 0;
        }
        walk(n.right);
        final double r = v;
        switch (n.op) {
        case '=':
            if (params == null)
                throw new IllegalArgumentException(
                        "I need a parameter map to handle assignments.");
            final Double t = new Double(r);
            params.put(((MathDom.Parameter) n.left).name, t);
            v = t.doubleValue();
        case '+':
            v = l + r;
            return;
        case '-':
            v = l - r;
            return;
        case '*':
            v = l * r;
            return;
        case '/':
            v = l / r;
            return;
        default:
            throw new IllegalArgumentException("Unknown operator [" + n.op
                    + "]");
        }
    }

    public void walk(MathDom.Block n) {
        walk(n.arg);
    }

    public void walk(MathDom.Function n) {
        walk(n.arg);
        final String fkt = n.name;
        if ("sin".equals(fkt))
            v = Math.sin(v);
        else if ("abs".equals(fkt))
            v = Math.abs(v);
        else if ("sqrt".equals(fkt))
            v = Math.sqrt(v);
        else
            throw new IllegalArgumentException("Unknown function [" + fkt + "]");
    }

    public void walk(MathDom.Literal n) {
        v = n.val;
    }

    public void walk(MathDom.Parameter n) {
        final Object t = params.get(n.name);
        if (t == null || !(t instanceof Number))
            throw new IllegalArgumentException("Parameter [" + n.name
                    + "] not known.");
        v = ((Double) t).doubleValue();
    }

    public void walk(MathDom.UnaryOp n) {
        walk(n.arg);
        switch (n.op) {
        case '-':
            v = -v;
            break;
        default:
            throw new IllegalArgumentException("Unknown operator [" + n.op
                    + "]");
        }
    }
}