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

import jcurl.core.NotImplementedYetException;
import jcurl.core.Rock;
import jcurl.core.RockSet;
import jcurl.core.dto.Ice;
import jcurl.sim.core.CollissionStrategy;
import jcurl.sim.core.SlideStrategy;

/**
 * Move rocks straight and without acceleration (friction)
 * 
 * @see jcurl.sim.core.model.SlideSimpleTest
 * @see jcurl.sim.core.RunComputer
 * @see jcurl.sim.core.model.CollissionSimple
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class SlideSimple extends SlideStrategy {

    public SlideSimple(final CollissionStrategy coll) {
        super(coll);
    }

    /**
     * Move rocks and don't care about hits.
     * 
     * @param dt
     */
    protected void computeDt(final double t1) {
        final double dt = t1 - tmax;
        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--) {
            computeDt(maxPos.getRock(i), maxSpeed.getRock(i), t1, dt, i);
        }
        tmax = t1;
    }

    /**
     * Move a single rock according to the given time.
     * 
     * @param pos
     * @param speed
     * @param dt
     * @param idx
     *            Rock index
     * @return '1' if the rock 'r' is still in motion <b>after </b> the given
     *         period 'dt', '0' otherwise.
     */
    protected int computeDt(final Rock pos, final Rock speed, double tEnd,
            final double dt, int idx) {
        pos.setX(pos.getX() + speed.getX() * dt);
        pos.setY(pos.getY() + speed.getY() * dt);
        pos.setZ(pos.getZ() + speed.getZ() * dt);
        return 1;
    }

    /**
     * @return seconds
     */
    public double estimateNextHit(double t) {
        return estimateNextHit(maxPos, maxSpeed);
    }

    protected RockSet getC(int c, double time, RockSet rocks) {
        throw new NotImplementedYetException();
    }

    /**
     * Guess the initial speed.
     * 
     * @param y0
     *            unused
     * @param Trun
     *            from Hog to Hog. see ./doc/eiszeit.tex for details.
     */
    public double getInitialSpeed(final double y0, final double Trun) {
        return Ice.HOG_2_HOG / Trun;
    }

    private int getInMotion(long t) {
        if (t < tmax)
            throw new IllegalArgumentException("You cannot walk back in time.");
        if (t > tmax)
            computeDt(t);
        return RockSet.nonZero(maxSpeed);
    }

    public double getMaxT() {
        throw new UnsupportedOperationException("Not supported");
    }

    public double getMinT() {
        return tmin;
    }

    /** Query the friction. */
    public double getMu() {
        return 0;
    }

    public RockSet getPosOld(final long t, RockSet rocks) {
        if (t < tmax)
            throw new IllegalArgumentException("You cannot walk back in time.");
        if (t == tmax)
            return maxPos;
        computeDt(t);
        return maxPos;
    }

    public boolean isDiscrete() {
        return true;
    }

    public boolean isForwardOnly() {
        return true;
    }

    public boolean isWithSpeed() {
        return true;
    }

    protected boolean move(double t0, double t1, int idx, Rock pos, Rock speed) {
        throw new NotImplementedYetException();
    }

    protected void set(double t0, RockSet pos, RockSet speed, int discontinuous) {
        maxPos = pos;
        maxSpeed = speed;
    }

    /**
     * Set the draw-to-T-time and curl.
     * 
     * @param time
     *            unused
     * @param curl
     *            unused
     */
    public void setDraw2Tee(final double time, final double curl) {
        ;
    }
}