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
package org.jcurl.core.math;

/**
 * Polynomes of n-th grade.
 * 
 * @see org.jcurl.math.analysis.PolynomeTest
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class PolynomeBase implements Polynome {

    private static final long serialVersionUID = -745491227197828208L;

    /**
     * Compute the polynome p at x.
     * 
     * @param x
     * @param p
     *            polynome coefficients
     * @return <code>p(x)</code>
     * @see #evaluate(double[], double, int)
     */
    public static double evaluate(final double x, final double[] p) {
        return evaluate(p, x, 0);
    }

    /**
     * Compute the c-th derivative of the polynome p at x.
     * 
     * @param coefficients
     *            polynome coefficients
     * @param x
     * @param derivative
     *            derivative
     * 
     * @return <code>d/dx^c p(x)</code>
     */
    public static double evaluate(final double[] coefficients, final double x,
            final int derivative) {
        double ret = 0;
        for (int i = coefficients.length - 1; i >= derivative; i--) {
            ret *= x;
            ret += factorial(i, i - derivative) * coefficients[i];
        }
        return ret;
    }

    /**
     * Compute <code>a!</code>
     * 
     * @param a
     * @return a!
     */
    static long factorial(final int a) {
        return factorial(a, 1);
    }

    /**
     * Compute <code>high! / low!</code>
     * 
     * @param high
     * @param low
     * @return high! / low!
     */
    static long factorial(final int high, int low) {
        if (high < 2)
            return 1;
        long ret = 1;
        for (int i = low < 2 ? 1 : low; i < high; ret *= ++i)
            ;
        return ret;
    }

    /**
     * Convenience method to get the "bewegungsgleichung" for a given initial
     * state.
     * 
     * @param t0
     *            initial time
     * @param x0
     *            initial location
     * @param v0
     *            initial speed
     * @param a0
     *            constant acceleration
     * @return the resulting polynome
     * @see #getPolyParams(double, double, double, double)
     */
    public static final Polynome getPoly(double t0, double x0, double v0,
            double a0) {
        return new PolynomeBase(getPolyParams(t0, x0, v0, a0));
    }

    /**
     * Convenience method to get the "bewegungsgleichung" for a given initial
     * state.
     * 
     * @param t0
     *            initial time
     * @param x0
     *            initial location
     * @param v0
     *            initial speed
     * @param a0
     *            constant acceleration
     * @return the resulting polynome's parameters
     */
    public static final double[] getPolyParams(double t0, double x0, double v0,
            double a0) {
        final double[] p = { x0 - v0 * t0 + 0.5 * a0 * t0 * t0, v0 - a0 * t0,
                0.5 * a0 };
        return p;
    }

    public static String toString(final double[] poly) {
        final StringBuffer ret = new StringBuffer();
        ret.append("p(x) = ");
        int i = poly.length - 1;
        ret.append(poly[i]);
        ret.append("*x^");
        ret.append(i);
        while (--i >= 0) {
            if (poly[i] == 0.0)
                continue;
            if (poly[i] < 0) {
                ret.append(" - ");
                ret.append(-poly[i]);
            } else {
                ret.append(" + ");
                ret.append(poly[i]);
            }
            ret.append("*x^");
            ret.append(i);
        }
        return ret.toString();
    }

    private final double[] coff;

    public PolynomeBase(final double[] params) {
        this.coff = params;
    }

    protected double[] coffs() {
        return coff;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jcurl.core.math.Polynome#toString()
     */
    public String toString() {
        return toString(this.coffs());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jcurl.core.math.Polynome#value(double)
     */
    public double value(double x) {
        return value(x, 0);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jcurl.core.math.Polynome#value(double, int)
     */
    public double value(final double x, final int c) {
        return evaluate(coffs(), x, c);
    }
}