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

import jcurl.core.Rock;
import jcurl.core.RockSet;
import jcurl.core.RockSetInterpolator;
import jcurl.core.Source;
import jcurl.core.dto.Ice;
import jcurl.core.dto.RockSetProps;

/**
 * Brings together {@link jcurl.sim.core.SlideStrategy},
 * {@link jcurl.sim.core.CollissionStrategy}and
 * {@link jcurl.core.RockSetInterpolator}.
 * 
 * @see jcurl.sim.core.RunComputerTest
 * @see jcurl.sim.core.SlideStrategy
 * @see jcurl.sim.core.CollissionStrategy
 * @see jcurl.core.RockSetInterpolator
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id: RunComputer.java 13 2005-03-05 22:58:41Z mrohrmoser $
 */
public class RunComputer implements Source {

    private static RockSet copy(final RockSet src, final RockSet dst) {
        if (dst == null)
            return (RockSet) src.clone();
        if (dst == src)
            return dst;
        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--) {
            final Rock _dst = dst.getRock(i);
            final Rock _src = src.getRock(i);
            _dst.setLocation(_src);
        }
        return dst;
    }

    private RockSet currPos = null;

    private RockSet currSpeed = null;

    private long currTime = 0;

    private final CollissionStrategy hitter;

    private final RockSetInterpolator ipol;

    // break bigger intervals down to this:
    private final long maxDt = 50;

    private final SlideStrategy slider;

    public RunComputer(final SlideStrategy slider,
            final CollissionStrategy hitter, final RockSetInterpolator ipol,
            final RockSetProps props, final long startTime,
            final RockSet startPos, final RockSet speed) {
        this.slider = slider;
        this.hitter = hitter;
        this.ipol = ipol;
        reset(startTime, startPos, speed, props);
    }

    public long getMaxT() {
        return ipol.getMaxT();
    }

    public long getMinT() {
        return ipol.getMinT();
    }

    public RockSet getPos(final long time, final RockSet rocks) {
        // convert seconds to milliseconds and slooowly approach hits
        final double fact = 0.9 * 1e3;
        // check if the interpolator knows about the time
        if (time > ipol.getMaxT()) {
            noHit: for (;;) {
                // slowly approach the hit
                inner: for (;;) {
                    // check the next hit
                    final long nextH = currTime
                            + (long) (fact * slider.estimateNextHit(currPos,
                                    currSpeed));
                    if (time < nextH)
                        break noHit;
                    if (currTime == nextH)
                        break inner;
                    // move til hit
                    move(nextH);
                }
                // compute the hit
                final int hit = hitter.compute(currPos, currSpeed);
                if (0 != hit)
                    ipol.setPos(++currTime, currPos, hit);
            }
            // move the rest of the time
            move(time);
        }
        if (time == currTime)
            return copy(currPos, rocks);
        return ipol.getPos(time, rocks);
    }

    public RockSet getSpeed(final long time, RockSet rocks) {
        if (time > ipol.getMaxT())
            getPos(time, currPos);
        if (time == currTime)
            return copy(currSpeed, rocks);
        return ipol.getSpeed(time, rocks);
    }

    public boolean isDiscrete() {
        return slider.isDiscrete();
    }

    public boolean isForwardOnly() {
        return false;
    }

    public boolean isWithSpeed() {
        return true;
    }

    private final void move(final long t1) {
        for (long t = currTime; t < t1; t += maxDt) {
            slider.getPos(t, currPos);
            slider.getSpeed(t, currSpeed);
            ipol
                    .setPos(currTime = t, currPos, Ice.checkOut(currPos,
                            currSpeed));
        }
        slider.getSpeed(t1, currSpeed);
        slider.getPos(t1, currPos);
        ipol.setPos(currTime = t1, currPos, Ice.checkOut(currPos, currSpeed));
    }

    public void reset(long startTime, final RockSet startPos,
            final RockSet startSpeed, final RockSetProps props) {
        currPos = startPos;
        currSpeed = startSpeed;
        currTime = startTime;
        slider.reset(currTime, currPos, currSpeed, props);
        if (ipol != null)
            ipol.reset(currTime, currPos, currSpeed, props);
    }
}