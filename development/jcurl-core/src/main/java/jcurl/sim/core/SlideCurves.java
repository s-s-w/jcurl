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

import jcurl.core.dto.RockSetProps;

import org.apache.commons.logging.Log;
import org.jcurl.core.PositionSet;
import org.jcurl.core.Rock;
import org.jcurl.core.RockSet;
import org.jcurl.core.SpeedSet;
import org.jcurl.core.helpers.JCLoggerFactory;
import org.jcurl.math.CurveBase;
import org.jcurl.math.CurveCombined;
import org.jcurl.math.CurveInterval;
import org.jcurl.math.MathVec;
import org.jcurl.math.Polynome;

/**
 * Abstract base class for analytic (non-discrete) curl models. Based on rock
 * trajectories in {@link org.jcurl.math.CurveBase}-form.
 * 
 * @see org.jcurl.math.CurveBase
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public abstract class SlideCurves extends SlideStrategy {

    private static final Log log = JCLoggerFactory
            .getLogger(SlideCurves.class);

    private static double findThalt(final double t0, final CurveBase cu) {
        final double thx = cu.computeNewtonZero(0, 1, t0);
        final double thy = cu.computeNewtonZero(1, 1, t0);
        final double thalt = thx > thy ? thx : thy;
        if (log.isDebugEnabled())
            log.debug("thalt=" + thalt);
        return thalt;
    }

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
    protected static void transformRc2Wc(final Point2D x0, final Point2D e,
            final double[] xt, final Polynome[] p) {
        // x(t) = x0 + e * x(t)
        double[] tmp = MathVec.mult(e.getX(), xt, null);
        tmp[0] += x0.getX();
        p[0] = new Polynome(tmp);
        tmp = MathVec.mult(e.getY(), xt, null);
        tmp[0] += x0.getY();
        p[1] = new Polynome(tmp);
    }

    private final CurveCombined[] c = new CurveCombined[RockSet.ROCKS_PER_SET];

    public SlideCurves() {
        super();
    }

    /**
     * Create a new trajectory curve for the given initial state.
     * 
     * @param t0
     *            [sec]
     * @param pos
     * @param speed
     * @return a 3-dimensional curve (x,y,alpha)
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
     * Get the n-th derivative. Used e.g. by {@link SlideStrategy#getPos()},
     * {@link SlideStrategy#getSpeed()}.
     * 
     * @param c
     *            0: value, 1: speed
     * @param time
     *            [sec]
     * @param rocks
     * @return the c'th derivative of x,y,alpha
     */
    protected RockSet getC(final int c, final double time, RockSet rocks) {
        for (int i = PositionSet.ROCKS_PER_SET - 1; i >= 0; i--)
            getC(c, time, i, rocks.getRock(i));
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

    public final void reset(final PositionSet startPos,
            final SpeedSet startSpeed, final RockSetProps props) {
        for (int i = PositionSet.ROCKS_PER_SET - 1; i >= 0; i--)
            c[i] = new CurveCombined(3);
        super.reset(startPos, startSpeed, props);
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
    public void set(final double t0, final PositionSet pos,
            final SpeedSet speed, final int discontinuous) {
        if (log.isDebugEnabled())
            log.debug("t0=" + t0 + " rockmask="
                    + Integer.toBinaryString(discontinuous));
        if (t0 <= tmax)
            throw new IllegalArgumentException("t must grow!");
        if (tmin == T0)
            tmin = t0;
        tmax = t0;

        for (int i = PositionSet.ROCKS_PER_SET - 1; i >= 0; i--) {
            if (0 != (discontinuous & (1 << i))) {
                log.info("compute rock #" + i);
                // add a new curve to the list
                final CurveBase cu = createCurve(t0, pos.getRock(i), speed
                        .getRock(i));
                c[i].add(t0, new CurveInterval(t0, findThalt(t0, cu), cu));
            }
        }
    }
}