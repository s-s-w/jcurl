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
package org.jcurl.core.curved;

import org.apache.commons.logging.Log;
import org.jcurl.core.base.Ice;
import org.jcurl.core.base.RockProps;
import org.jcurl.core.base.RockSet;
import org.jcurl.core.helpers.JCLoggerFactory;
import org.jcurl.core.math.MathException;
import org.jcurl.core.math.Polynome;
import org.jcurl.core.math.PolynomeImpl;

/**
 * ComputedSource without curl and with constant acceleration.
 * <p>
 * Public access to rock locations and speed via
 * {@link jcurl.sim.core.SlideCurves#getC(int, double, RockSet)}. The internal
 * computation (including hit-, out- and stillstand check) is via
 * {@link jcurl.sim.core.SlideStrategy#computeUntil(double, double)}.
 * 
 * </p>
 * 
 * @see jcurl.sim.model.SlideStraightTest
 * @see jcurl.sim.model.SlideDenny
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class CurlerStraight extends CurlerCurved {

    private static final Log log = JCLoggerFactory
            .getLogger(CurlerStraight.class);

    private double accel;

    public CurlerStraight() {
        super();
    }

    public CurveRock computeRC(double vabs, double omega) {
        final Polynome p;
        if (vabs >= 0)
            p = new PolynomeImpl(PolynomeImpl.getPolyParams(0, 0, vabs, accel));
        else
            p = new PolynomeImpl(PolynomeImpl.getPolyParams(0, 0, 0, 0));

        return new CurveRockBase() {
            protected double value(double t, int derivative, int component)
                    throws MathException {
                if (component == 0)
                    return p.value(t, derivative);
                return 0;
            }
        };
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

    public double getTeeCurl() {
        return 0;
    }

    public double getTeeTime() {
        return 0;
    }

    /**
     * Set the draw-to-T-time and curl.
     * 
     * @param time
     * @param curl
     *            unused
     */
    public void setTeeCurl(final double curl) {
    }

    /**
     * Set the draw-to-T-time and curl.
     * 
     * @param time
     * @param curl
     *            unused
     */
    public void setTeeTime(final double time) {
        accel = -2.0 * Ice.FAR_HOG_2_TEE / sqr(time);
    }
}