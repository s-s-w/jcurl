/*
 * jcurl curling simulation framework 
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
package org.jcurl.core.model;

import java.awt.geom.Point2D;


import org.apache.commons.logging.Log;
import org.jcurl.core.base.Ice;
import org.jcurl.core.base.Rock;
import org.jcurl.core.base.RockProps;
import org.jcurl.core.base.RockSet;
import org.jcurl.core.base.SlideCurves;
import org.jcurl.core.helpers.JCLoggerFactory;
import org.jcurl.math.CurveBase;
import org.jcurl.math.CurveFkt;
import org.jcurl.math.MathVec;
import org.jcurl.math.Polynome;

/**
 * ComputedSource without curl and with constant acceleration.
 * <p>
 * Public access to rock locations and speed via
 * {@link org.jcurl.core.base.SlideCurves#getC(int, double, RockSet)}. The internal
 * computation (including hit-, out- and stillstand check) is via
 * {@link org.jcurl.core.base.SlideStrategy#computeUntil(double, double)}.
 * 
 * </p>
 * 
 * @see org.jcurl.core.model.SlideStraightTest
 * @see org.jcurl.core.model.SlideDenny
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class SlideStraight extends SlideCurves {

    private static final Log log = JCLoggerFactory
            .getLogger(SlideStraight.class);

    private double accel;

    public SlideStraight() {
        super();
    }

    protected CurveBase createCurve(final double t0, final Rock x0,
            final Rock v0) {
        if (log.isDebugEnabled())
            log.debug("t0=" + t0 + " " + x0 + " " + v0);
        // get direction of movement
        final double vabs = MathVec.abs(v0);
        final double[] p1;
        final Point2D v0_1;
        if (vabs > 0) {
            v0_1 = MathVec.mult(1.0 / vabs, v0, null);
            // get the bewegungsgleichung
            p1 = Polynome.getPolyParams(t0, 0, vabs, accel);
        } else {
            v0_1 = MathVec.mult(0, v0, null);
            p1 = Polynome.getPolyParams(t0, 0, 0, 0);
        }
        if (log.isDebugEnabled())
            log.debug("untransformed : p(x) = " + p1[0] + " + " + p1[1]
                    + "*x + " + p1[2] + "*x**2");
        // transform it
        final Polynome p[] = new Polynome[3];
        transformRc2Wc(x0, v0_1, p1, p);
        // rotation remains constant
        p[2] = Polynome.getPoly(t0, x0.getZ(), v0.getZ(), 0);
        {
            log.info("x : " + p[0]);
            log.info("y : " + p[1]);
            log.info("z : " + p[2]);
        }
        return new CurveFkt(p);
    }

    public String description() {
        return "Straight movement";
    }

    double getAccel() {
        return accel;
    }

    public double getInitialSpeed(final double y0, final double Trun) {
        final double HF = Ice.FAR_HOG_2_TEE;
        final double HN = Ice.HOG_2_TEE;
        return Math.sqrt(2 * accel * (y0 - HF)
                + sqr(Trun * accel / 2 - (HN - HF) / Trun));
    }

    public double getMu() {
        return 2.0 * accel / RockProps.DEFAULT.getMass();
    }

    /**
     * Set the draw-to-T-time and curl.
     * 
     * @param time
     * @param curl
     *            unused
     */
    public void setDraw2Tee(final double time, final double curl) {
        super.setDraw2Tee(time, curl);
        accel = -2.0 * Ice.FAR_HOG_2_TEE / sqr(time);
    }
}