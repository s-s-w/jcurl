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

import org.apache.log4j.Logger;

import jcurl.core.Rock;
import jcurl.core.RockSet;
import jcurl.core.Source;
import jcurl.core.dto.RockProps;
import jcurl.core.dto.RockSetProps;
import jcurl.math.MathVec;
import jcurl.math.Polynome;
import jcurl.sim.model.SlideStraight;

/**
 * Abstract base class for propagation/friction models.
 * 
 * @see jcurl.sim.core.SlideStrategyTest
 * @see jcurl.sim.core.RunComputer
 * @see jcurl.sim.core.CollissionStrategy
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public abstract class SlideStrategy implements Source {
    private static final Logger log = Logger.getLogger(SlideStrategy.class);

    protected static double hypot(final double a, final double b) {
        return Math.sqrt(a * a + b * b);
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

    protected static double sqr(final double a) {
        return (a * a);
    }

    /**
     * Compute the time until two rocks hit. Return {@link Long#MAX_VALUE}/1000
     * if never.
     * 
     * @return seconds
     */
    protected static double timetilhit(final int a, final Rock ap,
            final Rock av, final int b, final Rock bp, final Rock bv) {
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

    static double tst_timetilhit(final int a, final Rock ap, final Rock av,
            final int b, final Rock bp, final Rock bv) {
        return timetilhit(a, ap, av, b, bp, bv);
    }

    protected final CollissionStrategy coll;

    protected int dt = 50;

    protected RockSet maxPos = RockSet.allZero(null);

    protected RockSet maxSpeed = RockSet.allZero(null);

    protected int rocksInMotion = 0;

    protected final long T0 = -1;

    protected long tmax = -1;

    protected long tmin = -1;

    protected SlideStrategy(final CollissionStrategy coll) {
        this.coll = coll;
        setDraw2Tee(23, 1.0);
    }

    /**
     * Walk on until the given time.
     * 
     * @param time
     * @param dt
     *            intervals
     */
    protected void computeUntil(final long time, final long dt) {
        if (log.isDebugEnabled())
            log.debug("t=" + time + " dt=" + dt);
        // convert seconds to milliseconds and slowly approach hits
        final double fact = 0.95 * 1e3;
        // check: is the time known already?
        while (time > tmax) {
            checkHit: for (;;) {
                // slowly approach the hit
                approach: for (;;) {
                    // check the next hit
                    final long dtHit = (long) (fact * this.estimateNextHit(
                            maxPos, maxSpeed));
                    if (log.isDebugEnabled())
                        log.debug("tmax=" + tmax + " dtHit=" + dtHit);
                    if (dt < dtHit)
                        break checkHit;
                    if (dtHit == 0)
                        break approach;
                    if (dtHit <= 0)
                        throw new IllegalStateException("dtHit=" + dtHit
                                + " is in the past!");
                    // move til hit
                    int mov = move(tmax, tmax + dtHit, maxPos, maxSpeed);
                    if (mov != rocksInMotion)
                        set(tmax + dtHit, maxPos, maxSpeed, mov ^ rocksInMotion);
                    else
                        tmax += dtHit;
                    rocksInMotion = mov;
                }
                // compute the hit
                final int hit = coll.compute(maxPos, maxSpeed);
                set(tmax + 1, maxPos, maxSpeed, hit);
                rocksInMotion |= hit;
            }
            // move on
            final int mov = move(tmax, tmax + dt, maxPos, maxSpeed);
            if (mov != rocksInMotion)
                set(tmax + dt, maxPos, maxSpeed, mov ^ rocksInMotion);
            else
                tmax += dt;
            rocksInMotion = mov;
        }
    }

    /**
     * Test all combinations of the given rocks for upcoming collissions.
     * 
     * @see SlideStrategy#timetilhit(Rock, Rock, Rock, Rock)
     * @param pos
     * @param speed
     * @return seconds until the next hit
     */
    public double estimateNextHit(final RockSet pos, final RockSet speed) {
        if (log.isDebugEnabled())
            log.debug("estimateNextHit");
        double t = Long.MAX_VALUE / 1000;
        final RockSet poss = pos;
        // test combination of all rocks:
        for (int a = 0; a < RockSet.ROCKS_PER_SET; a++) {
            final Rock ap = poss.getRock(a);
            final Rock av = speed.getRock(a);
            for (int b = 0; b < a; b++) {
                final Rock bp = poss.getRock(b);
                final Rock bv = speed.getRock(b);
                final double t1 = timetilhit(a, ap, av, b, bp, bv);
                if (t1 >= 0 && t1 < t)
                    t = t1;
            }
        }
        return t;
    }

    protected abstract RockSet getC(int c, long time, RockSet rocks);

    /**
     * Guess the initial speed.
     * 
     * @param y0
     *            start position (y) [meter]
     * @param Trun
     *            from Hog to Hog. [seconds] see ./doc/eiszeit.tex for details.
     */
    public abstract double getInitialSpeed(final double y0, final double Trun);

    /**
     * Not supported.
     */
    public long getMaxT() {
        throw new UnsupportedOperationException("Not supported");
    }

    public long getMinT() {
        return tmin;
    }

    /** Query the friction. */
    public abstract double getMu();

    public final RockSet getPos(final long time, RockSet rocks) {
        if (log.isDebugEnabled())
            log.debug("t=" + time);
        computeUntil(time, dt);
        return getC(0, time, rocks);
    }

    public final RockSet getSpeed(final long time, RockSet rocks) {
        if (log.isDebugEnabled())
            log.debug("t=" + time);
        computeUntil(time, dt);
        return getC(1, time, rocks);
    }

    /**
     * Checks if the rock is out of play.
     * 
     * @param x
     * @param v
     * @return
     */
    protected boolean isOut(final Rock x, final Rock v) {
        return false;
    }

    /**
     * Move one single rock without any checks.
     * 
     * @param t0
     * @param t1
     * @param idx
     *            rock index
     * @param pos
     * @param speed
     * @return <code>true</code> the rock moves at t1
     */
    protected abstract boolean move(final long t0, final long t1, int idx,
            final Rock pos, final Rock speed);

    /**
     * Generic mover. Checks each rock if still in play and motion and calls
     * {@link SlideStraight#move(long, long, int, Rock, Rock)}if so.
     * 
     * @param t0
     * @param t1
     * @param pos
     * @param speed
     * @return bitmask of the rocks in motion at t1
     */
    protected int move(final long t0, final long t1, final RockSet pos,
            final RockSet speed) {
        if (log.isDebugEnabled())
            log.debug("t0=" + t0 + " t1=" + t1);
        int ret = 0;
        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--) {
            final Rock x = pos.getRock(i);
            final Rock v = speed.getRock(i);
            if (isOut(x, v))
                ;
            if (move(t0, t1, i, x, v))
                ret |= 1 << i;
        }
        return ret;
    }

    public void reset(long startTime, RockSet startPos, RockSet startSpeed,
            RockSetProps props) {
        tmin = tmax = T0;
        set(startTime, startPos, startSpeed, RockSet.ALL_MASK);
        rocksInMotion = 0;
        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--) {
            final Rock v = startSpeed.getRock(i);
            if (v.getX() != 0 || v.getY() != 0)
                rocksInMotion |= 1 << i;
        }
    }

    /**
     * Add a discontinuity.
     * 
     * @param t0
     *            [msec]
     * @param pos
     * @param speed
     * @param discontinuous
     *            bitmask of the discontinuous rocks
     */
    protected abstract void set(final long t0, final RockSet pos,
            final RockSet speed, final int discontinuous);

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