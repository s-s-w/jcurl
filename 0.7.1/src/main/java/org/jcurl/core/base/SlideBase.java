/*
 * jcurl curling simulation framework http://www.jcurl.org
 * Copyright (C) 2005-2007 M. Rohrmoser
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
 * Implementation base for {@link Slider}s.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public abstract class SlideBase implements Slider {

    public static CurveRock still(final double x, final double y, final double a) {
        return new CurveRock() {

            @Override
            public double at(final int dim, final int c, final double t) {
                if (c > 0)
                    return 0;
                switch (dim) {
                case 0:
                    return x;
                case 1:
                    return y;
                case 2:
                    return a;
                default:
                    throw new RuntimeException();
                }
            }

            @Override
            public String toString() {
                final StringBuffer buf = new StringBuffer();
                buf.append('[');
                buf.append(x).append(", ");
                buf.append(y).append(", ");
                buf.append(a);
                buf.append(']');
                return buf.toString();
            }
        };
    }

    public static CurveRock still(final Rock x) {
        return still(x.getX(), x.getY(), x.getZ());
    }

    public abstract CurveRock computeRc(final Rock x0, final Rock v0);

    /**
     * Compute the RC-&gt;WC transformation for a rock at any time.
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
     * @return the transformation matrix
     * @see CurveTransformed#createRc2Wc(AffineTransform, double, double,
     *      double, double)
     * @deprecated Use
     *             {@link CurveTransformed#createRc2Wc(AffineTransform, double, double, double, double)}
     */
    @Deprecated
    public AffineTransform computeRc2Wc(final AffineTransform ret,
            final double x0, final double y0, final double vx, final double vy) {
        return CurveTransformed.createRc2Wc(ret, x0, y0, vx, vy);
    }

    /**
     * Compute the RC-&gt;WC transformation for a rock at any time.
     * 
     * @param ret
     *            <code>null</code> creates a new one.
     * @param x
     *            (world coordinates)
     * @param v
     *            (world coordinates)
     * @return the transformation matrix
     * @see CurveTransformed#createRc2Wc(AffineTransform, Point2D, Point2D)
     * @deprecated Use
     *             {@link CurveTransformed#createRc2Wc(AffineTransform, Point2D, Point2D)}
     */
    @Deprecated
    public AffineTransform computeRc2Wc(final AffineTransform ret,
            final Point2D x, final Point2D v) {
        return CurveTransformed.createRc2Wc(ret, x, v);
    }

    public abstract double computeV0(final double intervalTime);

    public abstract double getDrawToTeeCurl();

    public abstract double getDrawToTeeTime();

    /**
     * Compute the RC-&gt;WC transformation for a rock immediately after it's
     * release (at the hog).
     * 
     * @param ret
     *            <code>null</code> creates a new one.
     * @param broomX
     *            (world coordinates)
     * @param broomY
     *            (world coordinates)
     * @return the transformation matrix
     */
    public AffineTransform releaseRc2Wc(AffineTransform ret,
            final double broomX, final double broomY) {
        if (ret == null)
            ret = new AffineTransform();
        else
            ret.setToIdentity();
        final double dx = (0 - broomX) * Ice.HACK_2_HOG
                / (Ice.FAR_HACK_2_TEE - broomY);
        ret.translate(dx, Ice.FAR_HOG_2_TEE);
        // TUNE avoid trigonometry
        ret.rotate(Math.atan2(dx, Ice.HACK_2_HOG) + Math.PI);
        return ret;
    }

    /**
     * Compute the RC-&gt;WC transformation for a rock immediately after release
     * it's (at the hog).
     * <p>
     * Convenience wrapper for
     * {@link #releaseRc2Wc(AffineTransform, double, double)}.
     * </p>
     * 
     * @param ret
     *            <code>null</code> creates a new one.
     * @param broom
     *            (world coordinates)
     * @return the transformation matrix
     * @see #releaseRc2Wc(AffineTransform, double, double)
     */
    public AffineTransform releaseRc2Wc(final AffineTransform ret,
            final Point2D broom) {
        return releaseRc2Wc(ret, broom.getX(), broom.getY());
    }

}
