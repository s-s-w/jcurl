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
package jcurl.sim.model;

import java.awt.geom.Point2D;

import jcurl.core.Rock;
import jcurl.core.RockSet;
import jcurl.core.dto.Ice;
import jcurl.core.dto.RockProps;
import jcurl.math.CurveBase;
import jcurl.math.CurveFkt;
import jcurl.math.MathVec;
import jcurl.math.Polynome;
import jcurl.sim.core.CollissionStrategy;
import jcurl.sim.core.SlideAnalytic;

import org.apache.log4j.Logger;

/**
 * Model without curl and with constant acceleration.
 * <p>
 * Public access to rock locations and speed via
 * {@link SlideStraight#getC(int, double, RockSet)}. The internal computation
 * (including hit-, out- and stillstand check) is via
 * {@link SlideStraight#computeUntil(long)}.
 * 
 * </p>
 * 
 * @see jcurl.sim.model.SlideStraightTest
 * @see jcurl.sim.model.SlideDenny
 * @see jcurl.sim.core.RunComputer
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class SlideStraight extends SlideAnalytic {

    private static final Logger log = Logger.getLogger(SlideStraight.class);

    private double accel;

    public SlideStraight(final CollissionStrategy coll) {
        super(coll);
    }

    protected CurveBase createCurve(final double t0, final Rock x0,
            final Rock v0) {
        if (log.isDebugEnabled())
            log.debug("t0=" + t0);
        // get direction of movement
        final Point2D.Double v0_1 = new Point2D.Double();
        final double vabs = MathVec.abs(v0);
        final double[] p1;
        if (vabs > 0) {
            MathVec.mult(1.0 / vabs, v0, v0_1);
            // get the bewegungsgleichung
            p1 = Polynome.getPolyParams(t0, 0, vabs, accel);
        } else {
            p1 = Polynome.getPolyParams(t0, 0, 0, 0);
        }
        if (log.isDebugEnabled())
            log.debug("polynome " + p1[0] + " + " + p1[1] + "x + " + p1[2]
                    + "x^2");
        // transform it
        final Polynome p[] = new Polynome[3];
        transform(x0, v0_1, p1, p);
        p[2] = Polynome.getPoly(t0, x0.getZ(), v0.getZ(), 0);
        return new CurveFkt(p);
    }

    /**
     * Guess the initial speed.
     * 
     * @param y0
     *            [meter] Start
     * @param Trun
     *            [second] from Hog to Hog. see ./doc/eiszeit.tex for details.
     */
    public double getInitialSpeed(final double y0, final double Trun) {
        final double HF = Ice.FAR_HOG_2_TEE;
        final double HN = Ice.HOG_2_TEE;
        return Math.sqrt(2 * accel * (y0 - HF)
                + sqr(Trun * accel / 2 - (HN - HF) / Trun));
    }

    /** Query the friction. */
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
        accel = -2.0 * Ice.FAR_HOG_2_TEE / sqr(time);
    }
}