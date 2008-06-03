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

package org.jcurl.core.impl;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.jcurl.core.api.CollissionDetector;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.math.BisectionSolver;
import org.jcurl.math.Distance2DSq;
import org.jcurl.math.R1R1Function;
import org.jcurl.math.R1RNFunction;

/**
 * Collission detector based on {@link Distance2DSq} and {@link BisectionSolver}.
 * <p>
 * As the {@link NewtonCollissionDetector} got stuck in an endless loop under
 * certain circumstances (see the
 * <code>CollissionDetectorTest#testHngrrr()</code> JUnit test) let's try
 * bisection.
 * </p>
 * <p>
 * As crossing curves have a hit-distance 2 times we use a trick and use the
 * opposite sign of the first derivative to flip the "separating" half of the
 * distance function into negative y values. Now bisection can find the hit.
 * </p>
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class BisectionCollissionDetector extends CollissionDetectorBase
		implements Serializable {

	private static final Log log = JCLoggerFactory
			.getLogger(BisectionCollissionDetector.class);
	private static final long serialVersionUID = 5332814969180777771L;

	public double compute(final double t0, final double tstop,
			final R1RNFunction fa, final R1RNFunction fb, final double distSq) {
		if (false) {
			// TUNE test only rocks really in motion
			final double[] tmp = { 0, 0, 0 };
			fa.at(1, t0, tmp);
			final boolean fa_still = tmp[0] * tmp[0] + tmp[1] * tmp[1] < 1e-9;
			fb.at(1, t0, tmp);
			if (fa_still && tmp[0] * tmp[0] + tmp[1] * tmp[1] < 1e-9)
				return Double.NaN;
		}
		final R1R1Function dist = new Distance2DSq(fa, fb, 0);
		final R1R1Function f = new R1R1Function() {
			private static final long serialVersionUID = 7051701140539614770L;

			@Override
			public double at(final int c, final double x) {
				if (c == 0)
					return dist.at(0, x) * -Math.signum(dist.at(1, x));
				if (c == 1)
					return Math.abs(dist.at(1, x));
				throw new IllegalArgumentException();
			}
		};
		final double r = BisectionSolver.findRoot(f, CollissionDetector.RR2,
				t0, tstop, 1e-9);
		if (log.isDebugEnabled())
			log.debug(dist.at(r) - CollissionDetector.RR2);
		if (Double.isNaN(r)
				|| Math.abs(dist.at(r) - CollissionDetector.RR2) > 1e-6)
			return Double.NaN;
		return r;
	}
}
