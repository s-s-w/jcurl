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

import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import org.apache.commons.logging.Log;
import org.jcurl.core.base.ComputedTrajectorySet;
import org.jcurl.core.base.TestShowBase;
import org.jcurl.core.base.TrajectorySet;
import org.jcurl.core.base.Zoomer;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.core.model.CurveManager;
import org.jcurl.core.model.CurveManagerTest;
import org.jcurl.core.model.FixpointZoomer;

public class TrajectoryDisplayTest extends TestShowBase {
    private static final Log log = JCLoggerFactory
            .getLogger(TrajectoryDisplayTest.class);

    protected final TrajectoryDisplay display;

    public TrajectoryDisplayTest() {
        if (frame == null) {
            display = null;
            return;
        }
        frame.getContentPane().removeAll();
        frame.getContentPane().add(display = new TrajectoryDisplay());
    }

    public void _testThroughPut() {
        final int dt = 5000;
        final Graphics g = new BufferedImage(1024 * 2, 768 * 2,
                BufferedImage.TYPE_INT_ARGB).getGraphics();
        final TrajectorySet p = CurveManagerTest.initHammy(null);
        final int frames = showTrajectoryDisplay(p, FixpointZoomer.HOG2HACK,
                dt, new TimeRunnable() {
                    @Override
                    public void run(final double t) throws InterruptedException {
                        throw new UnsupportedOperationException();
                    }

                    @Override
                    public void run(final double t, final Component jp)
                            throws InterruptedException {
                        jp.paint(g);
                    }
                });
        if (frame != null) {
            System.out.println(getClass().getName() + " frequency: " + frames
                    * 1000L / (double) dt + " frames per second");
            // System.out.println(frames + " computations took " + dt
            // + " millis, i.e. " + frames * 1000L / dt + " per second.");
        }
    }

    public int showTrajectoryDisplay(final TrajectorySet p, final Zoomer zoom,
            final long millis, final TimeRunnable r) {
        if (frame == null)
            return -1;
        display.setZoom(zoom);
        display.setPos(p);
        frame.setVisible(true);

        final long t0 = System.currentTimeMillis();
        int loop = 0;
        try {
            while (System.currentTimeMillis() - t0 < millis) {
                r.run(1e-3 * (System.currentTimeMillis() - t0), display);
                loop++;
            }
        } catch (final InterruptedException e) {
            log.warn("Oops", e);
        }
        frame.setVisible(false);
        return loop;
    }

    public void testHammy() throws InterruptedException {
        final ComputedTrajectorySet te = CurveManagerTest
                .initHammy(new CurveManager());

        // Raw throughput:
        final long t0 = System.currentTimeMillis();
        te.setCurrentTime(25);
        te.setCurrentTime(0);
        log.info("Initial computation took "
                + (System.currentTimeMillis() - t0) + " millis");

        // with Display:
        showTrajectoryDisplay(te, FixpointZoomer.HOUSE, 7500,
                new TimeRunnable() {
                    @Override
                    public void run(final double t) throws InterruptedException {
                        te.setCurrentTime(t);
                        Thread.sleep(1000 / 50);
                    }
                });
    }
}
