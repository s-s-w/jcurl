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
package org.jcurl.core.curved;

import org.jcurl.core.base.Rock;
import org.jcurl.core.base.RockProps;
import org.jcurl.core.helpers.NotImplementedYetException;
import org.jcurl.core.math.MathException;
import org.jcurl.core.math.MathVec;
import org.jcurl.core.math.R1R1Function;

/**
 * The distance between two {@link CurveRock}s.
 * 
 * Differentiable min. 1x.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class DistanceSq implements R1R1Function {

    private final CurveRock c1;

    private final CurveRock c2;

    /** (r1+r2)^2 */
    private final double r2;

    /**
     * Distance between curve <code>c1</code> and curve <code>c2</code>.
     * 
     * @param c1
     * @param c2
     */
    public DistanceSq(CurveRock c1, CurveRock c2) {
        this(c1, c2, RockProps.DEFAULT.getRadius());
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
    DistanceSq(CurveRock c1, CurveRock c2, double r) {
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
    DistanceSq(CurveRock c1, double r1, CurveRock c2, double r2) {
        if (c1.dimension() != c2.dimension())
            throw new IllegalArgumentException("Dimension mismatch: "
                    + c1.dimension() + "!=" + c2.dimension());
        this.c1 = c1;
        this.c2 = c2;
        final double r = r1 + r2;
        this.r2 = r * r;
    }

    /**
     * <code>(c1(t) - c2(t))^2 - (r1 + r2)^2</code>.
     */
    public double value(double t) throws MathException {
        Rock a = c1.value(t, (Rock) null);
        Rock b = c2.value(t, (Rock) null);
        MathVec.sub(a, b, a);
        return a.distanceSq(0, 0) - r2;
    }

    /**
     * @see #value(double)
     * @see #valueC1(double)
     */
    public double value(double t, int derivative) throws MathException {
        if (derivative == 0)
            return value(t);
        if (derivative == 1)
            return valueC1(t);
        throw new NotImplementedYetException();
    }

    /**
     * <code>2 * (c1 - c2) * (c1' - c2')</code> Feed into maxima:
     * 
     * <pre>
     *                  a(t) := [ ax(t), ay(t) ];
     *                  b(t) := [ bx(t), by(t) ];
     *                  d(t) := (a(t) - b(t)) . (a(t) - b(t));
     *                  diff(d(t), t);
     *                  quit$
     * </pre>
     */
    double valueC1(double t) throws MathException {
        Rock a = c1.value(t, (Rock) null);
        Rock b = c2.value(t, (Rock) null);
        Rock da = c1.value(t, 1, (Rock) null);
        Rock db = c2.value(t, 1, (Rock) null);
        double ret = 0.0;
        ret += (a.getX() - b.getX()) * (da.getX() - db.getX());
        ret += (a.getY() - b.getY()) * (da.getY() - db.getY());
        return 2.0 * ret;
    }

}