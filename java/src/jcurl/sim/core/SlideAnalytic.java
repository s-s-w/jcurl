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

import java.awt.geom.Point2D;

import jcurl.core.Rock;
import jcurl.core.RockSet;
import jcurl.core.dto.RockSetProps;
import jcurl.math.CurveBase;
import jcurl.math.CurveInterval;
import jcurl.math.CurveParts;
import jcurl.math.MathVec;
import jcurl.math.Polynome;

import org.apache.log4j.Logger;

/**
 * Abstract base class for analytic (non-discrete) curl models.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public abstract class SlideAnalytic extends SlideStrategy {

    private static final Logger log = Logger.getLogger(SlideAnalytic.class);

    /**
     * Transform the trajectory from speed-coordinates (the y-axis points along
     * the direction of motion) to world coordinates and seed the
     * {@link Polynome}s <code>p[0]</code> and <code>p[1]</code>.
     * 
     * @param x0
     *            initial location x(t0)
     * @param e
     *            unit vector of speed
     * @param xt
     *            trajectory x(t) in "speed-coordinates"
     * @param p
     *            array of {@link Polynome}s of which <code>p[0]</code> and
     *            <code>p[1]</code> get filled.
     */
    protected static void transform(final Point2D x0, final Point2D e,
            final double[] xt, final Polynome[] p) {
        // x(t) = x0 + e * x(t)
        double[] tmp = MathVec.mult(e.getX(), xt, null);
        tmp[0] += x0.getX();
        p[0] = new Polynome(tmp);
        tmp = MathVec.mult(e.getY(), xt, null);
        tmp[0] += x0.getY();
        p[1] = new Polynome(tmp);
    }

    private final CurveParts[] c = new CurveParts[RockSet.ROCKS_PER_SET];

    protected SlideAnalytic(final CollissionStrategy coll) {
        super(coll);
    }

    /**
     * Create a new trajectory curve for the given initial state.
     * 
     * @param t0
     *            [second]
     * @param pos
     * @param speed
     * @return a 3-dimensional curve
     */
    protected abstract CurveBase createCurve(double t0, Rock pos, Rock speed);

    protected Rock getC(final int c, final double time, final int idx,
            final Rock r) {
        final CurveBase cu = this.c[idx];
        r.setLocation(cu.getC(0, c, time), cu.getC(1, c, time), cu.getC(2, c,
                time));
        return r;
    }

    /**
     * Get the n-th derivative. Used e.g. by
     * {@link SlideStrategy#getPos(double, RockSet)},
     * {@link SlideStrategy#getSpeed(double, RockSet)}.
     * 
     * @param c
     *            0: value, 1: speed
     * @param time
     *            [msec]
     * @param rocks
     * @return
     */
    protected RockSet getC(final int c, final double time, RockSet rocks) {
        final double t = time / DT;
        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--)
            getC(c, t, i, rocks.getRock(i));
        return rocks;
    }

    public final boolean isDiscrete() {
        return false;
    }

    public final boolean isForwardOnly() {
        return false;
    }

    public final boolean isWithSpeed() {
        return true;
    }

    protected boolean move(double t0, double t1, int idx, Rock pos, Rock speed) {
        getC(0, t1, idx, pos);
        getC(1, t1, idx, speed);
        return speed.getX() != 0 || speed.getY() != 0;
    }

    public final void reset(final double startTime, final RockSet startPos,
            final RockSet startSpeed, final RockSetProps props) {
        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--)
            c[i] = new CurveParts(3);
        super.reset(startTime, startPos, startSpeed, props);
    }

    /**
     * Add a discontinuity to the model.
     * 
     * @param t0
     *            [msec]
     * @param pos
     * @param speed
     * @param discontinuous
     *            bitmask of the discontuous rocks
     */
    public void set(final double t0, final RockSet pos, final RockSet speed,
            final int discontinuous) {
        if (log.isDebugEnabled())
            log.debug("t0=" + t0 + " rocks=b"
                    + Integer.toBinaryString(discontinuous));
        if (t0 <= tmax)
            throw new IllegalArgumentException("t must grow!");
        if (tmin == T0)
            tmin = t0;
        tmax = t0;

        final double t =  t0 / DT;
        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--) {
            int a = (1 << i);
            if (a == (discontinuous & a)) {
                // add a new curve to the list
                final CurveBase cu = createCurve(t, pos.getRock(i), speed
                        .getRock(i));
                c[i].add(t, new CurveInterval(t, findThalt(t, cu), cu));
            }
        }
    }

    private static double findThalt(final double t0, final CurveBase cu) {
        final double thx = cu.computeNewtonZero(0, 1, t0);
        final double thy = cu.computeNewtonZero(1, 1, t0);
        final double thalt = thx > thy ? thx : thy;
        if (log.isDebugEnabled())
            log.debug("thalt=" + thalt);
        return thalt;
    }
}