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

import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.jcurl.core.api.CurveStore;
import org.jcurl.core.api.Factory;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.math.R1RNFunction;

/**
 * Create a scenegraph node for a combined curve describing the path of one
 * rock.
 * 
 * <N> scenegraph group node type
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id:PTrajectoryFactory.java 795 2008-03-19 13:40:42Z mrohrmoser $
 */
public abstract class GenTrajectoryFactory<N> implements Factory {

	private static final Log log = JCLoggerFactory
			.getLogger(GenTrajectoryFactory.class);

	/**
	 * Create a visual path segment and add it to <code>dst</code>.
	 * 
	 * @param src
	 *            input curve
	 * @param tmin
	 * @param tmax
	 * @param isDark
	 *            rock color
	 * @param dst
	 *            scenegraph parent
	 */
	protected abstract void addSegment(R1RNFunction src, double tmin,
			double tmax, boolean isDark, N dst);

	protected abstract N post(N parent);

	protected abstract N pre(N parent);

	/**
	 * Replace the children of <code>dst</code> with the curve segments from
	 * <code>src</code>.
	 * <p>
	 * This method does all the management stuff and delegates to
	 * {@link #addSegment(R1RNFunction, double, double, boolean, Object)} to
	 * create the visual parts of the path.
	 * </p>
	 * 
	 * @param src
	 *            curve to visualize - see {@link CurveStore#iterator(int)}
	 * @param tmin
	 * @param tmax
	 * @param isDark
	 * @param dst
	 *            scenegraph parent node to clear and re-populate.
	 */
	public N refresh(final Iterator<Entry<Double, R1RNFunction>> src,
			final boolean isDark, final double tmin, final double tmax, N dst) {
		log.debug("curve");
		dst = pre(dst);

		if (!src.hasNext())
			return dst;
		Entry<Double, R1RNFunction> curr = src.next();
		while (src.hasNext()) {
			final Entry<Double, R1RNFunction> next = src.next();
			if (next.getKey() <= tmin) {
				curr = next;
				continue;
			}
			if (next.getKey() >= tmax)
				break;

			double t0 = curr.getKey();
			if (t0 < tmin)
				t0 = tmin;
			addSegment(curr.getValue(), t0, next.getKey(), isDark, dst);
			// step:
			curr = next;
		}

		// don't forget the tail (last segment):
		double t0 = curr.getKey();
		if (t0 < tmin)
			t0 = tmin;
		addSegment(curr.getValue(), t0, tmax, isDark, dst);
		return post(dst);
	}
}
