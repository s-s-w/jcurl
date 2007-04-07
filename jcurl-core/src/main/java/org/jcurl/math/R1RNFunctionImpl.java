/*
 * jcurl curling simulation framework http://www.jcurl.org
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
package org.jcurl.math;

import org.apache.commons.logging.Log;
import org.jcurl.core.log.JCLoggerFactory;

/**
 * Abstract base class for n-dimensional curves <code>f : R^1 -&gt; R^n</code>.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public abstract class R1RNFunctionImpl implements R1RNFunction {

    private static final Log log = JCLoggerFactory
            .getLogger(R1RNFunctionImpl.class);

    private final int dim;

    protected R1RNFunctionImpl(final int dim) {
        this.dim = dim;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jcurl.math.R1RNFunction#dim()
     */
    public final int dim() {
        return dim;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jcurl.math.R1RNFunction#at(int, double, double[])
     */
    public double[] at(final int c, final double t, double[] ret) {
        if (ret == null)
            ret = new double[dim];
        for (int i = dim - 1; i >= 0; i--)
            ret[i] = this.at(i, c, t);
        return ret;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jcurl.math.R1RNFunction#at(int, double, float[])
     */
    public float[] at(final int c, final double t, float[] ret) {
        if (ret == null)
            ret = new float[dim];
        for (int i = dim - 1; i >= 0; i--)
            ret[i] = (float) this.at(i, c, t);
        return ret;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jcurl.math.R1RNFunction#at(int, int, double)
     */
    public abstract double at(int dim, int c, double t);

    /**
     * Compute <code>x where f^c(x) = y</code> using Newton's algorithm.
     * 
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
    public double computeNewtonValue(final int dim, final int c,
            final double y, final double x0, final double xstop) {
        final double eps = 1e-9;
        for (double x = x0;;) {
            if (log.isDebugEnabled())
                log.debug("x=" + x + " y" + c + "=" + this.at(dim, c, x) + " y"
                        + (c + 1) + "=" + this.at(dim, c + 1, x));
            double dx = this.at(dim, c + 1, x);
            if (dx == 0)
                return Math.abs(this.at(dim, c, x) - y) < eps ? x : Double.NaN;
            dx = (this.at(dim, c, x) - y) / dx;
            x -= dx;
            if (!MathVec.isInside(x, x0, xstop, true))
                return Double.NaN;
            if (Math.abs(dx) < eps)
                return x;
        }
    }

    /**
     * Compute <code>x where f(x) = 0</code> using Newton's algorithm.
     * 
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
    public double computeNewtonZero(final int dim, final int c,
            final double x0, final double xstop) {
        return computeNewtonValue(dim, c, 0, x0, xstop);
    }
}