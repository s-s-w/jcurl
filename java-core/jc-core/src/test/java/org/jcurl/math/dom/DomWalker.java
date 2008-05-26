/*
 * jcurl java curling software framework http://www.jcurl.org
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
 * Base class for dom tree walkers.
 * 
 * @see org.jcurl.math.dom.MathDom
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id:DomWalker.java 473 2007-03-26 12:24:21Z mrohrmoser $
 */
public abstract class DomWalker {

	public abstract void reset();

	public abstract void walk(MathDom.BinaryOp n);

	public abstract void walk(MathDom.Block n);

	public abstract void walk(MathDom.Function n);

	public abstract void walk(MathDom.Literal n);

	public void walk(final MathDom.Node n) {
		if (n instanceof MathDom.BinaryOp)
			this.walk((MathDom.BinaryOp) n);
		else if (n instanceof MathDom.Block)
			this.walk((MathDom.Block) n);
		else if (n instanceof MathDom.Function)
			this.walk((MathDom.Function) n);
		else if (n instanceof MathDom.Literal)
			this.walk((MathDom.Literal) n);
		else if (n instanceof MathDom.Parameter)
			this.walk((MathDom.Parameter) n);
		else if (n instanceof MathDom.UnaryOp)
			this.walk((MathDom.UnaryOp) n);
		else
			throw new IllegalStateException("Unknown node type ["
					+ n.getClass().getName() + "]");
	}

	public abstract void walk(MathDom.Parameter n);

	public abstract void walk(MathDom.UnaryOp n);
}