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

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import org.jcurl.core.dto.Rock;
import org.jcurl.core.helpers.NotImplementedYetException;
import org.jcurl.core.math.MathException;

/**
 * Decorator to apply an rc -&gt; wc {@link AffineTransform} and a time-shift.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class CurveTransformed implements CurveRock {

    /**
     * Create the transformation from a Rock Coordinates (rc) System at p0_wc
     * with positive y axis along v0_wc into World Coordinates (wc).
     * 
     * @param p0_wc
     * @param v0_wc
     * @return trafo rc -&gt; wc
     */
    static AffineTransform create(final Point2D p0_wc, final Point2D v0_wc) {
        final double v = v0_wc.distance(0, 0);
        final AffineTransform at;
        if (v == 0) {
            at = new AffineTransform();
        } else {
            final double x = v0_wc.getX() / v;
            final double y = v0_wc.getY() / v;
            final double[] d = { y, -x, x, y, 0, 0 };
            at = new AffineTransform(d);
        }
        at.preConcatenate(AffineTransform.getTranslateInstance(p0_wc.getX(),
                p0_wc.getY()));
        return at;
    }

    private final CurveRock c;

    private final double[] p = new double[6];

    private final double rot;

    private final double t0;

    /**
     * 
     * @param c
     * @param t
     *            See {@link #create(Point2D, Point2D)}
     * @param t0
     */
    public CurveTransformed(CurveRock c, AffineTransform t, double t0) {
        this.t0 = t0;
        this.c = c;
        t.getMatrix(this.p);
        this.rot = Math.atan2(t.getShearY(), t.getScaleY());
    }

    /**
     * See {@link #create(Point2D, Point2D)} and
     * {@link #CurveTransformed(CurveRock, AffineTransform, double)}
     * 
     * @param c
     * @param x0_wc
     * @param v0_wc
     * @param t0
     */
    public CurveTransformed(CurveRock c, Point2D x0_wc, Point2D v0_wc, double t0) {
        this(c, create(x0_wc, v0_wc), t0);
    }

    public int dimension() {
        return 3;
    }

    public double[] value(double t, double[] ret) throws MathException {
        return value(t, 0, ret);
    }

    public double[] value(double t, int derivative, double[] ret)
            throws MathException {
        t -= t0;
        throw new NotImplementedYetException();
    }

    public Rock value(double t, int derivative, Rock ret) throws MathException {
        t -= t0;
        ret = c.value(t, derivative, ret);
        if (derivative < 1) {
            final double x = p[0] * ret.getX() + p[2] * ret.getY() + p[4];
            final double y = p[1] * ret.getX() + p[3] * ret.getY() + p[5];
            final double z = ret.getZ() + rot;
            ret.setLocation(x, y, z);
        } else {
            final double x = p[0] * ret.getX() + p[2] * ret.getY();
            final double y = p[1] * ret.getX() + p[3] * ret.getY();
            ret.setLocation(x, y);
        }
        return ret;
    }

    public Rock value(double t, Rock ret) throws MathException {
        return value(t, 0, ret);
    }
}
