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
package jcurl.core.gui;

import jcurl.core.RockSet;
import jcurl.core.Source;
import jcurl.core.TargetDiscrete;

/**
 * Extract locations from a (non-discrete) {@link jcurl.core.Source}and push
 * them into a {@link TargetDiscrete}.
 * 
 * @see jcurl.core.Source
 * @see jcurl.core.TargetDiscrete
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class RealTimePlayer implements Runnable {

    private final TargetDiscrete dst;

    private final Source src;

    private final long t0Start;

    private long tNow;

    private long t0Last;

    private volatile double timeScale;

    private final long timeSleep = 40;

    public RealTimePlayer(final long t0, final double scale, final Source src,
            final TargetDiscrete dst) {
        this.t0Start = t0Last = tNow = t0;
        this.timeScale = scale;
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
            RockSet pos = null;
            RockSet speed = null;
            final long start = System.currentTimeMillis();
            for (;;) {
                final long dt = System.currentTimeMillis() - start;
                tNow = t0Last + (long) (dt * timeScale);
                // get the position
                pos = src.getPos(tNow, pos);
                // push it to the target
                if (dst != null)
                    dst.setPos(tNow, pos);
                speed = src.getSpeed(tNow, speed);
                if (0 == RockSet.nonZero(speed)) {
                    t0Last = t0Start;
                    break;
                }
                try {
                    Thread.sleep(timeSleep);
                } catch (InterruptedException e) {
                    t0Last = tNow;
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println(e.toString());
            e.printStackTrace(System.err);
        }
    }

    /**
     * @param timeScale
     *            The timeScale to set.
     */
    public void setTimeScale(double timeScale) {
        t0Last = tNow;
        this.timeScale = timeScale;
    }
}