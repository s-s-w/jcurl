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

import org.apache.commons.logging.Log;
import org.jcurl.core.log.JCLoggerFactory;

/**
 * Bisection root-finding algorithm.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class BisectionSolver {
	private static final Log log = JCLoggerFactory
			.getLogger(BisectionSolver.class);

	/**
	 * Bisection root-finding algorithm. Ported from
	 * http://en.wikipedia.org/wiki/Bisection_method
	 * 
	 * @param f
	 *            the function to test
	 * @param y
	 *            y value to find x for. 0 finds the root.
	 * @param left
	 *            lower end of the interval to scan
	 * @param right
	 *            upper end of the interval to scan
	 * @param epsilon
	 *            iteration stops when
	 *            <code>Math.abs(right - left) <= epsilon</code>
	 */
	public static double findRoot(final R1R1Function f, final double y,
			double left, double right, final double epsilon) {
		double lefty = f.at(left);
		if (Double.isNaN(lefty))
			return Double.NaN;
		// check if left-y an right-y have opposite signs. Use signum to avoid
		// overflows.
		if (Math.signum(lefty) * f.at(right) > 0)
			return Double.NaN;
		double midy = Double.NaN;
		while (Math.abs(right - left) > epsilon) {
			// Calculate midpoint of domain
			final double midpoint = (right + left) / 2;
			midy = f.at(midpoint);
			if (log.isDebugEnabled())
				log.debug("f(" + left + ")=" + lefty + " f(" + midpoint + ")="
						+ midy + " f(" + right + ")=unused");
			if (Double.isNaN(midy))
				return Double.NaN;
			// examine midy
			if ((lefty - y) * (midy - y) > 0) {
				// Throw away left half
				left = midpoint;
				lefty = midy;
			} else
				// Throw away right half
				right = midpoint;
		}
		return (right + left) / 2;
	}
}