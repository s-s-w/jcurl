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
package org.jcurl.core.impl;

import org.jcurl.core.api.CurveRock;
import org.jcurl.core.api.Rock;
import org.jcurl.core.api.RockType;
import org.jcurl.core.api.RockType.Pos;

/**
 * Curve implementation for rocks NOT in motion.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id:CurveStill.java 682 2007-08-12 21:25:04Z mrohrmoser $
 */
public class CurveStill<T extends RockType> extends CurveRock<T> {
	private static final long serialVersionUID = -8031863193302315171L;

	public static CurveStill<Pos> newInstance(final double x, final double y,
			final double a) {
		return new CurveStill<Pos>(x, y, a);
	}

	public static CurveStill<Pos> newInstance(final double[] x) {
		return CurveStill.newInstance(x[0], x[1], x[2]);
	}

	public static CurveStill<Pos> newInstance(final Rock<Pos> x) {
		return CurveStill.newInstance(x.getX(), x.getY(), x.getA());
	}

	private final double a;
	private final double x;
	private final double y;

	private CurveStill(final double x, final double y, final double a) {
		this.x = x;
		this.y = y;
		this.a = a;
	}

	private CurveStill(final Rock<T> x) {
		this(x.getX(), x.getY(), x.getA());
	}

	@Override
	public double at(final double t, final int c, final int dim) {
		if (c > 0)
			return 0;
		switch (dim) {
		case 0:
			return x;
		case 1:
			return y;
		case 2:
			return a;
		default:
			throw new IllegalArgumentException();
		}
	}

	@Override
	public String toString() {
		final StringBuilder buf = new StringBuilder();
		buf.append('[');
		buf.append(x).append(", ");
		buf.append(y).append(", ");
		buf.append(a);
		buf.append(']');
		return buf.toString();
	}
}
