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
package org.jcurl.core.base;

import org.apache.commons.logging.Log;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.core.model.CollissionSimple;
import org.jcurl.core.swing.Zoomer;

public class TrajectoryManagerTest extends TestShowBase {

    private static final Log log = JCLoggerFactory
            .getLogger(TrajectoryManagerTest.class);

    public void testFastHit() throws InterruptedException {
        final TrajectoryManager te = new TrajectoryManager();
        te.setCollider(new CollissionSimple());
        te.setCollissionDetector(new CollissionNewton());
        te.setSlider(new SlideNoCurl(23, 0));
        te.setInitialPos(PositionSet.allHome());
        te.getInitialPos().getDark(0).setLocation(0, Ice.HOG_2_TEE, 0);
        te.getInitialPos().getLight(0).setLocation(0.1, Ice.BACK_2_TEE,
                0.25 * Math.PI);
        te.setInitialSpeed(new SpeedSet());
        te.getInitialSpeed().getDark(0).setLocation(0,
                -te.getSlider().computeV0(5), Math.PI / 2);

        assertFalse(Double.isNaN(te.doGetNextHit().t));
        assertFalse((1 > te.doGetNextHit().t));

        // Raw throughput:
        final int loops = 10000;
        final long t0 = System.currentTimeMillis();
        for (int i = loops; i > 0; i--)
            te.setCurrentTime(1e-3 * i);
        log.info(loops + " computations took "
                + (System.currentTimeMillis() - t0) + " millis, i.e. " + loops
                * 1000 / (System.currentTimeMillis() - t0) + " per second.");

        // with Display:
        showPositionDisplay(te.getCurrentPos(), Zoomer.HOUSE, 5000,
                new TimeRunnable() {
                    public void run(final double t) throws InterruptedException {
                        te.setCurrentTime(t);
                        Thread.sleep(20);
                    }
                });
        // FIXME Test is not ok yet!
    }

    public void testSlowNoHit() throws InterruptedException {
        final TrajectoryManager te = new TrajectoryManager();
        te.setCollider(new CollissionSimple());
        te.setCollissionDetector(new CollissionNewton());
        te.setSlider(new SlideNoCurl(23, 0));
        te.setInitialPos(PositionSet.allHome());
        te.getInitialPos().getDark(0).setLocation(0, Ice.HOG_2_TEE, Math.PI);
        te.setInitialSpeed(new SpeedSet());
        te.getInitialSpeed().getDark(0).setLocation(0,
                -te.getSlider().computeV0(9), Math.PI / 2);

        assertEquals(Double.NaN, te.doGetNextHit().t);
        assertFalse((1 > te.doGetNextHit().t));

        // Raw throughput:
        final int loops = 10000;
        final long t0 = System.currentTimeMillis();
        for (int i = loops; i > 0; i--)
            te.setCurrentTime(1e-3 * i);
        log.info(loops + " computations took "
                + (System.currentTimeMillis() - t0) + " millis, i.e. " + loops
                * 1000 / (System.currentTimeMillis() - t0) + " per second.");

        // with Display:
        showPositionDisplay(te.getCurrentPos(), Zoomer.HOUSE, 5000,
                new TimeRunnable() {
                    public void run(final double t) throws InterruptedException {
                        te.setCurrentTime(t);
                        Thread.sleep(20);
                    }
                });
        // FIXME Test is not ok yet!
    }
}
