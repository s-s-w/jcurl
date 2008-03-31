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
package org.jcurl.math;

import org.apache.commons.logging.Log;
import org.jcurl.core.log.JCLoggerFactory;

public class NewtonSimpleSolver {
	public static final double eps = 1e-9;
	private static final Log log = JCLoggerFactory
			.getLogger(NewtonSimpleSolver.class);
	public static final int maxsteps = 100000;

	/**
	 * Compute <code>x where f^c(x) = y</code> using Newton's algorithm.
	 * 
	 * @param f
	 * @param dim
	 *            dimension (0,1,2,...)
	 * @param c
	 *            c'th derivative
	 * @param y
	 *            value
	 * @param x0
	 *            start value
	 * @param xstop
	 *            stop value, may be {@link Double#NaN}.
	 * @return x for getC(dim, c, x) = y, {@link Double#NaN} if there's no
	 *         solution.
	 */
	public static double computeNewtonValue(final R1RNFunction f,
			final int dim, final int c, final double y, final double x0,
			final double xstop) {
		int i = 0;
		for (double x = x0;;) {
			if (log.isDebugEnabled())
				log.debug("x=" + x + " y" + c + "=" + f.at(dim, c, x) + " y"
						+ (c + 1) + "=" + f.at(dim, c + 1, x));
			double dx = f.at(dim, c + 1, x);
			if (dx == 0)
				return Math.abs(f.at(dim, c, x) - y) < eps ? x : Double.NaN;
			dx = (f.at(dim, c, x) - y) / dx;
			x -= dx;
			if (!MathVec.isInside(x, x0, xstop, true))
				return Double.NaN;
			if (Math.abs(dx) < eps)
				return x;
			if (++i > maxsteps)
				throw new IllegalStateException("Maxsteps overflow. dx=" + dx
						+ " x=" + x + " f=" + f);
		}
	}

	/**
	 * Compute <code>x where f(x) = 0</code> using Newton's algorithm.
	 * 
	 * @see #computeNewtonZero(R1RNFunction, int, int, double, double)
	 * @param f
	 * @param c
	 *            c'th derivative
	 * @param x
	 *            start value
	 * @return x for getC(c, x) = 0
	 */
	public static double computeNewtonZero(final R1RNFunction f, final int c,
			final double x) {
		return computeNewtonZero(f, 0, c, x, Double.MAX_VALUE);
	}

	/**
	 * Compute <code>x where f(x) = 0</code> using Newton's algorithm.
	 * 
	 * @param f
	 * @param dim
	 *            dimension (0,1,2,...)
	 * @param c
	 *            c'th derivative
	 * @param x0
	 *            start value
	 * @param xstop
	 *            stop value
	 * @return x for getC(dim, c, x) = 0, {@link Double#NaN} for "no solution".
	 */
	public static double computeNewtonZero(final R1RNFunction f, final int dim,
			final int c, final double x0, final double xstop) {
		return computeNewtonValue(f, dim, c, 0, x0, xstop);
	}
}