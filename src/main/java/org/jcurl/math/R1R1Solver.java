/*
 * jcurl curling simulation framework http://www.jcurl.org
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
package org.jcurl.math;

/**
 * Interface for (univariate real) rootfinding algorithms.
 * <p>
 * Implementations will search for only one zero in the given interval.
 * 
 * @version $Id: R1R1Solver.java 370 2007-01-23 03:04:05Z mrohrmoser $
 */
public interface R1R1Solver {

    /**
     * Get the actual absolute accuracy.
     * 
     * @return the accuracy
     */
    double getAbsoluteAccuracy();

    /**
     * Get the actual function value accuracy.
     * 
     * @return the accuracy
     */
    double getFunctionValueAccuracy();

    /**
     * Get the number of iterations in the last run of the solver.
     * <p>
     * This is mainly meant for testing purposes. It may occasionally help track
     * down performance problems: if the iteration count is notoriously high,
     * check whether the function is evaluated properly, and whether another
     * solver is more amenable to the problem.
     * 
     * @return the last iteration count.
     * @throws IllegalStateException
     *             if there is no result available, either because no result was
     *             yet computed or the last attempt failed.
     */
    int getIterationCount();

    /**
     * Get the upper limit for the number of iterations.
     * 
     * @return the actual upper limit
     */
    int getMaximalIterationCount();

    /**
     * Get the actual relative accuracy.
     * 
     * @return the accuracy
     */
    double getRelativeAccuracy();

    /**
     * Get the result of the last run of the solver.
     * 
     * @return the last result.
     * @throws IllegalStateException
     *             if there is no result available, either because no result was
     *             yet computed or the last attempt failed.
     */
    double getResult();

    /**
     * Reset the absolute accuracy to the default.
     * <p>
     * The default value is provided by the solver implementation.
     */
    void resetAbsoluteAccuracy();

    /**
     * Reset the actual function accuracy to the default. The default value is
     * provided by the solver implementation.
     */
    void resetFunctionValueAccuracy();

    /**
     * Reset the upper limit for the number of iterations to the default.
     * <p>
     * The default value is supplied by the solver implementation.
     * 
     * @see #setMaximalIterationCount(int)
     */
    void resetMaximalIterationCount();

    /**
     * Reset the relative accuracy to the default. The default value is provided
     * by the solver implementation.
     */
    void resetRelativeAccuracy();

    /**
     * Set the absolute accuracy.
     * <p>
     * The default is usually choosen so that roots in the interval -10..-0.1
     * and +0.1..+10 can be found with a reasonable accuracy. If the expected
     * absolute value of your roots is of much smaller magnitude, set this to a
     * smaller value.
     * <p>
     * Solvers are advised to do a plausibility check with the relative
     * accuracy, but clients should not rely on this.
     * 
     * @param accuracy
     *            the accuracy.
     * @throws IllegalArgumentException
     *             if the accuracy can't be achieved by the solver or is
     *             otherwise deemed unreasonable.
     */
    void setAbsoluteAccuracy(double accuracy);

    /**
     * Set the function value accuracy.
     * <p>
     * This is used to determine whan an evaluated function value or some other
     * value which is used as divisor is zero.
     * <p>
     * This is a safety guard and it shouldn't be necesary to change this in
     * general.
     * 
     * @param accuracy
     *            the accuracy.
     * @throws IllegalArgumentException
     *             if the accuracy can't be achieved by the solver or is
     *             otherwise deemed unreasonable.
     */
    void setFunctionValueAccuracy(double accuracy);

    /**
     * Set the upper limit for the number of iterations.
     * <p>
     * Usually a high iteration count indicates convergence problems. However,
     * the "reasonable value" varies widely for different solvers. Users are
     * advised to use the default value supplied by the solver.
     * <p>
     * A <code>ConvergenceException</code> will be thrown if this number is
     * exceeded.
     * 
     * @param count
     *            maximum number of iterations
     */
    void setMaximalIterationCount(int count);

    /**
     * Set the relative accuracy.
     * <p>
     * This is used to stop iterations if the absolute accuracy can't be
     * achieved due to large values or short mantissa length.
     * <p>
     * If this should be the primary criterion for convergence rather then a
     * safety measure, set the absolute accuracy to a ridiculously small value,
     * like 1E-1000.
     * 
     * @param accuracy
     *            the relative accuracy.
     * @throws IllegalArgumentException
     *             if the accuracy can't be achieved by the solver or is
     *             otherwise deemed unreasonable.
     */
    void setRelativeAccuracy(double accuracy);

    /**
     * Solve for a zero root in the given interval. A solver may require that
     * the interval brackets a single zero root.
     * 
     * @param derivative
     *            TODO
     * @param min
     *            the lower bound for the interval.
     * @param max
     *            the upper bound for the interval.
     * 
     * @return a value where the function is zero
     * @throws ConvergenceException
     *             if the maximum iteration count is exceeded or the solver
     *             detects convergence problems otherwise.
     * @throws FunctionEvaluationException
     *             if an error occurs evaluating the function
     * @throws MathException
     * @throws IllegalArgumentException
     *             if min > max or the endpoints do not satisfy the requirements
     *             specified by the solver
     */
    double solve(int derivative, double min, double max) throws MathException;

    /**
     * Solve for a zero in the given interval, start at startValue. A solver may
     * require that the interval brackets a single zero root.
     * 
     * @param derivative
     *            TODO
     * @param min
     *            the lower bound for the interval.
     * @param max
     *            the upper bound for the interval.
     * @param startValue
     *            the start value to use
     * 
     * @return a value where the function is zero
     * @throws ConvergenceException
     *             if the maximum iteration count is exceeded or the solver
     *             detects convergence problems otherwise.
     * @throws FunctionEvaluationException
     *             if an error occurs evaluating the function
     * @throws MathException
     * @throws IllegalArgumentException
     *             if min > max or the arguments do not satisfy the requirements
     *             specified by the solver
     */
    double solve(int derivative, double min, double max, double startValue)
            throws MathException;

}
