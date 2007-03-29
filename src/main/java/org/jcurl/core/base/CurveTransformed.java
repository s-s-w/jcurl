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

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

/**
 * Decorator to apply an rc -&gt; wc {@link AffineTransform} and a time-shift.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id: CurveTransformed.java 361 2006-08-28 20:21:07Z mrohrmoser $
 */
public class CurveTransformed extends CurveRockBase {

    /**
     * Create the transformation from a Rock Coordinates (rc) System at p0_wc
     * with positive y axis along v0_wc into World Coordinates (wc).
     * 
     * @param ret
     *            <code>null</code> creates a new one.
     * @param x0
     *            (world coordinates)
     * @param y0
     *            (world coordinates)
     * @param vx
     *            (world coordinates)
     * @param vy
     *            (world coordinates)
     * @return trafo rc -&gt; wc
     */
    public static AffineTransform createRc2Wc(AffineTransform ret,
            final double x0, final double y0, final double vx, final double vy) {
        if (ret == null)
            ret = new AffineTransform();
        else
            ret.setToIdentity();
        final double v = Math.sqrt(vx * vx + vy * vy);
        if (v != 0.0) {
            final double x = vx / v;
            final double y = vy / v;
            ret.setTransform(y, -x, x, y, 0, 0);
        }
        // TUNE save one Matrix Instanciation?
        ret.preConcatenate(AffineTransform.getTranslateInstance(x0, y0));
        return ret;
    }

    /**
     * Delegate to
     * {@link #createRc2Wc(AffineTransform, double, double, double, double)}
     * 
     * @param ret
     *            <code>null</code> creates a new one.
     * @param p0_wc
     * @param v0_wc
     * @return trafo rc -&gt; wc
     */
    public static AffineTransform createRc2Wc(final AffineTransform ret,
            final Point2D p0_wc, final Point2D v0_wc) {
        return createRc2Wc(ret, p0_wc.getX(), p0_wc.getY(), v0_wc.getX(), v0_wc
                .getY());
    }

    private final double[] p = new double[6];

    final CurveRock rc;

    final double rot;

    final double t0;

    /**
     * 
     * @param rc
     * @param t
     *            See {@link #createRc2Wc(AffineTransform, Point2D, Point2D)}
     * @param t0
     */
    public CurveTransformed(final CurveRock rc, final AffineTransform t,
            final double t0) {
        this.t0 = t0;
        this.rc = rc;
        t.getMatrix(p);
        rot = Math.atan2(t.getShearY(), t.getScaleY());
    }

    /**
     * See {@link #createRc2Wc(AffineTransform, Point2D, Point2D)} and
     * {@link #CurveTransformed(CurveRock, AffineTransform, double)}
     * 
     * @param c
     * @param x0_wc
     * @param v0_wc
     * @param t0
     */
    public CurveTransformed(final CurveRock c, final Point2D x0_wc,
            final Point2D v0_wc, final double t0) {
        this(c, createRc2Wc(null, x0_wc, v0_wc), t0);
    }

    public double[] at(final int derivative, double t, double[] ret) {
        t -= t0;
        ret = rc.at(derivative, t, ret);
        final double x;
        final double y;
        final double z;
        if (derivative < 1) {
            x = p[0] * ret[0] + p[2] * ret[1] + p[4];
            y = p[1] * ret[0] + p[3] * ret[1] + p[5];
            z = ret[2] + rot;
        } else {
            x = p[0] * ret[0] + p[2] * ret[1];
            y = p[1] * ret[0] + p[3] * ret[1];
            z = ret[2];
        }
        ret[0] = x;
        ret[1] = y;
        ret[2] = z;
        return ret;
    }

    public Rock at(final int derivative, double t, Rock ret) {
        t -= t0;
        ret = rc.at(derivative, t, ret);
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

    public double at(final int component, final int derivative, final double t) {
        throw new UnsupportedOperationException("Not supported.");
    }

    double getT0() {
        return t0;
    }
}
