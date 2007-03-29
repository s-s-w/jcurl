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

import org.jcurl.math.MathVec;
import org.jcurl.math.Polynome;

/**
 * This is not a realistic curl model but rather a baseline for the development
 * and implementation of others.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class SlideNoCurl extends SlideBase implements Strategy, Factory {

    final double beta;

    final double drawToTeeV0;

    /**
     * Computes the internal ice friction coefficient as:
     * <p>
     * <code>beta = {@link Ice#FAR_HOG_2_TEE} / drawToTeeTime^2</code>
     * </p>
     * 
     * @param drawToTeeTime
     *            may be {@link Double#POSITIVE_INFINITY}.
     * @param drawToTeeCurl
     *            MUST be 0
     */
    public SlideNoCurl(final double drawToTeeTime, final double drawToTeeCurl) {
        if (drawToTeeCurl != 0)
            throw new IllegalArgumentException("Curl must be zero!");
        if (Double.isInfinite(drawToTeeTime) && drawToTeeTime > 0)
            beta = drawToTeeV0 = 0;
        else {
            beta = Ice.FAR_HOG_2_TEE / MathVec.sqr(drawToTeeTime);
            drawToTeeV0 = 2 * Ice.FAR_HOG_2_TEE / drawToTeeTime;
        }
    }

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
    public AffineTransform computeRc2Wc(final AffineTransform ret,
            final Point2D x, final Point2D v) {
        return CurveTransformed.createRc2Wc(ret, x, v);
    }

    /**
     * Equations of motion:
     * <p>
     * <code>x(t) = 0 <br />
     y(t) = v_0 t - beta t^2 <br />
     alpha(t) = alpha_0 + omega_0 t</code>
     * </p>
     * 
     * @param alpha0
     * @param v0
     * @return The trajectory in rock coordinates.
     */
    Polynome[] computeRcPoly(final double alpha0, final double v0,
            final double omega0) {
        final double[] x = { 0, 0 };
        final double[] y = { 0, v0, -beta };
        final double[] a = { alpha0, omega0 };
        final Polynome[] ret = { new Polynome(x), new Polynome(y),
                new Polynome(a) };
        return ret;
    }

    public CurveRock computeRc(final Rock x0, final Rock v0) {
        return new CurveRockAnalytic(computeRcPoly(x0.getZ(),
                MathVec.abs2D(v0), v0.getZ()));
    }

    /**
     * Compute the (absolute) speed at the hog line for a rock released with
     * given split-time.
     * <p>
     * <code>v_0 = {@link Ice#BACK_2_HOG} / t_S - beta t_S</code>
     * </p>
     * 
     * @param splitTime
     * @return the hog speed.
     */
    public double computeV0(final double splitTime) {
        return Ice.BACK_2_HOG / splitTime - beta * splitTime;
    }

    public double getDrawToTeeCurl() {
        return 0;
    }

    public double getDrawToTeeTime() {
        return Math.sqrt(Ice.FAR_HOG_2_TEE / beta);
    }

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
