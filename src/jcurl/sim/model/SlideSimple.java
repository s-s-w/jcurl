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
import jcurl.core.dto.Ice;
import jcurl.core.dto.RockSetProps;
import jcurl.sim.core.SlideStrategy;

/**
 * Move rocks straight and without acceleration (friction)
 * 
 * @see jcurl.sim.core.model.SlideSimpleTest
 * @see jcurl.sim.core.RunComputer
 * @see jcurl.sim.core.model.CollissionSimple
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id: SlideSimple.java 13 2005-03-05 22:58:41Z mrohrmoser $
 */
public class SlideSimple extends SlideStrategy {

    /**
     * Make the function reachable for testing.
     * 
     * @param ap
     * @param av
     * @param bp
     * @param bv
     * @return seconds
     */
    double tst_timetilhit(final Rock ap, final Rock av, final Rock bp,
            final Rock bv) {
        return timetilhit(ap, av, bp, bv);
    }

    private RockSet currPos = null;

    private RockSet currSpeed = null;

    private long currTime = 0;

    private RockSetProps props = null;

    private long t0 = 0;

    /**
     * Move rocks and don't care about hits.
     * 
     * @param dt
     */
    protected void computeDt(final long t1) {
        final long dt = t1 - currTime;
        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--) {
            computeDt(currPos.getRock(i), currSpeed.getRock(i), t1, dt, i);
        }
        currTime = t1;
    }

    /**
     * Move a single rock according to the given time.
     * 
     * @param pos
     * @param speed
     * @param dt
     * @param idx
     *            TODO
     * @param t0
     *            TODO
     * @return '1' if the rock 'r' is still in motion <b>after </b> the given
     *         period 'dt', '0' otherwise.
     */
    protected int computeDt(final Rock pos, final Rock speed, long tEnd,
            final long dt, int idx) {
        pos.setX(pos.getX() + speed.getX() * dt * 1e-3);
        pos.setY(pos.getY() + speed.getY() * dt * 1e-3);
        pos.setZ(pos.getZ() + speed.getZ() * dt * 1e-3);
        return 1;
    }

    /**
     * @return seconds
     */
    public double estimateNextHit(long t) {
        return estimateNextHit(currPos, currSpeed);
    }

    private int getInMotion(long t) {
        if (t < currTime)
            throw new IllegalArgumentException("You cannot walk back in time.");
        if (t > currTime)
            computeDt(t);
        return RockSet.nonZero(currSpeed);
    }

    public long getMaxT() {
        throw new UnsupportedOperationException("Not supported");
    }

    public long getMinT() {
        return t0;
    }

    public RockSet getPos(final long t, RockSet rocks) {
        if (t < currTime)
            throw new IllegalArgumentException("You cannot walk back in time.");
        if (t == currTime)
            return currPos;
        computeDt(t);
        return currPos;
    }

    public RockSet getSpeed(final long t, RockSet rocks) {
        if (t < currTime)
            throw new IllegalArgumentException("You cannot walk back in time.");
        if (t == currTime)
            return currSpeed;
        computeDt(t);
        return currSpeed;
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

    public void reset(long startTime, RockSet startPos, RockSet startSpeed,
            RockSetProps props) {
        t0 = currTime = startTime;
        currPos = startPos;
        currSpeed = startSpeed;
        this.props = props == null ? RockSetProps.DEFAULT : props;
    }

    /** Query the friction. */
    public double getMu() {
        return 0;
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