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
import jcurl.core.RockSet;
import jcurl.core.dto.RockSetProps;
import jcurl.math.CurveBase;
import jcurl.math.CurveParts;
import jcurl.sim.core.SlideStrategy;

/**
 * Abstract base class for analytic (non-discrete) curl models.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public abstract class SlideAnalytic extends SlideStrategy {

    private final CurveParts[] c = new CurveParts[RockSet.ROCKS_PER_SET];

    private long tmax = -1;

    private long tmin = -1;

    protected SlideAnalytic() {

    }

    /**
     * Create a new trajectory curve for the given initial state.
     * 
     * @param t0
     *            [seconds]
     * @param pos
     * @param speed
     * @return a 3-dimensional curve
     */
    protected abstract CurveBase createCurve(double t0, Rock pos, Rock speed);

    /**
     * Not supported.
     */
    public double estimateNextHit(long t) {
        throw new UnsupportedOperationException("Not supported.");
    }

    /**
     * Get the n-th derivative. Used e.g. by
     * {@link SlideAnalytic#getPos(long, RockSet)},
     * {@link SlideAnalytic#getSpeed(long, RockSet)}.
     * 
     * @param c
     *            0: value, 1: speed
     * @param time
     *            [msec]
     * @param rocks
     * @return
     */
    protected RockSet getC(final int c, final long time, RockSet rocks) {
        final double t = 1e-3 * time;
        final double[] v = new double[3];
        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--) {
            final Rock r = (Rock)rocks.getRock(i);
            this.c[i].getC(c, t, v);
            r.setLocation(v[0], v[1], v[2]);
        }
        return rocks;
    }

    /**
     * Not supported.
     */
    public long getMaxT() {
        throw new UnsupportedOperationException("Not supported");
    }

    public long getMinT() {
        return tmin;
    }

    public final RockSet getPos(final long time, RockSet rocks) {
        return getC(0, time, rocks);
    }

    public final RockSet getSpeed(final long time, RockSet rocks) {
        return getC(1, time, rocks);
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

    public final void reset(long startTime, RockSet startPos,
            RockSet startSpeed, RockSetProps props) {
        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--)
            c[i] = new CurveParts(3);
        tmin = tmax = -1;
        set(startTime, startPos, startSpeed, RockSet.ALL_MASK);
    }

    /**
     * Add a discontinuity.
     * 
     * @param t0 [msec]
     * @param pos
     * @param speed
     * @param discontinuous
     *            bitmask of the discontuous rocks
     */
    public void set(final long t0, final RockSet pos, final RockSet speed,
            final int discontinuous) {
        if (t0 <= tmax)
            throw new IllegalArgumentException("t must grow!");
        if (tmin == -1)
            tmin = t0;
        tmax = t0;

        final double t = 1e-3 * t0;
        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--) {
            int a = (1 << i);
            if (a == (discontinuous & a)) {
                // add a new curve to the list
                c[i].add(t, createCurve(t, pos.getRock(i), speed.getRock(i)));
            }
        }
    }
}