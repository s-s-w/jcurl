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
package org.jcurl.core.model;

import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.jcurl.core.base.Ice;
import org.jcurl.core.base.PositionSet;
import org.jcurl.core.base.SpeedSet;
import org.jcurl.core.base.TestShowBase;
import org.jcurl.core.base.CenteredZoomer;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.math.R1RNFunction;

public class CurveManagerTest extends TestShowBase {

    private static final Log log = JCLoggerFactory
            .getLogger(CurveManagerTest.class);

    void showPaths(final Iterator<Iterable<Entry<Double, R1RNFunction>>> it,
            final double tmin, final double tmax) throws InterruptedException {
        if (frame == null)
            return;
        frame.setVisible(true);
        Thread.sleep(3000);
    }

    public void testFastHit() throws InterruptedException {
        final CurveManager te = new CurveManager();
        te.setCollider(new CollissionSimple());
        te.setCollissionDetector(new NewtonCollissionDetector());
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
        showPositionDisplay(te.getCurrentPos(), CenteredZoomer.HOUSE, 5000,
                new TimeRunnable() {
                    @Override
                    public void run(final double t) throws InterruptedException {
                        te.setCurrentTime(t);
                        Thread.sleep(1000 / 50);
                    }
                });
        // FIXME WC angles after collission!
        showPaths(te.getCurveStore().iterator(), 0, 10);
    }

    public void testSlowNoHit() throws InterruptedException {
        final CurveManager te = new CurveManager();
        te.setCollider(new CollissionSimple());
        te.setCollissionDetector(new NewtonCollissionDetector());
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
        showPositionDisplay(te.getCurrentPos(), CenteredZoomer.HOUSE, 5000,
                new TimeRunnable() {
                    @Override
                    public void run(final double t) throws InterruptedException {
                        te.setCurrentTime(t);
                        Thread.sleep(1000 / 50);
                    }
                });
        // FIXME at the end sit still and don't revert!
    }
}
