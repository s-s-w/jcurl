/*
 * jcurl curling simulation framework http://www.jcurl.org
 * Copyright (C) 2005-2008 M. Rohrmoser
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
 * The numerical algorithms are adapted from "Meyberg/Vachenauer": Hoehere
 * Mathematik I, second edition. Could this be based on
 * {@link org.jcurl.math.CurveCombined}with 3-dimensional polynomes?
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class CSplineInterpolator extends R1R1Function {

    private static final int a = 0;

    private static final float arrayFill = 0.80F;

    private static final int b = 1;

    private static final int c = 2;

    private static final int d = 3;

    private static final long serialVersionUID = -3630835719496788354L;

    /**
     * Compute the spline coefficients for the given points. The numerical
     * algorithm is adapted from "Meyberg/Vachenauer": Hoehere Mathematik I,
     * second edition.
     * 
     * @param points
     * @param x0
     * @param y0
     * @return <code>new double[N + 1][4]</code>
     */
    private static double[][] computeSplines(final int points,
            final double[] x0, final double[] y0) {
        final int N = points - 1;
        if (N < 1)
            throw new IllegalStateException("At least 2 values needed!");
        final double[][] out = new double[N + 1][4];
        double s = 0;
        double r = 0;
        out[N][a] = y0[N];
        out[N][b] = out[N][c] = out[N][d] = 0;
        for (int i = 0; i < N; ++i) {
            out[i][a] = y0[i];
            out[i][d] = x0[i + 1] - x0[i];
            r = (y0[i + 1] - y0[i]) / out[i][d];
            out[i][c] = r - s;
            s = r;
        }
        s = 0;
        r = 0;
        out[a][c] = 0;
        out[N][c] = 0;
        for (int i = 1; i < N; ++i) {
            out[i][c] += r * out[i - 1][c];
            out[i][b] = (x0[i - 1] - x0[i + 1]) * 2 - r * s;
            s = out[i][d];
            r = s / out[i][b];
        }
        for (int i = N - 1; i > 0; i--)
            out[i][c] = (out[i][d] * out[i + 1][c] - out[i][c]) / out[i][b];
        for (int i = 0; i < N; ++i) {
            s = out[i][d];
            r = out[i + 1][c] - out[i][c];
            out[i][d] = r / s;
            out[i][c] *= 3;
            out[i][b] = (y0[i + 1] - y0[i]) / s - (out[i][c] + r) * s;
        }
        out[N][b] = out[N][c] = out[N][d] = 0.0;
        return out;
    }

    private int points = 0;

    /**
     * Spline coefficients or null.
     */
    private double[][] splines = null;

    private double[] x;

    private double[] y;

    /**
     * Create a new spline interpolator with an initial capacity of 100 points.
     * 
     * @see CSplineInterpolator#CSplineInterpolator(int)
     */
    public CSplineInterpolator() {
        this(100);
    }

    /**
     * Create a new spline interpolator with the given initial capacity.
     * 
     * @param capacity
     */
    public CSplineInterpolator(final int capacity) {
        x = new double[capacity];
        y = new double[capacity];
    }

    /**
     * Add a point and discard the splines. The x-values must be strictly
     * monotonous ascending - which is not checked for the moment. Reallocates
     * the point buffer if necessary.
     * 
     * @param x
     * @param y
     */
    public void add(final double x, final double y) {
        if (points == this.x.length) {
            // resize _x and _y
            double[] tmp = new double[1 + (int) (points / arrayFill)];
            System.arraycopy(this.x, 0, tmp, 0, points);
            this.x = tmp;
            tmp = new double[this.x.length];
            System.arraycopy(this.y, 0, tmp, 0, points);
            this.y = tmp;
        }
        splines = null; // enforce re-computation
        this.x[points] = x;
        this.y[points] = y;
        points++;
    }

    /**
     * Convenience wrapper.
     * 
     * @see #at(int, int, double)
     * @param C
     *            n'th derivative (0=value,1=incline,2=acceleration)
     * @param x
     *            location
     * @return value ot the n'th derivative
     */
    @Override
    public double at(final int C, double x) {
        final int idx = findSplineIndex(x);
        final double[] spline = splines[idx];
        x -= this.x[idx];
        double ret = 0;
        for (int i = d; i >= C; i--) {
            ret *= x;
            ret += spline[i];
        }
        return ret;
    }

    /**
     * Look up the spline index for the given x. Triggers spline computation if
     * not existing yet.
     * 
     * @see CSplineInterpolator#computeSplines(int, double[], double[])
     * @param x
     * @return -1 for "x outside range"
     */
    private int findSplineIndex(final double x) {
        if (x < this.x[0] || x > this.x[points - 1])
            return -1;
        if (splines == null)
            // compute the c3 values
            splines = computeSplines(points, this.x, y);
        // find the correct index
        int idx = CurveCombined.binarySearch(this.x, 0, points - 1, x);
        if (idx < 0) {
            if (idx == -1)
                return -1;
            idx = -2 - idx;
        }
        return idx;
    }

    public double getMaxX() {
        return x[points - 1];
    }

    public double getMinX() {
        return x[0];
    }

    /**
     * Discard the buffered points and splines.
     */
    public void reset() {
        points = 0;
        splines = null;
    }
}