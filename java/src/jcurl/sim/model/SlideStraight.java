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

import jcurl.core.Rock;
import jcurl.core.dto.Ice;
import jcurl.core.dto.RockProps;
import jcurl.math.CurveBase;
import jcurl.math.CurveFkt;
import jcurl.math.Polynome;

/**
 * Model without curl and with constant acceleration.
 * 
 * @see jcurl.sim.model.SlideStraightTest
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class SlideStraight extends SlideAnalytic {

    private long tknown = 0;

    private double accel = 0;

    public SlideStraight() {
        setDraw2Tee(23, 0);
    }

    /**
     * Override!!!
     * 
     * @param t0
     * @param pos
     * @param speed
     * @return
     */
    protected CurveBase createCurve(double t0, Rock pos, Rock speed) {
        final Polynome p[] = new Polynome[3];
        p[0] = Polynome.getPoly(t0, pos.getX(), speed.getX(), accel);
        p[1] = Polynome.getPoly(t0, pos.getY(), speed.getY(), accel);
        p[2] = Polynome.getPoly(t0, pos.getZ(), speed.getZ(), 0);
        return new CurveFkt(p);
    }

    /**
     * Guess the initial speed.
     * 
     * @param y0
     *            Start
     * @param Trun
     *            from Hog to Hog. see ./doc/eiszeit.tex for details.
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
        accel = -2.0 * (0 - Ice.FAR_HOG_2_TEE) / sqr(time);
    }
}