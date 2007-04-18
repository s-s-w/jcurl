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

import java.awt.geom.AffineTransform;

/**
 * Multidimensional curves of polynomes.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class PolynomeCurve extends R1RNFunctionImpl {

    private static final long serialVersionUID = 7503158531478359260L;

    private final double[][] params;

    public static double[] mult(final double[] p1, final double[] p2,
            double[] ret) {
        // initialise the return array:
        if (ret == null || ret.length < p1.length + p2.length || p1 == ret
                || p2 == ret)
            ret = new double[p1.length + p2.length - 1];
        for (int i = ret.length - 1; i >= 0; i--)
            ret[i] = 0;
        for (int j = p1.length - 1; j >= 0; j--)
            for (int k = p2.length - 1; k >= 0; k--)
                ret[j + k] += p1[j] * p2[k];
        return ret;
    }

    static double[] add(double[] p1, double[] p2, double[] ret) {
        // swap if dim(p1) < dim(p2):
        if (p1.length < p2.length) {
            final double[] tmp = p1;
            p1 = p2;
            p2 = tmp;
        }
        // initialise the return array:
        if (ret == null || ret.length < p1.length || p1 == ret || p2 == ret)
            ret = new double[p1.length];
        else if (ret.length > p1.length)
            for (int i = p1.length; i < ret.length; i++)
                ret[i] = 0;
        // add p1 and p2
        for (int i = p2.length - 1; i >= 0; i--)
            ret[i] = p1[i] + p2[i];
        for (int i = p1.length - 1; i >= p2.length; i--)
            ret[i] = p1[i];
        return ret;
    }

    /**
     * Works only proper for 2 Dimensions!
     * 
     * @param at
     * @param p
     * @return the transformed curve.
     */
    public static PolynomeCurve transform(final AffineTransform at,
            final PolynomeCurve p) {
        final double[][] ret = new double[p.params.length][];
        for (int i = ret.length - 1; i >= 2; i--)
            ret[i] = p.params[i]; // clone? No: immutable
        final double m00 = at.getScaleX();
        final double m10 = at.getShearY();
        final double m01 = at.getShearX();
        final double m11 = at.getScaleY();
        final double m02 = at.getTranslateX();
        final double m12 = at.getTranslateY();
        double[] tmp = null;
        ret[0] = MathVec.mult(m00, p.params[0], ret[0]);
        tmp = MathVec.mult(m01, p.params[1], tmp);
        ret[0] = PolynomeCurve.add(ret[0], tmp, ret[0]);
        ret[0][0] += m02;

        ret[1] = MathVec.mult(m10, p.params[0], ret[1]);
        tmp = MathVec.mult(m11, p.params[1], tmp);
        ret[1] = PolynomeCurve.add(ret[1], tmp, ret[1]);
        ret[1][0] += m12;

        return new PolynomeCurve(ret);
    }

    public PolynomeCurve(final double[][] params) {
        super(params.length);
        this.params = params;
    }

    public PolynomeCurve(final Polynome[] polys) {
        super(polys.length);
        params = new double[polys.length][];
        for (int i = params.length - 1; i >= 0; i--)
            params[i] = polys[i].params;
    }

    @Override
    public double at(final int dim, final int c, final double t) {
        return Polynome.poly(c, t, params[dim]);
    }

    @Override
    public String toString() {
        final StringBuffer b = new StringBuffer();
        b.append("[");
        for (final double[] element : params)
            b.append(Polynome.toString(element)).append(", ");
        if (params.length > 0)
            b.setLength(b.length() - 2);
        return b.toString();
    }

}