/*
 * jcurl curling simulation framework http://www.jcurl.org
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
 * Convert the expression to postfix (reverse polish) notation.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class DomWalkerPostfix extends DomWalker {

    public static String toString(final MathDom.Node n) {
        return toString(n, new StringBuffer()).toString();
    }

    public static StringBuffer toString(final MathDom.Node n,
            final StringBuffer buf) {
        final DomWalkerPostfix w = new DomWalkerPostfix(buf);
        w.walk(n);
        return w.buf;
    }

    public final StringBuffer buf;

    public DomWalkerPostfix() {
        this(new StringBuffer());
    }

    public DomWalkerPostfix(StringBuffer b) {
        buf = b;
    }

    public void reset() {
        buf.setLength(0);
    }

    public void walk(MathDom.BinaryOp n) {
        buf.append(' ');
        buf.append(n.op).append(' ');
        this.walk(n.left);
        buf.append(' ');
        this.walk(n.right);
    }

    public void walk(MathDom.Block n) {
        this.walk(n.arg);
    }

    public void walk(MathDom.Function n) {
        buf.append(n.name).append(' ');
        this.walk(n.arg);
    }

    public void walk(MathDom.Literal n) {
        buf.append(n.val);
    }

    public void walk(MathDom.Parameter n) {
        buf.append(n.name);
    }

    public void walk(MathDom.UnaryOp n) {
        buf.append(n.op);
        this.walk(n.arg);
    }
}