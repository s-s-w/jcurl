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
package jcurl.sim.core;

import java.awt.Polygon;
import java.awt.geom.Point2D;

import jcurl.core.Rock;
import jcurl.core.RockSet;
import jcurl.core.Source;
import jcurl.core.dto.RockProps;
import jcurl.math.MathVec;
import jcurl.math.Polynome;

/**
 * Abstract base class for propagation/friction models.
 * 
 * @see jcurl.sim.core.RunComputer
 * @see jcurl.sim.core.CollissionStrategy
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public abstract class SlideStrategy implements Source {

    /**
     * Test all combinations of the given rocks for upcoming collissions.
     * 
     * @see SlideStrategy#timetilhit(Rock, Rock, Rock, Rock)
     * @param pos
     * @param speed
     * @return seconds
     */
    public double estimateNextHit(final RockSet pos, final RockSet speed) {
        double t = Long.MAX_VALUE / 1000;
        final RockSet poss = pos;
        // test combination of all rocks:
        for (int a = 0; a < RockSet.ROCKS_PER_SET; a++) {
            final Rock ap = poss.getRock(a);
            final Rock av = speed.getRock(a);
            for (int b = 0; b < a; b++) {
                final Rock bp = poss.getRock(b);
                final Rock bv = speed.getRock(b);
                final double t1 = timetilhit(ap, av, bp, bv);
                if (t1 < t)
                    t = t1;
            }
        }
        return t;
    }

    protected static double hypot(final double a, final double b) {
        return Math.sqrt(a * a + b * b);
    }

    protected static double sqr(final double a) {
        return (a * a);
    }

    protected static double poly(final double x, final int dim, final double[] p) {
        return Polynome.poly(x, p);
    }

    protected static byte sgn(final double a) {
        if (a > 0)
            return 1;
        if (a < 0)
            return -1;
        return 0;
    }

    /**
     * Compute the time until two rocks hit. Return {@link Long#MAX_VALUE}/1000
     * if never.
     * 
     * @return seconds
     */
    protected static double timetilhit(final Rock ap, final Rock av,
            final Rock bp, final Rock bv) {
        final double RR = sqr(RockProps.DEFAULT.getRadius()
                + RockProps.DEFAULT.getRadius());
        final Point2D x = MathVec.sub(bp, ap, new Point2D.Double());
        final Point2D v = MathVec.sub(bv, av, new Point2D.Double());
        final double vx = MathVec.scal(x, v);
        if (vx >= 0)
            return Long.MAX_VALUE / 1000;
        final double vv = MathVec.scal(v, v);
        final double xx = MathVec.scal(x, x);
        return -(vx + Math.sqrt(sqr(vx) - vv * (xx - RR))) / vv;
    }

    /**
     * Overload and typically feed
     * {@link SlideStrategy#estimateNextHit(RockSet, RockSet)}with the current
     * locations and velocities.
     * 
     * @param t
     *            TODO
     * 
     * @see SlideStrategy#estimateNextHit(RockSet, RockSet)
     * @return seconds
     */
    public abstract double estimateNextHit(long t);

    /**
     * Guess the initial speed.
     * 
     * @param y0
     *            start position (y) [meter]
     * @param Trun
     *            from Hog to Hog. [seconds] see ./doc/eiszeit.tex for details.
     */
    public abstract double getInitialSpeed(final double y0, final double Trun);

    /** Query the friction. */
    public abstract double getMu();

    /**
     * Set the draw-to-T-time and curl.
     * 
     * @param time
     *            [seconds]
     * @param curl
     *            [meter]
     */
    public abstract void setDraw2Tee(final double time, final double curl);
}