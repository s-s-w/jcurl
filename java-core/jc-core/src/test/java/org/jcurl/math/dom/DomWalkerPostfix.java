/*
 * jcurl curling simulation framework http://www.jcurl.org
 * Copyright (C) 2005-2008 M. Rohrmoser
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
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id:DomWalkerPostfix.java 473 2007-03-26 12:24:21Z mrohrmoser $
 */
public class DomWalkerPostfix extends DomWalker {

	public static String toString(final MathDom.Node n) {
		return toString(n, new StringBuilder()).toString();
	}

	public static StringBuilder toString(final MathDom.Node n,
			final StringBuilder buf) {
		final DomWalkerPostfix w = new DomWalkerPostfix(buf);
		w.walk(n);
		return w.buf;
	}

	public final StringBuilder buf;

	public DomWalkerPostfix() {
		this(new StringBuilder());
	}

	public DomWalkerPostfix(final StringBuilder b) {
		buf = b;
	}

	@Override
	public void reset() {
		buf.setLength(0);
	}

	@Override
	public void walk(final MathDom.BinaryOp n) {
		buf.append(' ');
		buf.append(n.op).append(' ');
		this.walk(n.left);
		buf.append(' ');
		this.walk(n.right);
	}

	@Override
	public void walk(final MathDom.Block n) {
		this.walk(n.arg);
	}

	@Override
	public void walk(final MathDom.Function n) {
		buf.append(n.name).append(' ');
		this.walk(n.arg);
	}

	@Override
	public void walk(final MathDom.Literal n) {
		buf.append(n.val);
	}

	@Override
	public void walk(final MathDom.Parameter n) {
		buf.append(n.name);
	}

	@Override
	public void walk(final MathDom.UnaryOp n) {
		buf.append(n.op);
		this.walk(n.arg);
	}
}