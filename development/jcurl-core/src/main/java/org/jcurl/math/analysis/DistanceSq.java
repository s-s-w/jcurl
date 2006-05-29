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
package org.jcurl.math.analysis;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.DifferentiableUnivariateRealFunction;
import org.apache.commons.math.analysis.UnivariateRealFunction;

/**
 * Function that describes the distance between two n-dimensional curves.
 * 
 * Differentiable max. 1x.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class DistanceSq implements DifferentiableUnivariateRealFunction {

    private final R1RnCurve c1;

    private final R1RnCurve c2;

    transient private UnivariateRealFunction derived;

    transient private R1RnCurve diff1;

    transient private R1RnCurve diff2;

    private final double r2;

    /**
     * Distance between curve <code>c1</code> and curve <code>c2</code>.
     * 
     * @param c1
     * @param c2
     */
    public DistanceSq(R1RnCurve c1, R1RnCurve c2) {
        this(c1, 0, c2, 0);
    }

    /**
     * Distance between two (n-dimensional) spheres moving along curve
     * <code>c1</code> and curve <code>c2</code>, each having radius
     * <code>r</code>.
     * 
     * @param c1
     * @param c2
     * @param r
     */
    public DistanceSq(R1RnCurve c1, R1RnCurve c2, double r) {
        this(c1, r, c2, r);
    }

    /**
     * Distance between two (n-dimensional) spheres moving along curve
     * <code>c1</code> and curve <code>c2</code>, having radii
     * <code>r1</code> and <code>r2</code>.
     * 
     * @param c1
     * @param r1
     * @param c2
     * @param r2
     */
    public DistanceSq(R1RnCurve c1, double r1, R1RnCurve c2, double r2) {
        if (c1.dimension() != c2.dimension())
            throw new IllegalArgumentException("Dimension mismatch: "
                    + c1.dimension() + "!=" + c2.dimension());
        this.c1 = c1;
        this.c2 = c2;
        final double r = r1 + r2;
        this.r2 = r * r;
    }

    /**
     * <code>2 * (c1 - c2) * (c1' - c2')</code> Feed into maxima:
     * 
     * <pre>
     *  a(t) := [ ax(t), ay(t) ];
     *  b(t) := [ bx(t), by(t) ];
     *  d(t) := (a(t) - b(t)) . (a(t) - b(t));
     *  diff(d(t), t);
     *  quit$
     * </pre>
     */
    public UnivariateRealFunction derivative() {
        if (derived == null) {
            diff1 = c1.derivative();
            diff2 = c2.derivative();
            derived = new UnivariateRealFunction() {
                public double value(double t)
                        throws FunctionEvaluationException {
                    double ret = 0.0;
                    for (int i = c1.dimension() - 1; i >= 0; i--) {
                        ret += (c1.value(i, t) - c2.value(i, t))
                                * (diff1.value(i, t) - diff2.value(i, t));
                    }
                    return 2.0 * ret;
                }
            };
        }
        return derived;
    }

    /**
     * <code>(c1(t) - c2(t))^2 - (r1 + r2)^2</code>.
     */
    public double value(double t) throws FunctionEvaluationException {
        double ret = 0.0;
        for (int i = c1.dimension() - 1; i >= 0; i--) {
            final double diff = c1.value(i, t) - c2.value(i, t);
            ret += diff * diff;
        }
        return ret - r2;
    }
}