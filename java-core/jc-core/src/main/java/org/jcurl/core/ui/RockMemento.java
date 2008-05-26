/*
 * jcurl java curling software framework http://www.jcurl.org Copyright (C)
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

import org.jcurl.core.api.Rock;
import org.jcurl.core.api.RockSet;
import org.jcurl.core.api.RockType;

/**
 * Change of a {@link Rock}.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public abstract class RockMemento<R extends RockType> extends
		P2DMemento<RockSet<R>> {
	private final int idx16;

	protected RockMemento(final RockSet<R> context, final int idx16,
			final double x, final double y) {
		super(context, x, y);
		this.idx16 = idx16;
	}

	protected RockMemento(final RockSet<R> context, final int idx16,
			final Point2D p) {
		this(context, idx16, p.getX(), p.getY());
	}

	@Override
	public RockSet<R> apply(final RockSet<R> dst) {
		dst.getRock(idx16).p().setLocation(p.getX(), p.getY());
		// FIXME make the following line obsolete:
		dst.fireStateChanged();
		return dst;
	}
}
