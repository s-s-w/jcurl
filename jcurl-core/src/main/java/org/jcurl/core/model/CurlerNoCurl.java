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

import java.util.Map;

import org.jcurl.core.base.CurlerBase;
import org.jcurl.core.base.CurveRock;
import org.jcurl.core.base.CurveRockAnalytic;
import org.jcurl.core.base.IceSize;
import org.jcurl.core.base.PropModelHelper;
import org.jcurl.core.base.Rock;
import org.jcurl.core.helpers.DimVal;
import org.jcurl.math.MathVec;
import org.jcurl.math.Polynome;
import org.jcurl.math.PolynomeCurve;

/**
 * This is not a realistic curl model but rather a baseline for the development
 * and implementation of others.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class CurlerNoCurl extends CurlerBase {

    transient double beta;

    transient double drawToTeeV0;

    public CurlerNoCurl() {
    }

    /**
     * Computes the internal ice friction coefficient as:
     * <p>
     * <code>beta = {@link IceSize#FAR_HOG_2_TEE} / drawToTeeTime^2</code>
     * </p>
     * 
     * @param drawToTeeTime
     *            may be {@link Double#POSITIVE_INFINITY}.
     * @param drawToTeeCurl
     *            MUST be 0
     */
    public CurlerNoCurl(final double drawToTeeTime, final double drawToTeeCurl) {
        final Map<CharSequence, DimVal> t = PropModelHelper.create();
        PropModelHelper.setDrawToTeeTime(t, drawToTeeTime);
        PropModelHelper.setDrawToTeeCurl(t, drawToTeeCurl);
        init(t);
    }

    public CurlerNoCurl(final Map<CharSequence, DimVal> ice) {
        init(ice);
    }

    @Override
    public CurveRock computeRc(final Rock x0, final Rock v0, final double sweepFactor) {
        return new CurveRockAnalytic(new PolynomeCurve(computeRcPoly(x0.getZ(),
                MathVec.abs2D(v0), v0.getZ(), sweepFactor)));
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
     * @param sweepFactor TODO
     * @return The trajectory in rock coordinates.
     */
    Polynome[] computeRcPoly(final double alpha0, final double v0,
            final double omega0, final double sweepFactor) {
        final double[] x = { 0 };
        final double[] y = { 0, v0, -beta };
        final double[] a = { alpha0, omega0 };
        final Polynome[] ret = { new Polynome(x), new Polynome(y),
                new Polynome(a) };
        return ret;
    }

    @Override
    public double computeV0(final double intervalTime) {
        return IceSize.BACK_2_HOG / intervalTime - beta * intervalTime;
    }

    @Override
    public double getDrawToTeeCurl() {
        return 0;
    }

    @Override
    public double getDrawToTeeTime() {
        return Math.sqrt(IceSize.FAR_HOG_2_TEE / beta);
    }

    void init(final double drawToTeeTime, final double drawToTeeCurl) {
        if (drawToTeeCurl != 0)
            throw new IllegalArgumentException("Curl must be zero!");
        if (Double.isInfinite(drawToTeeTime) && drawToTeeTime > 0)
            beta = 0;
        else {
            beta = IceSize.FAR_HOG_2_TEE / MathVec.sqr(drawToTeeTime);
            drawToTeeV0 = 2 * IceSize.FAR_HOG_2_TEE / drawToTeeTime;
        }
    }

    @Override
    public void init(final Map<CharSequence, DimVal> ice) {
        internalInit(ice);
        init(PropModelHelper.getDrawToTeeTime(params), PropModelHelper
                .getDrawToTeeCurl(params));
    }
}
