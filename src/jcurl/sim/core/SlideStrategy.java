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
import jcurl.core.Source;
import jcurl.core.dto.Ice;
import jcurl.core.dto.RockProps;
import jcurl.core.dto.RockSetProps;
import jcurl.math.MathVec;
import jcurl.math.Polynome;

import org.apache.log4j.Logger;

/**
 * Abstract base class for propagation/friction models.
 * 
 * @see jcurl.sim.core.SlideStrategyTest
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
     * @param a
     *            index of first rock
     * @param ap
     *            location of first rock
     * @param av
     *            speed of first rock
     * @param b
     *            index of second rock
     * @param bp
     *            location of second rock
     * @param bv
     *            speed of second rock
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

    protected double dt = 0.050;

    protected RockSet maxPos = RockSet.allZero(null);

    protected RockSet maxSpeed = RockSet.allZero(null);

    protected int rocksInMotion = 0;

    protected final double T0 = -1;

    protected double tmax = -1;

    protected double tmin = -1;

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
    protected void computeUntil(final double time, final double dt) {
        if (log.isDebugEnabled())
            log.debug("t=" + time + " dt=" + dt);
        // convert seconds to milliseconds and slowly approach hits
        final double fact = 0.95;
        final double dtHit = 1e-3;
        // check: is the time known already?
        while (time > tmax) {
            checkHit: for (;;) {
                // slowly approach the hit
                approach: for (;;) {
                    // check the next hit
                    final double dtNextHit = fact
                            * this.estimateNextHit(maxPos, maxSpeed);
                    if (log.isDebugEnabled())
                        log.debug("tmax=" + tmax + " dtNextHit=" + dtNextHit);
                    if (dt < dtNextHit)
                        break checkHit;
                    if (dtNextHit <= 0)
                        throw new IllegalStateException("dtNextHit="
                                + dtNextHit + " is in the past!");
                    if (dtNextHit < 1e-6)
                        break approach;
                    // move til hit
                    int mov = move(tmax, tmax + dtNextHit, maxPos, maxSpeed);
                    if (mov != rocksInMotion)
                        set(tmax + dtNextHit, maxPos, maxSpeed, mov
                                ^ rocksInMotion);
                    else
                        tmax += dtNextHit;
                    rocksInMotion = mov;
                }
                // compute the hit
                final int hit = coll.compute(maxPos, maxSpeed);
                set(tmax + dtHit, maxPos, maxSpeed, hit);
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
     * @see SlideStrategy#timetilhit(int, Rock, Rock, int, Rock, Rock)
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

    protected abstract RockSet getC(int c, double time, RockSet rocks);

    /**
     * Guess the initial speed.
     * 
     * @param y0
     *            start position (y) [meter]
     * @param Trun
     *            from Hog to Hog. [seconds] see ./doc/eiszeit.tex for details.
     * @return [meter/sec]
     */
    public abstract double getInitialSpeed(final double y0, final double Trun);

    /**
     * Not supported.
     * 
     * @return not supported
     */
    public double getMaxT() {
        throw new UnsupportedOperationException("Not supported");
    }

    public double getMinT() {
        return tmin;
    }

    /**
     * Query the friction.
     * 
     * @return friction coefficient
     */
    public abstract double getMu();

    public final RockSet getPos(final double time, RockSet rocks) {
        if (log.isDebugEnabled())
            log.debug("t=" + time);
        computeUntil(time, dt);
        return getC(0, time, rocks);
    }

    public final RockSet getSpeed(final double time, RockSet rocks) {
        if (log.isDebugEnabled())
            log.debug("t=" + time);
        computeUntil(time, dt);
        return getC(1, time, rocks);
    }

    /**
     * Checks if the rock is out of play.
     * 
     * @param x
     *            rock location
     * @param v
     *            rock speed
     * @return <code>true</code> the rock is out
     */
    protected boolean isOut(final Rock x, final Rock v) {
        if (x.getX() > Ice.SIDE_2_CENTER || x.getX() < -Ice.SIDE_2_CENTER)
            return true;
        if (x.getY() < -Ice.BACK_2_TEE)
            return true;
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
    protected abstract boolean move(final double t0, final double t1, int idx,
            final Rock pos, final Rock speed);

    /**
     * Generic mover. calls
     * {@link SlideStrategy#move(double, double, int, Rock, Rock)}for each rock
     * and checks if the rock is still in play afterwards.
     * 
     * @param t0
     *            [sec] start time
     * @param t1
     *            [sec] end time
     * @param pos
     *            positions
     * @param speed
     *            velocities
     * @return bitmask of the rocks in motion at t1
     */
    protected int move(final double t0, final double t1, final RockSet pos,
            final RockSet speed) {
        if (log.isDebugEnabled())
            log.debug("t0=" + t0 + " t1=" + t1);
        int ret = 0;
        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--) {
            final Rock x = pos.getRock(i);
            final Rock v = speed.getRock(i);
            if (move(t0, t1, i, x, v)) {
                // only check moving rocks.
                if (isOut(x, v)) {
                    Ice.setOut(x, (i % 2) == 0, i / 2);
                    v.setLocation(0, 0, 0);
                } else
                    ret |= 1 << i;
            }
        }
        return ret;
    }

    public void reset(double startTime, RockSet startPos, RockSet startSpeed,
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
    protected abstract void set(final double t0, final RockSet pos,
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