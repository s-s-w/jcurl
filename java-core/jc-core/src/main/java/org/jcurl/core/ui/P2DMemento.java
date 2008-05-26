/*
 * jcurl curling simulation framework http://www.jcurl.org Copyright (C)
 * 2005-2008 M. Rohrmoser
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */

package org.jcurl.core.ui;

import java.awt.geom.Point2D;

/**
 * Change of a {@link Point2D}.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public abstract class P2DMemento<E> extends Memento<E> {
	/** Read-only access. */
	protected final Point2D p = new Point2D() {

		@Override
		public double getX() {
			return x;
		}

		@Override
		public double getY() {
			return y;
		}

		@Override
		public void setLocation(double x, double y) {
			throw new UnsupportedOperationException();
		}

	};
	private final double x;
	private final double y;

	protected P2DMemento(final E context, final double x, final double y) {
		super(context);
		this.x = x;
		this.y = y;
	}

	protected P2DMemento(final E context, final Point2D p) {
		this(context, p.getX(), p.getY());
	}
}
