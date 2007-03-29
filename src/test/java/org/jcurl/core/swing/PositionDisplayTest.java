/*
 * jcurl curling simulation framework http://www.jcurl.org
 * Copyright (C) 2005-2007 M. Rohrmoser
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
import org.jcurl.core.base.TestShowBase;

public class PositionDisplayTest extends TestShowBase {

    public void testThroughPut() {
        final int dt = 5000;
        final PositionSet p = PositionSet.allHome();
        final int frames = showPositionDisplay(p, Zoomer.HOG2HACK, dt,
                new TimeRunnable() {
                    public void run(final double t) throws InterruptedException {
                        p.notifyChange();
                        Thread.sleep(4);
                    }
                });
        if (show)
            System.out.println(getClass().getName() + " frequency: " + frames
                    / (double) dt + " frames per second");
    }
}
