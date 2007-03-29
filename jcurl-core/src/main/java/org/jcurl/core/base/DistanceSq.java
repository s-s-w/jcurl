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
package org.jcurl.core.base;

import org.jcurl.core.helpers.NotImplementedYetException;
import org.jcurl.math.MathVec;
import org.jcurl.math.R1R1Function;

/**
 * The distance between two {@link CurveRock}s.
 * 
 * Differentiable min. 1x.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id: DistanceSq.java 370 2007-01-23 03:04:05Z mrohrmoser $
 */
public class DistanceSq extends R1R1Function {

    private final CurveRock c1;

    private final CurveRock c2;

    /** (r1+r2)^2 */
    private final double r2;

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
    DistanceSq(final CurveRock c1, final double r1, final CurveRock c2,
            final double r2) {
        this(c1, c2, MathVec.sqr(r1 + r2));
    }

    /**
     * Distance between two (n-dimensional) spheres moving along curve
     * <code>c1</code> and curve <code>c2</code>, having the square sum of
     * radii <code>r12Sqr</code>.
     * 
     * @param c1
     * @param c2
     * @param r12Sqr
     *            <code>(r1+r2)^2</code>
     */
    DistanceSq(final CurveRock c1, final CurveRock c2, final double r12Sqr) {
        if (c1.dimension() != c2.dimension())
            throw new IllegalArgumentException("Dimension mismatch: "
                    + c1.dimension() + "!=" + c2.dimension());
        this.c1 = c1;
        this.c2 = c2;
        r2 = r12Sqr;
    }

    /**
     * <code>(c1(t) - c2(t))^2 - (r1 + r2)^2</code>.
     * 
     * @param t
     * @return the value
     */
    public double at(final double t) {
        // TUNE Thread safety at the cost of 2 Rock instanciations
        final Rock a = c1.at(t, new RockDouble());
        final Rock b = c2.at(t, new RockDouble());
        return a.distanceSq(b) - r2;
    }

    /**
     * @param derivative
     * @param t
     * @return the value
     * @see #at(double)
     * @see #valueC1(double)
     */
    public double at(final int derivative, final double t) {
        if (derivative == 0)
            return at(t);
        if (derivative == 1)
            return valueC1(t);
        throw new NotImplementedYetException();
    }

    /**
     * <code>2 * (c1 - c2) * (c1' - c2')</code> Feed into maxima:
     * 
     * <pre>
     *    a(t) := [ ax(t), ay(t) ];
     *    b(t) := [ bx(t), by(t) ];
     *    d(t) := (a(t) - b(t)) . (a(t) - b(t));
     *    diff(d(t), t);
     *    quit$
     * </pre>
     */
    double valueC1(final double t) {
        // TUNE Thread safety at the cost of 4 Rock instanciations
        final Rock a = c1.at(t, new RockDouble());
        final Rock b = c2.at(t, new RockDouble());
        final Rock da = c1.at(1, t, new RockDouble());
        final Rock db = c2.at(1, t, new RockDouble());
        double ret = 0.0;
        ret += (a.getX() - b.getX()) * (da.getX() - db.getX());
        ret += (a.getY() - b.getY()) * (da.getY() - db.getY());
        return 2.0 * ret;
    }

}