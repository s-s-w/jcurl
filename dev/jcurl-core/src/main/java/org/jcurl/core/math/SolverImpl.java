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
 * Provide a default implementation for several functions useful to generic
 * solvers. *
 * 
 * @version $Id$
 */
public abstract class SolverImpl implements R1R1Solver {
    /** Serializable version identifier */
    private static final long serialVersionUID = 1112491292565386596L;

    /**
     * Compute the midpoint of two values.
     * 
     * @param a
     *            first value.
     * @param b
     *            second value.
     * @return the midpoint.
     */
    public static double midpoint(double a, double b) {
        return (a + b) * .5;
    }

    /** Maximum absolute error. */
    protected double absoluteAccuracy;

    /** Default maximum absolute error. */
    protected double defaultAbsoluteAccuracy;

    /** Default maximum error of function. */
    protected double defaultFunctionValueAccuracy;

    /** Default maximum number of iterations. */
    protected int defaultMaximalIterationCount;

    /** Default maximum relative error. */
    protected double defaultRelativeAccuracy;

    /** The function to solve. */
    protected R1R1Function f;

    /** Maximum error of function. */
    protected double functionValueAccuracy;

    // Mainly for test framework.
    /** The last iteration count. */
    protected int iterationCount;

    /** Maximum number of iterations. */
    protected int maximalIterationCount;

    /** Maximum relative error. */
    protected double relativeAccuracy;

    /** The last computed root. */
    protected double result;

    /** Indicates where a root has been computed. */
    protected boolean resultComputed = false;

    /**
     * Construct a solver with given iteration count and accuracy.
     * 
     * @param f
     *            the function to solve.
     * @param defaultAbsoluteAccuracy
     *            maximum absolute error
     * @param defaultMaximalIterationCount
     *            maximum number of iterations
     * @throws IllegalArgumentException
     *             if f is null or the defaultAbsoluteAccuracy is not valid
     */
    protected SolverImpl(R1R1Function f, int defaultMaximalIterationCount,
            double defaultAbsoluteAccuracy) {

        super();

        if (f == null) {
            throw new IllegalArgumentException("function can not be null.");
        }

        this.f = f;
        this.defaultAbsoluteAccuracy = defaultAbsoluteAccuracy;
        this.defaultRelativeAccuracy = 1E-14;
        this.defaultFunctionValueAccuracy = 1E-15;
        this.absoluteAccuracy = defaultAbsoluteAccuracy;
        this.relativeAccuracy = defaultRelativeAccuracy;
        this.functionValueAccuracy = defaultFunctionValueAccuracy;
        this.defaultMaximalIterationCount = defaultMaximalIterationCount;
        this.maximalIterationCount = defaultMaximalIterationCount;
    }

    /**
     * Convenience function for implementations.
     */
    protected final void clearResult() {
        this.resultComputed = false;
    }

    /**
     * Get the actual absolute accuracy.
     * 
     * @return the accuracy
     */
    public double getAbsoluteAccuracy() {
        return absoluteAccuracy;
    }

    /**
     * Get the actual function value accuracy.
     * 
     * @return the accuracy
     */
    public double getFunctionValueAccuracy() {
        return functionValueAccuracy;
    }

    /**
     * Access the last iteration count.
     * 
     * @return the last iteration count
     * @throws IllegalStateException
     *             if no root has been computed
     * 
     */
    public int getIterationCount() {
        if (resultComputed)
            return iterationCount;
        throw new IllegalStateException("No result available");
    }

    /**
     * Get the upper limit for the number of iterations.
     * 
     * @return the actual upper limit
     */
    public int getMaximalIterationCount() {
        return maximalIterationCount;
    }

    /**
     * Get the actual relative accuracy.
     * 
     * @return the accuracy
     */
    public double getRelativeAccuracy() {
        return relativeAccuracy;
    }

    /**
     * Access the last computed root.
     * 
     * @return the last computed root
     * @throws IllegalStateException
     *             if no root has been computed
     */
    public double getResult() {
        if (resultComputed)
            return result;
        throw new IllegalStateException("No result available");
    }

    /**
     * Returns true iff the function takes opposite signs at the endpoints.
     * 
     * @param lower
     *            the lower endpoint
     * @param upper
     *            the upper endpoint
     * @param f
     *            the function
     * @return true if f(lower) * f(upper) < 0
     * @throws FunctionEvaluationException
     *             if an error occurs evaluating the function at the endpoints
     */
    protected boolean isBracketing(double lower, double upper, R1R1Function f)
            throws MathException {
        double f1 = f.at(lower);
        double f2 = f.at(upper);
        return ((f1 > 0 && f2 < 0) || (f1 < 0 && f2 > 0));
    }

