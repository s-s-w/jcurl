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

    private final long t0;

    private final double timeScale;

    private final long timeSleep = 10;

    public RealTimePlayer(final long t0, final double scale, final Source src,
            final TargetDiscrete dst) {
        this.t0 = t0;
        this.timeScale = scale;
        this.src = src;
        this.dst = dst;
    }

    /**
     * 
     * @see java.lang.Runnable#run()
     */
    public void run() {
        final RockSet rs = RockSet.allHome();
        final long start = System.currentTimeMillis();
        // push them to the target
        for (;;) {
            final long dt = System.currentTimeMillis() - start;
            if (dt > 10000)
                break;
            final long t = t0 + (long) (dt * timeScale);
            src.getPos(t, rs);
            if (dst != null)
                dst.setPos(t, rs);
            try {
                Thread.sleep(timeSleep);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}