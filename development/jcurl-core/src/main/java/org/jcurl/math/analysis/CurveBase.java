/*
 * jcurl curling simulation framework 
 * Copyright (C) 2005 M. Rohrmoser
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
package org.jcurl.math.analysis;

import org.apache.commons.math.FunctionEvaluationException;

/**
 * Abstract base class for n-dimensional curves <code>R -&gt; R^n</code>.
 * 
 * @see org.jcurl.math.analysis.CurveTest
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
abstract class CurveBase {
    protected final int dim;

    protected CurveBase(final int dim) {
        this.dim = dim;
    }

    /**
     * Compute the c'th derivative of all dimensions at <code>t</code>.
     * 
     * @param c
     *            derivative (0=location, 1:speed, ...)
     * @param t
     *            t-value (input)
     * @param ret
     *            return value container
     * @return the c'th derivative at <code>t</code>
     */
    public double[] getC(int c, double t, double[] ret)
            throws FunctionEvaluationException {
        if (ret == null)
            ret = new double[dim];
        for (int i = dim - 1; i >= 0; i--)
            ret[i] = getC(i, c, t);
        return ret;
    }

    /**
     * Compute the c'th derivative of all dimensions at <code>t</code>.
     * 
     * @param c
     *            derivative (0=location, 1:speed, ...)
     * @param t
     *            t-value (input)
     * @param ret
     *            return value container
     * @return the c'th derivative at <code>t</code>
     */
    public float[] getC(int c, double t, float[] ret)
            throws FunctionEvaluationException {
        if (ret == null)
            ret = new float[dim];
        for (int i = dim - 1; i >= 0; i--)
            ret[i] = (float) getC(i, c, t);
        return ret;
    }

    /**
     * Compute the c'th derivative of the given dimension at <code>t</code>.
     * 
     * @param dim
     *            dimension (0,1,2,...)
     * @param c
     *            derivative (0=location, 1:speed, ...)
     * @param t
     *            t-value
     * @return the c'th derivative at <code>t</code>
     */
    public abstract double getC(int dim, int c, double t)
            throws FunctionEvaluationException;

    /**
     * Compute <code>x where f(x) = 0</code> using Newton's algorithm.
     * 
     * @param dim
     *            dimension (0,1,2,...)
     * @param c
     *            c'th derivative
     * @param x
     *            start value
     * @return x for getC(dim, c, x) = 0
     */
    public double computeNewtonZero(final int dim, final int c, double x)
            throws FunctionEvaluationException {
        return computeNewtonValue(dim, c, 0, x);
    }

    /**
     * Compute <code>x where f^c(x) = y</code> using Newton's algorithm.
     * 
     * @param dim
     *            dimension (0,1,2,...)
     * @param c
     *            c'th derivative
     * @param y
     *            value
     * @param x
     *            start value
     * @return x for getC(dim, c, x) = y
     */
    public double computeNewtonValue(final int dim, final int c, double y,
            double x) throws FunctionEvaluationException {
        final double eps = 1e-9;
        for (;;) {
            double dx = getC(dim, c + 1, x);
            if (dx == 0)
                return 0;
            dx = (getC(dim, c, x) - y) / dx;
            x -= dx;
            if (Math.abs(dx) < eps)
                return x;
        }
    }
}