    /**
     * Returns true if the arguments form a (strictly) increasing sequence
     * 
     * @param start
     *            first number
     * @param mid
     *            second number
     * @param end
     *            third number
     * @return true if the arguments form an increasing sequence
     */
    protected boolean isSequence(double start, double mid, double end) {
        return (start < mid) && (mid < end);
    }

    /**
     * Reset the absolute accuracy to the default.
     */
    public void resetAbsoluteAccuracy() {
        absoluteAccuracy = defaultAbsoluteAccuracy;
    }

    /**
     * Reset the actual function accuracy to the default.
     */
    public void resetFunctionValueAccuracy() {
        functionValueAccuracy = defaultFunctionValueAccuracy;
    }

    /**
     * Reset the upper limit for the number of iterations to the default.
     */
    public void resetMaximalIterationCount() {
        maximalIterationCount = defaultMaximalIterationCount;
    }

    /**
     * Reset the relative accuracy to the default.
     */
    public void resetRelativeAccuracy() {
        relativeAccuracy = defaultRelativeAccuracy;
    }

    /**
     * Set the absolute accuracy.
     * 
     * @param accuracy
     *            the accuracy.
     * @throws IllegalArgumentException
     *             if the accuracy can't be achieved by the solver or is
     *             otherwise deemed unreasonable.
     */
    public void setAbsoluteAccuracy(double accuracy) {
        absoluteAccuracy = accuracy;
    }

    /**
     * Set the function value accuracy.
     * 
     * @param accuracy
     *            the accuracy.
     * @throws IllegalArgumentException
     *             if the accuracy can't be achieved by the solver or is
     *             otherwise deemed unreasonable.
     */
    public void setFunctionValueAccuracy(double accuracy) {
        functionValueAccuracy = accuracy;
    }

    /**
     * Set the upper limit for the number of iterations.
     * 
     * @param count
     *            maximum number of iterations
     */
    public void setMaximalIterationCount(int count) {
        maximalIterationCount = count;
    }

    /**
     * Set the relative accuracy.
     * 
     * @param accuracy
     *            the relative accuracy.
     * @throws IllegalArgumentException
     *             if the accuracy can't be achieved by the solver or is
     *             otherwise deemed unreasonable.
     */
    public void setRelativeAccuracy(double accuracy) {
        relativeAccuracy = accuracy;
    }

    /**
     * Convenience function for implementations.
     * 
     * @param result
     *            the result to set
     * @param iterationCount
     *            the iteration count to set
     */
    protected final void setResult(double result, int iterationCount) {
        this.result = result;
        this.iterationCount = iterationCount;
        this.resultComputed = true;
    }

    /**
     * Verifies that the endpoints specify an interval and the function takes
     * opposite signs at the enpoints, throws IllegalArgumentException if not
     * 
     * @param lower
     *            lower endpoint
     * @param upper
     *            upper endpoint
     * @param f
     *            function
     * @throws IllegalArgumentException
     * @throws FunctionEvaluationException
     *             if an error occurs evaluating the function at the endpoints
     */
    protected void verifyBracketing(double lower, double upper, R1R1Function f)
            throws MathException {

        verifyInterval(lower, upper);
        if (!isBracketing(lower, upper, f)) {
            throw new IllegalArgumentException(
                    "Function values at endpoints do not have different signs."
                            + "  Endpoints: [" + lower + "," + upper + "]"
                            + "  Values: [" + f.at(lower) + "," + f.at(upper)
                            + "]");
        }
    }

    /**
     * Verifies that the endpoints specify an interval, throws
     * IllegalArgumentException if not
     * 
     * @param lower
     *            lower endpoint
     * @param upper
     *            upper endpoint
     * @throws IllegalArgumentException
     */
    protected void verifyInterval(double lower, double upper) {
        if (lower >= upper) {
            throw new IllegalArgumentException(
                    "Endpoints do not specify an interval: [" + lower + ","
                            + upper + "]");
        }
    }

    /**
     * Verifies that <code>lower < initial < upper</code> throws
     * IllegalArgumentException if not
     * 
     * @param lower
     *            lower endpoint
     * @param initial
     *            initial value
     * @param upper
     *            upper endpoint
     * @throws IllegalArgumentException
     */
    protected void verifySequence(double lower, double initial, double upper) {
        if (!isSequence(lower, initial, upper)) {
            throw new IllegalArgumentException(
                    "Invalid interval, initial value parameters:  lower="
                            + lower + " initial=" + initial + " upper=" + upper);
        }
    }
}
