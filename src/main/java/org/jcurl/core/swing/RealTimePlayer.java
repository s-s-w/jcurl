/*
 * jcurl curling simulation framework http://www.jcurl.org
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
package org.jcurl.core.swing;

import org.jcurl.core.base.PositionSet;
import org.jcurl.core.base.RockSet;
import org.jcurl.core.base.Source;
import org.jcurl.core.base.SpeedSet;
import org.jcurl.core.base.TargetDiscrete;

/**
 * Extract locations from a (non-discrete) {@link org.jcurl.core.base.Source}and
 * push them into a {@link TargetDiscrete}.
 * 
 * @see org.jcurl.core.base.Source
 * @see org.jcurl.core.base.TargetDiscrete
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:RealTimePlayer.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public class RealTimePlayer implements Runnable {

    private final TargetDiscrete dst;

    private final Source src;

    private final double t0Start;

    private double tNow;

    private double t0Last;

    private volatile double timeScale;

    private final long timeSleep = 25;

    public RealTimePlayer(final double t0, final double scale,
            final Source src, final TargetDiscrete dst) {
        t0Start = t0Last = tNow = t0;
        timeScale = scale;
        this.src = src;
        this.dst = dst;
    }

    /**
     * @return Returns the timeScale.
     */
    public double getTimeScale() {
        return timeScale;
    }

    /**
     * 
     * @see java.lang.Runnable#run()
     */
    public void run() {
        try {
            PositionSet pos = PositionSet.allHome(null);
            SpeedSet speed = new SpeedSet();
            final long start = System.currentTimeMillis();
            for (;;) {
                final long dt = System.currentTimeMillis() - start;
                tNow = t0Last + dt * timeScale * 1e-3;
                // get the position
                src.setTime(tNow);
                pos = src.getPos();
                // push it to the target
                if (dst != null)
                    dst.setPos(pos);
                speed = src.getSpeed();
                if (0 == RockSet.nonZero(speed)) {
                    t0Last = t0Start;
                    break;
                }
                try {
                    Thread.sleep(timeSleep);
                } catch (final InterruptedException e) {
                    t0Last = tNow;
                    break;
                }
            }
        } catch (final Exception e) {
            System.err.println(e.toString());
            e.printStackTrace(System.err);
        }
    }

    /**
     * @param timeScale
     *            The timeScale to set.
     */
    public void setTimeScale(final double timeScale) {
        t0Last = tNow;
        this.timeScale = timeScale;
    }
}