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
package jcurl.math;

/**
 * @see jcurl.math.PolynomeTest
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class Polynome extends Function1D {

    private static double[] create(final double a, final double b,
            final double c) {
        final double[] ret = new double[3];
        ret[0] = a;
        ret[1] = b;
        ret[2] = c;
        return ret;
    }

    public static final Polynome getPoly(double t0, double x0, double v0,
            double a0) {
        final double[] p = { x0 - (v0 * t0 + 0.5 * a0 * t0 * t0), v0, 0.5 * a0 };
        return new Polynome(p);
    }

    private final double[] params;

    private Polynome(final double a, final double b, final double c) {
        this(create(a, b, c));
    }

    public Polynome(final double[] params) {
        this.params = params;
    }

    public double getC(int c, double t) {
        final int ord = params.length - 1;
        double ret = 0;
        for (int i = ord; i >= c; i--)
            ret = ret * Math.pow(t, i) + params[i];
        return ret;
    }
}