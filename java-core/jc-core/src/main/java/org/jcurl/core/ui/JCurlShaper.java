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

import java.awt.Shape;

import org.apache.commons.logging.Log;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.math.Interpolator;
import org.jcurl.math.Interpolators;
import org.jcurl.math.NaturalShaper;
import org.jcurl.math.R1RNFunction;
import org.jcurl.math.Shaper;
import org.jcurl.math.ShaperUtils;

/**
 * The default jcurl {@link Shaper}.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class JCurlShaper extends NaturalShaper {

	private static final boolean DROP_STOP = true;
	private static final Interpolator ip = Interpolators.getQuadraticInstance();
	private static final Log log = JCLoggerFactory.getLogger(JCurlShaper.class);
	private static final double MIN_LEN = 1e-6;
	private final int samples;
	private final float zoom;

	/** use 20 samples */
	public JCurlShaper() {
		this(20);
	}

	private JCurlShaper(final int samples) {
		this(samples, 1.0F);
	}

	/**
	 * Sample the input interval into <code>N-1</code> segments and use
	 * {@link Interpolators#getQuadraticInstance()} and
	 * {@link ShaperUtils#approximateLinear(R1RNFunction, double, double, int, float, Interpolator)}
	 * to get the resulting shape.
	 * 
	 * @param samples
	 *            must be &gt;= 2
	 * @param zoom
	 *            an optional graphics zoom factor - usually 1.0F
	 */
	private JCurlShaper(final int samples, final float zoom) {
		this.samples = samples;
		this.zoom = zoom;
	}

	@Override
	public Shape toShape(final R1RNFunction f, final double tmin,
			final double tmax) {
		{
			final Shape s = super.toShape(f, tmin, tmax);
			if (s != null)
				return s;
		}
		if (DROP_STOP) {
			// treat some special cases:
			if (f == null || tmin + MIN_LEN >= tmax)
				return null;
			final double x = f.at(0, 0, tmin) - f.at(0, 0, tmax);
			final double y = f.at(1, 0, tmin) - f.at(1, 0, tmax);
			if (x * x + y * y <= MIN_LEN * MIN_LEN)
				return null;
		}
		return ShaperUtils.approximateLinear(f, (float) tmin, (float) tmax,
				samples, zoom, ip);
	}
}
