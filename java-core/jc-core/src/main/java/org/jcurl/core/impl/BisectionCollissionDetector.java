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

	/**
	 * Provide an "unrolled" implementation to save about 1/3rd function calls.
	 * 
	 * @see Distance2DSq
	 */
	private static class Distance2DSqFlipped extends R1R1Function {
		private static final long serialVersionUID = 6430453872388677378L;

		private final R1RNFunction c1;
		private final R1RNFunction c2;
		private final double r2;

		public Distance2DSqFlipped(final R1RNFunction c1,
				final R1RNFunction c2, final double sqr) {
			this.c1 = c1;
			this.c2 = c2;
			r2 = sqr;
		}

		/**
		 * @see Distance2DSq#at(double, int)
		 */
		@Override
		public double at(final double t, final int c) {
			if (c == 0 || c == 1) {
				final double a_minus_b_0 = c1.at(t, c, 0) - c2.at(t, c, 0);
				final double a_minus_b_1 = c1.at(t, c, 1) - c2.at(t, c, 1);
				final double C0 = a_minus_b_0 * a_minus_b_0 + a_minus_b_1
						* a_minus_b_1 - r2;

				double C1 = 0.0;
				final double da_minus_db_0 = c1.at(t, c + 1, 0)
						- c2.at(t, c + 1, 0);
				final double da_minus_db_1 = c1.at(t, c + 1, 1)
						- c2.at(t, c + 1, 1);

				C1 += a_minus_b_0 * da_minus_db_0;
				C1 += a_minus_b_1 * da_minus_db_1;
				C1 *= 2.0;

				if (c == 0)
					return C0 * -Math.signum(C1);
				if (c == 1)
					return -Math.abs(C1);
			}
			throw new IllegalArgumentException();
		}

		public double unflipped(final double t) {
			final int c = 0;
			final double a_minus_b_0 = c1.at(t, c, 0) - c2.at(t, c, 0);
			final double a_minus_b_1 = c1.at(t, c, 1) - c2.at(t, c, 1);
			return a_minus_b_0 * a_minus_b_0 + a_minus_b_1 * a_minus_b_1 - r2;
		}

	}

	private static final Log log = JCLoggerFactory
			.getLogger(BisectionCollissionDetector.class);

	private static final long serialVersionUID = 5332814969180777771L;

	public double compute(final double t0, final double tstop,
			final R1RNFunction fa, final R1RNFunction fb, final double distSq) {
		if (false) {
			// TUNE test only rocks really in motion
			final double[] tmp = { 0, 0, 0 };
			fa.at(t0, 1, tmp);
			final boolean fa_still = tmp[0] * tmp[0] + tmp[1] * tmp[1] < 1e-9;
			fb.at(t0, 1, tmp);
			if (fa_still && tmp[0] * tmp[0] + tmp[1] * tmp[1] < 1e-9)
				return Double.NaN;
		}
		if (false) {
			final R1R1Function dist = new Distance2DSq(fa, fb, 0);
			final R1R1Function f = new R1R1Function() {
				private static final long serialVersionUID = 7051701140539614770L;

				@Override
				public double at(final double x, final int c) {
					if (c == 0)
						return dist.at(x, 0) * -Math.signum(dist.at(x, 1));
					if (c == 1)
						return Math.abs(dist.at(x, 1));
					throw new IllegalArgumentException();
				}
			};
			final double r = BisectionSolver.findRoot(f,
					CollissionDetector.RR2, t0, tstop, 1e-9);
			if (log.isDebugEnabled())
				log.debug(dist.at(r) - CollissionDetector.RR2);
			if (Double.isNaN(r)
					|| Math.abs(dist.at(r) - CollissionDetector.RR2) > 1e-6)
				return Double.NaN;
			return r;
		} else {
			final Distance2DSqFlipped f = new Distance2DSqFlipped(fa, fb, 0);
			final double r = BisectionSolver.findRoot(f,
					CollissionDetector.RR2, t0, tstop, 1e-9);
			if (log.isDebugEnabled())
				log.debug(f.unflipped(r) - CollissionDetector.RR2);
			if (Double.isNaN(r)
					|| Math.abs(f.unflipped(r) - CollissionDetector.RR2) > 1e-6)
				return Double.NaN;
			return r;
		}
	}
}
