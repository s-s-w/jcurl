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

package org.jcurl.math;

import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.util.Iterator;
import java.util.Map.Entry;

import org.jcurl.core.ui.GenTrajectoryFactory;

/**
 * Use {@link Shapeable#toShape(double, double)} only.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class NaturalShaper implements Shaper {

	/**
	 * Connect the output from {@link #toShapes(Iterator, double, double)} into
	 * one.
	 */
	public Shape toShape(final Iterator<Entry<Double, R1RNFunction>> f,
			final double tmin, final double tmax) {
		final GeneralPath p = new GeneralPath();
		for (final Iterator<Shape> it = toShapes(f, tmin, tmax); it.hasNext();)
			p.append(it.next(), true);
		return p;
	}

	public Shape toShape(final R1RNFunction f, final double tmin,
			final double tmax) {
		if (f instanceof Shapeable)
			return ((Shapeable) f).toShape(tmin, tmax);
		return null;
	}

	/**
	 * TODO Put the management logic from
	 * {@link GenTrajectoryFactory#refresh(Iterator, boolean, double, double, Object)}
	 * here.
	 */
	public Iterator<Shape> toShapes(
			final Iterator<Entry<Double, R1RNFunction>> f, final double tmin,
			final double tmax) {
		throw new UnsupportedOperationException();
	}

}
