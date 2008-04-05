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

/**
 * Bisection rootfinding algorithm.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class BisectionSolver {
	/**
	 * Ported from http://en.wikipedia.org/wiki/Bisection_method
	 * 
	 * @param y
	 *            TODO
	 */
	public static double findRoot(final R1R1Function f, final double y,
			double left, double right) {
		double lefty = f.at(left);
		if (Double.isNaN(lefty))
			return Double.NaN;
		final double epsilon_x_2 = 1e-9;
		double midy = Double.NaN;
		while (Math.abs(right - left) > epsilon_x_2) {
			// Calculate midpoint of domain
			final double midpoint = (right + left) / 2;
			midy = f.at(midpoint);
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