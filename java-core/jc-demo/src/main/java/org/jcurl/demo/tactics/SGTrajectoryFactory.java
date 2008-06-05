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
package org.jcurl.demo.tactics;

import java.awt.BasicStroke;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.Iterator;
import java.util.Map.Entry;

import org.jcurl.core.api.Factory;
import org.jcurl.core.api.RockProps;
import org.jcurl.core.ui.IceShapes;
import org.jcurl.math.CurveShape;
import org.jcurl.math.Interpolator;
import org.jcurl.math.Interpolators;
import org.jcurl.math.R1RNFunction;

import com.sun.scenario.scenegraph.SGGroup;
import com.sun.scenario.scenegraph.SGShape;
import com.sun.scenario.scenegraph.SGAbstractShape.Mode;

/**
 * Create an unpickable {@link SGGroup} for a combined curve describing the path
 * of one rock.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id:PTrajectoryFactory.java 795 2008-03-19 13:40:42Z mrohrmoser $
 */
public abstract class SGTrajectoryFactory implements Factory {
	public static class Fancy extends SGTrajectoryFactory {

		private static final Paint dark = IceShapes.trace(
				new IceShapes.RockColors().dark, 255);
		/** sampling style along the {@link R1RNFunction} */
		private static final Interpolator ip = Interpolators
				.getLinearInstance();
		private static final Paint light = IceShapes.trace(
				new IceShapes.RockColors().light, 255);
		/** start + stop + intermediate */
		private static final int samples = 25;
		private static final Stroke stroke = new BasicStroke(
				2 * RockProps.DEFAULT.getRadius(), BasicStroke.CAP_ROUND,
				BasicStroke.JOIN_ROUND, 0);
		private static final float zoom = 1;

		@Override
		protected boolean addSegment(final R1RNFunction src, final double tmin,
				final double tmax, final boolean isDark, final SGGroup dst) {
			// TODO move to #refresh
			if (tmin >= tmax)
				return false;
			// create a high-quality scenegraph entity
			final SGShape c = new SGShape();
			c.setShape(CurveShape.approximateLinear(src, (float) tmin,
					(float) tmax, samples, zoom, ip));
			c.setDrawStroke(stroke);
			c.setDrawPaint(isDark ? dark : light);
			c.setAntialiasingHint(RenderingHints.VALUE_ANTIALIAS_ON);
			c.setMode(Mode.STROKE);
			dst.add(c);
			return true;
		}
	}

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
	 * @return <code>false</code> if out of scope.
	 */
	protected abstract boolean addSegment(R1RNFunction src, double tmin,
			double tmax, boolean isDark, SGGroup dst);

	/**
	 * Replace the children of <code>dst</code> with the curve segments from
	 * <code>src</code>.
	 * <p>
	 * This method does all the management stuff and delegates to
	 * {@link #addSegment(R1RNFunction, double, double, boolean, SGGroup)} to
	 * create the visual parts of the path.
	 * </p>
	 */
	public SGGroup refresh(final Iterator<Entry<Double, R1RNFunction>> src,
			final boolean isDark, final double tmin, final double tmax,
			SGGroup dst) {
		if (dst == null)
			dst = new SGGroup();
		final boolean vis = dst.isVisible();
		try {
			dst.setVisible(false);
			// remove all children of dst:
			for (int i = dst.getChildren().size() - 1; i >= 0; i--)
				dst.remove(i);

			if (!src.hasNext())
				return dst;
			Entry<Double, R1RNFunction> curr = src.next();
			while (src.hasNext()) {
				final Entry<Double, R1RNFunction> next = src.next();
				if (!addSegment(curr.getValue(), curr.getKey(), next.getKey(),
						isDark, dst))
					break;
				// step:
				curr = next;
			}
			// don't forget the tail (last segment):
			addSegment(curr.getValue(), curr.getKey(), tmax, isDark, dst);
			// TODO is this really necessary?
			dst.setMouseBlocker(true);
		} finally {
			dst.setVisible(vis);
		}
		return dst;
	}
}
