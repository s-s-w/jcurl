/*
 * jcurl java curling software framework http://www.jcurl.org
 * Copyright (C) 2005-2009 M. Rohrmoser
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
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id:DomWalkerInfix.java 473 2007-03-26 12:24:21Z mrohrmoser $
 */
public class DomWalkerInfix extends DomWalker {

	public static String toString(final MathDom.Node n) {
		return toString(n, new StringBuilder()).toString();
	}

	public static StringBuilder toString(final MathDom.Node n,
			final StringBuilder buf) {
		final DomWalkerInfix w = new DomWalkerInfix(buf);
		w.walk(n);
		return w.b;
	}

	public final StringBuilder b;

	public DomWalkerInfix() {
		this(new StringBuilder());
	}

	public DomWalkerInfix(final StringBuilder b) {
		this.b = b;
	}

	@Override
	public void reset() {
		b.setLength(0);
	}

	@Override
	public void walk(final MathDom.BinaryOp n) {
		this.walk(n.left);
		b.append(' ').append(n.op).append(' ');
		this.walk(n.right);
	}

	@Override
	public void walk(final MathDom.Block n) {
		b.append('(');
		this.walk(n.arg);
		b.append(')');
	}

	@Override
	public void walk(final MathDom.Function n) {
		b.append(n.name).append('(');
		this.walk(n.arg);
		b.append(')');
	}

	@Override
	public void walk(final MathDom.Literal n) {
		b.append(n.val);
	}

	@Override
	public void walk(final MathDom.Parameter n) {
		b.append(n.name);
	}

	@Override
	public void walk(final MathDom.UnaryOp n) {
		b.append(n.op);
		this.walk(n.arg);
	}
}