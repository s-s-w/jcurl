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

/**
 * Convert the expression to infix notation.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class DomWalkerInfix extends DomWalker {

    public static String toString(final MathDom.Node n) {
        return toString(n, new StringBuffer()).toString();
    }

    public static StringBuffer toString(final MathDom.Node n,
            final StringBuffer buf) {
        final DomWalkerInfix w = new DomWalkerInfix(buf);
        w.walk(n);
        return w.b;
    }

    public final StringBuffer b;

    public DomWalkerInfix() {
        this(new StringBuffer());
    }

    public DomWalkerInfix(StringBuffer b) {
        this.b = b;
    }

    public void reset() {
        b.setLength(0);
    }

    public void walk(MathDom.BinaryOp n) {
        walk(n.left);
        b.append(' ').append(n.op).append(' ');
        walk(n.right);
    }

    public void walk(MathDom.Block n) {
        b.append('(');
        walk(n.arg);
        b.append(')');
    }

    public void walk(MathDom.Function n) {
        b.append(n.name).append('(');
        walk(n.arg);
        b.append(')');
    }

    public void walk(MathDom.Literal n) {
        b.append(n.val);
    }

    public void walk(MathDom.Parameter n) {
        b.append(n.name);
    }

    public void walk(MathDom.UnaryOp n) {
        b.append(n.op);
        walk(n.arg);
    }
}