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
package org.jcurl.core.model;

import org.jcurl.core.base.CurveRock;
import org.jcurl.core.base.CurveRockAnalytic;
import org.jcurl.core.base.Ice;
import org.jcurl.core.base.Rock;
import org.jcurl.core.base.SlideBase;
import org.jcurl.math.MathVec;
import org.jcurl.math.Polynome;

/**
 * This is not a realistic curl model but rather a baseline for the development
 * and implementation of others.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class SlideNoCurl extends SlideBase {

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

    @Override
    public CurveRock computeRc(final Rock x0, final Rock v0) {
        return new CurveRockAnalytic(computeRcPoly(x0.getZ(),
                MathVec.abs2D(v0), v0.getZ()));
    }

    @Override
    public double computeV0(final double intervalTime) {
        return Ice.BACK_2_HOG / intervalTime - beta * intervalTime;
    }

    @Override
    public double getDrawToTeeCurl() {
        return 0;
    }

    @Override
    public double getDrawToTeeTime() {
        return Math.sqrt(Ice.FAR_HOG_2_TEE / beta);
    }
}
