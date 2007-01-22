/*
 * jcurl curling simulation framework 
 * Copyright (C) 2005-2006 M. Rohrmoser
 * 
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation; either version 2 of the License, or (at your
 * option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.jcurl.core.math;

/**
 * Implements <a href="http://mathworld.wolfram.com/NewtonsMethod.html">
 * Newton's Method</a> for finding zeros of real univariate functions.
 * <p>
 * The function should be continuous but not necessarily smooth.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class NewtonSolver extends SolverImpl {

    /** Serializable version identifier */
    private static final long serialVersionUID = 2606474895443431607L;

    /**
     * Construct a solver for the given function.
     * 
     * @param f
     *            function to solve.
     */
    public NewtonSolver(R1R1Function f) {
        super(f, 100, 1E-6);
    }

    /**
     * Find a zero near the midpoint of <code>min</code> and <code>max</code>.
     * 
     * @param min
     *            the lower bound for the interval
     * @param max
     *            the upper bound for the interval
     * 
     * @return the value where the function is zero
     * @throws MathException
     */
    public double solve(int derivative, double min, double max)
            throws MathException {
        return solve(derivative, min, max, SolverImpl.midpoint(min, max));
    }

    /**
     * Find a zero near the value <code>startValue</code>.
     * 
     * @param min
     *            the lower bound for the interval (ignored).
     * @param max
     *            the upper bound for the interval (ignored).
     * @param startValue
     *            the start value to use.
     * 
     * @return the value where the function is zero
     * @throws MathException
     */
    public double solve(int derivative, double min, double max,
            double startValue) throws MathException {

        clearResult();
        verifySequence(min, startValue, max);

        double x0 = startValue;
        double x1;

        int i = 0;
        while (i < maximalIterationCount) {
            x1 = x0 - (f.value(x0, derivative) / f.value(x0, derivative + 1));
            if (Math.abs(x1 - x0) <= absoluteAccuracy) {
                setResult(x1, i);
                return x1;
            }

            x0 = x1;
            ++i;
        }

        throw new ConvergenceException("Maximum number of iterations exceeded "
                + i);
    }

}
