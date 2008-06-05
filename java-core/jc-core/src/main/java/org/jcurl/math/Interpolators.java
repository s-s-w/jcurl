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

/**
 * Provides a number of built-in implementations of the {@link Interpolator}
 * interface.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class Interpolators {

	protected static class LinearInterpolator implements Interpolator {
		public float interpolate(final float fraction) {
			if (Float.isNaN(fraction) || fraction < 0.0F || fraction > 1.0F)
				return Float.NaN;
			return fraction;
		}
	}

	private static final Interpolator exponential = new LinearInterpolator() {
		@Override
		public float interpolate(final float fraction) {
			if (Float.isNaN(super.interpolate(fraction)))
				return Float.NaN;
			if (0.0F == fraction || 1.0F == fraction)
				return fraction;
			return 1.0F - (float) (Math.exp(1.0 - fraction) / Math.exp(1.0));
		}
	};

	private static final Interpolator linear = new LinearInterpolator();

	/** Getting exponentially denser towards <code>1.0F</code>. */
	public static Interpolator getExponentialInstance() {
		return exponential;
	}

	/**
	 * Returns a trivial implementation of Interpolator that provides linear
	 * time interpolation (the input "t" value is simply returned unmodified).
	 * 
	 * @return an instance of Interpolator that provides simple linear time
	 *         interpolation
	 */
	public static Interpolator getLinearInstance() {
		return linear;
	}
}
